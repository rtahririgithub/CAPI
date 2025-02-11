package com.telus.cmb.common.jms;

import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.cmb.common.identity.ClientIdentity;


/**
 *
 */
public abstract class BaseMessageDrivenBean<T> implements MessageListener, MessageDrivenBean {
	private static final Logger msgLogger = Logger.getLogger("failure.queue");
	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(BaseMessageDrivenBean.class);
	
	private MessageDrivenContext messageDrivenContext;

	private final Class<T> expectedType;
	private MessageConverter messageConverter = new SimpleMessageConverter();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public  BaseMessageDrivenBean () {
		
		Object obj = getClass().getGenericSuperclass();
		if (obj!=null && obj instanceof ParameterizedType ) { 
			final ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
			expectedType = (Class) parameterizedType.getActualTypeArguments()[0];
		} else {
			expectedType = null;
		}
	}
	
	@Override
	public void setMessageDrivenContext(MessageDrivenContext ctx) throws javax.ejb.EJBException {
		this.messageDrivenContext = ctx;
	}
	
	protected MessageDrivenContext getMessageDrivenContext() {
		return this.messageDrivenContext;
	}
	
//	public void ejbCreate() throws javax.ejb.EJBException {};
//	
	@Override
	public void ejbRemove() throws javax.ejb.EJBException {};

	
	@Override
	public void onMessage(Message message) {
		MessageInfo<T> messageInfo = new MessageInfo<T>();
		try {
			messageInfo.setMessageId(message.getJMSMessageID());
			messageInfo.setMessageType(message.getStringProperty(JmsSupport.MSG_PROPERTY_MESSAGE_TYPE));
			messageInfo.setMessageSubType(message.getStringProperty(JmsSupport.MSG_PROPERTY_MESSAGE_SUBTYPE));
			messageInfo.setTimestamp(message.getJMSTimestamp());
			messageInfo.setMessageObject(getMessagePayload(message));
			ClientIdentity clientIdentity = JmsSupport.getClientIdentity(message);
			consumeMessage(messageInfo, clientIdentity);
		} catch (Exception e) {
			logException(messageInfo, e, true);  // come out if needn't. 
			LOGGER.error( "Encountered exception while processing message, msgID:" + messageInfo.getMessageId() + ", msgType:" + messageInfo.getMessageType() + "/"+ messageInfo.getMessageSubType() , e) ;
			LOGGER.info( "Rollback message, ID:" +  messageInfo.getMessageId() );
			getMessageDrivenContext().setRollbackOnly();
		}
	}
	
	protected void consumeMessage(MessageInfo<T> messageInfo, ClientIdentity clientIdentity) throws ApplicationException {
		try {
			consume(messageInfo, clientIdentity);
		} catch (ApplicationException e) {
			if (isRequeueRequired(e)) {
				throw e;  // openSession failed - need Rollback
			} else {
				messageInfo.setForFallout(true);
				logException(messageInfo, e, false);
			}
		}
	}
	
	protected boolean isRequeueRequired(ApplicationException e) {
		return e.getErrorCode().equals(ErrorCodes.KB_CONNECT_ERROR) || e.getErrorCode().equals(ErrorCodes.KB_AMDOCS_SESSION_ERROR);
	}
	
	protected void logException(MessageInfo<T> messageInfo, Throwable t, boolean rollback) {
		messageInfo.setException(t, rollback);
		try {
			consume(messageInfo, null);
		} catch (ApplicationException e) {
			// It won't happen, but ignore any possible exception from this step. 
		}
	}
	
	@SuppressWarnings("unchecked")
	private T getMessagePayload(Message message) throws JMSException {		
		if ( matchesExpectedType( message ) ) {	//if subclass expect Message type, then pass along
			return (T) message;
		} else {
			//otherwise extract the payload then pass along.
			Object payload = extractPayload(message);
			return (T) payload;
		}
	}
	
	
	private boolean matchesExpectedType(Object obj) {
        return expectedType!=null && obj != null && expectedType.isAssignableFrom(obj.getClass());
    }

    protected Object extractPayload(Message message) throws JMSException {
    	return messageConverter.fromMessage(message);
    }

	/**
	 * @param messagePayload
	 * @param clientIdentity - this is optional field, will only be populated when the message contain clientIdenttiy info. 
	 */
	protected abstract void consume( MessageInfo<T> messageInfo, ClientIdentity clientIdentity) throws ApplicationException ; 
	
	/**
	 * MessageInfo include all required fields both business Handlers and and log Handlers.  
	 * @param <T>
	 */
	public static class MessageInfo<T> {
		private String messageId;
		private String messageType;
		private String messageSubType;
		private String timestamp;
		private long timestampLongValue;
		private T messageObject;
		private boolean rollback = false;
		private boolean forlog = false;
		private Throwable exp = null;
		private boolean forFallout = false;
		
		public void setMessageId(String messageId) {
			this.messageId = messageId;
		}
		public void setMessageType(String messageType) {
			this.messageType = messageType;
		}
		public void setMessageSubType(String messageSubType) {
			this.messageSubType = messageSubType;
		}
		public void setTimestamp(long timestamp) {
			timestampLongValue = timestamp;
			this.timestamp = getDateFormat().format(new Date(timestamp));
		}
		
		public long getTimeStampLongValue() {
			return timestampLongValue;
		}
		
		public void setMessageObject(T messageObject) {
			this.messageObject = messageObject;
		}
		public void setForlog() {
			this.forlog = true;
		}
		public void setForFallout(boolean forFallout) {
			this.forFallout = forFallout;
		}
		
		public void setException(Throwable t, boolean rollback) {
			this.exp = t;
			this.rollback = rollback;
			this.setForlog();
		}
		
		public Throwable getException() {
			return exp;
		}

		public String getMessageId() {
			return messageId;
		}
		public String getMessageType() {
			return messageType;
		}
		public String getMessageSubType() {
			return messageSubType;
		}
		public String getHandlerKey() {
			return messageSubType + (forlog ? "Log" : "");
		}
		public boolean isForlog() {
			return forlog;
		}
		
		public boolean isForFallout(){
			return forFallout;
		}
		
		public T getMessageObject() {
			return messageObject;
		}
		public SimpleDateFormat getDateFormat() {
			return formatter;
		}
		
		public void writeDefaultLog() {
			writeLog("");
		}
		public void writeLog(String appendixContent) {
			StringBuffer logcontent = new StringBuffer(timestamp);
			logcontent.append("|").append(messageId);
			logcontent.append("|").append(messageType);
			logcontent.append("|").append(messageSubType);
			if (rollback) {
				logcontent.append("|Message Rollbacked");
			}
			if (exp != null) {
				logcontent.append("|").append(exp.getClass().getName());
				logcontent.append("|").append(exp.getMessage());
			}
			logcontent.append("|").append(appendixContent);
			msgLogger.info(logcontent.toString());
		}
	}

}
