package com.mis.altius;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gmail.samehadar.iosdialog.IOSDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestActivity extends AppCompatActivity {
    private RequestActivity requestActivity;
    private RequestQueue requestQueue;
    private static final String URL = "https://altius.000webhostapp.com/request.php";
    private StringRequest request;
    IOSDialog dialog;
    TextView data_nama;
    EditText nama, pekerjaan, institusi, email, kontak, alasan;
    CheckBox phone;
    CheckBox address;
    CheckBox hometown;
    private static final String TAG = "SendRequestActivity";

    Button btn_submit_req;
    Integer first,second,third;
    Integer total;

    ArrayList<String> list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        list = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        data_nama = (TextView) findViewById(R.id.tv_data_nama);
        nama = (EditText) findViewById(R.id.nama_request);
        pekerjaan = (EditText) findViewById(R.id.pekerjaan_request);
        institusi = (EditText) findViewById(R.id.institusi_request);
        email = (EditText) findViewById(R.id.email_request);
        kontak = (EditText) findViewById(R.id.kontak_request);
        alasan = (EditText) findViewById(R.id.alasan_request);
        phone = (CheckBox) findViewById(R.id.checkBox1);
        address = (CheckBox) findViewById(R.id.checkBox2);
        hometown = (CheckBox) findViewById(R.id.checkBox3);

        //Thread.setDefaultUncaughtExceptionHandler(new UncaughtException(RequestActivity.this));

        Intent i = getIntent();
        String nama = i.getStringExtra("nama");
        data_nama.setText(nama);

        btn_submit_req = (Button) findViewById(R.id.btn_submit_request);
        btn_submit_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
    }

    public void send() {
        Log.d(TAG, "Send");
        if (!validate()) {
            onSendFailed();
            return;
        }
        final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(RequestActivity.this);
        myAlertDialog.setMessage("Are you sure want to submit your request?");
        myAlertDialog.setCancelable(false);
        myAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendData();
                dialog = new IOSDialog.Builder(RequestActivity.this)
                        .setTitle("Sending request")
                        .setTitleColorRes(R.color.gray)
                        .setMessageContent("Please wait")
                        .setCancelable(false)
                        .setMessageContentGravity(Gravity.END)
                        .build();
                dialog.show();
            }
        });
        myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }});
        myAlertDialog.show();
    }

    public void sendData(){
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d("Result:","" +response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.names().get(0).equals("success")) {
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                        finish();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                        onSendFailed();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("data", data_nama.getText().toString());
                                hashMap.put("nama", nama.getText().toString());
                                hashMap.put("pekerjaan", pekerjaan.getText().toString());
                                hashMap.put("institusi", institusi.getText().toString());
                                hashMap.put("email", email.getText().toString());
                                hashMap.put("kontak", kontak.getText().toString());
                                hashMap.put("alasan", alasan.getText().toString());
                                for (String str : list) {
                                    hashMap.put("info", str);
                                }
                                Log.d("Info:","" +hashMap.get("info"));
                                return hashMap;
                            }
                        };
                        requestQueue.add(request);
                    }
                }, 3000);
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if (phone.isChecked()){
            list.add("Phone number");
        }
        if (address.isChecked()){
            list.add("Address");
        }
        if (hometown.isChecked()){
            list.add("Hometown");
        }
        if (phone.isChecked() && address.isChecked()){
            list.add("Phone number & address");
        }
        if (address.isChecked() && hometown.isChecked()){
            list.add("Address & hometown");
        }
        if (phone.isChecked() && hometown.isChecked()){
            list.add("Phone number & hometown");
        }
        if (phone.isChecked() && address.isChecked() && hometown.isChecked()) {
            list.add("Phone number & address & hometown");
        }
        else list.equals(null);
    }

    public boolean validate() {
        boolean valid = true;
        String nama_request = nama.getText().toString();
        String pekerjaan_request = pekerjaan.getText().toString();
        String institusi_request = institusi.getText().toString();
        String email_request = email.getText().toString();
        String kontak_request = kontak.getText().toString();
        String alasan_request = alasan.getText().toString();

        if (nama_request.isEmpty()) {
            nama.setError("Your name can't be blanked");
            valid = false;
        } else {
            nama.setError(null);
        }

        if (pekerjaan_request.isEmpty()) {
            pekerjaan.setError("Your job can't be blanked");
            valid = false;
        } else {
            pekerjaan.setError(null);
        }

        if (institusi_request.isEmpty()) {
            institusi.setError("Your institution can't be blanked");
            valid = false;
        } else {
            institusi.setError(null);
        }

        if (email_request.isEmpty()) {
            email.setError("Your email can't be blanked");
            valid = false;
        } else if (!email_request.contains("@")){
            email.setError("It is not email format");
            valid = false;
        } else {
            email.setError(null);
        }

        if (kontak_request.isEmpty()) {
            kontak.setError("Your phone number can't be blanked");
            valid = false;
        } else if (!kontak_request.contains("08")){
            kontak.setError("It is not phone number");
            valid = false;
        } else {
            kontak.setError(null);
        }

        if (alasan_request.isEmpty()) {
            alasan.setError("Your reason can't be blanked");
            valid = false;
        } else {
            alasan.setError(null);
        }
        if (!phone.isChecked() && !address.isChecked() && !hometown.isChecked()){
            phone.setError("");
            address.setError("");
            hometown.setError("");
            Toast.makeText(getApplicationContext(), "Please complete the field", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Choose at least 1 information that you want", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    public void onSendFailed() {
        Toast.makeText(getApplicationContext(), "Failed to send request", Toast.LENGTH_LONG).show();
    }

}
