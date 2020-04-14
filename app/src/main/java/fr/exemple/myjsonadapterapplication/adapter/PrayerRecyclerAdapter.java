package fr.exemple.myjsonadapterapplication.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.text.SimpleDateFormat;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import fr.exemple.myjsonadapterapplication.R;
import fr.exemple.myjsonadapterapplication.pojo.Prayer;


/**
 * Custom adapter for the RecyclerViews.
 *
 * @author Anders Engen Olsen
 */
public class PrayerRecyclerAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The activity context
    private Context context;
    // The ArrayList of movies in the RecyclerView
    protected ArrayList<Prayer> prayerList;

    private Calendar currentCalendar = Calendar.getInstance();
    private Calendar prayerCalendar;

    /**
     * Constructor.
     *
     * @param context   Activity context
     * @param prayerList List with movies to show
     */
    public PrayerRecyclerAdapter(Context context, ArrayList<Prayer> prayerList) {
        this.context = context;
        this.prayerList = prayerList;
    }

    /**
     * Initiating ViewHolder with layout.
     *
     * @return RecyclerImageViewHolder
     * @see RecyclerListHolder(View)
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_prayer_list_item, parent, false);

        return new RecyclerListHolder(view);
    }

    /**
     * Setting content in Views in the ViewHolder.
     *
     * @param holder   ViewHolder
     * @param position position in adapter
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RecyclerListHolder recyclerListHolder = (RecyclerListHolder) holder;

        Prayer prayer = prayerList.get(position);

        recyclerListHolder.timePrayer.setText(prayer.getTimePrayer());

        recyclerListHolder.namePrayer.setText(prayer.getNamePrayer());
    }

    /**
     * @return int number of objects in adapter.
     */
    @Override
    public int getItemCount() {
        return (null != prayerList ? prayerList.size() : 0);
    }

    /**
     * Method to set a new movie list to be shown
     *
     * @param prayerList movieList to show
     */
    public void setPrayerList(ArrayList<Prayer> prayerList) {
        this.prayerList = prayerList;
        notifyDataSetChanged();
    }

    /**
     * Inner class, ViewHolder for the elements in the RecyclerView
     */
    private class RecyclerListHolder extends RecyclerView.ViewHolder {

        private TextView namePrayer;
        private TextView timePrayer;

        /**
         * @param view Root
         */
        private RecyclerListHolder(View view) {
            super(view);

            namePrayer = view.findViewById(R.id.name_prayer_tv);
            timePrayer = view.findViewById(R.id.time_prayer_tv);
        }
    }
}