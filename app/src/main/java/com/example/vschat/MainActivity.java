package com.example.vschat;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);


        setContentView(R.layout.activity_main);

        auth  = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);


        }


    }
}