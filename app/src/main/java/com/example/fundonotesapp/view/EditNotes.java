package com.example.fundonotesapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
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
import com.example.fundonotesapp.viewmodel.SharedViewModel;
import com.example.fundonotesapp.viewmodel.SharedViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;


public class EditNotes extends Fragment {
    EditText noteTitle, noteContent;
    FloatingActionButton fabSaveNoteButton;

    Notes notes;
    private NoteViewModel noteViewModel;
    private SharedViewModel sharedViewModel;

    String noteId, userId;

    public EditNotes() {
    }

    public static EditNotes newInstance(String param1, String param2) {
        EditNotes fragment = new EditNotes();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_notes, container, false);
        noteTitle = v.findViewById(R.id.edit_note_title);
        noteContent = v.findViewById(R.id.edit_content);
        fabSaveNoteButton = v.findViewById(R.id.edit_note);

        noteViewModel = new ViewModelProvider(this, new NoteViewModelFactory(new NoteService(new DBHelper(getContext())))).get(NoteViewModel.class);
        sharedViewModel = new ViewModelProvider(getActivity(), new SharedViewModelFactory()).get(SharedViewModel.class);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            noteTitle.setText(bundle.getString("title"));
            noteContent.setText(bundle.getString("content"));
            noteId = bundle.getString("noteId");
            userId = bundle.getString("userId");
        }
        editNotes();
    }

    private void editNotes() {
        fabSaveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = noteTitle.getText().toString();
                String content = noteContent.getText().toString();
                notes = new Notes(title, content, noteId, userId);
                noteViewModel.updateNotes(notes);
                noteViewModel.updateNotesStatus.observe(getViewLifecycleOwner(), status -> {
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
}
