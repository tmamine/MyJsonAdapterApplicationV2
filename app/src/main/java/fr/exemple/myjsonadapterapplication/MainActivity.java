package fr.exemple.myjsonadapterapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import fr.exemple.myjsonadapterapplication.adapter.PrayerRecyclerAdapter;
import fr.exemple.myjsonadapterapplication.loader.AdhanLoader;
import fr.exemple.myjsonadapterapplication.pojo.Prayer;

public class MainActivity extends AppCompatActivity {

    // The loader class
    private AdhanLoader adhanLoader;

    // The RecyclerView
    private RecyclerView prayerRecyclerView;

    // Our custom adapter for the RecyclerView
    private PrayerRecyclerAdapter prayerRecyclerAdapter;

    // The list downloaded from the API
    private ArrayList<Prayer> prayerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "onCreate : start");
        // Instantiating the loader and the list
        adhanLoader = AdhanLoader.getInstance(this);
        prayerList = new ArrayList<>();

        setUpRecycler();
        downloadPrayers();
        Log.d("MainActivity", "onCreate : stop");
    }

    /**
     * Setting up the recyclerview
     */
    private void setUpRecycler() {
        Log.d("MainActivity", "setUpRecycler : start");
        // Connecting the recyclerview to the view in the layout
        prayerRecyclerView = findViewById(R.id.recycler_main_movie);

        // Creating our custom adapter
        prayerRecyclerAdapter  = new PrayerRecyclerAdapter(this, prayerList);
        Log.d("MainActivity", "setUpRecycler : prayerList = " + prayerList.size());
        // Setting the adapter to our recyclerview
        prayerRecyclerView.setAdapter(prayerRecyclerAdapter);

        // Creating and setting a layout manager.
        // Note that the manager is VERTICAL, thus a vertical list
        LinearLayoutManager layout = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        prayerRecyclerView.setLayoutManager(layout);
        Log.d("MainActivity", "setUpRecycler : stop");
    }

    /**
     * Downloading prayers, and notifying the adapter when the list is downloaded.
     */
    private void downloadPrayers() {
        adhanLoader.getPrayers(new AdhanLoader.AdhanListener<ArrayList<Prayer>>() {
            @Override
            public void onPrayerDownloaded(ArrayList<Prayer> result) {
                prayerList = result;

                Log.d("MainActivity", "downloadPrayers : result "+result.size());
                Log.d("MainActivity", "downloadPrayers : result "+result.toString());
                // Setting the list to the adapter.
                // This will cause the list to be presented in the layout!
                prayerRecyclerAdapter.setPrayerList(result);
            }

            @Override
            public void onErrorDownloading(String errorMessage) {
                Log.d("tag", errorMessage.toString());
            }
        });

    }
}
