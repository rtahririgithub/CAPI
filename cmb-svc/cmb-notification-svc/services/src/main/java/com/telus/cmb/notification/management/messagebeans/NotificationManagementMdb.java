package com.telus.cmb.notification.management.messagebeans;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.cmb.common.jms.DelegatingMessageDrivenBean;
import com.telus.cmb.common.jms.JmsSupport;


@MessageDriven(
	activationConfig = {
	    @ActivationConfigProperty(propertyName="messageSelector", propertyValue=JmsSupport.MSG_PROPERTY_MESSAGE_TYPE +"='CMB_NOTIFICATION_MDB'")
	    ,@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Queue")
	    ,@ActivationConfigProperty(propertyName="acknowledgeMode", propertyValue="Auto-acknowledge")
	}
)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Interceptors (SpringBeanAutowiringInterceptor.class)
public class NotificationManagementMdb extends DelegatingMessageDrivenBean {

	private static final long serialVersionUID = 1L;
	
	
	private MessageBeanContext beanContext;
	
	@Override
	@Autowired
	//have to override this method in order for autowired annotation to work 
	protected void setHandlers( Map<String, MessageHandler<Object>>  handlers ) {
		super.setHandlers(handlers);
	}
	
	@PostConstruct
	private void initBeanContext() {
		
		beanContext = new MessageBeanContext();
	}

	@Override
	protected MessageBeanContext getBeanContext() {
		return beanContext;
	}

}
