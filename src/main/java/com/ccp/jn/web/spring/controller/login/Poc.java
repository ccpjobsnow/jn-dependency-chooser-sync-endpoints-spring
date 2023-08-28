package com.ccp.jn.web.spring.controller.login;

import com.ccp.decorators.CcpHashDecorator;
import com.ccp.decorators.CcpStringDecorator;

public class Poc {
	
	public static void main(String[] args) {
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXZ01234567890!@#$%&*()_+=";
		
		CcpStringDecorator csd = new CcpStringDecorator(str);
		String generateToken = csd.text().generateToken(50);
		
		CcpHashDecorator hash = new CcpStringDecorator(generateToken).hash();

		long currentTimeMillis = System.currentTimeMillis();
		int limite = 1000000;
		for(int k = 0; k < limite; k++) {
			hash.asString("MD5");
		}
		long diff = System.currentTimeMillis() - currentTimeMillis;
		currentTimeMillis = System.currentTimeMillis();
		System.out.println(diff);
		for(int k = 0; k < limite; k++) {
			hash.asString("SHA1");
		}
		diff = System.currentTimeMillis() - currentTimeMillis;
		currentTimeMillis = System.currentTimeMillis();
		System.out.println(diff);
		for(int k = 0; k < limite; k++) {
			hash.asString("SHA-256");
		}
		diff = System.currentTimeMillis() - currentTimeMillis;
		currentTimeMillis = System.currentTimeMillis();
		System.out.println(diff);
		for(int k = 0; k < limite; k++) {
			hash.asString("SHA-512");
		}
		diff = System.currentTimeMillis() - currentTimeMillis;
		currentTimeMillis = System.currentTimeMillis();
		System.out.println(diff);
	}
}
