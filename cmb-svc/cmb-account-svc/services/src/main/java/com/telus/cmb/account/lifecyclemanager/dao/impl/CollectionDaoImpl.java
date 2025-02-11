package com.telus.cmb.account.lifecyclemanager.dao.impl;

import java.util.Date;

import org.apache.log4j.Logger;

import amdocs.APILink.sessions.interfaces.UpdateBanConv;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.dao.CollectionDao;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.eas.account.info.CollectionStateInfo;
import com.telus.eas.account.info.CollectionStepInfo;

public class CollectionDaoImpl extends AmdocsDaoSupport implements CollectionDao{

	private static Logger LOGGER = Logger.getLogger(CollectionDaoImpl.class);	

	@Override
	public CollectionStateInfo retrieveBanCollectionInfo(final int ban, String sessionId)
	throws ApplicationException {
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<CollectionStateInfo>() {

			@Override
			public CollectionStateInfo doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);

				CollectionStateInfo ret = new CollectionStateInfo();
				amdocs.APILink.datatypes.BanCollectionInfo amdocsBanCollectionInfo = new amdocs.APILink.datatypes.BanCollectionInfo();


				// Set BanPK (which also retrieves the BAN)
				LOGGER.debug("Calling setBanPK()...");
				amdocsUpdateBanConv.setBanPK(ban);

				// retrieve BanCollectionInfo
				LOGGER.debug("Calling getBanCollectionInfo()...");
				amdocsBanCollectionInfo = amdocsUpdateBanConv.getBanCollectionInfo();

				// map Amdocs to Telus
				ret.setPathCode(amdocsBanCollectionInfo.autoTreatmentInfo.pathCode);
				ret.setCollectorCode(amdocsBanCollectionInfo.collectionInfo.assignedCollectorCode);
				ret.setCollectorName(amdocsBanCollectionInfo.collectionInfo.collector);
				ret.setCollectorAssignedDate(amdocsBanCollectionInfo.collectionInfo.collectorAssignedDate);
				ret.setAgencyCode(amdocsBanCollectionInfo.collectionInfo.collectionAgencyCode);
				ret.setAgencyAssignedDate(amdocsBanCollectionInfo.collectionInfo.agencyAssignedDate);
				ret.setAutoTreatment(amdocsBanCollectionInfo.collectionInfo.isAutoTreatment);
				ret.setNextStepApprovalCode(String.valueOf(amdocsBanCollectionInfo.autoTreatmentInfo.nextStepApprovalCode));
				CollectionStepInfo prevCollectionStep = new CollectionStepInfo();
				prevCollectionStep.setStep(amdocsBanCollectionInfo.autoTreatmentInfo.previousStepNumber);
				prevCollectionStep.setTreatmentDate(amdocsBanCollectionInfo.autoTreatmentInfo.previousStepDate);
				prevCollectionStep.setPath(amdocsBanCollectionInfo.autoTreatmentInfo.pathCode);
				ret.setPrevCollectionStep(prevCollectionStep);
				CollectionStepInfo nextCollectionStep = new CollectionStepInfo();
				nextCollectionStep.setStep(amdocsBanCollectionInfo.autoTreatmentInfo.nextStepNumber);
				nextCollectionStep.setTreatmentDate(amdocsBanCollectionInfo.autoTreatmentInfo.nextStepDate);
				nextCollectionStep.setPath(amdocsBanCollectionInfo.autoTreatmentInfo.pathCode);
				ret.setNextCollectionStep(nextCollectionStep);

				return ret;

			}
		});

	}

	@Override
	public void updateNextStepCollection(final int ban, final int stepNumber,
			final Date stepDate, final String pathCode, final String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);

				// Set BanPK (which also retrieves the BAN)
				amdocsUpdateBanConv.setBanPK(ban);

				amdocsUpdateBanConv.setNextStep(stepNumber,stepDate,pathCode);
				
				return null;
			}

		});

	}

}
