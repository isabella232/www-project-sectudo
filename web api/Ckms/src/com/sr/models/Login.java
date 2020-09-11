package com.sr.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sr.utilities.DataStoreAccess;

public class Login {

	private String username;
	private String password;
	private int userid;
	

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
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
			String loginQuery = "select * from usertable where username = ? and password = ?";
			stmt = (PreparedStatement) con.prepareStatement(loginQuery);

			stmt.setString(1, username);
			stmt.setString(2,  password);
			ResultSet rs = (ResultSet) stmt.executeQuery();
			
			while (rs.next()) {
				status = true;
				userid = rs.getInt("userid");
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

			Login l = new Login();
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
		System.out.println("test"+op);
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

}
