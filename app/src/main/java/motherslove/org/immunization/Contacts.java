package motherslove.org.immunization;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
//import android.view.View;
import android.widget.TextView;

import java.util.List;

public class Contacts extends AppCompatActivity {

    TextView contacts;
    String dbString = "";
    MyDBHandler dbHandler;
    List<Contact> dbContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        contacts = (TextView) findViewById(R.id.contacts);
        dbHandler = new MyDBHandler(this, null, null, 1);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                /*int count = dbHandler.getContactsCount();
                for(int i = 1; i <= count; i++) {
                    contact = dbHandler.getContact(i);
                    if(contact.getContactphone() != null && contact.getContactlmp() != null) {
                        dbString += i + "\t" + contact.getContactphone() + "\t" + contact.getContactlmp() + "\n";
                    }
                    else {
                        dbString += "";
                    }
                }
                contacts.setText(dbString);*/
                int i = 1;
                dbContacts = dbHandler.getAllContacts();
                for(Contact cn : dbContacts) {
                    dbString += i + "    " + cn.getContactphone() + "    " + cn.getContactdob() + "\n";
                    i++;
                }
                contacts.setText(dbString);
            }
        };

        Runnable r = new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        Thread thread = new Thread(r);
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.go_to_home) {
            Intent i = new Intent(this, Home.class);
            startActivity(i);
        }
        if (id == R.id.go_to_contacts) {
            Intent i = new Intent(this, Contacts.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}
