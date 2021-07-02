package com.example.synradar_sectudo.learningmodule;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.synradar_sectudo.IndBlogActivity;
import com.example.synradar_sectudo.MainNewActivity;
import com.example.synradar_sectudo.R;
import com.example.synradar_sectudo.helper.Constants;
import com.example.synradar_sectudo.helper.GeneralUtils;
import com.example.synradar_sectudo.insecure.InsHomeActivity;
import com.example.synradar_sectudo.insecure.InsLoginActivity;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {
    TextView ques;
    ImageView select;
    Button submit;
    EditText ans;
    String topic;
    TextView topicName;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserValues = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        ques = (TextView) findViewById(R.id.textView21);
        select = (ImageView) findViewById(R.id.buttonselect);
        ans = findViewById(R.id.qa);
        submit= (Button) findViewById(R.id.button66);

        listItems = getResources().getStringArray(R.array.sm_options);
        checkedItems = new boolean[listItems.length];
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
                builder.setTitle("Select Options");
                builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            if(! mUserValues.contains(position)){
                                mUserValues.add(position);
                            }else {
                             mUserValues.remove(position);
                            }
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item ="";
                        for (int i = 0; i <mUserValues.size(); i++){
                            item =item+listItems[mUserValues.get(i)];
                            if(i != mUserValues.size() - 1){
                                item = item + ",";
                            }
                        }
                        ans.setText(item);
                    }
                });
                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++ ){
                            checkedItems[i] = false;
                            mUserValues.clear();
                            ans.setText("");
                        }
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }});


         topic = getIntent().getStringExtra("topic");
        ques.setText(GeneralUtils.getQuizQues(topic));


        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                SharedPreferences prefs = getSharedPreferences(GeneralUtils.SHARED_PREFS, MODE_PRIVATE);
                switch (topic) {
                    case "dl":
                        String sol = GeneralUtils.readSharedPrefs(prefs, "dl");
                        String qa = ans.getText().toString();



                        if (sol.equals(qa)) {
                            startActivity(new Intent(QuestionActivity.this, CongratsActivity.class).putExtra("topic", topic));
                           }
                        else {
                            startActivity(new Intent(QuestionActivity.this, RetryActivity.class).putExtra("topic", topic));
                        }
                        break;
                    case "icp":
                    case "csua":
                        String solp = GeneralUtils.getAns(topic);
                        String qap = ans.getText().toString();
                        if (solp.equals(qap)) {
                            startActivity(new Intent(QuestionActivity.this, CongratsActivity.class).putExtra("topic", topic));
                        }
                        else {
                            startActivity(new Intent(QuestionActivity.this, RetryActivity.class).putExtra("topic", topic));
                        }
                        break;
                    case "sm":

                        String qam = ans.getText().toString();
                        boolean status = GeneralUtils.getSM(qam);
                        if (status) {
                            startActivity(new Intent(QuestionActivity.this, CongratsActivity.class).putExtra("topic", topic));
                        }
                        else {
                            startActivity(new Intent(QuestionActivity.this, RetryActivity.class).putExtra("topic", topic));
                        }
                        break;
                    case "ids":
                        String sols = GeneralUtils.readSharedPrefs(prefs, "ids");
                        String qas = ans.getText().toString();
                        if (sols.equals(qas)) {
                            startActivity(new Intent(QuestionActivity.this, CongratsActivity.class).putExtra("topic", topic));
                        }
                        else {
                            startActivity(new Intent(QuestionActivity.this, RetryActivity.class).putExtra("topic", topic));
                        }
                        break;
                    default:
                        ques.setText(("Select proper Topic"));
                }
            }
        });

        ImageView imgbtn;
        imgbtn = (ImageView) findViewById(R.id.imageView23);
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
                Intent webviewIntent = new Intent(QuestionActivity.this, IndBlogActivity.class);
                //String test = getIntent().getStringExtra("chlng");
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

        ImageView iv = (ImageView) findViewById(R.id.imageViewQ);
        topicName = (TextView) findViewById(R.id.textView36);
        switch (topic){
            case "csua":
                topicName.setText(("Task for Client Side Unauthenticated Access"));
                break;
            case "dl":
                topicName.setText(("Task for Data Leakage"));
                iv.setImageResource(R.drawable.dle);
                break;
            case "icp":
                topicName.setText(("Task for Insecure Code Protection"));
                iv.setImageResource(R.drawable.icpe);
                break;
            case "ids":
                topicName.setText(("Task for Insecure Data Storage"));
                iv.setImageResource(R.drawable.idse);
                break;
            case "sm":
                topicName.setText(("Task for Security Misconfiguration"));
                iv.setImageResource(R.drawable.sme);
                select.setVisibility(View.VISIBLE);
                break;
        }

        ImageView tpn;
        tpn = (ImageView) findViewById(R.id.imageViewtp);
        tpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuestionActivity.this, MainNewActivity.class).putExtra("irs","back"));
            }});
        ImageView bck;
        bck = (ImageView) findViewById(R.id.imageViewbk);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuestionActivity.this, LearningTabActivity.class).putExtra("topic",topic));
            }});
    }
    @Override
    public void onBackPressed() { }
}