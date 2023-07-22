package com.ccp.jn.web.spring.controller.login.resumes.crud;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.jn.sync.resumes.crud.controller.SaveCandidateData;

@CrossOrigin
@RestController
@RequestMapping(value = "resumes/", method = RequestMethod.POST)
public class SaveCandidateDataController {

	private SaveCandidateData injected = CcpDependencyInjection.getInjected(SaveCandidateData.class);

	public void execute(@RequestBody Map<String, Object> form) {
		this.injected.execute(form);
	}
}
