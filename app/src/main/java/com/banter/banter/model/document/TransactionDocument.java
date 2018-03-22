package com.banter.banter.model.document;
import com.google.firebase.firestore.ServerTimestamp;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString
public class TransactionDocument {

    
    private String userId;
    private String accountId;
    private Double amount; //Todo: better type than double??
    private List<String> categories; //TODO: Should categories be an enum??
    private String categoryId;
    private String transactionDate;
    private LocationMeta locationMeta;
    private String name;
    private String originalDescription;
    private PaymentMeta paymentMeta;
    private boolean pending;
    private String pendingTransactionId;
    private String transactionId;
    private String transactionType; //TODO: enum?
    private String accountOwner;
    @ServerTimestamp
    private Date createdAt; //Recommended to be stored as a long https://stackoverflow.com/questions/48473473/save-object-of-localdate-java-time-in-firebase-database


    public TransactionDocument() {
    }

    @Data
    @ToString
    public static final class LocationMeta {
        private String address;
        private String city;
        private String state;
        private String zip;
        private Double lat;
        private Double lon;
        private String storeNumber;

        public LocationMeta() {}
    }


    @Data
    @ToString
    public class PaymentMeta {

        private String byOrderOf;
        private String payee;
        private String payer;
        private String paymentMethod;
        private String paymentProcessor;
        private String ppdId;
        private String reason;
        private String referenceNumber;

        public PaymentMeta() {}
    }
}
