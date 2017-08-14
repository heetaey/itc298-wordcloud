package net.denryu.android.wordcloud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class WordCloudHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordcloudhistory_activity);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_clear_history:
                //clearHistory();
                break;
            case R.id.item_new_input:
                //return to wordcloud_activity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
