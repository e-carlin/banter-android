package com.banter.banter.service;

import android.util.Log;

import com.banter.banter.BuildConfig;
import com.banter.banter.exceptions.PlaidExchangePublicTokenException;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
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
     * @param publicToken The public token for the institutiojn
     * @return a response containing the resutling item_id and access_token
     * @throws PlaidExchangePublicTokenException
     */
    public Response<ItemPublicTokenExchangeResponse> exchangePublicToken(String publicToken) throws PlaidExchangePublicTokenException {
        Log.d(TAG, "Exchange public token called with token: "+publicToken);
        try {
            Response<ItemPublicTokenExchangeResponse> response = plaidClient.service().itemPublicTokenExchange(new ItemPublicTokenExchangeRequest(publicToken)).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "Success exchanging public token. ItemId: "+response.body().getItemId());
                return response;
            } else {
                Log.d(TAG, "Exchange public token response returned errors: "+response.errorBody().string());
                throw new PlaidExchangePublicTokenException(response.errorBody().string());
            }
        } catch (IOException e) {
            Log.d(TAG, "IOException when trying to exchange public token: "+e.getMessage());
            throw new PlaidExchangePublicTokenException(e.getMessage());
        }
    }

}
