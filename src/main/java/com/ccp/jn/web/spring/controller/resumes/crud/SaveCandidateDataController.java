package com.ccp.jn.web.spring.controller.resumes.crud;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.jn.sync.resumes.crud.controller.SaveCandidateData;

@CrossOrigin
@RestController
@RequestMapping(value = "resumes/", method = RequestMethod.POST)
public class SaveCandidateDataController {

	private final SaveCandidateData injected = CcpDependencyInjection.getInjected(SaveCandidateData.class);

	public Map<String, Object> execute(@RequestBody Map<String, Object> form) {
		CcpMapDecorator execute = this.injected.apply(form);
		return execute.content;
	}
}
