package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Account;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Invoice;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Payment;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Room;

import java.util.ArrayList;
import java.util.List;

import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.BaseApiService;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    public static Account cookies;
    public static Room roomCookies;
    public static int roomIndex;
//    static List<Room> roomList = new ArrayList<Room>();
    public static List<Room> rooms;
    public static List<Room> allRooms;
    List<Room> roomActivity;
    EditText pageInput;
    ListView list;
    BaseApiService mApiService;
    Context mContext;
    Button prev, next, go, historyButton;
    int pageNumber;

    public static List <Payment> payments;
    ListView paymentList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApiService = UtilsApi.getApiService();
        mContext = this;
        historyButton = findViewById(R.id.main_historyButton);

        next = findViewById(R.id.main_next_button);
        prev = findViewById(R.id.main_prev_button);
        go = findViewById(R.id.main_goButton);
        pageInput = findViewById(R.id.main_Input);
        list = (ListView) findViewById(R.id.main_listView);
        pageNumber = 0;
        getAllRooms(0, 1000); //Untuk Mendapatkan Seluruh Nilai Room

        getRoomList(pageNumber,5); //Call

//        ArrayList<Room> listRoom = new ArrayList<>();
//        ArrayList<String> listId = new ArrayList<>();
//
//        Gson gson = new Gson();
//        try {
//            path = getAssets().open("randomRoomList.json");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(path));
//
//            Room[] tempRoom = gson.fromJson(reader, Room[].class);
//            Collections.addAll(listRoom, tempRoom);
//
////            InputStream filepath = getAssets().open("randomRoomList.json");
////            BufferedReader reader = new BufferedReader(new InputStreamReader(filepath));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        for (Room r : listRoom ) {
//            listId.add(r.name);
//        }
//
//        ArrayAdapter<String> roomArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listId);
//        ListView listView = findViewById(R.id.main_listView);
//
//        listView.setAdapter(roomArrayAdapter);

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(allRooms);
                Intent move = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(move);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNumber++;
                System.out.println(pageNumber);
                getRoomList(pageNumber,5);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pageNumber >= 1){
                    pageNumber--;
                    System.out.println(pageNumber);
                    getRoomList(pageNumber, 5);
                }
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(pageInput.getText().toString()) >= 0){
                    pageNumber = Integer.parseInt(pageInput.getText().toString());
                    System.out.println(pageNumber);
                    getRoomList(pageNumber, 5);
                }
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                roomIndex = position;
                System.out.println(roomIndex);
                Intent move = new Intent(MainActivity.this, DetailRoomActivity.class);
                startActivity(move);
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        MenuItem addButton = menu.findItem(R.id.addbox_button);
        if(cookies.renter == null){
            addButton.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.person_button://Person button diklik intent ke aboutme
                Intent move = new Intent(MainActivity.this, AboutmeActivity.class);
                startActivity(move);
                return true;
            case R.id.addbox_button: //Addbox button diklik intent ke createroom
                Intent move2 = new Intent(MainActivity.this, CreateRoomActivity.class);
                startActivity(move2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    protected List<Room> getRoomList(int page, int pageSize){
        mApiService.getAllRoom(page, pageSize).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if(response.isSuccessful()){
                    System.out.println("Success");
                    ArrayList<String> temporary = new ArrayList<>();
                    rooms = response.body();
                    for(Room i : rooms){
                        temporary.add(i.name);
                    }
                    ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1,temporary);
                    list.setAdapter(itemAdapter);

                    Toast.makeText(mContext, "getRoomList successfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                System.out.println("Failed");
                System.out.println(t.toString());
                Toast.makeText(mContext, "Failed to getRoomList", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }

    protected List<Payment> getHistory(int page, int pageSize, int buyerId, int renterId){
        mApiService.getHistory(page, pageSize, buyerId, renterId).enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                if(response.isSuccessful()){
                    ArrayList<String> temporary = new ArrayList<>();
                    payments = response.body();
                    for(Payment i : payments){
                        temporary.add(MainActivity.rooms.get(i.getRoomId()).name + " STATUS: " + i.status);
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

    protected List<Room> getAllRooms(int page, int pageSize){
        mApiService.getAllRoom(page, pageSize).enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if(response.isSuccessful()){
                    allRooms = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                System.out.println("Failed");
                System.out.println(t.toString());
                Toast.makeText(mContext, "Failed to getAllRooms", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }




}