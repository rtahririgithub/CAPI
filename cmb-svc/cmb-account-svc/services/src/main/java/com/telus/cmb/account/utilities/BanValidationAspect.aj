package com.telus.cmb.account.utilities;

import java.lang.annotation.Annotation;

import org.aspectj.lang.reflect.MethodSignature;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.aop.utilities.BANValue;
import com.telus.cmb.common.validation.BanValidator;

public aspect BanValidationAspect {
	
	pointcut annotatedMethod(): execution(* *(.., @BANValue (int), ..));			
	
	before() throws ApplicationException: annotatedMethod() {
		MethodSignature signature = (MethodSignature) thisJoinPoint.getSignature();
		Annotation annotation[][] = signature.getMethod().getParameterAnnotations();
		
		Annotation annotatedParam[];
		Annotation param;
		int ban;
		for (int i = 0; i < annotation.length; i++) {
			annotatedParam = annotation[i];
			for (int j = 0; j < annotatedParam.length; j++) {
				param = annotation[i][j];
				if (param instanceof BANValue) {
					ban = (Integer) thisJoinPoint.getArgs()[i];
					BanValidator.validate(ban);
				}
			}
		}		
	};
}
