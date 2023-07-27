package com.ccp.jn.web.spring.application;

import com.ccp.decorators.CcpMapDecorator;
import com.jn.commons.JnEntity;

public class GenerateId {

	
	public static void main(String[] args) {
		
		long ontem = System.currentTimeMillis() - 86400000L;
		String id1 = JnEntity.email_message_sent.getId(new CcpMapDecorator("{\r\n" + 
				"  \"email\": \"onias@ccpjobsnow.com\",\r\n" + 
				"  \"language\": \"portuguese\"\r\n" + 
				"  ,\"_time\": \"26072023\"\r\n" + 
				"}").put("_time", ontem));
		String id2 = JnEntity.email_message_sent.getId(new CcpMapDecorator("{\r\n" + 
				"  \"email\": \"onias@ccpjobsnow.com\",\r\n" + 
				"  \"language\": \"portuguese\"\r\n" + 
				"}").put("_time", System.currentTimeMillis()));
		
		System.out.println(id1);
		System.out.println(id2);
		
	}
}
