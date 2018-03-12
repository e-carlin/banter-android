package com.banter.banter.service;

import android.util.Log;

import com.banter.banter.BuildConfig;
import com.banter.banter.exceptions.PlaidExchangePublicTokenException;
import com.banter.banter.exceptions.PlaidGetAccountsBalanceException;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.AccountsBalanceGetRequest;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.response.AccountsBalanceGetResponse;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;

import java.io.IOException;

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
     * @throws PlaidExchangePublicTokenException
     */
    public Response<ItemPublicTokenExchangeResponse> exchangePublicToken(String publicToken) throws PlaidExchangePublicTokenException {
        Log.d(TAG, "Exchange public token called with token: " + publicToken);
        try {
            Response<ItemPublicTokenExchangeResponse> response = plaidClient.service().itemPublicTokenExchange(new ItemPublicTokenExchangeRequest(publicToken)).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "Success exchanging public token. ItemId: " + response.body().getItemId());
                return response;
            } else {
                Log.d(TAG, "Exchange public token response returned errors: " + response.errorBody().string());
                throw new PlaidExchangePublicTokenException(response.errorBody().string());
            }
        } catch (IOException e) {
            Log.d(TAG, "IOException when trying to exchange public token: " + e.getMessage());
            throw new PlaidExchangePublicTokenException(e.getMessage());
        }
    }

    /**
     * Get balances for all accounts attached to a particular accessToken
     *
     * @param accessToken the accessToken for the accounts
     * @return a response object containing the balances
     * @throws PlaidGetAccountsBalanceException
     */
    public Response<AccountsBalanceGetResponse> getAccountsBalance(String accessToken) throws PlaidGetAccountsBalanceException {
        Log.d(TAG, "Plaid getAccountsBalance called with access token: " + accessToken); //TODO: Remove, we shouldn't be logging access tokens
        try {
            Response<AccountsBalanceGetResponse> response = this.plaidClient.service().accountsBalanceGet(
                    new AccountsBalanceGetRequest(accessToken))
                    .execute();
            if(response.errorBody() != null) {
                Log.e(TAG, "Response from plaid getAccountsBalance has an error: "+response.errorBody().string());
                throw new PlaidGetAccountsBalanceException(response.errorBody().string());
            }
            return response;
        } catch (IOException e) {
            Log.e(TAG, "Error getting balances from Plaid: " + e.getMessage());
            throw new PlaidGetAccountsBalanceException(e.getMessage());
        }
    }
}
