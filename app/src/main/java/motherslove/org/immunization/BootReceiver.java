package motherslove.org.immunization;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    AlarmStart alarmStart = new AlarmStart();

    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Start check on boot
            //alarmStart.instantCheck(context);
        }
    }
}
