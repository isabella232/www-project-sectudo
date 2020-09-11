package com.example.synradar_sectudo.secure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.synradar_sectudo.IndBlogActivity;
import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.helper.DBUtils;
import com.example.synradar_sectudo.helper.GeneralUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SecHomeActivity extends AppCompatActivity {
    private TextView textviews4;
    Button sec_view;
    Button sec_show;
    Button sec_chk;
    Button sec_kyc1;
    Button sec_logout;

    class MyTask extends AsyncTask<String, String, String> {

        protected String doInBackground(String... strs) {
            URL url;

            String responsestr = new String();
            SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
            String token = GeneralUtils.readSharedPrefs(prefs, "session_token");

            try {

                url = new URL(Constants.s_logout);



                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");

                //con.setRequestProperty("Content-Type", "application/json");

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

                  //System.out.println(tempstr);

                    responsestr = tempstr;

                }

                //System.out.println(responsestr);

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
        System.out.println("On Resume"+check_token);
        if(check_token == null || check_token.equals("")){

            Toast.makeText(SecHomeActivity.this, "Invalid Access", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SecHomeActivity.this, SecLoginActivity.class));
            return;
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);

        String check_token = GeneralUtils.readSharedPrefs(prefs, "session_token");
        if(check_token == null || check_token.equals("")){
            Toast.makeText(SecHomeActivity.this, "Invalid Access", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SecHomeActivity.this, SecLoginActivity.class));
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_home);

        textviews4 = (TextView) findViewById(R.id.textViews4);
        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
        textviews4.setText((" Welcome " +GeneralUtils.readSharedPrefs(prefs, "usernames") + " to Secure KYC Portal"));

        String check_token = GeneralUtils.readSharedPrefs(prefs, "session_token");
        if(check_token == null || check_token.equals("")){

            Toast.makeText(SecHomeActivity.this, "Invalid Access", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SecHomeActivity.this, SecLoginActivity.class));
            return;
        }


        sec_kyc1= (Button) findViewById(R.id.button32);
        sec_kyc1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SecHomeActivity.this, SecAddkycActivity.class));
            }
        });

        sec_view= (Button) findViewById(R.id.button33);
        sec_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SecHomeActivity.this, SecViewActivity.class));
            }
        });

        sec_show= (Button) findViewById(R.id.button34);
        sec_show.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SecHomeActivity.this, SecShowDetailsActivity.class));
            }
        });

        sec_chk= (Button) findViewById(R.id.button35);
        sec_chk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SecHomeActivity.this, SecCheckEligibilityActivity.class));
            }
        });

        sec_logout= (Button) findViewById(R.id.button36);
        sec_logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String resStr = new String();




                try {
                    SecHomeActivity.MyTask task = new SecHomeActivity.MyTask();
                    resStr = (String) task.execute(null, null, null).get();

                    System.out.println(resStr);
                    JSONObject rs = new JSONObject(resStr);
                    if(rs.has("status")) {
                        String status = (String) rs.get("status");
                        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear().apply();
                        Toast.makeText(SecHomeActivity.this, status, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SecHomeActivity.this, SecLoginActivity.class));
                    }
                   // resStr = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView6);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(SecHomeActivity.this, IndBlogActivity.class);
                webviewIntent.putExtra("URL",Constants.s_home_blog);
                startActivity(webviewIntent);
            }
        });
    }
}