package com.banter.banter.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.banter.banter.model.document.AccountsDocument;
import com.banter.banter.model.document.attribute.InstitutionAttribute;
import com.banter.banter.repository.listener.DoesUserHaveInstitutionListener;
import com.banter.banter.service.listener.AddDocumentListener;
import com.banter.banter.repository.listener.GetDocumentListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

    public void addAccountsDocument(AccountsDocument accountsDocument, AddDocumentListener listener) {
        this.accountsCollection.add(accountsDocument)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    } else {
                        listener.onFailure("Add AccountsDocument task was unsuccessful");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding document: ", e);
                    listener.onFailure(e.getMessage());
                });
    }


    public void doesUserHaveInstitution(String userId, String institutionId, DoesUserHaveInstitutionListener listener) {
        getMostRecentAccountsDocument(userId, new GetDocumentListener() {
            @Override
            public void onSuccess(Object document) {
                AccountsDocument accountsDocument = (AccountsDocument) document;
                for (InstitutionAttribute institutionAttribute : accountsDocument.getInstitutions()) {
                    if (institutionAttribute.getInstitutionId().equals(institutionId)) {
                        listener.userHasInstitution();
                        return;
                    }
                }
                listener.userDoesNotHaveInstitution();
            }

            @Override
            public void onEmptyResult() {
                listener.userDoesNotHaveInstitution();
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "Error getting the most recent accounts document: " + errorMessage);
                listener.onFailure(errorMessage);
            }
        });
    }

    public void addInstitutionAttribute(String userId, InstitutionAttribute institutionAttribute, AddDocumentListener listener) {
        Log.d(TAG, "Trying to add a new institutionAttribute for user: " + userId);
        getMostRecentAccountsDocument(userId, new GetDocumentListener() {
            @Override
            public void onSuccess(Object document) {
                AccountsDocument accountsDocument = (AccountsDocument) document;
                Log.d(TAG, "Got the most recent accountsDocument: " + accountsDocument);
                Log.d(TAG, "Adding institution attribute: " + institutionAttribute);
                accountsDocument.addInstitutionAttribute(institutionAttribute);
                accountsDocument.setCreatedAt(null); //Since we are copying an old accountsDocument and adding the new  institutionAttribute to it
                // we need to set the createdAt timestamp to null so that Firebase updates the createdAt timestamp
                accountsCollection.add(accountsDocument)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Error adding accounts document: ", e);
                                listener.onFailure(e.getMessage());
                            }
                        }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Successfully added the new institution attribute to the most recent accountsDocument and added the new updated doc to the db");
                            listener.onSuccess();
                        } else {
                            Log.e(TAG, "Error adding the new updated accountsDocument to the db");
                            listener.onFailure(task.getException().getMessage());
                        }
                    }
                });
            }

            @Override
            public void onEmptyResult() { //No existing accountsDocument for this user so create one
                //Create a new AccountsDocument object
                //Add this institutionattribute to it
                //Save
                Log.d(TAG, "No existing accountsDocument. Creating a new one for the user....");
                AccountsDocument accountsDocument = new AccountsDocument(userId);
                accountsDocument.addInstitutionAttribute(institutionAttribute);
                Log.d(TAG, "Added institutionAttribute to new accounts document");
                addAccountsDocument(accountsDocument, new AddDocumentListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Successfully added the accounts document to the db");
                        listener.onSuccess();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e(TAG, "Error adding the new accounts document to the db: " + errorMessage);
                        listener.onFailure(errorMessage);
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "Error trying to retrieve the latest accounts document: " + errorMessage);
                listener.onFailure(errorMessage);
            }
        });
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
