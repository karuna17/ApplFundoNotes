package com.example.fundonotesapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.fundonotesapp.model.DeleteDataListener;
import com.example.fundonotesapp.model.Listener;
import com.example.fundonotesapp.model.NoteListener;
import com.example.fundonotesapp.model.Notes;
import com.example.fundonotesapp.model.UpdateDataListener;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "FundoNotes";
    private static final String NOTES = "Notes";
    private static final String NOTE_ID = "Note_Id";
    private static final String USER_ID = "User_Id";
    private static final String NOTE_TITLE = "Note_Title";
    private static final String NOTE_CONTENT = "Note_Content";
    private static final String REMINDER_TIME = "ReminderTime";
    private static final String TIME_REMINDER = "Reminder_Time";

    SQLiteDatabase db;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("ALTER TABLE " + TIME_REMINDER + " RENAME TO " + REMINDER_TIME);
        String createNotes = "CREATE TABLE " + NOTES + "("
                + USER_ID + " TEXT,"
                + NOTE_ID + " TEXT PRIMARY KEY,"
                + NOTE_TITLE + " TEXT,"
                + NOTE_CONTENT + " TEXT,"
                + REMINDER_TIME + " INTEGER" + ")";
        db.execSQL(createNotes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + NOTES);
//        onCreate(db);
//        db.execSQL("ALTER TABLE " + TIME_REMINDER + " RENAME TO " + REMINDER_TIME);
//        onCreate(db);
    }

    public void addDBNotes(Notes notes, Listener dbListener) {
        db = this.getWritableDatabase();
        ContentValues noteDetails = new ContentValues();
        noteDetails.put(USER_ID, notes.getUserId());
        noteDetails.put(NOTE_ID, notes.getNoteId());
        noteDetails.put(NOTE_TITLE, notes.getNoteTitle());
        noteDetails.put(NOTE_CONTENT, notes.getNoteContent());
        noteDetails.put(REMINDER_TIME, notes.getReminderTime());

        long isInsert = db.insert(NOTES, null, noteDetails);
        Log.d(TAG, "addNotes: isInsertCheck " + isInsert);
        if (((int) isInsert) == -1) {
            Log.d(TAG, "addNotes: isInsertCheck " + isInsert);
            dbListener.onNotesFetched(false, "Failed To Add Data");
        } else {
            dbListener.onNotesFetched(true, "Data Added Successfully");
        }
    }

    public void updateDBNotes(Notes notes, UpdateDataListener updateDataListener) {
        db = this.getWritableDatabase();
        ContentValues noteDetails = new ContentValues();

        noteDetails.put(NOTE_TITLE, notes.getNoteTitle());
        noteDetails.put(NOTE_CONTENT, notes.getNoteContent());

        int isUpdate = db.update(NOTES, noteDetails, NOTE_ID + "= '" + notes.getNoteId() + "'", null);

        if (isUpdate > 0) {
            updateDataListener.onDataUpdate(true, "Updated Data Successfully");
        } else {
            updateDataListener.onDataUpdate(false, "Failed To Update Data");
        }
    }

    public void deleteDBNotes(Notes notes, DeleteDataListener deleteDataListener) {
        db = this.getWritableDatabase();
        int isDelete = db.delete(NOTES, NOTE_ID + "= '" + notes.getNoteId() + "'", null);

        if (isDelete > 0) {
            deleteDataListener.onDataDelete(true, "Deleted Note Successfully");
        } else {
            deleteDataListener.onDataDelete(false, "Failed To Update Note");
        }
    }

    public void fetchNotesFromDB(String uId, NoteListener noteListener) {
        ArrayList<Notes> notesList = new ArrayList<>();

        String query = "SELECT * FROM " + NOTES + " WHERE " + USER_ID + " = '" + uId + "'";
        db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Notes note = new Notes();
                note.setUserId(cursor.getString(0));
                note.setNoteId(cursor.getString(1));
                note.setNoteTitle(cursor.getString(2));
                note.setNoteContent(cursor.getString(3));
                note.setReminderTime(cursor.getLong(4));
                notesList.add(note);

            } while (cursor.moveToNext());
            noteListener.onNotesFetched(true, "Fetch Notes Successfully", notesList);
        } else {
            noteListener.onNotesFetched(true, "Failed To Fetch Notes", notesList);
        }
        cursor.close();
    }

    @Override
    public synchronized void close() {
        super.close();
        db.close();
    }
}