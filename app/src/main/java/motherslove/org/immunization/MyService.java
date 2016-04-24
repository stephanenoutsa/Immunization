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
                        String smsBody = "";
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
                        //Toast.makeText(cont, lang, Toast.LENGTH_SHORT).show();

                        // Welcome message
                        if(diff < 366) {
                            if (received == 0) {
                                if (lang.equals("English")) {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.welcome_msg_en), "UTF-8");
                                    //Toast.makeText(cont, lang + " is English", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.welcome_msg_fr), "UTF-8");
                                }

                                //Toast.makeText(cont, smsBody, Toast.LENGTH_LONG).show();

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
                                if (diff < 12) {
                                    received = 1;
                                }
                                else if (diff >= 12 && diff < 26) {
                                    received = 2;
                                }
                                else if (diff >= 26 && diff < 39) {
                                    received = 3;
                                }
                                else if (diff >= 39 && diff < 49) {
                                    received = 4;
                                }
                                else if (diff >= 49 && diff < 77) {
                                    received = 5;
                                }
                                else if (diff >= 77 && diff < 186) {
                                    received = 6;
                                }
                                else if (diff >= 186 && diff < 273) {
                                    received = 7;
                                }
                                else if (diff >= 273 && diff < 302) {
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
                        if(diff >= 4 && diff < 12) {
                            if(received == 1) {
                                if (lang.equals("English")) {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.msg2_en), "UTF-8");
                                }
                                else {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.msg2_fr), "UTF-8");
                                }

                                //Toast.makeText(cont, smsBody, Toast.LENGTH_LONG).show();

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
                        else if(diff >= 12 && diff < 26) {
                            if(received == 2) {
                                if (lang.equals("English")) {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.msg3_en), "UTF-8");
                                }
                                else {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.msg3_fr), "UTF-8");
                                }

                                //Toast.makeText(cont, smsBody, Toast.LENGTH_LONG).show();

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
                        else if(diff >= 26 && diff < 39) {
                            if(received == 3) {
                                if (lang.equals("English")) {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.msg4_en), "UTF-8");
                                }
                                else {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.msg4_fr), "UTF-8");
                                }

                                //Toast.makeText(cont, smsBody, Toast.LENGTH_LONG).show();

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
                        else if(diff >= 39 && diff < 49) {
                            if(received == 4) {
                                if (lang.equals("English")) {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.msg5_en), "UTF-8");
                                }
                                else {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.msg5_fr), "UTF-8");
                                }

                                //Toast.makeText(cont, smsBody, Toast.LENGTH_LONG).show();

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
                        else if(diff >= 67 && diff < 77) {
                            if(received == 5) {
                                if (lang.equals("English")) {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.msg6_en), "UTF-8");
                                }
                                else {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.msg6_fr), "UTF-8");
                                }

                                //Toast.makeText(cont, smsBody, Toast.LENGTH_LONG).show();

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

                        // Week 14 message (message 7)
                        else if(diff >= 95 && diff < 105) {
                            if(received == 6) {
                                if (lang.equals("English")) {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.msg7_en), "UTF-8");
                                }
                                else {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.msg7_fr), "UTF-8");
                                }

                                //Toast.makeText(cont, smsBody, Toast.LENGTH_LONG).show();

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

                        // Month 6 message
                        else if(diff >= 168 && diff < 197) {
                            if(received == 7) {
                                if (lang.equals("English")) {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.msg8_en), "UTF-8");
                                }
                                else {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.msg8_fr), "UTF-8");
                                }

                                //Toast.makeText(cont, smsBody, Toast.LENGTH_LONG).show();

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
                        else if(diff >= 273 && diff < 302) {
                            if(received == 8) {
                                if (lang.equals("English")) {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.msg9_en), "UTF-8");
                                }
                                else {
                                    smsBody += URLEncoder.encode(getResources().getString(R.string.msg9_fr), "UTF-8");
                                }

                                //Toast.makeText(cont, smsBody, Toast.LENGTH_LONG).show();

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
