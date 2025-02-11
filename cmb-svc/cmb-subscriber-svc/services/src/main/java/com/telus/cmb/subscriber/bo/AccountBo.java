package com.telus.cmb.subscriber.bo;

import java.util.HashMap;
import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.PhoneNumberSearchOption;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.cmb.subscriber.decorators.AccountDecorator;
import com.telus.cmb.subscriber.utilities.BaseChangeContext;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.PricePlanSubscriberCountInfo;
import com.telus.eas.subscriber.info.BaseChangeInfo;

public class AccountBo extends AccountDecorator {
	protected BaseChangeContext<? extends BaseChangeInfo> changeContext;
	private HashMap<String, HashMap<String, PricePlanSubscriberCountInfo>> pricePlanSubscriberCountHashMapCache = new HashMap<String, HashMap<String, PricePlanSubscriberCountInfo>>();
	private static final String SHAREABLE_KEY = "Shareable";

	public AccountBo(AccountInfo account, BaseChangeContext<? extends BaseChangeInfo> changeContext) {
		super(account);
		this.changeContext = changeContext;
	}

	public PricePlanSubscriberCount[] getShareablePricePlanSubscriberCount() throws ApplicationException {
		PricePlanSubscriberCount[] shareableCountResult = null;
		shareableCountResult = getShareablePricePlanSubscriberCount(false);
		return shareableCountResult;
	}

	public PricePlanSubscriberCount[] getShareablePricePlanSubscriberCount(boolean refresh) throws ApplicationException  {
		PricePlanSubscriberCountInfo[] shareableCountResult = null;

		if (refresh) {
				List<PricePlanSubscriberCountInfo> list = changeContext.getAccountInformationHelper().retrieveShareablePricePlanSubscriberCount(getBanId());
				shareableCountResult = list.toArray(new PricePlanSubscriberCountInfo[list.size()]);
				if (shareableCountResult != null) {
					HashMap<String, PricePlanSubscriberCountInfo> pricePlanSubscriberCountCache = pricePlanSubscriberCountHashMapCache.get(SHAREABLE_KEY);
					if (pricePlanSubscriberCountCache != null) {
						pricePlanSubscriberCountCache.clear();
					} else {
						pricePlanSubscriberCountCache = new HashMap<String, PricePlanSubscriberCountInfo>();
					}

					for (int i = 0; i < shareableCountResult.length; i++) {
						String key = shareableCountResult[i].getPricePlanCode();
						pricePlanSubscriberCountCache.put(key, shareableCountResult[i]);
					}

					pricePlanSubscriberCountHashMapCache.put(SHAREABLE_KEY, pricePlanSubscriberCountCache);
				}
			return shareableCountResult;
		} else {
			HashMap<String, PricePlanSubscriberCountInfo> pricePlanSubscriberCountCache = pricePlanSubscriberCountHashMapCache.get(SHAREABLE_KEY);
			if (pricePlanSubscriberCountCache != null) {
				return pricePlanSubscriberCountCache.values().toArray(new PricePlanSubscriberCount[pricePlanSubscriberCountCache.size()]);
			} else {
				// PricePlanCountCache has yet to be created. Therefore, we need to retrieve from database (using a recursive call)
				return getShareablePricePlanSubscriberCount(true);
			}
		}
	}

	public PricePlanSubscriberCountInfo getShareablePricePlanSubscriberCount(String pricePlanCode) throws ApplicationException  {

		HashMap<String, PricePlanSubscriberCountInfo> shareablePricePlanSubscriberCountCache = pricePlanSubscriberCountHashMapCache.get(SHAREABLE_KEY);

		if (shareablePricePlanSubscriberCountCache != null) {
			return shareablePricePlanSubscriberCountCache.get(pricePlanCode);
		} else {
			getShareablePricePlanSubscriberCount(); // call required to initialize ShareablePricePlanSubriberCountCache
			shareablePricePlanSubscriberCountCache = pricePlanSubscriberCountHashMapCache.get(SHAREABLE_KEY);

			if (shareablePricePlanSubscriberCountCache != null) {
				return shareablePricePlanSubscriberCountCache.get(pricePlanCode);
			}
		}
		return null;
	}

//	public void createManualLetterRequest(Letter letter) throws ApplicationException {
//		createManualLetterRequest(letter, null);
//	}
//	
//	public void createManualLetterRequest(Letter letter, Subscriber subscriber) throws ApplicationException {
//		LMSLetterRequestInfo letterRequestInfo = new LMSLetterRequestInfo();
//		letterRequestInfo.setLetterCode(letter.getCode());
//		letterRequestInfo.setLetterCategory(letter.getLetterCategory());
//		letterRequestInfo.setLetterVersion(letter.getLetterVersion());
//		letterRequestInfo.setOperatorId(changeContext.getClientIdentity().getPrincipal());
//		letterRequestInfo.setProductionDate(new Date());
//		letterRequestInfo.setSubmitDate(new Date());
//		letterRequestInfo.setBanId(getBanId());
//	    if (subscriber != null) {
//	    	letterRequestInfo.setSubscriberId(subscriber.getSubscriberId());
//	    }
//		
//		changeContext.getAccountLifecycleManager().createManualLetterRequest(letterRequestInfo, changeContext.getAccountLifecycleManagerSessionId());
//	}

	
	public Subscriber getSubscriberByPhoneNumber(String phoneNumber,
			PhoneNumberSearchOption phoneNumberSearchOption)
			throws UnknownSubscriberException, TelusAPIException {
		throw new UnsupportedOperationException("Method is not implemented here");
	}
}
