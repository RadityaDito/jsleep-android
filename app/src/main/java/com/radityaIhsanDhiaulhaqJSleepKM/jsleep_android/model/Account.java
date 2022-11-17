package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model;

public class Account extends Serializable {
    public String name;
    public String email;
    public String password;

    public Renter renter;
    public double balance;

    public Account(int id) {
        super(id);
    }
}
