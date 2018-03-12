package com.banter.banter.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.banter.banter.exceptions.PlaidExchangePublicTokenException;
import com.banter.banter.exceptions.PlaidGetAccountsBalanceException;
import com.banter.banter.model.document.AccountsDocument;
import com.banter.banter.model.document.InstitutionTokenDocument;
import com.banter.banter.model.document.attribute.InstitutionAttribute;
import com.banter.banter.model.response.PlaidLinkResponse;
import com.banter.banter.repository.AccountsRepository;
import com.banter.banter.repository.InstitutionTokenRepository;
import com.banter.banter.repository.DoesUserHaveInstitutionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Created by evan.carlin on 3/12/2018.
 */

public class AddAccountService extends IntentService {
    private static final String TAG = "AddAccountService";

    private InstitutionTokenService institutionTokenService;
    private InstitutionTokenRepository institutionTokenRepository;
    private InstitutionAttributeService institutionAttributeService;
    private AccountsRepository accountsRepository;
    private FirebaseUser currentUser;

    public AddAccountService() {
        super("AddAccountService");

        this.institutionTokenService = new InstitutionTokenService();
        this.institutionTokenRepository = new InstitutionTokenRepository();
        this.institutionAttributeService = new InstitutionAttributeService();
        this.accountsRepository = new AccountsRepository();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e(TAG, "In AddAccountService");
        //Check if this is a duplicate account
        PlaidLinkResponse plaidLinkResponse = (PlaidLinkResponse) intent.getSerializableExtra("plaidLinkResponse");
        accountsRepository.doesUserHaveInstitution(this.currentUser.getUid(), plaidLinkResponse.getInstitutionId(), new DoesUserHaveInstitutionListener() {
            @Override
            public void queryError() {
                Log.e(TAG, "Query error");
            }

            @Override
            public void userHasInstitution() {
                //TODO: alert the user that they tried to add a duplicate account
                Log.e(TAG, "The user has already added an institution with ins_id = " + plaidLinkResponse.getInstitutionId() + " name: " + plaidLinkResponse.getInstitutionName());
            }

            @Override
            public void userDoesNotHaveInstitution() {
                System.out.println("User does not have institution");
                //Needs to be done async becasuse firebase listeners are alway called on the ui thread
                //TODO: Finish implementing
                AsyncTask.execute(() -> {
                    try {
                        InstitutionTokenDocument institutionTokenDocument = institutionTokenService.getInstitutionTokenDocument(plaidLinkResponse.getPublicToken(), currentUser.getUid());
                        Log.d(TAG, "Institution token document: " + institutionTokenDocument);
                        //TODO: build the accountDocument
                        if (false) {  //TODO: First check if the user already has an accountsDocument. If so, append this institution to it

                        } else { //The user doesn't have an existing accountsDocument that means this is their first account
                            Log.d(TAG, "This is the users first institution. Creating new AccountsDocument");
                            AccountsDocument accountsDocument = new AccountsDocument(currentUser.getUid());

                            InstitutionAttribute institutionAttribute = institutionAttributeService.createInstitutionAttribute(
                                    institutionTokenDocument.getItemId(),
                                    plaidLinkResponse.getInstitutionName(),
                                    plaidLinkResponse.getInstitutionId(),
                                    institutionTokenDocument.getAccessToken()
                            );
                            accountsDocument.addInstitutionAttribute(institutionAttribute);
                            Log.d(TAG, "AccountsDocument: " + accountsDocument);
                            accountsRepository.addAccountsDocument(accountsDocument);
                        }
                        institutionTokenRepository.addInstitutionTokenDocument(institutionTokenDocument);

                    } catch (PlaidExchangePublicTokenException e) {
                        //TODO: implement some error handling. We need to let the user know there was an error and to try again.
                        Log.e(TAG, "Exception caught while exchaning plaid public token");
                        e.printStackTrace();
                    } catch (PlaidGetAccountsBalanceException e) {
                        Log.e(TAG, "Exception caught while getting accounts balances from Plaid: " + e.getMessage());
                        e.printStackTrace();
                    }
                });

            }
        });
    }
}
