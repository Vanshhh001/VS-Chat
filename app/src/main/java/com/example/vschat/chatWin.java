package com.example.vschat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class chatWin extends AppCompatActivity {

    String recieverimg, reciverUid, reciverName, senderUid;
    CircleImageView profile;
    TextView reciverNName;
    CardView sendbtn;
    EditText textmsg;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    public static String senderImg;
    public static String reciverIImg;
    String senderRoom, reciverRoom;
    RecyclerView msgadaptor;
    ArrayList<msgModelclass> messagesArrayList;
    messagesAdpter messageAdpter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_win);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // 2. Intent se data pehle lein (Taki UID mil jaye)
        reciverName = getIntent().getStringExtra("nameee");
        recieverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");

        senderUid = firebaseAuth.getUid();

        // 3. Room IDs ab banayein (Jab IDs mil chuki hain)
        senderRoom = senderUid + reciverUid;
        reciverRoom = reciverUid + senderUid;

        // 4. Views aur List setup
        messagesArrayList = new ArrayList<>();
        msgadaptor = findViewById(R.id.msgadaptor);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        msgadaptor.setLayoutManager(linearLayoutManager);

        messageAdpter = new messagesAdpter(chatWin.this, messagesArrayList);
        msgadaptor.setAdapter(messageAdpter);

        sendbtn = findViewById(R.id.sendbtnn);
        textmsg = findViewById(R.id.textmsg);
        profile = findViewById(R.id.profileimgg);
        reciverNName = findViewById(R.id.recivername);

        Picasso.get().load(recieverimg).into(profile);
        reciverNName.setText(reciverName);

        // 5. Data Fetching (Path: "chats" use karein, "user" nahi)
        DatabaseReference chatreference = database.getReference().child("chats").child(senderRoom).child("messages");

        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    msgModelclass messages = dataSnapshot.getValue(msgModelclass.class);
                    messagesArrayList.add(messages);
                }
                messageAdpter.notifyDataSetChanged();
            }

           @Override
          public void onCancelled(@NonNull DatabaseError error) {
         }
     });



//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//              senderImg = snapshot.child("profilepic").getValue().toString();
//                reciverIImg = recieverimg;
//
//
//           @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        senderUid = FirebaseAuth.getInstance().getUid();
        senderRoom = senderUid + reciverUid;
        reciverRoom = reciverUid + senderUid;



        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = textmsg.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(chatWin.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
                    return;
                }
                textmsg.setText("");
                Date date = new Date();
                msgModelclass messagess = new msgModelclass(senderUid, message, date.getTime());
                database.getReference().child("chats").child(senderRoom).child("messages")
                        .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats").child(reciverRoom).child("messages")
                                        .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                            }
                        });


            }

        });

    }
}



