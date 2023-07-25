package com.ccp.jn.web.spring.application;

import com.ccp.decorators.CcpMapDecorator;
import com.jn.commons.JnBusinessEntity;

public class GenerateId {

	
	public static void main(String[] args) {
		
		String id = JnBusinessEntity.values.getId(new CcpMapDecorator().put("id", "sendEmail").put("language", "portuguese"));
		
		System.out.println(id);
		
	}
}
