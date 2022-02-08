package com.example.rilgtempoapp2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    public static String LOG_TAG = MainActivity.class.toString();
    private static final String  CHANNEL_ID = "tempo_notif_channel_id";
    public static IEdfApi edfApi = null;
    private final Context context = this;

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
                public void onResponse(@NonNull Call<TempoDaysColor> call, @NonNull Response<TempoDaysColor> response) {
                    TempoDaysColor tempoDaysColor = response.body();
                    if (response.code() == HttpURLConnection.HTTP_OK && tempoDaysColor != null) {
                        Log.d(LOG_TAG, "J day color = " + tempoDaysColor.getJourJ().getTempo().toString());
                        Log.d(LOG_TAG, "J1 day color = " + tempoDaysColor.getJourJ1().getTempo().toString());
                        todayDcv.setDayColor(tempoDaysColor.getJourJ().getTempo());
                        tomorrowDcv.setDayColor(tempoDaysColor.getJourJ1().getTempo());
                        // this call is for notification demo purpose only, it should be done in a service to have sense
                        // checkColor4notif(tempoDaysColor);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TempoDaysColor> call, @NonNull Throwable t) {
                    Log.e(LOG_TAG, "Call to 'getTempoDaysColor' request failed");
                    Toast toast = Toast.makeText(context, R.string.toast_network_error, Toast.LENGTH_LONG);
                    toast.show();

                }
            });
        }
    }

    private void checkColor4notif(TempoDaysColor tempoDaysColor) {
        Log.d(LOG_TAG,"checkColor4notif()");
        if (tempoDaysColor.getJourJ1().getTempo() == TempoColor.RED || tempoDaysColor.getJourJ1().getTempo() == TempoColor.WHITE) {
            // Create notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.tempo_alert_title))
            .setContentText(getString(R.string.tempo_alert_message, tempoDaysColor.getJourJ1().getTempo()))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // show notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            // notificationId is a unique int for each notification that you must define
            int nid = Tools.getNextNotifId();
            Log.d(LOG_TAG, "create notif n°" + nid);
            notificationManager.notify(nid, builder.build());
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
                    Toast toast = Toast.makeText(context, R.string.toast_network_error, Toast.LENGTH_LONG);
                    toast.show();
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