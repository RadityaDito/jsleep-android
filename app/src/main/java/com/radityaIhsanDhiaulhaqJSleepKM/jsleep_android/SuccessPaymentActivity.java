package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

public class SuccessPaymentActivity extends AppCompatActivity {

    Context mContext;
    TextView paidAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_payment);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        paidAmount = findViewById(R.id.success_price);
        mContext = this;

        paidAmount.setText(BookingActivity.priceCurrency);

        Toast.makeText(mContext, "Back To Main Menu in 5 Seconds", Toast.LENGTH_LONG).show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent move = new Intent(SuccessPaymentActivity.this, MainActivity.class);
                        startActivity(move);
                    }
                }, 5000);
    }
}