package com.banter.banter.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.banter.banter.model.document.InstitutionTokenDocument;
import com.banter.banter.service.listener.AddDocumentListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.banter.banter.repository.RepositoryConstants.INSTITUTION_TOKENS_COLLECTION_REF;

/**
 * Created by evan.carlin on 3/9/2018.
 */

public class InstitutionTokenRepository {
    private static final String TAG = "InstitutionTokenRepo";

    private FirebaseFirestore db;
    private CollectionReference institutionTokensCollection;

    public InstitutionTokenRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.institutionTokensCollection = db.collection(INSTITUTION_TOKENS_COLLECTION_REF);
    }

    public void addInstitutionTokenDocument(InstitutionTokenDocument institutionTokenDocument, AddDocumentListener listener) {
        this.institutionTokensCollection.add(institutionTokenDocument)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()) {
                            listener.onSuccess();
                        }
                        else {
                            listener.onFailure(task.getException().getMessage());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding document: ", e);
                        listener.onFailure(e.getMessage());
                    }
                });
    }
}
