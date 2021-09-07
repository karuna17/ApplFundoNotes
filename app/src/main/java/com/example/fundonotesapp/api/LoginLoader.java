package com.example.fundonotesapp.api;

import android.util.Log;

import com.example.fundonotesapp.model.AuthListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginLoader {

    private static final String TAG = LoginLoader.class.getName();

    public static void getLoginDone(LoginListener listener, String email, String password) {
        FundoClient.getInstance().getMyApi().loginFundoUser(new LoginRequest(email, password, true))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Log.d(TAG, "onResponse: " + response.isSuccessful() + " " + response.code());
                        if (response.isSuccessful()) {
                            listener.onLogin(response.body(), true, "");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.d(TAG, "onResponse: onFailure: " +t.getMessage());
                        listener.onLogin(null, false, t.getMessage());
                    }
                });
    }
}
