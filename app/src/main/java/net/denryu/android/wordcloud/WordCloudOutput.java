package net.denryu.android.wordcloud;

import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

//import net.alhazmy13.example.R;
import net.alhazmy13.wordcloud.ColorTemplate;
import net.alhazmy13.wordcloud.WordCloud;
import net.alhazmy13.wordcloud.WordCloudView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordCloudOutput extends AppCompatActivity {
    private static final String TAG = "WordCloudActivity";
    List<WordCloud> list ;
    String text = "one two two three three three four four four four five five five five six six six six six six ";
    WordCounter wordCounter = new WordCounter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordcloud_output);
        wordCounter.countWords(text);
        generateRandomText();
        list = wordCounter.deriveMostCommonWordsStat();
        WordCloudView wordCloud = (WordCloudView) findViewById(R.id.wordCloud);
        wordCloud.setDataSet(list);
        wordCloud.setSize(600,600);
        wordCloud.setColors(ColorTemplate.MATERIAL_COLORS);
        wordCloud.notifyDataSetChanged();

    }

    private void generateRandomText() {
        String[] data = text.split(" ");
        list = new ArrayList<>();
        Random random = new Random();
        for (String s : data) {
            list.add(new WordCloud(s,random.nextInt(50)+10));
        }
    }
}


