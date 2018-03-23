package com.banter.banter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.banter.banter.ChatActivity;
import com.banter.banter.R;
import com.banter.banter.model.document.ChatDocument;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by evan.carlin on 3/23/2018.
 */

public class ChatRecyclerViewAdapter extends FirestoreRecyclerAdapter<ChatDocument, ChatRecyclerViewAdapter.ChatHolder>{


    public ChatRecyclerViewAdapter(FirestoreRecyclerOptions<ChatDocument> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position, ChatDocument model) {
        holder.messageTextView.setText(model.getMessage());
        holder.messengerTextView.setText(model.getUserId());
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.item_message, group, false);

        return new ChatHolder(view);
    }


    public class ChatHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.messageTextView)
        TextView messageTextView;
        @BindView(R.id.messengerTextView)
        TextView messengerTextView;


        public ChatHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
