package com.banter.banter.service.listener;

import com.plaid.client.response.Account;

import java.util.List;

/**
 * Created by evan.carlin on 3/13/2018.
 */

public interface GetAccountsBalanceListener {
    void onResponseSuccess(List<Account> accounts);
    void onResponseError(String errorMessage);
    void onFailure(String errorMessage);
}
