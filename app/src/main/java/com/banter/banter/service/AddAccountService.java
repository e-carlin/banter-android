package com.banter.banter.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.banter.banter.model.document.InstitutionTokenDocument;
import com.banter.banter.model.document.attribute.InstitutionAttribute;
import com.banter.banter.model.response.PlaidLinkResponse;
import com.banter.banter.repository.AccountsRepository;
import com.banter.banter.repository.InstitutionTokenRepository;
import com.banter.banter.repository.listener.DoesUserHaveInstitutionListener;
import com.banter.banter.service.listener.AddDocumentListener;
import com.banter.banter.service.listener.CreateInstitutionAttributeListener;
import com.banter.banter.service.listener.GetInstitutionTokenDocumentListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Created by evan.carlin on 3/12/2018.
 * <p>
 * TODO: This class is classic callback hell look at something like RxJava to help alleviate this
 */

public class AddAccountService extends IntentService {
    private static final String TAG = "AddAccountService";

    private InstitutionTokenDocumentService institutionTokenDocumentService;
    private InstitutionTokenRepository institutionTokenRepository;
    private InstitutionAttributeService institutionAttributeService;
    private AccountsRepository accountsRepository;
    private FirebaseUser currentUser;

    public AddAccountService() {
        super("AddAccountService");

        this.institutionTokenDocumentService = new InstitutionTokenDocumentService();
        this.institutionTokenRepository = new InstitutionTokenRepository();
        this.institutionAttributeService = new InstitutionAttributeService();
        this.accountsRepository = new AccountsRepository();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e(TAG, "User is trying to add a new institution");
        //Check if this is a duplicate account
        PlaidLinkResponse plaidLinkResponse = (PlaidLinkResponse) intent.getSerializableExtra("plaidLinkResponse");
        accountsRepository.doesUserHaveInstitution(this.currentUser.getUid(), plaidLinkResponse.getInstitutionId(), new DoesUserHaveInstitutionListener() {
            @Override
            public void queryError() {
                //TODO: Aler the user
                Log.e(TAG, "Query error asking if the user has already added this institution. TODO: Alert user");
            }

            @Override
            public void userHasInstitution() {
                //TODO: alert the user that they tried to add a duplicate account
                Log.e(TAG, "The user has already added an institution with ins_id = " + plaidLinkResponse.getInstitutionId() + " name: " + plaidLinkResponse.getInstitutionName() + " TODO: Alert user");
            }

            @Override
            public void userDoesNotHaveInstitution() {
                System.out.println("User does not have institution. Adding...");
                //Needs to be done async becasuse firebase listeners are alway called on the ui thread
                // So even though this is run in an IntentService which should put it on its own thread it we need to use async
                institutionTokenDocumentService.getInstitutionTokenDocument(plaidLinkResponse.getPublicToken(), currentUser.getUid(), new GetInstitutionTokenDocumentListener() {
                    @Override
                    public void onSuccess(InstitutionTokenDocument institutionTokenDocument) { //We got a new InstitutionTokenDocument
                        Log.d(TAG, "Created a new InstitutionTokenDocument for the user: "+institutionTokenDocument);
                        institutionAttributeService.createInstitutionAttribute(
                                institutionTokenDocument.getItemId(),
                                plaidLinkResponse.getInstitutionName(),
                                plaidLinkResponse.getInstitutionId(),
                                institutionTokenDocument.getAccessToken(),
                                new CreateInstitutionAttributeListener() {
                                    @Override
                                    public void onSuccess(InstitutionAttribute institutionAttribute) { //The institution attribute was successfully created
                                        Log.d(TAG, "Created a new institutionAttribute for the user: "+institutionAttribute);
                                        accountsRepository.addInstitutionAttribute(currentUser.getUid(), institutionAttribute, new AddDocumentListener() {
                                            @Override
                                            public void onSuccess() {
                                                //Save the institutionTokenDocumetn
                                                Log.d(TAG, "Successfully added the new institution.");
                                                institutionTokenRepository.addInstitutionTokenDocument(institutionTokenDocument, new AddDocumentListener() {
                                                    @Override
                                                    public void onSuccess() {
                                                        //TODO: Alert the user
                                                        Log.w(TAG, "SUCCESS. We added both the institutionTokenDocument and the new InstitutionAttribute");
                                                    }

                                                    @Override
                                                    public void onFailure(String errorMessage) {
                                                        Log.e(TAG, "FATAL error. We should never get here. In this a new institutionAttribute for a new institution" +
                                                                "for a user has been added to the db but there was a failure adding the corresponsding InstitutionTokenDocument");
                                                        //TODO: Delete the the institutionAttribute
                                                        //TODO: Alert the user
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onFailure(String errorMessage) { //Failure adding InstitutionAttribute
                                                //TODO: Alert user
                                                Log.w(TAG, "Error adding new institution");
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(String errorMessage) {
                                        //TODO: Display error to user
                                        Log.e(TAG, "Failure creating institutionAttribute: " + errorMessage + " TODO: implement display error to user");
                                    }
                                }
                        );

                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e(TAG, "Could not getInstitutionTokenDocument: " + errorMessage + " TODO: Alert user");
                        //TODO: Alert the user there was an error
                    }
                });
            }
        });
    }
}
