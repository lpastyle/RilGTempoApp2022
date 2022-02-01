package com.example.rilgtempoapp2022;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IEdfApi {

    // https://particulier.edf.fr/bin/edf_rc/servlets/ejptempodaysnew?TypeAlerte=TEMPO
    @GET("bin/edf_rc/servlets/ejptempodaysnew")
    Call<TempoDaysLeft> getTempoDaysLeft(
            @retrofit2.http.Query("TypeAlerte") String alertType
    );


}
