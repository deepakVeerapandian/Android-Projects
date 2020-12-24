package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    ArrayList<News> result = new ArrayList<>();
    private ListView newListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        newListView = findViewById(R.id.news_list);
        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey("NEW_MOVIE_ID")) {
                if(isConnected()){

                    String encodedUrl = "https://newsapi.org/v2/top-headlines?sources="+getIntent().getExtras().getString("NEW_MOVIE_ID")+"&apiKey=39bf10eb7a1a41ca9b7e36cbfbe6e3ec";
                    System.out.println("=====encodedurl====="+encodedUrl);
                    new GetKeywordsTask().execute(encodedUrl);

                }else{
                    Toast.makeText(getApplicationContext(),"Make sure your are connected to internet",Toast.LENGTH_SHORT).show();
                }

            }
        }


    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    private class GetKeywordsTask extends AsyncTask<String,Void, ArrayList<News>> {

        @Override
        protected void onPostExecute(ArrayList<News> news) {
            super.onPostExecute(news);
            NewsAdapter newsAdapter = new NewsAdapter(getBaseContext(),R.layout.news_item,result);
            newListView.setAdapter(newsAdapter);
            newListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    System.out.println("========selectedentry======="+result.get(i));
                    Intent webActivityintent = new Intent(NewsActivity.this,WebviewActivity.class);
                    webActivityintent.putExtra("NEWS_IMAGE_URL", result.get(i).getUrl());
                    startActivityForResult(webActivityintent,100);
                }
            });
        }

        @Override
        protected ArrayList<News> doInBackground(String... params) {
            HttpURLConnection connection = null;
            Log.d("demo", params[0]);
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONArray newsData = root.getJSONArray("articles");
                    for (int i=0;i<newsData.length();i++) {
                        JSONObject newsJson = newsData.getJSONObject(i);
                        News news = new News();
                        if(!newsJson.getString("author").equals("null") ) {
                            news.author = newsJson.getString("author");
                        }else {
                            news.author = "";
                        }

                        if(!newsJson.getString("title").equals("null") ) {
                            news.title = newsJson.getString("title");
                        }else {
                            news.title = "";
                        }
                        if(!newsJson.getString("url").equals("null") ) {
                            news.url = newsJson.getString("url");
                        }else {
                            news.url = "";
                        }
                        if(newsJson.getString("urlToImage") != null ) {
                            news.urlToImage = newsJson.getString("urlToImage");
                        }else {
                            news.urlToImage = "";
                        }
                        if(!newsJson.getString("publishedAt").equals("null") ) {
                            news.publishedAt = newsJson.getString("publishedAt");
                        }else {
                            news.publishedAt = "";
                        }
                        result.add(news);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                //Close the connections
            }
            return result;        }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
