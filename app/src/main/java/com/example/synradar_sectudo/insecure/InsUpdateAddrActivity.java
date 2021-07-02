package com.example.synradar_sectudo.insecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
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
import com.example.synradar_sectudo.helper.EncryptionUtil;
import com.example.synradar_sectudo.helper.GeneralUtils;
import com.example.synradar_sectudo.learningmodule.CongratsActivity;
import com.example.synradar_sectudo.learningmodule.LearningTabActivity;
import com.example.synradar_sectudo.learningmodule.QuestionActivity;
import com.example.synradar_sectudo.learningmodule.RetryActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsUpdateAddrActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editFull_Name, editPAN_Number, editAddress;
    Button btnSave;
    Button btnResult;
    String topic;
    TextView topicName;
    WebView webView;
    TextView status;
    String token;
    String status_val;

    private class MyTask extends AsyncTask<String,String,String> {
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

            String fname= editFull_Name.getText().toString();
            String pannum = editPAN_Number.getText().toString();
            String address= editAddress.getText().toString();


            JSONObject reqjson = new JSONObject();
            URL url;
            try {

                reqjson.put("fullname", fname);
                reqjson.put("address", address);
                reqjson.put("pannumber", pannum);


                url = new URL(Constants.update);


                String rndtoken = EncryptionUtil.getRandomToken();
                String param = EncryptionUtil.AsymEncrypt(rndtoken);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "text/html");
                con.setRequestProperty("Authorization", "Bearer " + token);
                con.setRequestProperty("param", param);
                con.setDoOutput(true);
                OutputStream out = con.getOutputStream();
              out.write(reqjson.toString().getBytes());

                Map<String, List<String>> map = con.getHeaderFields();
                List<String> status_list = map.get("status");

                for (String status : status_list) {
                   // System.out.println("status" + status);

                    status_val = EncryptionUtil.symDecrypt(status, rndtoken);
                }


                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tempstr;
                while ((tempstr = in.readLine()) != null) {
                  //  System.out.println(tempstr);
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
        setContentView(R.layout.activity_ins_update_addr);

        topic = getIntent().getStringExtra("topic");
        topicName = (TextView) findViewById(R.id.textView33ua);
        switch (topic) {
            case "xss":
                topicName.setText(("Task for Cross Site Scripting Attack"));
                break;
        }

        myDb = new DatabaseHelper(this);

        editFull_Name = (EditText) findViewById(R.id.e1ua);
        editPAN_Number = (EditText) findViewById(R.id.e2ua);
        editAddress = (EditText) findViewById(R.id.e3ua);

        loadData();

        status= (TextView) findViewById(R.id.update);
        webView= (WebView) findViewById(R.id.webViewua);
        btnSave = (Button) findViewById(R.id.buttonua);
        btnResult = (Button) findViewById(R.id.buttonvr);

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (TextUtils.isEmpty(editFull_Name.getText().toString()) && TextUtils.isEmpty(editPAN_Number.getText().toString())
                        && TextUtils.isEmpty(editAddress.getText().toString())
                        )
                {
                    Toast.makeText(InsUpdateAddrActivity.this,
                            "Empty field not allowed!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        InsUpdateAddrActivity.MyTask task = new InsUpdateAddrActivity.MyTask();
                        String resStr = task.execute().get();
                       // System.out.println(resStr);

                        webView.setVisibility(View.VISIBLE);
                        btnResult.setVisibility(View.VISIBLE);
                        btnSave.setVisibility(View.INVISIBLE);

                        webView.setWebChromeClient(new WebChromeClient() {
                        });

                        webView.getSettings().setBuiltInZoomControls(true);
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.getSettings().setDomStorageEnabled(true);
                        webView.getSettings().setDatabaseEnabled(true);
                        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                        webView.getSettings().setAppCacheEnabled(true);
                        webView.loadData(resStr, "text/html", "UTF-8");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnResult.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if("true".equals(status_val)){


                    startActivity(new Intent(InsUpdateAddrActivity.this, CongratsActivity.class).putExtra("topic", topic));
                }
                else{
                    startActivity(new Intent(InsUpdateAddrActivity.this, RetryActivity.class).putExtra("topic", topic));
                }
            }
        });


        ImageView tpn;
        tpn = (ImageView) findViewById(R.id.imageViewtp);
        tpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InsUpdateAddrActivity.this, MainNewActivity.class).putExtra("irs","back"));
            }});
        ImageView bck;
        bck = (ImageView) findViewById(R.id.imageViewbk);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InsUpdateAddrActivity.this, LearningTabActivity.class).putExtra("topic",topic));
            }});

        ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView31ua);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(InsUpdateAddrActivity.this, IndBlogActivity.class);
                webviewIntent.putExtra("URL", Constants.view_blog);
                startActivity(webviewIntent);
            }
        });
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
        }
    }

}
