package com.example.usuario.platzigram.login.view;

import android.view.View;

public interface LoginView {

    void enableInputs();
    void disableInputs();

    void showProgressBar();
    void hideProgressBar();

    void loginError(String error);
    void goCreateAccount();
    void goHome();

}
