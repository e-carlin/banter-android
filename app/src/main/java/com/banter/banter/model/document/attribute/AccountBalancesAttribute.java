package com.banter.banter.model.document.attribute;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AccountBalancesAttribute {

    //TODO: Do some more thinking to see if Double is the best option to represent money
    //Plaid returns a double from their sdk but maybe I should cast it to something else (ie currency or BigDecimal)
    private Double current;
    private Double available; //Plaid can give us a null value for this
    private Double limit; //Plaid can give us null values for this

    public AccountBalancesAttribute() {}

    public AccountBalancesAttribute(Double current, Double available, Double limit) {
        this.current = current;
        this.available = available;
        this.limit = limit;
    }

}
