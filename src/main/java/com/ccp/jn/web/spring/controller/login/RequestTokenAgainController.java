package com.ccp.jn.web.spring.controller.login;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.jn.sync.login.controller.RequestTokenAgain;

@CrossOrigin
@RestController
@RequestMapping(value = "/login/{email}/token/request", method = RequestMethod.PATCH)
public class RequestTokenAgainController {

	private final RequestTokenAgain injected = CcpDependencyInjection.getInjected(RequestTokenAgain.class);
	
	public void execute(@PathVariable("email") String email) {
		this.injected.execute(email);
	}

}
