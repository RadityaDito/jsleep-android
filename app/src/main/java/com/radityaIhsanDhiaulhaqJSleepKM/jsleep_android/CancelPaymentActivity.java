package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class CancelPaymentActivity extends AppCompatActivity {
    Context mContext;
    TextView returnAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_payment);

        mContext = this;
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        returnAmount = findViewById(R.id.cancel_price);
        returnAmount.setText(BookingActivity.priceCurrency);

        Toast.makeText(mContext, "Back To Main Menu in 5 Seconds", Toast.LENGTH_LONG).show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent move = new Intent(CancelPaymentActivity.this, MainActivity.class);
                        startActivity(move);
                    }
                }, 5000);
    }
}