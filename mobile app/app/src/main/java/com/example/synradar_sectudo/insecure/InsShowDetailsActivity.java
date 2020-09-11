package com.example.synradar_sectudo.insecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.synradar_sectudo.IndBlogActivity;
import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.helper.GeneralUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsShowDetailsActivity extends AppCompatActivity {

    Button back;
    String tokenstr;
    String TAG = "Log";
    private TextView textview12;
    private TextView textview13;
    private TextView textview14;



    private class MyTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strs){
            String responsestr = new String();
            SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
            String token = GeneralUtils.readSharedPrefs(prefs, "session_token");
            JSONObject reqjson = new JSONObject();
            URL url;
            try {
                url = new URL(Constants.getAcctSumURL);


                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Authorization", "Bearer " + token);

                con.setDoOutput(false);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tempstr;
                //responsestr = new String();
                while ((tempstr = in.readLine()) != null) {
                    System.out.println(tempstr);
                    responsestr = tempstr;
                }


                in.close();
            } catch (Exception e) {

                e.printStackTrace();
            }
            return responsestr;
        }
    }

    private class GetAcctTask extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strs){
            String responsestr = new String();
            SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
            String token = GeneralUtils.readSharedPrefs(prefs, "session_token");
            JSONObject reqjson = new JSONObject();
            URL url;
            try {
                url = new URL(Constants.getAcctURL+ strs[0]);


                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Authorization", "Bearer " + token);

                con.setDoOutput(false);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tempstr;
                //responsestr = new String();
                while ((tempstr = in.readLine()) != null) {
                    System.out.println(tempstr);
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
        LinearLayout mRlayout = (LinearLayout) findViewById(R.id.parent);
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

                    GetAcctTask task = new GetAcctTask();
                    String opStr = task.execute(acct_id).get();
                    JSONObject jsonObj = new JSONObject(opStr);
                    String act_name  = jsonObj.getString("account_name");
                    String act_balance = jsonObj.getString("account_balance");

                    // System.out.println(" Your Account Name is : - "  + act_name + " and your balance is " + act_balance);




                    //acct_details_view.setText(opStr);
                    acct_details_view.setText("Your Account Name is : -"  +act_name +" and your balance is " +act_balance);
                }catch(Exception e){
                    Log.e(TAG, "Received an exception " + e.getMessage() );
                }
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_show_details);
        back = (Button) findViewById(R.id.button11);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(InsShowDetailsActivity.this, InsHomeActivity.class));
            }
        });


        //vw = (Button) findViewById(R.id.button9);

        try{
            MyTask task = new MyTask();
            String resStr = task.execute().get();
            System.out.println(resStr);


            Log.i(resStr, "Response from server");

            JSONArray resObjarr = new JSONArray(resStr);
            for(int i = 0; i < resObjarr.length(); i++)
            {
                JSONObject jsonObj = resObjarr.getJSONObject(i);
                String acct_id = jsonObj.getString("account_id");
                System.out.println(i+1+" - "+acct_id);
                showAcctDetails(acct_id, i+1);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView13);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(InsShowDetailsActivity.this, IndBlogActivity.class);
                webviewIntent.putExtra("URL", Constants.show_blog);
                startActivity(webviewIntent);
            }
        });
    }



}