package com.banter.banter.model.document;

import com.banter.banter.model.document.attribute.AccountAttribute;
import com.banter.banter.model.document.attribute.InstitutionAttribute;
import com.google.firebase.firestore.ServerTimestamp;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

    public HashMap<String, List<AccountAttribute>> getAccountsGroupedByType() {
        HashMap<String, List<AccountAttribute>> finalGrouping = new HashMap<>();

        for(InstitutionAttribute institution : institutions) {
            HashMap<String, List<AccountAttribute>> institutionsAccounts = institution.getAccountsGroupedByType();
            for(String key : institutionsAccounts.keySet()) {
                //It would be more elegant to use Java map.merge(...) but that requires API level 24
                if(finalGrouping.containsKey(key)) {
                    finalGrouping.get(key).addAll(institutionsAccounts.get(key));
                }
                else {
                    finalGrouping.put(key, institutionsAccounts.get(key));
                }
            }
        }
        return finalGrouping;
    }
}
