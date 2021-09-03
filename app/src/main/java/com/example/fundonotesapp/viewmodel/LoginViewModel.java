package com.example.fundonotesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fundonotesapp.model.AuthService;
import com.example.fundonotesapp.model.Status;
import com.example.fundonotesapp.model.User;

public class LoginViewModel extends ViewModel {
    private AuthService authService;

    private MutableLiveData<Status> _userLoginStatus = new MutableLiveData<>();
    public LiveData<Status> userLoginStatus = (LiveData<Status>) _userLoginStatus;

//    private MutableLiveData<FBStatus> _userFBLoginStatus = new MutableLiveData<>();
//    public LiveData<FBStatus> userFBLoginStatus = (LiveData<FBStatus>) _userFBLoginStatus;

    private MutableLiveData<Status> _resetPasswordStatus = new MutableLiveData<>();
    public LiveData<Status> resetPasswordStatus = (LiveData<Status>) _resetPasswordStatus;

    private MutableLiveData<Status> _logoutStatus = new MutableLiveData<>();
    public LiveData<Status> logoutStatus = (LiveData<Status>) _logoutStatus;

    private MutableLiveData<Boolean> _isUserLoggedIn = new MutableLiveData<Boolean>();
    public LiveData<Boolean> isUserLoggedIn = (LiveData<Boolean>) _isUserLoggedIn;



    public LoginViewModel(AuthService authService) {
        this.authService = authService;
    }

    public void loginToFundoNotes(User user) {
        authService.loginUser(user, (status, message) -> _userLoginStatus.setValue(new Status(status, message)));
    }

    public void resettingPasswordToFundoNotes(String vmEmail) {
        authService.resetPassword(vmEmail, (status, message) -> _resetPasswordStatus.setValue(new Status(status, message)));
    }

    public void normalLogout() {
        authService.signOut();
    }

    public void checkUserExistence() {
        authService.getLoginStatus((status, message) -> _isUserLoggedIn.setValue(status));
    }

//    public void fbLoginToFundoNotes(AccessToken token) {
//        authService.fbLogin(token, (user, status) -> _userFBLoginStatus.setValue(new FBStatus(user, status)));
//    }
//
//    public void fbLogoutFundoNotes() {
//        authService.fbSignout();
//    }
}
