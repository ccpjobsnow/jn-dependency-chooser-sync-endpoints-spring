package com.ccp.jn.web.spring.controller.login;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.jn.sync.login.controller.Logout;

@CrossOrigin
@RestController
@RequestMapping(value = "/login/{email}")
public class LogoutController {

	private final Logout injected = new Logout();
	
	@DeleteMapping
	public void execute(@PathVariable("email") String email) {
		this.injected.execute(email);
	}
	
}
