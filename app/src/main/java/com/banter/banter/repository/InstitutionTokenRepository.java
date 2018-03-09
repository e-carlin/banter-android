package com.banter.banter.repository;

import com.banter.banter.model.document.InstitutionTokenDocument;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.banter.banter.repository.RepositoryConstants.ACCOUNTS_COLLECTION_REF;

/**
 * Created by evan.carlin on 3/9/2018.
 */

public class InstitutionTokenRepository {
    private FirebaseFirestore db;
    private CollectionReference accountsCollection;

    public InstitutionTokenRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.accountsCollection = db.collection(ACCOUNTS_COLLECTION_REF);
    }

    public void addInstitutionTokenDocument(InstitutionTokenDocument institutionTokenDocument) {
        this.accountsCollection.add(institutionTokenDocument);
    }
}
