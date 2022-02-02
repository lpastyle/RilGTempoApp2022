package com.example.rilgtempoapp2022;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IEdfApi {

    String EDF_TEMPO_ALERT_TYPE = "TEMPO";

    // https://particulier.edf.fr/bin/edf_rc/servlets/ejptempodaysnew?TypeAlerte=TEMPO
    @GET("bin/edf_rc/servlets/ejptempodaysnew")
    Call<TempoDaysLeft> getTempoDaysLeft(
            @retrofit2.http.Query("TypeAlerte") String alertType
    );

    // https://particulier.edf.fr/bin/edf_rc/servlets/ejptemponew?Date_a_remonter=2021-01-26&TypeAlerte=TEMPO
    @GET("bin/edf_rc/servlets/ejptemponew")
    Call<TempoDaysColor> getTempoDaysColor(
            @retrofit2.http.Query("Date_a_remonter") String date,
            @retrofit2.http.Query("TypeAlerte") String alertType
    );
}
