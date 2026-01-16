package com.example.vschat;

import static com.example.vschat.chatWin.reciverIImg;
import static com.example.vschat.chatWin.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class messagesAdpter extends RecyclerView.Adapter {
    Context context;
    ArrayList<msgModelclass> messagesAdopterArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECIVE = 2;



    public messagesAdpter(Context context, ArrayList<msgModelclass> messagesAdopterArrayList) {
        this.context = context;
        this.messagesAdopterArrayList = messagesAdopterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderviewholder(view);

        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout, parent, false);
            return new reciverviewholder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       msgModelclass messages = messagesAdopterArrayList.get(position);
       if (holder.getClass() == senderviewholder.class) {
           senderviewholder viewHolder = (senderviewholder) holder;
           viewHolder.msgtxt.setText(messages.getMessage());
             Picasso.get().load(senderImg).into(viewHolder.cirecleImageView);
       }
           else{
               reciverviewholder viewHolder = (reciverviewholder) holder;
               viewHolder.msgtxt.setText(messages.getMessage());
               Picasso.get().load(reciverIImg).into(viewHolder.cirecleImageView);


           }



       }




    @Override
    public int getItemCount() {
        return messagesAdopterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        msgModelclass messages = messagesAdopterArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECIVE;
        }
    }

    class senderviewholder extends RecyclerView.ViewHolder {
        CircleImageView cirecleImageView;
        TextView msgtxt;

        public senderviewholder(@NonNull View itemView) {
            super(itemView);
            cirecleImageView = itemView.findViewById(R.id.profilerggg);
            msgtxt = itemView.findViewById(R.id.msgsendertyp);
        }
    }
    class reciverviewholder extends RecyclerView.ViewHolder {
        CircleImageView cirecleImageView;
        TextView msgtxt;
        public reciverviewholder(@NonNull View itemView) {
            super(itemView);
            cirecleImageView = itemView.findViewById(R.id.pro);
            msgtxt = itemView.findViewById(R.id.recivertextset);

        }
    }

}
