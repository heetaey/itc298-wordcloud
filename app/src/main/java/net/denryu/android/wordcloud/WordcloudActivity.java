package net.denryu.android.wordcloud;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by hsMacbook on 2017. 7. 17..
 */

public class WordcloudActivity extends AppCompatActivity implements
        OnClickListener {

    private static final int OPEN_DOCUMENT_REQUEST = 1;

    private TextView inputTxtView;
    private EditText txtInput;
    private Button openFileButton;
    private Button generateButton;
    private TextView mostWord, mostWordResult;
    private TextView appearanceRate, appearanceResult;
    private TextView uniqueWords, uniqueResult;
    private TextView totalCount, totalCountResult;

    private WordCounter wordCounter;
    private WordCounterDB wordCounterDB;



    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordcloud_activity);

        inputTxtView = (TextView) findViewById(R.id.inputTxtView);
        txtInput = (EditText) findViewById(R.id.txtInput);
        generateButton = (Button) findViewById(R.id.generateButton);
        generateButton.setOnClickListener(this);
        openFileButton = (Button) findViewById(R.id.openFileButton);
        openFileButton.setOnClickListener(this);

        mostWord = (TextView) findViewById(R.id.mostWord);
        mostWordResult = (TextView) findViewById(R.id.commonWord);
        appearanceRate = (TextView) findViewById(R.id.appearanceRate);
        appearanceResult = (TextView) findViewById(R.id.appearanceResult);
        uniqueWords = (TextView) findViewById(R.id.uniqueWords);
        uniqueResult = (TextView) findViewById(R.id.distinctResult);
        totalCount = (TextView) findViewById(R.id.totalCount);
        totalCountResult = (TextView) findViewById(R.id.totalCountings);

        wordCounter = new WordCounter();
        wordCounterDB = new WordCounterDB(this);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.generateButton:
                wordCounter.countWords(txtInput.getText().toString());
                populateResults();
                break;
            case R.id.openFileButton:
                openFile();
                break;
        }
    }

    public void populateResults() {
        wordCounterDB.insertWords(wordCounter.getWordCountMap());
        uniqueResult.setText(String.valueOf(wordCounter.distinctWordCount()));
        totalCountResult.setText(String.valueOf(wordCounter.totalWordCount()));
        mostWordResult.setText(String.valueOf(wordCounter.mostCommonWord));
        String appearanceRateString = String.valueOf((int)(100 * wordCounter.appearanceRate)) + '%';
        appearanceResult.setText(appearanceRateString);


    }

    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, OPEN_DOCUMENT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){

        if(requestCode == OPEN_DOCUMENT_REQUEST && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri uri = resultData.getData();

                try {
                    String content = readFileContent(uri);
                    txtInput.setText(content);
                } catch (IOException e) {
                    //Some log, Alert or Toast goes here
                }

            }
        }
    }

    private String readFileContent(Uri uri) throws IOException {

        InputStream inStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        StringBuilder stringBuilder = new StringBuilder();
        String currentline;
        while((currentline = reader.readLine()) != null) {
            stringBuilder.append(currentline + "\n");
        }
        inStream.close();
        return stringBuilder.toString();
    }

}
