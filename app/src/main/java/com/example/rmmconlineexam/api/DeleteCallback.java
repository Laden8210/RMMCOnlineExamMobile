package com.example.rmmconlineexam.api;

public interface DeleteCallback {
    void onDeleteSuccess(String response);
    void onDeleteFail(String error);
}
