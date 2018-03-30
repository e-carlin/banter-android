package com.banter.banter;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Response;
import com.banter.banter.api.AccountApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class PlaidAddAccountActivity extends AppCompatActivity {
    private final static String TAG = "PlaidAddAccountActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plaid_add_account);


        FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            Log.d(TAG, "^^^^^^^^^^^^^^^^ ID TOKEN: "+idToken);
                        } else {
                            // Handle error -> task.getException();
                            Log.d(TAG, "Error retrieving idToken: " + task.getException().getMessage());
                            Log.d(TAG, task.getException().getStackTrace().toString());
                        }
                    }
                });


        openPlaidAddAccountWebView();
    }

    private void openPlaidAddAccountWebView() {
        final HashMap<String, String> linkInitializationOptions = getLinkInitializationOptions();
        final Uri linkInitializationUrl = generateLinkInitializationUrl(linkInitializationOptions);
        final WebView plaidLinkWebView = getConfiguredPlaidLinkWebView();
        plaidLinkWebView.loadUrl(linkInitializationUrl.toString());

        // Override the WebView's handler for redirects
        // Link communicates success and failure (analogous to the web's onSuccess and onExit
        // callbacks) via redirects.
        plaidLinkWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Parse the URL to determine if it's a special Plaid Link redirect or a request
                // for a standard URL (typically a forgotten password or account not setup link).
                // Handle Plaid Link redirects and open traditional pages directly in the  user's
                // preferred browser.
                Uri parsedUri = Uri.parse(url);
                if (parsedUri.getScheme().equals("plaidlink")) {
                    String action = parsedUri.getHost();
                    HashMap<String, String> linkData = parseLinkUriData(parsedUri);

                    if (action.equals("connected")) {
                        //Success! We got the account details from Plaid
                        Log.e(TAG, "Connected to Link. Starting addAccountService");

                        AccountApi.addAccount(new JSONObject(linkData),
                                getApplicationContext(),
                                getResponseListener(),
                                getResponseErrorListener());
                        startActivity(new Intent(PlaidAddAccountActivity.this, MainActivity.class));

                    } else if (action.equals("exit")) {
                        // User exited
                        Log.w(TAG, "User exited the Plaid link workflow");
                        startActivity(new Intent(PlaidAddAccountActivity.this, MainActivity.class));
                    } else {
                        Log.d("Link action detected: ", action);
                    }
                    // Override URL loading
                    return true;
                } else if (parsedUri.getScheme().equals("https") ||
                        parsedUri.getScheme().equals("http")) {
                    // Open in browser - this is most  typically for 'account locked' or
                    // 'forgotten password' redirects
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    // Override URL loading
                    return true;
                } else {
                    // Unknown case - do not override URL loading
                    return false;
                }
            }
        });
    }

    private Response.Listener<JSONObject> getResponseListener() {
        return response -> {
            Log.i(TAG, "Response from adding account is: " + response.toString());
            Toast.makeText(this, "Success adding account",
                    Toast.LENGTH_SHORT).show();
        };
    }

    private Response.ErrorListener getResponseErrorListener() {
        return error -> {
            //TODO: Maybe handle the add duplicate account different that other possible errors. We'll need to change api to send an error. Currently the API sents status OK with a message that it was a duplicate
            //TODO: Handle addDuplicateAccountDifferently than other errors
            Log.e(TAG, "Error sending plaid public token to our api: " + error);
            try {
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    Log.e(TAG, "Error message: " + new String(error.networkResponse.data, "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } finally {
                showSendPublicTokenErrorAlerDialog();
            }
        };
    }

    private void showSendPublicTokenErrorAlerDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error saving your account");
        alertDialog.setMessage("Sorry, our system encountered an error and was unable to save your account. Please try again.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(PlaidAddAccountActivity.this, PlaidAddAccountActivity.class));
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        startActivity(new Intent(PlaidAddAccountActivity.this, MainActivity.class));
                    }
                });
        alertDialog.show();
    }

    private WebView getConfiguredPlaidLinkWebView() {
        // Modify WebView settings
        // TODO: Determine which of these settings I need or not
        final WebView plaidLinkWebView = (WebView) findViewById(R.id.webview_add_account);
        WebSettings webSettings = plaidLinkWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        WebView.setWebContentsDebuggingEnabled(true);

        return plaidLinkWebView;
    }

    //TODO: These values should all be moved into a config file
    private HashMap<String, String> getLinkInitializationOptions() {
        HashMap<String, String> linkInitializeOptions = new HashMap<>();
        linkInitializeOptions.put("key", BuildConfig.PLAID_PUBLIC_KEY);
        linkInitializeOptions.put("product", "transactions");
        linkInitializeOptions.put("apiVersion", "v2");
        linkInitializeOptions.put("env", "sandbox");
        linkInitializeOptions.put("clientName", "Test App");
//        linkInitializeOptions.put("webhook", "http://requestb.in"); //TODO: Get the URL from the build config
        linkInitializeOptions.put("baseUrl", "https://cdn.plaid.com/link/v2/stable/link.html");
        // If initializing Link in PATCH / update mode, also provide the public_token
        // linkInitializeOptions.put("public_token", "PUBLIC_TOKEN")

        return linkInitializeOptions;
    }

    // Generate a Link initialization URL based on a set of configuration options
    private Uri generateLinkInitializationUrl(HashMap<String, String> linkOptions) {
        Uri.Builder builder = Uri.parse(linkOptions.get("baseUrl"))
                .buildUpon()
                .appendQueryParameter("isWebview", "true")
                .appendQueryParameter("isMobile", "true");
        for (String key : linkOptions.keySet()) {
            if (!key.equals("baseUrl")) {
                builder.appendQueryParameter(key, linkOptions.get(key));
            }
        }
        return builder.build();
    }


    //     Parse a Link redirect URL querystring into a HashMap for easy manipulation and access
    private HashMap<String, String> parseLinkUriData(Uri linkUri) {
        HashMap<String, String> linkData = new HashMap<>();
        for (String key : linkUri.getQueryParameterNames()) {
            linkData.put(key, linkUri.getQueryParameter(key));
        }
        return linkData;
    }
}