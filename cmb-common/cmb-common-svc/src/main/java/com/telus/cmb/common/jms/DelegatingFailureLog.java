package com.telus.cmb.common.jms;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.eas.framework.info.FalloutProcessInfo;
import com.telus.eas.framework.info.FalloutProcessInfo.FalloutExceptionInfo;


public abstract class DelegatingFailureLog extends DelegatingMessageDrivenBean {
	private static final long serialVersionUID = 1L;

	/**
	 * We override the consumeMessage for failure queue because we want to have the last attempt to consume the message.
	 * And we would log the exception and send the detail to the fallout framework.
	 */
	@Override
	protected void consumeMessage(MessageInfo<Object> messageInfo, ClientIdentity clientIdentity) throws ApplicationException {
		try {
			consume(messageInfo, clientIdentity);
		} catch (Exception e) {
			messageInfo.setForFallout(true);
			try {
				logException(messageInfo, e, false);
			}catch (Exception e2) {
				//do nothing
			}
		}
	}
	
	@Override
	protected void consume(MessageInfo<Object> messageInfo, ClientIdentity clientIdentity) throws ApplicationException {
		super.consume(messageInfo, clientIdentity);
	}
	
	public static abstract class MessageLogHandler<T> implements MessageHandler<T> {

		@Override
		public void handle(T message, ClientIdentity clientIdentity, MessageBeanContext beanContext) throws ApplicationException {
			writeLog(message);
			
			if (message instanceof MessageInfo) {
				@SuppressWarnings("unchecked")
				MessageInfo<Object> msgInfo = (MessageInfo<Object>) message;
				if (msgInfo.isForFallout()) {
					FalloutProcessInfo falloutInfo = mapFalloutData(msgInfo);
					if (falloutInfo != null) {
						try {
							writeToFallout(falloutInfo, beanContext);
						}catch (Exception e) {
							//do not throw exception if write to fallout fails.
						}
					}
				}
			}
		}
		
		public abstract void writeLog(T message);
		
		/**
		 * Default fallout data. It contains exception only
		 * 
		 * @param message
		 * @return
		 */
		public FalloutProcessInfo mapFalloutData(MessageInfo<Object> message) {
			return createFalloutData(message);
		}
			
		/** The default fallout data mapping **/
		protected FalloutProcessInfo createFalloutData(MessageInfo<Object> messageInfo) {
			FalloutProcessInfo falloutInfo = new FalloutProcessInfo();
			falloutInfo.setApplicationId(FalloutProcessInfo.APPID_CLIENTAPI_EJB);
			falloutInfo.setCorrelationId("N/A");
			falloutInfo.setCustomerId("0");
			falloutInfo.setResourceId("N/A");
			if (messageInfo.getMessageObject() != null) {
				falloutInfo.setRequestMessage(messageInfo.getMessageObject().toString());
			}
		
			Throwable exception = messageInfo.getException();
			
			if (exception != null) {
				FalloutExceptionInfo exceptionInfo = falloutInfo.new FalloutExceptionInfo();
				exceptionInfo.setExceptionType(exception.getClass().getName());
				exceptionInfo.setExceptionDetail(getStackTraceAsString(exception));
				exceptionInfo.setExceptionTimeStamp(messageInfo.getTimeStampLongValue());
				exceptionInfo.setTargetApplicationId("N/A");
				exceptionInfo.setTargetServiceName("N/A");
				
				List<FalloutExceptionInfo> falloutExceptionList = new ArrayList<FalloutExceptionInfo>();
				falloutExceptionList.add(exceptionInfo);
				falloutInfo.setFalloutExceptionInfoList(falloutExceptionList);
			}
			
			return falloutInfo;
		}
		
		public abstract void writeToFallout(FalloutProcessInfo falloutInfo, MessageBeanContext beanContext) throws ApplicationException;
		
		protected String getStackTraceAsString (Throwable t) {
			if (t != null) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(os);
				t.printStackTrace(ps);
				return os.toString();
			}
			
			return null;
		}
		
		public abstract boolean isFalloutFlagOn() throws ApplicationException;		
		
	}
}
