package com.banter.banter;

import android.content.Intent;
import android.support.annotation.Nullable;
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
import com.banter.banter.model.document.attribute.AccountAttribute;
import com.banter.banter.repository.AccountsRepository;
import com.banter.banter.repository.listener.GetDocumentListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.sectioned_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.my_toolbar) //TODO: Rename
    Toolbar myToolbar;

    private AccountsRepository accountsRepository;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Accounts");

        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.accountsRepository = new AccountsRepository();

        setUpRecyclerView();
        populateRecyclerView();
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
}
