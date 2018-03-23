package com.banter.banter.repository;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * Created by evan.carlin on 3/23/2018.
 */

public class ChatRepository {

    private FirebaseFirestore db;

    public ChatRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public Query getChatsQuery(String userId) {
        return db.collection("chats").whereEqualTo("userId", userId).orderBy("createdAt", Query.Direction.ASCENDING);
    }
}
