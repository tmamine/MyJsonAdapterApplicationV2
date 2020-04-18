package fr.exemple.myjsonadapterapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import fr.exemple.myjsonadapterapplication.adapter.PrayerRecyclerAdapter;
import fr.exemple.myjsonadapterapplication.loader.AdhanLoader;
import fr.exemple.myjsonadapterapplication.pojo.Prayer;

public class MainActivity extends AppCompatActivity {

    // The loader class
    private AdhanLoader adhanLoader;

    private DatePickerDialog datePickerDialog;
    private BottomAppBar bottomAppBar;
    private CoordinatorLayout coordinatorLayout;

    // The timePicker
    private EditText datePickerEditText;
    private Button datePickerButton;
    private TextView datePickerTextView;

    //FloatingActionButton
    private FloatingActionButton floatingActionButton;

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

        //initlaise floatingActionButton
        floatingActionButton = findViewById(R.id.fab);
        bottomAppBar = findViewById(R.id.bottom_app_bar);
        coordinatorLayout = findViewById(R.id.coordinator_layour);

        //setSupportActionBar(bottomAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // title of the Activity toolbar
        getSupportActionBar().setTitle(DateFormat.format("EEEE dd, MMMM yyyy", Calendar.getInstance().getTime()).toString());

        //Log.d("getTitle", getSupportActionBar().getTitle().toString());

        bottomAppBar.replaceMenu(R.menu.bottom_bar_menu);

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplaySnackBarAboveBar("Downloads", false);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplaySnackBarAboveBar("Please share", true);
            }
        });


        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.search:
                        Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.help:
                        Toast.makeText(getApplicationContext(), "Help", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
                }
                return true;
            }
        });

        //initalise pickers

        /*datePickerEditText.setInputType(InputType.TYPE_NULL);

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
*/
        //        datePickerTextView.setText("Selected Date: " + datePickerEditText.getText());

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
                Log.d("tag", errorMessage);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.calendar:
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
                Toast.makeText(this, "calendar", Toast.LENGTH_SHORT).show();
                // finish() will exit the activity, hence it has only one activity it will close the app.
                //finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.bottom_bar_menu, popup.getMenu());
        popup.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void DisplaySnackBarAboveBar(String message, Boolean setAction) {
        // This is an custom Snackbar to display the message above the BottomAppBar
        int marginSide = 0;
        int marginBottom = 150;
        Snackbar snackbar = Snackbar.make(
                coordinatorLayout,
                message,
                Snackbar.LENGTH_LONG
        );

        if (setAction) {
            snackbar.setAction("Share Now", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "please visit https://www.noobsplanet.com");
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Send to "));
                }
            });
        }

        View snackbarView = snackbar.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackbarView.getLayoutParams();
        params.setMargins(
                params.leftMargin + marginSide,
                params.topMargin,
                params.rightMargin + marginSide,
                params.bottomMargin + marginBottom + 100
        );

        snackbarView.setLayoutParams(params);
        snackbar.show();
    }
}
