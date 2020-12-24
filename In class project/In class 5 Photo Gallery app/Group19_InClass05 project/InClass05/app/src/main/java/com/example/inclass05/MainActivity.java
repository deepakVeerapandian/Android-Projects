//Deepak Veerapandian 801100869, Rishi Kumar Gnanasundaram 801101490
package com.example.inclass05;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private Button btn_go;
    private TextView tv_keyWord;
    private ImageView iv_imageDisplay;
    private ImageButton iv_previous;
    private ImageButton iv_next;
    ProgressBar progress;

    Boolean isConnected = false;
    String currentKeyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("InClass05");

        btn_go = findViewById(R.id.btnGo);
        tv_keyWord = findViewById(R.id.txtSearchKeyWord);
        iv_imageDisplay = findViewById(R.id.imgDisplay);
        iv_previous = findViewById(R.id.imgPrevious);
        iv_next = findViewById(R.id.imgNext);
        progress = (ProgressBar)findViewById(R.id.progressBar);

        isConnected = isConnected();
        iv_next.setEnabled(false);
        iv_previous.setEnabled(false);

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected())
                {
                    iv_next.setEnabled(true);
                    iv_previous.setEnabled(true);
                    new GetKeyWordAsync().execute("http://dev.theappsdr.com/apis/photos/keywords.php");
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

    private class GetKeyWordAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(final String keyWord) {
            final String [] listItems = keyWord.split(";");
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Choose item");

            builder.setItems(listItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(isConnected())
                    {
                        currentKeyword = listItems[which];
                        tv_keyWord.setText(currentKeyword);
                        new GetImageURLAsync(iv_imageDisplay, iv_next, iv_previous, progress).execute(currentKeyword);
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String result = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    result = stringBuilder.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }



    public class GetImageURLAsync extends AsyncTask<String, Void, String> {
        ImageView imgView;
        ImageView nextImgBtn;
        ImageView prevImgBtn;
        ProgressBar progress;
        Bitmap bitmap = null;
        int page = 0;
        Context context;


        public GetImageURLAsync(ImageView img, ImageView nextBtn, ImageView prevBtn, ProgressBar prg) {
            imgView = img;
            nextImgBtn = nextBtn;
            prevImgBtn = prevBtn;
            progress = prg;
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String result = null;
            try {
                URL url = new URL("http://dev.theappsdr.com/apis/photos/index.php?keyword=" + strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + ";");
                    }
                    result = stringBuilder.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imgView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String imageUrl) {
            super.onPostExecute(imageUrl);
            final String[] urls = imageUrl.split(";");

            if(urls[0].equals(""))
            {
                Toast.makeText(getApplicationContext(), "No images found", Toast.LENGTH_SHORT).show();
                imgView.setVisibility(View.INVISIBLE);
                iv_next.setEnabled(false);
                iv_previous.setEnabled(false);
            }
            else if(urls.length == 1)
            {
                iv_next.setEnabled(false);
                iv_previous.setEnabled(false);
            }
            else
            {
                if(isConnected())
                {
                    progress.setVisibility(View.VISIBLE);
                    new GetImageAsync(imgView, progress).execute(urls[0]);
                }

                nextImgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isConnected())
                        {
                            if(page < urls.length-1 )
                                page = page + 1;
                            else
                                page = 0;
                            progress.setVisibility(View.VISIBLE);
                            new GetImageAsync(imgView, progress).execute(urls[page]);
                        }
                    }
                });

                prevImgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isConnected())
                        {
                            if(page > 0)
                                page = page - 1;
                            else
                                page = urls.length-1;
                            progress.setVisibility(View.VISIBLE);
                            new GetImageAsync(imgView, progress).execute(urls[page]);
                        }
                    }
                });
            }
        }
    }
}
