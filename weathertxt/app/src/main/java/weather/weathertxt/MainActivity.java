package weather.weathertxt;
import java.io.IOException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends Activity {
    private TextView weatherData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnHit = (Button) findViewById(R.id.btnHit);
        weatherData = (TextView)findViewById(R.id.weatherJsonItem);
        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first =
                        "http://api.wunderground.com/api/0375e5a05ebf577c/conditions/q/";
                String lat = "39.183609";
                String lon = "-96.571671";
                String last = ".json";

                String weatherQuery = first + lat + "," + lon + last;

                new JSONTask().execute(weatherQuery);
            }
        });
    }


    public class JSONTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {


                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONObject current_observation = parentObject.getJSONObject("current_observation");
                String precip_1hr_in = current_observation.getString("precip_1hr_in");
                String precip_today_in = current_observation.getString("precip_today_in");
                String temperature_f = current_observation.getString("temp_f");


                return "precip in 1h: " + precip_1hr_in + "in \npercip today: " + precip_today_in  + "in \ntemperature_f: " + temperature_f;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            weatherData.setText(result);
        }
    }
}

