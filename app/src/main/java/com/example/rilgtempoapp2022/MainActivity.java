package com.example.rilgtempoapp2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    public static String LOG_TAG = MainActivity.class.toString();
    private static final String  CHANNEL_ID = "tempo_notif_channel_id";
    public static IEdfApi edfApi = null;

    // views
    private TextView redDaysTv;
    private TextView whiteDaysTv;
    private TextView blueDaysTv;

    private DayColorView todayDcv;
    private DayColorView tomorrowDcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        // init views
        redDaysTv = findViewById(R.id.red_days_tv);
        whiteDaysTv = findViewById(R.id.white_days_tv);
        blueDaysTv = findViewById(R.id.blue_days_tv);
        todayDcv = findViewById(R.id.today_dcv);
        tomorrowDcv = findViewById(R.id.tomorrow_dcv);

        // init Retrofit client
        Retrofit retrofitClient = ApiClient.get();
        if (retrofitClient != null) {
            // create API call interface
            edfApi = retrofitClient.create(IEdfApi.class);
        } else {
            Log.e(LOG_TAG,"retrofit init failed" );
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNbTempoDaysLeft();
        updateNbTempoDaysColors();
    }

    private void updateNbTempoDaysColors() {
        if (edfApi != null) {

            // create call
            Call<TempoDaysColor> call = edfApi.getTempoDaysColor(Tools.getNowDate("yyyy-MM-dd"), IEdfApi.EDF_TEMPO_ALERT_TYPE);

            // launch call
            call.enqueue(new Callback<TempoDaysColor>() {
                @Override
                public void onResponse(Call<TempoDaysColor> call, Response<TempoDaysColor> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                        Log.d(LOG_TAG, "J day color = " + response.body().getJourJ().getTempo().toString());
                        Log.d(LOG_TAG, "J1 day color = " + response.body().getJourJ1().getTempo().toString());
                        todayDcv.setDayColor(response.body().getJourJ().getTempo());
                        tomorrowDcv.setDayColor(response.body().getJourJ1().getTempo());
                    }
                }

                @Override
                public void onFailure(Call<TempoDaysColor> call, Throwable t) {
                    Log.e(LOG_TAG, "Call to 'getTempoDaysColor' request failed");
                }
            });
        }
    }

    private void updateNbTempoDaysLeft() {
        if (edfApi != null) {
            // create call
            Call<TempoDaysLeft> call = edfApi.getTempoDaysLeft(IEdfApi.EDF_TEMPO_ALERT_TYPE);

            // launch call
            call.enqueue(new Callback<TempoDaysLeft>() {
                @Override
                public void onResponse(@NonNull Call<TempoDaysLeft> call, @NonNull Response<TempoDaysLeft> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                        Log.d(LOG_TAG,"nb red days = " + response.body().getParamNbJRouge());
                        Log.d(LOG_TAG,"nb white days = " + response.body().getParamNbJBlanc());
                        Log.d(LOG_TAG,"nb blue days = " + response.body().getParamNbJBleu());
                        redDaysTv.setText(String.valueOf(response.body().getParamNbJRouge()));
                        whiteDaysTv.setText(String.valueOf(response.body().getParamNbJBlanc()));
                        blueDaysTv.setText(String.valueOf(response.body().getParamNbJBleu()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TempoDaysLeft> call, @NonNull Throwable t) {
                    Log.e(LOG_TAG, "Call to 'getTempoDaysLeft' request failed");
                }
            });
        }

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showHistory(View view) {
        Log.d(LOG_TAG,"showHistory()");
        Intent intent = new Intent();
        intent.setClass(this, HistoryActivity.class);
        startActivity(intent);
    }
}