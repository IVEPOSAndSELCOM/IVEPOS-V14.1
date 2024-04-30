package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 12/30/2014.
 */
import android.R;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

public class SearchResultsActivity extends AppCompatActivity {
    DatabaseTable db = new DatabaseTable(this);
    final Context context=this;
    ListView list;
    TextView v;
    String res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_list_item_1);
        v=(TextView)findViewById(R.id.text1);

        handleIntent(getIntent());
        v.setText(res);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Cursor c = db.getWordMatches(query, null);
            c.moveToFirst();
            res=c.getString(1);


        }
    }
}
