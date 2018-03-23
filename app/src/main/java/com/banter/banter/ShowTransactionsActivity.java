package com.banter.banter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.banter.banter.model.document.TransactionDocument;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowTransactionsActivity extends AppCompatActivity {
    private final String TAG = "ShowTransactionsAct";

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.friend_list)
    RecyclerView friendList;

    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_transactions);
        ButterKnife.bind(this);

        init();
        getFriendList();
    }

    private void init(){
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        friendList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
    }

    private void getFriendList(){
        Query query = db.collection("transactions");

        FirestoreRecyclerOptions<TransactionDocument> response = new FirestoreRecyclerOptions.Builder<TransactionDocument>()
                .setQuery(query, TransactionDocument.class)
                .build();

        List<TransactionDocument> docs = response.getSnapshots();
        Log.d(TAG, "Size: "+docs.size());
        for(TransactionDocument document : docs) {
            Log.d(TAG, document.toString());
        }

        adapter = new FirestoreRecyclerAdapter<TransactionDocument, TransactionsHolder>(response) {
            @Override
            public void onBindViewHolder(TransactionsHolder holder, int position, TransactionDocument model) {
                progressBar.setVisibility(View.GONE);
                holder.textName.setText(model.getName());
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
        friendList.setAdapter(adapter);
    }

    public class TransactionsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.transaction_name)
        TextView textName;
//        @BindView(R.id.image)
//        CircleImageView imageView;
//        @BindView(R.id.title)
//        TextView textTitle;
//        @BindView(R.id.company)
//        TextView textCompany;

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
