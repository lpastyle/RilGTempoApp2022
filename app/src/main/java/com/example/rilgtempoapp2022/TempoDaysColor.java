package com.example.rilgtempoapp2022;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TempoDaysColor {

    @SerializedName("JourJ1")
    @Expose
    private JourJ1 jourJ1;
    @SerializedName("JourJ")
    @Expose
    private JourJ jourJ;

    public JourJ1 getJourJ1() {
        return jourJ1;
    }

    public void setJourJ1(JourJ1 jourJ1) {
        this.jourJ1 = jourJ1;
    }

    public JourJ getJourJ() {
        return jourJ;
    }

    public void setJourJ(JourJ jourJ) {
        this.jourJ = jourJ;
    }


    public static class JourJ {

        @SerializedName("Tempo")
        @Expose
        private String tempo;

        public String getTempo() {
            return tempo;
        }

        public void setTempo(String tempo) {
            this.tempo = tempo;
        }

    }

    public static  class JourJ1 {

        @SerializedName("Tempo")
        @Expose
        private String tempo;

        public String getTempo() {
            return tempo;
        }

        public void setTempo(String tempo) {
            this.tempo = tempo;
        }

    }

}
