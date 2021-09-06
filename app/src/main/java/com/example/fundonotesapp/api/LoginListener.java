package com.example.fundonotesapp.api;

import retrofit2.http.Body;

public interface LoginListener {
    void onLogin(LoginResponse response, boolean status,String message);
}
