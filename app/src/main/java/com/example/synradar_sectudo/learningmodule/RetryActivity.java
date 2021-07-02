package com.example.synradar_sectudo.learningmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.synradar_sectudo.IndBlogActivity;
import com.example.synradar_sectudo.MainNewActivity;
import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.insecure.InsAddkycActivity;
import com.example.synradar_sectudo.insecure.InsHomeActivity;
import com.example.synradar_sectudo.insecure.InsLogin2Activity;
import com.example.synradar_sectudo.insecure.InsLoginActivity;
import com.example.synradar_sectudo.insecure.InsShowDetailsActivity;
import com.example.synradar_sectudo.insecure.InsUpdateAddrActivity;

public class RetryActivity extends AppCompatActivity {
    Button retry;
    String topic;
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retry);
        status = (TextView) findViewById(R.id.textView300);

        topic = getIntent().getStringExtra("topic");
        switch (topic) {
            case "sqli":
            case "ida":
                String act_name = getIntent().getStringExtra("actname");
                String act_balance = getIntent().getStringExtra("actbalance");
                String act_nmb = getIntent().getStringExtra("act_nmb");
                status.setVisibility(View.VISIBLE);
                status.setText(( "Your Account Number is : - "+act_nmb +" and Account Name is : - "  +act_name +" and your balance is :- "+act_balance));
                break;
            case "ism":

                String st = getIntent().getStringExtra("st");
                String ct = getIntent().getStringExtra("ct");
                String user = getIntent().getStringExtra("user");


                status.setVisibility(View.VISIBLE);

                status.setText((" Welcome " + user + " your status is " + st +" and card type is "+ ct  ));

                break;
        }



        retry = (Button) findViewById(R.id.button65);

        retry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                switch (topic) {
                    case "csua":
                    case "dl":
                    case "icp":
                    case "sm":
                    case "ids":
                        startActivity(new Intent(RetryActivity.this, QuestionActivity.class).putExtra("topic", topic));
                        break;
                    case "xss":

                        startActivity(new Intent(RetryActivity.this, InsUpdateAddrActivity.class).putExtra("topic", topic));
                        break;
                    case "ida":
                    case "sqli":
                        startActivity(new Intent(RetryActivity.this, InsShowDetailsActivity.class).putExtra("topic", topic));
                        break;
                    case"ism":
                        startActivity(new Intent(RetryActivity.this, InsHomeActivity.class).putExtra("topic", topic));
                        break;
                    case"ia":
                        startActivity(new Intent(RetryActivity.this, InsLogin2Activity.class).putExtra("topic", topic));

                }

        }});

        ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView20);
        TextView view;
        view = (TextView) findViewById(R.id.textView19);
        switch (topic){
            case "dl" :
            case "icp" :
            case "sm" :
            case "csua" :
                imgbtn.setVisibility(View.INVISIBLE);
                view.setVisibility(View.INVISIBLE);
        }
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(RetryActivity.this, IndBlogActivity.class);

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
                startActivity(new Intent(RetryActivity.this, MainNewActivity.class).putExtra("irs","back"));
            }});
        ImageView bck;
        bck = (ImageView) findViewById(R.id.imageViewbk);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RetryActivity.this, LearningTabActivity.class).putExtra("topic",topic));
            }});
    }
    @Override
    public void onBackPressed() { }
}