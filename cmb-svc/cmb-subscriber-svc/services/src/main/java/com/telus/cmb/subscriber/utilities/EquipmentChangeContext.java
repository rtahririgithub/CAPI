package com.telus.cmb.subscriber.utilities;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.api.account.Account;
import com.telus.cmb.subscriber.bo.AccountBo;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.subscriber.info.BaseChangeInfo;

public class EquipmentChangeContext extends BaseChangeContext<BaseChangeInfo> {

	public EquipmentChangeContext(BaseChangeInfo changeInfo) throws SystemException, ApplicationException {
		super(changeInfo);
	}
	
	@Override
	public AccountBo getCurrentAccount() throws ApplicationException {
        if (currentAccount == null) {
            BaseChangeInfo accountChangeInfo = getChangeInfo();
            if (accountChangeInfo.getBan() != 0) {
                AccountInfo account = getAccountInformationHelper().retrieveAccountByBan(accountChangeInfo.getBan(), Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
                currentAccount = new AccountBo(account, this);
            }
        }
        return currentAccount;
    }

}
