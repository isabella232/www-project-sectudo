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
import com.example.synradar_sectudo.helper.EncryptionUtil;
import com.example.synradar_sectudo.helper.GeneralUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SecCheckEligibilityActivity extends AppCompatActivity {
    private TextView textview7;
    private TextView textview10;
    Button back;

    class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strs) {
            String responsestr = new String();
            String opStr = new String();
            SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
            String token = GeneralUtils.readSharedPrefs(prefs, "session_token");


            JSONObject reqjson = new JSONObject();
            URL url;
            try {
                url = new URL(Constants.s_getEligibility);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                String s_token = EncryptionUtil.getRandomToken();
                reqjson.put("token", s_token);

                String plainJson = reqjson.toString();
                String encryptedJson = EncryptionUtil.AsymEncrypt(plainJson);

                if (Constants.session_mode.equals("JSE")) {
                    con.setRequestProperty("Cookie", "JSESSIONID=" + token);
                } else {
                    con.setRequestProperty("Authorization", "Bearer " + token);

                }

                con.setDoOutput(true);
                OutputStream out = con.getOutputStream();
                out.write(encryptedJson.getBytes());


                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tempstr;
                while ((tempstr = in.readLine()) != null) {

                    responsestr = tempstr;
                }

                String plainRes = EncryptionUtil.symDecrypt(responsestr, s_token);
                opStr = plainRes;

                in.close();
            } catch (Exception e) {

                e.printStackTrace();
                opStr = "Error Occurred";
            }
            return opStr;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);

        String check_token = GeneralUtils.readSharedPrefs(prefs, "session_token");
        System.out.println("On Resume"+check_token);
        if(check_token == null || check_token.equals("")){

            Toast.makeText(SecCheckEligibilityActivity.this, "Invalid Access", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SecCheckEligibilityActivity.this, SecLoginActivity.class));
            return;
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);

        String check_token = GeneralUtils.readSharedPrefs(prefs, "session_token");
        if(check_token == null || check_token.equals("")){
            Toast.makeText(SecCheckEligibilityActivity.this, "Invalid Access", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SecCheckEligibilityActivity.this, SecLoginActivity.class));
            return;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_check_eligibility);

        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
        String check_token = GeneralUtils.readSharedPrefs(prefs, "session_token");
        if(check_token == null || check_token.equals("")){
            Toast.makeText(SecCheckEligibilityActivity.this, "Invalid Access", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SecCheckEligibilityActivity.this, SecLoginActivity.class));
            return;
        }

        back = (Button) findViewById(R.id.button41);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SecCheckEligibilityActivity.this, SecHomeActivity.class));
            }
        });

        try {
            SecCheckEligibilityActivity.MyTask task = new SecCheckEligibilityActivity.MyTask();
            String resStr = task.execute().get();

            if(!(resStr.toLowerCase().contains("error")) ){
                JSONObject resObj = new JSONObject(resStr);
                String op = (String) resObj.get("status");
                String c_type = (String) resObj.get("card_type");



                textview7 = (TextView) findViewById(R.id.textViews7);
                textview7.setText(resObj.getString("status"));
                textview10 = (TextView) findViewById(R.id.textViews10);
                textview10.setText(resObj.getString("card_type"));
            } else{
                //Add Toast
                Toast.makeText(SecCheckEligibilityActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView10);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(SecCheckEligibilityActivity.this, IndBlogActivity.class);
                webviewIntent.putExtra("URL",Constants.s_check_blog);
                startActivity(webviewIntent);
            }
        });
    }
}
