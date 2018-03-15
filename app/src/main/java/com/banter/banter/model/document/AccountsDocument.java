package com.banter.banter.model.document;

import com.banter.banter.model.document.attribute.InstitutionAttribute;
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
    @ServerTimestamp
    private Date createdAt;
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

    public List<String> getAccountTypes() {
        List<String> accountTypes = new ArrayList<>();

        //For each institution, get a list of the accountTypes in the institution, add them to our list if they aren't already present in our list
        for (InstitutionAttribute institution : institutions) {
            List<String> institutionAccountTypes = institution.getAccountTypes();
            for (String institutionAccountType : institutionAccountTypes) {
                if (!accountTypes.contains(institutionAccountType)) {
                    accountTypes.add(institutionAccountType);
                }
            }
        }
        return accountTypes;
    }
}
