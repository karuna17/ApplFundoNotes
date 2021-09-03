package com.example.fundonotesapp.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fundonotesapp.R;
import com.example.fundonotesapp.database.DBHelper;
import com.example.fundonotesapp.model.NoteService;
import com.example.fundonotesapp.model.Notes;
import com.example.fundonotesapp.model.ReminderService;
import com.example.fundonotesapp.model.Status;
import com.example.fundonotesapp.viewmodel.NoteViewModel;
import com.example.fundonotesapp.viewmodel.NoteViewModelFactory;
import com.example.fundonotesapp.viewmodel.NotesSharedViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SetReminder extends DialogFragment {

    private static final String TAG = SetReminder.class.getName();
    private ImageView closeButton, checkButton;
    private TextView mDate, selectDate, mTime, selectTime;
    private Calendar reminderCalender;
    private Notes notes;
    private NoteViewModel noteViewModel;
    private NotesSharedViewModel notesSharedViewModel;

    public SetReminder() {
    }

    public SetReminder(Notes notes) {
        this.notes = notes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(1000, 750);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_set_reminder, container, false);

        closeButton = v.findViewById(R.id.reminderDialogeCloseIcon);
        checkButton = v.findViewById(R.id.setReminderIcon);
        mDate = v.findViewById(R.id.date);
        selectDate = v.findViewById(R.id.selectDate);
        mTime = v.findViewById(R.id.time);
        selectTime = v.findViewById(R.id.selectTime);
        reminderCalender = Calendar.getInstance();

        noteViewModel = new ViewModelProvider(this, new NoteViewModelFactory(new NoteService(new DBHelper(getContext())))).get(NoteViewModel.class);
        notesSharedViewModel = new ViewModelProvider(requireActivity()).get(NotesSharedViewModel.class);
        Log.d(TAG, "onCreateView: "+notes.toString());
        return v;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setReminder();
        handleDateOption();
        handleTimeOption();
    }

    private void setReminder() {
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Title : " + notes.getNoteTitle());
                new ReminderService(requireContext()).setReminderForNotes(reminderCalender, notes);
                notesSharedViewModel.setReminderTime(reminderCalender);
                dismiss();
            }
        });
    }

    private void handleTimeOption() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        selectTime.setText(DateFormat.format("hh:mm a", calendar));
        reminderCalender.set(Calendar.HOUR, hour);
        reminderCalender.set(Calendar.MINUTE, minute);

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                        (view, hourOfDay, minute1) -> {
                            Calendar timePickerCalender = Calendar.getInstance();
                            timePickerCalender.set(Calendar.HOUR, hourOfDay);
                            reminderCalender.set(Calendar.HOUR, hourOfDay);
                            timePickerCalender.set(Calendar.MINUTE, minute1);
                            reminderCalender.set(Calendar.MINUTE, minute1);
                            selectTime.setText(DateFormat.format("hh:mm a", timePickerCalender));
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });
    }

    private void handleDateOption() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        selectDate.setText(DateFormat.format("MMM d, yyyy", calendar));
        reminderCalender.set(Calendar.YEAR, year);
        reminderCalender.set(Calendar.MONTH, month);
        reminderCalender.set(Calendar.DAY_OF_MONTH, day);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                        (view, year1, month1, dayOfMonth) -> {
                            Calendar datePickerCalender = Calendar.getInstance();
                            datePickerCalender.set(Calendar.YEAR, year1);
                            datePickerCalender.set(Calendar.MONTH, month1);
                            datePickerCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            reminderCalender.set(Calendar.YEAR, year1);
                            reminderCalender.set(Calendar.MONTH, month1);
                            reminderCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            selectDate.setText(DateFormat.format("MMM d, yyyy", datePickerCalender));
                        }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

    }
}