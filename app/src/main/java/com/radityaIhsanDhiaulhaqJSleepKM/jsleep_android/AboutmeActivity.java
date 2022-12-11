package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Account;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Renter;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.BaseApiService;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.UtilsApi;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutmeActivity extends AppCompatActivity {
    BaseApiService mApiService;
    Context mContext;
    TextView balanceAccount;
    EditText nameInput, addressInput, phoneNumberInput, topUpInput;
    CardView registerCardView,dataCardView;
//    Handler mHandler;
    Account sessionAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutme);
        sessionAccount = MainActivity.cookies;

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        TextView nameAccount = findViewById(R.id.about_name);
        TextView emailAccount = findViewById(R.id.about_email);
        balanceAccount = findViewById(R.id.about_balance);
        Button aboutRegisterRenter = findViewById(R.id.aboutme_registerRenter);
        Button topUp = findViewById(R.id.about_topUpButton);
        topUpInput = findViewById(R.id.nominal);

        //Second Condition
        nameInput = findViewById(R.id.aboutme_name2);
        addressInput = findViewById(R.id.aboutme_address);
        phoneNumberInput = findViewById(R.id.aboutme_phoneNumber);
        Button aboutRegister = findViewById(R.id.aboutme_register);
        Button cancel = findViewById(R.id.aboutme_cancel);


        //Third Condition
        TextView nameRenter = findViewById(R.id.aboutme_textviewStore);
        TextView addressRenter = findViewById(R.id.aboutme_textviewPlace);
        TextView phoneNumberRenter = findViewById(R.id.aboutme_textviewNumber);
        dataCardView = findViewById(R.id.dataCardView);
        registerCardView = findViewById(R.id.registerCardView);

        nameAccount.setText(sessionAccount.name);
        emailAccount.setText(sessionAccount.email);

        //Update supaya bentuknya RP.
        String balanceCurrency = NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(sessionAccount.balance);
        balanceAccount.setText(balanceCurrency);

        dataCardView.setVisibility(View.INVISIBLE);

        mApiService = UtilsApi.getApiService();
        mContext = this;

        topUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topUpAccount();
            }
        });
        aboutRegisterRenter.setVisibility(Button.VISIBLE);
        if (MainActivity.cookies.renter == null) {
            dataCardView.setVisibility(View.INVISIBLE);
            registerCardView.setVisibility(View.INVISIBLE);
            aboutRegisterRenter.setVisibility(Button.VISIBLE);
            aboutRegisterRenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aboutRegisterRenter.setVisibility(Button.INVISIBLE); //Button menghilang
                    registerCardView.setVisibility(View.VISIBLE);
                    dataCardView.setVisibility(View.INVISIBLE);
                    aboutRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            System.out.println(nameInput.getText().toString());
//                            System.out.println(addressInput.getText().toString());
//                            System.out.println(phoneNumberInput.getText().toString());
                            requestRenter();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            aboutRegisterRenter.setVisibility(View.VISIBLE); //Button muncul
                            registerCardView.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            });
        }

        if(MainActivity.cookies.renter != null){
            registerCardView.setVisibility(View.INVISIBLE);
            dataCardView.setVisibility(View.VISIBLE);
            aboutRegisterRenter.setVisibility(Button.GONE);
            nameRenter.setText(MainActivity.cookies.renter.username);
            addressRenter.setText(MainActivity.cookies.renter.address);
            phoneNumberRenter.setText(String.valueOf(MainActivity.cookies.renter.phoneNumber));
        }
    }

    protected Renter requestRenter(){
        mApiService.registerRenterRequest(
                MainActivity.cookies.id,
                nameInput.getText().toString(),
                addressInput.getText().toString(),
                phoneNumberInput.getText().toString()).enqueue(new Callback<Renter>() {
            @Override
            public void onResponse(Call<Renter> call, Response<Renter> response) {
                if(response.isSuccessful()){
                    MainActivity.cookies.renter = response.body();
                    Intent move = new Intent(AboutmeActivity.this, MainActivity.class);
                    startActivity(move);
                    Toast.makeText(mContext, "Register Renter Successfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Renter> call, Throwable t) {
                System.out.println(t.toString());
                Toast.makeText(mContext, "Register Renter Failed", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }

    protected boolean topUpAccount() {
        mApiService.topUpRequest(
                MainActivity.cookies.id,
                Double.parseDouble(topUpInput.getText().toString())
        ).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
//                    topUp = response.body();
                    Toast.makeText(mContext, "Top Up Successful!", Toast.LENGTH_SHORT).show();
                    System.out.println(topUpInput.toString());
                    balanceAccount.setText(NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(MainActivity.cookies.balance + Double.parseDouble(topUpInput.getText().toString())));
//                    sessionAccount.balance+= Double.parseDouble(topUpInput.getText().toString());
                    MainActivity.cookies.balance += Double.parseDouble(topUpInput.getText().toString());
                    topUpInput.setText("");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println(t.toString());
                Toast.makeText(mContext, "Top Up Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        return false;
    }



}