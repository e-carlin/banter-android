package com.banter.banter.model.document;

import android.support.annotation.Keep;

import com.banter.banter.model.document.attribute.InstitutionAttribute;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ToString
public class AccountsDocument {

    private String userId;
    @ServerTimestamp private Date createdAt;
    private List<InstitutionAttribute> institutions;

    public AccountsDocument() {
        this.institutions = new ArrayList<>();
    }

    public AccountsDocument(String userId) {
        this.userId = userId;
        this.institutions = new ArrayList<>();
    }

    public void addInstitutionAttribute(InstitutionAttribute institutionAttribute) {
        this.institutions.add(institutionAttribute);
    }
}
