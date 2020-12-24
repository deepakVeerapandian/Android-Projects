package com.example.recyclerview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
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

public class MainActivity extends AppCompatActivity {
    private EditText et_song;
    private TextView tv_limit;
    private SeekBar seekBar;
    private Button btn_search;
    private RadioGroup rg_rating;
    //    private RadioButton rbtn_track;
//    private RadioButton rbtn_artist;
    private ListView lv_sources;
    private ProgressBar prg;
    private ProgressDialog pd;

    Boolean ratingArtist = false;
    int limit = 0;
    Integer MIN = 5;
    Integer MAX = 25;
    Integer STEP = 1;
    //    Integer seekvalue;
    Integer limit_value;
    String Url = "";
    ArrayList<Songs> sourcesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MusixMatch Track Search");

        et_song = findViewById(R.id.editTextSongName);
        tv_limit  = findViewById(R.id.txtLimit);
        seekBar = findViewById(R.id.seekBar);
        btn_search = findViewById(R.id.btnSearch);
        rg_rating = findViewById(R.id.rdgGrpRating);
        lv_sources = findViewById(R.id.songListView);
        prg = findViewById(R.id.progressBar);
        pd = new ProgressDialog(MainActivity.this);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createList();
            }
        });

        rg_rating.check(R.id.radiobtnTrack);
        rg_rating.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.radiobtnArtist:
                        ratingArtist = false;
                        createList();
                        break;
                    case R.id.radiobtnTrack:
                        ratingArtist = true;
                        createList();
                        break;
                    default:
                        ratingArtist = false;
                        break;
                }
            }
        });

        limit_value = (MAX - MIN) / STEP;
        seekBar.setMax(limit_value);
        tv_limit.setText("Limit: 5");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                limit_value =  MIN + (progress * STEP);
                tv_limit.setText("Limit: " + (limit_value.toString()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        lv_sources.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = sourcesList.get(position).trackshareURL.toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    private void createList(){
        if(isConnected())
        {
            String rating = (ratingArtist)? "s_artist_rating" : "s_track_rating";
            String song = et_song.getText().toString();
            if(!song.equals(""))
            {
                Url = "http://api.musixmatch.com/ws/1.1/track.search?q="+ song +"&page_size=" + limit_value +"&"+ rating + "=desc&apikey=e060dc62768b49b5e91977ca9f3c083e";
                new GetJSONParserAsync().execute(Url);
            }
            else{
                Toast.makeText(MainActivity.this, "Enter a song", Toast.LENGTH_SHORT).show();
            }

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

    private class GetJSONParserAsync extends AsyncTask<String, Void, ArrayList<Songs>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            prg.setVisibility(View.VISIBLE);

            pd.setMessage("Loading Sources...");
            pd.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Songs> sources) {
            super.onPostExecute(sources);
            sourcesList = sources;
            SourceAdapter adapter = new SourceAdapter(getBaseContext(),R.layout.music_list, sourcesList);
            lv_sources.setAdapter(adapter);
            if(sources.size() == 0)
                Toast.makeText(MainActivity.this, "No source Found", Toast.LENGTH_SHORT).show();
//            prg.setVisibility(View.INVISIBLE);

            pd.dismiss();
        }

        @Override
        protected ArrayList<Songs> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            ArrayList<Songs> result = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONObject message = root.getJSONObject("message");
                    JSONObject body = message.getJSONObject("body");
                    JSONArray trackList = body.getJSONArray("track_list");
                    Log.d("demo...", trackList+"");

                    if(trackList.length() !=0) {

                        for (int i = 0; i < trackList.length(); i++) {
                            JSONObject track = trackList.getJSONObject(i).getJSONObject("track");
                            Songs source = new Songs();
                            source.albumName = track.getString("album_name");
                            source.artistName = track.getString("artist_name");
                            source.trackName = track.getString("track_name");
                            source.trackshareURL = track.getString("track_share_url");
//                            source.updatedTime = track.getString("updated_time");

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                            try {
                                Date date = format.parse(track.getString("updated_time"));
                                String required_format  = (String) DateFormat.format("MM-dd-yyyy", date);
                                source.updatedTime = required_format;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            result.add(source);
                        }
                    }
                    else{
//                        Toast.makeText(MainActivity.this, "No source Found", Toast.LENGTH_SHORT).show();
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
