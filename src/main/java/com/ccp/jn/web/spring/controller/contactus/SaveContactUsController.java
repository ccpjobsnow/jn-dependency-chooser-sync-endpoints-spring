package com.ccp.jn.web.spring.controller.contactus;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.jn.sync.contactus.controller.SaveContactUs;

@CrossOrigin
@RestController
@RequestMapping(value = "/contact-us", method = RequestMethod.POST)
@ResponseStatus(code = HttpStatus.ACCEPTED)
public class SaveContactUsController {

	private final SaveContactUs injected = CcpDependencyInjection.getInjected(SaveContactUs.class);

	public void execute(@RequestBody Map<String, Object> json) {
		this.injected.execute(json);
	}
	
}
