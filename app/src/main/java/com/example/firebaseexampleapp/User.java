package com.example.firebaseexampleapp;

public class User {
    private String email;
    private String password;
    private String user_id;
    private String phone;
    private String nickName;

    public User(String email, String password, String user_id, String phone, String nickName) {
        this.email = email;
        this.password = password;
        this.user_id = user_id;
        this.phone = phone;
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public User(String email, String password) {

        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String user_id) {
        this.email = email;
        this.password = password;
        this.user_id = user_id;
    }
}
