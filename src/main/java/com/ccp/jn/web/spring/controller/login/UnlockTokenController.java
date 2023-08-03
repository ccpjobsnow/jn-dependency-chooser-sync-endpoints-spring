package com.ccp.jn.web.spring.controller.login;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.jn.sync.login.controller.UnlockToken;

@CrossOrigin
@RestController
@RequestMapping(value = "/login/{email}/token/lock", method = RequestMethod.DELETE)
public class UnlockTokenController {

	private final UnlockToken injected = CcpDependencyInjection.getInjected(UnlockToken.class);

	public Map<String, Object> execute(@PathVariable("email") String email) {
		CcpMapDecorator execute = this.injected.execute(email);
		return execute.content;
	}
	
}
