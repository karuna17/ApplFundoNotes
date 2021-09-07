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
import com.example.fundonotesapp.api.LoginListener;
import com.example.fundonotesapp.api.LoginLoader;
import com.example.fundonotesapp.api.LoginResponse;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private static final String TAG = AuthService.class.getName();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    public final String USER_COLLECTION = "users";

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
                    listner.onAuthComplete(true, "User Created Successfully");
                    addDetailsToFirestore(userDetails, listner);
                } else {
                    listner.onAuthComplete(false, "Failed To update user data");
                }
            }
        });
    }

    public void addDetailsToFirestore(User user, AuthListener listener) {
        Map<String, Object> userDB = new HashMap<>();
        userDB.put("fName", user.getName());
        userDB.put("u_email", user.getEmail());
        userDB.put("u_pass", user.getPasswod());
        userDB.put("u_img", user.getUri());

        fstore.collection(USER_COLLECTION).document(mAuth.getCurrentUser().getUid())
                .set(userDB).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onAuthComplete(true, "User Details Stored In Firestore Successfully");
                } else {
                    listener.onAuthComplete(true, "Failed To Store User Details In Firestore");
                }
            }
        });
    }

    public void getUsersDetails() {

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

    public void loginWithRestApi(User user, AuthListener listener) {
        LoginLoader.getLoginDone(new LoginListener() {
            @Override
            public void onLogin(LoginResponse response, boolean status, String message) {
                Log.d(TAG, "onLogin: Status: " + status);
                if (status) {
                    Constants.getInstance().setUserId(response.getLocalId());
                    listener.onAuthComplete(true, " Login Successfully");

                } else {
                    if (message == null || message.isEmpty()) {
                        listener.onAuthComplete(false, "Failed to Login");
                    } else {
                        listener.onAuthComplete(false, message);
                    }
                }
            }
        }, user.getEmail(), user.getPasswod());
    }
}


