package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Account;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Invoice;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Payment;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.BaseApiService;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookingActivity extends AppCompatActivity {
    BaseApiService mApiService;
    Context mContext;

    private DatePickerDialog datePickerDialog;
    private Button dateButtonFrom, dateButtonTo, saveBookButton, payButton, cancelButton;
    private int index = 0;
    protected static String from, to, from2, to2;
    protected static String totalPay;
    double roomPrice = DetailRoomActivity.sessionRoom.price.price;
    double accountBalance;
    long numDays = 0;

    protected TextView priceText;
    protected static String priceCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        mApiService = UtilsApi.getApiService();
        mContext = this;

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        dateButtonFrom = findViewById(R.id.booking_datePickerTo);
        dateButtonTo = findViewById(R.id.booking_datePickerFrom);
        saveBookButton = findViewById(R.id.booking_saveButton);
        payButton = findViewById(R.id.booking_payButton);
        cancelButton = findViewById(R.id.booking_cancelButton);

        TextView balanceText = findViewById(R.id.booking_balance);

        saveBookButton.setVisibility(Button.VISIBLE);
        payButton.setVisibility(Button.GONE);
        cancelButton.setVisibility(Button.GONE);

        //Update Saldo
        accountBalance = MainActivity.cookies.balance;
        System.out.println("saldo asli : "+MainActivity.cookies.balance);
        System.out.println(accountBalance);

        if((DetailRoomActivity.currentPayment != null) && (DetailRoomActivity.currentPayment.status == Invoice.PaymentStatus.WAITING)){
            dateButtonFrom.setText(simpleDateFormat(DetailRoomActivity.currentPayment.from));
            dateButtonTo.setText(simpleDateFormat(DetailRoomActivity.currentPayment.to));
            dateButtonTo.setEnabled(false);
            dateButtonFrom.setEnabled(false);

            //Update Balance
            String balanceCurrency = NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(accountBalance);
            balanceText.setText(balanceCurrency);


            //Update Price
            priceText = findViewById(R.id.booking_price);
            priceCurrency = NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(roomPrice * simpleCalcDays(DetailRoomActivity.currentPayment.from, DetailRoomActivity.currentPayment.to));
            totalPay = priceCurrency;
            priceText.setText(priceCurrency);

            saveBookButton.setVisibility(Button.GONE);
            payButton.setVisibility(Button.VISIBLE);
            cancelButton.setVisibility(Button.VISIBLE);

            payButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestAcceptPayment(DetailRoomActivity.currentPayment.id);
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestCancelPayment(DetailRoomActivity.currentPayment.id);
                }
            });
        }

        else{
            dateButtonTo.setEnabled(true);
            dateButtonFrom.setEnabled(true);
            //Default Value dari dateButton
            initDatePicker();
            from = getTodaysDate(0);
            to = getTodaysDate(1);

            from2 = from;
            to2 = to;

            dateButtonFrom.setText(from);
            dateButtonTo.setText(to);

            //dateButton ketika di klik
            dateButtonFrom.setOnClickListener(view -> {
                index = 1;
                datePickerDialog.show();
            });
            dateButtonTo.setOnClickListener(view -> {
                index = 2;
                datePickerDialog.show();
            });

            saveBookButton.setOnClickListener(view -> {
                numDays = calcDays(from, to);

//                System.out.println(MainActivity.cookies.id);
//                System.out.println(MainActivity.cookies.renter.id);
//                System.out.println(DetailRoomActivity.sessionRoom.id);
//                System.out.println(from);
//                System.out.println(formatDate(from));
//                System.out.println(formatDate(to));


                requestBooking(MainActivity.cookies.id,
                        MainActivity.cookies.renter.id,
                        DetailRoomActivity.sessionRoom.id,
                        formatDate(from),
                        formatDate(to));
            });

            updatePrice();
            String balanceCurrency = NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(accountBalance);
            balanceText.setText(balanceCurrency);
        }


    }

    private String getTodaysDate(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, offset);
        int year =cal.get(Calendar.YEAR);
        int month =cal.get(Calendar.MONTH);
        month = month + 1;
        int day =cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                if(index == 1){
                    from = date;
                    dateButtonFrom.setText(from);
                    updatePrice();
                }else if(index == 2){
                    String tempTo = to;
                    to = date;
                    if(calcDays(from, to) >= 1 ){
                        dateButtonTo.setText(to);
                        updatePrice();
                    }else{
                        to = tempTo;
                        Toast.makeText(mContext, "Min. 1 day of stay", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 30L *24*60*60*1000);
    }
    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        switch (month){
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
        }
        return null;
    }

    public String formatDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
        Date fDate = null;
        try {
            fDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        assert fDate != null;
        return sdfFormat.format(fDate);
    }

    public void updatePrice(){
        //Updating Price
        priceText = findViewById(R.id.booking_price);
        priceCurrency = NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(roomPrice * calcDays(from, to));
        totalPay = priceCurrency;
        priceText.setText(priceCurrency);
    }

    public long calcDays(String before, String after){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
        Date dateBefore = null;
        Date dateAfter = null;
        try {
            dateBefore = sdf.parse(before);
            dateAfter = sdf.parse(after);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeDiff = Math.abs(dateAfter.getTime() - dateBefore.getTime());
        long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
        return daysDiff;
    }

    public long simpleCalcDays(Date before, Date after){
        long timeDiff = Math.abs(after.getTime() - before.getTime());
        return TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    public String simpleDateFormat(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
        assert date != null;
        return sdf.format(date);
    }


    protected Payment requestBooking(int buyerId, int renterId, int roomId, String from, String to){
        mApiService.createPayment(buyerId, renterId, roomId,from, to).enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                System.out.println("Membuat Booking");
                if(response.isSuccessful()){
                    Payment payment;
                    payment = response.body();
                    System.out.println(payment.toString());
                    System.out.println("Payment Success");
                    Toast.makeText(mContext, "Booking Successful", Toast.LENGTH_LONG).show();
                    Intent move = new Intent(BookingActivity.this, MainActivity.class);
                    startActivity(move);
                    MainActivity.cookies.balance-= roomPrice * calcDays(from2, to2);
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                if(roomPrice * numDays  > accountBalance){
                    Toast.makeText(mContext, "Please top up first", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(mContext, "Please book another date", Toast.LENGTH_LONG).show();
                }
                System.out.println("Membuat Booking Gagal");
                Intent move = new Intent(BookingActivity.this, MainActivity.class);
                startActivity(move);
            }
        });
        return null;
    }

    protected Boolean requestAcceptPayment(int id){
        mApiService.accept(id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
//                    Toast.makeText(mContext, "Payment Successful", Toast.LENGTH_LONG).show();
                    Toast.makeText(mContext, "Payment Successful", Toast.LENGTH_SHORT).show();
                    requestAccount(MainActivity.cookies.id);
                    Intent move = new Intent(BookingActivity.this, SuccessPaymentActivity.class);
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
                    requestAccount(MainActivity.cookies.id);
                    Intent move = new Intent(BookingActivity.this, CancelPaymentActivity.class);
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

    protected Account requestAccount(int id){
        mApiService.getAccount(id).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.isSuccessful()){
                    Account account;
                    MainActivity.cookies = response.body();
                    System.out.println("BERHASILL");
                    System.out.println(MainActivity.cookies.toString());
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