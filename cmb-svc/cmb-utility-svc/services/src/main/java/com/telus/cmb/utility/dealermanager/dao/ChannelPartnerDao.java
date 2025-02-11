package com.telus.cmb.utility.dealermanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.CPMSDealerInfo;

public interface ChannelPartnerDao {
	CPMSDealerInfo getUserInformation(String channelCode, String userCode)throws   ApplicationException;
	
	CPMSDealerInfo getUserInformation2(String channelCode, String userCode) throws   ApplicationException;
	
	CPMSDealerInfo getCPMSDealerByKBDealerCode(String pKBDealerCode, 
			String pKBSalesRepCode)throws   ApplicationException;
	
	CPMSDealerInfo getCPMSDealerByLocationTelephoneNumber(String pLocationTelephoneNumber) 
			throws ApplicationException;
	
	long[] getChnlOrgAssociation(long chnlOrgId, String associateReasonCd)throws ApplicationException;
	
	boolean validUser(String channelCode,String userCode, String password)throws   ApplicationException;
	
	boolean changeUserPassword(String channelCode,String userCode, 
			String oldPassword, String newPassword) throws   ApplicationException;
	
	boolean resetUserPassword(String channelCode,String userCode, 
			String newPassword)throws   ApplicationException;

}
