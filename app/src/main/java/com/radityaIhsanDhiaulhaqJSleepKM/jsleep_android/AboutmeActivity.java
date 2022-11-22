package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Account;

public class AboutmeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutme);

        Account sessionAccount = MainActivity.cookies;
        TextView nameAccount = findViewById(R.id.about_name);
        TextView emailAccount = findViewById(R.id.about_email);
        TextView balanceAccount = findViewById(R.id.about_balance);

        nameAccount.setText(sessionAccount.name, sessionAccount.);
        emailAccount.setText(sessionAccount.email);
        balanceAccount.setText(Double.toString(sessionAccount.balance));
    }
}