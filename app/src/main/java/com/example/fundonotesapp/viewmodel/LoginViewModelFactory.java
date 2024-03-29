package com.example.fundonotesapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.fundonotesapp.model.AuthService;

public class LoginViewModelFactory implements ViewModelProvider.Factory {
    private AuthService authService;

    public LoginViewModelFactory(AuthService authService) {
        this.authService = authService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LoginViewModel(authService);
    }
}
