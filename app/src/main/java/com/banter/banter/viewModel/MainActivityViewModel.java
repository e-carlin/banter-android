package com.banter.banter.viewModel;

import android.arch.lifecycle.ViewModel;

/**
 * Created by evan.carlin on 3/8/2018.
 */

public class MainActivityViewModel extends ViewModel {

    private boolean mIsSigningIn;
//    private Filters mFilters;

    public MainActivityViewModel() {
        mIsSigningIn = false;
//        mFilters = Filters.getDefault();
    }

    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }

//    public Filters getFilters() {
//        return mFilters;
//    }

//    public void setFilters(Filters mFilters) {
//        this.mFilters = mFilters;
//    }
}