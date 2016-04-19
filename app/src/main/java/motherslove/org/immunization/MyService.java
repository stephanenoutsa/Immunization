package motherslove.org.immunization;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
//import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyService extends Service {

    SimpleDateFormat sdf;
    Date dobDate;
    Date today;
    List<Contact> dbContacts;
    Context context = this;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(context, "Service called", Toast.LENGTH_SHORT).show();

        final MyDBHandler dbHandler = new MyDBHandler(context, null, null, 1);
        final Context cont = context;

        String expectedPattern = "MMM d, yyyy";
        sdf = new SimpleDateFormat(expectedPattern);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    //Toast.makeText(cont, "In service handler", Toast.LENGTH_SHORT).show();
                    dbContacts = dbHandler.getAllContacts();
                    for(Contact cn : dbContacts) {
                        Toast.makeText(cont, "Checking contacts...", Toast.LENGTH_SHORT).show();
                        String url = "http://api.wasamundi.com/v2/texto/send_sms?user=motherslove&api_key=nKjahG4T&from=MothersLove&to=237";
                        //String url = "http://api.wasamundi.com/v2/texto/send_sms?user=humphrey&api_key=7vLIs7YY&from=MothersLove&to=237";
                        final String phone = cn.getContactphone();
                        //Toast.makeText(cont, "Phone number is " + phone, Toast.LENGTH_SHORT).show();
                        String dob = cn.getContactdob();
                        //Toast.makeText(cont, "dob is " + dob, Toast.LENGTH_SHORT).show();
                        dobDate = sdf.parse(dob);
                        today = new Date();
                        long diffInMs = today.getTime() - dobDate.getTime();
                        int diff = (int) diffInMs / (1000 * 60 * 60 * 24);
                        //Toast.makeText(cont, "Diff is " + diff, Toast.LENGTH_SHORT).show();

                        // Message at birth
                        if(diff == 0) {
                            String smsBody = URLEncoder.encode("Welcome message + ORS formula", "UTF-8");

                            /*String SMS_SENT = "SMS_SENT";
                            String SMS_DELIVERED = "SMS_DELIVERED";

                            PendingIntent sentPendingIntent = PendingIntent.getBroadcast(cont, 0, new Intent(SMS_SENT), 0);
                            PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(cont, 0, new Intent(SMS_DELIVERED), 0);

                            // When SMS has been sent
                            cont.registerReceiver(new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context cont, Intent intent) {
                                    switch (getResultCode()) {
                                        case Activity.RESULT_OK:
                                            Toast.makeText(cont, "SMS sent successfully to " + phone, Toast.LENGTH_SHORT).show();
                                            break;

                                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                            Toast.makeText(cont, "Generic failure cause", Toast.LENGTH_SHORT).show();
                                            break;
                                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                                            Toast.makeText(cont, "Service is currently unavailable", Toast.LENGTH_SHORT).show();
                                            break;
                                        case SmsManager.RESULT_ERROR_NULL_PDU:
                                            Toast.makeText(cont, "No pdu provided", Toast.LENGTH_SHORT).show();
                                            break;
                                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                                            Toast.makeText(cont, "Radio was explicitly turned off", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                            }, new IntentFilter(SMS_SENT));

                            // When SMS has been delivered
                            cont.registerReceiver(new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context cont, Intent intent) {
                                    switch (getResultCode()) {
                                        case Activity.RESULT_OK:
                                            Toast.makeText(cont, "SMS delivered", Toast.LENGTH_SHORT).show();
                                            break;
                                        case Activity.RESULT_CANCELED:
                                            Toast.makeText(cont, "SMS not delivered", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                            }, new IntentFilter(SMS_DELIVERED));

                            // Get default instance of SmsManager
                            SmsManager smsManager = SmsManager.getDefault();

                            // Send a text-based SMS
                            //smsManager.sendTextMessage(phone, null, smsBody, sentPendingIntent, deliveredPendingIntent);*/
                            Toast.makeText(cont, "Preparing to call message api", Toast.LENGTH_SHORT).show();
                            url += phone + "&msg=" + smsBody;
                            //Toast.makeText(cont, "URL is " + url, Toast.LENGTH_LONG).show();
                            URL url1 = new URL(url);
                            /*URI uri = new URI(url1.getProtocol(), url1.getUserInfo(), url1.getHost(),
                                    url1.getPort(), url1.getPath(), url1.getQuery(), url1.getRef());
                            url1 = uri.toURL();*/
                            MessageApiCaller messageApiCaller = new MessageApiCaller(cont);
                            messageApiCaller.execute(url1);
                        }

                        // Week one message
                        else if(diff == 7) {
                            String smsBody = URLEncoder.encode("Week one message", "UTF-8");

                            // Send a text-based SMS
                            Toast.makeText(cont, "Preparing to call message api", Toast.LENGTH_SHORT).show();
                            url += phone + "&msg=" + smsBody;
                            //Toast.makeText(cont, "URL is " + url, Toast.LENGTH_LONG).show();
                            URL url1 = new URL(url);
                            /*URI uri = new URI(url1.getProtocol(), url1.getUserInfo(), url1.getHost(),
                                    url1.getPort(), url1.getPath(), url1.getQuery(), url1.getRef());
                            url1 = uri.toURL();*/
                            MessageApiCaller messageApiCaller = new MessageApiCaller(cont);
                            messageApiCaller.execute(url1);
                        }

                        // Week three message
                        else if(diff == 21) {
                            String smsBody = URLEncoder.encode("Week three message", "UTF-8");

                            // Send a text-based SMS
                            Toast.makeText(cont, "Preparing to call message api", Toast.LENGTH_SHORT).show();
                            url += phone + "&msg=" + smsBody;
                            //Toast.makeText(cont, "URL is " + url, Toast.LENGTH_LONG).show();
                            URL url1 = new URL(url);
                            /*URI uri = new URI(url1.getProtocol(), url1.getUserInfo(), url1.getHost(),
                                    url1.getPort(), url1.getPath(), url1.getQuery(), url1.getRef());
                            url1 = uri.toURL();*/
                            MessageApiCaller messageApiCaller = new MessageApiCaller(cont);
                            messageApiCaller.execute(url1);
                        }

                        // Week six message
                        else if(diff == 40) {
                            String smsBody = URLEncoder.encode("Week six message", "UTF-8");

                            // Send a text-based SMS
                            Toast.makeText(cont, "Preparing to call message api", Toast.LENGTH_SHORT).show();
                            url += phone + "&msg=" + smsBody;
                            //Toast.makeText(cont, "URL is " + url, Toast.LENGTH_LONG).show();
                            URL url1 = new URL(url);
                            /*URI uri = new URI(url1.getProtocol(), url1.getUserInfo(), url1.getHost(),
                                    url1.getPort(), url1.getPath(), url1.getQuery(), url1.getRef());
                            url1 = uri.toURL();*/
                            MessageApiCaller messageApiCaller = new MessageApiCaller(cont);
                            messageApiCaller.execute(url1);
                        }

                        // Week ten message
                        else if(diff == 70) {
                            String smsBody = URLEncoder.encode("Week ten message", "UTF-8");

                            // Send a text-based SMS
                            Toast.makeText(cont, "Preparing to call message api", Toast.LENGTH_SHORT).show();
                            url += phone + "&msg=" + smsBody;
                            //Toast.makeText(cont, "URL is " + url, Toast.LENGTH_LONG).show();
                            URL url1 = new URL(url);
                            /*URI uri = new URI(url1.getProtocol(), url1.getUserInfo(), url1.getHost(),
                                    url1.getPort(), url1.getPath(), url1.getQuery(), url1.getRef());
                            url1 = uri.toURL();*/
                            MessageApiCaller messageApiCaller = new MessageApiCaller(cont);
                            messageApiCaller.execute(url1);
                        }

                        // Week fourteen message
                        else if(diff == 96) {
                            String smsBody = URLEncoder.encode("Week fourteen message", "UTF-8");

                            // Send a text-based SMS
                            Toast.makeText(cont, "Preparing to call message api", Toast.LENGTH_SHORT).show();
                            url += phone + "&msg=" + smsBody;
                            //Toast.makeText(cont, "URL is " + url, Toast.LENGTH_LONG).show();
                            URL url1 = new URL(url);
                            /*URI uri = new URI(url1.getProtocol(), url1.getUserInfo(), url1.getHost(),
                                    url1.getPort(), url1.getPath(), url1.getQuery(), url1.getRef());
                            url1 = uri.toURL();*/
                            MessageApiCaller messageApiCaller = new MessageApiCaller(cont);
                            messageApiCaller.execute(url1);
                        }

                        // Month nine message
                        else if(diff == 270) {
                            String smsBody = URLEncoder.encode("Month nine message", "UTF-8");

                            // Send a text-based SMS
                            Toast.makeText(cont, "Preparing to call message api", Toast.LENGTH_SHORT).show();
                            url += phone + "&msg=" + smsBody;
                            //Toast.makeText(cont, "URL is " + url, Toast.LENGTH_LONG).show();
                            URL url1 = new URL(url);
                            /*URI uri = new URI(url1.getProtocol(), url1.getUserInfo(), url1.getHost(),
                                    url1.getPort(), url1.getPath(), url1.getQuery(), url1.getRef());
                            url1 = uri.toURL();*/
                            MessageApiCaller messageApiCaller = new MessageApiCaller(cont);
                            messageApiCaller.execute(url1);
                        }

                        // Year one message
                        else if(diff == 365) {
                            String smsBody = URLEncoder.encode("Year one message", "UTF-8");

                            // Send a text-based SMS
                            Toast.makeText(cont, "Preparing to call message api", Toast.LENGTH_SHORT).show();
                            url += phone + "&msg=" + smsBody;
                            //Toast.makeText(cont, "URL is " + url, Toast.LENGTH_LONG).show();
                            URL url1 = new URL(url);
                            /*URI uri = new URI(url1.getProtocol(), url1.getUserInfo(), url1.getHost(),
                                    url1.getPort(), url1.getPath(), url1.getQuery(), url1.getRef());
                            url1 = uri.toURL();*/
                            MessageApiCaller messageApiCaller = new MessageApiCaller(cont);
                            messageApiCaller.execute(url1);
                        }

                        // Conclusion message
                        else if(diff == 366) {
                            String smsBody = URLEncoder.encode("Conclusion message", "UTF-8");

                            // Send a text-based SMS
                            Toast.makeText(cont, "Preparing to call message api", Toast.LENGTH_SHORT).show();
                            url += phone + "&msg=" + smsBody;
                            //Toast.makeText(cont, "URL is " + url, Toast.LENGTH_LONG).show();
                            URL url1 = new URL(url);
                            /*URI uri = new URI(url1.getProtocol(), url1.getUserInfo(), url1.getHost(),
                                    url1.getPort(), url1.getPath(), url1.getQuery(), url1.getRef());
                            url1 = uri.toURL();*/
                            MessageApiCaller messageApiCaller = new MessageApiCaller(cont);
                            messageApiCaller.execute(url1);
                        }

                        // If contact receives no message
                        else {
                            Toast.makeText(cont, "Skipping contact...", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable r = new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };

        Thread stephThread = new Thread(r);
        stephThread.start();

        stopSelf();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
