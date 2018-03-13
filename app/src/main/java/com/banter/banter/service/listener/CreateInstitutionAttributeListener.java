package com.banter.banter.service.listener;

import com.banter.banter.model.document.attribute.InstitutionAttribute;

/**
 * Created by evan.carlin on 3/13/2018.
 */

public interface CreateInstitutionAttributeListener {
    void onSuccess(InstitutionAttribute institutionAttribute);
    void onFailure(String errorMessage);
}
