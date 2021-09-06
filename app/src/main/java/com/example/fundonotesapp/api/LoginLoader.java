package com.example.fundonotesapp.api;

import com.example.fundonotesapp.model.AuthListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginLoader {

    public static void getLoginDone(LoginListener listener, String email, String password) {
        FundoClient.getInstance().getMyApi().loginFundoUser(new LoginRequest(email, password, true))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        if (response.isSuccessful()) {
                            listener.onLogin(response.body(), true, "");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        listener.onLogin(null, false, t.getMessage());
                    }
                });
    }
}
