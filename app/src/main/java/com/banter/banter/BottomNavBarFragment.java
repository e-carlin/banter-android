package com.banter.banter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BottomNavBarFragment extends Fragment {
    public final static String TAG = "BottomNavBarFragment";

    public BottomNavBarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_bottom_nav_bar, container, false);

        BottomNavigationView bottomNavBar = fragment.findViewById(R.id.bottom_nav_bar);
        bottomNavBar.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_accounts:
                            if (!(getActivity() instanceof MainActivity)) {
                                startActivity(new Intent(getActivity(), MainActivity.class));
                            }
                            break; //No-op. This is a reselect. We are already on the accounts activity

                        case R.id.action_transactions:
                            if (!(getActivity() instanceof TransactionsActivity)) {
                                startActivity(new Intent(getActivity(), TransactionsActivity.class));
                            }
                            break;

                        case R.id.action_chat:
                            if (!(getActivity() instanceof ChatActivity)) {
                                startActivity(new Intent(getActivity(), ChatActivity.class));
                            }
                            break;
                    }
                    return true;
                }
        );
        return fragment;
    }
}
