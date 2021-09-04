package com.example.fundonotesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fundonotesapp.model.AuthService;
import com.example.fundonotesapp.model.Status;

public class ProfileViewModel extends ViewModel {
    AuthService authService;

    private MutableLiveData<Boolean> _logoutStatus = new MutableLiveData<Boolean>();
    public LiveData<Boolean> logoutStatus = (LiveData<Boolean>) _logoutStatus;

    public ProfileViewModel(AuthService authService) {
        this.authService = authService;
    }

    public void logout() {
        _logoutStatus.setValue(true);
        authService.signOut();
    }

}
