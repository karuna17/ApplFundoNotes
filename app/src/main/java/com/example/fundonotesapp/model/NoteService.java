package com.example.fundonotesapp.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fundonotesapp.database.DBHelper;
import com.example.fundonotesapp.view.AddNotes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteService {

    private static final String TAG = NoteService.class.getName();
    private DBHelper dbHelper;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    FirebaseUser currentUser = mAuth.getCurrentUser();

    public final String USER_COLLECTION = "users";
    public final String NOTES_COLLECTION = "notes";
    public final String NOTE_TITLE = "title";
    public final String NOTE_CONTENT = "content";
    public final String REMINDER_TIME = "reminder_time";


    public NoteService() {
        super();
    }



    public NoteService(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void saveNotesInFireStore(Notes note, Listener addDatalistener) {

        documentReference = fstore.collection(USER_COLLECTION)
                .document(currentUser.getUid())
                .collection(NOTES_COLLECTION).document();

        Map<String, Object> noteDB = new HashMap<>();
        noteDB.put(NOTE_TITLE, note.getNoteTitle());
        noteDB.put(NOTE_CONTENT, note.getNoteContent());
        documentReference.set(noteDB).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("Check Data", "onComplete: " + note.getNoteTitle());
                if (task.isSuccessful()) {
                    note.setUserId(currentUser.getUid());
                    note.setNoteId(documentReference.getId());
                    dbHelper.addDBNotes(note, addDatalistener);
                } else {
                    addDatalistener.onNotesFetched(false, "Failed");
                }
            }
        });
    }

    public void getNotesFromFirestore(NoteListener listener) {
        fstore.collection(USER_COLLECTION)
                .document(Constants.getInstance().getUserId())
                .collection(NOTES_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG, "UserId: "+Constants.getInstance().getUserId());
                ArrayList<Notes> notesList = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Notes note = new Notes(document.getString(NOTE_TITLE).toString(),
                                document.getString(NOTE_CONTENT).toString(),
                                document.getId(), Constants.getInstance().getUserId());
                        notesList.add(note);
                        dbHelper.fetchNotesFromDB(note.getUserId(),listener);
                    }
                    listener.onNotesFetched(true, "All Notes Fetch Successfully", notesList);
                    Log.d("Note Status", "onComplete: " + notesList.size());

                } else {
                    listener.onNotesFetched(false, "Failed To Fetch Notes", notesList);
                }
            }
        });
    }

    public void updateNoteFromFirestore(Notes note, UpdateDataListener listener) {
        Map<String, Object> notes = new HashMap<>();
        notes.put(NOTE_TITLE, note.getNoteTitle());
        notes.put(NOTE_CONTENT, note.getNoteContent());

        fstore.collection(USER_COLLECTION)
                .document(currentUser.getUid())
                .collection(NOTES_COLLECTION).document(note.getNoteId()).update(notes)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            listener.onDataUpdate(true, " Update  Note Successfully");
                            dbHelper.updateDBNotes(note, listener);
                        } else {
                            listener.onDataUpdate(false, "Failed To Update Note");
                        }
                    }
                });
    }

    public void deleteNoteFromFirestore(Notes note, DeleteDataListener listener) {

        Log.d("Note Id", "deleteNoteFromFirestore: " + note.getNoteId());
        fstore.collection(USER_COLLECTION)
                .document(currentUser.getUid())
                .collection(NOTES_COLLECTION).document(note.getNoteId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dbHelper.deleteDBNotes(note, listener);
                        } else {
                            listener.onDataDelete(false, "Failed To Delete Note ");
                        }
                    }
                });
    }

//    public void getNotesFromDB(NoteListener noteListener) {
//        dbHelper.fetchNotesFromDB(currentUser.getUid(), noteListener);
//    }
}
