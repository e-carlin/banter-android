package com.banter.banter.model.document;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * Created by evan.carlin on 3/23/2018.
 */

@Data
@ToString
public class ChatDocument {
    private String userId;
    private String message;
    private Date createdAt;

    public ChatDocument() {}
}
