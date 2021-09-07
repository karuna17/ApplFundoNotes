package com.example.fundonotesapp.api;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FundoClient {
    private static FundoClient instance = null;
    private FundoApi myApi;
    private OkHttpClient client;

    private FundoClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        client = httpClient
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .build();
    }

    public static synchronized FundoClient getInstance() {
        if (instance == null) {
            instance = new FundoClient();
        }
        return instance;
    }

    public FundoApi getMyApi() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://identitytoolkit.googleapis.com/v1/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .client(client).build();
        myApi = retrofit.create(FundoApi.class);

        return myApi;
    }

}
