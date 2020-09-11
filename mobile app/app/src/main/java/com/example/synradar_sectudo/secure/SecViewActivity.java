package com.example.synradar_sectudo.secure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.synradar_sectudo.IndBlogActivity;
import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.helper.GeneralUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SecViewActivity extends AppCompatActivity {
    WebView webViews;
    Button back;

    class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strs) {
            String responsestr = new String();
            SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
            String token = GeneralUtils.readSharedPrefs(prefs, "session_token");

            JSONObject reqjson = new JSONObject();
            URL url;
            try {
                url = new URL(Constants.s_showKyc);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "text/html");

                if (Constants.session_mode.equals("JSE")) {
                    con.setRequestProperty("Cookie", "JSESSIONID=" + token);
                } else {
                    con.setRequestProperty("Authorization", "Bearer " + token);

                }

                con.setDoOutput(false);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tempstr;
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

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);

        String check_token = GeneralUtils.readSharedPrefs(prefs, "session_token");
        System.out.println("On Resume"+check_token);
        if(check_token == null || check_token.equals("")){

            Toast.makeText(SecViewActivity.this, "Invalid Access", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SecViewActivity.this, SecLoginActivity.class));
            return;
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);

        String check_token = GeneralUtils.readSharedPrefs(prefs, "session_token");
        if(check_token == null || check_token.equals("")){
            Toast.makeText(SecViewActivity.this, "Invalid Access", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SecViewActivity.this, SecLoginActivity.class));
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_view);

        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
        String check_token = GeneralUtils.readSharedPrefs(prefs, "session_token");
        if(check_token == null || check_token.equals("")){
            Toast.makeText(SecViewActivity.this, "Invalid Access", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SecViewActivity.this, SecLoginActivity.class));
            return;
        }


        back = (Button) findViewById(R.id.button39);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SecViewActivity.this, SecHomeActivity.class));
            }
        });

        try {
            SecViewActivity.MyTask task = new SecViewActivity.MyTask();
            String resStr = task.execute().get();
            System.out.println(resStr);
            webViews=(WebView)findViewById(R.id.webs1);

            webViews.setWebChromeClient(new WebChromeClient(){
            });

            webViews.getSettings().setBuiltInZoomControls(true);
            webViews.getSettings().setJavaScriptEnabled(true);
            webViews.getSettings().setDomStorageEnabled(true);
            webViews.getSettings().setDatabaseEnabled(true);
            webViews.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webViews.getSettings().setAppCacheEnabled(true);
            webViews.loadData(resStr, "text/html", "UTF-8");


        }catch (Exception e) {
            e.printStackTrace();
        }

        ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView8);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(SecViewActivity.this, IndBlogActivity.class);
                webviewIntent.putExtra("URL",Constants.s_view_blog);
                startActivity(webviewIntent);
            }
        });
    }
}
