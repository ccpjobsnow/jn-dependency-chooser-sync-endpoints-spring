package com.ccp.jn.web.spring.controller.resumes.crud;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.jn.sync.resumes.crud.controller.RemoveResume;

@CrossOrigin
@RestController
@RequestMapping(value = "/resumes/{resume}", method = RequestMethod.DELETE)
public class RemoveResumeController {

	public void execute(@PathVariable("resume") String resume) {
		RemoveResume injected = new RemoveResume();
		injected.execute(resume);
	}
	
}
