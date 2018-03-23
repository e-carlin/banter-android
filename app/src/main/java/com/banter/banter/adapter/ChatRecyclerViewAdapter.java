package com.banter.banter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.banter.banter.R;
import com.banter.banter.model.document.ChatDocument;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by evan.carlin on 3/23/2018.
 */

public class ChatRecyclerViewAdapter extends FirestoreRecyclerAdapter<ChatDocument, ChatRecyclerViewAdapter.MessageHolder>{


    public ChatRecyclerViewAdapter(FirestoreRecyclerOptions<ChatDocument> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position, ChatDocument model) {
        holder.messageTextView.setText(model.getMessage());
        holder.messengerTextView.setText(model.getUserId());
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.item_message, group, false);

        return new MessageHolder(view);
    }


    public class MessageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_message)
        TextView messageTextView;
        @BindView(R.id.text_view_messenger)
        TextView messengerTextView;


        public MessageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
