package net.denryu.android.wordcloud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by hsMacbook on 2017. 7. 17..
 */

public class WordcloudActivity extends AppCompatActivity implements
        OnClickListener {

    private TextView inputTxtView;
    private EditText txtInput;
    private Button generateButton;
    private TextView mostWord, mostWordResult;
    private TextView appearanceRate, appearanceResult;
    private TextView uniqueWords, uniqueResult;
    private TextView totalCount, totalCountResult;

    private WordCounter wordCounter;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordcloud_activity);

        inputTxtView = (TextView) findViewById(R.id.inputTxtView);
        txtInput = (EditText) findViewById(R.id.txtInput);
        generateButton = (Button) findViewById(R.id.generateButton);
        generateButton.setOnClickListener(this);

        mostWord = (TextView) findViewById(R.id.mostWord);
        mostWordResult = (TextView) findViewById(R.id.commonWord);
        appearanceRate = (TextView) findViewById(R.id.appearanceRate);
        appearanceResult = (TextView) findViewById(R.id.appearanceResult);
        uniqueWords = (TextView) findViewById(R.id.uniqueWords);
        uniqueResult = (TextView) findViewById(R.id.distinctResult);
        totalCount = (TextView) findViewById(R.id.totalCount);
        totalCountResult = (TextView) findViewById(R.id.totalCountings);

        wordCounter = new WordCounter();

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
        }
    }

    public void populateResults() {
        uniqueResult.setText(String.valueOf(wordCounter.distinctWordCount()));
        totalCountResult.setText(String.valueOf(wordCounter.totalWordCount()));
        mostWordResult.setText(String.valueOf(wordCounter.mostCommonWord));
        String appearanceRateString = String.valueOf((int)(100 * wordCounter.appearanceRate)) + '%';
        appearanceResult.setText(appearanceRateString);


    }
}
