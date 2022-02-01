package com.example.rilgtempoapp2022;

import com.google.gson.annotations.SerializedName;

public enum TempoColor {

    @SerializedName("TEMPO_ROUGE")
    RED(R.color.tempo_red_day_bg),

    @SerializedName("TEMPO_BLEU")
    BLUE(R.color.tempo_blue_day_bg),

    @SerializedName("TEMPO_BLANC")
    WHITE(R.color.tempo_white_day_bg),

    @SerializedName("ND")
    UNKNOWN(R.color.tempo_undecided_day_bg);

    private int resId; // Android color resource id

    /**
     * Ctor
     * @param id resource color id
     */
    TempoColor(int id) {
        resId=id;
    }

    public int getResId() {
        return resId;
    }
}
