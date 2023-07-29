package com.ccp.jn.web.spring.controller.resumes.search;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.jn.sync.resumes.searchs.controller.GetResumesData;

@CrossOrigin
@RestController
@RequestMapping(value = "resumes/{searchType}", method = RequestMethod.GET)
public class GetResumesDataController {

	private final GetResumesData injected = CcpDependencyInjection.getInjected(GetResumesData.class);

	public Map<String, Object> execute(@PathVariable("searchType") String searchType, @RequestBody Map<String, Object> requestBody){
		CcpMapDecorator execute = this.injected.execute(searchType, requestBody);
		return execute.content;
	}
}
