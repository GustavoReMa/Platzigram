package com.example.usuario.platzigram.login.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.usuario.platzigram.R;
import com.example.usuario.platzigram.login.presenter.LoginPresenter;
import com.example.usuario.platzigram.login.presenter.LoginPresenterImpl;
import com.example.usuario.platzigram.view.ConteinerActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.internal.WebDialog;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements LoginView{

    private TextInputEditText username,password;
    private Button login;
    private LoginButton loginButtonFacebook;
    private ProgressBar progressBarLogin;
    private LoginPresenter presenter;

    private static final String TAG = LoginActivity.class.getCanonicalName();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

 /*       try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.usuario.platzigram",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }    */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        firebaseAuth    = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser!=null){
                    Log.w(TAG,"Usuario logeado " + firebaseUser.getEmail());
                    goHome();
                }else{
                    Log.w(TAG,"Usuario no logeado ");
                }
            }
        };

        username = (TextInputEditText) findViewById(R.id.username);
        password = (TextInputEditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        loginButtonFacebook = (LoginButton) findViewById(R.id.login_facebook);
        progressBarLogin = (ProgressBar) findViewById(R.id.progressbarLogin);
        hideProgressBar();

        presenter = new LoginPresenterImpl(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("")&&password.getText().toString().equals("")){
                    loginError("Hey ingresa tus datos");
                }else if(username.getText().toString().equals("")){
                    loginError("Hey ingresa tu username");
                } else if(password.getText().toString().equals("")){
                    loginError("Hey ingresa tu password");
                }else{
                    signIn(username.getText().toString(),password.getText().toString());
                }
            }
        });
        loginButtonFacebook.setReadPermissions(Arrays.asList("email"));
        loginButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.w(TAG,"Facebok Login Success Token: "+loginResult.getAccessToken().getApplicationId());
                signInFacebookFirebase(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.w(TAG,"Facebok Login Success Cancelado ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.w(TAG,"Facebok Login Success Error: "+error.toString());
                error.printStackTrace();
            }
        });
    }

    private void signInFacebookFirebase(AccessToken accessToken) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user = task.getResult().getUser();

                    SharedPreferences preferences = getSharedPreferences("USER",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email",user.getEmail());
                    editor.commit();

                    goHome();
                    Toast.makeText(LoginActivity.this, "Login Facebook exitoso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this, "Login Facebook NO exitoso", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void signIn(String username, String password) {
        presenter.signIn(username,password,this,firebaseAuth);
    }

    @Override
    public void enableInputs() {
        username.setEnabled(true);
        password.setEnabled(true);
        login.setEnabled(true);
    }

    @Override
    public void disableInputs() {
        username.setEnabled(false);
        password.setEnabled(false);
        login.setEnabled(false);
    }

    @Override
    public void showProgressBar() {
        progressBarLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBarLogin.setVisibility(View.GONE);
    }

    @Override
    public void loginError(String error) {
        Toast.makeText(this, getString(R.string.login_error)+error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goCreateAccount() {
        Intent intent = new Intent(this,CreateAccountActivity.class);
        startActivity(intent);
    }

    @Override
    public void goHome() {
        Intent intent = new Intent(this,ConteinerActivity.class);
        startActivity(intent);
    }

    public void goHomeBoton(View view){
        goHome();
    }

    public void goCreateAccountBoton(View view){
        goCreateAccount();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onActivityResult(final int requestCode,final int resultCode,final Intent data) {
        Log.i(TAG, requestCode + " : " + resultCode + " : " + data);
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
