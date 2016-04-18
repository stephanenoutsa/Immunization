package motherslove.org.immunization;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
//import android.widget.Toast;

public class AlarmStart {

    public AlarmStart() {
    }

    public void startAlarm(Context context) {
        if(Build.VERSION.SDK_INT < 19) {
            //Toast.makeText(context, "os.Build < 19", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(context, MyBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + (1000 * 60 * 60 * 24),
                    //AlarmManager.INTERVAL_DAY,
                    pendingIntent);
        }
        else {
            //Toast.makeText(context, "os.Build >= 19", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(context, MyBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + (1000 * 60 * 60 * 24),
                    pendingIntent);
        }
    }

    public void instantCheck(Context context) {
        //Toast.makeText(context, "In alarm", Toast.LENGTH_SHORT).show();
        if(Build.VERSION.SDK_INT < 19) {
            //Toast.makeText(context, "os.Build < 19", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(context, MyBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + (1000 * 2),
                    pendingIntent);
        }
        else {
            //Toast.makeText(context, "os.Build >= 19", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(context, MyBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + (1000 * 2),
                    pendingIntent);
        }
    }

}
