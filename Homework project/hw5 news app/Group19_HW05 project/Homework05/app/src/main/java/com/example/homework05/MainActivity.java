//Deepak Veerapandian 801100869, Rishi Kumar Gnanasundaram 801101490
package com.example.homework05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView lv_Sources;
    private ProgressBar prg;

    ArrayList<Source> sourcesList = new ArrayList<>();
    ArrayAdapter<Source> adapter;
    public static String TAG_SELECTED_SOURCE = "TAG_SELECTED_SOURCE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_Sources = findViewById(R.id.listViewSources);
        prg = findViewById(R.id.prgBarMainActivity);

        lv_Sources.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                intent.putExtra(TAG_SELECTED_SOURCE, sourcesList.get(position));
                startActivity(intent);
            }
        });

        if(isConnected())
        {
            String Url = "https://newsapi.org/v2/sources?apiKey=" + "5c2051acb77f4eefadbace3229f9a145";
            new GetJSONParserAsync().execute(Url);
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

    private class GetJSONParserAsync extends AsyncTask<String, Void, ArrayList<Source> > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prg.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<Source> sources) {
            super.onPostExecute(sources);
            sourcesList = sources;
            SourceAdapter adapter = new SourceAdapter(getBaseContext(),R.layout.layout_main_listview, sourcesList);
            lv_Sources.setAdapter(adapter);
            prg.setVisibility(View.INVISIBLE);

        }

        @Override
        protected ArrayList<Source> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            ArrayList<Source> result = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONArray news = root.getJSONArray("sources");

                    if(news.length() !=0) {

                        for (int i = 0; i < news.length(); i++) {
                            JSONObject newsJson = news.getJSONObject(i);
                            Source source = new Source();
                            source.id = (newsJson.getString("id")) != null? newsJson.getString("id"):"";
                            source.name = (newsJson.getString("name")) != null? newsJson.getString("name"):"";
                            result.add(source);
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "No source Found", Toast.LENGTH_SHORT).show();
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
