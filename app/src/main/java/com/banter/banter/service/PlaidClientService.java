package com.banter.banter.service;

import android.util.Log;

import com.banter.banter.BuildConfig;
import com.banter.banter.service.listener.ExchangePlaidPublicTokenListener;
import com.banter.banter.service.listener.GetAccountsBalanceListener;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.AccountsBalanceGetRequest;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.response.AccountsBalanceGetResponse;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by evan.carlin on 3/9/2018.
 */

public class PlaidClientService {
    private static final String TAG = "PlaidClientService";

    private PlaidClient plaidClient;
    private String plaidClientId;
    private String plaidSecretKey;
    private String plaidPublicKey;

    public PlaidClientService() {
        this.plaidClientId = BuildConfig.PLAID_CLIENT_ID;
        this.plaidSecretKey = BuildConfig.PLAID_SECRET_KEY;
        this.plaidPublicKey = BuildConfig.PLAID_PUBLIC_KEY;

        this.plaidClient = PlaidClient.newBuilder()
                .clientIdAndSecret(this.plaidClientId, this.plaidSecretKey)
                .publicKey(this.plaidPublicKey) // optional. only needed to call endpoints that require a public key
                .sandboxBaseUrl() // or equivalent, depending on which environment you're calling into
                .build();
    }

    /**
     * Exchange a public token for an item_id and access_token
     *
     * @param publicToken The public token for the institutiojn
     * @return a response containing the resutling item_id and access_token
     */
    public void exchangePublicToken(String publicToken, ExchangePlaidPublicTokenListener listener) {
        Log.d(TAG, "Exchange public token called with token: " + publicToken);


        plaidClient.service().itemPublicTokenExchange(new ItemPublicTokenExchangeRequest(publicToken))
                .enqueue(new Callback<ItemPublicTokenExchangeResponse>() {
                    @Override
                    public void onResponse(Call<ItemPublicTokenExchangeResponse> call, Response<ItemPublicTokenExchangeResponse> response) {
                        if (response.isSuccessful()) {
                            listener.onResponseSuccess(response.body());
                        } else {
                            try {
                                listener.onResponseError(response.errorBody().string());
                            } catch (IOException e) {
                                Log.e(TAG, "Error parsing plaid exchange public token response error body: " + e.getMessage());
                                e.printStackTrace();
                                listener.onResponseError("Could not parse errorBody");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ItemPublicTokenExchangeResponse> call, Throwable t) {
                        Log.e(TAG, "Excahnge public token failure: " + t.getMessage());
                        t.printStackTrace();
                        listener.onFailure(t.getMessage());
                    }
                });


//        plaidClient.service().itemPublicTokenExchange(new ItemPublicTokenExchangeRequest(publicToken)).execute();
//        if (response.isSuccessful()) {
//            Log.d(TAG, "Success exchanging public token. ItemId: " + response.body().getItemId());
//            return response;
//        } else {
//            Log.d(TAG, "Exchange public token response returned errors: " + response.errorBody().string());
//            throw new PlaidExchangePublicTokenException(response.errorBody().string());
//        }
    }

    /**
     * Get balances for all accounts attached to a particular accessToken
     *
     * @param accessToken the accessToken for the accounts
     * @return a response object containing the balances
     */
    public void getAccountsBalance(String accessToken, GetAccountsBalanceListener listener) {
        Log.d(TAG, "Plaid getAccountsBalance called with access token: " + accessToken); //TODO: Remove, we shouldn't be logging access tokens
        this.plaidClient.service().accountsBalanceGet(
                new AccountsBalanceGetRequest(accessToken))
                .enqueue(new Callback<AccountsBalanceGetResponse>() {
                    @Override
                    public void onResponse(Call<AccountsBalanceGetResponse> call, Response<AccountsBalanceGetResponse> response) {
                        if (response.isSuccessful()) {
                            listener.onResponseSuccess(response.body().getAccounts());
                        } else {
                            try {
                                listener.onResponseError(response.errorBody().string());
                            } catch (IOException e) {
                                Log.e(TAG, "Error parsing plaid get accounts balance response error body: " + e.getMessage());
                                e.printStackTrace();
                                listener.onResponseError("Could not parse errorBody");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountsBalanceGetResponse> call, Throwable t) {
                        Log.e(TAG, "Get accounts balance failure: " + t.getMessage());
                        t.printStackTrace();
                        listener.onFailure(t.getMessage());
                    }
                });
    }
}
