package com.example.fundonotesapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fundonotesapp.model.NoteService;

import org.jetbrains.annotations.NotNull;

public class NoteViewModelFactory implements ViewModelProvider.Factory
{
    NoteService noteAuthService;

    public NoteViewModelFactory(NoteService noteAuthService) {
        this.noteAuthService = noteAuthService;
    }

    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        return (T) new NoteViewModel(noteAuthService);
    }
}
