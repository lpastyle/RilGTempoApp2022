package com.example.rilgtempoapp2022;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class Tools {
    private static AtomicInteger atomicInteger = null;
    private static final int INITIAL_GENERATOR_VALUE = 2018;


    // prevent object instanciation
    private Tools() {
    }
    /*
     * --- Helpers methods ---
     *
     */
    public static String getNowDate(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.FRANCE);
        Date now = new Date();
        return sdf.format(now);
    }

    public static int getNextNotifId() {
        if (atomicInteger == null) {
            atomicInteger = new AtomicInteger(INITIAL_GENERATOR_VALUE);
            return atomicInteger.get();
        } else {
            return atomicInteger.incrementAndGet();
        }
    }

}
