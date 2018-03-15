package com.banter.banter.model.document.attribute;

import lombok.Data;
import lombok.ToString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@ToString
public class InstitutionAttribute {

    private String itemId;
    private String name;
    private String institutionId;
    private List<AccountAttribute> accounts;

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

    public HashMap<String, List<AccountAttribute>> getAccountsGroupedByType() {
        HashMap<String, List<AccountAttribute>> groupedAccounts = new HashMap<>();
        for(AccountAttribute account : accounts) {
            if(groupedAccounts.containsKey(account.getType())) {
                List<AccountAttribute> accountGroup = groupedAccounts.get(account.getType());
                accountGroup.add(account);
            }
            else {
                List<AccountAttribute> accountGroupToAdd = new ArrayList<>();
                accountGroupToAdd.add(account);
                groupedAccounts.put(account.getType(), accountGroupToAdd);
            }
        }
        return groupedAccounts;
    }
}
