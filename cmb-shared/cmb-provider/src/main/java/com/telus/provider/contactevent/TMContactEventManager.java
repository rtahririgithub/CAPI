package com.telus.provider.contactevent;

import com.telus.api.TelusAPIException;
import com.telus.api.account.AccountManager;
import com.telus.api.account.EquipmentAccountMissMatchException;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.api.contactevent.ContactEventManager;
import com.telus.api.contactevent.EquipmentNotSMSCapableException;
import com.telus.api.contactevent.SMSNotification;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentManager;
import com.telus.eas.contactevent.info.SMSNotificationInfo;
import com.telus.eas.utility.info.NotificationMessageTemplateInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.reference.TMReferenceDataManager;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class TMContactEventManager extends BaseProvider implements ContactEventManager {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TMReferenceDataManager referenceMgr;
	private AccountManager acctMgr;
	private EquipmentManager equipMgr;

  public TMContactEventManager(TMProvider provider) throws TelusAPIException {
    super(provider);

    referenceMgr = provider.getReferenceDataManager0();
    acctMgr = provider.getAccountManager();
    equipMgr = provider.getEquipmentManager();
  }

  public SMSNotification newSMSNotification() throws TelusAPIException {

    SMSNotificationInfo n = new SMSNotificationInfo();
    n.setApplication(provider.getApplication());
    n.setUser(provider.getUser());

    return (new TMSMSNotification(provider, n));
  }

  public synchronized void process(SMSNotification notification) throws UnknownSubscriberException, EquipmentAccountMissMatchException, EquipmentNotSMSCapableException, TelusAPIException {

    if (notification == null)
      throw new java.lang.IllegalArgumentException("SMSNotification object cannot be null.");

    validateRequest(notification);

    if (notification.isValidatingInputRequest())
      validate(notification);

    try {
      SMSNotificationInfo n = ((TMSMSNotification)notification).getDelegate0();
      NotificationMessageTemplateInfo template = referenceMgr.getNotificationMessageTemplateInfo(n.getNotificationTypeCode());

      n.setNotificationMessageTemplateInfo(template);

      // process the request, insert into CONE database
      
		provider.getContactEventManagerNew().processNotification(((TMSMSNotification)notification).getDelegate0());
				      
    }	catch (Throwable t) {
		provider.getExceptionHandler().handleException(t);
	}

  }

  /**
   * Record a Subscriber Authentication Contact Event in CONE database
   * with optional associated dealer ids (could be null).
   *
   * @param subscriptionID is CODS.SUBSCRIPTION_ID
   * @param isAuthenticationSuccedded
   * @param channelOrganizationID is CODS.CHNL_ORG_ID
   * @param outletID is CODS.OUTLET_ID
   * @param salesRepID is CODS.SALES_REP_ID
   * @throws TelusAPIException
   */
	@Deprecated 
	//As of 2015 Oct release, as part of SERV DB upgrade, this method is marked as deprecated and should not use any more.
  public void logSubscriberAuthentication(
      long subscriptionID,
      boolean isAuthenticationSucceeded,
      String channelOrganizationID,
      String outletID,
      String salesRepID
      ) throws TelusAPIException {

    try {
    	provider.getContactEventManagerNew().logSubscriberAuthentication(
  	              subscriptionID,
	              isAuthenticationSucceeded,
	              channelOrganizationID,
	              outletID,
	              salesRepID,
	              provider.getApplication(),
	              provider.getUser()
	             );    		
    	

    } catch (Throwable t) {
		provider.getExceptionHandler().handleException(t);
	}

  }

  /**
   * Record a Subscriber Authentication Contact Event in CONE database
   * with optional associated dealer ids (could be null).
   *
   * @param ban is Knowbility account number (BAN)
   * @param isAuthenticationSuccedded
   * @param channelOrganizationID is CODS.CHNL_ORG_ID
   * @param outletID is CODS.OUTLET_ID
   * @param salesRepID is CODS.SALES_REP_ID
   * @throws TelusAPIException
   */
	@Deprecated 
	//As of 2015 Oct release, as part of SERV DB upgrade, this method is marked as deprecated and should not use any more.
  public void logSubscriberAuthentication(
      String ban,
      boolean isAuthenticationSucceeded,
      String channelOrganizationID,
      String outletID,
      String salesRepID
      ) throws TelusAPIException {

    try {
       	provider.getContactEventManagerNew().logSubscriberAuthentication(
    		           ban,
    		           isAuthenticationSucceeded,
    		           channelOrganizationID,
    		           outletID,
    		           salesRepID,
    		           provider.getApplication(),
    		           provider.getUser()
    		          );
       	
    } catch (Throwable t) {
		provider.getExceptionHandler().handleException(t);
	}

  }


  /**
   * Record an Account Authentication Contact Event in CONE database
   * with optional associated dealer ids (could be null).
   *
   * @param accountID is CODS.CLIENT_ACCOUNT_ID
   * @param isAuthenticationSucceeded
   * @param channelOrganizationID is CODS.CHNL_ORG_ID
   * @param outletID is CODS.OUTLET_ID
   * @param salesRepID is CODS.SALES_REP_ID
   * @throws TelusAPIException
   */
	@Deprecated 
	//As of 2015 Oct release, as part of SERV DB upgrade, this method is marked as deprecated and should not use any more.
  public void logAccountAuthentication (
      long accountID,
      boolean isAuthenticationSucceeded,
      String channelOrganizationID,
      String outletID,
      String salesRepID
      ) throws TelusAPIException{

    try {
      		provider.getContactEventManagerNew().logAccountAuthentication(
    		           accountID,
    		           isAuthenticationSucceeded,
    		           channelOrganizationID,
    		           outletID,
    		           salesRepID,
    		           provider.getApplication(),
    		           provider.getUser()
    		          );
      	
    } catch (Throwable t) {
		provider.getExceptionHandler().handleException(t);
	}
  }

  /**
   * Record an Account Authentication Contact Event in CONE database
   * with optional associated dealer ids (could be null).
   *
   * @param ban is Knowbility account number (BAN)
   * @param isAuthenticationSucceeded
   * @param channelOrganizationID is CODS.CHNL_ORG_ID
   * @param outletID is CODS.OUTLET_ID
   * @param salesRepID is CODS.SALES_REP_ID
   * @throws TelusAPIException
   */
	@Deprecated 
	//As of 2015 Oct release, as part of SERV DB upgrade, this method is marked as deprecated and should not use any more.
  public void logAccountAuthentication (
      String ban,
      boolean isAuthenticationSucceeded,
      String channelOrganizationID,
      String outletID,
      String salesRepID
      ) throws TelusAPIException{

    try {
     	provider.getContactEventManagerNew().logAccountAuthentication(
     	             ban,
     	             isAuthenticationSucceeded,
     	             channelOrganizationID,
     	             outletID,
     	             salesRepID,
     	             provider.getApplication(),
     	             provider.getUser()
     	            );     		
     	
    } catch (Throwable t) {
		provider.getExceptionHandler().handleException(t);
	}
  }

  public void validate(SMSNotification notification) throws UnknownSubscriberException, EquipmentAccountMissMatchException, EquipmentNotSMSCapableException, TelusAPIException {

    acctMgr.findAccountByBAN(notification.getBanId());
    acctMgr.findSubscriberByPhoneNumber(notification.getSubscriberNumber());
    Equipment equipment = equipMgr.getEquipment(notification.getEquipmentSerialNumber());

    if (!equipment.isSMSCapable()) {
      throw new EquipmentNotSMSCapableException("Equipment [" + equipment.getSerialNumber() +
                                                "] is not SMS capable");
    }
  }

  public void validateRequest(SMSNotification n) {

    String msg = null;

    if (n.getBanId() == 0)
      msg = "Ban Id is not set";
    else if (n.getDeliveryDate() == null)
      msg = "Delivery Date is not set";
    else if (n.getEquipmentSerialNumber() == null)
      msg = "Equipment Serial Number is not set";
    else if (n.getLanguage() == null)
      msg = "Language is not set";
    else if (n.getNotificationType() == null)
      msg = "Notification Type is not set";
    else if (n.getPhoneNumber() == null)
      msg = "Phone Number is not set";
    else if (n.getPriority() == 0)
      msg = "Priority is not set";
    else if (n.getProductType() == null)
      msg = "Product Type is not set";
    else if (n.getSubscriberNumber() == null)
      msg = "Subscriber Number is not set";
    else if (n.getTimeToLive() == 0)
      msg = "Time To Live is not set";

    if (msg != null)
      throw new IllegalStateException(msg);
  }
}
