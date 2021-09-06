package com.example.fundonotesapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FundoClient {
    private static FundoClient instance = null;
    private FundoApi myApi;

    private FundoClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(FundoApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(FundoApi.class);
    }

    public static synchronized FundoClient getInstance() {
        if (instance == null) {
            instance = new FundoClient();
        }
        return instance;
    }

    public FundoApi getMyApi() {
        return myApi;
    }

}
