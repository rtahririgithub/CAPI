package com.telus.cmb.notification.management.dao;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.confirmationnotification.ConfirmationNotification;
import com.telus.cmb.common.confirmationnotification.Subscriber;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.EnterpriseFormLetterManagementPort;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.enterpriseformlettermanagementservicerequestresponse_v1.SubmitFormLetter;
import com.telus.tmi.xmlschema.xsd.customer.customer.formletterservicetypes_v2.DigitalEnvelopeValue;
import com.telus.tmi.xmlschema.xsd.customer.customer.formletterservicetypes_v2.EmailLetterEnvelope;
import com.telus.tmi.xmlschema.xsd.customer.customer.formletterservicetypes_v2.FormLetter;
import com.telus.tmi.xmlschema.xsd.customer.customer.formletterservicetypes_v2.FormLetterContext;
import com.telus.tmi.xmlschema.xsd.customer.customer.formletterservicetypes_v2.FormLetterTemplate;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.AuditInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;

public class EnterpriseFormLetterManagementServiceDaoImpl extends SoaBaseSvcClient implements EnterpriseFormLetterManagementServiceDao {
	
	private static final Logger LOGGER = Logger.getLogger(EnterpriseFormLetterManagementServiceDaoImpl.class);
	
	private static final String DELIVERY_CHANNEL_EMAIL = "EMAIL";
	private static final long KB_MASTER_SOURCE = 130L;

	@Autowired
	private EnterpriseFormLetterManagementPort port;
	
	@Override
	public void submitFormLetter( final String categoryCode, final String templateCode , final ConfirmationNotification notificationInfo, final String xmlContent ) throws ApplicationException {
		
		execute( new SoaCallback<Object>() {
			
			@Override
			public Object doCallback() throws Throwable {

				FormLetter formLetter = new FormLetter();
				formLetter.setCreatedTs( new Date());
				formLetter.setDeliveryChannelCode(DELIVERY_CHANNEL_EMAIL);
				formLetter.setLanguageCode(notificationInfo.getLanguage());
				
				DigitalEnvelopeValue digitalEnvelope = new DigitalEnvelopeValue();
				EmailLetterEnvelope emailLetterEnvelope  = new EmailLetterEnvelope();
				emailLetterEnvelope.setToEmailAddress(notificationInfo.getEmailAddress());
				digitalEnvelope.setEmailLetterEnvelope(emailLetterEnvelope);
				formLetter.setDigitalEnvelopeValue(digitalEnvelope);
				
				//FormLetterContext
				FormLetterContext formLetterContext = new FormLetterContext();
				formLetterContext.setBillingAccountMasterSrcId( KB_MASTER_SOURCE );
				formLetterContext.setBillingAccountNumber( notificationInfo.getBillingAccount().getAccountNumber() );

				Subscriber subscriber =  notificationInfo.getSubscriber();
				if ( subscriber!=null ) {
					formLetterContext.setProductInstanceKeyId( subscriber.getSubscriptionId() );
					formLetterContext.setProductInstanceMasterSrcId(KB_MASTER_SOURCE);
					
					formLetterContext.setResourceTypeId("TN");
					formLetterContext.setResourceValueId(subscriber.getSubscriberPhoneNumber());
				}
				formLetter.setFormLetterContext(formLetterContext);
				
				//fill out FormLetterTemplate info 
				FormLetterTemplate formLetterTemplate = new FormLetterTemplate();
				formLetterTemplate.setCategoryCode(categoryCode);
				formLetterTemplate.setTemplateCode(templateCode);
				formLetter.setTemplate(formLetterTemplate);
				
				//the actual XML content contain dynamic variable values 
				formLetter.setXmlContent( xmlContent );
				
				AuditInfo auditInfo = null;
				if (notificationInfo.getAuditInfo()!=null) {
					auditInfo = new AuditInfo();
					com.telus.cmb.common.confirmationnotification.AuditInfo src = notificationInfo.getAuditInfo();
					auditInfo.setChannelOrganizationId(src.getChannelOrganizationId());
					auditInfo.setCorrelationId(src.getCorrelationId());
					auditInfo.setOriginatorApplicationId(src.getOriginatorApplicationId());
					auditInfo.setOutletId(src.getOutletId());
					auditInfo.setSalesRepresentativeId(src.getSalesRepresentativeId());
					if (src.getTimestamp()!=null) {
						auditInfo.setTimestamp(src.getTimestamp());
					}
					auditInfo.setUserId(src.getUserId());
					auditInfo.setUserTypeCode(src.getUserTypeCode());
				}
				
				SubmitFormLetter request = new SubmitFormLetter();
				request.setFormLetter(formLetter);
				request.setAuditInfo(auditInfo);
				request.setLetterFailureOverrideInd(false);
				
				port.submitFormLetter(request);
				
				LOGGER.debug("email notification request has been submitted");
				
				return null;
			}
		});
	}

	@Override
	public String ping() throws ApplicationException {

		return execute( new SoaCallback<String>() {
		
			@Override
			public String doCallback() throws Throwable {
				return port.ping( new Ping()).getVersion();
			}
		});
	}

}
