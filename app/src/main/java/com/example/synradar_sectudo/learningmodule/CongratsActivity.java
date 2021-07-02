package com.example.synradar_sectudo.learningmodule;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.synradar_sectudo.BlogActivity;
import com.example.synradar_sectudo.IndBlogActivity;
import com.example.synradar_sectudo.MainNewActivity;
import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.helper.DBLHelper;
import com.example.synradar_sectudo.helper.GeneralUtils;

import static com.example.synradar_sectudo.helper.Constants.s_blog;

public class CongratsActivity extends AppCompatActivity {
    Button proceed;
    TextView status;
    String topic;
    WebView webView;
    DBLHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrats);

        status = (TextView) findViewById(R.id.textView24);




        topic = getIntent().getStringExtra("topic");
        switch (topic) {
            case "sqli":
            case "ida":
                String act_name = getIntent().getStringExtra("actname");
                String act_balance = getIntent().getStringExtra("actbalance");
                String act_nmb = getIntent().getStringExtra("act_nmb");
                status.setVisibility(View.VISIBLE);
                status.setText(( "Your Account Number is : - "+act_nmb +" and Account Name is : - "  +act_name +" and your balance is :- " +act_balance));
                break;
            case "ism":

                String st = getIntent().getStringExtra("st");
                String ct = getIntent().getStringExtra("ct");
                String user = getIntent().getStringExtra("user");


                status.setVisibility(View.VISIBLE);

                status.setText((" Welcome " + user + " your status is " + st +" and card type is "+ ct  ));

                break;
        }






        myDb = new DBLHelper(this);
       update();

        proceed = (Button) findViewById(R.id.button64);

        proceed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                topic = getIntent().getStringExtra("topic");

                startActivity(new Intent(CongratsActivity.this, LearningTabActivity.class).putExtra("topic", topic));


            }
        });

        ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView21);
        switch (topic){
            case "dl" :
            case "icp" :
            case "sm" :
            case "csua" :
                imgbtn.setVisibility(View.INVISIBLE);
        }
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(CongratsActivity.this, IndBlogActivity.class);
               // String test = getIntent().getStringExtra("ch");
                switch (topic){
                    case "ia" :
                        webviewIntent.putExtra("URL", Constants.login_blog);
                        break;
                    case "ism" :
                        webviewIntent.putExtra("URL", Constants.home_blog);
                        break;
                    case "ids" :
                        webviewIntent.putExtra("URL", Constants.kyc_blog);
                        break;
                    case "xss" :
                        webviewIntent.putExtra("URL", Constants.view_blog);
                        break;
                    case "sqli" :
                    case "ida" :
                        webviewIntent.putExtra("URL", Constants.show_blog);
                        break;
                }

                startActivity(webviewIntent);
            }
        });

        ImageView tpn;
        tpn = (ImageView) findViewById(R.id.imageViewtp);
        tpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CongratsActivity.this, MainNewActivity.class).putExtra("irs","back"));
            }});
        ImageView bck;
        bck = (ImageView) findViewById(R.id.imageViewbk);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CongratsActivity.this, LearningTabActivity.class).putExtra("topic",topic));
            }});
    }

    @Override
    public void onBackPressed() { }

    public void update() {
         String Mit = "y";
         String Overall_Status ="completed";
         String Topic = topic;

        myDb.addMit(Mit, Overall_Status, Topic);
    }
}