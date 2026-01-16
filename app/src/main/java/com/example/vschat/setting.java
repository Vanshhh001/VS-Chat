package com.example.vschat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;


public class setting extends AppCompatActivity {

    ImageView setprofile;
    EditText setname, setstatus;
    Button donebut;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String email, password, name;
    Uri setImageUri;
    String existingImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        setprofile = findViewById(R.id.settingprofile);
        setname = findViewById(R.id.settingname);
        setstatus = findViewById(R.id.settingstatus);
        donebut = findViewById(R.id.donebut);

        DatabaseReference reference = database.getReference().child("Users").child(auth.getUid());
        StorageReference storageReference = storage.getReference().child("Upload").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    // 2. Use safe retrieval for every field
                    email = snapshot.child("mail").getValue() != null ?
                            snapshot.child("mail").getValue().toString() : "";

                    name = snapshot.child("userName").getValue() != null ?
                            snapshot.child("userName").getValue().toString() : "";

                    existingImageUri = snapshot.child("profilepic").getValue() != null ?
                            snapshot.child("profilepic").getValue().toString() : "";

                    String status = snapshot.child("status").getValue() != null ?
                            snapshot.child("status").getValue().toString() : "";

                    // 3. Set UI text safely
                    setname.setText(name);
                    setstatus.setText(status);

                    if (!existingImageUri.isEmpty()) {
                        Picasso.get().load(existingImageUri).into(setprofile);
                    }
                } else {
                    // Handle case where user data hasn't been created yet
                    Log.d("SettingActivity", "User data does not exist in database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 10);

            }

        });
        donebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namee = setname.getText().toString();
                String statuss = setstatus.getText().toString();

                // 1. If user selected a NEW image from gallery
                if (setImageUri != null) {
                    storageReference.putFile(setImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String finalImageUri = uri.toString();
                                        updateDatabase(namee, finalImageUri, statuss);
                                    }
                                });
                            }
                        }
                    });
                }
                // 2. If user did NOT select a new image, just use the old one (or empty string)
                else {
                    updateDatabase(namee, existingImageUri, statuss);
                }
            }

            private void updateDatabase(String namee, String finalImageUri, String statuss) {
                Users users = new Users(auth.getUid(), namee, email, password, finalImageUri, statuss);

                database.getReference().child("Users").child(auth.getUid()).setValue(users)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(setting.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(setting.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(setting.this, "Update Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 10) {
                if (data != null) {
                    setImageUri = data.getData();
                    setprofile.setImageURI(setImageUri);
                    //uploadImage(data.getData());

                }
            }

        }
    }


