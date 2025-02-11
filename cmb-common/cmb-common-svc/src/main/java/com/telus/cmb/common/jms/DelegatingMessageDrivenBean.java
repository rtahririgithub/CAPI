package com.telus.cmb.common.jms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.identity.ClientIdentity;

public class DelegatingMessageDrivenBean extends BaseMessageDrivenBean<Object> {

	private static final long serialVersionUID = 1L;
	
	private Map<String, MessageHandler<Object>> handlers=null;
	
	protected void setHandlers( Map<String, MessageHandler<Object> >  handlers ) {
		this.handlers = handlers;
	}
	
	@Override
	protected void consume(MessageInfo<Object> messageInfo, ClientIdentity clientIdentity) throws ApplicationException {
		MessageHandler<Object> handler = handlers.get(messageInfo.getHandlerKey());

		if (handler != null) {
			handler.handle( messageInfo.isForlog() ? messageInfo : messageInfo.getMessageObject(), clientIdentity, getBeanContext() );
		} else if (messageInfo.isForlog()) {
			messageInfo.writeDefaultLog();  
		} else {
			throw new ApplicationException(SystemCodes.CMB_EJB, ErrorCodes.MESSAGE_HANDLER_NOT_FOUND, "No MesssageHandler configured for message sub-type:" + messageInfo.getHandlerKey() +", registered handler:" + handlers.keySet(), "");
		}
		
	}
	
	/**
	 * @return a context that contain resources at MessageDrivenBean instance level 
	 */
	protected MessageBeanContext getBeanContext() {
		return null;
	}
	

	/**
	 * MessageBeanContex provides a facility for MessageHandler to access resource that are available inside the MessageDrivenBean
	 * Each DelegatingMessageDrivenBean is responsible to return MessageBeanContext that contain resource that its handlers need.
	 * Currently , provides method to acquire other EJB reference. 
	 */
	public static class MessageBeanContext {
		private Map<Class<?>, Object> ejbMap = new HashMap<Class<?>, Object>();
		
		public <T> void setEjb (Class<T> clazz, T ejbRef) {	
			ejbMap.put(clazz, ejbRef);
		}
		
		@SuppressWarnings("unchecked")
		public <T> T getEjb (Class<T> clazz) {	
			T ejbRef = (T) ejbMap.get(clazz);
			return ejbRef;
		}
	}

	/**
	 * MessaggeHandler is responsible to implement the business logic of how to handle one particular type of message.
	 *
	 * @param <T>
	 */
	public static interface MessageHandler<T> {
		public void handle( T message, ClientIdentity clientIdentity, MessageBeanContext beanContext ) throws ApplicationException;
	}
	
	/**
	 * MessaggeHandlerList is responsible to implement the business logic of how to handle one particular type of message.
	 * This handle method uses List which can hold more than one object for the business logic 
	 * @param List<T>
	 */
	public static interface MessageHandlerList<T extends List<Object>> {
		public void handle( T messageList, ClientIdentity clientIdentity, MessageBeanContext beanContext ) throws ApplicationException;
	}
}
