package com.example.fundonotesapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fundonotesapp.model.AuthService;

import org.jetbrains.annotations.NotNull;

public class ProfileViewModelFactory implements ViewModelProvider.Factory {
    AuthService service;

    public ProfileViewModelFactory(AuthService service) {
        this.service = service;
    }

    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        return (T) new ProfileViewModel(service);
    }
}
