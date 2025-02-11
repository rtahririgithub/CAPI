package com.telus.cmb.account.lifecyclemanager.dao.impl;

import java.util.ArrayList;
import java.util.List;
import amdocs.APILink.datatypes.CrdBalTransReqInfo;
import amdocs.APILink.datatypes.CrdBalTransferInfo;
import amdocs.APILink.datatypes.CrdBalTransferList;
import amdocs.APILink.sessions.interfaces.GenericServices;
import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.dao.CreditBalanceTransferDao;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.eas.account.info.CreditBalanceTransferInfo;


public class CreditBalanceTransferDaoImpl extends AmdocsDaoSupport implements
CreditBalanceTransferDao {

	@Override
	public List<CreditBalanceTransferInfo> getCreditBalanceTransferRequestList(final int ban, String sessionId) throws ApplicationException {
		CrdBalTransferList crdBalTransferList = getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<CrdBalTransferList>() {
			@Override
			public CrdBalTransferList doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				GenericServices genericServices = transactionContext.createBean(GenericServices.class);
				return genericServices.getCrdBalTransRequestList(ban);
			} 
		});
		List<CreditBalanceTransferInfo> creditBalanceTransferInfoList = new ArrayList<CreditBalanceTransferInfo>();
		if (crdBalTransferList != null) {
			CrdBalTransferInfo[] crBalTransInfo = crdBalTransferList.crdBalTransferInfo;
			if (crBalTransInfo != null ) {
				for (int i = 0; i < crBalTransInfo.length; i++) {
					CreditBalanceTransferInfo creditBalanceTransferInfo =  new CreditBalanceTransferInfo();
					CrdBalTransferInfo crdBalTransferInfo = crBalTransInfo[i];
					creditBalanceTransferInfo.setAdjSeqNo(crdBalTransferInfo.adjSeqNo);
					creditBalanceTransferInfo.setBillCycle(crdBalTransferInfo.billCycle);
					creditBalanceTransferInfo.setCreditBalanceTransferSeqNum(crdBalTransferInfo.cbtSeqNum);
					creditBalanceTransferInfo.setChargeSeqNo(crdBalTransferInfo.chgSeqNo);
					creditBalanceTransferInfo.setReqCreateDt(crdBalTransferInfo.reqCreateDt);
					creditBalanceTransferInfo.setSourceBan(crdBalTransferInfo.sourceBan);
					creditBalanceTransferInfo.setStatusUpdateDt(crdBalTransferInfo.statusUpdateDt);
					creditBalanceTransferInfo.setTargetBan(crdBalTransferInfo.targetBan);
					creditBalanceTransferInfo.setTransferAmt(crdBalTransferInfo.transferAmt);
					creditBalanceTransferInfo.setTransferStatus((char) crdBalTransferInfo.transferSts);
					creditBalanceTransferInfo.setSystemCreationDate(crdBalTransferInfo.sysCreationDt);
					creditBalanceTransferInfo.setSystemUpdateDate(crdBalTransferInfo.sysUpdateDt);
					creditBalanceTransferInfo.setOperatorId(crdBalTransferInfo.operatorId);
					creditBalanceTransferInfo.setApplicationId(crdBalTransferInfo.applicationId);
					creditBalanceTransferInfo.setUpdateStamp(crdBalTransferInfo.upStamp);
					creditBalanceTransferInfo.setFailureCd(crdBalTransferInfo.failureCd);
					creditBalanceTransferInfoList.add(creditBalanceTransferInfo);
				}
			}
		}
		return creditBalanceTransferInfoList;
	}


	@Override
	public void createCreditBalanceTransferRequest(final int sourceBan, final int targetBan ,String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {		
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				GenericServices genericServices = transactionContext.createBean(GenericServices.class);
				CrdBalTransReqInfo crdBalTransReqInfo = new CrdBalTransReqInfo();
				crdBalTransReqInfo.sourceBanNumber = sourceBan;
				crdBalTransReqInfo.targetBanNumber = targetBan;
				genericServices.createCrdBalTransRequest(crdBalTransReqInfo);
				return null;
			}
		});	
	}


	@Override
	public void cancelCreditBalanceTransferRequest(final int creditTransferSequenceNumber, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				GenericServices genericServices = transactionContext.createBean(GenericServices.class);
				genericServices.cancelCrdBalTransRequest(creditTransferSequenceNumber);
				return null;
			}
		});	
	}


}
