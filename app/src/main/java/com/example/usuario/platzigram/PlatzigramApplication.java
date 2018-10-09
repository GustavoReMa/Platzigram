package com.example.usuario.platzigram;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PlatzigramApplication extends Application {
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseStorage firebaseStorage;
    //Comentario
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        firebaseAuth    = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser!=null){
                    Log.w("PlatzigramApplication","Usuario logeado " + firebaseUser.getEmail());
                }else{
                    Log.w("PlatzigramApplication","Usuario no logeado ");
                }
            }
        };
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public StorageReference getStorageReference(){

        return firebaseStorage.getReference();
    }
}
