package com.fyp.confit;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
    //private static final String BASE_URL = "https://ab9a0d47.ngrok.io";
    private static final String BASE_URL = "https://confit-speech.herokuapp.com";
   //  private static final String BASE_URL = "https://ff77a2b02ed9";

    private static Retrofit retrofit;
    public static Retrofit getRetrofitClient(Context context) {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}