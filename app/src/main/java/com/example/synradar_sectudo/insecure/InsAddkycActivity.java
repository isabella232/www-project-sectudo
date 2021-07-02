package com.example.synradar_sectudo.insecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.example.synradar_sectudo.helper.DatabaseHelper;
import com.example.synradar_sectudo.helper.GeneralUtils;
import com.example.synradar_sectudo.learningmodule.LearningTabActivity;
import com.example.synradar_sectudo.learningmodule.QuestionActivity;

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

    String topic ;
    TextView topicName;
    ImageView iv;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_add_kyc);

         topic = getIntent().getStringExtra("topic");

        ImageView tpn;
        tpn = (ImageView) findViewById(R.id.imageViewtp);
        tpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InsAddkycActivity.this, MainNewActivity.class).putExtra("irs","back"));
            }});
        ImageView bck;
        bck = (ImageView) findViewById(R.id.imageViewbk);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InsAddkycActivity.this, LearningTabActivity.class).putExtra("topic",topic));
            }});

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
                if (TextUtils.isEmpty(editFull_Name.getText().toString()) && TextUtils.isEmpty(editPAN_Number.getText().toString()) && TextUtils.isEmpty(editEmployment_type.getText().toString())
                        && TextUtils.isEmpty(editDesignationOccupation.getText().toString())  && TextUtils.isEmpty(editSalary.getText().toString()))
                {
                    Toast.makeText(InsAddkycActivity.this,
                            "Empty field not allowed!",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {

                    addData();
                    startActivity(new Intent(InsAddkycActivity.this, QuestionActivity.class).putExtra("topic", topic));
                }

            }
        });

       ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView31ua);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(InsAddkycActivity.this, IndBlogActivity.class);
                webviewIntent.putExtra("URL", Constants.kyc_blog);
                startActivity(webviewIntent);
            }
        });

        iv = (ImageView) findViewById(R.id.imageView125);

        topicName = (TextView) findViewById(R.id.textView33);
        switch (topic){
            case "xss":
                topicName.setText(("Task for Cross Site Scripting Attack"));
                iv.setImageResource(R.drawable.ia2);
                break;
            case "ids":
                topicName.setText(("Task for Insecure Data Storage"));
                break;
        }
    }
    @Override
    public void onBackPressed() { }

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
