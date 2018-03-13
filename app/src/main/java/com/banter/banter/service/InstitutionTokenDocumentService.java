package com.banter.banter.service;

import android.util.Log;

import com.banter.banter.model.document.InstitutionTokenDocument;
import com.banter.banter.service.listener.ExchangePlaidPublicTokenListener;
import com.banter.banter.service.listener.GetInstitutionTokenDocumentListener;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;

/**
 * Created by evan.carlin on 3/9/2018.
 */

public class InstitutionTokenDocumentService {
    public static String TAG = "InstitutionTokenDocumentService";

    private PlaidClientService plaidClientService;

    public InstitutionTokenDocumentService() {
        this.plaidClientService = new PlaidClientService();
    }

    public void getInstitutionTokenDocument(String publicToken, String userId, GetInstitutionTokenDocumentListener listener) {
        Log.d(TAG, "Getting institution token document publicToken: "+publicToken+" userId: "+userId);
        plaidClientService.exchangePublicToken(publicToken, new ExchangePlaidPublicTokenListener() {
            @Override
            public void onResponseSuccess(ItemPublicTokenExchangeResponse response) {
                InstitutionTokenDocument institutionTokenDocument = new InstitutionTokenDocument();
                institutionTokenDocument.setAccessToken(response.getAccessToken());
                institutionTokenDocument.setItemId(response.getItemId());
                institutionTokenDocument.setUserId(userId);

                Log.d(TAG, "Success getting accessToken from plaid. InstitutionTokenDocument is: "+institutionTokenDocument);
                listener.onSuccess(institutionTokenDocument);
            }

            @Override
            public void onResponseError(String errorMessage) {
                Log.e(TAG, "Cannot create InstitutionTokenDocument. Error response from Plaid exchange public token: "+errorMessage);
                listener.onFailure(errorMessage);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "Cannot create InstitutionTokenDocument. Failure response from Plaid exchange public token: "+errorMessage);
                listener.onFailure(errorMessage);
            }
        });
    }
}
