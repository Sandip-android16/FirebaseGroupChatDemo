package com.example.firebase_group_chat_demo;

import com.google.firebase.database.FirebaseDatabase;

public class MyChatApplication  extends android.app.Application{

    public void onCreate(){
        super.onCreate();
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        instance.setPersistenceEnabled(true);
        instance.getReference().keepSynced(true);
    }
}