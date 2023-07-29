package com.ccp.jn.web.spring.controller.async.tasks;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.jn.sync.resumes.tasks.controller.GetAsyncTaskById;

@CrossOrigin
@RestController
@RequestMapping(value = "/async/task/{asyncTaskId}")
public class GetAsyncTaskByIdController {
	
	private final GetAsyncTaskById injected = CcpDependencyInjection.getInjected(GetAsyncTaskById.class);
	
	@GetMapping
	public Map<String, Object> execute(@PathVariable("asyncTaskId") String asyncTaskId){
		CcpMapDecorator execute = this.injected.execute(asyncTaskId);
		return execute.content;
	}
	
}
