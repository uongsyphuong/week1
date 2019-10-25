package com.example.usphuong.flicks.api;

import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiModule {
    private static ApiService instance = null;
    public static ApiService getInstance(){
        if (instance == null) {
            synchronized (ApiService.class) {
                if (instance == null) {
                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                    httpClient.writeTimeout(15 * 60 * 1000, TimeUnit.MILLISECONDS);
                    httpClient.readTimeout(60 * 1000, TimeUnit.MILLISECONDS);
                    httpClient.connectTimeout(20 * 1000, TimeUnit.MILLISECONDS);
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor(
                            new HttpLoggingInterceptor.Logger() {
                                @Override
                                public void log(String message) {
                                    Log.d("API", message);
                                }
                            }
                    );
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                    httpClient.addNetworkInterceptor(logging);
                    httpClient.addNetworkInterceptor(new StethoInterceptor());

                    OkHttpClient client = httpClient.build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(ApiService.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client)
                            .build();
                    instance = retrofit.create(ApiService.class);
                }
            }
        }

        return instance;
    }
}
