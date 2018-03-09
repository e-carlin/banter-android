package com.banter.banter.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.banter.banter.R;
import com.banter.banter.model.document.AccountsDocument;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by evan.carlin on 3/8/2018.
 */

public class AccountAdapter extends FirestoreRecyclerAdapter<AccountsDocument, AccountAdapter.AccountsHolder>{
    public static final String TAG = "AccountAdapter";

    private FirestoreRecyclerOptions<AccountsDocument> resposne;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AccountAdapter(FirestoreRecyclerOptions options) {
        super(options);
        resposne = options;
    }


    @Override
    protected void onBindViewHolder(AccountsHolder holder, int position, AccountsDocument model) {
        Log.e(TAG, "************** onBindViewHolderCalled");
        holder.textName.setText(model.getUserId());
    }

    @NonNull
    @Override
    public AccountsHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.item_account, group, false);

        return new AccountsHolder(view);
    }



    public static class AccountsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.account_item_name)
        TextView textName;

        public AccountsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
