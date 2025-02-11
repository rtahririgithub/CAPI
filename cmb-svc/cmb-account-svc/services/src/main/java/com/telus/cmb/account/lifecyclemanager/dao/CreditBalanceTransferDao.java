package com.telus.cmb.account.lifecyclemanager.dao;

import java.util.List;
import com.telus.api.ApplicationException;
import com.telus.eas.account.info.CreditBalanceTransferInfo;

public interface CreditBalanceTransferDao {

	public void createCreditBalanceTransferRequest(int sourceBan ,int targetBan,String sessionId) throws ApplicationException;
	public void cancelCreditBalanceTransferRequest(int ban,String sessionId)throws ApplicationException;
    public List<CreditBalanceTransferInfo> getCreditBalanceTransferRequestList(int ban,String sessionId) throws ApplicationException;
	
}
