package com.example.loginandsignupwithemailverification;

public class User {
    public String fullName;
    public String userName;
    public String email;

    public User() {
    }

    public User(String fullName, String userName, String email) {
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
    }
}
