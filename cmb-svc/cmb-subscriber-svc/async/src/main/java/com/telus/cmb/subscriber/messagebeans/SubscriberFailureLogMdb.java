package com.telus.cmb.subscriber.messagebeans;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.cmb.common.jms.DelegatingFailureLog;
import com.telus.cmb.common.jms.JmsSupport;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;

@MessageDriven(
		name="SubscriberFaiureLogMdb",
		mappedName=JmsSupport.JMS_QUEUE_NAME_CMB_FAILURE,
		activationConfig = {
		    @ActivationConfigProperty(propertyName="messageSelector", propertyValue=JmsSupport.MSG_PROPERTY_MESSAGE_TYPE +"='CMB_SUB_MDB'")
		    ,@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Queue")
		    ,@ActivationConfigProperty(propertyName="acknowledgeMode", propertyValue="Auto-acknowledge")
		}
	)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class SubscriberFailureLogMdb extends DelegatingFailureLog {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private SubscriberLifecycleFacade subscriberLifecycleFacade;

	private MessageBeanContext beanContext;

	@Override
	@Autowired
	//have to override this method in order for autowired annotation to work 
	protected void setHandlers( Map<String, MessageHandler<Object>>  handlers ) {
		super.setHandlers(handlers);
	}
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void initBeanContext() {
		
		beanContext = new MessageBeanContext();
		beanContext.setEjb(SubscriberLifecycleFacade.class, subscriberLifecycleFacade);
	}
	
	@Override
	protected MessageBeanContext getBeanContext() {
		return beanContext;
	}

}
