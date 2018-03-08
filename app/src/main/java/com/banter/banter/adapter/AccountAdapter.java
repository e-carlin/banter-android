package com.banter.banter.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.banter.banter.R;
import com.banter.banter.model.Account;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by evan.carlin on 3/8/2018.
 */
public class AccountAdapter extends FirestoreAdapter<AccountAdapter.ViewHolder>{
    public AccountAdapter(Query query) {
        super(query);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_account, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        throw new UnsupportedOperationException("The account adapter tried to call onBindViewholder!!!!!!!");
//        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.account_item_name)
        TextView nameView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot) {

            Account restaurant = snapshot.toObject(Account.class);
            Resources resources = itemView.getResources();

            nameView.setText(restaurant.getName());
        }

    }
}
