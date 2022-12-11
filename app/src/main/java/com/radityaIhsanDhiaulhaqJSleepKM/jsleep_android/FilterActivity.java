package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.BedType;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.City;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Room;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.BaseApiService;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends AppCompatActivity {
    BaseApiService mApiService;
    Context mContext;
    ArrayAdapter adapterCity, adapterBedType;
    Spinner city, bedType;
    Button filterCity, filterBed, filterPrice, next, prev;
    ListView filterList;
    CardView filterCard;
    EditText filterMin, filterMax;
    boolean isCity = false, isBedType = false, isPrice = false;
    int pageNumber;

    protected static List<Room> filterResult, multipleResult;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        filterCity = findViewById(R.id.filter_cityButton);
        filterBed = findViewById(R.id.filter_bedTypeButton);
        filterPrice = findViewById(R.id.filter_priceButton);

        filterList = findViewById(R.id.filter_list);
        city = findViewById(R.id.filter_citySpinner);
        bedType = findViewById(R.id.filter_bedTypeSpinner);
        filterCard = findViewById(R.id.filter_card);
        filterMin = findViewById(R.id.filter_minPrice);
        filterMax = findViewById(R.id.filter_maxPrice);

        next = findViewById(R.id.filter_nextButton);
        prev = findViewById(R.id.filter_prevButton);
        pageNumber = 0;
        isCity = isBedType = false;


        next.setVisibility(View.GONE);
        prev.setVisibility(View.GONE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNumber++;
                System.out.println(pageNumber);
                if(isCity){
                    System.out.println("City next page");
                    filterByCity(pageNumber, 10, (City)(city.getSelectedItem()));
                }
                else if(isBedType){
                    filterByBed(pageNumber, 10, (BedType) (bedType.getSelectedItem()));
                }
               // getHistory(pageNumber,5, MainActivity.cookies.id, MainActivity.cookies.renter.id);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pageNumber >= 1){
                    pageNumber--;
                    System.out.println(pageNumber);
                    if(isCity){
                        System.out.println("City next page");
                        filterByCity(pageNumber, 5, (City)(city.getSelectedItem()));
                    }
                    else if(isBedType){
                        filterByBed(pageNumber, 5, (BedType) (bedType.getSelectedItem()));
                    }
                    else if(isPrice){
                        filterByPrice(pageNumber, 5,(int)Integer.parseInt(filterMin.getText().toString()), (int)Integer.parseInt(filterMax.getText().toString()));
                    }
                }
            }
        });

        adapterCity = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item , City.values());
        city.setAdapter(adapterCity);
        adapterBedType = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item , BedType.values());
        bedType.setAdapter(adapterBedType);

        mApiService = UtilsApi.getApiService();
        mContext = this;

        filterCard.setVisibility(ListView.VISIBLE);
        filterList.setVisibility(ListView.GONE);
        System.out.println(MainActivity.allRooms);
        filterCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterList.setVisibility(ListView.VISIBLE);
                filterCard.setVisibility(ListView.GONE);
                multipleResult = filterByCity(0, 10,(City)(city.getSelectedItem()));
                System.out.println("Masuk Sini");
                isCity = true;
            }
        });

        filterBed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterList.setVisibility(ListView.VISIBLE);
                filterCard.setVisibility(ListView.GONE);
                multipleResult = filterByBed(0, 10,(BedType) (bedType.getSelectedItem()));
                System.out.println("Masuk Sini2");
                isBedType = true;
            }
        });

        filterPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterList.setVisibility(ListView.VISIBLE);
                filterCard.setVisibility(ListView.GONE);
                System.out.println(Integer.parseInt(filterMax.getText().toString()));
                int minPrice = (int)Integer.parseInt(filterMin.getText().toString());
                int maxPrice = (int) Integer.parseInt(filterMax.getText().toString());

                multipleResult = filterByPrice(0, 5,minPrice, maxPrice);
                System.out.println("Masuk Sini3");
                isPrice = true;
            }
        });

        filterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MainActivity.roomIndex = position;
                System.out.println(MainActivity.roomIndex);
                Intent move = new Intent(FilterActivity.this, DetailRoomActivity.class);
                startActivity(move);
            }
        });

    }

    protected List<Room> filterByCity(int page, int pageSize, City city){
        mApiService.filterByCity(page, pageSize, city).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if(response.isSuccessful()){
                    ArrayList<String> temporary = new ArrayList<>();
                    filterResult = response.body();
                    for(Room i : filterResult){
                        temporary.add(i.name);
                    }
                    ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1,temporary);
                    filterList.setAdapter(itemAdapter);
                    next.setVisibility(View.VISIBLE);
                    prev.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
        return null;
    }

    protected List<Room> filterByBed(int page, int pageSize, BedType bedType){
        mApiService.filterByBed(page, pageSize, bedType).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if(response.isSuccessful()){
                    ArrayList<String> temporary = new ArrayList<>();
                    filterResult = response.body();
                    for(Room i : filterResult){
                        temporary.add(i.name);
                    }
                    ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1,temporary);
                    filterList.setAdapter(itemAdapter);
                    next.setVisibility(View.VISIBLE);
                    prev.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
        return null;
    }

    protected List<Room> filterByPrice(int page, int pageSize, int minPrice, int maxPrice){
        mApiService.filterByPrice(page, pageSize, minPrice, maxPrice).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                ArrayList<String> temporary = new ArrayList<>();
                System.out.println(response.body());
                filterResult = response.body();
                for(Room i : filterResult){
                    temporary.add(i.name);
                }
                ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1,temporary);
                filterList.setAdapter(itemAdapter);
                next.setVisibility(View.VISIBLE);
                prev.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
        return null;
    }

}