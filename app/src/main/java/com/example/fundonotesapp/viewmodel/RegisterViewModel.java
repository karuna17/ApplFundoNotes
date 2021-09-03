package com.example.fundonotesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fundonotesapp.model.AuthService;
import com.example.fundonotesapp.model.Status;
import com.example.fundonotesapp.model.User;

public class RegisterViewModel extends ViewModel {
    private AuthService authService;

    private MutableLiveData<Status> _userRegisterStatus = new MutableLiveData<>();
    public LiveData<Status> userRegisterStatus = (LiveData<Status>) _userRegisterStatus;

    private MutableLiveData<Status> _profileStatus = new MutableLiveData<>();
    public LiveData<Status> profileStatus = (LiveData<Status>) _profileStatus;


    public RegisterViewModel(AuthService authService) {
        this.authService = authService;
    }

    public void registerToFundoNotes(User user) {
        authService.registerUser(user, ((status, message) -> _userRegisterStatus.setValue(new Status(status, message))));
    }
}
