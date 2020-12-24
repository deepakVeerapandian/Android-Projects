package com.example.a801100869_midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CurrentWeatherActivity extends AppCompatActivity {

    private TextView tv_heading;
    private TextView tv_temp;
    private TextView tv_tempmax;
    private TextView tv_tempmin;
    private TextView tv_desc;
    private TextView tv_humidity;
    private TextView tv_windspeed;
    private Button btn_forcast;
    private ImageView img_currentWea;

    ArrayList<CurrentWeather> sourceList;
    public static String TAG_SELECTED_SOURCE_1 = "TAG_SELECTED_SOURCE_1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_weather);
        setTitle("Current Weather");

        tv_temp = findViewById(R.id.txtValueTemp);
        tv_tempmax = findViewById(R.id.txtValueTempMax);
        tv_tempmin = findViewById(R.id.txtValueTempMin);
        tv_heading = findViewById(R.id.txtCurrentWeaHeading);
        tv_desc = findViewById(R.id.txtValueDesc);
        tv_humidity = findViewById(R.id.txtValueHumidity);
        tv_windspeed = findViewById(R.id.txtValueWind);
        img_currentWea = findViewById(R.id.imgCW);
        btn_forcast = findViewById(R.id.buttonForecast);

        final Cities source = (Cities) getIntent().getSerializableExtra(MainActivity.TAG_SELECTED_SOURCE);
        tv_heading.setText(source.city_name+","+source.country_name);

        if(isConnected()) {
            String Url = "https://samples.openweathermap.org/data/2.5/weather?q="+source.city_name+","+source.country_name+"&appid=b6907d289e10d714a6e88b30761fae22";
            new GetJSONParserAsync().execute(Url);
        }

        btn_forcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentWeatherActivity.this, ForecastActivity.class);
                intent.putExtra(TAG_SELECTED_SOURCE_1, source);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            Toast.makeText(getApplicationContext(), "No Internet Connected", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private class GetJSONParserAsync extends AsyncTask<String, Void, ArrayList<CurrentWeather>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<CurrentWeather> sources) {
            super.onPostExecute(sources);
            sourceList = sources;

            tv_temp.setText(sources.get(0).temp + " F");
            tv_tempmax.setText(sources.get(0).temp_max + " F");
            tv_tempmin.setText(sources.get(0).temp_min + " F");
            tv_humidity.setText(sources.get(0).humidity + " %");
            tv_windspeed.setText(sources.get(0).speed + " Miles/hr");
            tv_desc.setText(sources.get(0).description);
//            http://openweathermap.org/img/wn/10d@2x.png
            Picasso.get().load("http://openweathermap.org/img/wn/"+sources.get(0).icon+"@2x.png").into(img_currentWea);

            if(sources.size() == 0)
                Toast.makeText(CurrentWeatherActivity.this, "No source Found", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected ArrayList<CurrentWeather> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            ArrayList<CurrentWeather> result = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);

                    JSONObject main = root.getJSONObject("main");
                    CurrentWeather source = new CurrentWeather();
                    source.temp = main.getString("temp");
                    source.temp_min = main.getString("temp_min");
                    source.temp_max = main.getString("temp_max");
                    source.humidity = main.getString("humidity");

                    JSONObject wind = root.getJSONObject("wind");
                    source.speed = wind.getString("speed");

                    JSONArray weather = root.getJSONArray("weather");
                    JSONObject wae = weather.getJSONObject(0);
                    source.description = wae.getString("description");
                    source.icon = wae.getString("icon");

                    result.add(source);
                }
        } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

}
