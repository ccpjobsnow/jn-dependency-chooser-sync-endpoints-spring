package com.ccp.jn.web.spring.controller.login;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.jn.sync.login.controller.Login;


@CrossOrigin
@RestController
@RequestMapping(value = "/login/{email}")
public class LoginController {
	
	private final Login injected = new Login();
	
	@PostMapping
	public Map<String, Object> execute(HttpServletRequest request, @PathVariable("email") String email, @RequestBody Map<String, Object> body){
		String remoteAddr = request.getRemoteAddr();
		Map<String, Object> values = new CcpMapDecorator(body).put("ip", remoteAddr).put("email", email).content;
		CcpMapDecorator execute = this.injected.execute(values);
		return execute.content;
	}
	
}
