package com.banter.banter.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.banter.banter.model.document.AccountsDocument;
import com.banter.banter.model.document.attribute.InstitutionAttribute;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import static com.banter.banter.repository.RepositoryConstants.ACCOUNTS_COLLECTION_REF;

/**
 * Created by evan.carlin on 3/12/2018.
 */

public class AccountsRepository {
    private  static final String TAG = "AccountsRepository";

    private FirebaseFirestore db;
    private CollectionReference accountsCollection;

    public AccountsRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.accountsCollection = db.collection(ACCOUNTS_COLLECTION_REF);
    }

    public void addAccountsDocument(AccountsDocument accountsDocument) {
        this.accountsCollection.add(accountsDocument)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding document: ",e);
                        //TODO: think about more error handling here
                    }
                });
    }

    public void doesUserHaveInstitution(String userId, String institutionId, DoesUserHaveInstitutionListener listener) {
        Log.d(TAG, "doesUserHaveInstitution called");
        Query query = accountsCollection.whereEqualTo("userId", userId).orderBy("createdAt").limit(1);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG, "In oncomplete");
                if(task.isSuccessful()) {
                    Log.d(TAG, "Task was successful");
                    if(task.getResult().isEmpty()) { //No AccountsDocuments for this user so they don't have this institution
                        Log.d(TAG, "Result was empty");
                        listener.userDoesNotHaveInstitution();
                    }
                    else {
                        Log.d(TAG, "There was a result accountsDocument");
                        AccountsDocument accountsDocument = task.getResult().getDocuments().get(0).toObject(AccountsDocument.class);
                        for(InstitutionAttribute institutionAttribute : accountsDocument.getInstitutions()) {
                            if(institutionAttribute.getInstitutionId().equals(institutionId)) {
                                Log.d(TAG, "A matching institution_id was found");
                                listener.userHasInstitution();
                                return;
                            }
                        }
                        Log.d(TAG, "A matching institution id was not found.");
                        listener.userDoesNotHaveInstitution();
                    }
                }
                else {
                    Log.e(TAG, "Task was unsuccessful.");
                    Log.e(TAG, task.getException().getMessage());
                    //TODO: Is this in error. What should we do here?
                    listener.queryError();
                }
            }
        });
    }
}
