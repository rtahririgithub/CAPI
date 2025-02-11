package com.telus.cmb.account.lifecyclemanager.dao.impl;

import java.util.Date;

import amdocs.APILink.datatypes.ActivityInfo;
import amdocs.APILink.datatypes.CancelRequestInfo;
import amdocs.APILink.datatypes.MultipleActivitiesRequestInfo;
import amdocs.APILink.datatypes.SubscriberWaiveInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.dao.SubscriberDao;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.mapping.ChargeAndAdjustmentMapper;
import com.telus.cmb.common.util.AttributeTranslator;

public class SubscriberDaoImpl extends AmdocsDaoSupport implements SubscriberDao {

	@Override
	public void cancelSubscribers(final int ban, final Date activityDate,
			final String activityReasonCode,final char depositReturnMethod,
			final String[] subscriberId,final String[] waiveReason,final String userMemoText,
			String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
					throws Exception {
				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
				
				// set BanPK (which also retrieves the BAN)
			    amdocsUpdateBanConv.setBanPK(ban);

				// populate amdocs CancelRequestInfo from parameters
				CancelRequestInfo amdocsCancelRequestInfo = new CancelRequestInfo();
				amdocsCancelRequestInfo.activityDate = activityDate;
				amdocsCancelRequestInfo.activityReason = activityReasonCode;
				if (userMemoText != null && userMemoText.trim().isEmpty() == false) {
					amdocsCancelRequestInfo.userText = userMemoText;
				}
				amdocsCancelRequestInfo.depositReturnMethod = (byte)depositReturnMethod;
				amdocsCancelRequestInfo.subscriberWaiveInfo = new SubscriberWaiveInfo[subscriberId.length];
				for (int i=0;i<amdocsCancelRequestInfo.subscriberWaiveInfo.length;i++) {
					amdocsCancelRequestInfo.subscriberWaiveInfo[i] = new SubscriberWaiveInfo();
					amdocsCancelRequestInfo.subscriberWaiveInfo[i].subscriberNumber = AttributeTranslator.emptyFromNull(subscriberId[i]);
					amdocsCancelRequestInfo.subscriberWaiveInfo[i].waiveReason = AttributeTranslator.emptyFromNull(waiveReason[i]);
				}

				amdocsUpdateBanConv.cancelSubscribers(amdocsCancelRequestInfo);
				
				return null;
			}
			
		});
		
	}
	
	@Override
	public void suspendSubscribers(final int ban, final Date activityDate,
			final String activityReasonCode, final String[] subscriberId,
			final String userMemoText, String sessionId) throws ApplicationException {

		// populate amdocs CancelRequestInfo from parameters
		final MultipleActivitiesRequestInfo amdocsInfo = new MultipleActivitiesRequestInfo();
		
		amdocsInfo.activityDate = activityDate;
		amdocsInfo.activityReason = activityReasonCode;
		amdocsInfo.subscriberNumber = subscriberId;
		amdocsInfo.userText = userMemoText;
		
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
						UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
						try {
							// set BanPK (which also retrieves the BAN)
							amdocsUpdateBanConv.setBanPK(ban);
							amdocsUpdateBanConv.suspendSubscribers(amdocsInfo);
						} catch (ValidateException vex) {
							/**
							 * if we pass last active subscriber to suspend ,kb will revert us with error code - 1111730, Cannot suspend the last Active Subscriber. Please suspend the BAN." error msg.
							 * we were doing check to predict last activate subscriber before make this call . As discussed with Chung/Michael ,they are agreed to put below logic incase our prediction fails when it reaches actual transaction.
							 * 
							 */
							if (vex.getErrorInd() == 1111730) {
								suspendBan(amdocsUpdateBanConv, activityDate, activityReasonCode, userMemoText);
							}

			  }
				return null;
			}
		});
		
	}
	
	private void suspendBan(UpdateBanConv amdocsUpdateBanConv, Date activityDate, String activityReasonCode, String userMemoText ) throws Exception  {
		ActivityInfo amdocsActivityInfo = new ActivityInfo();
		amdocsActivityInfo.activityDate = activityDate == null ? new Date(): activityDate;
		amdocsActivityInfo.activityReason = activityReasonCode;
		amdocsActivityInfo.userText = userMemoText == null ? new String(""): userMemoText;
		amdocsUpdateBanConv.suspendBan(amdocsActivityInfo);
	}

	
	@Override
	public void restoreSuspendedSubscribers(final int ban, final Date restoreDate, final String restoreReasonCode,
   		 final String[] subscriberId, final String restoreComment, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
					throws Exception {
				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
				// set BanPK (which also retrieves the BAN)
			    amdocsUpdateBanConv.setBanPK(ban);

				// populate amdocs CancelRequestInfo from parameters
			    MultipleActivitiesRequestInfo amdocsInfo = new MultipleActivitiesRequestInfo();
			    amdocsInfo.activityDate = restoreDate;
			    amdocsInfo.activityReason = restoreReasonCode;
			    amdocsInfo.subscriberNumber = subscriberId;
			    amdocsInfo.userText = restoreComment;

				amdocsUpdateBanConv.restoreSuspendedSubscribers(amdocsInfo);
				
			return null;	
			}
		});
		
	}
}
