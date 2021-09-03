package com.example.fundonotesapp.model;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fundonotesapp.R;
import com.example.fundonotesapp.view.EditNotes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class MyAdapter extends RecyclerView.Adapter<NotesHolder> implements Filterable {
    private static final String TAG = "MyAdapter";
    private ArrayList<Notes> notesList = new ArrayList<>();
    private ArrayList<Notes> listOfNotes;
    private Context mContext;
    private DeleteListener deleteListener;

    public MyAdapter(ArrayList<Notes> notesList, Context mContext, DeleteListener deleteListener) {
        this.notesList = notesList;
        this.listOfNotes = new ArrayList<>();
        this.mContext = mContext;
        this.deleteListener = deleteListener;
        Log.d(TAG, "MyAdapter: " + notesList.size());
    }

    public void setNotesList(ArrayList<Notes> notesList) {
        this.notesList.clear();
        this.notesList = notesList;
        listOfNotes.clear();
        listOfNotes.addAll(notesList);
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public NotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View noteView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view, parent, false);
        NotesHolder notesHolder = new NotesHolder(noteView);
        return notesHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesHolder holder, int position) {

        Notes note = notesList.get(position);
        holder.bindNotes(note);
        Log.d(TAG, "onBindViewHolder: Title: " + note.getNoteTitle() + " Content: " + note.getNoteContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditNotes editNotes = new EditNotes();
                Bundle args = new Bundle();
                args.putString("title", note.getNoteTitle());
                args.putString("content", note.getNoteContent());
                args.putString("noteId", note.getNoteId());
                args.putString("userId", note.getUserId());

                editNotes.setArguments(args);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container, editNotes).addToBackStack(null).commit();
            }
        });

        ImageView popupButton = holder.itemView.findViewById(R.id.menuIcon);
        popupButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.setGravity(Gravity.END);
                popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        deleteListener.listenerDeleteNote(true, note);
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("Item List", "getItemCount: " + notesList.size());
        return notesList.size();
    }

    @Override
    public Filter getFilter() {
     return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Notes> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                Log.d(TAG, "performFiltering: IF Block: "+notesList.size());
                filteredList.addAll(listOfNotes);

            } else {
                for (Notes allNotes : listOfNotes) {
                    if (allNotes.getNoteTitle().toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        Log.d(TAG, "performFiltering: Else Block: "+allNotes.toString());
                        filteredList.add(allNotes);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            Log.d(TAG, "performFiltering: List "+filteredList.size());
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notesList.clear();
            notesList.addAll((Collection<? extends Notes>) results.values);
            notifyDataSetChanged();
        }
    };
}

class NotesHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "NotesHolder";
    TextView noteTitle, noteContent;
    CardView mCardView;
    View view;

    public NotesHolder(@NonNull View itemView) {
        super(itemView);
        noteTitle = itemView.findViewById(R.id.note_title);
        noteContent = itemView.findViewById(R.id.note_content);
        mCardView = itemView.findViewById(R.id.notecard);
        view = itemView;
    }

    public void bindNotes(Notes note) {
        noteTitle.setText(note.getNoteTitle());
        noteContent.setText(note.getNoteContent());
        Log.d(TAG, "bindNotes: Title: " + noteTitle.getText() + "  Content: " + noteContent.getText());

    }
}