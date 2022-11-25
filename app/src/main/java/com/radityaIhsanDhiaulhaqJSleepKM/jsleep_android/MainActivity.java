package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Account;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Renter;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Room;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Account;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.BaseApiService;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    public static Account cookies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream path = null;

        ArrayList<Room> listRoom = new ArrayList<>();
        ArrayList<String> listId = new ArrayList<>();

        Gson gson = new Gson();
        try {
            path = getAssets().open("randomRoomList.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(path));

            Room[] tempRoom = gson.fromJson(reader, Room[].class);
            Collections.addAll(listRoom, tempRoom);

//            InputStream filepath = getAssets().open("randomRoomList.json");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(filepath));

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Room r : listRoom ) {
            listId.add(r.name);
        }

        ArrayAdapter<String> roomArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listId);
        ListView listView = findViewById(R.id.main_listView);

        listView.setAdapter(roomArrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
//        Button item = findViewById(R.id.person_button);
//
//        aboutMe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent move = new Intent(MainActivity.this, AboutmeActivity.class);
//                startActivity(move);
//            }
//        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.person_button:
                Intent move = new Intent(MainActivity.this, AboutmeActivity.class);
                startActivity(move);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}