package com.ccp.jn.web.spring.controller.login;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.jn.sync.login.controller.SaveWeakPassword;

@CrossOrigin
@RestController
@RequestMapping(value = "/login/{email}/password/weak")
public class SaveWeakPasswordController {
	
	private final SaveWeakPassword injected = new SaveWeakPassword();

	@PostMapping
	public Map<String, Object> execute(@PathVariable("email") String email, @RequestBody Map<String, Object> requestBody) {
		CcpMapDecorator execute = this.injected.execute(new CcpMapDecorator(requestBody).put("email", email));
		return execute.content;

	}
}
