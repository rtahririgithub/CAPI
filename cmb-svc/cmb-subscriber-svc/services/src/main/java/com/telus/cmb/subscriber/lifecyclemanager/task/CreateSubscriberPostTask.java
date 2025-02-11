package com.telus.cmb.subscriber.lifecyclemanager.task;

import org.apache.log4j.Logger;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.BaseChangeInfo;
import com.telus.eas.utility.info.DiscountPlanInfo;

public class CreateSubscriberPostTask extends SubscriberTask {
	private final static Logger LOGGER = Logger.getLogger(CreateSubscriberPostTask.class);
	private boolean memosCreationCompleted = false;
	private boolean feesApplicationCompleted = false;
	
	public CreateSubscriberPostTask(BaseChangeInfo changeInfo) throws SystemException, ApplicationException {
		super(changeInfo);
	}

	@Override
	public void execute() throws ApplicationException {
		createHandsetActivationMemo();
		createContractTermAcceptanceMemo();
		memosCreationCompleted = true;

		applyPatternFeeCharge();
		applyActivationFeeCharge();
		feesApplicationCompleted = true;
		
		applyPromotionalDiscount();
	}

	@Override
	protected EquipmentInfo getEquipmentInfo() throws ApplicationException {
		try {
			return super.getEquipmentInfo();
		}catch (Throwable t) {
			LOGGER.warn("An Exception happend when retrieving Equipment Info - " + t.getMessage());
			throw new ApplicationException(SystemCodes.CMB_SLM_EJB, ErrorCodes.CREATE_SUBSCRIBER_ERROR_MEMOS_FEES_DISCOUNTS,
					"SubscriberManager createSubscriber() created the subscriber but did not complete the following steps: " + determineStepsNotExecuted(), "", t);
		}
	}

	@Override
	protected DiscountPlanInfo[] getPromotionalDiscounts() throws ApplicationException {
		try {
			return super.getPromotionalDiscounts();
		}catch(Throwable t) {
			LOGGER.warn("An Exception happend when retrieving Discount Plans - " + t.getMessage());
			throw new ApplicationException(SystemCodes.CMB_SLM_EJB, ErrorCodes.CREATE_SUBSCRIBER_ERROR_MEMOS_FEES_DISCOUNTS
					, "SubscriberManager createSubscriber() created the subscriber but did not complete the following steps: " +
					determineStepsNotExecuted()
					, "", t);
		}
	}

	@Override
	public void createHandsetActivationMemo() throws ApplicationException {
		try {
			super.createHandsetActivationMemo();
		} catch(Throwable t) {
			LOGGER.warn("An Exception happend when async reating handset activation Memo - " + t.getMessage());
			throw new ApplicationException(SystemCodes.CMB_SLM_EJB, ErrorCodes.CREATE_SUBSCRIBER_ERROR_MEMOS_FEES_DISCOUNTS
					, "SubscriberManager createSubscriber() created the subscriber but did not complete the following steps: " +
					determineStepsNotExecuted()
					, "", t);
		}
	}

	@Override
	public void createContractTermAcceptanceMemo() throws ApplicationException {
		try {
			super.createContractTermAcceptanceMemo();
		} catch(Throwable t) {
			LOGGER.warn("An Exception happend when async creating contract acceptance Memo - " + t.getMessage());
			throw new ApplicationException(SystemCodes.CMB_SLM_EJB, ErrorCodes.CREATE_SUBSCRIBER_ERROR_MEMOS_FEES_DISCOUNTS
					, "SubscriberManager createSubscriber() created the subscriber but did not complete the following steps: " +
					determineStepsNotExecuted()
					, "", t);
		}
	}

	@Override
	public void applyPatternFeeCharge() throws ApplicationException {
		try {
			super.applyPatternFeeCharge();
		} catch(Throwable t) {
			LOGGER.warn("An Exception happend when applying pattern fee charge - " + t.getMessage());
			throw new ApplicationException(SystemCodes.CMB_SLM_EJB, ErrorCodes.CREATE_SUBSCRIBER_ERROR_FEES_DISCOUNTS
					, "SubscriberManager createSubscriber() created the subscriber but did not complete the following steps: " +
					determineStepsNotExecuted()
					, "", t);
		}
	}

	@Override
	public void applyActivationFeeCharge() throws ApplicationException {
		try {
			super.applyActivationFeeCharge();
		} catch(Throwable t) {
			LOGGER.warn("An Exception happend when applying activation fee charge - " + t.getMessage());
			throw new ApplicationException(SystemCodes.CMB_SLM_EJB, ErrorCodes.CREATE_SUBSCRIBER_ERROR_FEES_DISCOUNTS
					, "SubscriberManager createSubscriber() created the subscriber but did not complete the following steps: " +
					determineStepsNotExecuted()
					, "", t);
		}
	}

	@Override
	public void applyPromotionalDiscount() throws ApplicationException {
		try {
			super.applyPromotionalDiscount();
		}catch(Throwable t) {
			LOGGER.warn("An Exception happend when applying activation discount - " + t.getMessage());
			throw new ApplicationException(SystemCodes.CMB_SLM_EJB, ErrorCodes.CREATE_SUBSCRIBER_ERROR_DISCOUNTS
					, "SubscriberManager createSubscriber() created the subscriber but did not complete the following steps: " +
					determineStepsNotExecuted()
					, "", t);
		}
	}

	private String determineStepsNotExecuted() {
		StringBuilder sb = new StringBuilder();
		if (memosCreationCompleted == false) {
			sb.append("Memo(s) creation");
		}
		
		if (feesApplicationCompleted == false) {
			if (sb.length() > 0 ) {
				sb.append(", ");
			}
			sb.append("Fee(s) application");
		}
		
		if (sb.length() > 0 ) {
			sb.append(", ");
		}
		sb.append("Discount(s) application");

		
		return sb.toString();
	}
	
	
}
