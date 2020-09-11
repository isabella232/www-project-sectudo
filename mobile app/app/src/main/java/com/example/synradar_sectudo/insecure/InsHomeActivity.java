package com.example.synradar_sectudo.insecure;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.synradar_sectudo.IndBlogActivity;
import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.helper.GeneralUtils;
import com.example.synradar_sectudo.secure.SecLoginActivity;

public class InsHomeActivity extends AppCompatActivity {
    private TextView textview4;
    Button view;
    Button show;
    Button chk;
    Button kyc1;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_home);
        textview4 = (TextView) findViewById(R.id.textView4);
        SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
        textview4.setText((" Welcome " +GeneralUtils.readSharedPrefs(prefs, "username") + " to Insecure KYC Portal"));


        kyc1= (Button) findViewById(R.id.buttona);
        kyc1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(InsHomeActivity.this, InsAddkycActivity.class));
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
                startActivity(new Intent(InsHomeActivity.this, InsShowDetailsActivity.class));
            }
        });

        chk= (Button) findViewById(R.id.button8);
        chk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(InsHomeActivity.this, InsCheckEligibilityActivity.class));
            }
        });

        logout= (Button) findViewById(R.id.button14);
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(InsHomeActivity.this, InsLoginActivity.class));
                SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear().apply();
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
