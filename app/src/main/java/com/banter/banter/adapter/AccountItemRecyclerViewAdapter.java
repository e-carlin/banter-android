package com.banter.banter.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.banter.banter.R;
import com.banter.banter.model.document.attribute.AccountAttribute;

import java.util.List;

public class AccountItemRecyclerViewAdapter extends RecyclerView.Adapter<AccountItemRecyclerViewAdapter.ItemViewHolder> {

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView accountName;
        private TextView currentBalance;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.accountName = (TextView) itemView.findViewById(R.id.text_account_name);
            this.currentBalance = (TextView) itemView.findViewById(R.id.text_current_balance);
        }
    }

    private Context context;
    private List<AccountAttribute> accounts;

    public AccountItemRecyclerViewAdapter(Context context, List<AccountAttribute> accounts) {
        this.context = context;
        this.accounts = accounts;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item_row_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.accountName.setText(accounts.get(position).getName());
        holder.currentBalance.setText(String.format("$ %s", accounts.get(position).getBalances().getCurrent().toString()));
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }


}
