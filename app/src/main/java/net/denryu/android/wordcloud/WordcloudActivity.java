package net.denryu.android.wordcloud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by hsMacbook on 2017. 7. 17..
 */

public class WordcloudActivity extends AppCompatActivity {

    private TextView inputTxtView;
    private EditText txtInput;
    private Button generateButton;
    private TextView mostWord;
    private TextView appearanceRate;
    private TextView uniqueWords;
    private TextView totalCount;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordcloud_activity);

        inputTxtView = (TextView) findViewById(R.id.inputTxtView);
        txtInput = (EditText) findViewById(R.id.txtInput);
        generateButton = (Button) findViewById(R.id.generateButton);

        mostWord = (TextView) findViewById(R.id.mostWord);
        appearanceRate = (TextView) findViewById(R.id.appearanceRate);
        uniqueWords = (TextView) findViewById(R.id.uniqueWords);
        totalCount = (TextView) findViewById(R.id.totalCount);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
