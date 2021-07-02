package com.example.synradar_sectudo.helper;

import android.content.SharedPreferences;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GeneralUtils {

    public static final String SHARED_PREFS = "sharedPrefs";

    public static void saveSharedPrefs(SharedPreferences prefs, String key, String value){
        SharedPreferences.Editor editor =prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String readSharedPrefs(SharedPreferences prefs, String key){

        String value = prefs.getString(key, "");
        return value;

    }

    public static String getTaskMsg(String chlng) {
        String msg = "";
        switch(chlng) {

            case "xss":
               msg = " Perform Cross Site Scripting on Address Field ";
                break;
            case "ids":
                msg = " Identify Database name ";
                break;
            case "sqli" :
                msg = " Perform SQL Injection ";
                break;
            case "ida" :
                msg = " Get Account details of other user ";
                break;
            case "ia" :
                msg = " Change the Response of Login ";
                break;
            case "ism" :
                msg = " Get Session of Other User ";
                break;
            default:
                msg = " Select proper Challenge ";

        }

        return msg;
    }

    public static String getQuizQues(String chlng) {
        String msg = "";
        switch(chlng) {

            case "csua":
                msg = " Write Identified code ";
                break;
            case "dl":
                msg = " What is the value of token found in log ";
                break;
            case "icp" :
                msg = " What is the icp_value found in Code ";
                break;
            case "sm" :
                msg = " Select Security Misconfiguration's Found in the App ";
                break;
            case "ids" :
                msg = " What is the Database name ";
                break;
            default:
                msg = " Select proper Challenge ";
        }

        return msg;
    }

    public static String getSecTaskMsg(String chlng) {
        String msg = "Observe & Verify the Solution Implemented ";
        return msg;
    }

    public static boolean getSM(String ans) {

        boolean result = false;
       List selectedans = Arrays.asList(ans.split(","));

        ArrayList answers = new ArrayList(){
            {
                add("allowBackup");
                add("debuggable");
                add("usesCleartextTraffic");
            }
        };

        if(selectedans.containsAll(answers) && (selectedans.size() == answers.size())){
            result = true;
        }

            return result;

    }

    public static String getAns(String chlng) {
        String msg = "";
        switch(chlng) {

            case "csua":
                msg = "2021";
                break;
            case "icp" :
                msg = "Reverse Engineering";
                break;
            default:
                msg = " Select proper Challenge ";
        }

        return msg;
    }

    String icp_value= "Reverse Engineering";

}