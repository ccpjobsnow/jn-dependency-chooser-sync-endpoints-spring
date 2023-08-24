package com.ccp.jn.web.spring.controller.resumes.crud;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.jn.sync.resumes.crud.controller.DownloadResumeToHisOwner;

@CrossOrigin
@RestController
@RequestMapping(value = "/cadidate/{email}/resume/view-type/{viewType}", method = RequestMethod.GET)
public class DownloadResumeToHisOwnerController {

	private final DownloadResumeToHisOwner injected = new DownloadResumeToHisOwner();
	
	public Map<String, Object> execute(@PathVariable("email") String email, @PathVariable("viewType") String viewType){
		CcpMapDecorator execute = this.injected.execute(email, viewType);
		return execute.content;
	}
	
}
