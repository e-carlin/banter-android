package com.banter.banter.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.banter.banter.MainActivity;
import com.banter.banter.R;
import com.banter.banter.model.Account;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by evan.carlin on 3/8/2018.
 */

public class AccountAdapter extends FirestoreRecyclerAdapter<Account, AccountAdapter.AccountsHolder>{
    public static final String TAG = "AccountAdapter";

    private FirestoreRecyclerOptions<Account> resposne;
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
    protected void onBindViewHolder(AccountsHolder holder, int position, Account model) {
        Log.e(TAG, "************** onBindViewHolderCalled");
        holder.textName.setText(model.getName());
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
