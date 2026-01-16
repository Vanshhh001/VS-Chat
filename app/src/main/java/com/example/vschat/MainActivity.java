package com.example.vschat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    ImageView imglogout;
    ImageView camBut;
    ImageView settingBut;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_main);


        database=FirebaseDatabase.getInstance();
        auth  = FirebaseAuth.getInstance();

        camBut = findViewById(R.id.camBut);
        settingBut = findViewById(R.id.settingBut);


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = database.getReference().child("Users");

        usersArrayList = new ArrayList<>();
        mainUserRecyclerView = findViewById(R.id.mainUserRecycle);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(MainActivity.this,usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    // Check if the data is actually a Map/Object before converting
                    if (dataSnapshot.getValue() != null) {
                        try {
                            Users users = dataSnapshot.getValue(Users.class);
                            if (users != null && !users.getUserId().equals(auth.getUid())) {
                                usersArrayList.add(users);
                            }
                        } catch (Exception e) {
                            // This prevents the app from crashing if there is one "bad" entry
                            Log.e("FirebaseError", "Data conversion failed for: " + dataSnapshot.getKey());
                        }
                    }
                    Users users = dataSnapshot.getValue(Users.class);

                    if (users != null && !users.getUserId().equals(auth.getUid())) {
                        usersArrayList.add(users);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        imglogout = findViewById(R.id.logoutimg);      //for mainactivity logout
        imglogout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Dialog dialog = new Dialog(MainActivity.this,R.style.dialog);
            dialog.setContentView(R.layout.dialog_layout);
            Button no,yes;
             yes = dialog.findViewById(R.id.yesbtn);
             no = dialog.findViewById(R.id.nobtn);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, login.class);
                    startActivity(intent);
                    finish();
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

          }
        });

//        mainUserRecyclerView = findViewById(R.id.mainUserRecycle);
//        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new UserAdapter(MainActivity.this,usersArrayList);
//
//        mainUserRecyclerView.setAdapter(adapter);

        settingBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, setting.class);
                startActivity(intent);
            }
        });

        camBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 10);
            }
        });

        if(auth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);


        }


    }
}