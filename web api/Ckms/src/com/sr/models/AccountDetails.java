package com.sr.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.sr.utilities.DataStoreAccess;
import com.sr.utilities.GeneralUtil;

public class AccountDetails {

	private static final int MAX_CHAR_ACCT_ID = 15;
	private String acct_name;
	private String balance;
	private String acct_id;
	private int fk_userid;
	private boolean secure_mode = false;

	public int getFk_userid() {
		return fk_userid;
	}

	public void setFk_userid(int fk_userid) {
		this.fk_userid = fk_userid;
	}

	public String getAcct_name() {
		return acct_name;
	}

	public void setAcct_name(String acct_name) {
		this.acct_name = acct_name;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getAcct_id() {
		return acct_id;
	}

	public void setSecureMode() {
		this.secure_mode = true;
	}

	public void setAcct_id(String ip_acct_id) throws Exception {

		if (secure_mode) {

			try {

				int actid = Integer.parseInt(ip_acct_id);
				this.acct_id = actid + "";

			} catch (Exception e) {
				throw new Exception("Not a valid input, Try again");
			}

		} else {

			int maxLength = (ip_acct_id.length() < MAX_CHAR_ACCT_ID) ? ip_acct_id.length() : MAX_CHAR_ACCT_ID;

			if (GeneralUtil.checkblacklistStr(ip_acct_id)) {
				throw new Exception("Not a valid input, Try again");
			}
			this.acct_id = ip_acct_id.substring(0, maxLength);
			 
		}
	}

	public void init() throws Exception {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = DataStoreAccess.getConnection(1);
			String loginQuery = "select * from accountstable where account_id = " + acct_id;
			stmt = (PreparedStatement) con.prepareStatement(loginQuery);

			ResultSet rs = (ResultSet) stmt.executeQuery();

			while (rs.next()) {

				acct_id = rs.getString("account_id");
				acct_name = rs.getString("accountname");
				balance = rs.getString("balance");
				fk_userid = rs.getInt("userid");

			}
		} catch (Exception e) {
			throw new Exception("Error Occured - " + e.getMessage());
		}
	}

	public boolean secureInit() throws Exception {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;
		boolean status = false;

		try {
			con = DataStoreAccess.getConnection(1);
			String sqlQuery = "select * from accountstable where account_id = ? and userid=?";
			stmt = (PreparedStatement) con.prepareStatement(sqlQuery);

			stmt.setString(1, acct_id);
			stmt.setInt(2, fk_userid);

			ResultSet rs = (ResultSet) stmt.executeQuery();

			while (rs.next()) {
				status = true;
				acct_id = rs.getString("account_id");
				acct_name = rs.getString("accountname");
				balance = rs.getString("balance");
			}
		} catch (Exception e) {
			return status;
		}

		return status;
	}

	public static void main(String[] args) {

		AccountDetails ad = new AccountDetails();
		try {
			ad.setAcct_id("1 OR 1=1 #");

			ad.init();

			System.out.println(ad.getAcct_id());
			System.out.println(ad.getAcct_name());
			System.out.println(ad.getBalance());
			System.out.println(ad.getFk_userid());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
