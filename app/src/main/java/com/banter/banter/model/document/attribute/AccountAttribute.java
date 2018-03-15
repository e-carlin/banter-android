package com.banter.banter.model.document.attribute;

import com.google.firebase.firestore.Exclude;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AccountAttribute {

    private String id;
    private String name;
    private String type; //TODO: Maybe make this an enum https://plaid.com/docs/api/#accounts https://stackoverflow.com/questions/37368952/what-is-the-best-way-to-save-java-enums-using-firebase
    private String subtype; //TODO: Maybe make this an enum too https://plaid.com/docs/api/#accounts https://stackoverflow.com/questions/37368952/what-is-the-best-way-to-save-java-enums-using-firebase
    private AccountBalancesAttribute balances;

    public AccountAttribute() {
    }

    public AccountAttribute(String id, String name, String type, String subtype, AccountBalancesAttribute balances) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.subtype = subtype;
        this.balances = balances;
    }

}
