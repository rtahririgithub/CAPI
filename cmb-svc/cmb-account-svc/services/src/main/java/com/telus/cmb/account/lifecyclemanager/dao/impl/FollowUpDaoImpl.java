package com.telus.cmb.account.lifecyclemanager.dao.impl;

import amdocs.APILink.datatypes.CloseFollowUpInfo;
import amdocs.APILink.datatypes.FollowUpKeyInfo;
import amdocs.APILink.datatypes.FollowUpList;
import amdocs.APILink.datatypes.ReassignFollowUpInfo;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;
import amdocs.APILink.sessions.interfaces.WorklistConv;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.dao.FollowUpDao;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.eas.account.info.FollowUpUpdateInfo;
import com.telus.eas.framework.info.FollowUpInfo;

public class FollowUpDaoImpl extends AmdocsDaoSupport implements FollowUpDao {

	@Override
	public void updateFollowUp(final FollowUpUpdateInfo followUpUpdateInfo, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);
				WorklistConv amdocsWorklistConv = transactionContext.createBean(WorklistConv.class);

				int ban = followUpUpdateInfo.getBan();
				int followUpId = followUpUpdateInfo.getFollowUpId();

				// add Follow Up text
				String[] additionalText = followUpUpdateInfo.getAdditionalText();

				int additionalTextSize = additionalText != null ? additionalText.length : 0;

				if (additionalTextSize > 0) {
					// Set BanPK (which also retrieves the BAN)
					amdocsUpdateBanConv.setBanPK(ban);

					for (int i = 0; i < additionalTextSize; i++) {
						amdocsUpdateBanConv.addFollowUpText(followUpId, additionalText[i]);
					}
				}

				// approve or deny Follow Up
				boolean toApprove = followUpUpdateInfo.getIsApproved();
				boolean toDeny = followUpUpdateInfo.getIsDenied();

				if (toApprove) {
					FollowUpKeyInfo followUpKeyInfo = new FollowUpKeyInfo();
					followUpKeyInfo.banNumber = ban;
					followUpKeyInfo.followUpId = followUpId;
					amdocsWorklistConv.approveFollowUp(followUpKeyInfo);
				} else if (toDeny) {
					//TODO when defect PROD00058189 is fixed. remove the enclosed lines and just
					//     call amdocsWorklistConv.denyFollowUp(followUpId); directly.  the for
					//     loop is a workaround to allow functionality.  This workaround takes
					//     a performance hit though.
					FollowUpList fulist = amdocsWorklistConv.getFollowUpWorkList();
					for (int i=0;i<fulist.followUpDetailsInfo.length;i++) {
						if (fulist.followUpDetailsInfo[i].fuId==followUpId) {
							amdocsWorklistConv.denyFollowUp(followUpId);
							break;
						}
					}
				}

				// close Follow Up
				String closeReasonCode = followUpUpdateInfo.getCloseReasonCode();

				if (closeReasonCode != null) {
					// Set BanPK (which also retrieves the BAN)
					amdocsUpdateBanConv.setBanPK(ban);

					CloseFollowUpInfo closeFollowUpInfo = new CloseFollowUpInfo();

					closeFollowUpInfo.followUpId = followUpId;
					closeFollowUpInfo.closeFollowUpRsnCode = closeReasonCode;

					amdocsUpdateBanConv.closeFollowUp(closeFollowUpInfo);
				}

				// reassign Follow Up
				String assignedToWorkPositionId = followUpUpdateInfo.getAssignedToWorkPositionId();

				if (assignedToWorkPositionId != null) {
					ReassignFollowUpInfo reassignFollowUpInfo = new ReassignFollowUpInfo();

					reassignFollowUpInfo.banNumber = new int[] {ban};
					reassignFollowUpInfo.followUpId = new int[] {followUpId};
					reassignFollowUpInfo.workPosition = assignedToWorkPositionId.trim();
					reassignFollowUpInfo.departmentCode = followUpUpdateInfo.getAssignedToDepartmentCode();
					reassignFollowUpInfo.functionCode = followUpUpdateInfo.getAssignedToFunctionCode();

					try {
						amdocsWorklistConv.reassignFollowUp(reassignFollowUpInfo);
					} catch(Exception e) {
						// Set BanPK (which also retrieves the BAN)
						amdocsUpdateBanConv.setBanPK(ban);
						// log the reassignment failure
						amdocsUpdateBanConv.addFollowUpText(followUpId, "Reassignment to work position [" + assignedToWorkPositionId + "] failed...");
						throw e;
					}

					// The commented part below is changed due to the Amdocs API changes.
					// No paging logic or preliminary FU retrieval is necessary anymore.
					/*
		                WorklistParameterInfo worklistParameterInfo = new WorklistParameterInfo();
		                worklistParameterInfo.followUpStatus = (byte) 'O';
		                worklistParameterInfo.openBy = true;

		                FollowUpList followUpList = null;
		                boolean followUpFound = false;
		                boolean morePages = true;
		                int pageNo = 0;

		                while (!followUpFound && morePages) {
		                    followUpList = amdocsWorklistConv.getFollowUpWorkList(worklistParameterInfo, pageNo);

		                    if (followUpList != null) {
		                        int rowCount = followUpList.followUpDetailsInfo != null ? followUpList.followUpDetailsInfo.length : 0;

		                        for (int i = 0; i < rowCount; i++) {
		                            if (followUpList.followUpDetailsInfo[i].fuBan == ban && followUpList.followUpDetailsInfo[i].fuId == followUpId) {
		                                followUpFound = true;
		                                break;
		                            }
		                        }

		                        morePages = followUpList.hasMoreRows;
		                        pageNo++;
		                    }
		                    else {
		                        morePages = false;
		                    }
		                }

		                if (followUpFound) {
		                    ReassignFollowUpInfo reassignFollowUpInfo = new ReassignFollowUpInfo();

		                    reassignFollowUpInfo.followUpId = new int[] {followUpId};
		                    reassignFollowUpInfo.workPosition = assignedToWorkPositionId;
		                    reassignFollowUpInfo.departmentCode = followUpUpdateInfo.getAssignedToDepartmentCode();
		                    reassignFollowUpInfo.functionCode = followUpUpdateInfo.getAssignedToFunctionCode();

		                    try {
		                        amdocsWorklistConv.reassignFollowUp(reassignFollowUpInfo);
		                    }
		                    catch(Throwable t) {
		                        // Set BanPK (which also retrieves the BAN)
					            amdocsUpdateBanConv.setBanPK(ban);
		                        // log the reassignment failure
		                        amdocsUpdateBanConv.addFollowUpText(followUpId, "Reassignment to work position [" + assignedToWorkPositionId + "] failed...");
		                        throw t;
		                    }
		                }
		                else {
		                    throw new Exception("Follow Up not found: ban = " + ban + ", followUpId = " + followUpId + ".");
		                }
					 */
				}
				return null;
			}
		});

	}

	@Override
	public void createFollowUp(final FollowUpInfo followUpInfo, String sessionId)
	throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);

				// Set BanPK (which also retrieves the BAN)
				amdocsUpdateBanConv.setBanPK(followUpInfo.getBan());

				// map telus info class to amdocs info class
				amdocs.APILink.datatypes.FollowUpInfo amdocsFollowUpInfo = new amdocs.APILink.datatypes.FollowUpInfo();
				amdocsFollowUpInfo.assignedToUser = followUpInfo.getAssignedToWorkPositionId();
				amdocsFollowUpInfo.dueDate = followUpInfo.getDueDate();
				amdocsFollowUpInfo.followUpText = followUpInfo.getText();
				amdocsFollowUpInfo.followUpType = followUpInfo.getType();

				// create follow-up
				amdocsUpdateBanConv.createFollowUp(amdocsFollowUpInfo);	
				
				return null;
			}

		});

	}

}
