package fr.exemple.myjsonadapterapplication.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.text.MessageFormat;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import fr.exemple.myjsonadapterapplication.R;
import fr.exemple.myjsonadapterapplication.pojo.Prayer;

/**
 * Custom adapter for the RecyclerViews.
 *
 * @author Mohammed Amine TALBAOUI
 */

public class PrayerRecyclerAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The activity context
    private Context context;
    // The ArrayList of movies in the RecyclerView
    protected ArrayList<Prayer> prayerList;

    private Calendar currentCalendar = Calendar.getInstance();


    /**
     * Constructor.
     *
     * @param context Activity context
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

        final Prayer prayer = prayerList.get(position);


        recyclerListHolder.namePrayer.setText(prayer.getNamePrayer());

        if (recyclerListHolder.timer != null) {
            recyclerListHolder.timer.cancel();
        }
        final Long endTime = getDatePrayerMillisec(prayer.getTimePrayer());
        recyclerListHolder.timer = new CountDownTimer(endTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //
                updateTimeRemaining(endTime, recyclerListHolder.timePrayer);
            }

            @Override
            public void onFinish() {
                recyclerListHolder.timePrayer.setText(prayer.getTimePrayer());
            }
        }.start();
    }

    /**
     * @param endTime      the time of prayer in millisecond
     * @param timeTextView textview which it containt the result
     */
    private void updateTimeRemaining(long endTime, TextView timeTextView) {

        long timeDiff = endTime - System.currentTimeMillis();
        if (timeDiff > 0) {
            int seconds = (int) (timeDiff / 1000) % 60;
            int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
            int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);

            timeTextView.setText(MessageFormat.format("{0}:{1}:{2}", hours, minutes, seconds));
        } else {
            timeTextView.setText("Expired!!");
        }
    }

    /**
     * @param timePrayer
     * @return time of a prayer in millisecond
     */
    private Long getDatePrayerMillisec(String timePrayer) {
        currentCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timePrayer.split(":")[0]));
        currentCalendar.set(Calendar.MINUTE, Integer.parseInt(timePrayer.split(":")[1]));
        currentCalendar.set(Calendar.SECOND, 0);

        return currentCalendar.getTimeInMillis();
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
        private CountDownTimer timer;

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