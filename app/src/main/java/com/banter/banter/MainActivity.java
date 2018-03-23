package com.banter.banter;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.banter.banter.adapter.AccountsSectionRecyclerViewAdapter;
import com.banter.banter.model.document.AccountsDocument;
import com.banter.banter.model.document.TransactionDocument;
import com.banter.banter.model.document.attribute.AccountAttribute;
import com.banter.banter.repository.AccountsRepository;
import com.banter.banter.repository.listener.GetDocumentListener;
import com.banter.banter.viewModel.MainActivityViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;

    @BindView(R.id.sectioned_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.top_nav_bar)
    Toolbar topNavBar;

    private MainActivityViewModel mViewModel;
    private FirebaseUser currentUser;

    private AccountsRepository accountsRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        accountsRepository = new AccountsRepository();

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        // Start sign in if necessary
//        if (shouldStartSignIn()) {
//            Log.w(TAG, "^^^^^^^^^^^^^^^^^^^^^ in shoultStartSignIn()");
//            startSignIn();
//            return;
//        }

        setSupportActionBar(topNavBar);
        getSupportActionBar().setTitle(R.string.add_account_menu_title); //TODO: Make @string

        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.w(TAG, "************************* ON CREATE. Current user: "+this.currentUser);

//        populateRecyclerView();
//        setUpRecyclerView();

        updateUI();
    }

    private void updateUI() {
        if(this.currentUser != null) {
            setUpRecyclerView();
            populateRecyclerView();
        }
        else {
            if(shouldStartSignIn()) {
                startSignIn();
            }
            else {
                Log.e(TAG,"Error: the current user is null but we also shouldn't start signing in...");
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_account_menu, menu);
        return true;
    }

    //setup recycler view
    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    //populate recycler view
    private void populateRecyclerView() {
        accountsRepository.getMostRecentAccountsDocument(this.currentUser.getUid(), new GetDocumentListener() {
            @Override
            public void onSuccess(Object document) {
                AccountsDocument accountsDocument = (AccountsDocument) document;
                Log.e(TAG, "******************************* Success getting document");

                List<SectionModel> sectionModels = new ArrayList<>();

                HashMap<String, List<AccountAttribute>> accountsGroupedByType = accountsDocument.getAccountsGroupedByType();

                for(String key : accountsGroupedByType.keySet()) {
                    String label = key.substring(0,1).toUpperCase() + key.substring(1).toLowerCase();
                    sectionModels.add(new SectionModel(label, accountsGroupedByType.get(key)));
                }

                AccountsSectionRecyclerViewAdapter adapter = new AccountsSectionRecyclerViewAdapter(MainActivity.this, sectionModels);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onEmptyResult() {
                Log.e(TAG, "******************************* Empty result");
                //TODO: Display no accounts added message
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "**************************** FAILURE: " + errorMessage);
                //TODO: Display error message
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_account:
                // User chose the "add account" item. take them to the add account activity
                startActivity(new Intent(this, PlaidAddAccountActivity.class));
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    //=====================
    //Sign In handling below

    private void startSignIn() {
        // Sign in with FirebaseUI
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build())) //TODO: Fix deprecation warnings
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
        mViewModel.setIsSigningIn(true);
    }

    private boolean shouldStartSignIn() {
        return (!mViewModel.getIsSigningIn() && FirebaseAuth.getInstance().getCurrentUser() == null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            mViewModel.setIsSigningIn(false);

            if (resultCode != RESULT_OK) {
                if (response == null) {
                    // User pressed the back button.
                    finish();
                } else if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSignInErrorDialog(R.string.message_no_network);
                } else {
                    showSignInErrorDialog(R.string.message_unknown);
                }
            }
            else {
                this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
                updateUI();
            }
        }
    }

    private void showSignInErrorDialog(@StringRes int message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.title_sign_in_error)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.option_retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startSignIn();
                    }
                })
                .setNegativeButton(R.string.option_exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).create();

        dialog.show();
    }
}
