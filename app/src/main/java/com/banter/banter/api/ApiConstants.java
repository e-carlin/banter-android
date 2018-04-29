package com.banter.banter.api;

import com.banter.banter.BuildConfig;

/**
 * Created by evan.carlin on 3/7/2018.
 */

public class ApiConstants {
    /* Banter API base url */
    final static String BANTER_BASE_URL = BuildConfig.BANTER_BASE_URL;


    /* User endpoints*/
    final static String REGISTER_USER_ENDPOINT = BANTER_BASE_URL + "/user/register";

    /* Account endpoints */
    final static String ACCOUNT_ENDPOINT = BANTER_BASE_URL + "/accounts";
    final static String ADD_ACCOUNT_ENDPOINT = ACCOUNT_ENDPOINT + "/add";

    /* Plaid webhook endpoints */
    public final static String PLAID_WEBHOOK_ENDPOINT = BANTER_BASE_URL + "/plaid/webhook";
}
