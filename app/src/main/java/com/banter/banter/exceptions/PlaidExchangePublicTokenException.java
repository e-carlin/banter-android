package com.banter.banter.exceptions;

import android.util.Log;

public class PlaidExchangePublicTokenException extends Exception {
    private final static String TAG = "ExchangePublicTokenExc";

    public PlaidExchangePublicTokenException(String message) {
        super("There was an exception exchanging the Plaid public token: "+message);
        Log.w(TAG, "Exception encountered when exchanging plaid public token: "+message);
    }
}
