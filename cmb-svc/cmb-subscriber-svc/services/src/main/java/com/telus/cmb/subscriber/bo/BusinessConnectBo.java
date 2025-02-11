package com.telus.cmb.subscriber.bo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.common.dao.provisioning.WirelessProvisioningServiceRequestFactory;
import com.telus.cmb.subscriber.utilities.BusinessConnectContext;
import com.telus.cmb.subscriber.utilities.BusinessConnectUtil;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.utility.info.ProvisioningLicenseInfo;

public class BusinessConnectBo {

	private static final Log logger = LogFactory.getLog(BusinessConnectBo.class);

	private BusinessConnectContext context;

	public BusinessConnectBo(BusinessConnectContext context) throws ApplicationException {
		this.context = context;
	}

	public void provision() throws ApplicationException {

		if (context.isValid()) {
			if (context.isActivation()) {
				activateVOIPService();
			}
			if (context.isPricePlanChange()) {
				changeVOIPService();
			}
			addLicenses();
		}
	}

	public void deprovision() throws ApplicationException {

		if (context.isValid()) {
			removeLicenses();
		}
	}

	private void activateVOIPService() throws ApplicationException {

		// Refresh to load the activated subscriber into the context
		context.refresh();
		AddressInfo addressInfo = (AddressInfo) context.getSubscriber().getAddress();
		if (context.isPrimaryStarterSeatActivation()) {
			// If this is the primary (first) starter seat on the account, send the request to Provisioning with the ADDACT action type
			String serviceEdition = BusinessConnectUtil.getVOIPServiceEditionFromPricePlan(context.getSubscriberContract(), context.getServiceEditions());
			context.getSubscriberLifecycleFacade().asyncSubmitProvisioningOrder(WirelessProvisioningServiceRequestFactory.createAccountAddRequest(context.getAccount(), context.getSubscriber(),
					context.getSubscriber().getStartServiceDate(), addressInfo, serviceEdition));
		} else {
			// Otherwise send the request to Provisioning with the ADD action type
			context.getSubscriberLifecycleFacade().asyncSubmitProvisioningOrder(WirelessProvisioningServiceRequestFactory.createSeatAddRequest(context.getAccount(), context.getSubscriber(), 
					context.getSubscriber().getStartServiceDate(), addressInfo));
		}
	}

	private void changeVOIPService() throws ApplicationException {

		// Check if this is at least a Business Connect starter seat before making any remote calls
		if (BusinessConnectUtil.isBusinessConnectStarterSeat(context.getSubscriber())) {
			try {
				if (BusinessConnectUtil.isBusinessConnectPrimaryStarterSeat(context.getSubscriber(), context.getVOIPAccountInfo())) {
					String newServiceEditionCode = BusinessConnectUtil.getVOIPServiceEditionFromPricePlan(context.getSubscriberContract(), context.getServiceEditions());
					BusinessConnectUtil.validateServiceEditionChange(context.getVOIPAccountInfo().getServiceEditionCode(), newServiceEditionCode);
					Date activityDate = BusinessConnectUtil.getActivityDateFromPricePlan(context.getSubscriberContract(), context.getLogicalDate());
					context.getSubscriberLifecycleFacade().asyncSubmitProvisioningOrder(WirelessProvisioningServiceRequestFactory.createAccountChangeRequest(context.getAccount(), activityDate, newServiceEditionCode));
				}
			} catch (ApplicationException ae) {
				if (StringUtils.equalsIgnoreCase(ErrorCodes.GENERIC_THROWABLE_ERROR_CODE, ae.getSystemCode())) {
					logger.error("Cannot find switch code for subscriber [" + context.getSubscriber().getPhoneNumber() + "] in price plan [" + context.getSubscriberContract().getPricePlanCode() + "].");
				} else if (StringUtils.equalsIgnoreCase(ErrorCodes.NO_SERVICE_EDITION_CHANGE, ae.getSystemCode())) {
					logger.info("No service edition change for subscriber [" + context.getSubscriber().getPhoneNumber() + "].");
				} else {
					throw ae;
				}
			}
		}
	}

	private ServiceAgreementInfo[] setServices(ServiceAgreementInfo[] contractServices) throws ApplicationException {

		for (ServiceAgreementInfo info : contractServices) {
			if (info.getService0() == null) {
				try {
					info.setService(context.getReferenceDataFacade().getRegularService(info.getCode()));
				} catch (TelusException te) {
					throw new SystemException(SystemCodes.CMB_SLF_EJB, te.getMessage(), StringUtils.EMPTY, te);
				}
			}
		}

		return contractServices;
	}

	private void addLicenses() throws ApplicationException {

		// Retrieve the service license switch codes to add
		List<String> switchCodes = BusinessConnectUtil.getLicenseSwitchCodesFromAddOnServices(setServices((ServiceAgreementInfo[]) context.getSubscriberContract().getAddedServices()),
				context.getLicenses());

		// Retrieve the price plan license switch code. //Not sure this can be supported ??
		String pricePlanSwitchCode = BusinessConnectUtil.getLicenseSwitchCodeFromPricePlan(context.getSubscriberContract().getPricePlan0(), context.getLicenses());
		if (pricePlanSwitchCode != null) {
			switchCodes.add(pricePlanSwitchCode);
		}

		if (!switchCodes.isEmpty()) {
			ProvisioningLicenseInfo provisioningLicenseInfo = new ProvisioningLicenseInfo();
			provisioningLicenseInfo.setBan(context.getAccount().getBanId());
			provisioningLicenseInfo.setSubscriptionId(String.valueOf(context.getSubscriber().getSubscriptionId()));
			provisioningLicenseInfo.setSwitchCodes(switchCodes);
			provisioningLicenseInfo.setTransactionType(ProvisioningLicenseInfo.PROV_LICENSE_TRANSACTION_TYPE_ADD);
			context.getSubscriberLifecycleFacade().asyncSubmitProvisioningLicenseOrder(provisioningLicenseInfo);
		} else {
			logger.debug("Add licenses list is empty for ban [" + context.getSubscriber().getBanId() + "] and subscriber [" + context.getSubscriber().getPhoneNumber() + "].");
		}
	}

	private void removeLicenses() throws ApplicationException {

		// Retrieve the service license switch codes to remove
		List<String> switchCodes = BusinessConnectUtil.getLicenseSwitchCodesFromAddOnServices(setServices((ServiceAgreementInfo[]) context.getSubscriberContract().getDeletedServices()),
				context.getLicenses());
		if (!switchCodes.isEmpty()) {
			context.getSubscriberLifecycleFacade().removeLicenses(context.getAccount().getBanId(), String.valueOf(context.getSubscriber().getSubscriptionId()), switchCodes);
		} else {
			logger.debug("Remove licenses list is empty for ban [" + context.getSubscriber().getBanId() + "] and subscriber [" + context.getSubscriber().getPhoneNumber() + "].");
		}
	}

}