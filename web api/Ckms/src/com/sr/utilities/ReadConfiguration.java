package com.sr.utilities;

import java.io.File;
import java.util.HashMap;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ReadConfiguration {

	private static HashMap configStore = null;

	public static void InitializeConfigStore() {

		HashMap config = new HashMap();
		config.put("Drivername", "com.mysql.cj.jdbc.Driver");
		config.put("DatabaseLocation", "jdbc:mysql://localhost:3306/dbname");
		config.put("User", "dbuser");
	    config.put("Password", "dbpass");
	
	
	    configStore = config;

	}

	public static String getDrivername() {

		if (configStore == null) {
			InitializeConfigStore();
		}
		String driverName = (String) configStore.get("Drivername");
		return driverName;
	}

	public static String getConnectionString(){
		if (configStore == null) {
			InitializeConfigStore();
		}
		String conString = (String) configStore.get("DatabaseLocation");
		return conString;
	}

	public static String getUsername() {
		if (configStore == null) {
			InitializeConfigStore();
		}
		String conString = (String) configStore.get("User");
		return conString;
	}

	public static String getPassword() {
		if (configStore == null) {
			InitializeConfigStore();
		}
		String conString = (String) configStore.get("Password");
		return conString;
	}

	public static void main(String[] args) {
		try {
			InitializeConfigStore();
			System.out.println(getConnectionString());
			System.out.println(getDrivername());
			System.out.println(getPassword());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

/*
 * 
 * In the next revision store encrypted values in config.xml and in getXYZ
 * method use decryption logic and return the actual value So that what is
 * stored in the hash map is also encrypted and decryption would have only
 * runtime
 * 
 */
