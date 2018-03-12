package com.banter.banter.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.banter.banter.model.document.InstitutionTokenDocument;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
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

    public void addInstitutionTokenDocument(InstitutionTokenDocument institutionTokenDocument) {
        this.institutionTokensCollection.add(institutionTokenDocument)
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error adding document: ",e);
                //TODO: think about more error handling here
            }
        });
    }
}
