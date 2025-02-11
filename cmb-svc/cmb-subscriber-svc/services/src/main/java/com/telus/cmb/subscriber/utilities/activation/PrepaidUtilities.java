package com.telus.cmb.subscriber.utilities.activation;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.InvalidServiceChangeException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.cmb.subscriber.bo.AccountBo;
import com.telus.cmb.subscriber.bo.ContractBo;
import com.telus.cmb.subscriber.bo.EquipmentBo;
import com.telus.cmb.subscriber.bo.SubscriberBo;
import com.telus.cmb.subscriber.utilities.BaseChangeContext;
import com.telus.eas.account.info.ActivationFeaturesPurchaseArrangementInfo;
import com.telus.eas.account.info.ActivationTopUpPaymentArrangementInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;

public class PrepaidUtilities {

	private static final Log logger = LogFactory.getLog(PrepaidUtilities.class);

	@SuppressWarnings("rawtypes")
	public static void updatePrepaidSystem(BaseChangeContext changeContext, ActivationTopUpPaymentArrangementInfo topUpPaymentInfo) throws ApplicationException, TelusAPIException {
		AccountBo account = changeContext.getCurrentAccount();
		SubscriberBo subscriber = changeContext.getCurrentSubscriber();
		ContractBo contract = changeContext.getCurrentContract();
		EquipmentBo equipment = changeContext.getCurrentEquipment();
		String identityPrincipal = changeContext.getClientIdentity().getPrincipal();
		if (!account.isPrepaidConsumer()) {
			logger.info("BAN [" + account.getBanId() + "] is not a prepaid account, skipping update to prepaid system.");
			return;
		}
		logger.info("Start updatePrepaidSystem for BAN " + account.getBanId());

		// Step 1: Update KB soc with prepaid calling circle parameters
		configurePrepaidCallingCircleService(account, contract);

		// Step 2: Save activation top up arrangement
		if (topUpPaymentInfo != null) {
			logger.debug("ActivationBO calling accountLifecycleManager.saveActivationTopUpArrangement");
			changeContext.getAccountLifecycleManager().saveActivationTopUpArrangement(String.valueOf(subscriber.getBanId()), subscriber.getPhoneNumber(), equipment.getSerialNumber(),
					topUpPaymentInfo, identityPrincipal);
			logger.debug("Finished saving save activation top up arrangement");
		}

		// Step 3: Save activation features purchase arrangement
		ActivationFeaturesPurchaseArrangementInfo[] activationFeaturesPurchaseArray = getActivationFPAInfoArray(contract.getServices());
		if (activationFeaturesPurchaseArray != null) {
			logger.debug("ActivationBO calling subscriberLifecycleManager.saveActivationFeaturesPurchaseArrangement");
			changeContext.getSubscriberLifecycleManager().saveActivationFeaturesPurchaseArrangement(subscriber.getDelegate(), activationFeaturesPurchaseArray, identityPrincipal);
			logger.debug("Finished saving save activation features purchase arrangement");
		}
	}

	private static void configurePrepaidCallingCircleService(AccountBo account, ContractBo contract) throws TelusAPIException, UnknownObjectException, InvalidServiceChangeException,
			ApplicationException {
		logger.debug("Updating prepaid calling circle service for BAN " + account.getBanId());
		ServiceAgreementInfo[] currentServices = contract.getDelegate().getServices0(true);
		ServiceAgreementInfo callingCircleService = CallingCircleUtilities.getPrepaidCallingCircleService(currentServices);
		if (callingCircleService != null && (callingCircleService.getTransaction() == ServiceFeatureInfo.ADD)) {
			String featureCode = Info.padTo(callingCircleService.getCode().trim(), ' ', 6);
			String prepaidCallingCircleParam = callingCircleService.getFeature(featureCode).getParameter();
			if (prepaidCallingCircleParam != null && !prepaidCallingCircleParam.equals("")) {
				String kbMappedPrepaidServiceCode = callingCircleService.getService().getWPSMappedKBSocCode();
				ServiceAgreementInfo kbMappedPrepaidService = contract.addService(kbMappedPrepaidServiceCode);
				ContractFeature kbCallingCircleFeature = CallingCircleUtilities.getCallingCircleFeature(kbMappedPrepaidService);
				if (kbCallingCircleFeature != null) {
					logger.debug("Updating features for calling circle soc " + kbMappedPrepaidServiceCode);
					kbCallingCircleFeature.setParameter(prepaidCallingCircleParam);
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static ActivationFeaturesPurchaseArrangementInfo[] getActivationFPAInfoArray(ContractService[] contractServices) {
		if (contractServices != null && contractServices.length > 0) {
			ArrayList afpaList = new ArrayList();
			for (int i = 0; i < contractServices.length; i++) {
				ServiceAgreementInfo serviceAgreement = (ServiceAgreementInfo) contractServices[i];
				if (serviceAgreement.isWPS()) {
					ActivationFeaturesPurchaseArrangementInfo afpa = new ActivationFeaturesPurchaseArrangementInfo();
					afpa.setAutoRenewIndicator(contractServices[i].getAutoRenew() + "");
					afpa.setAutoRenewFundSource(contractServices[i].getAutoRenewFundSource());
					afpa.setPurchaseFundSource(contractServices[i].getPurchaseFundSource());
					afpa.setFeatureId(contractServices[i].getCode());
					afpaList.add(afpa);
				}
			}
			return (afpaList.size() > 0) ? (ActivationFeaturesPurchaseArrangementInfo[]) afpaList.toArray(new ActivationFeaturesPurchaseArrangementInfo[afpaList.size()]) : null;
		} else {
			return null;
		}
	}
}
