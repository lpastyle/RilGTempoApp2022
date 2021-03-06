package com.example.rilgtempoapp2022;

import static com.example.rilgtempoapp2022.Tools.getNowDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {
    private static final String LOG_TAG = HistoryActivity.class.getSimpleName();
    private final Context context = this;
    private final List<TempoDate> tempoDates = new ArrayList<>();

    // views
    private RecyclerView tempoDateRv;
    private TempoDateAdapter tempoDateAdapter;

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
        tempoDateRv.setAdapter(tempoDateAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshTempoDates();
    }

    private void refreshTempoDates() {
        if (MainActivity.edfApi != null) {

            // get date range
            String yearNow = getNowDate("yyyy");
            String yearBefore = String.valueOf(Integer.parseInt(yearNow) - 1);

           // Create API call
            Call<TempoHistory> call = MainActivity.edfApi.getTempoHistory(yearBefore, yearNow);

            // launch call
            call.enqueue(new Callback<TempoHistory>() {
                @Override
                public void onResponse(Call<TempoHistory> call, Response<TempoHistory> response) {
                    tempoDates.clear();
                    if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                        TempoHistory tempoHistory = response.body();
                        tempoDates.addAll(tempoHistory.getTempoDates());
                        Log.d(LOG_TAG, "nb elements = "+ tempoDates.size());
                        //showTempoHistoryDates(tempoDates);
                    }
                    tempoDateAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<TempoHistory> call, Throwable t) {
                    Log.e(LOG_TAG, "Call to 'getTempoHistory' failed");
                    Toast toast = Toast.makeText(context, R.string.toast_network_error, Toast.LENGTH_LONG);
                    toast.show();
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