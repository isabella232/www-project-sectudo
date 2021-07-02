package com.example.synradar_sectudo.secure;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.synradar_sectudo.IndBlogActivity;
import com.example.synradar_sectudo.MainNewActivity;
import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.helper.DBHelper;
import com.example.synradar_sectudo.helper.EncryptionUtil;
import com.example.synradar_sectudo.helper.GeneralUtils;
import com.example.synradar_sectudo.learningmodule.LearningTabActivity;

import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONObject;


import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import static android.widget.Toast.*;

public class SecAddkycActivity extends AppCompatActivity {

    private static final String XEALTH_KEY_ALIAS = "";
    DBHelper myDb;
    EditText editFull_Name, editPAN_Number, editAddress, editEmployment_type, editDesignationOccupation, editSalary;
    Button sec_btnSave;
    Button sec_back;
    String topic;
    TextView topicName;


    private class MyTask extends AsyncTask<String, String, String> {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected String doInBackground(String... strs) {
            String responsestr = new String();
            SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
            String token = GeneralUtils.readSharedPrefs(prefs, "session_token");



            String fname = editFull_Name.getText().toString();
            String pannum = editPAN_Number.getText().toString();
            String address = editAddress.getText().toString();
            String emp_type = editEmployment_type.getText().toString();
            String desgn = editDesignationOccupation.getText().toString();
            String sal = editSalary.getText().toString();

            JSONObject reqjson = new JSONObject();
            URL url;
            try {

                reqjson.put("fullname", fname);
                reqjson.put("address", address);
                reqjson.put("pannumber", pannum);
                reqjson.put("designation", desgn);
                reqjson.put("employment_type", emp_type);
                reqjson.put("salary", sal);


                url = new URL(Constants.s_addkycURL);


                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                if (Constants.session_mode.equals("JSE")) {
                    con.setRequestProperty("Cookie", "JSESSIONID=" + token);

                } else {
                    con.setRequestProperty("Authorization", "Bearer " + token);


                }

                con.setDoOutput(true);
                OutputStream out = con.getOutputStream();


                String plaintextJson = reqjson.toString();

                String encryptedJSON = EncryptionUtil.AsymEncrypt(plaintextJson);

                out.write(encryptedJSON.getBytes());

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tempstr;
                while ((tempstr = in.readLine()) != null) {

                    responsestr = tempstr;
                }

                //  addData();

                if (responsestr.contains("Successfully")) {
                    addData();
                }


                in.close();
            } catch (Exception e) {

                e.printStackTrace();
            }
            return responsestr;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);

        String check_token = GeneralUtils.readSharedPrefs(prefs, "session_token");
       // System.out.println("On Resume"+check_token);
        if(check_token == null || check_token.equals("")){

            Toast.makeText(SecAddkycActivity.this, "Invalid Access", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SecAddkycActivity.this, SecLoginActivity.class));
            return;
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);

        String check_token = GeneralUtils.readSharedPrefs(prefs, "session_token");
        if(check_token == null || check_token.equals("")){
            Toast.makeText(SecAddkycActivity.this, "Invalid Access", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SecAddkycActivity.this, SecLoginActivity.class));
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_addkyc);
        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
        String check_token = GeneralUtils.readSharedPrefs(prefs, "session_token");
        if(check_token == null || check_token.equals("")){
            makeText(SecAddkycActivity.this, "Invalid Access", LENGTH_LONG).show();
            startActivity(new Intent(SecAddkycActivity.this, SecLoginActivity.class));
            return;
        }



        topic = getIntent().getStringExtra("topic");


        ImageView tpn;
        tpn = (ImageView) findViewById(R.id.imageViewtp);
        tpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecAddkycActivity.this, MainNewActivity.class).putExtra("irs","back"));
            }});
        ImageView bck;
        bck = (ImageView) findViewById(R.id.imageViewbk);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecAddkycActivity.this, LearningTabActivity.class).putExtra("topic",topic));
            }});
        SQLiteDatabase.loadLibs(this);
        myDb = new DBHelper(this);

        editFull_Name = (EditText) findViewById(R.id.se1);
        editPAN_Number = (EditText) findViewById(R.id.se2);
        editAddress = (EditText) findViewById(R.id.se3);
        editEmployment_type = (EditText) findViewById(R.id.se4);
        editDesignationOccupation = (EditText) findViewById(R.id.se5);
        editSalary = (EditText) findViewById(R.id.se6);
        sec_btnSave = (Button) findViewById(R.id.button37);
        // AddData();
        loadData();
        sec_btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (TextUtils.isEmpty(editFull_Name.getText().toString()) && TextUtils.isEmpty(editPAN_Number.getText().toString()) && TextUtils.isEmpty(editEmployment_type.getText().toString())
                        && TextUtils.isEmpty(editDesignationOccupation.getText().toString())  && TextUtils.isEmpty(editSalary.getText().toString()))
                {
                    Toast.makeText(SecAddkycActivity.this,
                            "Empty field not allowed!",
                            Toast.LENGTH_SHORT).show();
                }
                else {

                    try {
                        SecAddkycActivity.MyTask task = new SecAddkycActivity.MyTask();
                        String resStr = task.execute(null, null, null).get();
                        JSONObject resObj = new JSONObject(resStr);
                        String opstr = (String) resObj.get("Status");
                        if (resObj.has("error")) {
                            String error = (String) resObj.getString("error");
                            opstr = opstr + error;
                            makeText(SecAddkycActivity.this, opstr, LENGTH_LONG).show();
                        } else {
                            makeText(SecAddkycActivity.this, opstr, LENGTH_LONG).show();
                        }

                        Toast.makeText(SecAddkycActivity.this, opstr, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SecAddkycActivity.this, SecHomeActivity.class).putExtra("topic", topic).putExtra("status", opstr));


                    } catch (Exception e) {
                    }
                }
            }
        });

        ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView7);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(SecAddkycActivity.this, IndBlogActivity.class);
                webviewIntent.putExtra("URL",Constants.s_kyc_blog);
                startActivity(webviewIntent);
            }
        });

        ImageView iv = (ImageView) findViewById(R.id.imageView127);
        topicName = (TextView) findViewById(R.id.textView30);
        switch (topic){
            case "xss":
                iv.setImageResource(R.drawable.xm);
                topicName.setText(("Mitigation for Cross Site Scripting Attack"));
                break;
            case "ids":
                iv.setImageResource(R.drawable.idsm);
                topicName.setText(("Mitigation for Insecure Data Storage"));
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void loadData() {
        DBHelper.getInstance(SecAddkycActivity.this);
        HashMap vals = myDb.loadData();
        String status = (String) vals.get("Status");
        if (status.equals("1")) {

            editFull_Name.setText((String) vals.get(myDb.FULL_NAME));
            editPAN_Number.setText((String) vals.get(myDb.PAN_NUMBER));
            editAddress.setText((String) vals.get(myDb.ADDRESS));
            editEmployment_type.setText((String) vals.get(myDb.EMPLOYMENT_TYPE));
            editDesignationOccupation.setText((String) vals.get(myDb.DESIGNATION_OCCUPATION));
            editSalary.setText((int) vals.get(myDb.SALARY) + "");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addData() {
        DBHelper.getInstance(SecAddkycActivity.this);
        String fname = editFull_Name.getText().toString();
        String pannum = editPAN_Number.getText().toString();
        String address = editAddress.getText().toString();
        String emp_type = editEmployment_type.getText().toString();
        String desgn = editDesignationOccupation.getText().toString();
        String sal = editSalary.getText().toString();
        boolean isInserted = myDb.insertData(fname, pannum, address, emp_type, desgn, sal);
        if (isInserted == true) {

        } else {
        }
        }


    @Override
    public void onBackPressed() { }
    }
