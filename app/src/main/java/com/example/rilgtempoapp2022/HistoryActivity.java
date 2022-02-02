package com.example.rilgtempoapp2022;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {
    private static final String LOG_TAG = HistoryActivity.class.getSimpleName();

    private final List<TempoDate> tempoDates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshTempoDates();
    }

    private void refreshTempoDates() {
        if (MainActivity.edfApi != null) {

           // Create API call
            Call<TempoHistory> call = MainActivity.edfApi.getTempoHistory("2021","2022");

            // launch call
            call.enqueue(new Callback<TempoHistory>() {
                @Override
                public void onResponse(Call<TempoHistory> call, Response<TempoHistory> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                        TempoHistory tempoHistory = response.body();
                        tempoDates.addAll(tempoHistory.getTempoDates());
                        Log.d(LOG_TAG, "nb elements = "+ tempoDates.size());
                    }
                }

                @Override
                public void onFailure(Call<TempoHistory> call, Throwable t) {
                    Log.e(LOG_TAG, "Call to 'getTempoHistory' failed");
                }
            });
        }


    }

}