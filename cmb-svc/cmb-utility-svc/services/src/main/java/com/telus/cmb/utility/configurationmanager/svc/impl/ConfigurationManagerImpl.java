package com.telus.cmb.utility.configurationmanager.svc.impl;

import java.util.Date;

import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.interaction.Interaction;
import com.telus.api.interaction.InteractionDetail;
import com.telus.cmb.common.dao.testpoint.DataSourceTestPointDao;
import com.telus.cmb.utility.configurationmanager.dao.ConfigurationManagerDao;
import com.telus.cmb.utility.configurationmanager.svc.ConfigurationManager;
import com.telus.cmb.utility.configurationmanager.svc.ConfigurationManagerTestPoint;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.config.info.ConfigurationInfo;
import com.telus.eas.config.info.EquipmentChangeInfo;
import com.telus.eas.config.info.InteractionInfo;
import com.telus.eas.config.info.LogInfo;
import com.telus.eas.config1.info.ActivationLogInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.framework.config.ConfigContext;


@Stateless(name="ConfigurationManager", mappedName="ConfigurationManager")
@Remote({ConfigurationManager.class, ConfigurationManagerTestPoint.class})
@RemoteHome(ConfigurationManagerHome.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)

@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

public class ConfigurationManagerImpl implements ConfigurationManager, ConfigurationManagerTestPoint {
	private static final Logger LOGGER = Logger.getLogger(ConfigurationManagerImpl.class);

	@Autowired
	private ConfigurationManagerDao configurationManagerDao;
	@Autowired
	@Qualifier("cfmTestPointDao")
	private DataSourceTestPointDao testPointDao;
	
	public void setConfigurationManagerDao(	ConfigurationManagerDao configurationManagerDao) {
		this.configurationManagerDao = configurationManagerDao;
	}

	@Override
	public void addProperties(int configurationId, String[] name, String[] value)
			throws ApplicationException {
		configurationManagerDao.addProperties(configurationId, name, value);
	}

	@Override
	public long getActivationLogID() throws ApplicationException {
		return configurationManagerDao.getActivationLogID();
	}

	@Override
	public ConfigurationInfo[] getChildConfigurations(int parentConfigurationId)
			throws ApplicationException {
		return configurationManagerDao.getChildConfigurations(parentConfigurationId);
	}

	@Override
	public ConfigurationInfo[] getConfiguration(String[] path)
			throws ApplicationException {
		return configurationManagerDao.getConfiguration(path);
	}

	@Override
	public com.telus.eas.config1.info.ConfigurationInfo getConfiguration1(String[] path) throws ApplicationException {
		com.telus.api.config.ConfigurationManager configurationManager = null;
		try {
			configurationManager = com.telus.api.config.ConfigurationManager.getInstance();
			com.telus.api.config.Configuration c = configurationManager.lookup(path);
			return new com.telus.eas.config1.info.ConfigurationInfo(path, c.getProperties());
		} catch (com.telus.api.config.UnknownConfigurationException e) {
			throw new ApplicationException(SystemCodes.CMB_CFGMGR_EJB, "Unknown objects ["+ arrayToString(path, ",")+"]", "", e);
		} catch (Throwable e) {
			throw new ApplicationException(SystemCodes.CMB_CFGMGR_EJB, arrayToString(path, ","), "", e);
		} finally {
			if (configurationManager != null) {
				configurationManager.destroy();
			}
		}
	}

	@Override
	public com.telus.eas.config1.info.ConfigurationInfo getConfiguration1(String path) throws ApplicationException {
		com.telus.api.config.ConfigurationManager configurationManager = null;
		try {
			configurationManager = com.telus.api.config.ConfigurationManager.getInstance();
			com.telus.api.config.Configuration c = configurationManager.lookup(path);
			return new com.telus.eas.config1.info.ConfigurationInfo(new String[] { path }, c.getProperties());
		} catch (com.telus.api.config.UnknownConfigurationException e) {
			throw new ApplicationException(SystemCodes.CMB_CFGMGR_EJB, "Unknown object ["+ path +"]", "", e);
		} catch (Throwable e) {
			throw new ApplicationException(SystemCodes.CMB_CFGMGR_EJB, path, "", e);
		} finally {
			if (configurationManager != null) {
				configurationManager.destroy();
			}
		}
	}

	@Override
	public EquipmentChangeInfo getEquipmentChangeByESN(String oldESN,
			String newESN) throws ApplicationException {
		return configurationManagerDao.getEquipmentChangeDetailsByESNs(oldESN, newESN);
	}

	@Override
	public InteractionDetail[] getInteractionDetails(long id)
			throws ApplicationException {
		return configurationManagerDao.getInteractionDetails(id);
	}

	@Override
	public Interaction[] getInteractionsByBan(int ban, Date from, Date to,
			String type) throws ApplicationException {
		return configurationManagerDao.getInteractionsByBan(ban, from, to, type);
	}

	@Override
	public Interaction[] getInteractionsBySubscriber(String subscriberId,
			Date from, Date to, String type) throws ApplicationException {
		return configurationManagerDao.getInteractionsBySubscriber(subscriberId, from, to, type);
	}

	@Override
	public ConfigurationInfo loadProperties(ConfigurationInfo info)
			throws ApplicationException {
		configurationManagerDao.loadProperties(info);
		return info;
	}

	@Override
	public void logActivation(ActivationLogInfo pActivationLogInfo,
			long pActLogID) throws ApplicationException {
		configurationManagerDao.logActivation(pActivationLogInfo, pActLogID);		
	}

	@Override
	public void logActivationSummary(ActivationLogInfo pActivationLogInfo)
			throws ApplicationException {
		configurationManagerDao.logActivationSummary(pActivationLogInfo);
	}

	@Override
	public void logApplication(LogInfo plogInfo) throws ApplicationException {
		configurationManagerDao.logApplication(plogInfo);
	}

	@Override
	public ConfigurationInfo newConfiguration(int parentConfigurationId,
			String name) throws ApplicationException {
		return configurationManagerDao.newConfiguration(parentConfigurationId, name);
	}

	@Override
	public InteractionInfo newReport(String transactionType,
			String applicationId, int operatorId, String dealerCode,
			String salesRepCode, int banId, String subscriberId, long reasonId)
			throws ApplicationException {
		return configurationManagerDao.newReport(transactionType, applicationId, operatorId, dealerCode, salesRepCode, banId, subscriberId, reasonId);
	}

	@Override
	public int removeConfiguration(int configurationId)
			throws ApplicationException {
		return configurationManagerDao.removeConfiguration(configurationId);
	}

	@Override
	public int removeProperties(int configurationId, boolean recursively)
			throws ApplicationException {
		if (recursively) {
			return configurationManagerDao.removePropertiesRecursively(configurationId);
		} else {
			return configurationManagerDao.removeProperties(configurationId);
		}
	}

	@Override
	public int removeProperties(int configurationId, String[] name)
			throws ApplicationException {
		return configurationManagerDao.removeProperties(configurationId, name);
	}

	@Override
	public void report_accountStatusChange(long transactionId,
			Date transactionDate, char oldHotlinedInd, char newHotlinedInd,
			char oldStatus, char newStatus, char statusFlag)
			throws ApplicationException {
		configurationManagerDao.report_accountStatusChange(transactionId, transactionDate, oldHotlinedInd, newHotlinedInd, oldStatus, newStatus, statusFlag);
		
	}

	@Override
	public void report_changeAddress(long transactionId, Date transactionDate,
			AddressInfo oldAddr, AddressInfo newAddr)
			throws ApplicationException {
		configurationManagerDao.report_changeAddress(transactionId, transactionDate, oldAddr, newAddr);
		
	}

	@Override
	public void report_changePaymentMethod(long transactionId,
			Date transactionDate, char oldPaymentMethod, char newPaymentMethod)
			throws ApplicationException {
		configurationManagerDao.report_changePaymentMethod(transactionId, transactionDate, oldPaymentMethod, newPaymentMethod);
		
	}

	@Override
	public void report_changePhoneNumber(long transactionId,
			Date transactionDate, String oldPhoneNumber, String newPhoneNumber)
			throws ApplicationException {
		configurationManagerDao.report_changePhoneNumber(transactionId, transactionDate, oldPhoneNumber, newPhoneNumber);
		
	}

	@Override
	public void report_changePricePlan(long transactionId,
			Date transactionDate, String oldPlan, String newPlan,
			ServiceAgreementInfo[] services) throws ApplicationException {
		configurationManagerDao.report_changePricePlan(transactionId, transactionDate, oldPlan, newPlan, services);
		
	}

	@Override
	public void report_changeRole(long transactionId, Date transactionDate,
			String oldRole, String newRole) throws ApplicationException {
		configurationManagerDao.report_changeRole(transactionId, transactionDate, oldRole, newRole);
		
	}

	@Override
	public void report_changeService(long transactionId, Date transactionDate,
			ServiceAgreementInfo[] services) throws ApplicationException {
		configurationManagerDao.report_changeService(transactionId, transactionDate, services);		
	}

	@Override
	public void report_changeSubscriber(long transactionId,
			Date transactionDate, SubscriberInfo oldSubscriber,
			SubscriberInfo newSubscriber) throws ApplicationException {
		configurationManagerDao.report_changeSubscriber(transactionId, transactionDate, oldSubscriber, newSubscriber);
		
	}

	@Override
	public void report_makePayment(long transactionId, Date transactionDate,
			char paymentMethod, double paymentAmount)
			throws ApplicationException {
		configurationManagerDao.report_makePayment(transactionId, transactionDate, paymentMethod, paymentAmount);
		
	}

	@Override
	public void report_prepaidAccountTopUp(long transactionId,
			Date transactionDate, Double amount, char cardType, char topUpType)
			throws ApplicationException {
		configurationManagerDao.report_prepaidAccountTopUp(transactionId, transactionDate, amount, cardType, topUpType);
		
	}

	@Override
	public void report_subscriberChangeEquipment(long transactionId,
			Date transactionDate, SubscriberInfo subscriberInfo,
			EquipmentInfo oldEquipmentInfo, EquipmentInfo newEquipmentInfo,
			String dealerCode, String salesRepCode, String requestorId,
			String repairId, String swapType,
			EquipmentInfo associatedMuleEquipmentInfo, String applicationName)
			throws ApplicationException {
		configurationManagerDao.report_subscriberChangeEquipment(transactionId, transactionDate, subscriberInfo, oldEquipmentInfo, newEquipmentInfo, dealerCode, salesRepCode, requestorId, repairId, swapType, associatedMuleEquipmentInfo, applicationName);
		
	}

	@Override
	public void report_subscriberNewCharge(long transactionId,
			Date transactionDate, String chargeCode, String waiverCode)
			throws ApplicationException {
		configurationManagerDao.report_subscriberNewCharge(transactionId, transactionDate, chargeCode, waiverCode);		
	}

		
	private static String arrayToString(Object[] array, String separator) {
		String result = "";
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				if (i > 0) {
					result += separator;
				}
				result += array[i];
			}
		}
		return result;
	}

	@Override
	public TestPointResultInfo testEasDataSource() {
		return testPointDao.testEasDataSource();
	}

	@Override
	public TestPointResultInfo testServDataSource() {
		return testPointDao.testServDataSource();
	}

	@Override
	public TestPointResultInfo getLogUtilityPkgVersion() {
		return testPointDao.getLogUtilityPkgVersion();
	}

	@Override
	public String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}
}
