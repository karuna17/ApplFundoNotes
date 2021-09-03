package com.example.fundonotesapp.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fundonotesapp.R;
import com.example.fundonotesapp.database.DBHelper;
import com.example.fundonotesapp.model.NoteService;
import com.example.fundonotesapp.model.Notes;
import com.example.fundonotesapp.viewmodel.NoteViewModel;
import com.example.fundonotesapp.viewmodel.NoteViewModelFactory;
import com.example.fundonotesapp.viewmodel.NotesSharedViewModel;
import com.example.fundonotesapp.viewmodel.SharedViewModel;
import com.example.fundonotesapp.viewmodel.SharedViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class AddNotes extends Fragment {
    private static final String TAG = AddNotes.class.getName();
    private EditText noteTitle, noteContent;
    private FloatingActionButton fabSaveNoteButton, reminderButton;
    private Toolbar toolbar;

    private NoteViewModel noteViewModel;
    private NotesSharedViewModel notesSharedViewModel;
    private SharedViewModel sharedViewModel;
    Notes notes;

    public AddNotes() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_notes, container, false);

        noteTitle = v.findViewById(R.id.create_note_title);
        noteContent = v.findViewById(R.id.create_content);
        fabSaveNoteButton = v.findViewById(R.id.save_note);
        reminderButton = v.findViewById(R.id.reminderBtn);

        noteViewModel = new ViewModelProvider(this, new NoteViewModelFactory(new NoteService(new DBHelper(getContext())))).get(NoteViewModel.class);
        sharedViewModel = new ViewModelProvider(getActivity(), new SharedViewModelFactory()).get(SharedViewModel.class);
        notesSharedViewModel = new ViewModelProvider(requireActivity()).get(NotesSharedViewModel.class);

        notesSharedViewModel.reminderTime.observe(getViewLifecycleOwner(), new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                notes.setReminderTime(calendar.getTimeInMillis());
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        saveNotes();
        setNoteReminder();
    }

    //    @Override
//    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
//       inflater.inflate(R.menu.addnote_toolbar_menu,menu);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Notes");
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStack();
//            }
//        });
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
//                if (item.getItemId() == android.R.id.home) {
//                    this.setEnabled(false);
//                    requireActivity().onBackPressed();
//                }
                if (item.getItemId() == R.id.addReminder) {
//                    new SetReminder(notes).show(requireActivity().getSupportFragmentManager(), "Reminder");
                }
            }
        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        return super.onOptionsItemSelected(item);
    }

    private void saveNotes() {
        fabSaveNoteButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View v) {
                String title = noteTitle.getText().toString();
                String content = noteContent.getText().toString();
                Notes note = new Notes(title,content);
                Log.d(TAG, "onClick: Reminder "+note.getReminderTime());
                note.setReminderTime(notes.getReminderTime());

                noteViewModel.addingNotesInFireStore(note);
                noteViewModel.addNotesStatus.observe(AddNotes.this, status -> {
                    if (status.getStatus()) {
                        Toast.makeText(getContext(), status.getMessage(), Toast.LENGTH_SHORT).show();
                        sharedViewModel.set_gotoHomePageStatus(true);
                    } else {
                        Toast.makeText(getContext(), status.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setNoteReminder() {
        reminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = noteTitle.getText().toString();
                String content = noteContent.getText().toString();
                 notes = new Notes(title, content);
                new SetReminder(notes).show(requireActivity().getSupportFragmentManager(), "Reminder");
            }
        });
    }
}