package com.banter.banter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowTransactionsActivity extends AppCompatActivity {


    @BindView(R.id.button_sign_out)
    Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_transactions);

        ButterKnife.bind(this);

        signOutButton.setOnClickListener((View v) -> {
            AuthUI.getInstance().signOut(this);
            startActivity(new Intent(this, MainActivity.class));
        });
    }
}
