package com.example.fundonotesapp.model;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.fundonotesapp.R;
import com.example.fundonotesapp.view.AddNotes;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private static final String TAG = "AuthService";
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    DocumentReference documentReference;

    //StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    public final String USER_COLLECTION = "users";
    public final String NOTES_COLLECTION = "notes";
    public final String MY_NOTES_COLLECTION = "myNotes";



    public void loginUser(User user, AuthListener listner) {
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPasswod()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    listner.onAuthComplete(true, "Logged In Successfully");

                } else {
                    listner.onAuthComplete(false, "Failed to logged in");
                }
            }
        });
    }

    public void resetPassword(String email, AuthListener listener) {
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                listener.onAuthComplete(true, "Reset Link Sent To Your Email");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onAuthComplete(false, "Error...! Reset Link Is Not Sent" + e.getMessage());
            }
        });
    }

    public void registerUser(User userDetails, AuthListener listner) {

        mAuth.createUserWithEmailAndPassword(userDetails.getEmail(), userDetails.getPasswod()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                    documentReference = fstore.collection(USER_COLLECTION).document(userID);
                    Map<String, Object> userDB = new HashMap<>();
                    userDB.put("fName", userDetails.getName());
                    userDB.put("u_email", userDetails.getEmail());
                    userDB.put("u_pass", userDetails.getPasswod());
                    userDB.put("u_img", userDetails.getUri());

                    fstore.collection(USER_COLLECTION).document(mAuth.getCurrentUser().getUid())
                            .set(userDB).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                listner.onAuthComplete(true, "User Created Successfully");
                            } else {
                                listner.onAuthComplete(false, "Failed To update user data");
                            }
                        }
                    });
                } else {
                    listner.onAuthComplete(false, "Failed To Register, Please Try Again");
                }
            }
        });
    }

    public void signOut() {
        mAuth.signOut();
    }

    public void getLoginStatus(AuthListener listener) {
        if (currentUser != null) {
            mAuth.addAuthStateListener(firebaseAuth ->
                    listener.onAuthComplete(true, "User Logged In"));
        }
    }
}


