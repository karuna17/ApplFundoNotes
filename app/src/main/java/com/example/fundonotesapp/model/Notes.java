package com.example.fundonotesapp.model;

import com.google.gson.annotations.SerializedName;

public class Notes {
    private String noteTitle, noteContent, noteId, userId;
    private long reminderTime;

    public Notes() {
        super();
    }

    public Notes(String noteTitle, String noteContent) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
    }

    public Notes(String noteTitle, String noteContent, String noteId, String userId) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteId = noteId;
        this.userId = userId;
    }

    public Notes(String userId, String noteId, String noteTitle, String noteContent, long reminderTime) {
        this.userId = userId;
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.reminderTime = reminderTime;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public long getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(long reminderTime) {
        this.reminderTime = reminderTime;
    }
}
