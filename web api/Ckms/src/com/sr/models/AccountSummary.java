package com.sr.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.sr.utilities.DataStoreAccess;

public class AccountSummary {
	private int fk_userid;
	private ArrayList<AccountDetails> all_accts = new ArrayList<AccountDetails>();

	public void setAccounts(AccountDetails singleact) {
		all_accts.add(singleact);
	}

	public ArrayList getAllAccounts() {
		return all_accts;
	}
	
	public static void main(String[] args) {

		AccountSummary as = new AccountSummary();
		as.setFk_userid(900);
		as.init();
		ArrayList all_accts = as.getAllAccounts();
		
		for (int counter = 0; counter < all_accts.size(); counter++) {
			AccountDetails ad = (AccountDetails) all_accts.get(counter);
			System.out.println(ad.getAcct_id());
			System.out.println(ad.getAcct_name());
			System.out.println(ad.getBalance() );
		}

	}
	
	public int getFk_userid() {
		return fk_userid;
	}

	public void setFk_userid(int fk_userid) {
		this.fk_userid = fk_userid;
	}

	public void init() {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = DataStoreAccess.getConnection(1);
			String loginQuery = "select * from accountstable where userid = " + fk_userid;
			stmt = (PreparedStatement) con.prepareStatement(loginQuery);

			ResultSet rs = (ResultSet) stmt.executeQuery();

			while (rs.next()) {
				AccountDetails singleact = new AccountDetails();
				singleact.setAcct_id(rs.getString("account_id"));
				singleact.setAcct_name(rs.getString("accountname"));
				singleact.setBalance(rs.getString("balance"));

				this.setAccounts(singleact);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
