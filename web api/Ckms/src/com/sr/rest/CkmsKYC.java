package com.sr.rest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.sr.models.AccountDetails;
import com.sr.models.AccountSummary;
import com.sr.models.KYCDetails;
import com.sr.models.Login;
import com.sr.utilities.GeneralUtil;

@Path("/kycservice")
public class CkmsKYC {

	@GET
	@Path("/reflect/{id}")
	public Response reflect(@PathParam("id") String id) {
		return Response.status(200).entity(id).build();

	}

	@GET
	@Path("/showheader")
	public Response test(@HeaderParam("Authorization") String auth) {
		String token = auth.substring("Bearer".length()).trim();
		return Response.status(200).entity(token).build();

	}

	@GET
	@Path("/showdetails")
	@Produces("text/html")
	public Response showkyc(@HeaderParam("Authorization") String auth) {

		String op = "Record could not be fetched, Try Again";
		String opHTML = new String();

		try {
			int userid = verifyAuth(auth);

			if (userid == 0) {
				op = "Access Denied";
				opHTML = "<html><head><title>KYC Details</title><body>"+op+"</body></html>";
				return Response.status(200).entity(opHTML).build();
			}

			KYCDetails kyc = new KYCDetails();
			kyc.setUserid(userid);
			kyc.init();
			String fullname =  kyc.getFullname();
			String address = kyc.getAddress();
			String pannumber = kyc.getPannumber();
			String employmemt_type = kyc.getEmployment_type();
			String designation = kyc.getDesignation();
			String salary = kyc.getSalary()+"";
			
			opHTML = "<html><head><style>tr{text-align: left;}</style><title>KYC Details</title></head><body><br>"
					+ "<table cellpadding='5' cellspacing='10'>"
					+ "<tr><th>Full Name</th><td>"+fullname+"</td>"
					+ "</tr>"
					+ "<tr><th>Address</th><td>"+address+"</td>"
					+ "</tr>"
					+ "<tr><th>Pan Number</th><td>"+pannumber+"</td>"
					+ "</tr>"
					+ "<tr><th>Employment Type</th><td>"+employmemt_type+"</td>"
					+ "</tr>"
					+ "<tr><th>Designation</th><td>"+designation+"</td>"
					+ "</tr>"
					+ "<tr><th>Salary</th><td>"+salary+"</td>"
					+ "</tr>"
					+ "</table></body>"
					+ "</html>";
			
		} catch (Exception e) {
			op = "Error Occurred - " + e.getMessage();
			opHTML = "<html><head><title>KYC Details</title><body>"+op+"</body></html>";
			return Response.status(200).entity(opHTML).build();
		}

		return Response.status(200).entity(opHTML).build();
	}

	@GET
	@Path("/geteligibility")
	@Produces("application/json")
	public Response getEligibility(@HeaderParam("Authorization") String auth) {

		String op = "Record could not be fetched, Try Again";
		JSONObject opJSON = new JSONObject();

		try {
			int userid = verifyAuth(auth);

			if (userid == 0) {
				op = "Access Denied";
				opJSON.put("Status", op);
				return Response.status(200).entity(opJSON.toString()).build();
			}

			KYCDetails kyc = new KYCDetails();
			kyc.setUserid(userid);
			kyc.init();
			HashMap eligibility_status = kyc.getEligibility();

			opJSON.put("status", eligibility_status.get("status"));
			opJSON.put("card_type", eligibility_status.get("card_type"));

		} catch (Exception e) {
			op = "Error Occurred - " + e.getMessage();
			return Response.status(200).entity(op.toString()).build();
		}

		return Response.status(200).entity(opJSON.toString()).build();
	}

	@POST
	@Path("/add")
	@Consumes("application/json")
	@Produces("application/json")
	public Response addkyc(InputStream incomingData, @HeaderParam("Authorization") String auth) {

		String op = "Insert Failed, Try Again";
		JSONObject opJSON = new JSONObject();

		try {
			int userid = verifyAuth(auth);

			if (userid == 0) {
				op = "Access Denied";
				opJSON.put("Status", op);
				return Response.status(200).entity(opJSON.toString()).build();
			}

			JSONObject reqJSON = GeneralUtil.getJSON(incomingData);
			KYCDetails kyc = new KYCDetails();
			kyc.setFullname(reqJSON.getString("fullname"));
			kyc.setAddress(reqJSON.getString("address"));
			kyc.setPannumber(reqJSON.getString("pannumber"));
			kyc.setEmployment_type(reqJSON.getString("employment_type"));
			kyc.setDesignation(reqJSON.getString("designation"));
			kyc.setSalary(reqJSON.getString("salary"));
			kyc.setUserid(userid);

			op = kyc.addKyc();

			opJSON.put("Status", op);

		} catch (Exception e) {
			op = "Error Occurred - " + e.getMessage();
			return Response.status(200).entity(op.toString()).build();
		}

		return Response.status(200).entity(opJSON.toString()).build();

	}

	@GET
	@Path("/acctdetails/{acct_id}")
	@Produces("application/json")
	public Response showaccount(@PathParam("acct_id") String acct_id, @HeaderParam("Authorization") String auth) {

		String op = "Record Could not be Fetched, Try Again";
		JSONObject opJSON = new JSONObject();

		try {
			int userid = verifyAuth(auth);

			if (userid == 0) {
				op = "Access Denied";
				opJSON.put("Status", op);
				return Response.status(200).entity(opJSON.toString()).build();
			}

			AccountDetails ad = new AccountDetails();
			ad.setAcct_id(acct_id);
			ad.init();

			opJSON.put("account_id", ad.getAcct_id());
			opJSON.put("account_name", ad.getAcct_name());
			opJSON.put("account_balance", ad.getBalance());
			opJSON.put("userid", ad.getFk_userid());

		} catch (Exception e) {
			op = "Error Occurred - " + e.getMessage();
			return Response.status(200).entity(op.toString()).build();
		}

		return Response.status(200).entity(opJSON.toString()).build();

	}

	@GET
	@Path("/acctsum")
	@Produces("application/json")
	public Response showallaccounts(@HeaderParam("Authorization") String auth) {

		String op = "Record Could not be Fetched, Try Again";
		JSONObject opJSON = new JSONObject();
		JSONArray jsonarr = new JSONArray();

		try {
			int userid = verifyAuth(auth);

			if (userid == 0) {
				op = "Access Denied";
				opJSON.put("Status", op);
				return Response.status(200).entity(opJSON.toString()).build();
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
	public Response showhome(@HeaderParam("Authorization") String auth) {

		String op = "Record Could not be Fetched, Try Again";
		JSONObject opJSON = new JSONObject();

		try {
			int userid = verifyAuth(auth);

			if (userid == 0) {
				op = "Access Denied";
				opJSON.put("Status", op);
				return Response.status(200).entity(opJSON.toString()).build();
			}

			AccountSummary acctas = new AccountSummary();
			acctas.setFk_userid(userid);
			acctas.init();
			ArrayList arr = acctas.getAllAccounts();

			//getting only 1st record
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

	private int verifyAuth(String auth) throws Exception {

		try {
			String useridstr = auth.substring("Bearer".length()).trim();
			int userid = Integer.parseInt(useridstr);
			Login l = new Login();
			l.setUserid(userid);
			boolean status = l.verifyuser(userid);
			if (status) {
				return userid;
			}
		} catch (Exception e) {
			return 0;
		}

		return 0;
	}
}
