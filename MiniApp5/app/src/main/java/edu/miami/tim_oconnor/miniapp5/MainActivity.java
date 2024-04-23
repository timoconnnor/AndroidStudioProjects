package edu.miami.tim_oconnor.miniapp5;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;
import java.util.concurrent.TimeUnit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private DatePicker datePicker;

    private Calendar baseTime = Calendar.getInstance(); // Initial base time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = findViewById(R.id.timePicker);
        datePicker = findViewById(R.id.datePicker);

        // Initialize clock to current time
        updateClock();

        // Set up listeners for date and time pickers
        datePicker.init(baseTime.get(Calendar.YEAR), baseTime.get(Calendar.MONTH), baseTime.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Update base time when date changes
                        baseTime.set(year, monthOfYear, dayOfMonth);
                        updateClock();
                    }
                });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // Update base time when time changes
                baseTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                baseTime.set(Calendar.MINUTE, minute);
                updateClock();
            }
        });

        // Schedule a task to update the clock every minute
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateClock();
            }
        }, 0, 60000); // Update every minute (60000 milliseconds)
    }

    private void updateClock() {
        // Get the current time
        Calendar currentTime = Calendar.getInstance();

        // Calculate the elapsed time from the base time
        long elapsedTimeInMillis = currentTime.getTimeInMillis() - baseTime.getTimeInMillis();

        // Calculate the elapsed minutes
        long elapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeInMillis);

        // Update the DatePicker and TimePicker
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Update the DatePicker based on the base time
                datePicker.updateDate(
                        baseTime.get(Calendar.YEAR),
                        baseTime.get(Calendar.MONTH),
                        baseTime.get(Calendar.DAY_OF_MONTH)
                );

                // Calculate the new time based on the elapsed time
                Calendar updatedTime = Calendar.getInstance();
                updatedTime.setTimeInMillis(baseTime.getTimeInMillis() + TimeUnit.MINUTES.toMillis(elapsedMinutes));

                // Update the TimePicker based on the new time
                timePicker.setHour(updatedTime.get(Calendar.HOUR_OF_DAY));
                timePicker.setMinute(updatedTime.get(Calendar.MINUTE));
            }
        });
    }
}








