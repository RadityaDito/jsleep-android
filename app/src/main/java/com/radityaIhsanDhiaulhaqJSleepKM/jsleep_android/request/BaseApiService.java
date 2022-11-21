package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.request;

import com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model.Account;
//Base API Service
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BaseApiService {
    @GET("account/{id}")
    Call<Account> getAccount (@Path("id") int id);
}
