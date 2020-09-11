package com.example.synradar_sectudo.insecure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.synradar_sectudo.IndBlogActivity;
import com.example.synradar_sectudo.MainActivity;
import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.helper.GeneralUtils;
import com.example.synradar_sectudo.secure.SecLoginActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class InsLoginActivity extends AppCompatActivity {
    Button login;
    Button menu;
    EditText uname;
    EditText pass;


    public static final String SHARED_PREFS = "sharedPrefs";
    String tokenstr;


    class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strs) {

            String uname = strs[0];
            String pass = strs[1];
            String responsestr = new String();

            JSONObject reqjson = new JSONObject();
            URL url;
            try {

                reqjson.put("username", uname);
                reqjson.put("password", pass);

                url = new URL(Constants.loginURL);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
                OutputStream out = con.getOutputStream();
                out.write(reqjson.toString().getBytes());

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tempstr;

                while ((tempstr = in.readLine()) != null) {
                    //System.out.println(tempstr);
                    responsestr = tempstr;
                }


                in.close();


            } catch (Exception e) {

                e.printStackTrace();
            }

            return responsestr;
        }
    }

    //SharedPreferences sharedprefrences;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_login);
        menu = (Button) findViewById(R.id.button2);
        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(InsLoginActivity.this, MainActivity.class));
            }
        });
        uname = findViewById(R.id.u1);
        pass = findViewById(R.id.p1);


        login = (Button) findViewById(R.id.button);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    String username = uname.getText().toString();
                    String password = pass.getText().toString();



                    MyTask task = new MyTask();
                    String resStr = task.execute(username, password).get();
                    JSONObject resObj = new JSONObject(resStr);
                    int token = (int) resObj.get("token");
                    tokenstr = token + "";
                    String opstr = (String) resObj.get("Status");

                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    GeneralUtils.saveSharedPrefs(sharedPreferences, "session_token", tokenstr);
                    GeneralUtils.saveSharedPrefs(sharedPreferences, "username", username );

                    if (opstr.equals("Login Successful")) {
                        Intent intent = new Intent(InsLoginActivity.this, InsHomeActivity.class);
                        intent.putExtra("Name", username);
                        startActivity(intent);
                       // startActivity(new Intent(InsLoginActivity.this, InsHomeActivity.class));
                    } else {
                        Toast.makeText(InsLoginActivity.this, opstr, Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView5);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(InsLoginActivity.this, IndBlogActivity.class);
                webviewIntent.putExtra("URL",Constants.login_blog);
                startActivity(webviewIntent);
            }
        });

    }

}
