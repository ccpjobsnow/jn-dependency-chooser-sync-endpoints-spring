package com.ccp.jn.web.spring.application;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.decorators.CcpStringDecorator;

public class GenerateJson {
	public static void main(String[] args) {
		String extractStringContent = new CcpStringDecorator("C:\\Users\\Onias\\Downloads\\token.html").file().extractStringContent();
	
		CcpMapDecorator put = new CcpMapDecorator().put("value", extractStringContent).put("id", "sendUserToken");
		System.out.println(put);
	}
}
