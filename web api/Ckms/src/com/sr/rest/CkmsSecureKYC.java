package com.sr.rest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.text.StringEscapeUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.sr.models.AccountDetails;
import com.sr.models.AccountSummary;
import com.sr.models.KYCDetails;
import com.sr.models.Login;
import com.sr.utilities.Constants;
import com.sr.utilities.EncryptionUtil;
import com.sr.utilities.GeneralUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

@Path("/secure/kycservice")
public class CkmsSecureKYC {

	@GET
	@Path("/test")
	public Response test(@Context HttpServletRequest request) {

		String op = "";
		JSONObject opJSON = new JSONObject();
		try {
			String useridStr = verifyAuth(request);

			if (useridStr.equals("")) {
				op = "Access Denied";

			} else {
				op = "Authenticated Access";
				opJSON.put("userid", useridStr);

			}
			opJSON.put("Status", op);

		} catch (Exception e) {
			return Response.status(200).entity("Error Occured").build();
		}

		return Response.status(200).entity(opJSON.toString()).build();

	}

	@GET
	@Path("/showdetails")
	@Produces("text/html")
	public Response showkyc(@Context HttpServletRequest request) {

		String op = "Record could not be fetched, Try Again";
		String opHTML = new String();

		try {
			String useridStr = verifyAuth(request);
			int userid = 0;

			if (useridStr.equals("")) {
				op = "Access Denied";
				opHTML = "<html><head><title>KYC Details</title><body>" + op + "</body></html>";
				return Response.status(200).entity(opHTML).build();
			} else {
				userid = Integer.parseInt(useridStr);
			}

			KYCDetails kyc = new KYCDetails();
			kyc.setUserid(userid);
			kyc.init();

			String fullname = StringEscapeUtils.escapeHtml4(kyc.getFullname());
			String address = StringEscapeUtils.escapeHtml4(kyc.getAddress());
			String pannumber = StringEscapeUtils.escapeHtml4(kyc.getPannumber());
			String employmemt_type = StringEscapeUtils.escapeHtml4(kyc.getEmployment_type());
			String designation = StringEscapeUtils.escapeHtml4(kyc.getDesignation());
			String salary = StringEscapeUtils.escapeHtml4(kyc.getSalary() + "");

			opHTML = "<html><head><style>tr{text-align: left;}</style><title>KYC Details</title></head><body><br>"
					+ "<table cellpadding='5' cellspacing='10'>" + "<tr><th>Full Name</th><td>" + fullname + "</td>"
					+ "</tr>" + "<tr><th>Address</th><td>" + address + "</td>" + "</tr>" + "<tr><th>Pan Number</th><td>"
					+ pannumber + "</td>" + "</tr>" + "<tr><th>Employment Type</th><td>" + employmemt_type + "</td>"
					+ "</tr>" + "<tr><th>Designation</th><td>" + designation + "</td>" + "</tr>"
					+ "<tr><th>Salary</th><td>" + salary + "</td>" + "</tr>" + "</table></body>" + "</html>";
		
		} catch (Exception e) {
			op = "Error Occurred - " + e.getMessage();
			opHTML = "<html><head><title>KYC Details</title><body>" + op + "</body></html>";
			return Response.status(200).entity(opHTML).header("cache-control", "no-store").build();
		}

		return Response.status(200).entity(opHTML).build();
	}

	@POST
	@Path("/geteligibility")
	@Produces("application/json")
	public Response getEligibility(InputStream incomingData, @Context HttpServletRequest request) {

		String op = "Insert Failed, Try Again";
		JSONObject opJSON = new JSONObject();
		request.getSession();
		String plaintext_op = "";
		String encrypted_op = "";

		try {
			String useridStr = verifyAuth(request);
			int userid = 0;

			if (useridStr.equals("")) {
				op = "Access Denied";
				opJSON.put("Status", op);
				return Response.status(200).entity(opJSON.toString()).build();
			} else {
				userid = Integer.parseInt(useridStr);
			}

			JSONObject reqJSON = GeneralUtil.getDecryptedJSON(incomingData);
			String token = reqJSON.getString("token");

			KYCDetails kyc = new KYCDetails();
			kyc.setUserid(userid);
			kyc.init();
			HashMap eligibility_status = kyc.getEligibility();

			opJSON.put("status", eligibility_status.get("status"));
			opJSON.put("card_type", eligibility_status.get("card_type"));

			plaintext_op = opJSON.toString();
			encrypted_op = EncryptionUtil.symEncryp(plaintext_op, token);

		} catch (Exception e) {
			op = "Error Occurred - " + e.getMessage();
			return Response.status(200).entity(op.toString()).build();
		}

		return Response.status(200).entity(encrypted_op).build();
	}

	@POST
	@Path("/add")
	@Consumes("application/json")
	@Produces("application/json")
	public Response addkyc(InputStream incomingData, @Context HttpServletRequest request) {

		String op = "Insert Failed, Try Again";
		JSONObject opJSON = new JSONObject();
		request.getSession();

		try {
			String useridStr = verifyAuth(request);
			int userid = 0;

			if (useridStr.equals("")) {
				op = "Access Denied";
				opJSON.put("Status", op);
				return Response.status(200).entity(opJSON.toString()).build();
			} else {
				userid = Integer.parseInt(useridStr);
			}

			JSONObject reqJSON = GeneralUtil.getDecryptedJSON(incomingData);
			KYCDetails kyc = new KYCDetails();

			String fullname = reqJSON.getString("fullname");
			String address = reqJSON.getString("address");
			String pan_num = reqJSON.getString("pannumber");
			String emp_type = reqJSON.getString("employment_type");
			String designation = reqJSON.getString("designation");
			String salary = reqJSON.getString("salary");

			kyc.setFullname(fullname);
			kyc.setAddress(address);
			kyc.setPannumber(pan_num);
			kyc.setEmployment_type(emp_type);
			kyc.setDesignation(designation);
			kyc.setSalary(salary);
			kyc.setUserid(userid);

			String errors = kyc.validate();
			if (!errors.equals("")) {
				opJSON.put("Status", op);
				opJSON.put("error", errors);
				return Response.status(200).entity(opJSON.toString()).build();
			}

			op = kyc.addKyc();
			opJSON.put("Status", op);

		} catch (Exception e) {
			e.printStackTrace();
			op = "Error Occurred - " + e.getMessage();
			return Response.status(200).entity(op.toString()).build();
		}
		

		return Response.status(200).entity(opJSON.toString()).build();

	}

	@GET
	@Path("/acctdetails/{acct_id}")
	@Produces("application/json")
	public Response showaccount(@PathParam("acct_id") String acct_id, @Context HttpServletRequest request) {

		String op = "Record Could not be Fetched, Try Again";
		JSONObject opJSON = new JSONObject();

		try {
			String useridStr = verifyAuth(request);
			int userid = 0;

			if (useridStr.equals("")) {
				op = "Access Denied";
				opJSON.put("Status", op);
				return Response.status(200).entity(opJSON.toString()).build();
			} else {
				userid = Integer.parseInt(useridStr);
			}

			AccountDetails ad = new AccountDetails();
			ad.setSecureMode();
			ad.setAcct_id(acct_id);
			ad.setFk_userid(userid);
			boolean status = ad.secureInit();

			if (!status) {
				opJSON.put("error", "No Record Found");
			} else {

				opJSON.put("account_id", ad.getAcct_id());
				opJSON.put("account_name", ad.getAcct_name());
				opJSON.put("account_balance", ad.getBalance());
				opJSON.put("userid", userid);
			}
		} catch (Exception e) {
			op = "Error Occurred - " + e.getMessage();
			return Response.status(200).entity(op.toString()).build();
		}

		return Response.status(200).entity(opJSON.toString()).build();

	}

	@GET
	@Path("/acctsum")
	@Produces("application/json")
	public Response showallaccounts(@Context HttpServletRequest request) {

		String op = "Record Could not be Fetched, Try Again";
		JSONObject opJSON = new JSONObject();
		JSONArray jsonarr = new JSONArray();

		try {
			String useridStr = verifyAuth(request);
			int userid = 0;

			if (useridStr.equals("")) {
				op = "Access Denied";
				opJSON.put("Status", op);
				return Response.status(200).entity(opJSON.toString()).build();
			} else {
				userid = Integer.parseInt(useridStr);
			}

			AccountSummary acctas = new AccountSummary();
			acctas.setFk_userid(userid);
			acctas.init();
			ArrayList arr = acctas.getAllAccounts();

			for (int counter = 0; counter < arr.size(); counter++) {
				AccountDetails ad = (AccountDetails) arr.get(counter);
				JSONObject singleacct = new JSONObject();
				singleacct.put("account_id", ad.getAcct_id());
				singleacct.put("account_name", ad.getAcct_name());
				singleacct.put("account_balance", ad.getBalance());
				singleacct.put("userid", ad.getFk_userid());

				jsonarr.put(singleacct);

			}

		} catch (Exception e) {
			op = "Error Occurred - " + e.getMessage();
			return Response.status(200).entity(op.toString()).build();
		}

		return Response.status(200).entity(jsonarr.toString()).build();
	}

	@GET
	@Path("/home")
	@Produces("application/json")
	public Response showhome(@Context HttpServletRequest request) {

		String op = "Record Could not be Fetched, Try Again";
		JSONObject opJSON = new JSONObject();

		try {
			String useridStr = verifyAuth(request);
			int userid = 0;

			if (useridStr.equals("")) {
				op = "Access Denied";
				opJSON.put("Status", op);
				return Response.status(200).entity(opJSON.toString()).build();
			} else {
				userid = Integer.parseInt(useridStr);
			}

			AccountSummary acctas = new AccountSummary();
			acctas.setFk_userid(userid);
			acctas.init();
			ArrayList arr = acctas.getAllAccounts();

			// getting only 1st record
			AccountDetails ad = (AccountDetails) arr.get(0);
			KYCDetails kyc = new KYCDetails();
			kyc.setUserid(userid);
			kyc.init();

			opJSON.put("userid", userid);
			opJSON.put("fullname", kyc.getFullname());
			opJSON.put("account_id", ad.getAcct_id());
			opJSON.put("account_name", ad.getAcct_name());
			opJSON.put("account_balance", ad.getBalance());

		} catch (Exception e) {
			op = "Error Occurred - " + e.getMessage();
			return Response.status(200).entity(op.toString()).build();
		}

		return Response.status(200).entity(opJSON.toString()).build();
	}

	private String verifyAuth(HttpServletRequest request) throws Exception {

		String userid = "";
		if (Constants.getSessMode().equals("JSE")) {
			HttpSession session = request.getSession();
			String uid = (String) session.getAttribute("userid");
			if (uid != null) {
				userid = uid;
			}
		} else if (Constants.getSessMode().equals("JWT")) {

			String auth_header = request.getHeader("Authorization");
			String jwttoken = auth_header.substring("Bearer".length()).trim();
			userid = verifyJWTToken(jwttoken);

		}

		return userid;
	}

	private String verifyJWTToken(String jwttoken) {
		// TODO Auto-generated method stub
		String useridStr = "";
		try {
			SecretKey skey = new SecretKeySpec(Constants.key.getBytes("UTF-8"), "AES");
			Jws<Claims> claims = Jwts.parser().setSigningKey(skey).parseClaimsJws(jwttoken);
			String encryptedStr = claims.getBody().getSubject();
			useridStr = EncryptionUtil.symDecrypt(encryptedStr, Constants.key);

		} catch (SignatureException e) {
			useridStr = "";
		} catch (Exception e) {
			useridStr = "";
		}
		return useridStr;
	}

}
