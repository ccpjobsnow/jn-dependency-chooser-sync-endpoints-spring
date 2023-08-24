package com.ccp.jn.web.spring.controller.login;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.jn.sync.login.controller.RequestToken;

@CrossOrigin
@RestController
@RequestMapping(value = "/login/{email}/token/request", method = RequestMethod.POST)
public class RequestTokenController {

	private final RequestToken injected = new RequestToken();

	public Map<String, Object> execute(@PathVariable("email") String email) {
		
		CcpMapDecorator execute = this.injected.execute(email);
		return execute.content;
	}

	
}
