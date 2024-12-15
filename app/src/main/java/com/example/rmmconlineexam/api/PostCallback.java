package com.example.rmmconlineexam.api;

public interface PostCallback {
    void onPostSuccess(String responseData);

    void onPostError(String errorMessage);
}
