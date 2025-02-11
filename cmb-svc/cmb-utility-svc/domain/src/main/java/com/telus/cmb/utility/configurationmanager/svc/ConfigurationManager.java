package com.telus.cmb.utility.configurationmanager.svc;

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


public interface ConfigurationManager {

	  com.telus.eas.config1.info.ConfigurationInfo getConfiguration1(String[] path) throws ApplicationException;

	  com.telus.eas.config1.info.ConfigurationInfo getConfiguration1(String path) throws ApplicationException;

	  ConfigurationInfo[] getConfiguration(String[] path) throws ApplicationException;
	  
	  ConfigurationInfo[] getChildConfigurations(int parentConfigurationId) throws ApplicationException;
	  
	  int removeConfiguration(int configurationId) throws ApplicationException;
	  
	  ConfigurationInfo newConfiguration(int parentConfigurationId, String name) throws ApplicationException;
	  
	  int removeProperties(int configurationId, boolean recursively) throws ApplicationException;
	  
	  int removeProperties(int configurationId, String[] name) throws ApplicationException;
	  
	  long getActivationLogID() throws ApplicationException;
	  
	  void logActivation(com.telus.eas.config1.info.ActivationLogInfo pActivationLogInfo, 
			  long pActLogID) throws ApplicationException;
	  
	  void logApplication(LogInfo plogInfo) throws ApplicationException;
	  
	  void logActivationSummary(com.telus.eas.config1.info.ActivationLogInfo 
			  pActivationLogInfo) throws ApplicationException;
	  
	  ConfigurationInfo loadProperties(ConfigurationInfo info) throws ApplicationException;
	  
	  void addProperties(int configurationId, String[] name, String[] value) throws ApplicationException;
	  
	  InteractionInfo newReport(String transactionType, String applicationId, 
			  int operatorId, String dealerCode, String salesRepCode, 
			  int banId, String subscriberId, long reasonId) throws ApplicationException;
	  
	  void report_changePricePlan(long transactionId, java.util.Date transactionDate, 
			  String oldPlan, String newPlan, ServiceAgreementInfo[] services) throws ApplicationException;
	  
	  void report_changeAddress(long transactionId, java.util.Date transactionDate, 
			  AddressInfo oldAddr, AddressInfo newAddr) throws ApplicationException;
	  
	  void report_subscriberNewCharge(long transactionId, java.util.Date transactionDate, 
			  String chargeCode, String waiverCode) throws ApplicationException;
	  
	  void report_changePaymentMethod(long transactionId, java.util.Date transactionDate, 
			  char oldPaymentMethod, char newPaymentMethod) throws ApplicationException;
	  
	  void report_makePayment(long transactionId, java.util.Date transactionDate, 
			  char paymentMethod, double paymentAmount) throws ApplicationException;
	  
	  void report_accountStatusChange(long transactionId, java.util.Date transactionDate, char oldHotlinedInd,
			  char newHotlinedInd, char oldStatus, char newStatus, char statusFlag) throws ApplicationException;
	  
	  void report_prepaidAccountTopUp(long transactionId, java.util.Date transactionDate, 
			  Double amount, char cardType, char topUpType) throws ApplicationException;
	  
	  void report_changePhoneNumber(long transactionId, java.util.Date transactionDate, 
			  String oldPhoneNumber, String newPhoneNumber) throws ApplicationException;
	  
	  void report_changeService(long transactionId, java.util.Date transactionDate, 
			  ServiceAgreementInfo[] services) throws ApplicationException;
	  
	  void report_changeSubscriber(long transactionId, java.util.Date transactionDate, 
			  SubscriberInfo oldSubscriber, SubscriberInfo newSubscriber) throws ApplicationException;
	  
	  void report_subscriberChangeEquipment(long transactionId, java.util.Date transactionDate,
			  SubscriberInfo subscriberInfo, EquipmentInfo oldEquipmentInfo, EquipmentInfo newEquipmentInfo,
			  String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType,
			  EquipmentInfo associatedMuleEquipmentInfo, String applicationName) throws ApplicationException;

	  void report_changeRole(long transactionId, java.util.Date transactionDate, 
			  String oldRole, String newRole) throws ApplicationException;
	  
	  InteractionDetail[] getInteractionDetails(long id) throws ApplicationException;
	  
	  Interaction[] getInteractionsBySubscriber(String subscriberId, 
			  java.util.Date from, java.util.Date to, String type) throws ApplicationException;
	  
	  Interaction[] getInteractionsByBan(int ban, java.util.Date from,
			  java.util.Date to, String type) throws ApplicationException;
	  
	  EquipmentChangeInfo getEquipmentChangeByESN(String oldESN, String newESN) throws ApplicationException;
}
