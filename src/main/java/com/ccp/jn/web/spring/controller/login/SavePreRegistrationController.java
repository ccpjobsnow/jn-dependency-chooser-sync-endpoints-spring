package com.ccp.jn.web.spring.controller.login;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.jn.sync.login.controller.SavePreRegistration;

@CrossOrigin
@RestController
@RequestMapping(value = "/login/{email}/pre-registration")
public class SavePreRegistrationController {

	private final SavePreRegistration injected = new SavePreRegistration();

	@PostMapping
	public void execute(@PathVariable("email") String email,@RequestBody Map<String, Object> requestBody) {
		CcpMapDecorator cmd = new CcpMapDecorator(requestBody);
		CcpMapDecorator put = cmd.put("email", email);
		this.injected.execute(put);
	}
	
}
