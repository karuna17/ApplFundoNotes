package com.example.fundonotesapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fundonotesapp.model.AuthService;

public class RegisterViewModelFactory implements ViewModelProvider.Factory {
    private AuthService authService;

    public RegisterViewModelFactory(AuthService authService) {
        this.authService = authService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new RegisterViewModel(authService);
    }
}
