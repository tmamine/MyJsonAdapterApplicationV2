package fr.exemple.myjsonadapterapplication.pojo;

import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;


public class Prayer {

    private final String POSTER_BASE_URL = "http://api.aladhan.com/v1/calendar?latitude=48.4186574&longitude=-4.4948202&method=FRANCE&month=4&year=2020";

    //@SerializedName("namePrayer")
    private String namePrayer;

    private String timePrayer;

    public Prayer() {
    }

    public Prayer(String namePrayer, String timePrayer) {
        this.namePrayer = namePrayer;
        this.timePrayer = timePrayer;

    }

    public String getNamePrayer() {
        return namePrayer;
    }

    public String getTimePrayer() {
        return timePrayer;
    }

    public void setNamePrayer(String namePrayer) {
        this.namePrayer = namePrayer;
    }

    public void setTimePrayer(String timePrayer) {
        this.timePrayer = timePrayer;
    }

    @NonNull
    @Override
    public String toString() {
        return "la prière : "+ namePrayer + " à l'heure suivante : "+timePrayer;
    }
}

