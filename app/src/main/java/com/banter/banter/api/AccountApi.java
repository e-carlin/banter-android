package com.banter.banter.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by evan.carlin on 3/21/2018.
 */

public class AccountApi {
    private static String TAG = "AccountApi";

    public static void addAccount(JSONObject data, Context ctx, Response.Listener responseListener, Response.ErrorListener errorListener) {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            addPlaidAccount(data, ctx, idToken, responseListener, errorListener);
                        } else {
                            // Handle error -> task.getException();
                            Log.d(TAG, "Error retrieving idToken: " + task.getException().getMessage());
                            Log.d(TAG, task.getException().getStackTrace().toString());
                        }
                    }
                });
    }

    /**
     * Helper method for adding an account. If we ever decide to not use Plaid we can *hopefully* just point addAccount to a
     * different method and no other code will need to be changed.
     *
     * @param data
     * @param ctx
     */
    private static void addPlaidAccount(JSONObject data, Context ctx, String idToken, Response.Listener responseListener, Response.ErrorListener errorListener) {
        RequestQueueSingleton requestQueue = RequestQueueSingleton.getInstance(ctx);
        Log.i(TAG, "Sending account data to our api. URL: " + ApiConstants.ADD_ACCOUNT_ENDPOINT + "  DATA: " + data.toString());


        //TODO: subclass this so we don't have to add the getHeaders() method everytime we want to make an authorized call to our api
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(ApiConstants.ADD_ACCOUNT_ENDPOINT, data, responseListener, errorListener) {
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", idToken);
                return params;
            }
        };
        //TODO: Think about this some more
        //This needs to be set so that volley doesn't send the data 2x. I think it is sending data 2x because our backend is taking a while to responsd
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjReq);
    }
}
