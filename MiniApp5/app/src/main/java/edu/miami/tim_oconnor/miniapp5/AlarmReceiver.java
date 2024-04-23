package edu.miami.tim_oconnor.miniapp5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Perform your periodic task here
        Log.d("AlarmReceiver", "Task executed at: " + Calendar.getInstance().getTime());
    }
}
