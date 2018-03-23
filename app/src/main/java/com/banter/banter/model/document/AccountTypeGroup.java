package com.banter.banter.model.document;

import com.banter.banter.model.document.attribute.AccountAttribute;

import java.util.List;

public class AccountTypeGroup {
    private String accountType;
    private List<AccountAttribute> accounts;

    public AccountTypeGroup(String accountType, List<AccountAttribute> accounts) {
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
