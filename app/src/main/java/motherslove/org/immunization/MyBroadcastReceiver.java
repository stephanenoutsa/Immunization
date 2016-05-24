package motherslove.org.immunization;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    AlarmStart alarmStart = new AlarmStart();

    public MyBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "In broadcast receiver", Toast.LENGTH_SHORT).show();

        Intent service = new Intent(context, MyService.class);
        context.startService(service);

        //Schedule next alarm
        //alarmStart.startAlarm(context); // Needs debugging as it might simulate sending even when data connection is off
    }
}
