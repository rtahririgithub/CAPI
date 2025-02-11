package com.telus.cmb.common.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a parameter so that it does not get written to
 * output by the ExceptionParamLogger class.
 * 
 * @see ExceptionParamLogger
 * 
 * @author Canh Tran
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sensitive {

}
