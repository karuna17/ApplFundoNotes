package com.example.fundonotesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;

public class NotesSharedViewModel extends ViewModel {
    public MutableLiveData<Calendar> _reminderTime = new MutableLiveData<Calendar>();
    public LiveData<Calendar> reminderTime = (LiveData<Calendar>) _reminderTime;

    public void setReminderTime(Calendar reminderCalender) {
        _reminderTime.setValue(reminderCalender);
    }
}
