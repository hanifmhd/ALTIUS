package com.mis.altius;

import android.animation.Animator;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.historyprovider_library.WordContract;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.mis.altius.adapter.CustomAdapter;
import com.mis.altius.adapter.SearchResult;
import com.mis.altius.adapter.SearchResultAdapter;
import com.mis.altius.adapter.SimpleAnimationListener;

import org.cryse.widget.persistentsearch.PersistentSearchView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ResultActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    RecyclerView mRecyclerView;
    GridLayoutManager gridLayoutManager;
    private CustomAdapter adapter;
    protected ArrayList<MyData> data_list;
    IOSDialog dialog;
//    CheckBox cbNama, cbPerusahaan, cbAngkatan;
//    RadioButton rbAscending, rbDescending;

    private PersistentSearchView mSearchView;
    private View mSearchTintView;
    private RecyclerView mRecyclerView1;
    private SearchResultAdapter mResultAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    BackgroundTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        final ImageView filter = (ImageView) findViewById(R.id.filter);
        ImageView logo = (ImageView) findViewById(R.id.logo);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialoglayout = inflater.inflate(R.layout.dialog_filter_option, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this,R.style.AppTheme_Splash);
                builder.setView(dialoglayout);
                final Dialog d = builder.setView(dialoglayout).create();
                Button btn_submit = dialoglayout.findViewById(R.id.btn_submit_filter);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(d.getWindow().getAttributes());

                lp.width = lp.WRAP_CONTENT;
                lp.height = lp.WRAP_CONTENT;
                lp.x= filter.getLeft();
                lp.y= filter.getMaxHeight();
                d.show();
                d.getWindow().setAttributes(lp);

                final CheckBox cbNama = dialoglayout.findViewById(R.id.cbNama);
                final CheckBox cbAngkatan = dialoglayout.findViewById(R.id.cbAngkatan);
                final CheckBox cbPerusahaan = dialoglayout.findViewById(R.id.cbPerusahaan);
                final RadioButton rbAscending = dialoglayout.findViewById(R.id.rbAscending);
                final RadioButton rbDescending = dialoglayout.findViewById(R.id.rbDescending);

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sortChecking(d, cbNama, cbAngkatan, cbPerusahaan, rbAscending, rbDescending);
                    }
                });

            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        if (mSwipeRefreshLayout!=null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.blue);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.setLayoutManager(null);
                            data_list.clear();

                            mSwipeRefreshLayout.setRefreshing(false);
                            Intent i = getIntent();
                            String nama = i.getStringExtra("nama");
                            String angkatan = i.getStringExtra("angkatan");
                            String kota_kantor = i.getStringExtra("kota_kantor");
                            String perusahaan = i.getStringExtra("perusahaan");
                            String jabatan = i.getStringExtra("jabatan");
                            task = new BackgroundTask();
                            task.execute(nama,angkatan,kota_kantor,perusahaan,jabatan);
                        }
                    }, 1000);
                }
            });
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        data_list = new ArrayList<>();
        Intent i = getIntent();
        String nama = i.getStringExtra("nama");
        String angkatan = i.getStringExtra("angkatan");
        String kota_kantor = i.getStringExtra("kota_kantor");
        String perusahaan = i.getStringExtra("perusahaan");
        String jabatan = i.getStringExtra("jabatan");
        task = new BackgroundTask();
        task.execute(nama, angkatan, kota_kantor, perusahaan, jabatan);

        mSearchView = (PersistentSearchView) findViewById(R.id.searchview);
        mSearchTintView = findViewById(R.id.view_search_tint);
        mRecyclerView1 = (RecyclerView)findViewById(R.id.recyclerview_search_result);
        mRecyclerView1.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(this));
        mResultAdapter = new SearchResultAdapter(new ArrayList<SearchResult>());
        mRecyclerView1.setAdapter(mResultAdapter);

//        Thread.setDefaultUncaughtExceptionHandler(new UncaughtException(ResultActivity.this));

        mSearchTintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.cancelEditing();
            }
        });
        mSearchView.setSuggestionBuilder(new SampleSuggestionsBuilder(this));
        mSearchView.setSearchListener(new PersistentSearchView.SearchListener() {
            @Override
            public void onSearchEditOpened() {
                //Use this to tint the screen
                mSearchTintView.setVisibility(View.VISIBLE);
                mSearchTintView
                        .animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new SimpleAnimationListener())
                        .start();
            }

            @Override
            public void onSearchEditClosed() {
                mSearchTintView
                        .animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new SimpleAnimationListener() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mSearchTintView.setVisibility(View.GONE);
                            }
                        })
                        .start();
            }

            @Override
            public boolean onSearchEditBackPressed() {
                return false;
            }

            @Override
            public void onSearchExit() {
                mResultAdapter.clear();
                if(mRecyclerView1.getVisibility() == View.VISIBLE) {
                    mRecyclerView1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSearchTermChanged(String term) {

            }

            @Override
            public void onSearch(String nama) {
                String angkatan = "";
                String kota_kantor = "";
                String perusahaan = "";
                String jabatan = "";
                String name = nama;
                ContentValues values = new ContentValues();
                values.put(WordContract.WordEntry.COLUMN_WORD_NAME,name);
                Uri newUri = getContentResolver().insert(WordContract.WordEntry.CONTENT_URI, values);

                finish();
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                intent.putExtra("nama", nama);
                intent.putExtra("angkatan", angkatan);
                intent.putExtra("kota_kantor", kota_kantor);
                intent.putExtra("perusahaan", perusahaan);
                intent.putExtra("jabatan", jabatan);
                intent.putExtra("angkatan", angkatan);
                startActivity(intent);
            }

            @Override
            public void onSearchCleared() {
                //Called when the clear button is clicked
            }
        });
    }

    public void sortChecking(Dialog d, CheckBox cbNama, CheckBox cbAngkatan, CheckBox cbPerusahaan, RadioButton rbAscending, RadioButton rbDescending){
        if (cbNama.isChecked()){
            if (rbAscending.isChecked()){
                Collections.sort(data_list, new Comparator<MyData>() {
                    @Override
                    public int compare(MyData lhs, MyData rhs) {
                        return lhs.getNama().toLowerCase(Locale.getDefault()).compareTo(rhs.getNama().toLowerCase(Locale.getDefault()));
                    }
                });
                for (MyData row : data_list){
                    Log.d("Result: ",row.getNama());
                }
                setLayout();
            }
            if (rbDescending.isChecked()){
                Collections.sort(data_list, new Comparator<MyData>() {
                    @Override
                    public int compare(MyData lhs, MyData rhs) {
                        return rhs.getNama().toLowerCase(Locale.getDefault()).compareTo(lhs.getNama().toLowerCase(Locale.getDefault()));
                    }
                });
                for (MyData row : data_list){
                    Log.d("Result: ",row.getNama());
                }
                setLayout();
            }
            d.dismiss();
        }
        if (cbAngkatan.isChecked()){
            if (rbAscending.isChecked()){
                Collections.sort(data_list, new Comparator<MyData>() {
                    @Override
                    public int compare(MyData lhs, MyData rhs) {
                        return lhs.getAngkatan().toLowerCase(Locale.getDefault()).compareTo(rhs.getAngkatan().toLowerCase(Locale.getDefault()));
                    }
                });
                for (MyData row : data_list){
                    Log.d("Result: ",row.getAngkatan());
                }
                setLayout();
            }
            if (rbDescending.isChecked()){
                Collections.sort(data_list, new Comparator<MyData>() {
                    @Override
                    public int compare(MyData lhs, MyData rhs) {
                        return rhs.getAngkatan().toLowerCase(Locale.getDefault()).compareTo(lhs.getAngkatan().toLowerCase(Locale.getDefault()));
                    }
                });
                for (MyData row : data_list){
                    Log.d("Result: ",row.getAngkatan());
                }
                setLayout();
            }
            d.dismiss();
        }
        if (cbPerusahaan.isChecked()){
            if (rbAscending.isChecked()){
                Collections.sort(data_list, new Comparator<MyData>() {
                    @Override
                    public int compare(MyData lhs, MyData rhs) {
                        return lhs.getPerusahaan().toLowerCase(Locale.getDefault()).compareTo(rhs.getPerusahaan().toLowerCase(Locale.getDefault()));
                    }
                });
                for (MyData row : data_list){
                    Log.d("Result: ",row.getPerusahaan());
                }
                setLayout();
            }
            if (rbDescending.isChecked()){
                Collections.sort(data_list, new Comparator<MyData>() {
                    @Override
                    public int compare(MyData lhs, MyData rhs) {
                        return rhs.getPerusahaan().toLowerCase(Locale.getDefault()).compareTo(lhs.getPerusahaan().toLowerCase(Locale.getDefault()));
                    }
                });
                for (MyData row : data_list){
                    Log.d("Result: ",row.getPerusahaan());
                }
                setLayout();
            }
            d.dismiss();
        }
        if (cbNama.isChecked() && cbAngkatan.isChecked()){
            if (rbAscending.isChecked()){
                Collections.sort(data_list, new Comparator<MyData>() {
                    @Override
                    public int compare(MyData lhs, MyData rhs) {
                        return lhs.getNama().toLowerCase(Locale.getDefault()).compareTo(rhs.getNama().toLowerCase(Locale.getDefault()))
                                &lhs.getAngkatan().toLowerCase(Locale.getDefault()).compareTo(rhs.getAngkatan().toLowerCase(Locale.getDefault()));
                    }
                });
                for (MyData row : data_list){
                    Log.d("Result: ",row.getNama());
                }
                setLayout();
            }
            if (rbDescending.isChecked()){
                Collections.sort(data_list, new Comparator<MyData>() {
                    @Override
                    public int compare(MyData lhs, MyData rhs) {
                        return rhs.getNama().toLowerCase(Locale.getDefault()).compareTo(lhs.getNama().toLowerCase(Locale.getDefault()))
                                &rhs.getAngkatan().toLowerCase(Locale.getDefault()).compareTo(lhs.getAngkatan().toLowerCase(Locale.getDefault()));
                    }
                });
                for (MyData row : data_list){
                    Log.d("Result: ",row.getNama());
                }
                setLayout();
            }
            d.dismiss();
        }
        if (cbNama.isChecked() && cbPerusahaan.isChecked()){
            if (rbAscending.isChecked()){
                Collections.sort(data_list, new Comparator<MyData>() {
                    @Override
                    public int compare(MyData lhs, MyData rhs) {
                        return lhs.getNama().toLowerCase(Locale.getDefault()).compareTo(rhs.getNama().toLowerCase(Locale.getDefault()))
                                &lhs.getPerusahaan().toLowerCase(Locale.getDefault()).compareTo(rhs.getPerusahaan().toLowerCase(Locale.getDefault()));
                    }
                });
                for (MyData row : data_list){
                    Log.d("Result: ",row.getNama());
                }
                setLayout();
            }
            if (rbDescending.isChecked()){
                Collections.sort(data_list, new Comparator<MyData>() {
                    @Override
                    public int compare(MyData lhs, MyData rhs) {
                        return rhs.getNama().toLowerCase(Locale.getDefault()).compareTo(lhs.getNama().toLowerCase(Locale.getDefault()))
                                &rhs.getPerusahaan().toLowerCase(Locale.getDefault()).compareTo(lhs.getPerusahaan().toLowerCase(Locale.getDefault()));
                    }
                });
                for (MyData row : data_list){
                    Log.d("Result: ",row.getNama());
                }
                setLayout();
            }
            d.dismiss();
        }
        if (cbAngkatan.isChecked() && cbPerusahaan.isChecked()){
            if (rbAscending.isChecked()){
                Collections.sort(data_list, new Comparator<MyData>() {
                    @Override
                    public int compare(MyData lhs, MyData rhs) {
                        return lhs.getAngkatan().toLowerCase(Locale.getDefault()).compareTo(rhs.getAngkatan().toLowerCase(Locale.getDefault()))
                                &lhs.getPerusahaan().toLowerCase(Locale.getDefault()).compareTo(rhs.getPerusahaan().toLowerCase(Locale.getDefault()));
                    }
                });
                for (MyData row : data_list){
                    Log.d("Result: ",row.getAngkatan());
                }
                setLayout();
            }
            if (rbDescending.isChecked()){
                Collections.sort(data_list, new Comparator<MyData>() {
                    @Override
                    public int compare(MyData lhs, MyData rhs) {
                        return rhs.getAngkatan().toLowerCase(Locale.getDefault()).compareTo(lhs.getAngkatan().toLowerCase(Locale.getDefault()))
                                &rhs.getPerusahaan().toLowerCase(Locale.getDefault()).compareTo(lhs.getPerusahaan().toLowerCase(Locale.getDefault()));
                    }
                });
                for (MyData row : data_list){
                    Log.d("Result: ",row.getAngkatan());
                }
                setLayout();
            }
            d.dismiss();
        }
        if (cbNama.isChecked() && cbAngkatan.isChecked() && cbPerusahaan.isChecked()){
            if (rbAscending.isChecked()){
                Collections.sort(data_list, new Comparator<MyData>() {
                    @Override
                    public int compare(MyData lhs, MyData rhs) {
                        return lhs.getNama().toLowerCase(Locale.getDefault()).compareTo(rhs.getNama().toLowerCase(Locale.getDefault()))
                                &lhs.getAngkatan().toLowerCase(Locale.getDefault()).compareTo(rhs.getAngkatan().toLowerCase(Locale.getDefault()))
                                &lhs.getPerusahaan().toLowerCase(Locale.getDefault()).compareTo(rhs.getPerusahaan().toLowerCase(Locale.getDefault()));
                    }
                });
                for (MyData row : data_list){
                    Log.d("Result: ",row.getNama());
                }
                setLayout();
            }
            if (rbDescending.isChecked()){
                Collections.sort(data_list, new Comparator<MyData>() {
                    @Override
                    public int compare(MyData lhs, MyData rhs) {
                        return rhs.getNama().toLowerCase(Locale.getDefault()).compareTo(lhs.getNama().toLowerCase(Locale.getDefault()))
                                &rhs.getAngkatan().toLowerCase(Locale.getDefault()).compareTo(lhs.getAngkatan().toLowerCase(Locale.getDefault()))
                                &rhs.getPerusahaan().toLowerCase(Locale.getDefault()).compareTo(lhs.getPerusahaan().toLowerCase(Locale.getDefault()));
                    }
                });
                for (MyData row : data_list){
                    Log.d("Result: ",row.getNama());
                }
                setLayout();
            }
            d.dismiss();
        }
        if (cbNama.isChecked() || cbAngkatan.isChecked() || cbPerusahaan.isChecked()){
            if (!rbAscending.isChecked() && !rbDescending.isChecked()){
                Toast.makeText(getApplicationContext(),"Please choose the option", Toast.LENGTH_SHORT).show();
            }
        }
        if (!cbNama.isChecked() && !cbAngkatan.isChecked() && !cbPerusahaan.isChecked()){
            if (!rbAscending.isChecked() && !rbDescending.isChecked()){
                Toast.makeText(getApplicationContext(),"Please choose the option", Toast.LENGTH_SHORT).show();
            }
        }
        if (!cbNama.isChecked() && !cbAngkatan.isChecked() && !cbPerusahaan.isChecked()){
            if (rbAscending.isChecked() || rbDescending.isChecked()){
                Toast.makeText(getApplicationContext(),"Please choose the option", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void setLayout(){
        gridLayoutManager = new GridLayoutManager(ResultActivity.this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        adapter = new CustomAdapter(ResultActivity.this, data_list);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton rb = (RadioButton) group.findViewById(checkedId);
        if (null != rb && checkedId > -1) {

            // checkedId is the RadioButton selected
            switch (checkedId) {
                case R.id.rbAscending:
                    // Do Something
                    Toast.makeText(getApplicationContext(),"ASCENDING",Toast.LENGTH_SHORT).show();
                    break;

                case R.id.rbDescending:
                    // Do Something
                    Toast.makeText(getApplicationContext(),"DESCENDING",Toast.LENGTH_SHORT).show();
                    break;

            }
        }

    }

    private class BackgroundTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            dialog = new IOSDialog.Builder(ResultActivity.this)
                    .setTitle("Searching")
                    .setTitleColorRes(R.color.gray)
                    .setMessageContent("Please wait")
                    .setCancelable(false)
                    .setMessageContentGravity(Gravity.END)
                    .build();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String nama = strings[0];
            String angkatan = strings[1];
            String kota_kantor = strings[2];
            String perusahaan = strings[3];
            String jabatan = strings[4];
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url("https://altius.000webhostapp.com/result.php?nama="+ nama +
                            "&angkatan=" + angkatan +
                            "&kota_kantor=" + kota_kantor +
                            "&perusahaan=" + perusahaan +
                            "&jabatan=" + jabatan).build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("Result:","" +result);
            dialog.dismiss();
            if (result==null){
                Toast.makeText(getApplicationContext(),"Check your connection",Toast.LENGTH_LONG).show();
                return;
            }
            if (result.contains("error")){
                Toast.makeText(getApplicationContext(), "No result\nPlease try other searching", Toast.LENGTH_LONG).show();
            }

            try {
                JSONArray array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    MyData data = new MyData(object.getInt("id"),
                            object.getString("nama"),
                            object.getString("ttl"),
                            object.getString("jenis_kelamin"),
                            object.getString("alamat_rumah"),
                            object.getString("kota"),
                            object.getString("angkatan"),
                            object.getString("no_telepon"),
                            object.getString("email"),
                            object.getString("perusahaan"),
                            object.getString("sektor"),
                            object.getString("jabatan"),
                            object.getString("level_jabatan"),
                            object.getString("alamat_kantor"),
                            object.getString("kota_kantor"),
                            object.getString("domisili"));
                    data_list.add(data);

                    gridLayoutManager = new GridLayoutManager(ResultActivity.this, 3);
                    mRecyclerView.setLayoutManager(gridLayoutManager);
                    adapter = new CustomAdapter(ResultActivity.this, data_list);
                    mRecyclerView.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}