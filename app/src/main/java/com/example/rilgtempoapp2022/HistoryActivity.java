package com.example.rilgtempoapp2022;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    // views
    RecyclerView tempoDateRv;
    TempoDateAdapter tempoDateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // find view
        tempoDateRv = findViewById(R.id.tempo_history_rv);

        // Recycler view init
        tempoDateRv.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        tempoDateRv.setLayoutManager(layoutManager);

        tempoDateAdapter = new TempoDateAdapter(this, tempoDates);
        //tempoDateRv.setAdapter(tempoDateAdapter);

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
                        showTempoHistoryDates(tempoDates);
                    }
                }

                @Override
                public void onFailure(Call<TempoHistory> call, Throwable t) {
                    Log.e(LOG_TAG, "Call to 'getTempoHistory' failed");
                }
            });
        }


    }

    // -------------- HELPER FUNCTIONS ---------------

    private void showTempoHistoryDates(List<TempoDate> tempoDates) {
        for(TempoDate tempoDate : tempoDates) {
             Log.d(LOG_TAG,"Date="+tempoDate.getDate()+" Color="+tempoDate.getCouleur());
        }
    }

}