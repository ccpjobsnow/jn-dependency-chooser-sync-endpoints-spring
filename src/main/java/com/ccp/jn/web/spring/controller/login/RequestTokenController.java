package com.ccp.jn.web.spring.controller.login;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.jn.sync.login.controller.RequestToken;

@CrossOrigin
@RestController
@RequestMapping(value = "/login/{email}/token/request", method = RequestMethod.POST)
public class RequestTokenController {

	private RequestToken injected = CcpDependencyInjection.getInjected(RequestToken.class);

	public void execute(@PathVariable("email") String email) {
		this.injected.execute(email);
	}

	
}
