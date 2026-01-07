package com.example.vschat;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class chatWin extends AppCompatActivity {

    String recieverimg,reciverUid,reciverName,senderUid;
    CircleImageView profile;
    TextView reciverNName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_win);


        reciverName = getIntent().getStringExtra("nameee");
        recieverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");

        profile = findViewById(R.id.profileimgg);
        reciverNName = findViewById(R.id.recivername);

        Picasso.get().load(recieverimg).into(profile);
        reciverNName.setText(reciverName);






    }
}