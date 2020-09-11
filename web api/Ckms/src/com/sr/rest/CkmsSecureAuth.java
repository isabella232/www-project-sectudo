package com.sr.rest;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import com.sr.annotation.Decryption;
import com.sr.models.SecureLogin;
import com.sr.utilities.Constants;
import com.sr.utilities.EncryptionUtil;
import com.sr.utilities.GeneralUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Path("/secure/authservice")
public class CkmsSecureAuth {

	@GET
	@Decryption
	@Path("/sreflect/{id}")
	public Response sreflect(@PathParam("id") String id) {
		return Response.status(200).entity(id).build();

	}

	@POST
	@Path("/login")
	@Consumes("application/json")
	@Produces("application/json")
	public Response login(InputStream incomingData, @Context HttpServletRequest request,
			@QueryParam("enc_mode") String encryption_mode) {

		String op = "Invalid Login";
		String encryptedRes = "";
		try {
			JSONObject opJSON = new JSONObject();
			JSONObject reqJSON;

			if (encryption_mode.toLowerCase().equals("y")) {
				reqJSON = GeneralUtil.getDecryptedJSON(incomingData);
			} else {
				reqJSON = GeneralUtil.getJSON(incomingData);
			}

			String username = reqJSON.getString("username");
			String userpass = reqJSON.getString("password");
			String token = EncryptionUtil.decode(reqJSON.getString("token"));

			SecureLogin newlogin = new SecureLogin();
			newlogin.init(username);

			String storedpass = newlogin.getPassword();

			String storedPasstoken = storedpass + token;
			String genePass = EncryptionUtil.generateMessageDigest(storedPasstoken);

			String key = token;
			String userid = "";

			if (userpass.equals(genePass)) {
				op = "Login Successful";
				userid = newlogin.getUserid();
				String sessionID = issueToken(userid, request);
				opJSON.put("token", sessionID);

			} else {

				invalidateSession(request);
			}

			opJSON.put("status", op);
			
			if (encryption_mode.toLowerCase().equals("y")) {

			encryptedRes = EncryptionUtil.symEncryp(opJSON.toString(), key);
			System.out.println(encryptedRes);
			} else {
				encryptedRes = opJSON.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
			String error = "{\"status\":\"Error occurred, Please try again\"}";
			return Response.status(200).entity(error).build();
		}

		return Response.status(200).entity(encryptedRes).build();
	}

	@GET
	@Path("/logout")
	@Produces("application/json")
	public Response logout(InputStream incomingData,@Context HttpServletRequest request) {
		JSONObject opJSON = new JSONObject();
		try {

			invalidateSession(request);
			opJSON.put("status", "Logout Successful");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return Response.status(200).entity("Error - " + e.getMessage()).build();
		}
		return Response.status(200).entity(opJSON.toString()).build();

	}

	private void invalidateSession(HttpServletRequest request) throws Exception {

		if (Constants.getSessMode().equals("JSE")) {
			HttpSession session = request.getSession();
			session.invalidate();
		} else if (Constants.getSessMode().equals("JWT")) {
			/*
			 * SecretKey skey = new SecretKeySpec(Constants.key.getBytes("UTF-8"), "AES");
			 * String auth_header = request.getHeader("Authorization"); String jwttoken =
			 * auth_header.substring("Bearer".length()).trim();
			 * 
			 * Jws<Claims> claims =
			 * Jwts.parser().setSigningKey(skey).parseClaimsJws(jwttoken); String useridStr
			 * = claims.getBody().getSubject(); int sessCount = (int)
			 * claims.getBody().get("sequence"); SecureLogin l = new SecureLogin();
			 * l.init(useridStr); l.removeSession(sessCount);
			 */

		}
	}

	private String issueToken(String userid, HttpServletRequest request) throws Exception {
		String token = "";
		if (Constants.getSessMode().equals("JSE")) {

			HttpSession session = request.getSession(true);
			String sessionID = session.getId();
			session.setAttribute("userid", userid + "");
			token = sessionID;

		} else if (Constants.getSessMode().equals("JWT")) {
			token = getJWTToken(userid, request);
			/*
			 * SecureLogin newlogin = new SecureLogin(); newlogin.init(userid);
			 * newlogin.setActiveSession();
			 */
		}
		return token;

	}

	private String getJWTToken(String userid, HttpServletRequest request) throws Exception {

		String uid = userid;
		// int newCount = sessCount+1;

		SecretKey secretKey = new SecretKeySpec(Constants.key.getBytes("UTF-8"), "AES");
		LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(15L);
		Date expirydate = Date.from(expiryTime.atZone(ZoneId.systemDefault()).toInstant());

		/*
		 * String jwtToken =
		 * Jwts.builder().setSubject(uid).setIssuer("www.synradar.com").setIssuedAt(new
		 * Date()) .setExpiration(expirydate).claim("sequence",
		 * newCount).signWith(SignatureAlgorithm.HS512, secretKey) .compact();
		 */
		String encryptedUid = EncryptionUtil.symEncryp(uid, Constants.key);

		String jwtToken = Jwts.builder().setSubject(encryptedUid).setIssuer("www.synradar.com").setIssuedAt(new Date())
				.setExpiration(expirydate).signWith(SignatureAlgorithm.HS512, secretKey).compact();

		return jwtToken;
	}

	public static void main(String[] args) {
		try {
			CkmsSecureAuth ck = new CkmsSecureAuth();
			String token = ck.issueToken("123", null);
			System.out.println(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
