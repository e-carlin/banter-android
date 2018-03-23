package com.banter.banter.repository;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.banter.banter.model.document.ChatDocument;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.banter.banter.repository.RepositoryConstants.CHAT_COLLECTION_REF;
import static com.banter.banter.repository.RepositoryConstants.CREATED_AT_REF;
import static com.banter.banter.repository.RepositoryConstants.USER_ID_REF;

/**
 * Created by evan.carlin on 3/23/2018.
 */

public class ChatRepository {
    private static final String TAG = "ChatRepository";
    private FirebaseFirestore db;
    private CollectionReference chatCollection;

    public ChatRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.chatCollection = db.collection(CHAT_COLLECTION_REF);
    }

    public Query getChatsQuery(String userId) {
        return chatCollection.whereEqualTo(USER_ID_REF, userId).orderBy(CREATED_AT_REF, Query.Direction.ASCENDING);
    }

    public void sendMessage(String message, String userId, Context ctx) {
        ChatDocument chatDocument = new ChatDocument(message, userId);
       Task<DocumentReference> ref = this.chatCollection.add(chatDocument);
       ref.addOnFailureListener(e -> {
           Log.e(TAG, "There was an error sending the chat message: "+e.getMessage());
           e.printStackTrace();
           Toast.makeText(ctx, "Agh! There was a problem. Please send your message again.",
                   Toast.LENGTH_SHORT).show();
       });
    }
}
