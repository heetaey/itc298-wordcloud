package net.denryu.android.wordcloud;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class WordCloudHistoryActivity extends AppCompatActivity {

    private ListView historyListView;
    WordCounterDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordcloudhistory_activity);
        historyListView = (ListView) findViewById(android.R.id.list);

        db = new WordCounterDB(this);
        refreshHistory();
    }

    private void refreshHistory() {
        ArrayList<TextInput> textInputs = db.getTextInputs();
        historyListView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, textInputs));

        historyListView.setOnItemClickListener((AdapterView<?> parent, View v, int pos, long id) -> {

            //retrieve TextInput that was clicked
            TextInput currTextInput = (TextInput) parent.getAdapter().getItem(pos);
            long currInputId = currTextInput.inputId;

            //build new WordCounter using words and counts stored in DB
            WordCounter wc = db.getWordCounter(currInputId);
            Log.d("wordcounter", "selected inputid: " + currInputId + ", wordcounter contents: " + wc);

            //send text to to output activity to re-create cloud & results
            Intent i = new Intent(WordCloudHistoryActivity.this, WordCloudOutputActivity.class);
            i.putExtra("txtInput", wc.toString());
            i.putExtra("skipDBstore", true);
            startActivity(i);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_word_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_clear_history:
                db.clearDB();
                refreshHistory();
                break;
            case R.id.item_new_input:
                Intent i = new Intent(WordCloudHistoryActivity.this, WordCloudActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}