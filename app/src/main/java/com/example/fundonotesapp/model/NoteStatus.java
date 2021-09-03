package com.example.fundonotesapp.model;

import java.util.ArrayList;
import java.util.List;

public class NoteStatus {
    private boolean status;
    private String message;
    private ArrayList<Notes> noteList;
    private Notes notes;

    public NoteStatus(Notes notes,boolean status) {
        this.notes = notes;
        this.status = status;
    }

    public NoteStatus(boolean status, String message, ArrayList<Notes> noteList) {
        this.status = status;
        this.message = message;
        this.noteList = noteList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Notes> getNoteList() {
        return noteList;
    }

    public void setNoteList(ArrayList<Notes> noteList) {
        this.noteList = noteList;
    }
}
