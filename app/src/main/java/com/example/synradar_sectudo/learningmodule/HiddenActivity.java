package com.example.synradar_sectudo.learningmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.GeneralUtils;

import java.util.Random;

public class HiddenActivity extends AppCompatActivity {
    TextView rndm;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden);

        rndm = (TextView) findViewById(R.id.textView23);
        //Random random = new Random();
        //int val = random.nextInt(1000);
        int val = 2021;
        rndm.setText(Integer.toString(val));


        String value = Integer.toString(val);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        GeneralUtils.saveSharedPrefs(sharedPreferences, "rnd", value);

    }
}