package com.banter.banter.service;

import android.util.Log;

import com.banter.banter.exceptions.PlaidExchangePublicTokenException;
import com.banter.banter.model.document.InstitutionTokenDocument;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;

import retrofit2.Response;

/**
 * Created by evan.carlin on 3/9/2018.
 */

public class InstitutionTokenService {
    public static String TAG = "InstitutionTokenService";

    private PlaidClientService plaidClientService;
    public InstitutionTokenService() {
        this.plaidClientService = new PlaidClientService();
    }

    public InstitutionTokenDocument getInstitutionTokenDocument(String publicToken, String userId) throws PlaidExchangePublicTokenException {
        Log.d(TAG, "Getting institution token document publicToken: "+publicToken+" userId: "+userId);
        Response<ItemPublicTokenExchangeResponse> response = plaidClientService.exchangePublicToken(publicToken);

        InstitutionTokenDocument institutionTokenDocument = new InstitutionTokenDocument();
        institutionTokenDocument.setAccessToken(response.body().getAccessToken());
        institutionTokenDocument.setItemId(response.body().getItemId());
        institutionTokenDocument.setUserId(userId);

        return institutionTokenDocument;
    }
}
