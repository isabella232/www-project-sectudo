package com.example.synradar_sectudo.insecure;

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

import com.example.synradar_sectudo.IndBlogActivity;
import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.helper.GeneralUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsViewActivity extends AppCompatActivity {
    WebView webView;
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
                url = new URL(Constants.showkyc);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "text/html");
                con.setRequestProperty("Authorization", "Bearer " + token);

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_view);

        back = (Button) findViewById(R.id.button5);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(InsViewActivity.this, InsHomeActivity.class));
            }
        });

        try {
            InsViewActivity.MyTask task = new InsViewActivity.MyTask();
            String resStr = task.execute().get();
            System.out.println(resStr);
            webView=(WebView)findViewById(R.id.web1);

            webView.setWebChromeClient(new WebChromeClient(){
            });

            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setDatabaseEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.getSettings().setAppCacheEnabled(true);
            webView.loadData(resStr, "text/html", "UTF-8");


        }catch (Exception e) {
            e.printStackTrace();
        }

        ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView15);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(InsViewActivity.this, IndBlogActivity.class);
                webviewIntent.putExtra("URL", Constants.view_blog);
                startActivity(webviewIntent);
            }
        });
    }

}
