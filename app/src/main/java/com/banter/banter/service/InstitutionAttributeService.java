package com.banter.banter.service;

import android.util.Log;

import com.banter.banter.exceptions.PlaidGetAccountsBalanceException;
import com.banter.banter.model.document.attribute.AccountAttribute;
import com.banter.banter.model.document.attribute.AccountBalancesAttribute;
import com.banter.banter.model.document.attribute.InstitutionAttribute;
import com.plaid.client.response.Account;
import com.plaid.client.response.AccountsBalanceGetResponse;

import java.util.List;

import retrofit2.Response;

/**
 * Created by evan.carlin on 3/12/2018.
 */

public class InstitutionAttributeService {
    private static final String TAG = "InstitutionAttributeSer";

    private PlaidClientService plaidClientService;

    public InstitutionAttributeService() {
        this.plaidClientService = new PlaidClientService();
    }


    public InstitutionAttribute createInstitutionAttribute(String itemId, String institutionName, String institutionId, String accessToken) throws PlaidGetAccountsBalanceException {
        InstitutionAttribute institutionAttribute = new InstitutionAttribute(
                itemId,
                institutionName,
                institutionId);

        Response<AccountsBalanceGetResponse> response = this.plaidClientService.getAccountsBalance(accessToken);
        List<Account> accounts = response.body().getAccounts();
        for (Account account : accounts) {
            AccountBalancesAttribute accountBalancesAttribute = new AccountBalancesAttribute(
                    account.getBalances().getCurrent(),
                    account.getBalances().getAvailable(),
                    account.getBalances().getLimit());

            AccountAttribute accountAttribute = new AccountAttribute(
                    account.getAccountId(),
                    account.getName(),
                    account.getType(),
                    account.getSubtype(),
                    accountBalancesAttribute);

            institutionAttribute.addAccountAttribute(accountAttribute);
        }
        return institutionAttribute;
    }
}
