package com.banter.banter.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.banter.banter.model.document.AccountsDocument;
import com.banter.banter.repository.listener.GetDocumentListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import static com.banter.banter.repository.RepositoryConstants.ACCOUNTS_COLLECTION_REF;

/**
 * Created by evan.carlin on 3/12/2018.
 */

public class AccountsRepository {
    private static final String TAG = "AccountsRepository";

    private FirebaseFirestore db;
    private CollectionReference accountsCollection;

    public AccountsRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.accountsCollection = db.collection(ACCOUNTS_COLLECTION_REF);
    }


    public void getMostRecentAccountsDocument(String userId, GetDocumentListener listener) {
        Query query = accountsCollection.whereEqualTo("userId", userId).orderBy("createdAt", Query.Direction.DESCENDING).limit(1);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) { //No AccountsDocuments for this user so they don't have this institution
                        listener.onEmptyResult();
                    } else {
                        AccountsDocument accountsDocument = task.getResult().getDocuments().get(0).toObject(AccountsDocument.class);
                        listener.onSuccess(accountsDocument);
                    }
                } else {
                    Log.e(TAG, "Error getting the most recent accounts document: " + task.getException().getMessage());
                    listener.onFailure(task.getException().getMessage());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failure getting most recent accounts document", e);
                listener.onFailure(e.getMessage());
            }
        });
    }

    //TODO: This listener persists for the life of the (?) app. Add the activity context to the call to addSnapshotListener() to make it last only the life of the activity
    public void listenToMostRecentAccountsDocument(String userId, GetDocumentListener listener) {
        Query query = accountsCollection.whereEqualTo("userId", userId).orderBy("createdAt", Query.Direction.DESCENDING).limit(1);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null) {
                    listener.onFailure(e.getMessage());
                    Log.e(TAG, "Error on listenToMostRecentAccountsDocument: "+e.getMessage());
                }
                else {
                    if(documentSnapshots == null || documentSnapshots.isEmpty()) {
                        listener.onEmptyResult();
                    }
                    else{
                        listener.onSuccess(documentSnapshots.getDocuments().get(0).toObject(AccountsDocument.class));
                    }
                }
            }
        });
    }
}
