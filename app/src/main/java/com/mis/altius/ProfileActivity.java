package com.mis.altius;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.samehadar.iosdialog.IOSDialog;
import com.mis.altius.adapter.CustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileActivity extends Activity {

    private CustomAdapter adapter;
    protected ArrayList<MyData> data_list;
    TextView tv_nama, tv_angkatan, tv_kantor, tv_kelamin, tv_ttl, tv_email, tv_sektor, tv_jabatan, tv_alamat_kantor, tv_kota_kantor, tv_domisili;
    private String nama, angkatan, kantor, kelamin, ttl, email, sektor, jabatan, alamat_kantor, kota_kantor, domisili;
    ImageView foto_profil;
    IOSDialog dialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    BackgroundTask task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tv_nama = findViewById(R.id.tv_nama);
        tv_angkatan = findViewById(R.id.tv_tahun);
        tv_kantor = findViewById(R.id.tv_kantor);
        tv_kelamin = findViewById(R.id.tv_kelamin);
        tv_ttl = findViewById(R.id.tv_ttl);
        tv_email = findViewById(R.id.tv_email);
        tv_sektor = findViewById(R.id.tv_sektor);
        tv_jabatan = findViewById(R.id.tv_jabatan);
        tv_alamat_kantor = findViewById(R.id.tv_alamat_kantor);
        tv_kota_kantor = findViewById(R.id.tv_kota_kantor);
        tv_domisili = findViewById(R.id.tv_domisili);
        foto_profil = findViewById(R.id.foto_profil);

        //Thread.setDefaultUncaughtExceptionHandler(new UncaughtException(ProfileActivity.this));

        ImageView btn_request = findViewById(R.id.request);
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RequestActivity.class);
                intent.putExtra("nama", nama);
                startActivity(intent);
            }
        });

        final ImageView btn_favorite = findViewById(R.id.favorite);
        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Favorite is successfully added",Toast.LENGTH_SHORT).show();
                btn_favorite.setImageResource(R.drawable.favorite);
            }
        });

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        if (mSwipeRefreshLayout!=null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.blue);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tv_nama.setText(null);
                            tv_angkatan.setText(null);
                            tv_kantor.setText(null);
                            tv_kelamin.setText(null);
                            tv_ttl.setText(null);
                            tv_email.setText(null);
                            tv_sektor.setText(null);
                            tv_jabatan.setText(null);
                            tv_alamat_kantor.setText(null);
                            tv_kota_kantor.setText(null);
                            tv_domisili.setText(null);
                            foto_profil.setImageDrawable(null);

                            mSwipeRefreshLayout.setRefreshing(false);
                            String id = String.valueOf(getIntent().getIntExtra("id",0));
                            task = new BackgroundTask();
                            task.execute(id);
                        }
                    }, 1000);
                }
            });
        }


        data_list = new ArrayList<>();
        String id = String.valueOf(getIntent().getIntExtra("id",0));
        task = new BackgroundTask();
        task.execute(id);


    }
    private class BackgroundTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            dialog = new IOSDialog.Builder(ProfileActivity.this)
                    .setTitleColorRes(R.color.gray)
                    .setCancelable(false)
                    .setMessageContentGravity(Gravity.END)
                    .build();
            dialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url("https://altius.000webhostapp.com/profile.php?id="+id).build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                finish();
                Toast.makeText(getApplicationContext(), "Check your connection", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("Result: ", result);
            dialog.dismiss();
            if (result==null){
                Toast.makeText(getApplicationContext(),"Check your connection",Toast.LENGTH_LONG).show();
                return;
            }
            if (result.contains("error")){
                Toast.makeText(getApplicationContext(), "Check your connection", Toast.LENGTH_LONG).show();
            }
            try {
                JSONArray array = new JSONArray(result);
                for (int i=0; i<array.length(); i++){
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
                    nama = data.getNama();
                    angkatan = data.getAngkatan();
                    kantor = data.getPerusahaan();
                    kelamin = data.getJenis_kelamin();
                    ttl = data.getTtl();
                    email = data.getEmail();
                    sektor = data.getSektor();
                    jabatan = data.getJabatan();
                    alamat_kantor = data.getAlamat_kantor();
                    kota_kantor = data.getKota_kantor();
                    domisili = data.getDomisili();

                    if (kelamin.equals("Perempuan")){
                        foto_profil.setImageResource(R.drawable.woman);
                    }
                    else foto_profil.setImageResource(R.drawable.man);

                    adapter = new CustomAdapter(ProfileActivity.this, data_list);

                    tv_nama.setText(nama);
                    tv_angkatan.setText(angkatan);
                    tv_kantor.setText(kantor);
                    tv_kelamin.setText(kelamin);
                    tv_ttl.setText(ttl);
                    tv_email.setText(email);
                    tv_sektor.setText(sektor);
                    tv_jabatan.setText(jabatan);
                    tv_alamat_kantor.setText(alamat_kantor);
                    tv_kota_kantor.setText(kota_kantor);
                    tv_domisili.setText(domisili);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
