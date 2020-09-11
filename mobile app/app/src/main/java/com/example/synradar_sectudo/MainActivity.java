package com.example.synradar_sectudo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.insecure.InsLoginActivity;
import com.example.synradar_sectudo.secure.SecLoginActivity;

public class MainActivity extends AppCompatActivity {
    Button insecureButton;
    Button secureButton;
    Button video;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insecureButton = (Button) findViewById(R.id.button4);

        secureButton = (Button) findViewById(R.id.button3);

        video = (Button) findViewById(R.id.button13);

        insecureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InsLoginActivity.class));
            }
        });

        secureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecLoginActivity.class));
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BlogActivity.class));
            }
        });

        Button knowbtn;
        knowbtn = (Button) findViewById(R.id.button15);
        knowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(MainActivity.this, IndBlogActivity.class);
                webviewIntent.putExtra("URL", Constants.intro_blog);
                startActivity(webviewIntent);
            }
        });
    }
}
