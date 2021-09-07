package com.example.fundonotesapp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FundoApi {
//   static final String BASE_URL="https://identitytoolkit.googleapis.com/v1/";
    @POST("./accounts:signInWithPassword?key=AIzaSyD653l858SyUaFgjf6KvRH2Jl5Iv_8Bc5s")
    Call<LoginResponse> loginFundoUser(@Body LoginRequest request);
}
