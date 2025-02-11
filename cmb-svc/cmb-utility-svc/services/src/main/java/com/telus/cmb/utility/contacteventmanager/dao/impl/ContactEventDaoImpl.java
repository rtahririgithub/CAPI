package com.telus.cmb.utility.contacteventmanager.dao.impl;

import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.cmb.utility.contacteventmanager.dao.ContactEventDao;
import com.telus.cmb.wsclient.EnterpriseCustomerNotificationManagementServicePort;
import com.telus.eas.contactevent.info.SMSNotificationInfo;
import com.telus.framework.config.ConfigContext;
import com.telus.tmi.xmlschema.srv.cmo.contactmgmt.enterprisecustomernotificationmanagementservicerequestresponse_v1.SubmitNotification;
import com.telus.tmi.xmlschema.xsd.customer.customerinteraction.customernotificationtypes_v1.BillingAccountSourceType;
import com.telus.tmi.xmlschema.xsd.customer.customerinteraction.customernotificationtypes_v1.ChannelAttributeOverrideType;
import com.telus.tmi.xmlschema.xsd.customer.customerinteraction.customernotificationtypes_v1.NotificationTargetType;
import com.telus.tmi.xmlschema.xsd.customer.customerinteraction.customernotificationtypes_v1.ResourceSourceType;
import com.telus.tmi.xmlschema.xsd.customer.customerinteraction.customernotificationtypes_v1.SMSAttributeType;
import com.telus.tmi.xmlschema.xsd.customer.customerinteraction.customernotificationtypes_v1.SMSAttributeType.ShortMessageTemplate;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.AuditInfo;
public class ContactEventDaoImpl implements ContactEventDao {

	private JdbcTemplate codsJdbcTemplate;
	@Deprecated
	//As of 2015 Oct release, as part of SERV DB upgrade, this variable is marked as deprecated and should not use any more. 
	private JdbcTemplate coneJdbcTemplate;

	
	private EnterpriseCustomerNotificationManagementServicePort ecnmsPort;

	
	private static final Logger LOGGER = Logger.getLogger(ContactEventDaoImpl.class);
	
	public EnterpriseCustomerNotificationManagementServicePort getEcnmsPort() {
		return ecnmsPort;
	}

	public void setEcnmsPort(EnterpriseCustomerNotificationManagementServicePort ecnmsPort) {
		this.ecnmsPort = ecnmsPort;
	}

	public JdbcTemplate getCodsJdbcTemplate() {
		return codsJdbcTemplate;
	}

	public void setCodsJdbcTemplate(JdbcTemplate codsJdbcTemplate) {
		this.codsJdbcTemplate = codsJdbcTemplate;
	}

	@Deprecated 
	//As of 2015 Oct release, as part of SERV DB upgrade, this method is marked as deprecated and should not use any more.
	public JdbcTemplate getConeJdbcTemplate() {
		return coneJdbcTemplate;
	}

	@Deprecated 
	//As of 2015 Oct release, as part of SERV DB upgrade, this method is marked as deprecated and should not use any more.
	public void setConeJdbcTemplate(JdbcTemplate coneJdbcTemplate) {
		this.coneJdbcTemplate = coneJdbcTemplate;
	}

	@Override
	public long getAccountID(String ban) throws ApplicationException {
		String sql = "SELECT client_account_id FROM client_account WHERE ban = ?";
		Object[] args = new Object[] {ban};
		
		return getCodsJdbcTemplate().query(sql, args, new ResultSetExtractor<Long>() {

			@Override
			public Long extractData(ResultSet result) throws SQLException, DataAccessException {
				long accountId = -1;
				if (result.next()) {
					accountId = result.getLong(1);
				}

				return accountId;
			}
		});
	}

	@Override
	public long getSubscriptionID(String min) throws ApplicationException {
		String sql = "SELECT max(subscription_id) FROM subscription WHERE " +
        				"cell_phone_no = ? AND current_status_cd = 'A'";
		Object[] args = new Object[] {min};

		return getCodsJdbcTemplate().query(sql, args, new ResultSetExtractor<Long>() {

			@Override
			public Long extractData(ResultSet result) throws SQLException, DataAccessException {
				long subscriptionId = -1;
				if (result.next()) {
					subscriptionId = result.getLong(1);
				}

				return subscriptionId;
			}
		});
	}
	
    /**
     * Record an Account Authentication Contact Event in CONE database
     * with optional associated dealer ids (could be null).
     *
     * @param accountID is CODS.CLIENT_ACCOUNT_ID
     * @param isAuthenticationSuccedded
     * @param channelOrganizationID is CODS.CHNL_ORG_CD
     * @param outletID is CODS.OUTLET_CODE
     * @param salesRepID is CODS.SALES_REP_CD
     * @param applicationID is Telus API - Provider application ID
     * @param userID is Telus API - Provider user ID
     * @return 
     * @throws TelusAPIException
     *
     */
	@Deprecated 
	//As of 2015 Oct release, as part of SERV DB upgrade, this method is marked as deprecated and should not use any more.
	@Override
	public void logAccountAuthentication(final long accountID,
			boolean isAuthenticationSucceeded, final String channelOrganizationID,
			final String outletID, final String salesRepID, final String applicationID,
			final String userID) throws ApplicationException {
		
		LOGGER.info("Deprecated method was called: logAccountAuthentication.");
		
		String call =  "{ call contact_event_pkg.insertAccAuthenticationCE(?,?,?,?,?,?,?) }";
		final String successCode = (isAuthenticationSucceeded ? "Y" : "N" );
		
		getConeJdbcTemplate().execute(call, new CallableStatementCallback<Object>() {
			@Override
			public Object doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				callable.setLong   (1, accountID);
		        callable.setString (2, successCode);
		        callable.setString (3, channelOrganizationID);
		        callable.setString (4, outletID);
		        callable.setString (5, salesRepID);
		        callable.setString (6, applicationID);
		        callable.setString (7, userID);

		        callable.execute();
		        
		        return null;
			}
		});
	}

	@Deprecated 
	//As of 2015 Oct release, as part of SERV DB upgrade, this method is marked as deprecated and should not use any more.
	@Override
	public void logSubscriberAuthentication(final long subscriptionID,
			boolean isAuthenticationSucceeded, final String channelOrganizationID,
			final String outletID, final String salesRepID, final String applicationID,
			final String userID) throws ApplicationException {
		
		LOGGER.info("Deprecated method was called: logSubscriberAuthentication.");
		
		String call =  "{ call contact_event_pkg.insertSubAuthenticationCE(?,?,?,?,?,?,?) }";
		final String successCode = (isAuthenticationSucceeded ? "Y" : "N" );
		
		getConeJdbcTemplate().execute(call, new CallableStatementCallback<Object>() {
			@Override
			public Object doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				callable.setLong(1, subscriptionID);
				callable.setString(2, successCode);
				callable.setString(3, channelOrganizationID);
				callable.setString(4, outletID);
				callable.setString(5, salesRepID);
				callable.setString(6, applicationID);
				callable.setString(7, userID);
				callable.execute();
		        
		        return null;
			}
		});
	}
	
	@Override
	public void processNotification(SMSNotificationInfo notification) throws ApplicationException {
		LOGGER.debug("[Utility] processNotification - Keystone through ECNMS V1.2");
		try {
			sendNotification(notification);
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_ECNMS_DAO, t.getMessage(), "", t);
		}
	}
	
	private boolean isEbillType(String contentTypeCode) {
		List<String> ebillList = new ArrayList<String>();
		String smsContentTypeToEbill = ConfigContext.getProperty("ContactEventManager/smsContentTypeToEbill"); 
		if (smsContentTypeToEbill != null) {
			String[] ebillArray = smsContentTypeToEbill.split(",");
			for (String ebillType : ebillArray) {
				ebillList.add(ebillType.trim());
			}
		}
		return ebillList.contains(contentTypeCode);
	}
	
	private String toVariableXmlContent(String[] params) throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("legacy");
		doc.appendChild(rootElement);

		int seq = 1;
		for (String param : params) {
			Element paramElement = doc.createElement("t"+(seq++));
			paramElement.appendChild(doc.createTextNode(param));
			rootElement.appendChild(paramElement);
		}
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
		LOGGER.debug("[VariableXmlContent]" + output);
		return output;
	}

	
	private void sendNotification(SMSNotificationInfo notification) throws Exception {
		
		SubmitNotification input = new SubmitNotification();		
		
		BillingAccountSourceType billingAccountSource = new BillingAccountSourceType();
		billingAccountSource.setBillingAccountNumber(String.valueOf(notification.getBanId()));
		billingAccountSource.setBillingAccountMasterSourceId(130); // As P.J.

		ResourceSourceType resourceSourceType = new ResourceSourceType();
		resourceSourceType.setResourceTypeId("TN");
		resourceSourceType.setResourceValueId(notification.getSubscriberNumber()); // As M.J.
		
		NotificationTargetType notificationTarget = new NotificationTargetType();
		notificationTarget.setBillingAccountSource(billingAccountSource);
		notificationTarget.setResourceSource(resourceSourceType);

		input.setNotificationTarget(notificationTarget);
		input.setTransactionType(isEbillType(notification.getNotificationTypeCode()) ? "EBILL" : "LEGACY"); // As Canh, Jun 17, 2014
		if (notification.getContentParameters() != null && notification.getContentParameters().length > 0) {
			input.setVariableXmlContent(toVariableXmlContent(notification.getContentParameters()));
		}

		ShortMessageTemplate shortMessageTemplate = new ShortMessageTemplate();
		shortMessageTemplate.setTemplateCode(notification.getTemplateCode()); 

		SMSAttributeType smsAttribute = new SMSAttributeType();
		smsAttribute.setShortMessageTemplate(shortMessageTemplate);
		smsAttribute.setMobilityPhoneNumber(notification.getSubscriberNumber());
		smsAttribute.setLanguageCode(notification.getLanguage());
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(notification.getDeliveryDate());
		XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		smsAttribute.setDeliveryDateTime(calendar);
		smsAttribute.setTimeToLiveAmt(notification.getTimeToLive());
		
		ChannelAttributeOverrideType deliveryChannelOverrideAttribute = new ChannelAttributeOverrideType();
		deliveryChannelOverrideAttribute.setSMSAttribute(smsAttribute);
		input.setDeliveryChannelOverrideAttribute(deliveryChannelOverrideAttribute);
		
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOriginatorApplicationId(notification.getApplication());
		auditInfo.setUserId(notification.getUser());
		input.setAuditInfo(auditInfo);
		
		ecnmsPort.submitNotification(input);
	}

}