package com.ccp.jn.web.spring.controller.async.tasks;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.jn.sync.resumes.tasks.controller.GetAsyncTaskById;

@CrossOrigin
@RestController
@RequestMapping(value = "/async/task")
public class GetAsyncTaskByIdController {
	
	private final GetAsyncTaskById injected = new GetAsyncTaskById();
	
	@GetMapping
	
	public String oi() {
		return "olá mundo";
	}
	
	@GetMapping("/{asyncTaskId}")
	public Map<String, Object> execute(@PathVariable("asyncTaskId") String asyncTaskId){
		CcpMapDecorator execute = this.injected.apply(asyncTaskId);
		return execute.content;
	}
	
}
