package motherslove.org.immunization;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    EditText numberField, dobField;
    CheckBox language;
    String lang;
    int received = 0;
    MyDBHandler dbHandler;
    AlarmStart alarmStart = new AlarmStart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        numberField = (EditText) findViewById(R.id.numberField);
        dobField = (EditText) findViewById(R.id.dobField);
        language = (CheckBox) findViewById(R.id.language);
        dbHandler = new MyDBHandler(this, null, null, 1);
    }

    public void languageClicked(View view) {
        if (language.isChecked()) {
            lang = "French";
        }
        else {
            lang = "English";
        }
    }

    public void onClickSave(View view) {
        // Handle scenario where user does not click checkbox
        if(lang == null)
            lang = "English";
        Contact contact = new Contact(numberField.getText().toString(), dobField.getText().toString(), lang, received);
        dbHandler.addContact(contact);
        Toast.makeText(this, "Contact added", Toast.LENGTH_LONG).show();
        numberField.setText("");
        dobField.setText("");
    }

    public void onClickDelete(View view) {
        String contactphone = numberField.getText().toString();
        dbHandler.deleteContact(contactphone);
        Toast.makeText(this, "Contact " + contactphone + " deleted", Toast.LENGTH_LONG).show();
    }

    public void onClickLaunch(View view) {
        // Start check
        alarmStart.instantCheck(this);

        // Enable receiver when device boots
        ComponentName receiver = new ComponentName(this, BootReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void onClickCheckBalance(View view) {
        String url = "http://api.wasamundi.com/v2/texto/balance?user=motherslove&api_key=nKjahG4T";
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
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
