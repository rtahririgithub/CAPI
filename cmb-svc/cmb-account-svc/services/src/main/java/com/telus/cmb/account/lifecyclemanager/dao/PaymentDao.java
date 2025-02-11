package com.telus.cmb.account.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.api.account.PaymentTransfer;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;

public interface PaymentDao {
	void applyPaymentToAccount(PaymentInfo pPaymentInfo,String sessionId) throws ApplicationException;
	
	void changePaymentMethodToRegular(int pBan, String sessionId) throws ApplicationException;
	
	PaymentMethodInfo updatePaymentMethod(int pBan,PaymentMethodInfo pPaymentMethodInfo, 
			String sessionId) throws ApplicationException;
	void updateTransferPayment(int ban, int seqNo, PaymentTransfer[] paymentTransfers, boolean allowOverPayment, String memonText,String sessionId)throws ApplicationException;
	void refundPaymentToAccount( int ban, int paymentSeq, String reasonCode, String memoText, boolean isManual, String authorizationCode,String sessionId)throws ApplicationException;
}
