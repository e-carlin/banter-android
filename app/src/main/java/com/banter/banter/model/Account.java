package com.banter.banter.model;

/**
 * Created by evan.carlin on 3/8/2018.
 */

public class Account {

    private String name;

    public Account() {}

    public Account(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
