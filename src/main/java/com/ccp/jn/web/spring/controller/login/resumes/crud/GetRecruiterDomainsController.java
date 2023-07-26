package com.ccp.jn.web.spring.controller.login.resumes.crud;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.jn.sync.resumes.crud.controller.GetRecruiterDomains;

@CrossOrigin
@RestController
@RequestMapping(value = "recruiters/domains/{firstLetters}", method = RequestMethod.GET)
public class GetRecruiterDomainsController {
	
	private final GetRecruiterDomains injected = CcpDependencyInjection.getInjected(GetRecruiterDomains.class);
	
	public Map<String, Object> execute(@PathVariable("firstLetters") String firstLetters){
		CcpMapDecorator execute = this.injected.execute(firstLetters);
		return execute.content;
	}
}
