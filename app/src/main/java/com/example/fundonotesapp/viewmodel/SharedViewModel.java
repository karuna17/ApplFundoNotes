package com.example.fundonotesapp.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.fundonotesapp.model.AuthService;

public class SharedViewModel extends ViewModel {
    AuthService authService;

    private MutableLiveData<Boolean> _gotoRegisterPageStatus = new MutableLiveData<Boolean>();
    public LiveData<Boolean> gotoRegisterPageStatus = (LiveData<Boolean>) _gotoRegisterPageStatus;

    private MutableLiveData<Boolean> _gotoLoginPageStatus = new MutableLiveData<Boolean>();
    public LiveData<Boolean> gotoLoginPageStatus = (LiveData<Boolean>) _gotoLoginPageStatus;

    private MutableLiveData<Boolean> _gotoHomePageStatus = new MutableLiveData<Boolean>();
    public LiveData<Boolean> gotoHomePageStatus = (LiveData<Boolean>) _gotoHomePageStatus;

    private MutableLiveData<Boolean> _gotoProfileStatus = new MutableLiveData<Boolean>();
    public LiveData<Boolean> gotoProfileStatus = (LiveData<Boolean>) _gotoProfileStatus;

    private MutableLiveData<Boolean> _gotoAddNotesStatus = new MutableLiveData<Boolean>();
    public LiveData<Boolean> gotoAddNotesStatus = (LiveData<Boolean>) _gotoAddNotesStatus;

    private MutableLiveData<Boolean> _gotoGetNotesStatus = new MutableLiveData<Boolean>();
    public LiveData<Boolean> gotoGetNotesStatus = (LiveData<Boolean>) _gotoGetNotesStatus;

    private MutableLiveData<Boolean> _gotoUpdateNotesStatus = new MutableLiveData<Boolean>();
    public LiveData<Boolean> gotoUpdateNotesStatus = (LiveData<Boolean>) _gotoUpdateNotesStatus;

    private MutableLiveData<Boolean> _gotoDeleteNotesStatus = new MutableLiveData<Boolean>();
    public LiveData<Boolean> gotoDeleteNotesStatus = (LiveData<Boolean>) _gotoDeleteNotesStatus;

    private MutableLiveData<String> _queryText = new MutableLiveData<String>();
    public LiveData<String> queryText = (LiveData<String>) _queryText;

    private MutableLiveData<Boolean> _userLoggedIn = new MutableLiveData<Boolean>();
    public LiveData<Boolean> userLoggedIn = (LiveData<Boolean>) _userLoggedIn;

    public void set_gotoRegisterPageStatus(Boolean status) {
        this._gotoRegisterPageStatus.setValue(status);
    }

    public void setUserLoggedIn(Boolean status){
        this._userLoggedIn.setValue(status);
    }

    public void set_gotoLoginPageStatus(Boolean status) {
        this._gotoLoginPageStatus.setValue(status);
    }

    public void set_gotoHomePageStatus(Boolean status) {
        Log.d("TAG", "set_gotoHomePageStatus: " + status);
        this._gotoHomePageStatus.setValue(status);
    }

    public void set_gotoAddNotesStatus(Boolean status){
        this._gotoAddNotesStatus.setValue(status);
    }

    public void set_gotoGetNotesStatus(Boolean status){
        this._gotoGetNotesStatus.setValue(status);
    }

    public void setQueryText(String string){
        this._queryText.setValue(string);
    }
}

