package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Account;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.BaseApiService;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {
    BaseApiService mApiService;
    EditText username, password, email;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mApiService = UtilsApi.getApiService();
        mContext = this;
        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        email = findViewById(R.id.register_email);

        Button register = findViewById(R.id.register_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestRegister();
                System.out.println("aduihwuadauiwd");
            }
        });


    }

    protected Account requestRegister(){
        mApiService.register(username.getText().toString(),email.getText().toString(), password.getText().toString()).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.isSuccessful()){
                    MainActivity.cookies = response.body();
                    Intent move = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(move);
                    Toast.makeText(mContext, "Register Successfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t){
                System.out.println(t.toString());
                Toast.makeText(mContext, "Already registered", Toast.LENGTH_SHORT).show();
            }
        });

        return null;
    }
}
