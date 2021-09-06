package com.example.fundonotesapp.api;

public class LoginRequest {
    String email,password;
    boolean returnSecureToken;

    public LoginRequest(){

    }

    public LoginRequest(String email, String password, boolean returnSecureToken) {
        this.email = email;
        this.password = password;
        this.returnSecureToken = returnSecureToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getReturnSecureToken() {
        return returnSecureToken;
    }

    public void setReturnSecureToken(boolean returnSecureToken) {
        this.returnSecureToken = returnSecureToken;
    }
}
