//Deepak Veerapandian 801100869, Rishi Kumar Gnanasundaram 801101490
package com.example.inclass05;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetImageAsync extends AsyncTask<String, Void, Void> {
    ImageView imageView;
    Bitmap bitmap = null;
    ProgressBar progress;

    public GetImageAsync(ImageView iv, ProgressBar prg) {
        imageView = iv;
        progress = prg;
    }

    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection connection = null;
        bitmap = null;
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (bitmap != null && imageView != null) {
            imageView.setImageBitmap(bitmap);
            progress.setVisibility(View.INVISIBLE);
        }
    }
}

