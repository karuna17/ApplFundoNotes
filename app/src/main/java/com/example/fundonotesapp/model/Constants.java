package com.example.fundonotesapp.model;

public class Constants {
    private String userId;
    private static Constants constants ;
    private Constants() {
    }

    public static Constants getInstance() {
        if (constants==null) {
            constants = new Constants();
        }
        return constants;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
