package com.telus.eas.framework.exception;

import com.telus.eas.framework.info.ExceptionInfo;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 22-Oct-2007
 */
/** * @deprecated Interaction with HPA through Client API has been decommissioned as of July 2013 release.  Use services exposed by HPA team. * @author Naresh Annabathula * */
public class HpaEligibilityException extends TelusApplicationException {
  public static final String ERROR_CODE_DUPLICATE_ACCOUNT = "APPC0001";
  public static final String ERROR_CODE_CANNOT_ADD_SUBSCRIBER = "APPC0002";
  public static final String ERROR_CODE_CANNOT_GET_HPA_ELIGIBILITY = "APPC0003";
  public static final String ERROR_CODE_ACCOUNT_NOT_FOUND = "APPC0004";
  public static final String ERROR_CODE_CREDIT_CHECK_FAILURE = "APPC0005";
  public static final String ERROR_CODE_CANNOT_CREATE_ACCOUNT = "APPC0006";

  public HpaEligibilityException() {
    super();
  }
  public HpaEligibilityException(String id, String message) {
    super(id,message);
  }
  public HpaEligibilityException(String id, String message, Throwable exception) {
    super(id,message,exception);
  }
  public HpaEligibilityException(ExceptionInfo pExceptionInfo){
    super(pExceptionInfo);
  }
}
