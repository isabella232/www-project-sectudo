# Project Sectudo 
## OWASP Foundation Web Respository

![Intro Image](https://github.com/OWASP/www-project-sectudo/blob/master/assets/images/intro.jpg?raw=true)

### Sectudo is a Mobile app that aims at imparting simplified Mobile Application Security Learning. ###

**Sectudo** demonstrates the security flaws prone to Mobile apps and their Server-side Web APIs. These security flaws can be seen and practiced in the insecure instance of a demo application present in the app. 

*The details of the flaws along with the step-by-step guide of enumerating them are given in the form of videos.*

Along with security flaws, Sectudo also aims to provide an understanding of its security controls. A separate secure instance of the demo application is present in it, with the necessary security measures in place. The security implementation can be studied by navigating through its different features.

## What is inside Sectudo? ##

### Insecure version:
Demo KYC Portal – A demo application with features like Add/View KYC, View Accounts, etc. They have different security flaws embedded in them.

### Secure version:
Demo KYC Portal – A secure version of the same demo application with similar features like Add/View KYC, View Accounts, etc. showcasing the security controls implemented in them.

### A Learning Guide:
Mobile Application Security – The learning guide is ideal for beginners to know about all the important application security topics related to the Mobile app. It focuses on highlighting the root cause of the flaws and their impact.

## To run the app:

* Download latest APK from the folder - "Latest Apk"
* Install the apk on your Android device
* Browse through the secure or insecure demo features
* Refer to the credentials given in the tab_details.md

## To build the app:

### Build the Web API:
* Download the web api source code from "web api/Ckms"
* Load the project in Eclipse IDE
* Download the SQL file from the "web api/SQL"
* Run the SQL file in the DB
* Update the DB details in the file "web api\Ckms\src\com\sr\utilities\ReadConfigurations.java"
* Run the project

### Build the mobile app:
* Download the mobile app source code from "mobile app"
* Load the project in Android Studio
* If the web api is also running locally then change the value of "ip" variable in _src/main/java/com/example/synradar_sectudo/helper/Constants.java_ to that of local instance's IP address
