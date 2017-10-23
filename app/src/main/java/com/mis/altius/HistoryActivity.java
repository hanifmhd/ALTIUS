package com.mis.altius;

/**
 * Created by Hanifmhd on 9/17/2017.
 */

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.historyprovider_library.WordContract;
import com.mis.altius.adapter.WordCursorAdapter;

public class HistoryActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    WordCursorAdapter mCursorAdapter;
    private static final int WORD_LOADER = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        final ListView wordListView = (ListView) findViewById(R.id.list);
        mCursorAdapter = new WordCursorAdapter(this, null);
        wordListView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(WORD_LOADER, null,this);

//        wordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int angkatan = wordListView.getPositionForView(view);
//                Context context = view.getContext();
//                Intent intent = new Intent(context, ResultActivity.class);
//                intent.putExtra("angkatan", angkatan);
//                context.startActivity(intent);
//                finish();
//            }
//        });
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                WordContract.WordEntry._ID,
                WordContract.WordEntry.COLUMN_WORD_NAME};

        return new android.content.CursorLoader(this,
                WordContract.WordEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}