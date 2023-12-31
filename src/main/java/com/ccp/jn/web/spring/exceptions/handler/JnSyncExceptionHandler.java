package com.ccp.jn.web.spring.exceptions.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.exceptions.mensageria.sender.MensageriaTopicGenericError;
import com.ccp.exceptions.process.CcpFlow;
import com.ccp.jn.sync.business.JnSyncBusinessNotifyError;

import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class JnSyncExceptionHandler {

	private final JnSyncBusinessNotifyError notifyError = new JnSyncBusinessNotifyError();

	@ExceptionHandler({ CcpFlow.class })
	@ResponseBody
	public Map<String, Object> handle(CcpFlow e, HttpServletResponse res){
		
		res.setStatus(e.status);
		
		String message = e.getMessage();
		
		CcpJsonRepresentation result = CcpConstants.EMPTY_JSON.put("message", message);
		
		if(e.fields.length <= 0) {
			return result.content;
		}
		
		CcpJsonRepresentation subMap = e.values.getJsonPiece(e.fields);
		
		CcpJsonRepresentation putAll = result.putAll(subMap);
		
		return putAll.content;
	}

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ Throwable.class })
	public CcpJsonRepresentation handle(Throwable e) {
		return this.notifyError.apply(e);
	}
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ MensageriaTopicGenericError.class })
	public void handle(MensageriaTopicGenericError e) {
	}
}
