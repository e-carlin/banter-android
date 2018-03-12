package com.banter.banter.model.document;

import lombok.Data;
import lombok.ToString;

@Data
//@ToString(exclude = "accessToken")
@ToString //TODO: Use exclude toString. AccessToken visible for just testing
public class InstitutionTokenDocument {


    private String itemId;
    private String accessToken;
    private String userId;

    public InstitutionTokenDocument(String itemId, String accessToken, String userId) {
        this.itemId = itemId;
        this.accessToken = accessToken;
        this.userId = userId;
    }

    public InstitutionTokenDocument() {}

    public String getItemId() { return this.itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
}
