package com.banter.banter.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.banter.banter.repository.RepositoryConstants.CREATED_AT_REF;
import static com.banter.banter.repository.RepositoryConstants.TRANSACTIONS_COLLECTION_REF;
import static com.banter.banter.repository.RepositoryConstants.USER_ID_REF;

/**
 * Created by evan.carlin on 3/22/2018.
 */

public class TransactionsRepository {
    private final String TAG = "TransactionsRepository";

    public static final String TRANSACTION_DATE_REF = "transactionDate";

    private FirebaseFirestore db;
    private CollectionReference transactionsCollection;

    public TransactionsRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.transactionsCollection = db.collection(TRANSACTIONS_COLLECTION_REF);
    }

    public Query getTransactionsQuery(String userId) {
        return transactionsCollection.whereEqualTo(USER_ID_REF, userId).orderBy(TRANSACTION_DATE_REF, Query.Direction.DESCENDING);
    }
}
