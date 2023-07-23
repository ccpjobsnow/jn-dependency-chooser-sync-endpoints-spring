package com.ccp.jn.web.spring.controller.login;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.jn.sync.login.controller.ExistsLoginToken;

@CrossOrigin
@RestController
@RequestMapping(value = "/login/{email}/token")
public class ExistsLoginTokenController {
	private ExistsLoginToken injected = CcpDependencyInjection.getInjected(ExistsLoginToken.class);

	@RequestMapping(method = RequestMethod.HEAD)
	public void execute(@PathVariable("email")String email) {
		this.injected.execute(email);
	}
	
}
