package com.banter.banter.repository.listener;

/**
 * Created by evan.carlin on 3/13/2018.
 */

public interface GetDocumentListener {
    void onSuccess(Object document);
    void onEmptyResult();
    void onFailure(String errorMessage);
}
