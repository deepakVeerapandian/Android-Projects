package com.example.a801100869_midterm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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

public class ForecastActivity extends AppCompatActivity {

    private TextView tv_heading;
    private RecyclerView rv_list;

    ArrayList<Forecast> sourcesList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        setTitle("Weather Forecast");

        tv_heading = findViewById(R.id.txtheadingForecast);
        rv_list = findViewById(R.id.recylerView);

        final Cities source = (Cities) getIntent().getSerializableExtra(CurrentWeatherActivity.TAG_SELECTED_SOURCE_1);
        tv_heading.setText(source.city_name+","+source.country_name);

        if(isConnected()) {
            String Url = "https://samples.openweathermap.org/data/2.5/forecast?q="+source.city_name+","+source.country_name+"&appid=b6907d289e10d714a6e88b30761fae22";
            new ForecastActivity.GetJSONParserAsync().execute(Url);
        }
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

    private class GetJSONParserAsync extends AsyncTask<String, Void, ArrayList<Forecast>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Forecast> sources) {
            super.onPostExecute(sources);
            sourcesList = sources;

            rv_list.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getBaseContext());
            rv_list.setLayoutManager(layoutManager);
            mAdapter = new MyAdapter(sourcesList);
            rv_list.setAdapter(mAdapter);

//            SourceAdapter adapter = new SourceAdapter(getBaseContext(),R.layout.music_list, sourcesList);
//            lv_sources.setAdapter(adapter);
            if(sources.size() == 0)
                Toast.makeText(ForecastActivity.this, "No source Found", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected ArrayList<Forecast> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            ArrayList<Forecast> result = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONArray trackList = root.getJSONArray("list");

                    if(trackList.length() !=0) {

                        for (int i = 0; i < trackList.length(); i++) {
                            JSONObject track = trackList.getJSONObject(i);
                            Forecast source = new Forecast();

                            JSONObject main = track.getJSONObject("main");
                            source.temp = main.getString("temp");
                            source.humidity = main.getString("humidity");

//                            JSONObject wind = root.getJSONObject("wind");
//                            source.speed = wind.getString("speed");

                            JSONArray weather = track.getJSONArray("weather");
                            JSONObject wae = weather.getJSONObject(0);
                            source.description = wae.getString("description");
                            source.icon = wae.getString("icon");

                            source.dt_txt = track.getString("dt_txt");

                            result.add(source);
                        }
                    }
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
