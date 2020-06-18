package strath.magss.stma;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/* View Activity Class */
public class ViewAcivity extends AppCompatActivity {
    private Intent intent = new Intent();
    /* onCreate method for starting activity */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_acivity);

        final Button daysbtn  = findViewById(R.id.daybtn);
        final Button weeksbtn = findViewById(R.id.weekbtn);
        final Button monthbtn = findViewById(R.id.monthbtn);
        final LinearLayout daysView = findViewById(R.id.days_view);
        final LinearLayout weeksView = findViewById(R.id.week_view);
        final LinearLayout monthView = findViewById(R.id.month_view);
        final Button createEvent = findViewById(R.id.createEvent);

        // create new event activity.
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(), EditEventActivity.class);
                startActivity(intent);
            }
        });

        // day, week and month navigation bar
        daysbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daysbtn.setBackgroundColor(getResources().getColor(R.color.white));
                weeksbtn.setBackgroundColor(getResources().getColor(R.color.gray));
                monthbtn.setBackgroundColor(getResources().getColor(R.color.gray));
                daysView.setVisibility(View.VISIBLE);
                weeksView.setVisibility(View.GONE);
                monthView.setVisibility(View.GONE);
            }
        });
        weeksbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weeksbtn.setBackgroundColor(getResources().getColor(R.color.white));
                daysbtn.setBackgroundColor(getResources().getColor(R.color.gray));
                monthbtn.setBackgroundColor(getResources().getColor(R.color.gray));
                weeksView.setVisibility(View.VISIBLE);
                daysView.setVisibility(View.GONE);
                monthView.setVisibility(View.GONE);
            }
        });
        monthbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthbtn.setBackgroundColor(getResources().getColor(R.color.white));
                daysbtn.setBackgroundColor(getResources().getColor(R.color.gray));
                weeksbtn.setBackgroundColor(getResources().getColor(R.color.gray));
                monthView.setVisibility(View.VISIBLE);
                daysView.setVisibility(View.GONE);
                weeksView.setVisibility(View.GONE);
            }
        });

        // date picker dialog
        final EditText s_day = findViewById(R.id.day);
        final EditText s_month = findViewById(R.id.month);
        final EditText s_year = findViewById(R.id.year);

        // touch listeners for day, week and month buttons
        s_day.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v.getId() == R.id.day) {
                    datePicker(v).show();
                }
                return true;
            }
        });
        s_month.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v.getId() == R.id.month) {
                    datePicker(v).show();
                }
                return true;
            }
        });
        s_year.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v.getId() == R.id.year) {
                    datePicker(v).show();
                }
                return true;
            }
        });

        // run message notification activity.
        if (Settings.getNotificationStatus()) {
            NewMessageNotification.notify(getApplicationContext(), "an upcoming events", 0);
        }

        // onclick listener for single event view
        final LinearLayout event1 = findViewById(R.id.event1);
        final LinearLayout event2 = findViewById(R.id.event2);
        final LinearLayout event3 = findViewById(R.id.event3);
        final LinearLayout event4 = findViewById(R.id.event4);
        final LinearLayout event5 = findViewById(R.id.event5);

        doSingleEventOnClick(event1);
        doSingleEventOnClick(event2);
        doSingleEventOnClick(event3);
        doSingleEventOnClick(event4);
        doSingleEventOnClick(event5);

        // setting event
        final Button saveBtn = findViewById(R.id.save_settings);
        final Switch turnOnNotify = findViewById(R.id.notify_settings);

        // Application settings
        // set notification status onCreate activity
        turnOnNotify.setChecked(Settings.getNotificationStatus());
        // save settings
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout settingsTab = findViewById(R.id.settings_tab);
                final Switch turnOnNotify = findViewById(R.id.notify_settings);

                if (turnOnNotify.isChecked()){
                    turnOnNotify.setChecked(true);
                    Settings.setNotification(true);
                }
                else {
                    turnOnNotify.setChecked(false);
                    Settings.setNotification(false);
                }
                settingsTab.animate().translationY(-400f).setDuration(1000);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        settingsTab.setVisibility(View.GONE);
                    }
                }, 1000);
                Toast.makeText(getApplicationContext(), "Settings saved.", Toast.LENGTH_LONG).show();
            }
        });

    }

    /* Start search method */
    public  void startSearch(View v){
        final LinearLayout search_event = findViewById(R.id.search_events);
        final LinearLayout dummy_search = findViewById(R.id.dummy_search);
        final LinearLayout spinner = findViewById(R.id.spinner);
        final TextView spinner_icon = spinner.findViewById(R.id.spinner_icon);
        Handler handler = new Handler();

        search_event.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        dummy_search.setVisibility(View.INVISIBLE);
        // animate spinner
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        spinner_icon.startAnimation(animation);
        // delay search result
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                spinner.setVisibility(View.GONE);
                dummy_search.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    /* Close search event method */
    public void closeSearch(View v){
        final LinearLayout search_event = findViewById(R.id.search_events);
        final LinearLayout dummy_search = findViewById(R.id.dummy_search);
        final LinearLayout spinner = findViewById(R.id.spinner);

        search_event.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        dummy_search.setVisibility(View.INVISIBLE);
    }
    /* Show upcoming events method */
    public void showUpcomingEvents(View v){
        final ScrollView upcomingEvents = findViewById(R.id.upcoming_events);
        upcomingEvents.setVisibility(View.VISIBLE);
        upcomingEvents.animate().alpha(1f).setDuration(1000);
    }
    /* Hide upcoming events method */
    public void hideUpcomingEvents(View v){
        final ScrollView upcomingEvents = findViewById(R.id.upcoming_events);
        upcomingEvents.animate().alpha(0f).setDuration(1000);
        upcomingEvents.setVisibility(View.GONE);
    }
    /* Method to display settings */
    public void showSettings(View v){
        final LinearLayout settingsTab = findViewById(R.id.settings_tab);
        settingsTab.setVisibility(View.VISIBLE);
        settingsTab.animate().translationY(0f).setDuration(1000);
    }
    /* onCreateOptionsMenu method for setting custom action bar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);

        menu.add(Menu.NONE, 0, Menu.NONE, "custom")
                .setActionView(R.layout.notification)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    /* Method to trigger single event view */
    public void doSingleEventOnClick(LinearLayout layout){
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(), DetailsOfActivity.class);
                startActivity(intent);
            }
        });
    }

    /* Date picker method, this returns the date picker dialog */
    public DatePickerDialog datePicker(View v){
        final EditText s_day = findViewById(R.id.day);
        final EditText s_month = findViewById(R.id.month);
        final EditText s_year = findViewById(R.id.year);
        final Calendar c = Calendar.getInstance();
        final int mdate = c.get(Calendar.DAY_OF_MONTH);
        final int mMonth = c.get(Calendar.MONTH);
        final int mYear = c.get(Calendar.YEAR);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                s_day.setText(String.valueOf(dayOfMonth));
                s_month.setText(String.valueOf(month));
                s_year.setText(String.valueOf(year));
            }
        }, mYear, mMonth, mdate);

        return datePickerDialog;
    }
}