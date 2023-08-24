package com.ccp.jn.web.spring.controller.login;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.jn.sync.login.controller.RequestUnlockToken;

@CrossOrigin
@RestController
@RequestMapping(value = "/login/{email}/token/lock", method = RequestMethod.HEAD)
public class RequestUnlockTokenController {
	
	private final RequestUnlockToken injected = new RequestUnlockToken();

	public Map<String, Object> execute(@PathVariable("email") String email) {
		CcpMapDecorator execute = this.injected.execute(email);
		return execute.content;
	}


}
