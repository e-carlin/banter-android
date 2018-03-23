package com.banter.banter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.banter.banter.model.document.ChatDocument;
import com.banter.banter.model.document.ChatDocument;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    @BindView(R.id.chat_recycler)
    RecyclerView chatRecycler;

    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        FirebaseFirestore.setLoggingEnabled(true);

        init();
        getFriendList();
    }

    private void init(){

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        chatRecycler.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
    }

    private void getFriendList(){
        Query query = db.collection("chats"); //TODO: Limit to only user chats, oder them,...

        FirestoreRecyclerOptions<ChatDocument> response = new FirestoreRecyclerOptions.Builder<ChatDocument>()
                .setQuery(query, ChatDocument.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ChatDocument, ChatActivity.ChatHolder>(response) {
            @Override
            public void onBindViewHolder(ChatActivity.ChatHolder holder, int position, ChatDocument model) {
                holder.messageTextView.setText(model.getMessage());
                holder.messengerTextView.setText(model.getUserId());
            }

            @Override
            public ChatActivity.ChatHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_message, group, false);

                return new ChatActivity.ChatHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        chatRecycler.setAdapter(adapter);
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

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}

