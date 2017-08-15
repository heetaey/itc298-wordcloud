package net.denryu.android.wordcloud;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by archie on 8/15/2017.
 */

public class ListViewAdapter extends ArrayAdapter<TextInput> {
    public ListViewAdapter(Context context, int resource, List<TextInput> items) {
        super(context, resource, items);
    }

    @Override
    public getItemId(int position) {
        
    }
}
