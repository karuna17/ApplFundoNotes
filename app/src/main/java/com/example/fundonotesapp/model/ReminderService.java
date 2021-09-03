package com.example.fundonotesapp.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class ReminderService {
    private static final String TAG = ReminderService.class.getName();
    Context context;

    public ReminderService(Context context) {
        this.context = context;
    }

    public void setReminderForNotes(Calendar remindCalander, Notes notes) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReciever.class);
        Log.d(TAG, "setReminderForNotes: " + notes.getNoteContent());

        intent.putExtra("title", notes.getNoteTitle());
        intent.putExtra("content", notes.getNoteContent());
        intent.putExtra("noteId", notes.getNoteId());
        intent.putExtra("userId", notes.getUserId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, remindCalander.getTimeInMillis(), pendingIntent);
    }

}
