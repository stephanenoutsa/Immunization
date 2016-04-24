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
    int received;

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
                        received = cn.getContactreceived();
                        String lang = cn.getContactlanguage();

                        // Welcome message
                        if(diff < 366) {
                            if (received == 0) {
                                String smsBody;
                                if (lang == "English") {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.welcome_msg_en), "UTF-8");
                                }
                                else {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.welcome_msg_fr), "UTF-8");
                                }

                                Toast.makeText(cont, getResources().getString(R.string.welcome_msg_en), Toast.LENGTH_LONG).show();

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

                                // Increment received messages count
                                if (diff < 21) {
                                    received = 1;
                                }
                                else if (diff >= 21 && diff < 40) {
                                    received = 2;
                                }
                                else if (diff >= 40 && diff < 70) {
                                    received = 3;
                                }
                                else if (diff >= 70 && diff < 96) {
                                    received = 4;
                                }
                                else if (diff >= 96 && diff < 270) {
                                    received = 5;
                                }
                                else if (diff >= 270 && diff < 365) {
                                    received = 6;
                                }
                                else if (diff == 365) {
                                    received = 7;
                                }
                                else if (diff == 366) {
                                    received = 8;
                                }

                                // Update current contact
                                dbHandler.deleteContact(phone);
                                Contact contact = new Contact(phone, dob, lang, received);
                                dbHandler.addContact(contact);
                            }
                            // If contact receives no message
                            else {
                                Toast.makeText(cont, "Skipping...", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Week one message
                        if(diff >= 7 && diff < 21) {
                            if(received == 1) {
                                String smsBody;
                                if (lang == "English") {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.msg2_en), "UTF-8");
                                }
                                else {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.msg2_fr), "UTF-8");
                                }

                                Toast.makeText(cont, getResources().getString(R.string.msg2_en), Toast.LENGTH_LONG).show();

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

                                // Increment received messages count
                                received++;

                                // Update current contact
                                dbHandler.deleteContact(phone);
                                Contact contact = new Contact(phone, dob, lang, received);
                                dbHandler.addContact(contact);
                            }
                            // If contact receives no message
                            else {
                                Toast.makeText(cont, "Skipping...", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Week three message
                        else if(diff >= 21 && diff < 40) {
                            if(received == 2) {
                                String smsBody;
                                if (lang == "English") {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.msg3_en), "UTF-8");
                                }
                                else {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.msg3_fr), "UTF-8");
                                }

                                Toast.makeText(cont, getResources().getString(R.string.msg3_en), Toast.LENGTH_LONG).show();

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

                                // Increment received messages count
                                received++;

                                // Update current contact
                                dbHandler.deleteContact(phone);
                                Contact contact = new Contact(phone, dob, lang, received);
                                dbHandler.addContact(contact);
                            }
                            // If contact receives no message
                            else {
                                Toast.makeText(cont, "Skipping...", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Week six message
                        else if(diff >= 40 && diff < 70) {
                            if(received == 3) {
                                String smsBody;
                                if (lang == "English") {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.msg4_en), "UTF-8");
                                }
                                else {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.msg4_fr), "UTF-8");
                                }

                                Toast.makeText(cont, getResources().getString(R.string.msg4_en), Toast.LENGTH_LONG).show();

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

                                // Increment received messages count
                                received++;

                                // Update current contact
                                dbHandler.deleteContact(phone);
                                Contact contact = new Contact(phone, dob, lang, received);
                                dbHandler.addContact(contact);
                            }
                            // If contact receives no message
                            else {
                                Toast.makeText(cont, "Skipping...", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Week ten message
                        else if(diff >= 70 && diff < 96) {
                            if(received == 4) {
                                String smsBody;
                                if (lang == "English") {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.msg5_en), "UTF-8");
                                }
                                else {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.msg5_fr), "UTF-8");
                                }

                                Toast.makeText(cont, getResources().getString(R.string.msg5_en), Toast.LENGTH_LONG).show();

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

                                // Increment received messages count
                                received++;

                                // Update current contact
                                dbHandler.deleteContact(phone);
                                Contact contact = new Contact(phone, dob, lang, received);
                                dbHandler.addContact(contact);
                            }
                            // If contact receives no message
                            else {
                                Toast.makeText(cont, "Skipping...", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Week fourteen message
                        else if(diff >= 96 && diff < 270) {
                            if(received == 5) {
                                String smsBody;
                                if (lang == "English") {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.msg6_en), "UTF-8");
                                }
                                else {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.msg6_fr), "UTF-8");
                                }

                                Toast.makeText(cont, getResources().getString(R.string.msg6_en), Toast.LENGTH_LONG).show();

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

                                // Increment received messages count
                                received++;

                                // Update current contact
                                dbHandler.deleteContact(phone);
                                Contact contact = new Contact(phone, dob, lang, received);
                                dbHandler.addContact(contact);
                            }
                            // If contact receives no message
                            else {
                                Toast.makeText(cont, "Skipping...", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Month nine message
                        else if(diff >= 270 && diff < 365) {
                            if(received == 6) {
                                String smsBody;
                                if (lang == "English") {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.msg7_en), "UTF-8");
                                }
                                else {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.msg7_fr), "UTF-8");
                                }

                                Toast.makeText(cont, getResources().getString(R.string.msg7_en), Toast.LENGTH_LONG).show();

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

                                // Increment received messages count
                                received++;

                                // Update current contact
                                dbHandler.deleteContact(phone);
                                Contact contact = new Contact(phone, dob, lang, received);
                                dbHandler.addContact(contact);
                            }
                            // If contact receives no message
                            else {
                                Toast.makeText(cont, "Skipping...", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Year one message
                        else if(diff == 365) {
                            if(received == 7) {
                                String smsBody;
                                if (lang == "English") {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.msg8_en), "UTF-8");
                                }
                                else {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.msg8_fr), "UTF-8");
                                }

                                Toast.makeText(cont, getResources().getString(R.string.msg8_en), Toast.LENGTH_LONG).show();

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

                                // Increment received messages count
                                received++;

                                // Update current contact
                                dbHandler.deleteContact(phone);
                                Contact contact = new Contact(phone, dob, lang, received);
                                dbHandler.addContact(contact);
                            }
                            // If contact receives no message
                            else {
                                Toast.makeText(cont, "Skipping...", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Conclusion message
                        else if(diff == 366) {
                            if(received == 8) {
                                String smsBody;
                                if (lang == "English") {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.msg9_en), "UTF-8");
                                }
                                else {
                                    smsBody = URLEncoder.encode(getResources().getString(R.string.msg9_fr), "UTF-8");
                                }

                                Toast.makeText(cont, getResources().getString(R.string.msg9_en), Toast.LENGTH_LONG).show();

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

                                // Increment received messages count
                                received++;

                                // Update current contact
                                dbHandler.deleteContact(phone);
                                Contact contact = new Contact(phone, dob, lang, received);
                                dbHandler.addContact(contact);
                            }
                            // If contact receives no message
                            else {
                                Toast.makeText(cont, "Skipping...", Toast.LENGTH_SHORT).show();
                            }
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
