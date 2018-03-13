package com.banter.banter.service.listener;

import com.plaid.client.response.ItemPublicTokenExchangeResponse;

import retrofit2.Response;

/**
 * Created by evan.carlin on 3/13/2018.
 */

public interface ExchangePlaidPublicTokenListener {
    void onResponseSuccess(ItemPublicTokenExchangeResponse response);
    void onResponseError(String errorMessage);
    void onFailure(String errorMessage);
}
