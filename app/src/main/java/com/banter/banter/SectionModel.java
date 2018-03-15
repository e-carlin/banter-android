package com.banter.banter;

import com.banter.banter.model.document.attribute.AccountAttribute;

import java.util.List;

public class SectionModel {
    private String accountType;
    private List<AccountAttribute> accounts;

    public SectionModel(String accountType, List<AccountAttribute> accounts) {
        this.accountType = accountType;
        this.accounts = accounts;
    }

    public String getAccountType() {
        return accountType;
    }

    public List<AccountAttribute> getAccounts() {
        return accounts;
    }
}
