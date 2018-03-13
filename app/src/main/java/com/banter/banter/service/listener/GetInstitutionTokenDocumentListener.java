package com.banter.banter.service.listener;

import com.banter.banter.model.document.InstitutionTokenDocument;

/**
 * Created by evan.carlin on 3/13/2018.
 */

public interface GetInstitutionTokenDocumentListener {
    void onSuccess(InstitutionTokenDocument institutionTokenDocument);
    void onFailure(String errorMessage);
}
