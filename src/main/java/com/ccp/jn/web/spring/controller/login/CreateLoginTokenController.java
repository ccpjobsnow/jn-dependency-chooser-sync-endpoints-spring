package com.ccp.jn.web.spring.controller.login;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.jn.sync.login.controller.CreateLoginToken;

@CrossOrigin
@RestController
@RequestMapping("/login/{email}/token/language/{language}")
public class CreateLoginTokenController {
	
	private final CreateLoginToken injected = CcpDependencyInjection.getInjected(CreateLoginToken.class);

	@PostMapping
	public void execute(@PathVariable("email") String email, @PathVariable("language")String language) {
		this.injected.execute(email, language);
	}

}
