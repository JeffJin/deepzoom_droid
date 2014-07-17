package com.intellibeacons.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import uk.co.senab.photoview.PhotoView;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnMatrixChangedListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class IndoorMapActivity extends Activity {
    static final String PHOTO_TAP_TOAST_STRING = "Photo Tap! X: %.2f %% Y:%.2f %% ID: %d";
    static final String SCALE_TOAST_STRING = "Scaled to: %.2ff";

    private TextView mCurrMatrixTv;

    private PhotoViewAttacher mAttacher;

    private Toast mCurrentToast;

    private Matrix mCurrentDisplayMatrix = null;

    public Drawable drawableFromUrl(String url) throws IOException {
        AsyncImageLoadTask loader = new AsyncImageLoadTask();
        AsyncTask<String, Integer, Drawable> task = loader.execute(url);

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String url = intent.getStringExtra("mapUrl");
        setContentView(R.layout.activity_indoor_map);

        ImageView mImageView = (ImageView) findViewById(R.id.iv_photo);
        mCurrMatrixTv = (TextView) findViewById(R.id.tv_current_matrix);

        Drawable bitmap = null;
//        try {
//            bitmap = drawableFromUrl(url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        bitmap = getResources().getDrawable(R.drawable.wallpaper);
        mImageView.setImageDrawable(bitmap);

        // The MAGIC happens here!
        mAttacher = new PhotoViewAttacher(mImageView);

        // Lets attach some listeners, not required though!
        mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
        mAttacher.setOnPhotoTapListener(new PhotoTapListener());



        // Any implementation of ImageView can be used!
        //mImageView = (ImageView) findViewById(R.id.image_container);

        // Set the Drawable displayed
//        Drawable bitmap = getResources().getDrawable(R.drawable.wallpaper);
//        mImageView.setImageDrawable(bitmap);
//
//        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
//        mAttacher = new PhotoViewAttacher(mImageView);
//
//
//        PhotoView photoView = new PhotoView(this);
//        photoView.setMaximumScale(16);
//        setContentView(photoView);
//
//        final ProgressDialog dlg = new ProgressDialog(this);
//        dlg.setTitle("Loading...");
//        dlg.setIndeterminate(false);
//        dlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        dlg.show();
//
//        // this is going to load a 30mb download...
//        Ion.with(this)
//                .load(url)
//                .progressDialog(dlg)
//                .setLogging("DeepZoom", Log.VERBOSE)
//                .withBitmap()
//                .deepZoom()
//                .intoImageView(photoView)
//                .setCallback(new FutureCallback<ImageView>() {
//                    @Override
//                    public void onCompleted(Exception e, ImageView result) {
//                        dlg.cancel();
//                    }
//                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.indoor_map, menu);
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        // Need to call clean-up
        mAttacher.cleanup();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem zoomToggle = menu.findItem(R.id.menu_zoom_toggle);
        assert null != zoomToggle;
        zoomToggle.setTitle(mAttacher.canZoom() ? R.string.menu_zoom_disable : R.string.menu_zoom_enable);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_zoom_toggle:
                mAttacher.setZoomable(!mAttacher.canZoom());
                return true;

            case R.id.menu_scale_fit_center:
                mAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return true;

            case R.id.menu_scale_fit_start:
                mAttacher.setScaleType(ImageView.ScaleType.FIT_START);
                return true;

            case R.id.menu_scale_fit_end:
                mAttacher.setScaleType(ImageView.ScaleType.FIT_END);
                return true;

            case R.id.menu_scale_fit_xy:
                mAttacher.setScaleType(ImageView.ScaleType.FIT_XY);
                return true;

            case R.id.menu_scale_scale_center:
                mAttacher.setScaleType(ImageView.ScaleType.CENTER);
                return true;

            case R.id.menu_scale_scale_center_crop:
                mAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return true;

            case R.id.menu_scale_scale_center_inside:
                mAttacher.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                return true;

            case R.id.menu_scale_random_animate:
            case R.id.menu_scale_random:
                Random r = new Random();

                float minScale = mAttacher.getMinimumScale();
                float maxScale = mAttacher.getMaximumScale();
                float randomScale = minScale + (r.nextFloat() * (maxScale - minScale));
                mAttacher.setScale(randomScale, item.getItemId() == R.id.menu_scale_random_animate);

                showToast(String.format(SCALE_TOAST_STRING, randomScale));

                return true;
            case R.id.menu_matrix_restore:
                if (mCurrentDisplayMatrix == null)
                    showToast("You need to capture display matrix first");
                else
                    mAttacher.setDisplayMatrix(mCurrentDisplayMatrix);
                return true;
            case R.id.menu_matrix_capture:
                mCurrentDisplayMatrix = mAttacher.getDisplayMatrix();
                return true;
            case R.id.extract_visible_bitmap:
                try {
                    Bitmap bmp = mAttacher.getVisibleRectangleBitmap();
                    File tmpFile = File.createTempFile("photoview", ".png",
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                    FileOutputStream out = new FileOutputStream(tmpFile);
                    bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.close();
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/png");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tmpFile));
                    startActivity(share);
                    Toast.makeText(this, String.format("Extracted into: %s", tmpFile.getAbsolutePath()), Toast.LENGTH_SHORT).show();
                } catch (Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(this, "Error occured while extracting bitmap", Toast.LENGTH_SHORT).show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class PhotoTapListener implements OnPhotoTapListener {

        @Override
        public void onPhotoTap(View view, float x, float y) {
            float xPercentage = x * 100f;
            float yPercentage = y * 100f;

            showToast(String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage, view == null ? 0 : view.getId()));
        }
    }

    private void showToast(CharSequence text) {
        if (null != mCurrentToast) {
            mCurrentToast.cancel();
        }

        mCurrentToast = Toast.makeText(IndoorMapActivity.this, text, Toast.LENGTH_SHORT);
        mCurrentToast.show();
    }

    private class MatrixChangeListener implements OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF rect) {
            mCurrMatrixTv.setText(rect.toString());
        }
    }
}
