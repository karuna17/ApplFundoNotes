package com.example.fundonotesapp.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fundonotesapp.R;
import com.example.fundonotesapp.database.DBHelper;
import com.example.fundonotesapp.model.DeleteListener;
import com.example.fundonotesapp.model.MyAdapter;
import com.example.fundonotesapp.model.NoteService;
import com.example.fundonotesapp.model.Notes;
import com.example.fundonotesapp.viewmodel.NoteViewModel;
import com.example.fundonotesapp.viewmodel.NoteViewModelFactory;
import com.example.fundonotesapp.viewmodel.SharedViewModel;
import com.example.fundonotesapp.viewmodel.SharedViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class Home extends Fragment {
    private static final String TAG = "Home";
    private FloatingActionButton fabButtonAddNote;
    private RecyclerView mRecyclerview;
    MyAdapter adapter;
    NoteViewModel noteViewModel;
    SharedViewModel sharedViewModel;

    ArrayList<Notes> notes = new ArrayList<>();

    public Home() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        fabButtonAddNote = v.findViewById(R.id.floating_btn_AddNote);
        mRecyclerview = v.findViewById(R.id.notelist_recyclerView);

        noteViewModel = new ViewModelProvider(this, new NoteViewModelFactory(new NoteService(new DBHelper(getContext())))).get(NoteViewModel.class);
        sharedViewModel = new ViewModelProvider(getActivity(), new SharedViewModelFactory()).get(SharedViewModel.class);

        mRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new MyAdapter(notes, getContext(), deleteListener);
        mRecyclerview.setAdapter(adapter);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        openAddNote();
        getAllNotesFromFirestore();
        searchNote();
    }

    private void openAddNote() {
        fabButtonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.set_gotoAddNotesStatus(true);
            }
        });
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void getAllNotesFromFirestore() {
        noteViewModel.gettingNotesFromFireStore();
        noteViewModel.getNotesStatus.observe(getViewLifecycleOwner(), noteStatus -> {
            notes = noteStatus.getNoteList();
            Log.d(TAG, "getAllNotesFromFirestore: " + notes.size());
            if (noteStatus.isStatus() && !noteStatus.getNoteList().isEmpty()) {
                Toast.makeText(getContext(), noteStatus.getMessage(), Toast.LENGTH_SHORT).show();
//                adapter = new MyAdapter(notes, getContext(), deleteListener);
//                mRecyclerview.setAdapter(adapter);
                adapter.setNotesList(noteStatus.getNoteList());
            } else {
                Toast.makeText(getContext(), noteStatus.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void notesDeleted(Notes note) {
        noteViewModel.deleteNotes(note);
        noteViewModel.deleteNotesStatus.observe(getViewLifecycleOwner(), noteStatus -> {
            if (noteStatus.getStatus()) {
                Toast.makeText(getContext(), noteStatus.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), noteStatus.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    DeleteListener deleteListener = new DeleteListener() {
        @Override
        public void listenerDeleteNote(boolean deleteOnclick, Notes note) {
            if (deleteOnclick && note != null) {
                notesDeleted(note);
            }
        }
    };

    private void searchNote() {
        sharedViewModel.queryText.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String text) {
                Log.d(TAG, "onChanged: for search Text: "+text.toString());
                adapter.getFilter().filter(text);
            }
        });
    }
}


