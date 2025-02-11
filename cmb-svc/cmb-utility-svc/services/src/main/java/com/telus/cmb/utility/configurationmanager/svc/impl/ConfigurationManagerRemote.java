package com.telus.cmb.utility.configurationmanager.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.telus.api.ApplicationException;
import com.telus.api.interaction.Interaction;
import com.telus.api.interaction.InteractionDetail;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.config.info.ConfigurationInfo;
import com.telus.eas.config.info.EquipmentChangeInfo;
import com.telus.eas.config.info.InteractionInfo;
import com.telus.eas.config.info.LogInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public interface ConfigurationManagerRemote extends EJBObject {

	  com.telus.eas.config1.info.ConfigurationInfo getConfiguration1(String[] path) throws ApplicationException, RemoteException;

	  com.telus.eas.config1.info.ConfigurationInfo getConfiguration1(String path) throws ApplicationException, RemoteException;

	  ConfigurationInfo[] getConfiguration(String[] path) throws ApplicationException, RemoteException;
	  
	  ConfigurationInfo[] getChildConfigurations(int parentConfigurationId) throws ApplicationException, RemoteException;
	  
	  int removeConfiguration(int configurationId) throws ApplicationException, RemoteException;
	  
	  ConfigurationInfo newConfiguration(int parentConfigurationId, String name) throws ApplicationException, RemoteException;
	  
	  int removeProperties(int configurationId, boolean recursively) throws ApplicationException, RemoteException;
	  
	  int removeProperties(int configurationId, String[] name) throws ApplicationException, RemoteException;
	  
	  long getActivationLogID() throws ApplicationException, RemoteException;
	  
	  void logActivation(com.telus.eas.config1.info.ActivationLogInfo pActivationLogInfo, 
			  long pActLogID) throws ApplicationException, RemoteException;
	  
	  void logApplication(LogInfo plogInfo) throws ApplicationException, RemoteException;
	  
	  void logActivationSummary(com.telus.eas.config1.info.ActivationLogInfo 
			  pActivationLogInfo) throws ApplicationException, RemoteException;
	  
	  ConfigurationInfo loadProperties(ConfigurationInfo info) throws ApplicationException, RemoteException;
	  
	  void addProperties(int configurationId, String[] name, 
			  String[] value) throws ApplicationException, RemoteException;
	  
	  InteractionInfo newReport(String transactionType, String applicationId, 
			  int operatorId, String dealerCode, String salesRepCode, 
			  int banId, String subscriberId, long reasonId) throws ApplicationException, RemoteException;
	  
	  void report_changePricePlan(long transactionId, java.util.Date transactionDate, 
			  String oldPlan, String newPlan, 
			  ServiceAgreementInfo[] services) throws ApplicationException, RemoteException;
	  
	  void report_changeAddress(long transactionId, java.util.Date transactionDate, 
			  AddressInfo oldAddr, AddressInfo newAddr) throws ApplicationException, RemoteException;
	  
	  void report_subscriberNewCharge(long transactionId, java.util.Date transactionDate, 
			  String chargeCode, String waiverCode) throws ApplicationException, RemoteException;
	  
	  void report_changePaymentMethod(long transactionId, java.util.Date transactionDate, 
			  char oldPaymentMethod, char newPaymentMethod) throws ApplicationException, RemoteException;
	  
	  void report_makePayment(long transactionId, java.util.Date transactionDate, 
			  char paymentMethod, double paymentAmount) throws ApplicationException, RemoteException;
	  
	  void report_accountStatusChange(long transactionId, java.util.Date transactionDate, char oldHotlinedInd,
			  char newHotlinedInd, char oldStatus, char newStatus, 
			  char statusFlag) throws ApplicationException, RemoteException;
	  
	  void report_prepaidAccountTopUp(long transactionId, java.util.Date transactionDate, 
			  Double amount, char cardType, char topUpType) throws ApplicationException, RemoteException;
	  
	  void report_changePhoneNumber(long transactionId, java.util.Date transactionDate, 
			  String oldPhoneNumber, String newPhoneNumber) throws ApplicationException, RemoteException;
	  
	  void report_changeService(long transactionId, java.util.Date transactionDate, 
			  ServiceAgreementInfo[] services) throws ApplicationException, RemoteException;
	  
	  void report_changeSubscriber(long transactionId, 
			  java.util.Date transactionDate, SubscriberInfo oldSubscriber, 
			  SubscriberInfo newSubscriber) throws ApplicationException, RemoteException;
	  
	  void report_subscriberChangeEquipment(long transactionId, java.util.Date transactionDate,
			  SubscriberInfo subscriberInfo, EquipmentInfo oldEquipmentInfo, EquipmentInfo newEquipmentInfo,
			  String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType,
			  EquipmentInfo associatedMuleEquipmentInfo, 
			  String applicationName) throws ApplicationException, RemoteException;

	  void report_changeRole(long transactionId, java.util.Date transactionDate, 
			  String oldRole, String newRole) throws ApplicationException, RemoteException;
	  
	  InteractionDetail[] getInteractionDetails(long id) throws ApplicationException, RemoteException;
	  
	  Interaction[] getInteractionsBySubscriber(String subscriberId, 
			  java.util.Date from, java.util.Date to, String type) throws ApplicationException, RemoteException;
	  
	  Interaction[] getInteractionsByBan(int ban, java.util.Date from,
			  java.util.Date to, String type) throws ApplicationException, RemoteException;
	  
	  EquipmentChangeInfo getEquipmentChangeByESN(String oldESN, 
			  String newESN) throws ApplicationException, RemoteException;


}
