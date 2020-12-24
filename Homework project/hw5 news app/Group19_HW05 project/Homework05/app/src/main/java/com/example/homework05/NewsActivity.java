//Deepak Veerapandian 801100869, Rishi Kumar Gnanasundaram 801101490
package com.example.homework05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsActivity extends AppCompatActivity {
    private Handler handler;
    public static String Url = "";

    private ListView lv_news;
    private ProgressBar prg;
    ArrayList<News> newsList;
    public static String TAG_SELECTED_IMAGEURL = "TAG_SELECTED_IMAGEURL";
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        lv_news = findViewById(R.id.newsListView);
        prg = findViewById(R.id.prgBarNewsActivity);

        Source source = (Source) getIntent().getSerializableExtra(MainActivity.TAG_SELECTED_SOURCE);
        setTitle(source.name);

        if(isConnected())
        {
            prg.setVisibility(View.VISIBLE);
            Url = "https://newsapi.org/v2/top-headlines?sources="+ source.id + "&apiKey=5c2051acb77f4eefadbace3229f9a145";
            ExecutorService taskPool = Executors.newFixedThreadPool(2);
            taskPool.execute(new ThreadCall());
        }

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                newsList = (ArrayList<News>) message.getData().getSerializable("list");
                NewsAdapter adapter = new NewsAdapter(getBaseContext(),R.layout.news_list, newsList);
                lv_news.setAdapter(adapter);
                prg.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NewsActivity.this,WebActivity.class);
                intent.putExtra(TAG_SELECTED_IMAGEURL, newsList.get(position).url);
                startActivity(intent);
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

    class ThreadCall implements Runnable {

        void sendMsg(ArrayList<News> outputList)
        {
            Bundle bundle = new Bundle();
            bundle.putSerializable("list", outputList);
            Message msg = new Message();
            msg.setData(bundle);
            handler.sendMessage(msg);
        }

        @Override
        public void run()
        {
            Looper.prepare();
            HttpURLConnection connection = null;
            ArrayList<News> result = new ArrayList<>();
            try {
                URL url = new URL(Url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONArray articles = root.getJSONArray("articles");

                    if(articles.length() > 0) {
                        for (int i = 0; i < articles.length(); i++) {
                            JSONObject newsJson = articles.getJSONObject(i);
                            News news = new News();

                            news.author = (newsJson.getString("author")) != "null"? newsJson.getString("author"):"";
                            news.title = (newsJson.getString("title")) != "null"? newsJson.getString("title"):"";
                            news.publishedAt = (newsJson.getString("publishedAt")) != "null"? newsJson.getString("publishedAt"):"";
                            news.url = (newsJson.getString("url")) != "null"? newsJson.getString("url"):"";
                            news.urlToImage = (newsJson.getString("urlToImage")) != "null"? newsJson.getString("urlToImage"):"";

                            result.add(news);
                        }
                    sendMsg(result);
                    }
                    else{
                        toast.makeText(getApplicationContext(), "No source Found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Looper.loop();
            sendMsg(result);
        }
    }

}
