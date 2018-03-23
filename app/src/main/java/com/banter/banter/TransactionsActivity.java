package com.banter.banter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.banter.banter.adapter.TransactionsRecyclerViewAdapter;
import com.banter.banter.model.document.TransactionDocument;
import com.banter.banter.repository.TransactionsRepository;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionsActivity extends AppCompatActivity {
    private final String TAG = "ShowTransactionsAct";

    @BindView(R.id.top_nav_bar)
    Toolbar topNavBar;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.transactions_recycler)
    RecyclerView transactionsList;

    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private TransactionsRepository transactionsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_transactions);
        ButterKnife.bind(this);
        this.transactionsRepository = new TransactionsRepository();

        setSupportActionBar(topNavBar);
        getSupportActionBar().setTitle(R.string.transactions_menu_title);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        transactionsList.setLayoutManager(linearLayoutManager);
        this.db = FirebaseFirestore.getInstance();

        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();

        setupTransactionsRecycler();
    }

    private void setupTransactionsRecycler(){

        FirestoreRecyclerOptions<TransactionDocument> response = new FirestoreRecyclerOptions.Builder<TransactionDocument>()
                .setQuery(transactionsRepository.getTransactionsQuery(currentUser.getUid()), TransactionDocument.class)
                .build();

        adapter = new TransactionsRecyclerViewAdapter(response, this);

        adapter.notifyDataSetChanged();
        transactionsList.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
