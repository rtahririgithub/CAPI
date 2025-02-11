package com.telus.cmb.common.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.telus.eas.framework.info.FalloutProcessInfo;
import com.telus.eas.framework.info.FalloutProcessInfo.FalloutExceptionInfo;

public class FalloutUtil {
	
	/**
	 *  Logical is copied from DelegatingFailureLog.createFalloutData(MessageInfo<Object> messageInfo);
	 *  Later, should update DelegatingFailureLog.createFalloutData(MessageInfo<Object> messageInfo) to refer this method.
	 * 
	 *  @param exception
	 *  @return
	 */
	public static FalloutProcessInfo createBaseFalloutData(Throwable exception) {
		FalloutProcessInfo falloutInfo = new FalloutProcessInfo();
		falloutInfo.setApplicationId(FalloutProcessInfo.APPID_CLIENTAPI_EJB);
		falloutInfo.setCorrelationId("N/A");
		falloutInfo.setCustomerId("N/A");
		falloutInfo.setResourceId("N/A");
	
		if (exception != null) {
			FalloutExceptionInfo exceptionInfo = falloutInfo.new FalloutExceptionInfo();
			exceptionInfo.setExceptionType(exception.getClass().getName());
			exceptionInfo.setExceptionDetail(getStackTraceAsString(exception));
			exceptionInfo.setExceptionTimeStamp(Calendar.getInstance());
			exceptionInfo.setTargetApplicationId("N/A");
			exceptionInfo.setTargetServiceName("N/A");
			
			List<FalloutExceptionInfo> falloutExceptionList = new ArrayList<FalloutExceptionInfo>();
			falloutExceptionList.add(exceptionInfo);
			falloutInfo.setFalloutExceptionInfoList(falloutExceptionList);
		}
		
		return falloutInfo;
	}

	private static String getStackTraceAsString (Throwable t) {
		if (t != null) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(os);
			t.printStackTrace(ps);
			return os.toString();
		}
		
		return null;
	}
	
}
