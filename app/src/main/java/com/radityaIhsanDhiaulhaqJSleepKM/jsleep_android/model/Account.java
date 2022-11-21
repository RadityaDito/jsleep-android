package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.BaseApiService;

public class Account extends Serializable {
    public String name;
    public String email;
    public String password;

    public Renter renter;
    public double balance;
//    BaseApiService mApiService;
//    Context mContext;


    public Account(int id) {
        super(id);
    }

    @Override
    public String toString(){
        return "Account{" +
                "balance=" + balance +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", renter=" + renter +
                '}';
    }



}
