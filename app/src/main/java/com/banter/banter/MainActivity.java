package com.banter.banter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.banter.banter.adapter.AccountAdapter;
import com.banter.banter.model.document.AccountsDocument;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.recycler_accounts)
    RecyclerView accountList;

    @BindView(R.id.button_add_account)
    Button add_account_button;

    private FirebaseFirestore db;
    private AccountAdapter accountAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        add_account_button.setOnClickListener((View v) -> {
            startActivity(new Intent(this, PlaidAddAccountActivity.class));
        });
//        init();
//        getAccountList();
    }

    private void init(){
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        accountList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();

        add_account_button.setOnClickListener((View v) -> {
            startActivity(new Intent(this, PlaidAddAccountActivity.class));
        });
    }

    private void getAccountList(){
        Query query = db.collection("Accounts");

        FirestoreRecyclerOptions<AccountsDocument> response = new FirestoreRecyclerOptions.Builder<AccountsDocument>()
                .setQuery(query, AccountsDocument.class)
                .build();

        accountAdapter = new AccountAdapter(response) {
            @Override
            public void onDataChanged() {
                Log.d(TAG, "DataChangedCalled");
                Log.d(TAG, "Count: "+getItemCount());
                if(getItemCount() == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        };
        accountAdapter.notifyDataSetChanged();
        accountList.setAdapter(accountAdapter);
    }

    public class AccountsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.account_item_name)
        TextView textName;

        public AccountsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        accountAdapter.startListening(); //TODO: add back in
    }

    @Override
    public void onStop() {
        super.onStop();
//        accountAdapter.stopListening(); //TODO: add back in
    }

}
