package com.ccp.jn.web.spring.controller.login;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.jn.sync.login.controller.UpdatePassword;

@CrossOrigin
@RestController
@RequestMapping(value = "/login/{email}/password")
public class UpdatePasswordController {
	
	private final UpdatePassword injected = new UpdatePassword();
	
	@PostMapping
	public Map<String, Object> execute(@PathVariable("email") String email, @RequestBody Map<String, Object> requestBody) {
		CcpMapDecorator put = new CcpMapDecorator(requestBody).put("email", email);
		CcpMapDecorator execute = this.injected.execute(put);
		return execute.content;
	}
}
