package com.telus.cmb.subscriber.utilities;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.eas.subscriber.info.ActivationChangeInfo;
import com.telus.eas.subscriber.info.BaseChangeInfo;
import com.telus.eas.subscriber.info.ContractChangeInfo;
import com.telus.eas.subscriber.info.MigrateSeatChangeInfo;
import com.telus.eas.subscriber.info.MigrationChangeInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;

public class ChangeContextFactory {

	public static ContractChangeContext createChangeContext(ContractChangeInfo changeInfo) throws SystemException, ApplicationException {		
		return new ContractChangeContext(changeInfo);
	}
	
	public static MigrationChangeContext createChangeContext(MigrationChangeInfo changeInfo) throws SystemException, ApplicationException {		
		return new MigrationChangeContext(changeInfo);
	}
	
	public static ActivationChangeContext createChangeContext(ActivationChangeInfo changeInfo) throws SystemException, ApplicationException {
		return new ActivationChangeContext(changeInfo);
	}
	
	public static ActivatePortinContext createActivatePortinContext(ActivationChangeInfo changeInfo, PortRequestInfo portRequest, ServiceRequestHeaderInfo requestHeader, AuditInfo auditInfo)
			throws SystemException, ApplicationException {
		return new ActivatePortinContext(changeInfo, portRequest, requestHeader, auditInfo);
	}

	public static EquipmentChangeContext createEquipmentChangeContext(AccountInfo accountInfo, SubscriberInfo subscriberInfo) throws SystemException, ApplicationException {
		
		BaseChangeInfo changeInfo = new BaseChangeInfo();
		changeInfo.setBan(accountInfo.getBanId());
		changeInfo.setCurrentAccountInfo(accountInfo);
		changeInfo.setSubscriberId(subscriberInfo.getSubscriberId());
		changeInfo.setCurrentSubscriberInfo(subscriberInfo);
		
		return new EquipmentChangeContext(changeInfo);
	}
	
	public static AsyncSubscriberCommitContext createAsyncSubscriberCommitContext(int ban, String subscriberId) throws SystemException, ApplicationException {
		
		BaseChangeInfo baseInfo = new BaseChangeInfo();
		baseInfo.setBan(ban);
		baseInfo.setSubscriberId(subscriberId);
		
		return new AsyncSubscriberCommitContext(baseInfo);
	}
	
	public static MigrateSeatChangeContext createChangeContext(MigrateSeatChangeInfo changeInfo) throws SystemException, ApplicationException {		
		return new MigrateSeatChangeContext(changeInfo);
	}
	
}