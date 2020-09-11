package com.example.synradar_sectudo.secure;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.synradar_sectudo.IndBlogActivity;
import com.example.synradar_sectudo.MainActivity;
import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.helper.EncryptionUtil;
import com.example.synradar_sectudo.helper.GeneralUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SecLoginActivity extends AppCompatActivity {
    Button mains;
    Button logins;
    EditText unames;
    EditText passs;
    Switch switch1;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH1 = "switch1";
    String tokenstrs;
    String enc_mode;

    ImageButton imgbtn;

    class MyTask extends AsyncTask<String, String, String> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... strs) {

            String uname = strs[0];
            String pass = strs[1];
            String responsestr = new String();
            String opstr = new String();
            String enc_mode = strs[2];

            JSONObject reqjson = new JSONObject();
            URL url;
            try {

                String token = EncryptionUtil.getRandomToken();

                //String token = EncryptionUtil.getSecureToken();

                String passHash = EncryptionUtil.generateMessageDigest(pass);
                String finalpassHash = EncryptionUtil.generateMessageDigest(passHash + token);

                reqjson.put("username", uname);
                reqjson.put("password", finalpassHash);
                reqjson.put("token", EncryptionUtil.encode(token));


                url = new URL(Constants.s_loginURL + "?enc_mode=" + enc_mode);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                OutputStream out = con.getOutputStream();


                String plainreq = reqjson.toString();
                String opReq = "";


                if (enc_mode.toLowerCase().equals("y")) {
                    opReq = EncryptionUtil.AsymEncrypt(plainreq);
                } else {
                    opReq = plainreq;
                }

                out.write(opReq.getBytes());

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tempstr;


                while ((tempstr = in.readLine()) != null) {

                    responsestr = tempstr;
                }



                if (enc_mode.toLowerCase().equals("y")) {
                    String plainRes = EncryptionUtil.symDecrypt(responsestr, token);
                    opstr = plainRes;
                }
                else {
                    opstr= responsestr;
                }

                in.close();
            } catch (Exception e) {
                e.printStackTrace();
                opstr = "";
            }
            return opstr;
        }
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_login2);

        mains = (Button) findViewById(R.id.button30);
        mains.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SecLoginActivity.this, MainActivity.class));
            }
        });

        unames = findViewById(R.id.su1);
        passs = findViewById(R.id.sp1);


        logins = (Button) findViewById(R.id.button31);
        logins.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    String usernames = unames.getText().toString();
                    String passwords = passs.getText().toString();
                    Switch simpleSwitch = (Switch) findViewById(R.id.switch1);
                    Boolean switchState = simpleSwitch.isChecked();
                    if (simpleSwitch.isChecked()) {
                        enc_mode = "y";
                        System.out.println("aaa" + enc_mode);
                    } else {
                        enc_mode = "n";
                        System.out.println("aba" + enc_mode);
                    }


                    MyTask task = new MyTask();
                    String resStr = task.execute(usernames, passwords, enc_mode).get();
                    if (!resStr.equals("")) {
                        JSONObject resObj = new JSONObject(resStr);
                        String op = (String) resObj.get("status");

                        if (op.contains("Successful")) {
                            String sessiontoken = (String) resObj.get("token");

                            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                            GeneralUtils.saveSharedPrefs(sharedPreferences, "session_token", sessiontoken);
                            GeneralUtils.saveSharedPrefs(sharedPreferences, "usernames", usernames);

                            Intent intent = new Intent(SecLoginActivity.this, SecHomeActivity.class);
                            intent.putExtra("Name", usernames);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SecLoginActivity.this, op, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SecLoginActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SecLoginActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
                }
            }
        });
        ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView4);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(SecLoginActivity.this, IndBlogActivity.class);
                webviewIntent.putExtra("URL",Constants.s_login_blog);
                startActivity(webviewIntent);
            }
        });


    }
}