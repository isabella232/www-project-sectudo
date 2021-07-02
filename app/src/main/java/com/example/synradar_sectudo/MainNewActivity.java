package com.example.synradar_sectudo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.helper.DBLHelper;
import com.example.synradar_sectudo.helper.EncryptionUtil;
import com.example.synradar_sectudo.helper.GeneralUtils;
import com.example.synradar_sectudo.learningmodule.LearningTabActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class MainNewActivity extends AppCompatActivity {
    TextView ia;
    TextView ism;
    TextView csua;
    TextView ids;
    TextView xss;
    TextView sqli;
    TextView ida;
    TextView dl;
    TextView icp;
    TextView sm;
    DBLHelper myDb;
    int per;
    String topic ="dl";
    String log;
    String token;
    String status_val;
    ImageView test;
    ImageView contact;

    public static final String SHARED_PREFS = "sharedPrefs";

    class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strs) {
            String responsestr = new String();

            URL url;
            try {
                url = new URL(Constants.test_cred);

                String rndtoken = EncryptionUtil.getRandomToken();
                String param = EncryptionUtil.AsymEncrypt(rndtoken);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("param", param);
                con.setDoOutput(false);


                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tempstr;
                while ((tempstr = in.readLine()) != null) {
                //    System.out.println(tempstr);
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
        setContentView(R.layout.activity_main_new);

        if(!isConnected(this)){
            showCustomDialog();
        }



        myDb = new DBLHelper(this);
        String irs = getIntent().getStringExtra("irs");

        if("back".equals(irs) ){
            per = myDb.getCount();
        }
        else{

            loadData();
        }

       int prcnt = per;
       prcnt *=10;

       // addData();

        log = "A20V21";
        Log.d("Sectudo","Data Leakage Value : " + log );



        ProgressBar _progressBar = (ProgressBar)findViewById (R.id.progress_bar);
        _progressBar.setProgress( prcnt );
        ((TextView) findViewById(R.id.text_view_progress)).setText(prcnt+"%");

        ia = (TextView) findViewById(R.id.ia);
        ism = (TextView) findViewById(R.id.ism);
        csua = (TextView) findViewById(R.id.csua);
        ids = (TextView) findViewById(R.id.ids);
        xss = (TextView) findViewById(R.id.xss);
        sqli = (TextView) findViewById(R.id.sqli);
        ida = (TextView) findViewById(R.id.ida);
        dl = (TextView) findViewById(R.id.dl);
        icp = (TextView) findViewById(R.id.icp);
        sm = (TextView) findViewById(R.id.sm);


        test = (ImageView) findViewById(R.id.testcred);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    MainNewActivity.MyTask task = new MainNewActivity.MyTask();
                    String resStr = task.execute().get();


                    JSONObject jsonObj = new JSONObject(resStr);
                    //System.out.println(resStr);
                    String credentials  = jsonObj.getString("credentials");
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainNewActivity.this);
                    builder.setTitle("Test Credentials for Using App ");
                    builder.setMessage(credentials);



                    builder.setPositiveButton("OK", null);


                    AlertDialog dialog = builder.create();
                    dialog.show();


                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        contact = (ImageView) findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(MainNewActivity.this, IndBlogActivity.class);
                webviewIntent.putExtra("URL", Constants.con);
                startActivity(webviewIntent);
            }
        });




       // ism.setEnabled(this.getActiveStatus("ia"));
        //csua.setEnabled(this.getActiveStatus("ism"));
        //ids.setEnabled(this.getActiveStatus("csua"));
        //xss.setEnabled(this.getActiveStatus("ids"));
        //sqli.setEnabled(this.getActiveStatus("xss"));
        //ida.setEnabled(this.getActiveStatus("sqli"));
        //dl.setEnabled(this.getActiveStatus("ida"));
        //icp.setEnabled(this.getActiveStatus("dl"));
        //sm.setEnabled(this.getActiveStatus("sm"));



        ia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainNewActivity.this, LearningTabActivity.class).putExtra("topic", "ia"));
            }
        });

        ism.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(MainNewActivity.this, LearningTabActivity.class).putExtra("topic", "ism"));
            }
        });

        csua.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainNewActivity.this, LearningTabActivity.class).putExtra("topic", "csua"));
            }
        });

        ids.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                GeneralUtils.saveSharedPrefs(sharedPreferences, "ids", "insecurekyc.db");
                startActivity(new Intent(MainNewActivity.this, LearningTabActivity.class).putExtra("topic", "ids"));
            }
        });
        xss.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(MainNewActivity.this, LearningTabActivity.class).putExtra("topic", "xss"));
            }
        });

        sqli.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(MainNewActivity.this, LearningTabActivity.class).putExtra("topic", "sqli"));
            }
        });

        ida.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                startActivity(new Intent(MainNewActivity.this, LearningTabActivity.class).putExtra("topic", "ida"));
            }
        });

        dl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                GeneralUtils.saveSharedPrefs(sharedPreferences, "dl", log);
                startActivity(new Intent(MainNewActivity.this, LearningTabActivity.class).putExtra("topic", "dl"));
            }
        });
        icp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainNewActivity.this, LearningTabActivity.class).putExtra("topic", "icp"));
            }
        });

        sm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                startActivity(new Intent(MainNewActivity.this, LearningTabActivity.class).putExtra("topic", "sm"));
            }
        });




    }



    private boolean isConnected(MainNewActivity mainNewActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mainNewActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn != null && wifiConn.isConnected() || (mobConn != null && mobConn.isConnected()))){
            return true;
        }
        else{
            return false;
        }
    }
    private void showCustomDialog() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(MainNewActivity.this);
        mbuilder.setMessage(" Please connect to Internet to view Learning Content and Perform Testing.");
        mbuilder.setCancelable(false);

        // add a button
        mbuilder.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        mbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                startActivity(new Intent(getApplicationContext(), MainNewActivity.class).putExtra("irs","back"));
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = mbuilder.create();
        dialog.show();
    }

    private boolean getActiveStatus(String topicName) {
        boolean activeStatus = false;
       String status =  myDb.getStatus(topicName);

        if(status.equals("completed")){
            activeStatus = true;
        }
        return activeStatus;

    }

    private void loadData() {
        HashMap vals = myDb.loadData();
        String status = (String) vals.get("Status");
        if(status.equals("1")) {
            per = myDb.getCount();
        }
        else {
            myDb.insertData();
        }
    }




}