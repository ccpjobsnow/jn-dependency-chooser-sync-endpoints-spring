package com.ccp.jn.web.spring.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.jn.sync.common.business.NotifyError;

@RestControllerAdvice
public class JnSiteExceptionHandler {

	private NotifyError notifyError = CcpDependencyInjection.getInjected(NotifyError.class);

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ Throwable.class })
	public void handle(Throwable e) {
		this.notifyError.execute(e);
	}
}
