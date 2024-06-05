package com.example.weatherapponmidsemester;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

//6.9194626,79.9526246
public class MainActivity extends AppCompatActivity {
    TextView tvLatLng, tvTime, tvAddress, tvWeatherInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLatLng = findViewById(R.id.tvLatLng);
        tvTime = findViewById(R.id.tvTime);
        tvAddress =  findViewById(R.id.tvAddress);
        tvWeatherInfo = findViewById(R.id.tvWeatherInfo);
        double lat = 6.9194626;
        double lng = 79.9526246;
        String openWeatherMapsAPIKeY = "8d295e98f81b4b6742a879b9b9f0447e";
        tvLatLng.setText("Latitude " + lat + " : " + "Longitude " + lng);

        String urlForWeatherAPI = "https://api.openweathermap.org/data/2.5/weather?lat="  + lat + "&lon=" + lng + "&appid=" + openWeatherMapsAPIKeY;
        new ReadJSONFeedTask().execute(urlForWeatherAPI);

        //Getting the reverse geocoded address of the above lat and lng
        /*
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try{
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            String address="";
            if (addresses.size() > 0)
            {
                for(int i=0; i<addresses.get(0).getMaxAddressLineIndex();i++)
                    address += addresses.get(0).getAddressLine(i) + "\n";
            }
            tvAddress.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        /*
        String addressString="";
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = gc.getFromLocation(lat, lng, 1);
            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                    sb.append(address.getAddressLine(i)).append("\n");
                sb.append(address.getLocality()).append("\n");
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());
            }
                addressString = sb.toString();
        } catch (IOException e) {
            addressString = "No specific address available";
        }

        tvAddress.setText(addressString);
        */
        //Current time
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat. format(currentDate);
        tvTime.setText(currentDateTime);
        startCounter();
    }

    public void startCounter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<=1000; i++) {
                    final int valueOfi = i;
                    //---update UI---
                    tvTime.post(new Runnable() {
                        public void run() {
                            //---UI thread for updating---
                            Date currentDate = new Date();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String currentDateTime = dateFormat. format(currentDate);
                            tvTime.setText(currentDateTime);
                        }
                    });
                    //---insert a delay
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }}}).start();
    }

    public String readJSONFeed(String address) {
        URL url = null;
        try {
            url = new URL(address);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream content = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

        } catch (IOException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        } finally {
            urlConnection.disconnect();
        }
        //Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_LONG).show();

        return stringBuilder.toString();
    }

    private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return readJSONFeed(urls[0]);
        }

        protected void onPostExecute(String result) {
            try {
                //JSONArray jsonArray = new JSONArray(result);
                JSONObject jObj = new JSONObject(result);
                //JSONObject sysObj = getObject("sys", jObj);
                //tvWeatherInfo.setText(getString("country", sysObj));
                JSONObject mainObj = getObject("main", jObj);
                Weather weather = new Weather();

                weather.setHumidity(getInt("humidity", mainObj));
                weather.setPressure(getInt("pressure", mainObj));
                weather.setTemp_max(getFloat("temp_max", mainObj));
                weather.setTemp_min(getFloat("temp_min", mainObj));
                weather.setTemp(getFloat("temp", mainObj));

                // Wind
                JSONObject wObj = getObject("wind", jObj);
                weather.setWindSpeed(getFloat("speed", wObj));
                tvWeatherInfo.setText(weather.getWeatherDescription());


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getJSONObject(tagName);
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }
}