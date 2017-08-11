package net.denryu.android.wordcloud;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordCloudOutputActivity extends AppCompatActivity {
    private static final String TAG = "WordCloud";
    List<WordCloud> list;
    WordCounter wc = new WordCounter();

    private TextView mostWordResult;
    private TextView appearanceResult;
    private TextView uniqueResult;
    private TextView totalCountResult;
    private File imagePath;

    public String advertisingId;
    private WordCounter wordCounter;
    private WordCounterDB wordCounterDB;

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission is granted");
                return true;
            } else {
                Log.d(TAG, "Permission is revoked");
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
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordcloud_output);

        Runnable getAdvertIdTask = () ->  {
            setAdvertId();
        };
        new Thread(getAdvertIdTask).start();
        //This is for asking user granting permission to access the storage (after SDK23)
        isStoragePermissionGranted();

        mostWordResult = (TextView) findViewById(R.id.commonWord);
        appearanceResult = (TextView) findViewById(R.id.appearanceResult);
        uniqueResult = (TextView) findViewById(R.id.distinctResult);
        totalCountResult = (TextView) findViewById(R.id.totalCountings);

        wordCounter = new WordCounter();
        wordCounterDB = new WordCounterDB(this);

        Intent i = getIntent();
        String getText = i.getStringExtra("txtInput");

        wordCounter.countWords(getText);
        generateText();
        list = wordCounter.deriveMostCommonWordsStat();
        WordCloudView wordCloud = (WordCloudView) findViewById(R.id.wordCloud);
        wordCloud.setDataSet(list);
        wordCloud.setSize(300, 350);
        wordCloud.setColors(ColorTemplate.MATERIAL_COLORS);
        wordCloud.notifyDataSetChanged();

        processInput(getText);
        populateResults();
    }

    public void populateResults() {
        wordCounterDB.storeInput(wordCounter.getWordCountMap(), advertisingId, null, null);
        uniqueResult.setText(String.valueOf(wordCounter.distinctWordCount()));
        totalCountResult.setText(String.valueOf(wordCounter.totalWordCount()));
        mostWordResult.setText(String.valueOf(wordCounter.mostCommonWord));
        String appearanceRateString = String.valueOf((int) (100 * wordCounter.appearanceRate)) + '%';
        appearanceResult.setText(appearanceRateString);
    }

    private void processInput(String text) {
        wordCounter.countWords(text);
    }

    private void clearHistory() {
        wordCounterDB.clearDB();
    }

    private void generateText() {
        String[] data = wc.toString().split(" ");
        list = new ArrayList<>();
        Random random = new Random();
        for (String s : data) {
            list.add(new WordCloud(s, random.nextInt(50)));
        }
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
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
                shareIt();
                break;
            case R.id.item_clear_history:
                clearHistory();
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
        imagePath = new File(Environment.getExternalStorageDirectory() + "/scrnshot.png"); ////File imagePath
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
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "This is my Word Cloud!";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out my WordCloud!");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void setAdvertId() {
        //retrieve advertising ID
//        AdvertisingIdClient.Info idInfo = null;


        AdvertisingIdClient.Info id2Info = null;
        try {
            id2Info = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
//                    Log.d("wordcounter", idInfo.toString());
        } catch (GooglePlayServicesNotAvailableException |GooglePlayServicesRepairableException e) {
            Log.d("wordcounter", "inside google error");
            e.printStackTrace();
        } catch (Exception e) {
            Log.d("wordcounter", "inside general error " + e.getMessage());
            e.printStackTrace();
        }
        try{
            advertisingId = id2Info.getId();
        }catch (Exception e){
            Log.d("wordcounter", "inside getId error");
            e.printStackTrace();
        }
        Log.d("wordcounter", "AdvertID is: " + advertisingId);

    }

}