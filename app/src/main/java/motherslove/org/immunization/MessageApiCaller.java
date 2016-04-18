package motherslove.org.immunization;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MessageApiCaller extends AsyncTask<URL, String, String> {

    Context context;

    public MessageApiCaller(Context mcontext) {
        context = mcontext;
    }

    @Override
    protected String doInBackground(URL... urls) {
        //Toast.makeText(context, "In AsyncTask", Toast.LENGTH_SHORT).show();
        int count = urls.length;
        for (int i = 0; i < count; i++) {
            try {
                URL url = urls[i];
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200) {
                    //Toast.makeText(context, "Status code is 200", Toast.LENGTH_SHORT).show();
                    InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader read = new InputStreamReader(it);
                    BufferedReader buff = new BufferedReader(read);
                    StringBuilder dta = new StringBuilder();
                    String chunks ;
                    while((chunks = buff.readLine()) != null)
                    {
                        dta.append(chunks);
                    }
                }
                else {
                    //Toast.makeText(context, "General failure", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        Toast.makeText(context, "Sending message", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context, "Message sent", Toast.LENGTH_SHORT).show();
    }
}
