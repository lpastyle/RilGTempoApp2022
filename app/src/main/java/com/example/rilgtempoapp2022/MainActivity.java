package com.example.rilgtempoapp2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    public static String LOG_TAG = MainActivity.class.toString();
    public static IEdfApi edfApi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init Retrofit client
        Retrofit retrofitClient = ApiClient.get();
        if (retrofitClient != null) {
            // create API call interface
            edfApi = retrofitClient.create(IEdfApi.class);
        } else {
            Log.e(LOG_TAG,"retrofit init failed" );
            finish();
        }

        if (edfApi != null) {
            // create call
            Call<TempoDaysLeft> call = edfApi.getTempoDaysLeft("TEMPO");

            // launch call
            call.enqueue(new Callback<TempoDaysLeft>() {
                @Override
                public void onResponse(@NonNull Call<TempoDaysLeft> call, @NonNull Response<TempoDaysLeft> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                        Log.d(LOG_TAG,"nb red days = " + response.body().getParamNbJRouge());
                        Log.d(LOG_TAG,"nb white days = " + response.body().getParamNbJBlanc());
                        Log.d(LOG_TAG,"nb blue days = " + response.body().getParamNbJBleu());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TempoDaysLeft> call, @NonNull Throwable t) {
                    Log.e(LOG_TAG, "Call to 'getTempoDaysLeft' request failed");
                }
            });
        }


    }
}