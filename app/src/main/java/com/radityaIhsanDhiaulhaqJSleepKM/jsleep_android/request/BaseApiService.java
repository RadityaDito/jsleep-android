package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request;

import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Account;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.BedType;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.City;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Facility;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Payment;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Renter;
import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Room;
//Base API Service
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {
    @GET("account/{id}")
    Call<Account> getAccount (@Path("id") int id);

    @POST("account/login")
    Call<Account> login (@Query("email") String email,
                         @Query("password") String password);

    @POST("/account/register")
    Call<Account> register  (@Query("name") String name,
                             @Query("email") String email,
                             @Query("password") String password);

    @POST("/account/{id}/registerStore")
    Call<Renter> registerRenterRequest (
            @Path("id") int id,
            @Query("username") String username,
            @Query("address")  String address,
            @Query("phoneNumber") String phoneNumber
    );

    @GET("room/getAllRoom")
    Call<List<Room>> getAllRoom (@Query("page") int page, @Query("pageSize") int pageSize);

    @POST("room/create")
    Call<Room> createRoom (@Query("accountId") int accountId,
                           @Query("name") String name,
                           @Query("size") int size,
                           @Query("price") int price,
                           @Query("facility") List<Facility> facility,
                           @Query("city") City city,
                           @Query("address") String address,
                           @Query("bedType") BedType bedType
    );


    @POST("account/{id}/topUp")
    Call<Boolean> topUpRequest(@Path("id") int id,
                        @Query("balance") double balance);

    //PaymentController BaseApi
//    @GET("payment/{id}")
//    Call<Payment> getPayment(@Path("id") int id);
//
    @POST("payment/create")
    Call<Payment> createPayment (@Query("buyerId") int buyerId,
                                 @Query("renterId") int renterId,
                                 @Query("roomId") int roomId,
                                 @Query("from") String from,
                                 @Query("to") String to);

    @POST("payment/{id}/accept")
    Call<Boolean> accept (@Path("id") int id);

    @POST("payment/{id}/cancel")
    Call<Boolean> cancel (@Path("id") int id);

    //RoomController BaseApi
    @GET("room/{id}")
    Call<Room> getRoom (@Path("id") int id);

    @GET("room/{id}/renter")
    Call<List<Room>> getRoomByRenter (@Path("id") int id,
                                      @Query("page") int page,
                                      @Query("pageSize") int pageSize);
    @GET("payment/getPayment")
    Call<Payment> getPayment (@Query("buyerId") int buyerId,
                              @Query("renterId") int renterId,
                              @Query("roomId") int roomId);

    @GET("payment/getHistory")
    Call<List<Payment>> getHistory(@Query("page") int page, @Query("pageSize") int pageSize, @Query("buyerId") int buyerId,
                                   @Query("renterId") int renterId);


    //VoucherController BaseApi
//    @GET("voucher/{id}")
//    Call<Voucher> getVoucher (@Path("id") int id);

    @GET("voucher/{id}/isUsed")
    Call<Boolean> isUsed (@Path("id") int id);

    @GET("voucher/{id}/canApply")
    Call<Boolean> canApply (@Path("id") int id,
                            @Query("price") double price);
//
//    @GET("voucher/getAvailable")
//    Call<List<Voucher>> getAvailable(@Query("page") int page,
//                                     @Query("pageSize") int pageSize);

}
