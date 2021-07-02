package com.example.synradar_sectudo.helper;

public class Constants {
    public static String ip = "sectudo.com";

    public static final String loginURL = "http://" + ip + "/Ckms/api/authservice/login";
    public static final String floginURL = "http://" + ip + "/Ckms/api/authservice/ilogin";
    public static final String addkycURL = "http://" + ip + "/Ckms/api/kycservice/add";
    public static final String getAcctSumURL = "http://" + ip + "/Ckms/api/kycservice/acctsum";
    public static final String getAcctURL = "http://" + ip + "/Ckms/api/kycservice/acctdetails/";
    public static final String getEligibility = "http://" + ip + "/Ckms/api/kycservice/geteligibility";
    public static final String showkyc = "http://" + ip + "/Ckms/api/kycservice/showdetails";
    public static final String home = "http://" + ip + "/Ckms/api/kycservice/home";
    public static final String update= "http://" + ip + "/Ckms/api/kycservice/updateaddress";


    /*******************************************/

    public static final String s_loginURL = "http://" + ip + "/Ckms/api/secure/authservice/login";
    public static final String s_addkycURL = "http://" + ip + "/Ckms/api/secure/kycservice/add";
    public static final String s_showKyc = "http://" + ip + "/Ckms/api/secure/kycservice/showdetails";
    public static final String s_home = "http://" + ip + "/Ckms/api/secure/kycservice/home";
    public static final String s_getAcctSumURL = "http://" + ip + "/Ckms/api/secure/kycservice/acctsum";
    public static final String s_getAcctURL = "http://" + ip + "/Ckms/api/secure/kycservice/acctdetails/";
    public static final String s_getEligibility = "http://" + ip + "/Ckms/api/secure/kycservice/geteligibility";
    public static final String s_test = "http://" + ip +"/Ckms/api/secure/kycservice/test";
    public static final String s_logout = "http://"+ip+"/Ckms/api/secure/authservice/logout";

    /*******************************************/

    public static String session_mode = "JWT";


    public static String publickeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuYq3V+bHGCZQbZmxd+NmTV9NqkyrYQ4ihuVL7TJbYPUoAGDMzlNbgt2DD/QUkL2KNBSJwllokLKN1//LBcx4XMkpZqU4rYW8xA0VCjCKcHsHJiXQ2YoXGbJW7R+kY9o7ETCFCS6IuXZzDKurNwIuZg1oyAPCp4IB2QQuIdb67TnzMhJMPfd7FlwQoqdKwPenMH8NX8IU0LZg9LV36Ap4IFDdhIjOYDDcoNs4OyRqUBzHlkxTdmNY1a8vQoupZ7n51wuVQfwXxgUUO//A7L0+TXTg2utoivXAczvG6WVQfo0+sEIIDhBA6BehL4JoNxwlKGKMhWorJggdfyznOHJiSQIDAQAB";

    public static String privatekeyStr = "";

    public static final String s_blog="http://"+ip+"/guide/topics.html";

    public static final String login_blog="http://"+ip+"/guide/intro/1login_intro.html";
    public static final String home_blog="http://"+ip+"/guide/intro/6Home_intro.html";
    public static final String kyc_blog="http://"+ip+"/guide/intro/2KYC_intro.html";
    public static final String view_blog="http://"+ip+"/guide/intro/3ViewKYC_intro.html";
    public static final String show_blog="http://"+ip+"/guide/intro/5Account_intro.html";
    public static final String check_blog="http://"+ip+"/guide/intro/4Eligibility_intro.html";

    public static final String s_login_blog="http://"+ip+"/guide/intro/secure_1Login_intro.html";
    public static final String s_home_blog="http://"+ip+"/guide/intro/secure_6Home_intro.html";
    public static final String s_kyc_blog="http://"+ip+"/guide/intro/secure_2KYC_intro.html";
    public static final String s_view_blog="http://"+ip+"/guide/intro/secure_3View_intro.html";
    public static final String s_show_blog="http://"+ip+"/guide/intro/secure_5Account_intro.html";
    public static final String s_check_blog="http://"+ip+"/guide/intro/secure_4Eligibility_intro.html";

    public static final String intro_blog="http://"+ip+"/guide/Introduction.html";


    public static final String test_cred= "http://" + ip + "/Ckms/api/authservice/getcred";
    public static final String learn="http://"+ip+ "/guide/topics/";

    public static final String con="https://synradar.com/contact-us";



}
