package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Account;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.BaseApiService;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.UtilsApi;

//Login Activity
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;


public class LoginActivity extends AppCompatActivity {
    BaseApiService mApiService;
    EditText username, password;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mApiService = UtilsApi.getApiService();
        mContext = this;
        TextView register = findViewById(R.id.login_registerNow);
        Button login = findViewById(R.id.login_loginButton);
        username = findViewById(R.id.usernameTextBox);
        password = findViewById(R.id.passwordTextBox);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account account = requestAccount();
//                Intent move = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(move);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(move);
            }

        });
    }

    protected Account requestAccount(){
        mApiService.getAccount(0).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.isSuccessful()){
                    Account account;
                    account = response.body();
                    System.out.println("BERHASILL");
                    System.out.println(account.toString());
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t){
                System.out.println("gagal");
                System.out.println(t.toString());
                Toast.makeText(mContext, "no Account id=0", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
}