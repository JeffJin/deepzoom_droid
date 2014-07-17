package com.intellibeacons.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class AsyncImageLoadTask extends AsyncTask<String, Integer, Drawable> {


    private Drawable drawableFromUrl(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        Bitmap bitmap = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(bitmap);
    }


    protected Drawable doInBackground(String... urls) {
        try {
            return drawableFromUrl(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(Integer... progress) {
        //Log.d(null, "progress ... " + progress[0].toString());
    }

    protected void onPostExecute(Drawable result) {
        Log.d(null, result.toString());
    }
}