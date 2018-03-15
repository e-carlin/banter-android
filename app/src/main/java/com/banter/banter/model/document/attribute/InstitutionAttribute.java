package com.banter.banter.model.document.attribute;

import lombok.Data;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Create a list of the account types in this InstitutionAttribute
     * The list contains one entry for each unique account type
     * @return
     */
    public List<String> getAccountTypes() {
        List<String> accountTypes = new ArrayList<>();
        for(AccountAttribute account : accounts) {
            if(!accountTypes.contains(account.getType())) {
                accountTypes.add(account.getType());
            }
        }
        return accountTypes;
    }
}
