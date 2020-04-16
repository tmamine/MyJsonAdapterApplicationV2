package fr.exemple.myjsonadapterapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import fr.exemple.myjsonadapterapplication.adapter.PrayerRecyclerAdapter;
import fr.exemple.myjsonadapterapplication.loader.AdhanLoader;
import fr.exemple.myjsonadapterapplication.pojo.Prayer;

public class MainActivity extends AppCompatActivity {

    // The loader class
    private AdhanLoader adhanLoader;

    // The timePicker
    private DatePickerDialog datePickerDialog;

    private EditText datePickerEditText;
    private Button datePickerButton;
    private TextView datePickerTextView;

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

        //initalise pickers
        datePickerButton = findViewById(R.id.date_picker_btn);
        datePickerEditText = findViewById(R.id.date_picker_et);
        datePickerTextView = findViewById(R.id.date_picker_tv);

        datePickerEditText.setInputType(InputType.TYPE_NULL);

        datePickerEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                datePickerEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        datePickerButton = findViewById(R.id.date_picker_btn);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerTextView.setText("Selected Date: " + datePickerEditText.getText());
            }
        });

        //end initializing pickers
        //--
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
        final ProgressBar loading;
        loading = new ProgressBar(this);
        loading.setMax(1000);

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
