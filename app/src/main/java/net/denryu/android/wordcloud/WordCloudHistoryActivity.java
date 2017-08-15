package net.denryu.android.wordcloud;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}