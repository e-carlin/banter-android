package com.banter.banter.model.document.attribute;

import lombok.Data;
import lombok.ToString;
import java.util.ArrayList;

@Data
@ToString
public class InstitutionAttribute {

    private String itemId;
    private String name;
    private String institutionId;
    private ArrayList<AccountAttribute> accounts;

    public InstitutionAttribute() {

        this.accounts = new ArrayList<>();
    }

    public InstitutionAttribute(String itemId, String name, String institutionId) {
        this.itemId = itemId;
        this.name = name;
        this.institutionId = institutionId;
        this.accounts = new ArrayList<>();
    }

    public void addAccountAttribute(AccountAttribute accountAttribute) {
        this.accounts.add(accountAttribute);
    }
}
