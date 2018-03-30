package com.banter.banter.model.document;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

/**
 * Created by evan.carlin on 3/23/2018.
 */

@Data
@ToString
public class ChatDocument {
    @NonNull
    private String userId;
    @NonNull
    private String message;
    @NonNull
    private boolean messageIsFromUser;
    @NonNull
    private boolean botHasReplied;
    @ServerTimestamp
    private Date createdAt;

    public ChatDocument() {}

    public ChatDocument(String message, String userId, boolean messageIsFromUser) {
        this.message = message;
        this.userId = userId;
        this.messageIsFromUser = messageIsFromUser;
    }
}
