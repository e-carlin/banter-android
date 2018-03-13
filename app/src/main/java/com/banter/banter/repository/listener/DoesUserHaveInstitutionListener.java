package com.banter.banter.repository.listener;

/**
 * Created by evan.carlin on 3/12/2018.
 */

public interface DoesUserHaveInstitutionListener {
    void userHasInstitution();
    void userDoesNotHaveInstitution();
    void queryError();
}
