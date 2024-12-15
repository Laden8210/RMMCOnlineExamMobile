package com.example.rmmconlineexam.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    private int id;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("user_type")
    private String user_type;

    @SerializedName("is_exam_taken")
    private int isExamTaken;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public int getIsExamTaken() {
        return isExamTaken;
    }

    public void setIsExamTaken(int isExamTaken) {
        this.isExamTaken = isExamTaken;
    }
}
