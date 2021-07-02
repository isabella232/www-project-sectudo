package com.example.synradar_sectudo.insecure;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.synradar_sectudo.IndBlogActivity;
import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.helper.EncryptionUtil;
import com.example.synradar_sectudo.helper.GeneralUtils;
import com.example.synradar_sectudo.learningmodule.CongratsActivity;
import com.example.synradar_sectudo.learningmodule.RetryActivity;
import com.example.synradar_sectudo.secure.SecLoginActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsHomeActivity extends AppCompatActivity {
    private TextView textview4;
    Button view;
    Button show;
    Button chk;
    Button kyc1;
    Button logout;
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
        setContentView(R.layout.activity_ins_home);
        textview4 = (TextView) findViewById(R.id.textView4);
        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
        textview4.setText((" Welcome " +GeneralUtils.readSharedPrefs(prefs, "username") + " to Insecure KYC Portal"));

        kyc1= (Button) findViewById(R.id.buttona);
        view= (Button) findViewById(R.id.button6);
        show= (Button) findViewById(R.id.button7);
        chk= (Button) findViewById(R.id.button8);


        topic = getIntent().getStringExtra("topic");

        switch (topic) {
            case "ism":
            case "ia":
                kyc1.setEnabled(false);
                //chk.setEnabled(false);
                show.setEnabled(false);
                view.setEnabled(false);

                break;
        }



        kyc1= (Button) findViewById(R.id.buttona);
        kyc1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             //   String chlng = getIntent().getStringExtra("chlng");
                startActivity(new Intent(InsHomeActivity.this, InsAddkycActivity.class).putExtra("topic", topic));
            }
        });

        view= (Button) findViewById(R.id.button6);
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(InsHomeActivity.this, InsViewActivity.class));
            }
        });

        show= (Button) findViewById(R.id.button7);
        show.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // String chlng = getIntent().getStringExtra("chlng");
                startActivity(new Intent(InsHomeActivity.this, InsShowDetailsActivity.class).putExtra("topic",topic));
            }
        });

        chk= (Button) findViewById(R.id.button8);
        chk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    InsHomeActivity.MyTask task = new InsHomeActivity.MyTask();
                    String rndtoken = EncryptionUtil.getRandomToken();
                    String resStr = task.execute(rndtoken).get();

                    JSONObject jsonObj = new JSONObject(resStr);




                    String encuser = jsonObj.getString("user");
                    String user = EncryptionUtil.symDecrypt(encuser,rndtoken);



                    SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
                    String uname = GeneralUtils.readSharedPrefs(prefs, "username");

                    String st = jsonObj.getString("status");
                    String ct = jsonObj.getString("card_type");
                    if(uname.equals(user)) {


                        startActivity(new Intent(InsHomeActivity.this, RetryActivity.class).putExtra("topic", topic).putExtra("st", st).putExtra("ct", ct).putExtra("user", user ));
                    }
                    else{


                        startActivity(new Intent(InsHomeActivity.this, CongratsActivity.class).putExtra("topic", topic).putExtra("st", st).putExtra("ct", ct).putExtra("user", user ));
                    }
                }catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });



        logout= (Button) findViewById(R.id.button14);
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //String chlng = getIntent().getStringExtra("chlng");
                startActivity(new Intent(InsHomeActivity.this, InsLoginActivity.class).putExtra("topic",topic));
                SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("session_token");
                editor.remove("username");
                editor.commit();
                String st= GeneralUtils.readSharedPrefs(prefs, "username");
                String u = GeneralUtils.readSharedPrefs(prefs, "session_token");
                String ins = GeneralUtils.readSharedPrefs(prefs, "session");

               // editor.clear().apply();
            }
        });


        ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView11);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(InsHomeActivity.this, IndBlogActivity.class);
                webviewIntent.putExtra("URL", Constants.home_blog);
                startActivity(webviewIntent);
            }
        });



    }
}
