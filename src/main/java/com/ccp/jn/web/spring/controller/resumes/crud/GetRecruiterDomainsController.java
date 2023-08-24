package com.ccp.jn.web.spring.controller.resumes.crud;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.jn.sync.resumes.crud.controller.GetRecruiterDomains;

@CrossOrigin
@RestController
@RequestMapping(value = "recruiters/domains/{firstLetters}", method = RequestMethod.GET)
public class GetRecruiterDomainsController {
	
	private final GetRecruiterDomains injected = new GetRecruiterDomains();
	
	public Map<String, Object> execute(@PathVariable("firstLetters") String firstLetters){
		CcpMapDecorator execute = this.injected.apply(firstLetters);
		return execute.content;
	}
}
