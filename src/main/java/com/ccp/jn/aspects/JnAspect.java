package com.ccp.jn.aspects;

import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.ccp.validation.Validations;
import com.ccp.validation.annotations.ValidationRules;

@Aspect
public class JnAspect {
	@SuppressWarnings("unchecked")
	@Before("@annotation(com.ccp.validation.annotations.ValidationRules)")
	public void validateFields(JoinPoint jp) {
		ValidationRules rules = null;
		Object[] args = jp.getArgs();
		if (args == null) {
			return;
		}
		if (args.length == 0) {
			return;
		}
		boolean doNotReceiveJson = (args[0] instanceof Map) == false;

		if (doNotReceiveJson) {
			return;
		}
		Map<String, Object> map = (Map<String, Object>) args[0];
		Validations.validate(rules, map);
	}
}
