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
import android.widget.ImageView;

import com.example.synradar_sectudo.MainNewActivity;
import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.helper.EncryptionUtil;
import com.example.synradar_sectudo.helper.GeneralUtils;
import com.example.synradar_sectudo.learningmodule.CongratsActivity;
import com.example.synradar_sectudo.learningmodule.LearningTabActivity;
import com.example.synradar_sectudo.learningmodule.RetryActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class InsViewActivity extends AppCompatActivity {
    WebView webView;

    String status_val = "";
    String topic ="";
    String token;

    class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strs) {
            String responsestr = new String();
            SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
            String tk = GeneralUtils.readSharedPrefs(prefs, "session_token");
            if(tk==""){
                token = GeneralUtils.readSharedPrefs(prefs, "session");
            }
            else{
                token = GeneralUtils.readSharedPrefs(prefs, "session_token");
            }
            JSONObject reqjson = new JSONObject();
            URL url;
            try {
                url = new URL(Constants.showkyc);

                String rndtoken = EncryptionUtil.getRandomToken();
                String param = EncryptionUtil.AsymEncrypt(rndtoken);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "text/html");
                con.setRequestProperty("Authorization", "Bearer " + token);
                con.setRequestProperty("param", param);
                con.setDoOutput(false);

                Map<String, List<String>> map = con.getHeaderFields();

                List<String> status_list = map.get("status");

                for (String status : status_list) {


                    status_val = EncryptionUtil.symDecrypt(status, rndtoken);
                }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_view);


        topic = getIntent().getStringExtra("topic");

        ImageView tpn;
        tpn = (ImageView) findViewById(R.id.imageViewtp);
        tpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InsViewActivity.this, MainNewActivity.class).putExtra("irs","back"));
            }});
        ImageView bck;
        bck = (ImageView) findViewById(R.id.imageViewbk);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InsViewActivity.this, LearningTabActivity.class).putExtra("topic",topic));
            }});

        try {
            InsViewActivity.MyTask task = new InsViewActivity.MyTask();
            String resStr = task.execute().get();


           // System.out.println(resStr);
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


    }
    @Override
    public void onBackPressed() { }

    @Override
    protected void onPause() {
        super.onPause();
        if("true".equals(status_val)){


           startActivity(new Intent(InsViewActivity.this, CongratsActivity.class).putExtra("abc", "").putExtra("topic", topic));
            }
            else{
                startActivity(new Intent(InsViewActivity.this, RetryActivity.class).putExtra("topic", topic));
            }
    }

}
