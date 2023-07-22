package com.ccp.jn.web.spring.controller.login;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.jn.sync.login.controller.Login;


@CrossOrigin
@RestController
@RequestMapping(value = "/login/{email}", method = RequestMethod.POST)
public class LoginController {
	
	private Login injected = CcpDependencyInjection.getInjected(Login.class);

	public Map<String, Object> execute(HttpServletRequest request, @PathVariable("email") String email){
		String remoteAddr = request.getRemoteAddr();
		Map<String, Object> values = new CcpMapDecorator().put("ip", remoteAddr).put("email", email).content;
		Map<String, Object> execute = this.injected.execute(values);
		return execute;
	}
	
}
