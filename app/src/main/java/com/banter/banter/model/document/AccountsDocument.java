package com.banter.banter.model.document;

import com.banter.banter.model.document.attribute.InstitutionAttribute;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ToString
public class AccountsDocument {

    private String userId;
    private Date createdAt;
    private List<InstitutionAttribute> institutions;

    public AccountsDocument() {
        this.institutions = new ArrayList<>();
    }


    public String getUserId() { return this.userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public void addInstitutionAttribute(InstitutionAttribute institutionAttribute) {
        this.institutions.add(institutionAttribute);
    }
}
