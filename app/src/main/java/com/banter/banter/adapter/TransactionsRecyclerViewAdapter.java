package com.banter.banter.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.banter.banter.R;
import com.banter.banter.model.document.TransactionDocument;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by evan.carlin on 3/23/2018.
 */

public class TransactionsRecyclerViewAdapter extends FirestoreRecyclerAdapter<TransactionDocument, TransactionsRecyclerViewAdapter.TransactionsHolder> {
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    public TransactionsRecyclerViewAdapter(FirestoreRecyclerOptions<TransactionDocument> options, Activity activity) {
        super(options);

        ButterKnife.bind(this, activity);
    }

    @Override
    public void onBindViewHolder(TransactionsHolder holder, int position, TransactionDocument model) {
        progressBar.setVisibility(View.GONE);
        holder.transactionName.setText(model.getName());
        holder.amount.setText(String.format("$ %s", model.getAmount()));
    }

    @Override
    public TransactionsHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.transaction_item, group, false);

        return new TransactionsHolder(view);
    }


    public class TransactionsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.transaction_name)
        TextView transactionName;
        @BindView(R.id.text_amount)
        TextView amount;


        public TransactionsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
