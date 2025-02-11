package com.telus.cmb.common.logging;

import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.eas.framework.info.Info;


public class ExceptionParamLogger {
	private static Log ExcParamLogger = LogFactory.getLog("cmbejb.exception.parameter");
	private static Log AppExcepLogger = LogFactory.getLog("cmbejb.application.exception");
	private static DateFormat KeyGenerator = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	/**
	 * This method writes to the Log DEBUG level if t is an ApplicationException otherwise
	 * it will log to ERROR level.  It will output the method name, its parameters and the
	 * parameter values.
	 * 
	 * @param logger 	The logger from the source class
	 * @param jp 		The AspectJ joinpoint to establish the method signature and arguments
	 * @param t			The Throwable class that initiated the exception
	 */
	public static void logError(Log logger, JoinPoint jp, Throwable t) {
		try {
			String messageKey = "{" + KeyGenerator.format(new Date()) + "}"; 
			StringBuilder sb = new StringBuilder(100);
			sb.append("**** " + messageKey + " ****");
			sb.append("\n" + jp.getSignature().toLongString());
			sb.append("\n" + constructThrowableAndRoot(t));
			if ((t instanceof ApplicationException) && (!SystemCodes.AMDOCS.equals(((ApplicationException)t).getSystemCode()))) {
				logger.info("**** Detail in ApplicationException log with key:" + messageKey);
				if (logger.isDebugEnabled()) {
					sb.append("\n" + constructArgsOutString(jp.getSignature(), jp.getArgs()));
				}
				sb.append("\n************** END OF " + messageKey + " **************\n");
				AppExcepLogger.info(sb.toString());
			} else {
				logger.error("**** Detail in ExceptionParameter log with key:" + messageKey);
				sb.append("\n" + constructArgsOutString(jp.getSignature(), jp.getArgs()));
				sb.append("\n************** END OF " + messageKey + " **************\n");
				ExcParamLogger.error(sb.toString());
			}
		} catch (Throwable throwableInThisBlock) {
			// swallow the exception - don't want to introduce exceptions from logging
		}
	}
	
	/**
	 * The method constructs a parameter list with the name of the parameter and its value and
	 * returns this as a String in the form of something like the example
	 * <code>
	 * Argument values: (ban=-1, portalUserID=123, pBillNotificationContact=** Param Masked **, applicationCode=appcode)
	 * </code>
	 * If this method encounters a parameter annotated with @Sensitive the parameter prints "** Param Masked **"
	 * 
	 * @param sig				The method signature
	 * @param joinpointArgs		The method arguments
	 * @return					Example:
	 * <code>
	 * Argument values: (ban=-1, portalUserID=123, pBillNotificationContact=** Param Masked **, applicationCode=appcode)
	 * </code>
	 * @see Sensitive
	 */
	private static String constructArgsOutString(Signature sig, Object[] joinpointArgs) {
		StringBuilder args = new StringBuilder(100);
		if (sig instanceof MethodSignature) {
			try {
				MethodSignature signature = (MethodSignature) sig;
				Annotation annotation[][] = signature.getMethod().getParameterAnnotations();

				args.append("Argument values: (\n");
				Object o;
				boolean isSensitive;

				Annotation annotatedParam[];
				Annotation param;
				assert (annotation.length == joinpointArgs.length);
				for (int i = 0; i < annotation.length; i++) {
					isSensitive = false;
					annotatedParam = annotation[i];
					o = joinpointArgs[i];

					// output the names of the parameters
					args.append("<").append(signature.getParameterTypes()[i]).append(">");
					args.append(signature.getParameterNames()[i]);
					args.append("=");

					// determine if the parameter has a Sensitive annotation
					for (int j = 0; j < annotatedParam.length; j++) {
						param = annotation[i][j];
						if (param instanceof Sensitive) {
							isSensitive = true;
							break;
						}
					}

					// output the parameter or indicate parameter is masked
					if (!isSensitive) {
						if (o != null) {
							if (o.getClass().isArray()) {
								Object[] objArray = (Object[]) o;
								args.append("(array size: " + objArray.length + ")\n");
								for (Object obj : objArray) {
									if (obj instanceof Info) {
										// call method toStringForLogging if it is an Info subclass
										args.append(((Info) obj).toStringForLogging());
									} else {
										args.append(obj.toString());
									}
									args.append("\n");
								}
							} else {
								if (o instanceof Info) {
									// call method toStringForLogging if it is an Info subclass
									args.append(((Info) o).toStringForLogging());
								} else {
									args.append(o.toString());
								}
							}
						} else {
							args.append(o); // this will output null
						}
					} else {
						args.append("** Param Masked **");
					}

					args.append(", ");
				}

				args = args.delete(args.lastIndexOf(","), args.length());
			} catch (Throwable throwableInThisBlock) {
				// swallow the exception - don't want to introduce exceptions from logging
			} finally {
				args.append(")");
			}
		}

		return args.toString();
	}
	
	/**
	 * @param t
	 * @return
	 */
	private static String constructThrowableAndRoot(Throwable t) {
		StringBuilder sb = new StringBuilder(100);
		sb.append("\t" + t.toString());
		StackTraceElement[] trace = t.getStackTrace();
		if (trace != null && trace.length > 0) {
			sb.append("\n\tfrom " + trace[0]);
		}
		return sb.toString();
	}
}
