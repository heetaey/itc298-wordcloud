package net.denryu.android.wordcloud;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.graphics.Bitmap;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class WordCloudHistoryActivity extends AppCompatActivity {

    private ListView historyListView;
    WordCounterDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordcloudhistory_activity);
        historyListView = (ListView) findViewById(android.R.id.list);

        db = new WordCounterDB(this);
        ArrayList<TextInput> textInputs = db.getTextInputs();


//        setListAdapter(new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, textInputs));
//        getListView().setTextFilterEnabled(false);

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
                break;
            case R.id.item_new_input:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}