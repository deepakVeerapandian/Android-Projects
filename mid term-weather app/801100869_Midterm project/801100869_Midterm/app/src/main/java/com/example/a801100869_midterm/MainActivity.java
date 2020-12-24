package com.example.a801100869_midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ListView lv_Sources;

    ArrayList<Cities> sourcesList = new ArrayList<>();
    ArrayAdapter<Cities> adapter;
    public static String TAG_SELECTED_SOURCE = "TAG_SELECTED_SOURCE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Select City");

        lv_Sources = findViewById(R.id.listViewMainActivity);

        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.cities);
            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        JSONObject root = null;
        try {
            root = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray data = root.getJSONArray("data");
            if(data.length() !=0) {

                for (int i = 0; i < data.length(); i++) {
                    JSONObject track = data.getJSONObject(i);
                    Cities source = new Cities();
                    source.country_name = track.getString("country");
                    source.city_name = track.getString("city");
                    sourcesList.add(source);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SourceAdapter adapter = new SourceAdapter(getBaseContext(),R.layout.layout_main_listview, sourcesList);
        lv_Sources.setAdapter(adapter);

        lv_Sources.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, CurrentWeatherActivity.class);
                intent.putExtra(TAG_SELECTED_SOURCE, sourcesList.get(position));
                startActivity(intent);
            }
        });
    }


}
