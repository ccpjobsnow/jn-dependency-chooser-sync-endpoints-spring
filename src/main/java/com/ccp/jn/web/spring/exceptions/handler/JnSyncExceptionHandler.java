package com.ccp.jn.web.spring.exceptions.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.exceptions.mensageria.sender.MensageriaTopicGenericError;
import com.ccp.exceptions.process.CcpFlow;
import com.ccp.jn.sync.business.JnSyncBusinessNotifyError;

import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class JnSyncExceptionHandler {

	private JnSyncBusinessNotifyError notifyError = new JnSyncBusinessNotifyError();

	@ExceptionHandler({ CcpFlow.class })
	@ResponseBody
	public Map<String, Object> handle(CcpFlow e, HttpServletResponse res){
		res.setStatus(e.status);
		CcpMapDecorator putAll = new CcpMapDecorator().put("message", e.getMessage()).putAll(e.values).removeKeys("_entities", "pastSteps", "step", "business");
		return putAll.content;
	}

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ Throwable.class })
	public CcpMapDecorator handle(Throwable e) {
		return this.notifyError.apply(e);
	}
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ MensageriaTopicGenericError.class })
	public void handle(MensageriaTopicGenericError e) {
	}
}
