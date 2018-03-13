package com.banter.banter.service.listener;

/**
 * Created by evan.carlin on 3/13/2018.
 */

public interface AddDocumentListener {
    void onSuccess();
    void onFailure(String errorMessage);
}
