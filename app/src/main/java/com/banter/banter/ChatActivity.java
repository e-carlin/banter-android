package com.banter.banter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.banter.banter.adapter.ChatRecyclerViewAdapter;
import com.banter.banter.model.document.ChatDocument;
import com.banter.banter.repository.ChatRepository;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    @BindView(R.id.chat_recycler)
    RecyclerView chatRecycler;

    @BindView(R.id.button_send_message)
    Button sendMessageButton;

    @BindView(R.id.edit_text_message)
    EditText messageToSend;

    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ChatRepository chatRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        FirebaseFirestore.setLoggingEnabled(true);

        this.chatRepository = new ChatRepository();

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        chatRecycler.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();

        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();

        sendMessageButton.setOnClickListener(getSendButtonOnClickListener());
        messageToSend.addTextChangedListener(getMessageToSendTextChangedListener());

        setupChatRecycler();
    }

    private TextWatcher getMessageToSendTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    sendMessageButton.setEnabled(true);
                } else {
                    sendMessageButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

    private View.OnClickListener getSendButtonOnClickListener() {
        return v -> {
            chatRepository.sendMessage(messageToSend.getText().toString(), currentUser.getUid(), this);
            messageToSend.setText("");
        };
    }
    private void setupChatRecycler(){

        FirestoreRecyclerOptions<ChatDocument> response = new FirestoreRecyclerOptions.Builder<ChatDocument>()
                .setQuery(this.chatRepository.getChatsQuery(this.currentUser.getUid()), ChatDocument.class)
                .build();

        adapter = new ChatRecyclerViewAdapter(response);

        adapter.notifyDataSetChanged();
        chatRecycler.setAdapter(adapter);

        /*
        * Scroll to the bottom of the chats if the view is being loaded or if
        * the user is at the bottom and they send a new message
         */
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int numberOfMessages = adapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (numberOfMessages - 1) && lastVisiblePosition == (positionStart - 1))) {
                    chatRecycler.scrollToPosition(positionStart);
                }
            }
        });
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

