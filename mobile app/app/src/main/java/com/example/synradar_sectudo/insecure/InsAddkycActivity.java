package com.example.synradar_sectudo.insecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.synradar_sectudo.IndBlogActivity;
import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.helper.DatabaseHelper;
import com.example.synradar_sectudo.helper.GeneralUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class InsAddkycActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editFull_Name, editPAN_Number, editAddress, editEmployment_type, editDesignationOccupation, editSalary;
    Button btnSave;
    Button back;

    private class MyTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strs) {
            String responsestr = new String();
            SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
            String token = GeneralUtils.readSharedPrefs(prefs, "session_token");

            String fname= editFull_Name.getText().toString();
            String pannum = editPAN_Number.getText().toString();
            String address= editAddress.getText().toString();
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

                url = new URL(Constants.addkycURL);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Authorization", "Bearer " + token);

                con.setDoOutput(true);
                OutputStream out = con.getOutputStream();
                out.write(reqjson.toString().getBytes());

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tempstr;
                // String responsestr = new String();
                while ((tempstr = in.readLine()) != null) {
                    System.out.println(tempstr);
                    responsestr = tempstr;
                }

                //  addData();

                if(responsestr.contains("Successfully")) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_add_kyc);
        back = (Button) findViewById(R.id.button12);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(InsAddkycActivity.this, InsHomeActivity.class));
            }
        });

        myDb = new DatabaseHelper(this);

        editFull_Name = (EditText) findViewById(R.id.e1);
        editPAN_Number = (EditText) findViewById(R.id.e2);
        editAddress = (EditText) findViewById(R.id.e3);
        editEmployment_type = (EditText) findViewById(R.id.e4);
        editDesignationOccupation = (EditText) findViewById(R.id.e5);
        editSalary = (EditText) findViewById(R.id.e6);
        btnSave = (Button) findViewById(R.id.button10);
        // AddData();
        loadData();
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try{
                    InsAddkycActivity.MyTask task = new InsAddkycActivity.MyTask();
                    String resStr = task.execute(null, null, null).get();
                    JSONObject resObj = new JSONObject(resStr);
                    String opstr = (String) resObj.get("Status");
                    Toast.makeText(InsAddkycActivity.this, opstr, Toast.LENGTH_LONG).show();
                } catch(Exception e){
                }
            }
        });

        ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView12);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(InsAddkycActivity.this, IndBlogActivity.class);
                webviewIntent.putExtra("URL", Constants.kyc_blog);
                startActivity(webviewIntent);
            }
        });
    }

    private void loadData() {
        HashMap vals = myDb.loadData();
        String status = (String) vals.get("Status");
        if(status.equals("1")) {

            editFull_Name.setText((String) vals.get(myDb.FULL_NAME));
            editPAN_Number.setText((String) vals.get(myDb.PAN_NUMBER));
            editAddress.setText((String) vals.get(myDb.ADDRESS));
            editEmployment_type.setText((String) vals.get(myDb.EMPLOYMENT_TYPE));
            editDesignationOccupation.setText((String) vals.get(myDb.DESIGNATION_OCCUPATION));
            editSalary.setText((int) vals.get(myDb.SALARY) + "");
        }
    }


    public void addData() {
        String fname = editFull_Name.getText().toString();
        String pannum = editPAN_Number.getText().toString();
        String address = editAddress.getText().toString();
        String emp_type = editEmployment_type.getText().toString();
        String desgn = editDesignationOccupation.getText().toString();
        String sal = editSalary.getText().toString();
        boolean isInserted = myDb.insertData(fname, pannum, address, emp_type, desgn, sal);
        if(isInserted = true)
            Toast.makeText(InsAddkycActivity.this,"Data Saved Successfully",Toast.LENGTH_LONG) .show();
        else
            Toast.makeText(InsAddkycActivity.this,"Something went wrong",Toast.LENGTH_LONG) .show();
    }
}
