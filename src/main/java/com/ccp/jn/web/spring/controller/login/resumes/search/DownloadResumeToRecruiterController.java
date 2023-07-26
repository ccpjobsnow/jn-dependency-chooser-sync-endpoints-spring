package com.ccp.jn.web.spring.controller.login.resumes.search;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.jn.sync.resumes.searchs.controller.DownloadResumeToRecruiter;

@CrossOrigin
@RestController
@RequestMapping(value = "/recruiters/{recruiter}/resumes/{resume}/view-type/{viewType}", method = RequestMethod.GET)
public class DownloadResumeToRecruiterController {

	private final DownloadResumeToRecruiter injected = CcpDependencyInjection.getInjected(DownloadResumeToRecruiter.class);

	public Map<String, Object> execute(@PathVariable("resume") String resume, @PathVariable("recruiter") String recruiter, @PathVariable("viewType") String viewType) {
		
		Map<String, Object> execute = this.injected.execute(resume, recruiter, viewType);
		
		return execute;
	}
}
