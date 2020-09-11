package com.sr.rest;

import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import com.sr.models.Login;
import com.sr.utilities.GeneralUtil;

@Path("/authservice")
public class CkmsAuth {

	@POST
	@Path("/login")
	@Consumes("application/json")
	@Produces("application/json")
	public Response login(InputStream incomingData) {

		String op = "Invalid Login";
		JSONObject opJSON = new JSONObject();
		try {
			JSONObject reqJSON = GeneralUtil.getJSON(incomingData);

			String username = reqJSON.getString("username");
			String password = reqJSON.getString("password");

			Login newlogin = new Login();
			newlogin.setUsername(username);
			newlogin.setPassword(password);

			boolean result = newlogin.validateLogin();
			int userid = 0;

			if (result) {
				op = "Login Successful";
				userid = newlogin.getUserid();
				opJSON.put("token", userid);
			}
			
			opJSON.put("Status", op);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.status(200).entity(opJSON.toString()).build();
	}

	private String issueToken(int userid) {

		return userid + "";
	}

}
