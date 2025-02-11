package com.telus.cmb.account.lifecyclemanager.dao.impl;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.UserEnv;
import amdocs.APILink.datatypes.UserNewPasswordInfo;
import amdocs.APILink.sessions.interfaces.GenericServices;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.dao.UserDao;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.logging.Sensitive;
import com.telus.eas.account.info.FleetInfo;

public class UserDaoImpl extends AmdocsDaoSupport implements UserDao {
	private static final Logger LOGGER = Logger.getLogger(UserDaoImpl.class);
	
	@Override
	public void changeKnowbilityPassword(final String userId
			,@Sensitive final String oldPassword
			,@Sensitive final String newPassword, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				GenericServices amdocsGenericServices = transactionContext.createBean(GenericServices.class);

				UserNewPasswordInfo userNewPasswordInfo = new UserNewPasswordInfo();
				userNewPasswordInfo.userId = Integer.parseInt(userId.trim());
				userNewPasswordInfo.currentPassword = oldPassword;
				userNewPasswordInfo.newPassword = newPassword;
				userNewPasswordInfo.validateCurrentPassword = true;

				amdocsGenericServices.changePassword(userNewPasswordInfo);	
				
				return null;
			}
		});

	}

	@Override
	public String getUserProfileID(String sessionId) throws ApplicationException {
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<String>() {

			@Override
			public String doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				String methodName = "getUserProfileID";

				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
					
				// get User Environment
			    UserEnv userEnv = amdocsUpdateBanConv.getUserEnv();
			    LOGGER.debug("("+getClass().getName()+"."+methodName+") Found ProfileID=[" + userEnv.profileId + "] for operatorId=[" + userEnv.operatorId + "]");
			    return userEnv.profileId;
			}
		});
	}

}
