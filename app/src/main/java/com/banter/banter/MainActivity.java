package com.banter.banter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.banter.banter.model.document.AccountsDocument;
import com.banter.banter.repository.AccountsRepository;
import com.banter.banter.repository.listener.GetDocumentListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.button_add_account)
    Button addAccountButton;

    @BindView(R.id.view_no_account_data)
    View viewNoAccountData;

    private AccountsRepository accountsRepository;

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.viewNoAccountData.setVisibility(View.INVISIBLE);
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();

        this.accountsRepository = new AccountsRepository();

        this.progressBar.setVisibility(View.VISIBLE);

        this.addAccountButton.setOnClickListener((View v) -> {
            startActivity(new Intent(this, PlaidAddAccountActivity.class));
        });

        getAccountsData();
    }

    //TODO: This is where I am working
    private void getAccountsData() {
        this.accountsRepository.listenToMostRecentAccountsDocument(currentUser.getUid(), new GetDocumentListener() {
            @Override
            public void onSuccess(Object document) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.w(TAG, "SUCCESSL "+(AccountsDocument)document);
            }

            @Override
            public void onEmptyResult() {
                progressBar.setVisibility(View.INVISIBLE);
                viewNoAccountData.setVisibility(View.VISIBLE);
                Log.w(TAG, "Empty result");
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "Failure");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        this.progressBar.setVisibility(View.VISIBLE);
        getAccountsData();
    }
}
