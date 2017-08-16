package net.denryu.android.wordcloud;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import net.alhazmy13.wordcloud.ColorTemplate;
import net.alhazmy13.wordcloud.WordCloud;
import net.alhazmy13.wordcloud.WordCloudView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WordCloudOutputActivity extends AppCompatActivity {
    private static final String TAG = "WordCloud";
    List<WordCloud> list;

    private TextView mostWordResult;
    private TextView appearanceResult;
    private TextView uniqueResult;
    private TextView totalCountResult;
    private File imagePath;

    public String advertisingId, textSource;
    private WordCounter wordCounter;
    private WordCounterDB wordCounterDB;

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission is granted");
                return true;
            } else {
                Log.d(TAG, "Permission hasn't been granted yet");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            Bitmap bitmap = takeScreenshot();
            saveBitmap(bitmap);
            shareIt();
        } else {
            //user didn't grant permissions, do nothing (don't try to share)
            new AlertDialog.Builder(this).setTitle("Unable to Share")
                    .setMessage("In order to share results, this app needs permission to write to storage.")
                    .setNeutralButton("OK", (DialogInterface dialog, int which) -> {
                        //do nothing
                    }).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordcloud_output);

        //start thread to retrieve advertising ID for later use
        Runnable getAdvertIdTask = () -> {
            setAdvertId();
        };
        new Thread(getAdvertIdTask).start();

        mostWordResult = (TextView) findViewById(R.id.commonWord);
        appearanceResult = (TextView) findViewById(R.id.appearanceResult);
        uniqueResult = (TextView) findViewById(R.id.distinctResult);
        totalCountResult = (TextView) findViewById(R.id.totalCountings);

        wordCounterDB = new WordCounterDB(this);

        Intent i = getIntent();
        String getText = i.getStringExtra("txtInput");

        if (getText.length() > 30)
            textSource = getText.substring(0, 29) + "...";
        else
            textSource = getText;

        wordCounter = new WordCounter(getText);
        list = wordCounter.createCloudList();
        WordCloudView wordCloud = (WordCloudView) findViewById(R.id.wordCloud);
        wordCloud.setDataSet(list);
        wordCloud.setColors(ColorTemplate.MATERIAL_COLORS);
        wordCloud.notifyDataSetChanged();

        //don't store anything in db if skipDBstore is true
        if (!i.getBooleanExtra("skipDBstore", false))
            storeInDB();
        populateResultsOutput();
    }

    private void storeInDB() {
        int versionCode = 0;
        versionCode = BuildConfig.VERSION_CODE;
        long currDate = System.currentTimeMillis();

        Log.d("Wordcounter", "textSource is: " + textSource);
        TextInput currInput = new TextInput(advertisingId, versionCode, currDate, null, textSource, wordCounter);

        wordCounterDB.storeInput(currInput);
    }

    public void populateResultsOutput() {
        uniqueResult.setText(String.valueOf(wordCounter.distinctWordCount()));
        totalCountResult.setText(String.valueOf(wordCounter.totalWordCount()));
        mostWordResult.setText(String.valueOf(wordCounter.mostCommonWord));
        String appearanceRateString = String.valueOf((float) (100 * wordCounter.appearanceRate)) + '%';
        appearanceResult.setText(appearanceRateString);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.wordcloud_output);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_word_output, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_share:
                if (isStoragePermissionGranted()) {
                    Bitmap bitmap = takeScreenshot();
                    saveBitmap(bitmap);
                    shareIt();
                }
                break;
            case R.id.item_history:
                Intent i = new Intent(WordCloudOutputActivity.this, WordCloudHistoryActivity.class);
                startActivity(i);
                break;
            case R.id.item_new_input:
                //start wordcloudhistory_activity
                Intent intent = new Intent(WordCloudOutputActivity.this, WordCloudActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    private void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/scrnshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    private void shareIt() {
        try {
            Uri uri = convertFileToContentUri(getBaseContext(), this.imagePath);
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("image/*");
            String shareBody = "This is my Word Cloud!";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out my WordCloud!");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdvertId() {
        AdvertisingIdClient.Info id2Info = null;
        try {
            id2Info = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
        } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
            Log.d("wordcounter", "Google Play error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.d("wordcounter", "Error setting advertId: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            advertisingId = id2Info.getId();
        } catch (Exception e) {
            Log.d("wordcounter", "error retrieving advertising ID: " + e.getMessage());
            e.printStackTrace();
        }
        Log.d("wordcounter", "AdvertID is: " + advertisingId);
    }

    protected static Uri convertFileToContentUri(Context context, File file) throws Exception {
        /**
         * by ban-geoengineering
         * https://stackoverflow.com/questions/7305504/convert-file-uri-to-content-uri
         *
         * Converts a file to a content uri, by inserting it into the media store.
         * Requires WRITE_EXTERNAL_STORAGE permission */
        ContentResolver cr = context.getContentResolver();
        String imagePath = file.getAbsolutePath();
        String imageName = null;
        String imageDescription = null;
        try {
            String uriString = MediaStore.Images.Media.insertImage(cr, imagePath, imageName, imageDescription);
            return Uri.parse(uriString);
        } catch (Exception e) {
            Log.e("GREC", e.getMessage(), e);
            return null;
        }
    }

}