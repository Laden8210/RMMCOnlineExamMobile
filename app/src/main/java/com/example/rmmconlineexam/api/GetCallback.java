package com.example.rmmconlineexam.api;

public interface GetCallback {

    void onGetSuccess(String responseData);

    void onGetError(String errorMessage);
}
