package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Facility;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Invoice;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Payment;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Room;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.BaseApiService;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.UtilsApi;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRoomActivity extends AppCompatActivity {
    protected static Room sessionRoom;
    protected static Payment currentPayment;
    List<Facility> facilityList = MainActivity.rooms.get(MainActivity.roomIndex).facility;
    CheckBox checkboxAC, checkboxBalcony, checkboxBathtub, checkboxFitnessCenter, checkboxRefrigerator, checkboxRestaurant, checkboxSwimPool, checkboxWifi;
    Button rentButton, payButton, cancelButton;
    CardView afterRent;
    TextView bookSuccess, bookFailed;
    BaseApiService mApiService;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_room);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}


        mApiService = UtilsApi.getApiService();
        mContext = this;
        sessionRoom = MainActivity.rooms.get(MainActivity.roomIndex);
        //TextView
        TextView nameDetail = findViewById(R.id.detail_name);
        TextView bedDetail = findViewById(R.id.detail_bedType);
        TextView sizeDetail = findViewById(R.id.detail_size);
        TextView priceDetail = findViewById(R.id.detail_price);
        TextView addressDetail = findViewById(R.id.detail_address);
        bookSuccess = findViewById(R.id.detail_successBook);
//        bookFailed = findViewById(R.id.detail_failedBook);
        //Checkbox
         checkboxAC = findViewById(R.id.cb_AC);
         checkboxBalcony = findViewById(R.id.cb_balcony);
         checkboxBathtub = findViewById(R.id.cb_bathtub);
         checkboxFitnessCenter = findViewById(R.id.cb_fitnessCenter);
         checkboxRefrigerator = findViewById(R.id.cb_refrigerator);
         checkboxRestaurant = findViewById(R.id.cb_restaurant);
         checkboxSwimPool = findViewById(R.id.cb_swimmingPool);
         checkboxWifi = findViewById(R.id.cb_wifi);
         fillCheckbox();

//        afterRent = findViewById(R.id.detail_paymentCardView);
//        afterRent.setVisibility(CardView.INVISIBLE);

        payButton = findViewById(R.id.detail_confirmPay);
        payButton.setVisibility(Button.GONE);
//        cancelButton = findViewById(R.id.detail_cancelButton);

         rentButton = findViewById(R.id.detail_rentButton);
         rentButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent move = new Intent(DetailRoomActivity.this, BookingActivity.class);
                 startActivity(move);
             }
         });

         currentPayment = requestGetPayment(MainActivity.cookies.id, MainActivity.cookies.renter.id, sessionRoom.id);


        String price = NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(sessionRoom.price.price);
        String address = sessionRoom.address + ", " + sessionRoom.city;

        System.out.println(sessionRoom);

        nameDetail.setText(sessionRoom.name);
        bedDetail.setText(sessionRoom.bedType.toString());
        sizeDetail.setText(sessionRoom.size + "m\u00B2");
        priceDetail.setText(price);
        addressDetail.setText(address);


    }

    public void mainLogic(){
        System.out.println(currentPayment);
//        if(currentPayment.status == Invoice.PaymentStatus.SUCCESS || currentPayment.status == Invoice.PaymentStatus.FAILED){
//            System.out.println(currentPayment.status);
//        }
          rentButton.setVisibility(Button.VISIBLE);
        bookSuccess.setVisibility(TextView.INVISIBLE);
//        bookFailed.setVisibility(TextView.INVISIBLE);
        if(currentPayment != null && currentPayment.status == Invoice.PaymentStatus.WAITING){
            System.out.println("Masok siniii deh");
//            afterRent.setVisibility(CardView.VISIBLE);
            rentButton.setVisibility(Button.GONE);
            payButton.setVisibility(Button.VISIBLE);
            payButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent move = new Intent(DetailRoomActivity.this, BookingActivity.class);
                    startActivity(move);
//                    requestAcceptPayment(currentPayment.id);
                }
            });
//            cancelButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    requestCancelPayment(currentPayment.id);
//                }
//            });
        }
        else if(currentPayment != null && currentPayment.status == Invoice.PaymentStatus.SUCCESS){
//            afterRent.setVisibility(CardView.INVISIBLE);
            payButton.setVisibility(Button.GONE);
            rentButton.setVisibility(Button.GONE);
            bookSuccess.setVisibility(TextView.VISIBLE);
        }
//        else if(currentPayment != null && currentPayment.status == Invoice.PaymentStatus.FAILED){
//            afterRent.setVisibility(CardView.INVISIBLE);
//            rentButton.setVisibility(Button.INVISIBLE);
//            bookFailed.setVisibility(TextView.VISIBLE);
//        }


    }

    public void fillCheckbox(){
        for(Facility facility: facilityList){
            if (facility == Facility.AC){
                checkboxAC.setChecked(true);
            }else if(facility == Facility.Balcony){
                checkboxBalcony.setChecked(true);
            }else if(facility == Facility.Bathtub) {
                checkboxBathtub.setChecked(true);
            }else if(facility == Facility.FitnessCenter){
                checkboxFitnessCenter.setChecked(true);
            }else if(facility == Facility.Refrigerator){
                checkboxRefrigerator.setChecked(true);
            }else if(facility == Facility.Restaurant){
                checkboxRestaurant.setChecked(true);
            }else if(facility == Facility.SwimmingPool){
                checkboxSwimPool.setChecked(true);
            }else if(facility == Facility.WiFi){
                checkboxWifi.setChecked(true);
            }
        }
    }

    protected Boolean requestAcceptPayment(int id){
        mApiService.accept(id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
//                    Toast.makeText(mContext, "Payment Successful", Toast.LENGTH_LONG).show();
                    Toast.makeText(mContext, "Payment Successful", Toast.LENGTH_SHORT).show();
                    Intent move = new Intent(DetailRoomActivity.this, MainActivity.class);
                    startActivity(move);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println(t.toString());
                Toast.makeText(mContext, "Payment Failed", Toast.LENGTH_LONG).show();
            }
        });
        return null;
    }

    protected Boolean requestCancelPayment(int id){
        mApiService.cancel(id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Toast.makeText(mContext, "Payment Canceled", Toast.LENGTH_LONG).show();
                    Intent move = new Intent(DetailRoomActivity.this, MainActivity.class);
                    startActivity(move);
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println(t.toString());
                Toast.makeText(mContext, "Error!!", Toast.LENGTH_LONG).show();
            }
        });
        return null;
    }

    protected Payment requestGetPayment(int buyerId, int renterId, int roomId){
        mApiService.getPayment(buyerId, renterId, roomId).enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if(response.isSuccessful()){
                    currentPayment = response.body();
                    System.out.println(currentPayment);
                    mainLogic();
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                System.out.println(t.toString());
                currentPayment = null;
                mainLogic();
            }
        });
        return null;
    }
}