package com.banter.banter.service.listener;

/**
 * Created by evan.carlin on 3/13/2018.
 */

public interface GetDocumentListener {
    void onSuccess(Object document);
    void onEmptyResult();
    void onFailure(String errorMessage);
}
