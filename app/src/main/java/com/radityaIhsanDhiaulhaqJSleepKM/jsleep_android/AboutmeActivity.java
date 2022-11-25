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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutmeActivity extends AppCompatActivity {
    BaseApiService mApiService;
    Context mContext;
    EditText nameInput, addressInput, phoneNumberInput;
    CardView registerCardView,dataCardView;
//    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutme);

        Account sessionAccount = MainActivity.cookies;
        TextView nameAccount = findViewById(R.id.about_name);
        TextView emailAccount = findViewById(R.id.about_email);
        TextView balanceAccount = findViewById(R.id.about_balance);
        Button aboutRegisterRenter = findViewById(R.id.aboutme_registerRenter);

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
        balanceAccount.setText(Double.toString(sessionAccount.balance));
        dataCardView.setVisibility(View.INVISIBLE);

        if (MainActivity.cookies.renter == null) {
            dataCardView.setVisibility(View.INVISIBLE);
            registerCardView.setVisibility(View.INVISIBLE);
            aboutRegisterRenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aboutRegisterRenter.setVisibility(View.INVISIBLE); //Button menghilang
                    registerCardView.setVisibility(View.VISIBLE);
                    dataCardView.setVisibility(View.INVISIBLE);
                    aboutRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Renter registerRenterAccount = requestRenter();
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
            registerCardView.setVisibility(View.VISIBLE);
            dataCardView.setVisibility(View.VISIBLE);

            nameRenter.setText(MainActivity.cookies.renter.username);
            addressRenter.setText(MainActivity.cookies.renter.address);
            phoneNumberRenter.setText(String.valueOf(MainActivity.cookies.renter.phoneNumber));
        }
    }

//    protected Account requestRenter() {
//        mApiService.registerRenterRequest(
//                MainActivity.cookies.id,
//                nameInput.getText().toString(),
//                addressInput.getText().toString(),
//                phoneNumberInput.getText().toString()
//                ).enqueue(new Callback<Account>() {
//            @Override
//            public void onResponse(Call<Account> call, Response<Account> response) {
//                if (response.isSuccessful()) {
//                    MainActivity.cookies = response.body();
//                    Toast.makeText(mContext, "Register Renter Successfull", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Account> call, Throwable t) {
//                System.out.println(t.toString());
//                Toast.makeText(mContext, "Register Renter Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        return null;
//    }

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
                    Toast.makeText(mContext, "Register Renter Successfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Renter> call, Throwable t) {
                Toast.makeText(mContext, "Register Renter Failed", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }



}