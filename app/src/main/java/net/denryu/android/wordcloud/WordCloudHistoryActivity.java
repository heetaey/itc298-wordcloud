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

public class WordCloudHistoryActivity extends ListActivity {

    private ListView historyListView;
    WordCounterDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordcloudhistory_activity);
        historyListView = (ListView) findViewById(android.R.id.list);

        db = new WordCounterDB(this);
        ArrayList<TextInput> textInputs = db.getTextInputs();


        setListAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, textInputs));
        getListView().setTextFilterEnabled(false);
//
//        LayoutInflater inflater = (LayoutInflater)
//                this.getSystemService(INFLAT);
//        inflater.inflate(R.id.list, null, true);

//        if (textInputs != null && ! textInputs.isEmpty())
//            for (TextInput ti : textInputs) {
//                historyListView.
//            }
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
            case R.id.item_history:
                //go to wordcloudhistory_activity
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

//public class WordCloudHistoryActivity extends ListActivity
//        implements LoaderManager.LoaderCallbacks<Cursor> {
//
//    // The adapter that binds our data to the ListView
//    private SimpleCursorAdapter mAdapter;
//
//    WordCounterDB wordCounterDB;
//
//    private ListView historyListView;
//
////    @Override
////    protected void onLoadFinished(Loader<Cursor>, Cursor) {
////
////    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.wordcloudhistory_activity);
//        historyListView = (ListView) findViewById(R.id.list);
//        int[] viewIDs = { R.id.currItem };
//
//        wordCounterDB = new WordCounterDB(this);
//        populateHistory();
//
//
//        // Query for all people contacts using the Contacts.People convenience class.
//        // Put a managed wrapper around the retrieved cursor so we don't have to worry about
//        // requerying or closing it as the activity changes state.
//        getLoaderManager().initLoader(0, null, this);
//
//
//
//    }
//
//    private void populateHistory() {
//        ArrayList<TextInput> textInputsHistory = wordCounterDB.getTextInputs();
//
//        for (TextInput ti : textInputsHistory) {
//            historyListView
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.item_clear_history:
//                //clearHistory();
//                break;
//            case R.id.item_new_input:
//                //return to wordcloud_activity();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        android.net.Uri uri;
//        String[] test = new String[] {"test", "test2"};
//
//        SimpleCursorAdapter sca = new SimpleCursorAdapter(WordCloudHistoryActivity.this, 0, )
//
//        CursorLoader loader = new CursorLoader(
//                WordCloudHistoryActivity.this,
//                SOME_CONTENT_URI,
//                projection,
//                selection,
//                selectionArgs,
//                sortOrder);
//        // Create a new CursorLoader with the following query parameters.
//        return
//                //new CursorLoader(WordCloudHistoryActivity.this, uri, test,
//                //null, null, null);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//        // A switch-case is useful when dealing with multiple Loaders/IDs
//        switch (loader.getId()) {
//            case LOADER_ID:
//                // The asynchronous load is complete and the data
//                // is now available for use. Only now can we associate
//                // the queried Cursor with the SimpleCursorAdapter.
//                mAdapter.swapCursor(cursor);
//                break;
//        }
//        // The listview now displays the queried data.
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        // For whatever reason, the Loader's data is now unavailable.
//        // Remove any references to the old data by replacing it with
//        // a null Cursor.
//        mAdapter.swapCursor(null);
//    }
//}
