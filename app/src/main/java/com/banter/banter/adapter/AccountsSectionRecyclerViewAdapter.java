package com.banter.banter.adapter;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.banter.banter.model.document.AccountTypeGroup;
import com.banter.banter.R;

import java.util.List;

/**
 * Created by sonu on 24/07/17.
 */

public class AccountsSectionRecyclerViewAdapter extends RecyclerView.Adapter<AccountsSectionRecyclerViewAdapter.SectionViewHolder> {


    class SectionViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionLabel;
        private RecyclerView itemRecyclerView;

        public SectionViewHolder(View itemView) {
            super(itemView);
            sectionLabel = (TextView) itemView.findViewById(R.id.section_label);
            itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_recycler_view);
        }
    }

    private Context context;
    private List<AccountTypeGroup> accountTypeGroups;

    public AccountsSectionRecyclerViewAdapter(Context context, List<AccountTypeGroup> accountTypeGroups) {
        this.context = context;
        this.accountTypeGroups = accountTypeGroups;
    }

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accounts_section_layout, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SectionViewHolder holder, int position) {
        final AccountTypeGroup accountTypeGroup = accountTypeGroups.get(position);
        holder.sectionLabel.setText(accountTypeGroup.getAccountType());

        //recycler view for items
        holder.itemRecyclerView.setHasFixedSize(true);
        holder.itemRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.itemRecyclerView.setLayoutManager(linearLayoutManager);

        AccountItemRecyclerViewAdapter adapter = new AccountItemRecyclerViewAdapter(context, accountTypeGroup.getAccounts());
        holder.itemRecyclerView.setAdapter(adapter);

        //show toast on click of show all button
//        holder.showAllButton.setOnClickListener(v -> Toast.makeText(context, "You clicked on Show All of : " + accountTypeGroup.getAccountType(), Toast.LENGTH_SHORT).show());

    }

    @Override
    public int getItemCount() {
        return accountTypeGroups.size();
    }


}