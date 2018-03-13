package com.banter.banter.service;

import android.util.Log;

import com.banter.banter.model.document.attribute.AccountAttribute;
import com.banter.banter.model.document.attribute.AccountBalancesAttribute;
import com.banter.banter.model.document.attribute.InstitutionAttribute;
import com.banter.banter.service.listener.CreateInstitutionAttributeListener;
import com.banter.banter.service.listener.GetAccountsBalanceListener;
import com.plaid.client.response.Account;

import java.util.List;

/**
 * Created by evan.carlin on 3/12/2018.
 */

public class InstitutionAttributeService {
    private static final String TAG = "InstitutionAttributeSer";

    private PlaidClientService plaidClientService;

    public InstitutionAttributeService() {
        this.plaidClientService = new PlaidClientService();
    }


    public void createInstitutionAttribute(String itemId, String institutionName, String institutionId, String accessToken, CreateInstitutionAttributeListener listener) {
        InstitutionAttribute institutionAttribute = new InstitutionAttribute(
                itemId,
                institutionName,
                institutionId);

        this.plaidClientService.getAccountsBalance(accessToken, new GetAccountsBalanceListener() {
            @Override
            public void onResponseSuccess(List<Account> accounts) {
//                List<Account> accounts = response.body().getAccounts();
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
                listener.onSuccess(institutionAttribute);
            }

            @Override
            public void onResponseError(String errorMessage) {
                Log.e(TAG, "Cannot create institutionAttribute. Error response from Plaid get balances: "+errorMessage);
                listener.onFailure(errorMessage);
            }
            
            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "Cannot create institutionAttribute. Error response from Plaid get balances: "+errorMessage);
                listener.onFailure(errorMessage);
            }
        });
    }
}
