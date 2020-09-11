package com.sr.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
@com.sr.annotation.Decryption
@Priority(Priorities.AUTHENTICATION)
public class Decryption implements ContainerRequestFilter{

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// TODO Auto-generated method stub
		
		InputStream ip = requestContext.getEntityStream();		
		System.out.println(ip.toString());
		
		System.out.println("I am getting applied...Hurray!!!");
		
	}

}
