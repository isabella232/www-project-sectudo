package com.sr.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Arrays;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GeneralUtil {
	
	
	public static JSONObject getJSON(InputStream incomingData) throws Exception {

		StringBuilder requestData = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
		String line = null;
		while ((line = in.readLine()) != null) {
			requestData.append(line);
		}
		JSONObject jsonObj = new JSONObject(requestData.toString());

		return jsonObj;

	}
	
	public static boolean checkblacklistStr(String ip) {
		boolean status = false;
		final List<String> blackList = Arrays.asList("select", "shutdown", "insert", "update", "waitfordelay", "where");
		for(int counter = 0; counter < blackList.size(); counter++) {
			if(ip.toLowerCase().contains(blackList.get(counter).toLowerCase())){
				status = true;
				break;
			}

		}
		return status;
	}

	public static String validate(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public static JSONObject getDecryptedJSON(InputStream incomingData) throws Exception {
		// TODO Auto-generated method stub		
		StringBuilder requestData = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
		String line = null;
		while ((line = in.readLine()) != null) {
			requestData.append(line);
		}
		
		String encryptedStr = requestData.toString();
		String decryptedStr = EncryptionUtil.decryptAsym(encryptedStr);
		JSONObject jsonObj = new JSONObject(decryptedStr);

		return jsonObj;
	}

}
