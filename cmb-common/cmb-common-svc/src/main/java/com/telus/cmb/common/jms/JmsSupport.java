package com.telus.cmb.common.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.ProducerCallback;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import weblogic.jms.extensions.WLMessageProducer;

import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.framework.crypto.EncryptionUtil;

/**
 * This class provides support for sending message to JMS queue.
 * 
 */
public class JmsSupport {
	
	public static final String JMS_QUEUE_NAME_CMB_MANAGEMENT = "com.telus.cmb.queue.CMB_MANAGEMENT_QUEUE";
	public static final String JMS_QUEUE_NAME_CMB_FAILURE = "com.telus.cmb.queue.CMB_MANAGEMENT_QUEUE_FAILURE";
	
	public static final String MSG_PROPERTY_MESSAGE_TYPE= "CMB_JMS_messageType";
	public static final String MSG_PROPERTY_MESSAGE_SUBTYPE= "CMB_JMS_messageSubType";
	
	
	private static final String MSG_PROPERTY_KBUSER  = "CMB_JMS_KBUserId"; 
	private static final String MSG_PROPERTY_KBPASS  = "CMB_JMS_KBPassword"; 
	private static final String MSG_PROPERTY_APPCODE = "CMB_JMS_AppCode"; 

	private MessageConverter defaultMessageConverter = new SimpleMessageConverter(); 
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	private String messageType;
	
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public void send( Object message ) {
		send( message, null, null );
	}
	
	/**
	 * @param object
	 * @param messageSubType - a indicator that MDB can use to identify different message.
	 */
	public void send( Object object, String messageType ) {
		send( object, messageType, null );
	}
	
	/**
	 * @param object
	 * @param clientIdentity - optional, if set, principal, credential and application from the ClientIdentity will be populated as
	 *                         Message's properties.
	 */
	public void send( Object object, ClientIdentity clientIdentity ) {
		send( object, null, clientIdentity );
	}

	/**
	 * @param object
	 * @param messageSubType - a indicator that MDB can use to identify different message.
	 * @param clientIdentity - optional, if set, principal, credential and application from the ClientIdentity will be populated as
	 *                         Message's properties.
	 */
	public void send( final Object object, final String messageSubType, final ClientIdentity clientIdentity ) {
		
		jmsTemplate.send( new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				
				Message aMessage = defaultMessageConverter.toMessage( object, session );

				setMessageType( aMessage , messageSubType );
				setClientIdentity( aMessage, clientIdentity );
				
				return aMessage;
			}
		});			
	}

	/**
	 * Send message with given unit-of-order name.
	 * @param unitOfOrderName -  unit-of-order name. Messages that tagged with same unit-of-order name will be processed in order.
	 * @param object - the message payload
	 */
	public void sendUnitOfOrder( String unitOfOrderName, Object object ) {
		sendUnitOfOrder( unitOfOrderName, object, null, null );
	}
	
	/**
	 * Send message with given unit-of-order name.
	 * @param unitOfOrderName -  unit-of-order name. Messages that tagged with same unit-of-order name will be processed in order.
	 * @param object - the message payload
	 * @param messageSubType - a indicator that MDB can use to identify different message.
	 */
	public void sendUnitOfOrder( String unitOfOrderName,  Object object, String messageSubType ) {
		sendUnitOfOrder( unitOfOrderName, object, messageSubType, null );
	}
	
	/**
	 * Send message with given unit-of-order name.
	 * @param unitOfOrderName -  unit-of-order name. Messages that tagged with same unit-of-order name will be processed in order.
	 * @param object - the message payload
	 * @param clientIdentity - optional, if set, principal, credential and application from the ClientIdentity will be populated as
	 *                         Message's properties.
	 */
	public void sendUnitOfOrder( String unitOfOrderName,  Object object, ClientIdentity clientIdentity ) {
		sendUnitOfOrder( unitOfOrderName, object, null, clientIdentity );
	}
	
	/**
	 * Send message with given unit-of-order name.
	 * @param unitOfOrderName -  unit-of-order name. Messages that tagged with same unit-of-order name will be processed in order.
	 * @param object - the message payload
	 * @param messageSubType - a indicator that MDB can use to identify different message.
	 * @param clientIdentity - optional, if set, principal, credential and application from the ClientIdentity will be populated as
	 *                         Message's properties.
	 */
	public void sendUnitOfOrder( final String unitOfOrderName, final Object object, final String messageSubType, final ClientIdentity clientIdentity  ) {
		
		jmsTemplate.execute(new ProducerCallback<Object>() {

			@Override
			public Object doInJms(Session session, MessageProducer producer) throws JMSException {
				
				Message message = defaultMessageConverter.toMessage(object, session);
				
				setMessageType( message , messageSubType );
				setClientIdentity( message, clientIdentity );
				
				((WLMessageProducer) producer).setUnitOfOrder( unitOfOrderName );
				producer.send( message );
				return null;
			}
		});			
	}
	
	//setup message type for message selector on the queue listener (MDB) side.
	private void setMessageType( Message message , String messageSubType ) throws JMSException {
		if ( getMessageType()!=null) 
			message.setStringProperty(MSG_PROPERTY_MESSAGE_TYPE,    getMessageType());
		if ( messageSubType!=null)	
			message.setStringProperty(MSG_PROPERTY_MESSAGE_SUBTYPE, messageSubType );
	}
	
	private static void setClientIdentity( Message message, ClientIdentity clientIdentity ) throws JMSException {
		
		if ( clientIdentity!=null ) {
			message.setStringProperty( MSG_PROPERTY_KBUSER,   clientIdentity.getPrincipal());
			message.setStringProperty( MSG_PROPERTY_KBPASS,   EncryptionUtil.encrypt( clientIdentity.getCredential() ) );
			message.setStringProperty( MSG_PROPERTY_APPCODE, clientIdentity.getApplication());
		}
	}
	
	/**
	 * Extract and return ClientIdentity from the given message if it contain client identity information 
	 * @param message
	 * @return ClientIdentity - can be null if the message does not have client identity information.
	 * @throws JMSException
	 */
	public static ClientIdentity getClientIdentity( Message message ) throws JMSException {
		
		ClientIdentity clientIdentity = null;
		if ( message.getStringProperty(JmsSupport.MSG_PROPERTY_KBUSER)!=null ) {
			clientIdentity = new ClientIdentity();
			clientIdentity.setPrincipal(message.getStringProperty(JmsSupport.MSG_PROPERTY_KBUSER));
			clientIdentity.setCredential( EncryptionUtil.decrypt( message.getStringProperty(JmsSupport.MSG_PROPERTY_KBPASS) ) );
			clientIdentity.setApplication(message.getStringProperty(JmsSupport.MSG_PROPERTY_APPCODE));
		}
		return clientIdentity;
	}
	
}
