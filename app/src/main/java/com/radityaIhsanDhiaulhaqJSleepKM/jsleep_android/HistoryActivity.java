package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Payment;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.BaseApiService;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {
    BaseApiService mApiService;
    Context mContext;
    ListView paymentList;
    public static List <Payment> payments;
    int pageNumber;
    Button next, prev;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mApiService = UtilsApi.getApiService();
        mContext = this;
        paymentList = findViewById(R.id.history_list);
        pageNumber = 0;
        next = findViewById(R.id.history_nextButton);
        prev = findViewById(R.id.history_prevButton);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNumber++;
                System.out.println(pageNumber);
                getHistory(pageNumber,5, MainActivity.cookies.id, MainActivity.cookies.renter.id);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pageNumber >= 1){
                    pageNumber--;
                    System.out.println(pageNumber);
                    getHistory(pageNumber, 5, MainActivity.cookies.id, MainActivity.cookies.renter.id);
                }
            }
        });



        getHistory(0, 5, MainActivity.cookies.id, MainActivity.cookies.renter.id);

    }

    protected List<Payment> getHistory(int page, int pageSize, int buyerId, int renterId){
        mApiService.getHistory(page, pageSize, buyerId, renterId).enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                if(response.isSuccessful()){
                    ArrayList<String> temporary = new ArrayList<>();
                    payments = response.body();
                    for(Payment i : payments){
                        temporary.add("" + MainActivity.allRooms.get(i.getRoomId()).name + " STATUS: " + i.status);
//                        temporary.add("" + i.getRoomId() + i.status + "");
                    }
                    ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1,temporary);
                    paymentList.setAdapter(itemAdapter);

                    Toast.makeText(mContext, "getHistory successfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {
                System.out.println(t.toString());
                Toast.makeText(mContext, "Failed to getHistory", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }

}

