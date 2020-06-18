package strath.magss.stma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;
import android.app.TimePickerDialog;
import android.widget.TimePicker;

public class EditEventActivity extends AppCompatActivity {

    Button startDateButton;
    Button endDateButton;
    Button startTimeButton;
    Button endTimeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        startDateButton = (Button) findViewById(R.id.startDateButton);
        endDateButton = (Button) findViewById(R.id.endDateButton);
        startTimeButton = (Button) findViewById(R.id.startTimeButton);
        endTimeButton = (Button) findViewById(R.id.endTimeButton);
    }

    public void setStartDate(View v) {
        if(v.getId() == R.id.startDateButton) {
            datePicker(v).show();
        }
    }

    public void setEndDate(View v) {
        if (v.getId() == R.id.endDateButton) {
            datePicker(v).show();
        }
    }

    public DatePickerDialog datePicker(View v){
        final Button b = (Button) v;
        final Calendar c = Calendar.getInstance();
        final int mdate = c.get(Calendar.DAY_OF_MONTH);
        final int mMonth = c.get(Calendar.MONTH);
        final int mYear = c.get(Calendar.YEAR);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateString = String.valueOf(dayOfMonth) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
                if (b.getId() == R.id.startDateButton){
                    startDateButton.setText(dateString);
                }
                else if(b.getId() == R.id.endDateButton){
                    endDateButton.setText(dateString);
                }

            }
        }, mYear, mMonth, mdate);

        return datePickerDialog;
    }

    public void setStartTime(View v) {
        if(v.getId() == R.id.startTimeButton) {
            timePicker(v).show();
        }
    }

    public void setEndTime(View v) {
        if (v.getId() == R.id.endTimeButton) {
            timePicker(v).show();
        }
    }

    public TimePickerDialog timePicker(View v){
        final Button b = (Button) v;
        final Calendar c = Calendar.getInstance();
        final int mHour = c.get(Calendar.HOUR_OF_DAY);
        final int mMin = c.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String timeString = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                if (b.getId() == R.id.startTimeButton){
                    startTimeButton.setText(timeString);
                }
                else if(b.getId() == R.id.endTimeButton){
                    endTimeButton.setText(timeString);
                }
            }
        }, mHour, mMin, false);

        return timePickerDialog;
    }
}
