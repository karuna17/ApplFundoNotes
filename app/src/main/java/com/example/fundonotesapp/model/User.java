package com.example.fundonotesapp.model;

public class User {
    private String uri, name, email, passwod;

    public User(String uri, String name, String email, String passwod) {
        this.uri = uri;
        this.name = name;
        this.email = email;
        this.passwod = passwod;
    }

    public User(String email, String passwod) {
        this.email = email;
        this.passwod = passwod;
    }

    public User(String name, String email, String passwod) {
        this.name = name;
        this.email = email;
        this.passwod = passwod;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswod() {
        return passwod;
    }

    public void setPasswod(String passwod) {
        this.passwod = passwod;
    }
}
