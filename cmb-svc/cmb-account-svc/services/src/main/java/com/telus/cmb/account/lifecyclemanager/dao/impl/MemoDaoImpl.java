package com.telus.cmb.account.lifecyclemanager.dao.impl;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.ManualMemoInfo;
import amdocs.APILink.sessions.interfaces.NewCdpdConv;
import amdocs.APILink.sessions.interfaces.NewCellularConv;
import amdocs.APILink.sessions.interfaces.NewIdenConv;
import amdocs.APILink.sessions.interfaces.NewPagerConv;
import amdocs.APILink.sessions.interfaces.NewTangoConv;
import amdocs.APILink.sessions.interfaces.SearchServices;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;
import amdocs.APILink.sessions.interfaces.UpdateCdpdConv;
import amdocs.APILink.sessions.interfaces.UpdateCellularConv;
import amdocs.APILink.sessions.interfaces.UpdateIdenConv;
import amdocs.APILink.sessions.interfaces.UpdatePagerConv;
import amdocs.APILink.sessions.interfaces.UpdateTangoConv;

import com.telus.api.ApplicationException;
import com.telus.api.account.Subscriber;
import com.telus.cmb.account.lifecyclemanager.dao.MemoDao;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.eas.framework.info.MemoInfo;

public class MemoDaoImpl extends AmdocsDaoSupport implements MemoDao {

	private static final Logger LOGGER = Logger.getLogger(MemoDaoImpl.class);

	@Override
	public void createMemoForSubscriber(final MemoInfo pMemoInfo, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				ManualMemoInfo amdocsManualMemoInfo = new ManualMemoInfo();
				int ban = pMemoInfo.getBanId();
				String subscriberId = pMemoInfo.getSubscriberId();

				// search for subscriber to find subscriber status
				SearchServices searchServices = transactionContext.createBean(SearchServices.class);				
				amdocs.APILink.datatypes.SearchSubscriber[] subscribers = searchServices.searchBySubscriber(subscriberId, AttributeTranslator.byteFromString(pMemoInfo.getProductType()));
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Sub search ban=["+subscribers[0].ban +"] memoBan=["+ban+"]");
				}
				boolean reserved = subscribers[0].subscriberStatus == (byte) 'R' && subscribers[0].ban == ban;

				// map telus info class to amdocs info class
				pMemoInfo.setText(AttributeTranslator.emptyFromNull(pMemoInfo.getText()));
				amdocsManualMemoInfo.userText = pMemoInfo.getText().trim().length() >= 2000 ? pMemoInfo.getText().trim().substring(0, 1999) : pMemoInfo.getText().trim();
				amdocsManualMemoInfo.memoType = pMemoInfo.getMemoType();

				try {
					// Set ProductPK /create Memo
					if (pMemoInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS)) {
						if (reserved) {
							NewCellularConv newCellularConv = transactionContext.createBean(NewCellularConv.class);
							newCellularConv.setProductPK(ban, subscriberId);
							LOGGER.debug("creating memo for reserved cellular subscriber...");
							newCellularConv.createMemo(amdocsManualMemoInfo);
						} else {
							UpdateCellularConv updateCellularConv = transactionContext.createBean(UpdateCellularConv.class);
							updateCellularConv.setProductPK(ban, subscriberId);
							LOGGER.debug("creating memo for cellular subscriber...");
							updateCellularConv.createMemo(amdocsManualMemoInfo);
						}
					}
					if (pMemoInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
						if (reserved) {
							NewIdenConv newIdenConv = transactionContext.createBean(NewIdenConv.class);
							newIdenConv.setProductPK(ban, subscriberId);
							LOGGER.debug("creating memo for reserved iden subscriber...");
							newIdenConv.createMemo(amdocsManualMemoInfo);
						} else {
							UpdateIdenConv updateIdenConv = transactionContext.createBean(UpdateIdenConv.class);
							updateIdenConv.setProductPK(ban, subscriberId);
							LOGGER.debug("creating memo for iden subscriber...");
							updateIdenConv.createMemo(amdocsManualMemoInfo);
						}
					}
					if (pMemoInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PAGER)) {
						if (reserved) {
							NewPagerConv newPagerConv = transactionContext.createBean(NewPagerConv.class);
							newPagerConv.setProductPK(ban, subscriberId);
							LOGGER.debug("creating memo for reserved pager subscriber...");
							newPagerConv.createMemo(amdocsManualMemoInfo);
						} else {
							UpdatePagerConv updatePagerConv = transactionContext.createBean(UpdatePagerConv.class);
							updatePagerConv.setProductPK(ban, subscriberId);
							LOGGER.debug("creating memo for pager subscriber...");
							updatePagerConv.createMemo(amdocsManualMemoInfo);
						}
					}
					if (pMemoInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_TANGO)) {
						if (reserved) {
							NewTangoConv newTangoConv = transactionContext.createBean(NewTangoConv.class);
							newTangoConv.setProductPK(ban, subscriberId);
							LOGGER.debug("creating memo for reserved tango subscriber...");
							newTangoConv.createMemo(amdocsManualMemoInfo);
						} else {
							UpdateTangoConv updateTangoConv = transactionContext.createBean(UpdateTangoConv.class);
							updateTangoConv.setProductPK(ban, subscriberId);
							LOGGER.debug("creating memo for tango subscriber...");
							updateTangoConv.createMemo(amdocsManualMemoInfo);
						}
					}
					if (pMemoInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_CDPD)) {
						if (reserved) {
							NewCdpdConv newCdpdConv = transactionContext.createBean(NewCdpdConv.class);
							newCdpdConv.setProductPK(ban, subscriberId);
							LOGGER.debug("creating memo for reserved cdpd subscriber...");
							newCdpdConv.createMemo(amdocsManualMemoInfo);
						} else {
							UpdateCdpdConv updateCdpdConv = transactionContext.createBean(UpdateCdpdConv.class);
							updateCdpdConv.setProductPK(ban, subscriberId);
							LOGGER.debug("creating memo for tango subscriber...");
							updateCdpdConv.createMemo(amdocsManualMemoInfo);
						}
					}
				} catch (Exception e) {
					LOGGER.info(pMemoInfo); //for prod troubleshooting
					throw e;
				}
				return null;
			}
		});
	}

	@Override
	public void createMemoForBan(final MemoInfo pMemoInfo, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				try {
					ManualMemoInfo amdocsManualMemoInfo = new ManualMemoInfo();
					// Set BanPK (which also retrieves the BAN)
					UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
					updateBanConv.setBanPK(pMemoInfo.getBanId());
					// map telus info class to amdocs info class
					pMemoInfo.setText(AttributeTranslator.emptyFromNull(pMemoInfo.getText()));
					amdocsManualMemoInfo.userText = pMemoInfo.getText().trim().length() >= 2000 ? pMemoInfo.getText().trim().substring(0, 1999) : pMemoInfo.getText().trim();
					amdocsManualMemoInfo.memoType = pMemoInfo.getMemoType();

					// create generic memo
					updateBanConv.createMemo(amdocsManualMemoInfo);
				} catch (Exception e) {
					LOGGER.info(pMemoInfo); // for prod troubleshooting
					throw e;
				}
				return null;
			}
		});
	}

}
