package com.example.fundonotesapp.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class Home extends Fragment {
    private static final String TAG = "Home";
    private FloatingActionButton fabButtonAddNote;
    private RecyclerView mRecyclerview;
    private ProgressBar recyclerProgress;

    NoteViewModel noteViewModel;
    SharedViewModel sharedViewModel;

    StaggeredGridLayoutManager manager;
    ArrayList<Notes> notes = new ArrayList<>();
    MyAdapter adapter;

    Boolean isScrolling = false;
    int currentItems;
    int totalItems;
    int[] scrollOutItems;

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
        recyclerProgress = v.findViewById(R.id.recycler_progressBar);

        noteViewModel = new ViewModelProvider(this, new NoteViewModelFactory(new NoteService(new DBHelper(getContext())))).get(NoteViewModel.class);
        sharedViewModel = new ViewModelProvider(getActivity(), new SharedViewModelFactory()).get(SharedViewModel.class);

        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(manager);
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
       // recyclerScrolling();
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
                Log.d(TAG, "onChanged: for search Text: " + text.toString());
                adapter.getFilter().filter(text);
            }
        });
    }

//    private void recyclerScrolling() {
//        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    isScrolling = true;
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                currentItems = manager.getChildCount();
//                totalItems = manager.getItemCount();
//                scrollOutItems = manager.findFirstVisibleItemPositions(null);
//
//                if (isScrolling && (currentItems + (scrollOutItems.length) == totalItems)) {
//                    isScrolling = false;
//                    fetchData();
//                }
//
//            }
//        });
//    }
//
//    private void fetchData() {
//        recyclerProgress.setVisibility(View.VISIBLE);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                adapter.setNotesList(notes);
//                getAllNotesFromFirestore();
//                adapter.notifyDataSetChanged();
//                recyclerProgress.setVisibility(View.VISIBLE);
//            }
//        }, 5000);
//    }


}


