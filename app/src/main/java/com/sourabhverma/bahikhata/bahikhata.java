package com.sourabhverma.bahikhata;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class bahikhata extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }
}
