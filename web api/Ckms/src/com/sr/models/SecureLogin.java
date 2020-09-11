package com.sr.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sr.utilities.DataStoreAccess;

public class SecureLogin {

	private String username;
	private String password;
	private String userid;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {

		this.username = new String(username);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {

		this.password = new String(password);
	}

	public boolean validateLogin() throws SQLException {
		boolean status = false;

		Connection con = null;
		PreparedStatement stmt = null;

		try {

			con = DataStoreAccess.getConnection(1);
			String loginQuery = "select * from usertable where username = ? and secure_pass = ?";
			stmt = (PreparedStatement) con.prepareStatement(loginQuery);

			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = (ResultSet) stmt.executeQuery();

			while (rs.next()) {
				status = true;
				userid = rs.getString("userid");
			}
		}

		catch (Exception e) {

			e.printStackTrace();

		} finally {
			con.close();
			stmt.close();
		}
		return status;
	}

	public static void main(String[] args) {

		String op = "Failed";
		try {

			SecureLogin l = new SecureLogin();
			l.setUsername("adam");
			l.setPassword("adam123");

			boolean result;

			result = l.validateLogin();

			if (result) {
				op = "Success";

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		System.out.println("test" + op);
	}

	public boolean verifyuser(int userid) throws Exception {
		// TODO Auto-generated method stub

		boolean status = false;

		Connection con = null;
		PreparedStatement stmt = null;

		try {

			con = DataStoreAccess.getConnection(1);
			String loginQuery = "select * from usertable where userid = ?";
			stmt = (PreparedStatement) con.prepareStatement(loginQuery);

			stmt.setInt(1, userid);
			ResultSet rs = (ResultSet) stmt.executeQuery();

			while (rs.next()) {
				status = true;
			}
		}

		catch (Exception e) {

			e.printStackTrace();

		} finally {
			con.close();
			stmt.close();
		}

		return status;
	}

	public void init(String username) throws SQLException {
		// TODO Auto-generated method stub
		this.username = username;
		Connection con = null;
		PreparedStatement stmt = null;

		try {

			con = DataStoreAccess.getConnection(1);
			String loginQuery = "select * from usertable where username = ?";
			stmt = (PreparedStatement) con.prepareStatement(loginQuery);
			stmt.setString(1, username);

			ResultSet rs = (ResultSet) stmt.executeQuery();

			while (rs.next()) {
				this.password = rs.getString("secure_pass");
				this.userid = rs.getInt("userid") +"";
			}
		}

		catch (Exception e) {

			e.printStackTrace();

		} finally {
			con.close();
			stmt.close();
		}

	}

	public void setActiveSession() {
		// TODO Auto-generated method stub

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = DataStoreAccess.getConnection(1);
			String loginQuery = "insert into avtive_logins (fk_userid) values (?)";
			stmt = (PreparedStatement) con.prepareStatement(loginQuery);
			stmt.setString(1, userid);
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getSessionCount() {

		int sessCount = 0;

		Connection con = null;
		PreparedStatement stmt = null;

		try {

			con = DataStoreAccess.getConnection(1);
			String loginQuery = "select * from active_logins limit 1";
			stmt = (PreparedStatement) con.prepareStatement(loginQuery);
			stmt.setString(1, userid);

			ResultSet rs = (ResultSet) stmt.executeQuery();

			while (rs.next()) {
				sessCount = rs.getInt("session_count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sessCount;

	}

	public void removeSession(int session_count) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;

		try {

			con = DataStoreAccess.getConnection(1);
			String loginQuery = "Delete from active_logins where fk_userid =? & session_count =?";
			stmt = (PreparedStatement) con.prepareStatement(loginQuery);
			stmt.setString(1, userid);
			stmt.setInt(2, session_count);

			stmt.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
