package com.example.fundonotesapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fundonotesapp.database.DBHelper;
import com.example.fundonotesapp.model.NoteService;
import com.example.fundonotesapp.model.NoteStatus;
import com.example.fundonotesapp.model.Notes;
import com.example.fundonotesapp.model.Status;

public class NoteViewModel extends ViewModel {
    private static final String TAG = "NoteViewModel";
    private NoteService noteAuthService;

    private MutableLiveData<Status> _addNotesStatus = new MutableLiveData<>();
    public LiveData<Status> addNotesStatus = (LiveData<Status>) _addNotesStatus;

    private MutableLiveData<NoteStatus> _getNotesStatus = new MutableLiveData<>();
    public LiveData<NoteStatus> getNotesStatus = (LiveData<NoteStatus>) _getNotesStatus;

    private MutableLiveData<Status> _updateNotesStatus = new MutableLiveData<>();
    public LiveData<Status> updateNotesStatus = (LiveData<Status>) _updateNotesStatus;

    private MutableLiveData<Status> _deleteNotesStatus = new MutableLiveData<>();
    public LiveData<Status> deleteNotesStatus = (LiveData<Status>) _deleteNotesStatus;

    public NoteViewModel(NoteService noteAuthService) {
        this.noteAuthService = noteAuthService;
    }

    public void addingNotesInFireStore(Notes notes) {
        noteAuthService.saveNotesInFireStore(notes, (status, message) -> _addNotesStatus.setValue(new Status(status, message)));
    }

    public void gettingNotesFromFireStore() {
        noteAuthService.getNotesFromFirestore(((status, message, notesList) ->{
            Log.d(TAG, "gettingNotesFromFireStore: "+notesList.size());
            _getNotesStatus.setValue(new NoteStatus(status, message, notesList));
        }));
    }

    public void updateNotes(Notes note) {
        noteAuthService.updateNoteFromFirestore(note, (status, message) -> _updateNotesStatus.setValue(new Status(status, message)));
    }

    public void deleteNotes(Notes note) {
        noteAuthService.deleteNoteFromFirestore(note, (status, message) -> _deleteNotesStatus.setValue(new Status(status, message)));
    }

}
