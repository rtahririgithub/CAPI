package com.telus.cmb.notification.management.messagebeans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.confirmationnotification.ConfirmationNotification;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageHandler;
import com.telus.cmb.common.util.JAXBUtil;
import com.telus.cmb.notification.management.dao.EnterpriseFormLetterManagementServiceDao;
import com.telus.cmb.notification.management.mapping.MapperFactory;
import com.telus.cmb.notification.management.utils.ReferencePdsWrapper;


public class ConfirmationMessageHandler implements MessageHandler<String > {
	
	private static final Log LOGGER = LogFactory.getLog(ConfirmationMessageHandler.class);
	
	@Autowired
	private EnterpriseFormLetterManagementServiceDao gccDao;

	@Autowired @Qualifier("jaxbUtil")
	private JAXBUtil jaxbUtil;
	
	@Autowired @Qualifier("jaxbUtil_v2")
	private JAXBUtil jaxbUtil_v2;
	
	@Autowired @Qualifier("jaxbUtil_v3")
	private JAXBUtil jaxbUtil_v3;
	
	
	public void handle ( String  confirmationMessage, ClientIdentity clientIdentity, MessageBeanContext beanContext ) throws ApplicationException{
		
		try {
			
			ConfirmationNotification confirmationNotification = jaxbUtil.xmlToObject( confirmationMessage, ConfirmationNotification.class );
			
			if ( ReferencePdsWrapper.isNotificationEligible(
					confirmationNotification.getTransactionType(),
					confirmationNotification.getAuditInfo().getOriginatorApplicationId(),
					confirmationNotification.getBillingAccount().getBrandId(),
					confirmationNotification.getBillingAccount().getAccountType() + confirmationNotification.getBillingAccount().getAccountSubType(),
					confirmationNotification.getBillingAccount().getSegment(),
					confirmationNotification.getSubscriber()==null? null: confirmationNotification.getSubscriber().getProductType())) {
				
				
				String[] templateInfo = ReferencePdsWrapper.getFormLetterTemplate( confirmationNotification.getTransactionType(),
						confirmationNotification.getBillingAccount().getBrandId(),
						confirmationNotification.getBillingAccount().getAccountType() + confirmationNotification.getBillingAccount().getAccountSubType(),
						confirmationNotification.getBillingAccount().getSegment(),
						confirmationNotification.getSubscriber()==null? null: confirmationNotification.getSubscriber().getProductType(),
						"EMAIL",
						confirmationNotification.getLanguage()
						);
				
				String schemaVersion = templateInfo[2];
				
				String gccMessage =  composeXmlContent( confirmationNotification, schemaVersion );

				if ( LOGGER.isDebugEnabled()) {
					LOGGER.debug("\nNotification[type=" + confirmationNotification.getTransactionType() 
							+ ", accountNo=" +confirmationNotification.getBillingAccount().getAccountNumber() 
							+ ", template=" + templateInfo[0] + "/" + templateInfo[1] + "/" + templateInfo[2]
							+ "], xmlContent:\n"
							+ gccMessage );
				}
				
				gccDao.submitFormLetter(templateInfo[0], templateInfo[1], confirmationNotification, gccMessage);
			} else {
				if ( LOGGER.isInfoEnabled()) {
					LOGGER.info("\nNotification is not eligible, originalXML message:\n"+ confirmationMessage );
				}
			}
			
		} catch ( ApplicationException e) {
			LOGGER.error("\nEncountered exception while process notificaiton message.\noriginal message:\n" + confirmationMessage, e);
			throw e;
		} catch ( RuntimeException e) {
			LOGGER.error("\nEncountered SystemException while process notificaiton message.\noriginal message:\n" + confirmationMessage, e);
			throw e;
		}
	}
	
	private String composeXmlContent(ConfirmationNotification notificationInfo, String schemaVersion) {
		
		Object gccTemplateVariable = null;
		
		if ( notificationInfo.getServiceChange()!=null 
			|| notificationInfo.getTransferOwnership()!=null 
			|| notificationInfo.getServiceCancellation()!=null ) {
			
			gccTemplateVariable = MapperFactory.getSubscriberTransactionMapper(schemaVersion).mapToSchema(notificationInfo);
		} else {
			
			gccTemplateVariable = MapperFactory.getAccountTransactionMapper(schemaVersion).mapToSchema(notificationInfo);
		}

		String xml = getJAXBUtil(schemaVersion).objToXML( gccTemplateVariable );
		
		return xml;
	}
	
	private JAXBUtil getJAXBUtil(String schemaVersion) {
		if ("3.0".equals(schemaVersion)) {
			return jaxbUtil_v3;
		}else if ("2.0".equals(schemaVersion)) {
			return jaxbUtil_v2;
		}else {
			return jaxbUtil;
		}
	}
	
	
}
