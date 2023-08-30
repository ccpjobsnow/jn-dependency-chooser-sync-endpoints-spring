package com.ccp.jn.web.spring.controller.login;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.jn.sync.login.controller.UnlockToken;

@CrossOrigin
@RestController
@RequestMapping(value = "/login/{email}/token/lock")
public class UnlockTokenController {

	private final UnlockToken injected = new UnlockToken();
	
	@DeleteMapping
	public Map<String, Object> execute(@PathVariable("email") String email, @RequestBody Map<String, Object> body) {
		CcpMapDecorator execute = this.injected.execute(new CcpMapDecorator(body).put("email", email));
		return execute.content;
	}
	
}
