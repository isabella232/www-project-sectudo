# Project Sectudo 
## OWASP Foundation Web Respository

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
