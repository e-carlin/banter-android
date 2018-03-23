package com.banter.banter.repository;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.banter.banter.repository.RepositoryConstants.CHAT_COLLECTION_REF;
import static com.banter.banter.repository.RepositoryConstants.CREATED_AT_REF;
import static com.banter.banter.repository.RepositoryConstants.USER_ID_REF;

/**
 * Created by evan.carlin on 3/23/2018.
 */

public class ChatRepository {
    private FirebaseFirestore db;

    public ChatRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public Query getChatsQuery(String userId) {
        return db.collection(CHAT_COLLECTION_REF).whereEqualTo(USER_ID_REF, userId).orderBy(CREATED_AT_REF, Query.Direction.ASCENDING);
    }
}
