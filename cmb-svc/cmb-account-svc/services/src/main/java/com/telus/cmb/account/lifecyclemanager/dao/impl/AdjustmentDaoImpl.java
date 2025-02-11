package com.telus.cmb.account.lifecyclemanager.dao.impl;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.AdjustChargeInfo;
import amdocs.APILink.datatypes.AdjustDetailInfo;
import amdocs.APILink.datatypes.AdjustDetailList;
import amdocs.APILink.datatypes.AdjustmentReversalInfo;
import amdocs.APILink.datatypes.ChargeAndAdjDetailInfo;
import amdocs.APILink.datatypes.ChargeDetailInfo;
import amdocs.APILink.datatypes.CreateChargeAdjustInfo;
import amdocs.APILink.datatypes.CreateChargeAdjustWithTaxInfo;
import amdocs.APILink.datatypes.CreateChargeInfo;
import amdocs.APILink.datatypes.CreateCreditInfo;
import amdocs.APILink.datatypes.DeleteChargeInfo;
import amdocs.APILink.datatypes.DiscountKeyInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.APIBaseConv;
import amdocs.APILink.sessions.interfaces.NewCdpdConv;
import amdocs.APILink.sessions.interfaces.NewCellularConv;
import amdocs.APILink.sessions.interfaces.NewIdenConv;
import amdocs.APILink.sessions.interfaces.NewPagerConv;
import amdocs.APILink.sessions.interfaces.NewTangoConv;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;
import amdocs.APILink.sessions.interfaces.UpdateCdpdConv;
import amdocs.APILink.sessions.interfaces.UpdateCellularConv;
import amdocs.APILink.sessions.interfaces.UpdateIdenConv;
import amdocs.APILink.sessions.interfaces.UpdatePagerConv;
import amdocs.APILink.sessions.interfaces.UpdateProductConv;
import amdocs.APILink.sessions.interfaces.UpdateTangoConv;
import amdocs.enjutil.exceptions.ValidationException;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.account.Subscriber;
import com.telus.cmb.account.lifecyclemanager.dao.AdjustmentDao;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.mapping.ChargeAndAdjustmentMapper;
import com.telus.eas.account.info.FeeWaiverInfo;
import com.telus.eas.framework.info.ChargeAdjustmentInfo;
import com.telus.eas.framework.info.ChargeAdjustmentWithTaxInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.DiscountInfo;

public class AdjustmentDaoImpl extends AmdocsDaoSupport implements AdjustmentDao {

	private static final Logger LOGGER = Logger.getLogger(AdjustmentDaoImpl.class);

	@Override
	public List<FeeWaiverInfo> retrieveFeeWaivers(final int banId,String sessionId)
	throws ApplicationException {
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<List<FeeWaiverInfo>>() {

			@Override
			public List<FeeWaiverInfo> doInTransaction(AmdocsTransactionContext transactionContext)	throws Exception {
				amdocs.APILink.datatypes.FeeWaiverInfo[] amdocsFeeWaivers = null;
				List<FeeWaiverInfo> infos = new ArrayList<FeeWaiverInfo>();

				FeeWaiverInfo feeWaiver = null;
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(banId);
				amdocsFeeWaivers= updateBanConv.getFeeWaiverList();

				if (amdocsFeeWaivers != null) {
					for(int i=0; i<amdocsFeeWaivers.length; i++) {

						feeWaiver = new com.telus.eas.account.info.FeeWaiverInfo();
						feeWaiver.setTypeCode(amdocsFeeWaivers[i].featureCode);
						feeWaiver.setReasonCode(amdocsFeeWaivers[i].waiverRsn);
						feeWaiver.setEffectiveDate(amdocsFeeWaivers[i].waiverEffDate);
						feeWaiver.setExpiryDate(amdocsFeeWaivers[i].waiverExpDate);
						feeWaiver.setBanId(banId);
						infos.add(feeWaiver);
					}
				}
				return infos;
			}

		});	
	}

	@Override
	public void applyFeeWaiver(final FeeWaiverInfo feeWaiver,String sessionId)
	throws ApplicationException {

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {

				amdocs.APILink.datatypes.FeeWaiverInfo[] amdocsFeeWaivers = new amdocs.APILink.datatypes.FeeWaiverInfo[1];

				amdocsFeeWaivers[0] = new amdocs.APILink.datatypes.FeeWaiverInfo();
				amdocsFeeWaivers[0].featureCode = feeWaiver.getTypeCode();
				amdocsFeeWaivers[0].waiverRsn = feeWaiver.getReasonCode();
				amdocsFeeWaivers[0].waiverEffDate = feeWaiver.getEffectiveDate();
				amdocsFeeWaivers[0].waiverExpDate = feeWaiver.getExpiryDate();
				amdocsFeeWaivers[0].actMode = feeWaiver.getMode();
				amdocsFeeWaivers[0].waiverLevel = amdocs.APILink.datatypes.FeeWaiverInfo.BAN_LEVEL_FEE_WAIVER;

				// set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
				updateBanConv.setBanPK(feeWaiver.getBanId());

				// apply feeWaiverInfo
				updateBanConv.applyFeeWaiverList(amdocsFeeWaivers);

				return null;
			}

		});

	}


	@Override
	public void reverseCreditForSubscriber(final CreditInfo creditInfo, 
			final String reversalReasonCode, final String memoText, final boolean overrideThreshold,String sessionId) throws ApplicationException{

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {

				AdjustmentReversalInfo amdocsAdjustmentReversalInfo = new AdjustmentReversalInfo();
				//ProductPK productPK = new ProductPK();

				// map telus info class to amdocs info class
				amdocsAdjustmentReversalInfo.chargeSeqNo = creditInfo.getId();
				amdocsAdjustmentReversalInfo.actvReasonCode = reversalReasonCode;
				amdocsAdjustmentReversalInfo.memoUserText = memoText;

				// Set ProductPK / reverse credit
				if (creditInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS)) {
					UpdateCellularConv updateCellularConv=transactionContext.createBean(UpdateCellularConv.class);
					updateCellularConv.setProductPK(creditInfo.getBan(), creditInfo.getSubscriberId());
					if (creditInfo.isRecurring())
					{
						if (creditInfo.isReverseAllRecurring())
							amdocsAdjustmentReversalInfo.reverseAllRecurringAdjustments = AdjustmentReversalInfo.YES_CHECKED;
						amdocsAdjustmentReversalInfo.bypassAuthorization = creditInfo.isBypassAuthorization();
					}
					LOGGER.debug("revese credit for cellular subscriber...");
					updateCellularConv.adjustmentReversal(amdocsAdjustmentReversalInfo,overrideThreshold);
				}
				if (creditInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
					UpdateIdenConv updateIdenConv=transactionContext.createBean(UpdateIdenConv.class);
					updateIdenConv.setProductPK(creditInfo.getBan(), creditInfo.getSubscriberId());
					if (creditInfo.isRecurring())
					{
						if (creditInfo.isReverseAllRecurring())
							amdocsAdjustmentReversalInfo.reverseAllRecurringAdjustments = AdjustmentReversalInfo.YES_CHECKED;
						amdocsAdjustmentReversalInfo.bypassAuthorization = creditInfo.isBypassAuthorization();
					}
					LOGGER.debug("revese credit for iden subscriber...");
					updateIdenConv.adjustmentReversal(amdocsAdjustmentReversalInfo,overrideThreshold);
				}
				if (creditInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PAGER)) {
					UpdatePagerConv updatePagerConv=transactionContext.createBean(UpdatePagerConv.class);
					updatePagerConv.setProductPK(creditInfo.getBan(), creditInfo.getSubscriberId());
					if (creditInfo.isRecurring())
					{
						if (creditInfo.isReverseAllRecurring())
							amdocsAdjustmentReversalInfo.reverseAllRecurringAdjustments = AdjustmentReversalInfo.YES_CHECKED;
						amdocsAdjustmentReversalInfo.bypassAuthorization = creditInfo.isBypassAuthorization();
					}
					LOGGER.debug("revese credit for pager subscriber...");
					updatePagerConv.adjustmentReversal(amdocsAdjustmentReversalInfo,overrideThreshold);
				}
				if (creditInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_TANGO)) {
					UpdateTangoConv updateTangoConv=transactionContext.createBean(UpdateTangoConv.class);
					updateTangoConv.setProductPK(creditInfo.getBan(), creditInfo.getSubscriberId());
					if (creditInfo.isRecurring())
					{
						if (creditInfo.isReverseAllRecurring())
							amdocsAdjustmentReversalInfo.reverseAllRecurringAdjustments = AdjustmentReversalInfo.YES_CHECKED;
						amdocsAdjustmentReversalInfo.bypassAuthorization = creditInfo.isBypassAuthorization();
					}
					LOGGER.debug("revese credit for tango subscriber...");
					updateTangoConv.adjustmentReversal(amdocsAdjustmentReversalInfo,overrideThreshold);
				}
				if (creditInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_CDPD)) {
					UpdateCdpdConv updateCdpdConv=transactionContext.createBean(UpdateCdpdConv.class);
					updateCdpdConv.setProductPK(creditInfo.getBan(), creditInfo.getSubscriberId());
					if (creditInfo.isRecurring())
					{
						if (creditInfo.isReverseAllRecurring())
							amdocsAdjustmentReversalInfo.reverseAllRecurringAdjustments = AdjustmentReversalInfo.YES_CHECKED;
						amdocsAdjustmentReversalInfo.bypassAuthorization = creditInfo.isBypassAuthorization();
					}
					LOGGER.debug("revese credit for cdpd subscriber...");
					updateCdpdConv.adjustmentReversal(amdocsAdjustmentReversalInfo,overrideThreshold);
				}

				return null;
			}

		});
	}

	@Override
	public void reverseCreditForBan(final CreditInfo creditInfo, 
			final String reversalReasonCode, final String memoText, final boolean overrideThreshold,String sessionId) throws ApplicationException{

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {

				AdjustmentReversalInfo amdocsAdjustmentReversalInfo = new AdjustmentReversalInfo();

				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(creditInfo.getBan());

				// map telus info class to amdocs info class
				amdocsAdjustmentReversalInfo.chargeSeqNo = creditInfo.getId();
				amdocsAdjustmentReversalInfo.actvReasonCode = reversalReasonCode;
				amdocsAdjustmentReversalInfo.memoUserText = memoText;

				if ( creditInfo.isRecurring())
				{
					if (creditInfo.isReverseAllRecurring())
						amdocsAdjustmentReversalInfo.reverseAllRecurringAdjustments = AdjustmentReversalInfo.YES_CHECKED;
					amdocsAdjustmentReversalInfo.bypassAuthorization = creditInfo.isBypassAuthorization();

				}

				// delete charge
				updateBanConv.adjustmentReversal(amdocsAdjustmentReversalInfo,overrideThreshold);

				return null;
			}

		});
	}

	@Override
	public void deleteChargeForSubscriber(final ChargeInfo chargeInfo, 
			final String deletionReasonCode, final String memoText, final boolean overrideThreshold,String sessionId) throws ApplicationException{

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {

				DeleteChargeInfo amdocsDeleteChargeInfo = new DeleteChargeInfo();

				// map telus info class to amdocs info class
				amdocsDeleteChargeInfo.chargeSeqNo = chargeInfo.getId();
				amdocsDeleteChargeInfo.reasonCode = deletionReasonCode;
				amdocsDeleteChargeInfo.memoUserText = memoText;

				// Set ProductPK / delete Charge
				if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS)) {
					UpdateCellularConv updateCellularConv=transactionContext.createBean(UpdateCellularConv.class);
					updateCellularConv.setProductPK(chargeInfo.getBan(), chargeInfo.getSubscriberId());
					LOGGER.debug("deleting charge for cellular subscriber...");
					updateCellularConv.deleteCharge(amdocsDeleteChargeInfo,overrideThreshold);
				}
				if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
					UpdateIdenConv updateIdenConv=transactionContext.createBean(UpdateIdenConv.class);
					updateIdenConv.setProductPK(chargeInfo.getBan(), chargeInfo.getSubscriberId());
					LOGGER.debug("deleting charge for iden subscriber...");
					updateIdenConv.deleteCharge(amdocsDeleteChargeInfo,overrideThreshold);
				}
				if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PAGER)) {
					UpdatePagerConv updatePagerConv=transactionContext.createBean(UpdatePagerConv.class);
					updatePagerConv.setProductPK(chargeInfo.getBan(), chargeInfo.getSubscriberId());
					LOGGER.debug("deleting charge for pager subscriber...");
					updatePagerConv.deleteCharge(amdocsDeleteChargeInfo,overrideThreshold);
				}
				if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_TANGO)) {
					UpdateTangoConv updateTangoConv=transactionContext.createBean(UpdateTangoConv.class);
					updateTangoConv.setProductPK(chargeInfo.getBan(), chargeInfo.getSubscriberId());
					LOGGER.debug("deleting charge for tango subscriber...");
					updateTangoConv.deleteCharge(amdocsDeleteChargeInfo,overrideThreshold);
				}
				if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_CDPD)) {
					UpdateCdpdConv updateCdpdConv=transactionContext.createBean(UpdateCdpdConv.class);
					updateCdpdConv.setProductPK(chargeInfo.getBan(), chargeInfo.getSubscriberId());
					LOGGER.debug("deleting charge for cdpd subscriber...");
					updateCdpdConv.deleteCharge(amdocsDeleteChargeInfo,overrideThreshold);
				}

				return null;
			}

		});
	}

	@Override
	public void deleteChargeForBan(final ChargeInfo chargeInfo, 
			final String deletionReasonCode, final String memoText, final boolean overrideThreshold,String sessionId) throws ApplicationException{

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {

				DeleteChargeInfo amdocsDeleteChargeInfo = new DeleteChargeInfo();

				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(chargeInfo.getBan());

				// map telus info class to amdocs info class
				amdocsDeleteChargeInfo.chargeSeqNo = chargeInfo.getId();
				amdocsDeleteChargeInfo.reasonCode = deletionReasonCode;
				amdocsDeleteChargeInfo.memoUserText = memoText;

				// delete charge
				updateBanConv.deleteCharge(amdocsDeleteChargeInfo,overrideThreshold);

				return null;
			}

		});
	}

	@Override
	public double applyChargeToAccountForSubscriber(final ChargeInfo chargeInfo, 
			final boolean overrideThreshold,String sessionId) throws ApplicationException{

		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Double>() {

			@Override
			public Double doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {

				CreateChargeInfo amdocsCreateChargeInfo = new CreateChargeInfo();

				// map telus info class to amdocs info class
				amdocsCreateChargeInfo.chargeAmount = chargeInfo.getAmount();
				amdocsCreateChargeInfo.memoText = chargeInfo.getText();
				amdocsCreateChargeInfo.chargeCode = chargeInfo.getChargeCode();
				amdocsCreateChargeInfo.effectiveDate = chargeInfo.getEffectiveDate();

				ChargeDetailInfo chargeDetailInfo = null;

				// Set ProductPK /create Charge
				if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS)) {
					UpdateCellularConv updateCellularConv=transactionContext.createBean(UpdateCellularConv.class);
					updateCellularConv.setProductPK(chargeInfo.getBan(), chargeInfo.getSubscriberId());
					LOGGER.debug("creating charge for cellular subscriber..."+chargeInfo);
					chargeDetailInfo = updateCellularConv.applyChargeToAccount(amdocsCreateChargeInfo,overrideThreshold);
				}
				if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
					UpdateIdenConv updateIdenConv=transactionContext.createBean(UpdateIdenConv.class);
					updateIdenConv.setProductPK(chargeInfo.getBan(), chargeInfo.getSubscriberId());
					LOGGER.debug("creating charge for iden subscriber..."+chargeInfo);
					chargeDetailInfo = updateIdenConv.applyChargeToAccount(amdocsCreateChargeInfo,overrideThreshold);
				}
				if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PAGER)) {
					UpdatePagerConv updatePagerConv=transactionContext.createBean(UpdatePagerConv.class);
					updatePagerConv.setProductPK(chargeInfo.getBan(), chargeInfo.getSubscriberId());
					LOGGER.debug("creating charge for pager subscriber..."+chargeInfo);
					chargeDetailInfo = updatePagerConv.applyChargeToAccount(amdocsCreateChargeInfo,overrideThreshold);
				}
				if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_TANGO)) {
					UpdateTangoConv updateTangoConv=transactionContext.createBean(UpdateTangoConv.class);
					updateTangoConv.setProductPK(chargeInfo.getBan(), chargeInfo.getSubscriberId());
					LOGGER.debug("creating charge for tango subscriber..."+chargeInfo);
					chargeDetailInfo = updateTangoConv.applyChargeToAccount(amdocsCreateChargeInfo,overrideThreshold);
				}
				if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_CDPD)) {
					UpdateCdpdConv updateCdpdConv=transactionContext.createBean(UpdateCdpdConv.class);
					updateCdpdConv.setProductPK(chargeInfo.getBan(), chargeInfo.getSubscriberId());
					LOGGER.debug("creating charge for cdpd subscriber..."+chargeInfo);
					chargeDetailInfo = updateCdpdConv.applyChargeToAccount(amdocsCreateChargeInfo,overrideThreshold);
				}

				LOGGER.debug("Unique reference number is " + chargeDetailInfo.chargeSeqNo);
				return chargeDetailInfo.chargeSeqNo;
			}

		});
	}

	@Override
	public double applyChargeToAccountForBan(
			final ChargeInfo chargeInfo, final boolean overrideThreshold,String sessionId) throws ApplicationException{

		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Double>() {

			@Override
			public Double doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {

				LOGGER.debug("applyChargeToAccountForBan.."+chargeInfo);
			    CreateChargeInfo amdocsCreateChargeInfo = new CreateChargeInfo();

			      // Set BanPK (which also retrieves the BAN)
			    UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(chargeInfo.getBan());

			      // map telus info class to amdocs info class
			      amdocsCreateChargeInfo.chargeAmount = chargeInfo.getAmount();
			      amdocsCreateChargeInfo.memoText = chargeInfo.getText();
			      amdocsCreateChargeInfo.chargeCode = chargeInfo.getChargeCode();
			      amdocsCreateChargeInfo.effectiveDate = chargeInfo.getEffectiveDate();

			      // create charge
			      ChargeDetailInfo chargeDetailInfo = updateBanConv.applyChargeToAccount(amdocsCreateChargeInfo,overrideThreshold);

			      LOGGER.debug("Leaving... unique reference number is " + chargeDetailInfo.chargeSeqNo );
			      return chargeDetailInfo.chargeSeqNo;
			}

		});
	}
	@Override
	public double adjustChargeForSubscriber(final ChargeInfo pChargeInfo,
			final double pAdjustmentAmount, final String pAdjustmentReasonCode,
			final String pMemoText, final boolean pOverrideThreshold,String sessionId)
	throws ApplicationException {

		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Double>() {

			@Override
			public Double doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {


				// map telus info class to amdocs info class
				AdjustChargeInfo amdocsAdjustChargeInfo = new AdjustChargeInfo();
				amdocsAdjustChargeInfo.chargeSeqNo = pChargeInfo.getId();
				amdocsAdjustChargeInfo.isBilled = pChargeInfo.isBilled();
				amdocsAdjustChargeInfo.reasonCode = pAdjustmentReasonCode;
				amdocsAdjustChargeInfo.memoUserText = pMemoText;
				amdocsAdjustChargeInfo.adjAmt = pAdjustmentAmount;
				amdocsAdjustChargeInfo.billSequenceNo = pChargeInfo.getBillSequenceNo();

				AdjustDetailInfo adjustDetailInfo = null;
				// Set ProductPK / adjust Charge
				if (pChargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS)) {
					UpdateCellularConv updateCellularConv=transactionContext.createBean(UpdateCellularConv.class);
					updateCellularConv.setProductPK(pChargeInfo.getBan(), pChargeInfo.getSubscriberId());
					adjustDetailInfo = updateCellularConv.adjustCharge(amdocsAdjustChargeInfo,pOverrideThreshold);
				}
				if (pChargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
					UpdateIdenConv updateIdenConv=transactionContext.createBean(UpdateIdenConv.class);
					updateIdenConv.setProductPK(pChargeInfo.getBan(), pChargeInfo.getSubscriberId());
					adjustDetailInfo = updateIdenConv.adjustCharge(amdocsAdjustChargeInfo,pOverrideThreshold);
				}
				if (pChargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PAGER)) {
					UpdatePagerConv updatePagerConv = transactionContext.createBean(UpdatePagerConv.class);
					updatePagerConv.setProductPK(pChargeInfo.getBan(), pChargeInfo.getSubscriberId());
					adjustDetailInfo = updatePagerConv.adjustCharge(amdocsAdjustChargeInfo,pOverrideThreshold);
				}
				if (pChargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_TANGO)) {
					UpdateTangoConv updateTangoConv = transactionContext.createBean(UpdateTangoConv.class);
					updateTangoConv.setProductPK(pChargeInfo.getBan(), pChargeInfo.getSubscriberId());
					adjustDetailInfo = updateTangoConv.adjustCharge(amdocsAdjustChargeInfo,pOverrideThreshold);
				}
				if (pChargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_CDPD)) {
					UpdateCdpdConv updateCdpdConv = transactionContext.createBean(UpdateCdpdConv.class);
					updateCdpdConv.setProductPK(pChargeInfo.getBan(), pChargeInfo.getSubscriberId());
					adjustDetailInfo = updateCdpdConv.adjustCharge(amdocsAdjustChargeInfo,pOverrideThreshold);
				}
				return  adjustDetailInfo.adjustmentId;
			}
		});

	}

	@Override
	public double adjustChargeForBan(final ChargeInfo pChargeInfo,
			final double pAdjustmentAmount, final String pAdjustmentReasonCode,
			final String pMemoText, final boolean pOverrideThreshold,String sessionId)
	throws ApplicationException {

		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Double>() {

			@Override
			public Double doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {

				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
				updateBanConv.setBanPK(pChargeInfo.getBan());
				AdjustChargeInfo amdocsAdjustChargeInfo = new AdjustChargeInfo();
				amdocsAdjustChargeInfo.chargeSeqNo = pChargeInfo.getId();
				amdocsAdjustChargeInfo.isBilled = pChargeInfo.isBilled();
				amdocsAdjustChargeInfo.reasonCode = pAdjustmentReasonCode;
				amdocsAdjustChargeInfo.memoUserText = pMemoText;
				amdocsAdjustChargeInfo.adjAmt = pAdjustmentAmount;
				amdocsAdjustChargeInfo.billSequenceNo = pChargeInfo.getBillSequenceNo();
				AdjustDetailInfo adjustDetailInfo = updateBanConv.adjustCharge(amdocsAdjustChargeInfo,pOverrideThreshold);
				return adjustDetailInfo.adjustmentId;
			}
		});
	}
	@Override
	public void applyDiscountToAccountForSubscriber(final DiscountInfo discountInfo,String sessionId)
			throws ApplicationException {
		 getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

				@Override
				public Object doInTransaction(AmdocsTransactionContext transactionContext)
				throws Exception {
			
				    DiscountKeyInfo[] amdocsDiscountKeyInfoArray = new DiscountKeyInfo[1];
				      char applyMode =
				    	  discountInfo.getDiscountSequenceNo() == 0
				              ? DiscountInfo.INSERT_DISCOUNT
				              : discountInfo.getEffectiveDate().compareTo(getToday()) == 0
				              && discountInfo.getExpiryDate().compareTo(getToday()) == 0
				                  ? DiscountInfo.DELETE_DISCOUNT
				                  : DiscountInfo.UPDATE_DISCOUNT;

				      amdocsDiscountKeyInfoArray[0] = new DiscountKeyInfo();
				      amdocsDiscountKeyInfoArray[0].code = discountInfo.getDiscountCode();
				      if (applyMode != DiscountInfo.DELETE_DISCOUNT) {
				        amdocsDiscountKeyInfoArray[0].effectiveDate = discountInfo.getEffectiveDate();
				        amdocsDiscountKeyInfoArray[0].expirationDate = discountInfo.getExpiryDate();
				      }
				      amdocsDiscountKeyInfoArray[0].sequenceNumber = discountInfo.getDiscountSequenceNo();

				      boolean isforFutureSubscriberActivation = discountInfo.isForFutureSubscriberActivation();
				    		  //(discountInfo.getEffectiveDate() != null) && (discountInfo.getEffectiveDate().after(getToday()));
					// Set ProductPK / adjust Charge
					if (discountInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS)) {
						if (isforFutureSubscriberActivation) {
							NewCellularConv newCellularConv = transactionContext.createBean(NewCellularConv.class);
							newCellularConv.setProductPK(discountInfo.getBan(), discountInfo.getSubscriberId());
							newCellularConv.applyDiscounts(amdocsDiscountKeyInfoArray,applyMode);
						} else {
							UpdateCellularConv updateCellularConv=transactionContext.createBean(UpdateCellularConv.class);
							updateCellularConv.setProductPK(discountInfo.getBan(), discountInfo.getSubscriberId());
							updateCellularConv.applyDiscounts(amdocsDiscountKeyInfoArray,applyMode);
						}
						
					}
					if (discountInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
						if (isforFutureSubscriberActivation) {
							NewIdenConv newIdenConv=transactionContext.createBean(NewIdenConv.class);
							newIdenConv.setProductPK(discountInfo.getBan(), discountInfo.getSubscriberId());
							newIdenConv.applyDiscounts(amdocsDiscountKeyInfoArray,applyMode);
						} else {
							UpdateIdenConv updateIdenConv=transactionContext.createBean(UpdateIdenConv.class);
							updateIdenConv.setProductPK(discountInfo.getBan(), discountInfo.getSubscriberId());
							updateIdenConv.applyDiscounts(amdocsDiscountKeyInfoArray,applyMode);
						}
					}
					if (discountInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PAGER)) {
						if (isforFutureSubscriberActivation) {
							NewPagerConv newPagerConv = transactionContext.createBean(NewPagerConv.class);
							newPagerConv.setProductPK(discountInfo.getBan(), discountInfo.getSubscriberId());
							newPagerConv.applyDiscounts(amdocsDiscountKeyInfoArray,applyMode);
						} else {
							UpdatePagerConv updatePagerConv = transactionContext.createBean(UpdatePagerConv.class);
							updatePagerConv.setProductPK(discountInfo.getBan(), discountInfo.getSubscriberId());
							updatePagerConv.applyDiscounts(amdocsDiscountKeyInfoArray,applyMode);
						}
					}
					if (discountInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_TANGO)) {
						if (isforFutureSubscriberActivation) {
							NewTangoConv newTangoConv = transactionContext.createBean(NewTangoConv.class);
							newTangoConv.setProductPK(discountInfo.getBan(), discountInfo.getSubscriberId());
							newTangoConv.applyDiscounts(amdocsDiscountKeyInfoArray,applyMode);
						} else {
							UpdateTangoConv updateTangoConv = transactionContext.createBean(UpdateTangoConv.class);
							updateTangoConv.setProductPK(discountInfo.getBan(), discountInfo.getSubscriberId());
							updateTangoConv.applyDiscounts(amdocsDiscountKeyInfoArray,applyMode);
						}
					}
					if (discountInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_CDPD)) {
						if (isforFutureSubscriberActivation) {
							NewCdpdConv newCdpdConv = transactionContext.createBean(NewCdpdConv.class);
							newCdpdConv.setProductPK(discountInfo.getBan(), discountInfo.getSubscriberId());
							newCdpdConv.applyDiscounts(amdocsDiscountKeyInfoArray,applyMode);
						} else {
							UpdateCdpdConv updateCdpdConv = transactionContext.createBean(UpdateCdpdConv.class);
							updateCdpdConv.setProductPK(discountInfo.getBan(), discountInfo.getSubscriberId());
							updateCdpdConv.applyDiscounts(amdocsDiscountKeyInfoArray,applyMode);
						}
					}
					return  null;
				}
			});
		
	}
	 private Date getToday() {

	     Date curDate = new Date();
	     SimpleDateFormat yrFormat = new SimpleDateFormat("yyyy");
	     SimpleDateFormat mnthFormat = new SimpleDateFormat("MM");
	     SimpleDateFormat dayFormat = new SimpleDateFormat("d");
	     int day = Integer.parseInt(dayFormat.format(curDate));
	     int month = Integer.parseInt(mnthFormat.format(curDate)) -1;
	     int year = Integer.parseInt(yrFormat.format(curDate));

	     GregorianCalendar today = new GregorianCalendar(year, month, day);
	     return today.getTime();
	 }
	@Override
	public void applyDiscountToAccountForBan(final DiscountInfo discountInfo,String sessionId)
			throws ApplicationException {
		 getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

				@Override
				public Object doInTransaction(AmdocsTransactionContext transactionContext)
				throws Exception {
				    DiscountKeyInfo[] amdocsDiscountKeyInfoArray = new DiscountKeyInfo[1];
				      char applyMode =
				    	  discountInfo.getDiscountSequenceNo() == 0
				              ? DiscountInfo.INSERT_DISCOUNT
				              : discountInfo.getEffectiveDate().compareTo(getToday()) == 0
				              && discountInfo.getExpiryDate().compareTo(getToday()) == 0
				                  ? DiscountInfo.DELETE_DISCOUNT
				                  : DiscountInfo.UPDATE_DISCOUNT;

				      amdocsDiscountKeyInfoArray[0] = new DiscountKeyInfo();
				      amdocsDiscountKeyInfoArray[0].code = discountInfo.getDiscountCode();
				      if (applyMode != DiscountInfo.DELETE_DISCOUNT) {
				        amdocsDiscountKeyInfoArray[0].effectiveDate = discountInfo.getEffectiveDate();
				        amdocsDiscountKeyInfoArray[0].expirationDate = discountInfo.getExpiryDate();
				      }
				      amdocsDiscountKeyInfoArray[0].sequenceNumber = discountInfo.getDiscountSequenceNo();
				      
					UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
					updateBanConv.setBanPK(discountInfo.getBan());
					updateBanConv.applyDiscounts(amdocsDiscountKeyInfoArray,applyMode);
					return null;
				}


			});
		
	}
	@Override
	public boolean applyCreditToAccountForSubscriber(final CreditInfo pCreditInfo,
			final boolean pOverrideThreshold,String sessionId) throws ApplicationException {
		
		 return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Boolean>() {

			@Override
			public Boolean doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {
				
				boolean result = false;
				
				// Set ProductPK / adjust Charge
				if (pCreditInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS)) {
					UpdateCellularConv updateCellularConv=transactionContext.createBean(UpdateCellularConv.class);
					updateCellularConv.setProductPK(pCreditInfo.getBan(), pCreditInfo.getSubscriberId());
					result = applyCreditToAccount(updateCellularConv, pCreditInfo, pOverrideThreshold);
					
				}
				else if (pCreditInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
					UpdateIdenConv updateIdenConv=transactionContext.createBean(UpdateIdenConv.class);
					updateIdenConv.setProductPK(pCreditInfo.getBan(), pCreditInfo.getSubscriberId());
					result = applyCreditToAccount(updateIdenConv, pCreditInfo, pOverrideThreshold);
				}
				else if (pCreditInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PAGER)) {
					UpdatePagerConv updatePagerConv = transactionContext.createBean(UpdatePagerConv.class);
					updatePagerConv.setProductPK(pCreditInfo.getBan(), pCreditInfo.getSubscriberId());
					result = applyCreditToAccount(updatePagerConv, pCreditInfo, pOverrideThreshold);
				}
				else if (pCreditInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_TANGO)) {
					UpdateTangoConv updateTangoConv = transactionContext.createBean(UpdateTangoConv.class);
					updateTangoConv.setProductPK(pCreditInfo.getBan(), pCreditInfo.getSubscriberId());
					result = applyCreditToAccount(updateTangoConv, pCreditInfo, pOverrideThreshold);
				}
				else if (pCreditInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_CDPD)) {
					UpdateCdpdConv updateCdpdConv = transactionContext.createBean(UpdateCdpdConv.class);
					updateCdpdConv.setProductPK(pCreditInfo.getBan(), pCreditInfo.getSubscriberId());
					result = applyCreditToAccount(updateCdpdConv, pCreditInfo, pOverrideThreshold);
				}
				return  result;
			}
		});

	}

	@Override
	public boolean applyCreditToAccountForBan(final CreditInfo pCreditInfo,
			final boolean pOverrideThreshold,String sessionId) throws ApplicationException {
		
		 return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Boolean>() {

			@Override
			public Boolean doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {

				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
				updateBanConv.setBanPK(pCreditInfo.getBan());
				return applyCreditToAccount(updateBanConv, pCreditInfo, pOverrideThreshold);

			}
		});

	}
	  private boolean applyCreditToAccount(APIBaseConv apiBaseConvBean, CreditInfo pCreditInfo, boolean pOverrideThreshold) throws ValidationException, RemoteException {
		    CreateCreditInfo amdocsCreateCreditInfo = new CreateCreditInfo();
		    
		    // Map the Telus info class to the Amdocs info class
		    amdocsCreateCreditInfo.adjustmentAmt = pCreditInfo.getAmount();
		    amdocsCreateCreditInfo.balanceImpactCode = pCreditInfo.isBalanceImpactFlag();
		    amdocsCreateCreditInfo.memoText = pCreditInfo.getText();
		    amdocsCreateCreditInfo.reasonCode = pCreditInfo.getReasonCode();
		    if (amdocsCreateCreditInfo.balanceImpactCode) {
		      amdocsCreateCreditInfo.billDate = pCreditInfo.getEffectiveDate();
		    } else {
		      amdocsCreateCreditInfo.effectiveDate = pCreditInfo.getEffectiveDate();
		    }

		    if (pCreditInfo.isRecurring()) {
		       amdocsCreateCreditInfo.noOfTimesToApply = pCreditInfo.getNumberOfRecurring();
		       amdocsCreateCreditInfo.bypassAuthorization = pCreditInfo.isBypassAuthorization();
		    }

		    boolean adjustmentCreated = false;
		    // Create the credit
		    AdjustDetailList adjustDetailList = apiBaseConvBean.applyCreditToAccount(amdocsCreateCreditInfo, pOverrideThreshold);
		    if ( adjustDetailList!=null 
		    		&& adjustDetailList.adjustDetailInfo!=null 
		    		&& adjustDetailList.adjustDetailInfo.length>0 
		    		&& adjustDetailList.adjustDetailInfo[0].adjustmentId>0){
		    	adjustmentCreated = true;
		    }

			// Handle taxes for credits if the credit is taxable and not prepaid
			if (!pCreditInfo.isPrepaid() && (pCreditInfo.getTaxOption() != CreditInfo.TAX_OPTION_NO_TAX)) {
				// GST
				if (pCreditInfo.getGSTAmount() != 0) {
					amdocsCreateCreditInfo.adjustmentAmt = pCreditInfo.getGSTAmount();
				    amdocsCreateCreditInfo.reasonCode = pCreditInfo.getGSTAdjustmentReasonCode();
				    amdocsCreateCreditInfo.memoText = "GST for: " + pCreditInfo.getText();
				    apiBaseConvBean.applyCreditToAccount(amdocsCreateCreditInfo, pOverrideThreshold);
				}
				// PST
				if ((pCreditInfo.getPSTAmount() != 0) && (pCreditInfo.getTaxOption() != CreditInfo.TAX_OPTION_GST_ONLY)){
				    amdocsCreateCreditInfo.adjustmentAmt = pCreditInfo.getPSTAmount();
				    amdocsCreateCreditInfo.reasonCode = pCreditInfo.getPSTAdjustmentReasonCode();
				    if (pCreditInfo.getTaxSummary().isQuebec()) {
				    	amdocsCreateCreditInfo.memoText = "QST for: " + pCreditInfo.getText();
					} else {
						amdocsCreateCreditInfo.memoText = "PST for: " + pCreditInfo.getText();
					}
					apiBaseConvBean.applyCreditToAccount(amdocsCreateCreditInfo, pOverrideThreshold);
				}
				// HST
				if ((pCreditInfo.getHSTAmount() != 0) && (pCreditInfo.getTaxOption() != CreditInfo.TAX_OPTION_GST_ONLY)) {
					amdocsCreateCreditInfo.adjustmentAmt = pCreditInfo.getHSTAmount();
					amdocsCreateCreditInfo.reasonCode = pCreditInfo.getHSTAdjustmentReasonCode();
					amdocsCreateCreditInfo.memoText = "HST for: " + pCreditInfo.getText();
					apiBaseConvBean.applyCreditToAccount(amdocsCreateCreditInfo, pOverrideThreshold);
				}
			}			 
			return adjustmentCreated;
	  }

	  @Override
	  public List<ChargeAdjustmentInfo> applyChargesAndAdjustmentsToAccount(final List<ChargeAdjustmentInfo> chargeInfoList, String sessionId) throws ApplicationException {

		  return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<List<ChargeAdjustmentInfo>>() {			  
			  @Override
			  public List<ChargeAdjustmentInfo> doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				  List<ChargeAdjustmentInfo> list = new ArrayList<ChargeAdjustmentInfo>();
				  UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
				  for (ChargeAdjustmentInfo chargeInfo : chargeInfoList) {
					  CreateChargeAdjustInfo createChargeAdjustInfo = new CreateChargeAdjustInfo();
					  try {
						  // Set BanPK (which also retrieves the BAN)			
						  updateBanConv.setBanPK(chargeInfo.getBan());
						  createChargeAdjustInfo  = ChargeAndAdjustmentMapper.mapToAmdocsCreateChargeAdjustInfo(chargeInfo);
						  ChargeAndAdjDetailInfo chargeAndAdjDetailInfo = updateBanConv.applyChargeAndAdjust(createChargeAdjustInfo);
						  list.add(ChargeAndAdjustmentMapper.mapToTelusChargeAdjustmentInfo(chargeAndAdjDetailInfo, chargeInfo));
						  LOGGER.debug("In loop... chargeSeqNo is " + chargeAndAdjDetailInfo.chargeDetailInfo.chargeSeqNo + " ... adjustmentId is " + 
								  chargeAndAdjDetailInfo.adjustDetailInfo.adjustmentId);
					  } catch (ValidateException vex) {
						  list.add(ChargeAndAdjustmentMapper.getFailedTransactionsDetails(chargeInfo, "KB-"+String.valueOf(vex.getErrorInd()), vex.getErrorMsg()));
					  } catch (Exception ex) {
						  list.add(ChargeAndAdjustmentMapper.getFailedTransactionsDetails(chargeInfo, SystemCodes.CMB_ALM_DAO, ex.getMessage()));
					  }
				  }
				  return list;
			  }
		  });
	  }
	  
	  @Override
	  public List<ChargeAdjustmentInfo> applyChargesAndAdjustmentsToAccountForSubscriber(final List<ChargeAdjustmentInfo> chargeInfoList, String sessionId) throws ApplicationException {		  
		  return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<List<ChargeAdjustmentInfo>>() {			
			  
			  @Override
			  public List<ChargeAdjustmentInfo> doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				  List<ChargeAdjustmentInfo> list = new ArrayList<ChargeAdjustmentInfo>();				  
				  for (ChargeAdjustmentInfo chargeInfo : chargeInfoList )  {					  
					try {						  
						CreateChargeAdjustInfo createChargeAdjustInfo  = ChargeAndAdjustmentMapper.mapToAmdocsCreateChargeAdjustInfo(chargeInfo);						  
						UpdateProductConv updateProductConv = null;						  
						if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS)) {
							updateProductConv=transactionContext.createBean(UpdateCellularConv.class);
						} else if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
							updateProductConv=transactionContext.createBean(UpdateIdenConv.class);
						} else if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PAGER)) {
							updateProductConv = transactionContext.createBean(UpdatePagerConv.class);
						} else if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_TANGO)) {
							updateProductConv = transactionContext.createBean(UpdateTangoConv.class);
						} else if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_CDPD)) {
							updateProductConv = transactionContext.createBean(UpdateCdpdConv.class);
						} else {
							throw new SystemException(SystemCodes.CMB_EJB, "Invalid Product Type : " + chargeInfo.getProductType(), "");
						}					
						updateProductConv.setProductPK(chargeInfo.getBan(), chargeInfo.getSubscriberId());						
						ChargeAndAdjDetailInfo chargeAndAdjDetailInfo  =  updateProductConv.applyChargeAndAdjust(createChargeAdjustInfo);
						list.add(ChargeAndAdjustmentMapper.mapToTelusChargeAdjustmentInfo(chargeAndAdjDetailInfo, chargeInfo));						
						LOGGER.debug("In loop... chargeSeqNo is " + chargeAndAdjDetailInfo.chargeDetailInfo.chargeSeqNo + " ... adjustmentId is " + 
								chargeAndAdjDetailInfo.adjustDetailInfo.adjustmentId);						
					} catch (ValidateException vex){
						list.add(ChargeAndAdjustmentMapper.getFailedTransactionsDetails(chargeInfo, "KB-"+String.valueOf(vex.getErrorInd()),vex.getErrorMsg()));
					} catch (Exception ex){
						list.add(ChargeAndAdjustmentMapper.getFailedTransactionsDetails(chargeInfo, SystemCodes.CMB_ALM_DAO, ex.getMessage()));
					}
				}
				return list;
			}
		  });
	}
	  
	@Override
	public List<ChargeAdjustmentInfo> applyChargesAndAdjustmentsToAccountWithTax(final List<ChargeAdjustmentWithTaxInfo> chargeInfoList, String sessionId) throws ApplicationException {
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<List<ChargeAdjustmentInfo>>() {			  
			  
			@Override
			public List<ChargeAdjustmentInfo> doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				List<ChargeAdjustmentInfo> list = new ArrayList<ChargeAdjustmentInfo>();
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
				for (ChargeAdjustmentWithTaxInfo chargeTaxInfo : chargeInfoList) {
					CreateChargeAdjustWithTaxInfo createChargeAdjustWithTaxInfo = new CreateChargeAdjustWithTaxInfo();
					ChargeAdjustmentInfo chargeInfo = chargeTaxInfo.getChargeAdjustmentInfo();
					try {
						// Set BanPK (which also retrieves the BAN)			
						updateBanConv.setBanPK(chargeInfo.getBan());
						createChargeAdjustWithTaxInfo = ChargeAndAdjustmentMapper.mapToAmdocsCreateChargeAdjustWithTaxInfo(chargeTaxInfo);
						ChargeAndAdjDetailInfo chargeAndAdjDetailInfo = updateBanConv.applyChargeAndAdjustWithTax(createChargeAdjustWithTaxInfo);						  
						list.add(ChargeAndAdjustmentMapper.mapToTelusChargeAdjustmentInfo(chargeAndAdjDetailInfo, chargeInfo));
						LOGGER.debug("In loop... chargeSeqNo is " + chargeAndAdjDetailInfo.chargeDetailInfo.chargeSeqNo + " ... adjustmentId is " + 
						chargeAndAdjDetailInfo.adjustDetailInfo.adjustmentId);
					} catch (ValidateException vex) {
						list.add(ChargeAndAdjustmentMapper.getFailedTransactionsDetails(chargeInfo, "KB-"+String.valueOf(vex.getErrorInd()), vex.getErrorMsg()));
					} catch (Exception ex) {
						list.add(ChargeAndAdjustmentMapper.getFailedTransactionsDetails(chargeInfo, SystemCodes.CMB_ALM_DAO, ex.getMessage()));
					}
				}
				return list;
			}
		});
	}
	  
	@Override
	public List<ChargeAdjustmentInfo> applyChargesAndAdjustmentsToAccountForSubscriberWithTax(final List<ChargeAdjustmentWithTaxInfo> chargeInfoList, String sessionId) 
			throws ApplicationException {		  
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<List<ChargeAdjustmentInfo>>() {			
			  
			@Override
			public List<ChargeAdjustmentInfo> doInTransaction(AmdocsTransactionContext transactionContext)  throws Exception {
				List<ChargeAdjustmentInfo> list = new ArrayList<ChargeAdjustmentInfo>();				  
				for (ChargeAdjustmentWithTaxInfo chargeWithTaxInfo : chargeInfoList)  {				
					ChargeAdjustmentInfo chargeInfo = chargeWithTaxInfo.getChargeAdjustmentInfo();	  
					try {						  
						CreateChargeAdjustWithTaxInfo createChargeAdjustWithTaxInfo  = ChargeAndAdjustmentMapper.mapToAmdocsCreateChargeAdjustWithTaxInfo(chargeWithTaxInfo);						  
						UpdateProductConv updateProductConv = null;						  
						if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS)) {
							updateProductConv = transactionContext.createBean(UpdateCellularConv.class);
						} else if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
							updateProductConv = transactionContext.createBean(UpdateIdenConv.class);
						} else if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PAGER)) {
							updateProductConv = transactionContext.createBean(UpdatePagerConv.class);
						} else if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_TANGO)) {
							updateProductConv = transactionContext.createBean(UpdateTangoConv.class);
						} else if (chargeInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_CDPD)) {
							updateProductConv = transactionContext.createBean(UpdateCdpdConv.class);
						} else {
							throw new ApplicationException(ErrorCodes.GENERIC_THROWABLE_ERROR_CODE, "Invalid Product Type : " + chargeInfo.getProductType(), "");
						}					
						updateProductConv.setProductPK(chargeInfo.getBan(), chargeInfo.getSubscriberId());
						ChargeAndAdjDetailInfo chargeAndAdjDetailInfo  =  updateProductConv.applyChargeAndAdjustWithTax(createChargeAdjustWithTaxInfo);
						list.add(ChargeAndAdjustmentMapper.mapToTelusChargeAdjustmentInfo(chargeAndAdjDetailInfo, chargeInfo));						
						LOGGER.debug("In loop... chargeSeqNo is " + chargeAndAdjDetailInfo.chargeDetailInfo.chargeSeqNo + " ... adjustmentId is " + 
							chargeAndAdjDetailInfo.adjustDetailInfo.adjustmentId);						
					} catch (ValidateException vex){
						list.add(ChargeAndAdjustmentMapper.getFailedTransactionsDetails(chargeInfo, "KB-"+String.valueOf(vex.getErrorInd()),vex.getErrorMsg()));
					} catch (Exception ex){
						list.add(ChargeAndAdjustmentMapper.getFailedTransactionsDetails(chargeInfo, SystemCodes.CMB_ALM_DAO, ex.getMessage()));
					}
				}
				return list;
			}
		});
	}
	
}
