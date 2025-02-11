package com.telus.cmb.productequipment.messagebeans;

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

import com.telus.cmb.common.jms.DelegatingMessageDrivenBean;
import com.telus.cmb.common.jms.JmsSupport;
import com.telus.cmb.productequipment.lifecyclefacade.svc.ProductEquipmentLifecycleFacade;
import com.telus.cmb.productequipment.manager.svc.ProductEquipmentManager;


@MessageDriven(
		activationConfig = {
		    @ActivationConfigProperty(propertyName="messageSelector", propertyValue=JmsSupport.MSG_PROPERTY_MESSAGE_TYPE +"='CMB_PE_MDB'")
		    ,@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Queue")
		    ,@ActivationConfigProperty(propertyName="acknowledgeMode", propertyValue="Auto-acknowledge")
		}
	)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Interceptors (SpringBeanAutowiringInterceptor.class)
public class ProductManagementMdb extends DelegatingMessageDrivenBean {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private ProductEquipmentManager productEquipmentManager;
	
	@EJB
	private ProductEquipmentLifecycleFacade productEquipmentLifecycleFacade;
	
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
		beanContext.setEjb(ProductEquipmentManager.class, productEquipmentManager);
		beanContext.setEjb(ProductEquipmentLifecycleFacade.class, productEquipmentLifecycleFacade);
	}

	@Override
	protected MessageBeanContext getBeanContext() {
		return beanContext;
	}

}
