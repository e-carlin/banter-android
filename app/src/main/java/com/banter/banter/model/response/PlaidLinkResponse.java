package com.banter.banter.model.response;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

import lombok.Data;
import lombok.ToString;

/**
 * Created by evan.carlin on 3/9/2018.
 */
@Data
@ToString
//@SuppressWarnings("serial")
public class PlaidLinkResponse implements Serializable{
    private String linkSessionId;
    private String publicToken;
    private String institutionName;
    private String institutionId;

    public PlaidLinkResponse() {}

    public PlaidLinkResponse(HashMap<String, String> linkData) {
        this.linkSessionId = linkData.get("link_session_id");
        this.publicToken = linkData.get("public_token");
        this.institutionName = linkData.get("institution_name");
        this.institutionId = linkData.get("institution_id");
    }
}
