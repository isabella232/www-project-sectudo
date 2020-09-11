package com.sr.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Constants {

	public static String key = "ThisKEyisforJWT!";
	public static String session_mode = ""; //{"JWT" or "JSE"}
	
	
	public static String publickeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuYq3V+bHGCZQbZmxd+NmTV9NqkyrYQ4ihuVL7TJbYPUoAGDMzlNbgt2DD/QUkL2KNBSJwllokLKN1//LBcx4XMkpZqU4rYW8xA0VCjCKcHsHJiXQ2YoXGbJW7R+kY9o7ETCFCS6IuXZzDKurNwIuZg1oyAPCp4IB2QQuIdb67TnzMhJMPfd7FlwQoqdKwPenMH8NX8IU0LZg9LV36Ap4IFDdhIjOYDDcoNs4OyRqUBzHlkxTdmNY1a8vQoupZ7n51wuVQfwXxgUUO//A7L0+TXTg2utoivXAczvG6WVQfo0+sEIIDhBA6BehL4JoNxwlKGKMhWorJggdfyznOHJiSQIDAQAB";
	public static String privatekeyStr = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC5irdX5scYJlBtmbF342ZNX02qTKthDiKG5UvtMltg9SgAYMzOU1uC3YMP9BSQvYo0FInCWWiQso3X/8sFzHhcySlmpTithbzEDRUKMIpwewcmJdDZihcZslbtH6Rj2jsRMIUJLoi5dnMMq6s3Ai5mDWjIA8KnggHZBC4h1vrtOfMyEkw993sWXBCip0rA96cwfw1fwhTQtmD0tXfoCnggUN2EiM5gMNyg2zg7JGpQHMeWTFN2Y1jVry9Ci6lnufnXC5VB/BfGBRQ7/8DsvT5NdODa62iK9cBzO8bpZVB+jT6wQggOEEDoF6Evgmg3HCUoYoyFaismCB1/LOc4cmJJAgMBAAECggEBAIOMDvt6epPrrkBWpJWMm16mAEmKvEM+o0oizhcpCdKPXkIOX4a1YP9CkAbClcYdLBxKp3UssM9OWPTYvZep5Xhbg0fsQmf8lt4lLFKiIbUn9OSeERn7Ju+oGqtaMNI+ynwge2IMJRHhIibMm8KPgFgIj86no93hde/oX9DMO/HuXCrztvo4ONdLbdWNWAfC7JisA6j7QILo07qG/0gMKUXa0DR6UU2/KOoY8RhNblYRYa9zsSClXn9b9i3d0VCV7BOcbDDnY78YS8UBeo+7Hn1b05O0mDh/aVcfCva5lSrxOuZddzVX1/h2wq4SpH10+ub8hXln6YOBshw5JY6jVYECgYEA234hrtiOIWTOuWwqJfmZZIBTc7XqCsensWEbZpGao9GUO8ujsCiDDpTg67f3Evsmdko/A1kFWx/BrTEkVYq7BwJ2ldlYieyPRLi2mDoQLf7Nyh3m6+Sy7ZrFOGuEDlQj2SGWCjFIe+j2UbMtXrcFJBtIRRIlJaqiHOR6cy47PnkCgYEA2Gb6X2GdRvxi8Ezx1nGc25RkuJ2wvK+w55yqn/kzxhT2qF7iDlJ5hXfqZE6Doh70MTdOe149MpTfpKWOAkrkntLh4PB02g9decytQLwfAUN7r2YXw+BF1/54oLJLZP51xZ+joAI9Q43d5/wUaETkmHKTIMHdyM8Qni1WTsZRDlECgYEAskPHwYTaMp74ErqZpMwKXAipPoIRun2bYcH3ih9ZlBFELihfIlRU1MPvxSJdhcn62/nDIcnsQq0RFMOjueH5smC6xUuH0EUTuwG2Mv59Y43dv3j3ssDE3Ztk8ETKNQT7NH9Fp7ONJi332DUI9TL59vA9ivOIvgXOsl6SYPe8YCkCgYBBUeQlQku9D5W1vyQIfMbCKOq2JzMf4VBeuRVvsEMGeXFypSFK/W4c8XNYz7JAD2PaLz2LU4jZacKP6kERRBX6MuFnim1bHOQ0TanGLoKVWQ9OhFyMDXhHmFJE/0xusbhO7L7xH3vqljnzOk3Wo2T09zCd5KbTZF05wamZoZb54QKBgDMFHYBVM+xfWb4b1VnFngFb4GlSVinI9QscMopwepo2hSFH+qeXXNNHWB2A19VH0HgMquwOClnx3Ux0EgESseAs3GrlKsh2WTcy413kU5fiuJbSHbeAZQfzxxZS+qcMewWRgCPQUCpuGLoLjMpWQavd5hsoYXNXKAmcJiiHNUCp";

	
	public static void loadSessMode() {
		Connection con = null;
		PreparedStatement stmt = null;

		try {

			con = DataStoreAccess.getConnection(1);
			String loginQuery = "select * from settings where id= 1";
			stmt = (PreparedStatement) con.prepareStatement(loginQuery);
	
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				String session_mode = rs.getString("setting_val");
				Constants.session_mode = session_mode;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getSessMode() {
		// TODO Auto-generated method stub
		
		if(session_mode.equals("")) {
			loadSessMode();
		}
		return session_mode;
	}

}
