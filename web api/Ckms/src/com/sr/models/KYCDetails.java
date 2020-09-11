package com.sr.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.internal.compiler.ast.WhileStatement;

import com.sr.utilities.DataStoreAccess;

public class KYCDetails {
	private int fk_userid = 0;
	private String fullname;
	private String address;
	private String pannumber;
	private Integer salary;
	private String designation;
	private String employment_type;
	private boolean enable_validate = false;

	ArrayList err = new ArrayList<String>();

	public int getUserid() {
		return fk_userid;
	}

	public void setUserid(int id) {
		this.fk_userid = id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {

		this.fullname = fullname;

	}

	private void validateFullname() {
		// TODO Auto-generated method stub
		int max_length = 100;
		if (fullname.length() > max_length) {
			addError("Full name is too long");
		}

		String whitelist = "^[a-zA-Z]+(\\s[a-zA-Z]+)?$";
		if (!fullname.matches(whitelist)) {
			addError("Only Letters are allowed in Full name");
		}

	}

	private void validateAddress() {
		// TODO Auto-generated method stub
		int max_length = 1000;
		if (address.length() > max_length) {
			addError("Address is too long");
		}

		Pattern p = Pattern.compile("[@_!#$%^&*()<>?/\\|}{~:]");
		Matcher m = p.matcher(address);
		// boolean b = m.matches();
		boolean status = m.find();
		if (status) {
			addError("Special characters are not allowed in address");
		}
	}

	private void validatePan() {
		// TODO Auto-generated method stub
		int max_length = 10;
		if (pannumber.length() > max_length) {
			addError("Invalid Length of Pan Number");
		}

		String whitelist = "^[a-zA-Z0-9]+$";
		if (!pannumber.matches(whitelist)) {
			addError("Only Alpha-numeric Characters are allowed in Pan Number");
		}

	}

	private void validateDesign() {
		// TODO Auto-generated method stub
		int max_length = 50;
		if (designation.length() > max_length) {
			addError("Invalid Length of Designation");
		}

		String whitelist = "^[a-zA-Z]+(\\s[a-zA-Z]+)?$";
		if (!designation.matches(whitelist)) {
			addError("Only Alpha-numeric Characters are allowed in Designation");
		}

	}

	private void validateEmplType() {
		// TODO Auto-generated method stub
		int max_length = 50;
		if (employment_type.length() > max_length) {
			addError("Invalid Length of Employment Type");
		}

		final List<String> validEmp = Arrays.asList("Salaried", "Self-Employed");
		int valid = 0;
		for (int counter = 0; counter < validEmp.size(); counter++) {
			if (employment_type.equals(validEmp.get(counter))) {
				valid = 1;
				break;
			}
		}

		if (valid != 1) {
			addError("Invalid Employment Type. It can be Salaried/Self-Employed");
		}

	}

	public String validate() {

		validateFullname();
		validateAddress();
		validatePan();
		validateDesign();
		validateEmplType();

		String errors = "";
		if (!err.isEmpty()) {
			for (int i = 0; i < err.size(); i++) {

				errors = errors + err.get(i) + ";";
			}
		}

		return errors;
	}

	private void addError(String string) {
		// TODO Auto-generated method stub
		err.add(string);

	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {

		this.address = address;
	}

	public String getPannumber() {
		return pannumber;
	}

	public void setPannumber(String pannumber) {
		this.pannumber = pannumber;
	}

	public int getSalary() {
		return salary.intValue();
	}

	public void setSalary(String salary) {

		try {
			this.salary = Integer.parseInt(salary);
		} catch (Exception e) {
			this.salary = 0;
		}
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEmployment_type() {
		return employment_type;
	}

	public void setEmployment_type(String employment_type) {
		this.employment_type = employment_type;
	}

	public String addKyc() {
		String op = "KYC Details could not be updated, Please try again";
		Connection con = null;
		PreparedStatement stmt = null;
		int count_sqlop = 0;

		boolean recordexists = isRecord(fk_userid);

		try {

			con = DataStoreAccess.getConnection(1);
			String sqlQuery = "";

			if (recordexists) {

				sqlQuery = "UPDATE kycdetails SET fullname = ?, address = ?, pannumber = ?, emp_type = ?, designation = ?, salary = ? where fk_userid = ?";

			} else {

				sqlQuery = "insert into kycdetails (fullname, address, pannumber, emp_type, designation, salary, fk_userid) values (?,?,?,?,?,?,?)";

			}

			stmt = (PreparedStatement) con.prepareStatement(sqlQuery);

			stmt.setString(1, fullname);
			stmt.setString(2, address);
			stmt.setString(3, pannumber);
			stmt.setString(4, employment_type);
			stmt.setString(5, designation);
			stmt.setInt(6, salary);
			stmt.setInt(7, fk_userid);

			count_sqlop = stmt.executeUpdate();
			if (count_sqlop == 1) {
				op = "KYC Details Updated Successfully";
			}
		} catch (Exception e) {
			op = op + " - " + e.getMessage();
		}

		return op;

	}

	private boolean isRecord(int userid) {
		// TODO Auto-generated method stub

		boolean status = false;
		Connection con = null;
		PreparedStatement stmt = null;

		try {

			con = DataStoreAccess.getConnection(1);
			String loginQuery = "select * from kycdetails where fk_userid = ?";
			stmt = (PreparedStatement) con.prepareStatement(loginQuery);

			stmt.setInt(1, fk_userid);

			ResultSet rs = (ResultSet) stmt.executeQuery();

			while (rs.next()) {
				status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return status;

	}

	public HashMap checkEligibility() {
		HashMap eligibility = null;
		return eligibility;
	}

	public static void main(String[] args) {
		KYCDetails kc = new KYCDetails();

		kc.setFullname("Sam Daniel");
		kc.setAddress("200, testing, test building, new delhi");
		kc.setDesignation("Manager");
		kc.setEmployment_type("Salaried");
		kc.setPannumber("AND123DR2");
		kc.setSalary("200000");
		String op = kc.validate();

		System.out.println(op);

		// kc.setUserid(900);
		// kc.init();
		HashMap eligibility_status = kc.getEligibility();
		System.out.println("Status: " + eligibility_status.get("status"));
		System.out.println("Card Type: " + eligibility_status.get("card_type"));
	}

	public void init() {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DataStoreAccess.getConnection(1);
			String loginQuery = "select * from kycdetails where fk_userid = ?";
			stmt = (PreparedStatement) con.prepareStatement(loginQuery);

			stmt.setInt(1, fk_userid);
			ResultSet rs = (ResultSet) stmt.executeQuery();

			while (rs.next()) {
				fullname = rs.getString("fullname");
				address = rs.getString("address");
				pannumber = rs.getString("pannumber");
				employment_type = rs.getString("emp_type");
				designation = rs.getString("designation");
				salary = rs.getInt("salary");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public HashMap getEligibility() {
		// TODO Auto-generated method stub

		HashMap eligibility = new HashMap();
		String card_type = "";
		String status = "";

		if (employment_type != "" && employment_type.equals("Salaried")) {

			if (salary > 0) {
				status = "Approved";

				if (salary > 50000 && salary <= 200000) {
					card_type = "Silver";
				} else if (salary <= 600000 && salary > 200000) {
					card_type = "Gold";
				} else if (salary > 600000) {
					card_type = "Premium";
				} else {
					card_type = "basic";
				}
			} else {
				card_type = "Not Applicable";
				status = "Not Applicable";
			}

		} else if (employment_type != "" && !employment_type.equals("Salaried")) {
			card_type = "Not Applicable";
			status = "Not Applicable";
		}

		eligibility.put("status", status);
		eligibility.put("card_type", card_type);

		return eligibility;
	}

	public void enableValidation() {
		// TODO Auto-generated method stub

		this.enable_validate = true;

	}

}
