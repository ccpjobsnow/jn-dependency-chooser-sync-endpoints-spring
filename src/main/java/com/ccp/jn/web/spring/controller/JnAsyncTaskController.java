package com.ccp.jn.web.spring.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.jn.sync.service.JnSyncAsyncTaskService;

@CrossOrigin
@RestController
@RequestMapping(value = "/async/task")
public class JnAsyncTaskController {
	
	private final JnSyncAsyncTaskService injected = new JnSyncAsyncTaskService();
	
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
