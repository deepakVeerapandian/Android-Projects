package com.example.inclass06;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Person;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView tv_category;
    private TextView tv_title;
    private TextView tv_date;
    private TextView tv_description;
    private TextView tv_pageNumber;
    private ImageView iv_imageDisplay;
    private ImageView iv_next;
    private ImageView iv_previous;
    private Button btn_select;
    private ProgressBar prg;

    Boolean isConnected = false;
    ArrayList<Articles> newsList = new ArrayList<>();
    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("InClass 06");

        tv_category = findViewById(R.id.txtShowCategories);
        tv_title = findViewById(R.id.txtTitle);
        tv_date = findViewById(R.id.txtPublishedAt);
        tv_description = findViewById(R.id.txtDescription);
        tv_pageNumber = findViewById(R.id.txtPageNumber);
        iv_imageDisplay = findViewById(R.id.imgDisplay);
        iv_next = findViewById(R.id.imgNext);
        iv_previous = findViewById(R.id.imgPrevious);
        btn_select = findViewById(R.id.btnShowCategory);
        prg = findViewById(R.id.progressBar);

        isConnected = isConnected();
        tv_title.setVisibility(View.INVISIBLE);
        tv_date.setVisibility(View.INVISIBLE);
        tv_description.setVisibility(View.INVISIBLE);
        tv_pageNumber.setVisibility(View.INVISIBLE);
        iv_imageDisplay.setVisibility(View.INVISIBLE);
        iv_previous.setVisibility(View.INVISIBLE);
        iv_next.setVisibility(View.INVISIBLE);

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected())
                {
                    tv_title.setVisibility(View.INVISIBLE);
                    tv_date.setVisibility(View.INVISIBLE);
                    tv_description.setVisibility(View.INVISIBLE);
                    tv_pageNumber.setVisibility(View.INVISIBLE);
                    iv_imageDisplay.setVisibility(View.INVISIBLE);
                    iv_previous.setVisibility(View.INVISIBLE);
                    iv_next.setVisibility(View.INVISIBLE);

                    page = 0;
                    final String [] category = {"business","entertainment", "general", "health", "science", "sports", "technology"};
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Choose Category");

                    builder.setItems(category, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(isConnected())
                            {
                                tv_category.setText(category[which]);
                                String Url = "https://newsapi.org/v2/top-headlines?category=" + category[which] + "&apiKey=5c2051acb77f4eefadbace3229f9a145";
                                new GetJSONParserAsync().execute(Url);
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected())
                {
                    if(page < newsList.size()-1 )
                        page = page + 1;
                    else
                        page = 0;
                    setdetails(page);
                }
            }
        });

        iv_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected())
                {
                    if(page > 0)
                        page = page - 1;
                    else
                        page = newsList.size()-1;
                    setdetails(page);
                }
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

    public void setdetails(int i){
        tv_title.setVisibility(View.VISIBLE);
        tv_date.setVisibility(View.VISIBLE);
        tv_description.setVisibility(View.VISIBLE);
        tv_pageNumber.setVisibility(View.VISIBLE);
        iv_imageDisplay.setVisibility(View.VISIBLE);
        iv_previous.setVisibility(View.VISIBLE);
        iv_next.setVisibility(View.VISIBLE);

        tv_title.setText(newsList.get(i).title);
        tv_date.setText(newsList.get(i).date);
        tv_description.setText(newsList.get(i).description);
        Picasso.get().load(newsList.get(i).imageURL).into(iv_imageDisplay);
        tv_pageNumber.setText((i+1)+" Out of "+newsList.size());

        tv_description.setMovementMethod(new ScrollingMovementMethod());
        tv_title.setMovementMethod(new ScrollingMovementMethod());
    }

    private class GetJSONParserAsync extends AsyncTask<String, Void, ArrayList<Articles> >{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prg.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<Articles> articles) {
            super.onPostExecute(articles);
            newsList = articles;
            setdetails(0);
            prg.setVisibility(View.INVISIBLE);
        }

        @Override
        protected ArrayList<Articles> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            ArrayList<Articles> result = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONArray news = root.getJSONArray("articles");
                    
                    if(news.length() !=0) {

                        for (int i = 0; i < 20; i++) {
                            JSONObject newsJson = news.getJSONObject(i);
                            Articles articles = new Articles();
                            articles.title = newsJson.getString("title");
                            articles.date = newsJson.getString("publishedAt");
                            articles.description = newsJson.getString("description");
                            articles.imageURL = newsJson.getString("urlToImage");

                            result.add(articles);
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "No News Found", Toast.LENGTH_SHORT).show();
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
