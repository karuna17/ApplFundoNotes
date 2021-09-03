package com.example.fundonotesapp.model;

import java.util.ArrayList;
import java.util.List;

public interface NoteListener {
    void onNotesFetched(boolean status, String message, ArrayList<Notes> notesList);
}

