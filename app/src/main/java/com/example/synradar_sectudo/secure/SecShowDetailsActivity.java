package com.example.synradar_sectudo.secure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.synradar_sectudo.IndBlogActivity;
import com.example.synradar_sectudo.MainNewActivity;
import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.helper.GeneralUtils;
import com.example.synradar_sectudo.learningmodule.LearningTabActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SecShowDetailsActivity extends AppCompatActivity {
    String tokenstrs;
    private TextView textviews12;
    private TextView textviews13;
    private TextView textviews14;
    String topic;
    TextView topicName;




    private class MysTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strs){
            String responsestr = new String();
            SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
            String token = GeneralUtils.readSharedPrefs(prefs, "session_token");

            JSONObject reqjson = new JSONObject();
            URL url;
            try {

                url = new URL(Constants.s_getAcctSumURL);


                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                if (Constants.session_mode.equals("JSE")) {
                    con.setRequestProperty("Cookie", "JSESSIONID=" + token);
                } else {
                    con.setRequestProperty("Authorization", "Bearer " + token);

                }

                con.setDoOutput(false);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tempstr;

                while ((tempstr = in.readLine()) != null) {

                    responsestr = tempstr;
                }


                in.close();
            } catch (Exception e) {

                e.printStackTrace();
            }
            return responsestr;
        }
    }

    private class GetsAcctTask extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strs){
            String responsestr = new String();
            SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
            String token = GeneralUtils.readSharedPrefs(prefs, "session_token");



            JSONObject reqjson = new JSONObject();
            String acct_id = strs[0];
            URL url;
            try {
                url = new URL(Constants.s_getAcctURL+acct_id);


                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");


                if (Constants.session_mode.equals("JSE")) {
                    con.setRequestProperty("Cookie", "JSESSIONID=" + token);
                } else {
                    con.setRequestProperty("Authorization", "Bearer " + token);

                }

                con.setDoOutput(false);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tempstr;
                while ((tempstr = in.readLine()) != null) {

                    responsestr = tempstr;
                }


                in.close();
            } catch (Exception e) {

                e.printStackTrace();
            }
            return responsestr;
        }
    }




    protected void showAcctDetails(final String acct_id, int counter){
        LinearLayout mRlayout = (LinearLayout) findViewById(R.id.parents);
        LinearLayout.LayoutParams mRparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        TextView acct_id_view = new TextView(this);
        acct_id_view.setText(counter+". "+"Account Number: "+acct_id);

        acct_id_view.setLayoutParams(mRparams);
        mRlayout.addView(acct_id_view);



        Button btnshow_details = new Button(this);
        btnshow_details.setLayoutParams(mRparams);
        btnshow_details.setText("View Details ");


        mRlayout.addView(btnshow_details);

        final TextView acct_details_view = new TextView(this);
        acct_details_view.setLayoutParams(mRparams);
        mRlayout.addView(acct_details_view);

        btnshow_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    GetsAcctTask task = new GetsAcctTask();
                    String opStr = task.execute(acct_id).get();
                    JSONObject jsonObj = new JSONObject(opStr);
                    String act_name  = jsonObj.getString("account_name");
                    String act_balance = jsonObj.getString("account_balance");


                    acct_details_view.setText("Your Account Name is : -"  +act_name +" and your balance is " +act_balance);
                }catch(Exception e){

                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);

        String check_token = GeneralUtils.readSharedPrefs(prefs, "session_token");
       // System.out.println("On Resume"+check_token);
        if(check_token == null || check_token.equals("")){

            Toast.makeText(SecShowDetailsActivity.this, "Invalid Access", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SecShowDetailsActivity.this, SecLoginActivity.class));
            return;
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);

        String check_token = GeneralUtils.readSharedPrefs(prefs, "session_token");
        if(check_token == null || check_token.equals("")){
            Toast.makeText(SecShowDetailsActivity.this, "Invalid Access", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SecShowDetailsActivity.this, SecLoginActivity.class));
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_show_details);

        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
        String check_token = GeneralUtils.readSharedPrefs(prefs, "session_token");
        if(check_token == null || check_token.equals("")){
            Toast.makeText(SecShowDetailsActivity.this, "Invalid Access", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SecShowDetailsActivity.this, SecLoginActivity.class));
            return;
        }

        topic = getIntent().getStringExtra("topic");

        ImageView tpn;
        tpn = (ImageView) findViewById(R.id.imageViewtp);
        tpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecShowDetailsActivity.this, MainNewActivity.class).putExtra("irs","back"));
            }});
        ImageView bck;
        bck = (ImageView) findViewById(R.id.imageViewbk);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecShowDetailsActivity.this, LearningTabActivity.class).putExtra("topic",topic));
            }});

        try{
            MysTask task = new MysTask();
            String resStr = task.execute().get();




            JSONArray resObjarr = new JSONArray(resStr);
            for(int i = 0; i < resObjarr.length(); i++)
            {
                JSONObject jsonObj = resObjarr.getJSONObject(i);
                String acct_id = jsonObj.getString("account_id");

                showAcctDetails(acct_id, i+1);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView9);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(SecShowDetailsActivity.this, IndBlogActivity.class);
                webviewIntent.putExtra("URL",Constants.s_show_blog);
                startActivity(webviewIntent);
            }
        });

        topicName = (TextView) findViewById(R.id.textView31);
        switch (topic){
            case "sqli":
                topicName.setText(("Mitigation for Server Side - SQL Injection Attack"));
                break;
            case "ida":
                topicName.setText(("Mitigation for Insecure Data Access"));
                break;
        }
    }
    @Override
    public void onBackPressed() { }
}
