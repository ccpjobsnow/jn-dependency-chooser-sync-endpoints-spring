package com.ccp.jn.web.spring.controller.login;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.jn.sync.login.controller.SaveWeakPassword;

@CrossOrigin
@RestController
@RequestMapping(value = "/login/{email}/password/weak", method = RequestMethod.POST)
public class SaveWeakPasswordController {
	
	private final SaveWeakPassword injected = CcpDependencyInjection.getInjected(SaveWeakPassword.class);

	public void execute(@PathVariable("email") String email, @RequestBody Map<String, Object> requestBody) {
		this.injected.execute(new CcpMapDecorator(requestBody).put("email", email));

	}
}
