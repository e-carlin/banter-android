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
        db = FirebaseFirestore.getInstance();

        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();

        setupTransactionsRecycler();
    }

    private void setupTransactionsRecycler(){

        FirestoreRecyclerOptions<TransactionDocument> response = new FirestoreRecyclerOptions.Builder<TransactionDocument>()
                .setQuery(transactionsRepository.getTransactionsQuery(currentUser.getUid()), TransactionDocument.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<TransactionDocument, TransactionsHolder>(response) {
            @Override
            public void onBindViewHolder(TransactionsHolder holder, int position, TransactionDocument model) {
                progressBar.setVisibility(View.GONE);
                holder.textName.setText(model.getName());
                holder.amount.setText(String.format("$ %s",model.getAmount()));
            }

            @Override
            public TransactionsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.transaction_item, group, false);

                return new TransactionsHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        transactionsList.setAdapter(adapter);
    }

    public class TransactionsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_name)
        TextView textName;
        @BindView(R.id.text_amount)
        TextView amount;


        public TransactionsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
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
