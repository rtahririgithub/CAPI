package com.telus.cmb.account.lifecyclemanager.dao.impl;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.ApplyPaymentDetailsInfo;
import amdocs.APILink.datatypes.CheckDetailsInfo;
import amdocs.APILink.datatypes.CreditCardDetailsInfo;
import amdocs.APILink.datatypes.PaymentRefundInfo;
import amdocs.APILink.datatypes.PaymentTransferBanInfo;
import amdocs.APILink.datatypes.PaymentTransferInfo;
import amdocs.APILink.sessions.interfaces.GenericServices;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;

import com.telus.api.ApplicationException;
import com.telus.api.account.PaymentMethod;
import com.telus.api.account.PaymentTransfer;
import com.telus.cmb.account.lifecyclemanager.dao.PaymentDao;
import com.telus.cmb.account.utilities.TelusToAmdocsAccountMapper;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.logging.Sensitive;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.common.util.TelusConstants;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;

public class PaymentDaoImpl extends AmdocsDaoSupport implements PaymentDao {
	
	private static final Logger LOGGER = Logger.getLogger(PaymentDaoImpl.class);	

	@Override
	public void applyPaymentToAccount(@Sensitive final PaymentInfo pPaymentInfo, final String sessionId)throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {
				ApplyPaymentDetailsInfo amdocsApplyPaymentDetailsInfo = new ApplyPaymentDetailsInfo();
				CreditCardDetailsInfo amdocsCreditCardDetailsInfo = new CreditCardDetailsInfo();
				CheckDetailsInfo amdocsCheckDetailsInfo = new CheckDetailsInfo();
					// map telus info class to amdocs class
				amdocsApplyPaymentDetailsInfo = (ApplyPaymentDetailsInfo) TelusToAmdocsAccountMapper.mapTelusToAmdocs(
							pPaymentInfo, amdocsApplyPaymentDetailsInfo);

				GenericServices genericServices = transactionContext.createBean(GenericServices.class);

				// apply payment to account
				if (pPaymentInfo.getPaymentMethod().equals(TelusConstants.PAYMENT_METHOD_CREDIT_CARD)) {
					amdocsCreditCardDetailsInfo = (CreditCardDetailsInfo) TelusToAmdocsAccountMapper.mapTelusToAmdocs(
							pPaymentInfo.getCreditCardInfo(), amdocsCreditCardDetailsInfo);

					LOGGER.debug("Excecuting applyPaymentToAccount() - start..." );
					genericServices.applyPaymentToAccount(amdocsApplyPaymentDetailsInfo,amdocsCreditCardDetailsInfo);
					LOGGER.debug("Excecuting applyPaymentToAccount() - end..." );
				} else {
					amdocsCheckDetailsInfo = (CheckDetailsInfo) TelusToAmdocsAccountMapper.mapTelusToAmdocs(
							pPaymentInfo.getChequeInfo(), amdocsCheckDetailsInfo);
					LOGGER.debug("Excecuting applyPaymentToAccount() - start..." );
					genericServices.applyPaymentToAccount(amdocsApplyPaymentDetailsInfo, amdocsCheckDetailsInfo);
					LOGGER.debug("Excecuting applyPaymentToAccount() - end..." );
				}

				return null;
			}
		}); 
	}

	@Override
	public void changePaymentMethodToRegular(final int pBan, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {
				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(pBan);

				updateBanConv.changePaymentInfo();
				return null;
			}
		});
		
	}

	@Override
	public PaymentMethodInfo updatePaymentMethod(final int pBan, @Sensitive final PaymentMethodInfo pPaymentMethodInfo, 
			String sessionId) throws ApplicationException {
		 return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<PaymentMethodInfo>() {
			
			@Override
			public PaymentMethodInfo doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				CheckDetailsInfo amdocsCheckDetailsInfo = new CheckDetailsInfo();
				CreditCardDetailsInfo amdocsCreditCardDetailsInfo = new CreditCardDetailsInfo();
				PaymentMethodInfo outPaymentMethodInfo=pPaymentMethodInfo;
				//set the initial transaction type to no change.
				outPaymentMethodInfo.setTransactionType( PaymentMethodInfo.PREAUTHORIZED_TRASACTION_TYPE_NOCHANGE );
				String oldPaymentMethod = "";
				String newPaymentMethod = "";

				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(pBan);

				// determine old/new payment method
				oldPaymentMethod = AttributeTranslator.stringFrombyte(updateBanConv.getBillDetailsInfo().autoGenPymType);
				newPaymentMethod = pPaymentMethodInfo.getPaymentMethod();

				if(!(oldPaymentMethod.equals(PaymentMethodInfo.PAYMENT_METHOD_REGULAR))){
					amdocs.APILink.datatypes.PaymentInfo amdocPaymentInfo = updateBanConv.getPaymentInfo();
					if(oldPaymentMethod.equals(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD)){
						outPaymentMethodInfo.setOldPaymentMethodCardNumber(amdocPaymentInfo.creditCardDetailsInfo.ccLastFourDigits);
					}
					else{
						outPaymentMethodInfo.setOldPaymentMethodCardNumber(amdocPaymentInfo.checkDetailsInfo.bankAccountNo);
					}
				}
				
				// 'Regular' - change payment method
				// - only change if method has changed
				// - set status to 'cancelled'
				if (newPaymentMethod.equals(PaymentMethodInfo.PAYMENT_METHOD_REGULAR) && !newPaymentMethod.equals(oldPaymentMethod)) {
					if (oldPaymentMethod.equals(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT)) {
						outPaymentMethodInfo.setTransactionType(PaymentMethodInfo.PREAUTHORIZED_TRASACTION_TYPE_REMOVAL);
						outPaymentMethodInfo.setOldPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT);
						amdocsCheckDetailsInfo = (CheckDetailsInfo) TelusToAmdocsAccountMapper.mapTelusToAmdocs(
								pPaymentMethodInfo, amdocsCheckDetailsInfo);
						amdocsCheckDetailsInfo.directDebitStatus = PaymentMethodInfo.DIRECT_DEBIT_STATUS_CANCELED;
			        	LOGGER.debug("Excecuting changePaymentInfo() to reset to 'Regular'(Check) - start..." );
			        	updateBanConv.changePaymentInfo(amdocsCheckDetailsInfo);
			        	LOGGER.debug("Excecuting changePaymentInfo() - end..." );
					} else {
						outPaymentMethodInfo.setTransactionType(PaymentMethodInfo.PREAUTHORIZED_TRASACTION_TYPE_REMOVAL);
						outPaymentMethodInfo.setOldPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD);
						amdocsCreditCardDetailsInfo = (amdocs.APILink.datatypes.CreditCardDetailsInfo) 
							TelusToAmdocsAccountMapper.mapTelusToAmdocs(pPaymentMethodInfo, amdocsCreditCardDetailsInfo);
						amdocsCreditCardDetailsInfo.directDebitStatus = PaymentMethodInfo.DIRECT_DEBIT_STATUS_CANCELED;
			        	LOGGER.debug("Excecuting changePaymentInfo() to reset to 'Regular'(CC) - start..." );
			        	updateBanConv.changePaymentInfo(amdocsCreditCardDetailsInfo);
			        	LOGGER.debug("Excecuting changePaymentInfo() - end..." );
					}
				}

				// 'PAP' - change payment method
				// - change bank info
				// - set status to 'Active' if method has changed
				if (newPaymentMethod.equals(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT)) {
					amdocsCheckDetailsInfo = (CheckDetailsInfo) TelusToAmdocsAccountMapper.mapTelusToAmdocs(pPaymentMethodInfo, amdocsCheckDetailsInfo);
					if (!newPaymentMethod.equals(oldPaymentMethod)){
						if(oldPaymentMethod.equals(PaymentMethodInfo.PAYMENT_METHOD_REGULAR)) {
							outPaymentMethodInfo.setTransactionType(PaymentMethodInfo.PREAUTHORIZED_TRASACTION_TYPE_NEW);
						} 
						else if(oldPaymentMethod.equals(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD)) {
							outPaymentMethodInfo.setOldPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD);
							outPaymentMethodInfo.setTransactionType(PaymentMethodInfo.PREAUTHORIZED_TRASACTION_TYPE_UPDATE);
						}
						amdocsCheckDetailsInfo.directDebitStatus = PaymentMethodInfo.DIRECT_DEBIT_STATUS_ACTIVE;
					}else{
						outPaymentMethodInfo.setTransactionType(PaymentMethodInfo.PREAUTHORIZED_TRASACTION_TYPE_UPDATE);
						outPaymentMethodInfo.setOldPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT);
					}
					LOGGER.debug("Excecuting changePaymentInfo() for Check - start..." );
					updateBanConv.changePaymentInfo(amdocsCheckDetailsInfo);
					LOGGER.debug("Excecuting changePaymentInfo() for Check - end..." );
				}
				

				// 'PAC' - change payment method
				// - change credit card info
				// - set status to 'Active' if method has changed
				if (newPaymentMethod.equals(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD)) {
					amdocsCreditCardDetailsInfo = (amdocs.APILink.datatypes.CreditCardDetailsInfo) 
					TelusToAmdocsAccountMapper.mapTelusToAmdocs(pPaymentMethodInfo, amdocsCreditCardDetailsInfo);
					if (!newPaymentMethod.equals(oldPaymentMethod)){
						if(oldPaymentMethod.equals(PaymentMethodInfo.PAYMENT_METHOD_REGULAR)){
							outPaymentMethodInfo.setTransactionType(PaymentMethodInfo.PREAUTHORIZED_TRASACTION_TYPE_NEW);
						}
						else if(oldPaymentMethod.equals(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT)){
							outPaymentMethodInfo.setTransactionType(PaymentMethodInfo.PREAUTHORIZED_TRASACTION_TYPE_UPDATE);
							outPaymentMethodInfo.setOldPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT);
						}
							
						amdocsCreditCardDetailsInfo.directDebitStatus = PaymentMethodInfo.DIRECT_DEBIT_STATUS_ACTIVE;
					}else{
						 outPaymentMethodInfo.setTransactionType(PaymentMethodInfo.PREAUTHORIZED_TRASACTION_TYPE_UPDATE);
						 outPaymentMethodInfo.setOldPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD);
					}
					LOGGER.debug("Excecuting changePaymentInfo() for CC - start..." );
					updateBanConv.changePaymentInfo(amdocsCreditCardDetailsInfo);
					LOGGER.debug("Excecuting changePaymentInfo() for CC - end..." );
				}
				

				// 'Regular' payment method with CC info on 'Hold'
				// (used for storing top-up cc for pay&talk subscribers)
				// - change credit card info only, leave status as 'Hold'
				if (newPaymentMethod.equals(PaymentMethod.PAYMENT_METHOD_REGULAR) && oldPaymentMethod.equals(PaymentMethod.PAYMENT_METHOD_REGULAR)
						&& (updateBanConv.getPaymentInfo().creditCardDetailsInfo.directDebitStatus.equals(PaymentMethodInfo.DIRECT_DEBIT_STATUS_HOLD) ||
								(pPaymentMethodInfo.getStatus() != null && pPaymentMethodInfo.getStatus().equals(PaymentMethodInfo.DIRECT_DEBIT_STATUS_HOLD)))) {
					
					amdocsCreditCardDetailsInfo = (amdocs.APILink.datatypes.CreditCardDetailsInfo) 
					TelusToAmdocsAccountMapper.mapTelusToAmdocs(pPaymentMethodInfo, amdocsCreditCardDetailsInfo);

					LOGGER.debug("calling changePaymentInfo() for CC on-hold..." );
					updateBanConv.changePaymentInfo(amdocsCreditCardDetailsInfo);
				}
			
				return outPaymentMethodInfo;
			}
		});		
	}

	@Override
	public void updateTransferPayment(final int ban, final int seqNo,
			final PaymentTransfer[] paymentTransfers, final boolean allowOverPayment,
			final String memonText, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(ban);
			      int size = paymentTransfers != null ? paymentTransfers.length : 0;

			      amdocs.APILink.datatypes.PaymentTransferInfo paymentTransferInfo = new PaymentTransferInfo();
			      paymentTransferInfo.paymentSequenceNumber = seqNo;
			      paymentTransferInfo.memoManualText = memonText;
			      paymentTransferInfo.paymentTransferBanInfo = new PaymentTransferBanInfo[size];

			      for (int i = 0; i < size; i++) {
			        paymentTransferInfo.paymentTransferBanInfo[i] = new PaymentTransferBanInfo();

			        paymentTransferInfo.paymentTransferBanInfo[i].toBan = paymentTransfers[i].getTargetBanId();
			        paymentTransferInfo.paymentTransferBanInfo[i].reasonCode = paymentTransfers[i].getReason();
			        paymentTransferInfo.paymentTransferBanInfo[i].transferAmount = paymentTransfers[i].getAmount();
			      }
			      updateBanConv.transferPayment(paymentTransferInfo, allowOverPayment);
				return null;
			}
		});	
	}

	@Override
	public void refundPaymentToAccount(final int ban, final int paymentSeq,
			final String reasonCode, final String memoText, final boolean isManual,
			final String authorizationCode, String sessionId)
			throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(ban);
				PaymentRefundInfo info = new PaymentRefundInfo();
				info.paymentSequenceNumber=paymentSeq;
				info.reasonCode=reasonCode;
				info.isManualRefund=isManual;
				info.memoManualText=memoText;
				info.creditCardAuthCode=authorizationCode;
				updateBanConv.refundPayment(info);
				return null;
			}
		});	
	}

}
