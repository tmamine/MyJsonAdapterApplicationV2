package fr.exemple.myjsonadapterapplication.loader;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.exemple.myjsonadapterapplication.pojo.Prayer;

/**
 * Class responsible for downloading data from the Adhan API.
 *
 * @author Mohammed Amine TALBAOUI
 */

public class AdhanLoader {
    /**
     * The interface which will be notified when the movies are downloaded
     */
    public interface AdhanListener<AnyType> {
        void onPrayerDownloaded(AnyType a);
        void onErrorDownloading(String errorMessage);
    }

    /**
     * Singleton instance of this class
     */
    private static AdhanLoader tmdbLoader = null;

    /**
     * RequestQueue Volley-library
     */
    protected RequestQueue queue;
    /**
     * Activity context
     */
    protected Context context;
    /**
     * API KEY
     */
    protected String apiKey = null;

    /**
     * Private constructor. Called from getInstance().
     *
     * @param context Activity-context
     * @see #getInstance(Context)
     */
    private AdhanLoader(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    /**
     * Initalizing a AdhanLoader object if null.
     *
     * @param context Activity-context
     * @return Singleton object
     */
    public static synchronized AdhanLoader getInstance(Context context) {
        if (tmdbLoader == null)
            tmdbLoader = new AdhanLoader(context);

        return tmdbLoader;
    }

    /**
     * Fetching popular movies
     *
     * @param listener listener, fired when downloaded
     */
    public void getPrayers(final AdhanListener<ArrayList<Prayer>> listener) {

        Log.d("AdhanLoader", "getPrayers : start");

        // This is the base URL where the movies are downloaded from
        String url = "http://api.aladhan.com/v1/timingsByCity?city=Brest&country=France&method=12";
        // Append the api key to the url
        Uri uri = Uri.parse(url).buildUpon()
                .appendQueryParameter("api_key", apiKey)
                .build();

        // Construction a JsonObjectRequest.
        // This means that we expect to receive a JsonObject from the API.
        final JsonObjectRequest json = new JsonObjectRequest(
                Request.Method.GET, uri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // This indicates that the call was successfull.

                        // Creating an ArrayList with Movie-objects.
                        ArrayList<Prayer> prayers = parsePrayers(response);

                        // Firing our interface-method, returning the movies to the activity
                        listener.onPrayerDownloaded(prayers);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // This indicates that the call wasn't successful.
                // We'll return a String to our activity
                listener.onErrorDownloading("Error connecting to the API");
            }
        }
        );

        // Adding the response to our requestqueue
        queue.add(json);

        Log.d("AdhanLoader", "getPrayers : stop");
    }

    /**
     * Fetching movies from json provided by the TMDB API.
     *
     * @param jsonObject
     * @return ArrayList of movies
     */
    private ArrayList<Prayer> parsePrayers(JSONObject jsonObject) {
        Log.d("AdhanLoader", "parsePrayers : start");
        try {
            // The arraylist with movies
            ArrayList<Prayer> prayers = new ArrayList<>();

            // Fetching the array of movie results
            JSONObject data = jsonObject.getJSONObject("data");

            Log.d("AdhanLoader", "parsePrayers : data " + data.toString());

            JSONObject jsonPrayersTiming = data.getJSONObject("timings");

            Log.d("AdhanLoader", "parsePrayers : jsonPrayersTiming " + jsonPrayersTiming.toString());

            // Looping through and creating the Movie objects
            for (String namePrayer :  enumPrayer) {
                Prayer prayer = new Prayer(namePrayer, jsonPrayersTiming.getString(namePrayer));
                prayers.add(prayer);
            }
            Log.d("AdhanLoader", "parsePrayers : prayers " + prayers.toString());
            Log.d("AdhanLoader", "parsePrayers : stop");
            return prayers;
        } catch (JSONException err) {
            // Error occurred!
            return null;
        }
    }

    public String[] enumPrayer = {"Midnight", "Imsak", "Fajr", "Sunrise", "Dhuhr", "Asr", "Sunset", "Maghrib", "Isha"};
}
