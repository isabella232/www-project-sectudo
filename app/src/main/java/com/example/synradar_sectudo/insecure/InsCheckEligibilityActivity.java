package com.example.synradar_sectudo.insecure;

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
import com.example.synradar_sectudo.learningmodule.CongratsActivity;
import com.example.synradar_sectudo.learningmodule.RetryActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsCheckEligibilityActivity extends AppCompatActivity {
    private TextView textview7;
    private TextView textview10;
    Button back;
    String topic;

    class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strs) {
            String responsestr = new String();
            SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
            String token = GeneralUtils.readSharedPrefs(prefs, "session_token");

            JSONObject reqjson = new JSONObject();
            URL url;
            try {
                url = new URL(Constants.getEligibility);


                String param = EncryptionUtil.AsymEncrypt(strs[0]);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Authorization", "Bearer " + token);
                con.setRequestProperty("param", param);
                con.setDoOutput(false);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tempstr;
                while ((tempstr = in.readLine()) != null) {
                    // System.out.println(tempstr);
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
        setContentView(R.layout.activity_ins_check_eligibility);
        topic = getIntent().getStringExtra("topic");

        back = (Button) findViewById(R.id.button9);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(InsCheckEligibilityActivity.this, InsHomeActivity.class));
            }
        });
        topic = getIntent().getStringExtra("topic");

        textview7 = (TextView) findViewById(R.id.textView7);
        textview10 = (TextView) findViewById(R.id.textView10);
        try {
            InsCheckEligibilityActivity.MyTask task = new InsCheckEligibilityActivity.MyTask();
            String rndtoken = EncryptionUtil.getRandomToken();
            String resStr = task.execute(rndtoken).get();

            JSONObject jsonObj = new JSONObject(resStr);




            String encuser = jsonObj.getString("user");
            String user = EncryptionUtil.symDecrypt(encuser,rndtoken);
            Toast.makeText(InsCheckEligibilityActivity.this, user, Toast.LENGTH_LONG).show();


            SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
            String uname = GeneralUtils.readSharedPrefs(prefs, "username");

            String st = jsonObj.getString("status");
            String ct = jsonObj.getString("card_type");
            if(uname.equals(user)) {


                startActivity(new Intent(InsCheckEligibilityActivity.this, RetryActivity.class).putExtra("topic", topic).putExtra("st", st).putExtra("ct", ct).putExtra("user", user ));
            }
            else{


                startActivity(new Intent(InsCheckEligibilityActivity.this, CongratsActivity.class).putExtra("topic", topic).putExtra("st", st).putExtra("ct", ct).putExtra("user", user ));
            }
        }catch (Exception e) {
            e.printStackTrace();

        }

        ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView14);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(InsCheckEligibilityActivity.this, IndBlogActivity.class);
                webviewIntent.putExtra("URL", Constants.check_blog);
                startActivity(webviewIntent);
            }
        });
    }
}
