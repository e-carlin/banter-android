package com.banter.banter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.banter.banter.model.document.AccountsDocument;
import com.banter.banter.repository.AccountsRepository;
import com.banter.banter.repository.listener.GetDocumentListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.sectioned_recycler_view)
    RecyclerView recyclerView;

    private AccountsRepository accountsRepository;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.accountsRepository = new AccountsRepository();

        //get enum type passed from MainActivity
        setUpRecyclerView();
        populateRecyclerView();
    }

    //setup recycler view
    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    //populate recycler view
    private void populateRecyclerView() {
        accountsRepository.listenToMostRecentAccountsDocument(this.currentUser.getUid(), new GetDocumentListener() {
            @Override
            public void onSuccess(Object document) {
                AccountsDocument accountsDocument = (AccountsDocument) document;
                Log.e(TAG, "******************************* Success getting document");

                List<SectionModel> sectionModels = new ArrayList<>();

                List<String> accountTypes = accountsDocument.getAccountTypes();
                for (String accountType : accountTypes) {
                    //TODO: Fill out with actual account data
                    ArrayList<String> itemArrayList = new ArrayList<>();
                    for (int j = 1; j <= 10; j++) {
                        itemArrayList.add("Item " + j);
                    }
                    sectionModels.add(new SectionModel(accountType, itemArrayList));
                }
                SectionRecyclerViewAdapter adapter = new SectionRecyclerViewAdapter(MainActivity.this, sectionModels);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onEmptyResult() {
                Log.e(TAG, "******************************* Empty result");
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "**************************** FAILURE: " + errorMessage);
            }
        });


//        ArrayList<SectionModel> sectionModelArrayList = new ArrayList<>();
//        //for loop for sections
//        for (int i = 1; i <= 5; i++) {
//            ArrayList<String> itemArrayList = new ArrayList<>();
//            //for loop for items
//            for (int j = 1; j <= 10; j++) {
//                itemArrayList.add("Item " + j);
//            }
//
//            //add the section and items to array list
//            sectionModelArrayList.add(new SectionModel("Section " + i, itemArrayList));
//        }

//        SectionRecyclerViewAdapter adapter = new SectionRecyclerViewAdapter(this, sectionModelArrayList);
//        recyclerView.setAdapter(adapter);
    }
}
