package com.example.imigration_test_app.Viewmodel;

import com.example.imigration_test_app.Model.User;

public interface LoginCallback {
    void onLoginSuccess(User currentUser);

    void onLoginFailed();
}
