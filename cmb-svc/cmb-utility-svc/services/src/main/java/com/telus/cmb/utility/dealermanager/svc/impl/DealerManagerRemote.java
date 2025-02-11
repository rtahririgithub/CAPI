package com.telus.cmb.utility.dealermanager.svc.impl;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.EJBObject;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.CPMSDealerInfo;
import com.telus.eas.utility.info.DealerInfo;
import com.telus.eas.utility.info.SalesRepInfo;

public interface DealerManagerRemote extends EJBObject {

	String openSession(String userId, String password, String applicationId) throws ApplicationException, RemoteException;
	
	void addDealer(DealerInfo dealerInfo, String sessionId) throws ApplicationException, RemoteException; 

	void expireDealer(String dealerCode, Date endDate, String sessionId) throws ApplicationException,RemoteException;
	
	void unexpireDealer(String dealerCode, String sessionId) throws ApplicationException, RemoteException;

	void changeDealerName(String dealerCode, String dealerName, String sessionId) throws ApplicationException, RemoteException;

	void addSalesperson(SalesRepInfo salesRepInfo, String sessionId) throws ApplicationException, RemoteException;

	void expireSalesperson(String dealerCode, String salesCode,
			Date endDate, String sessionId) throws ApplicationException, RemoteException;
	
	void unexpireSalesperson(String dealerCode, String salesCode, String sessionId) throws ApplicationException, RemoteException;


	void changeSalespersonName(String dealerCode, String salesCode,
			String salesName, String sessionId) throws ApplicationException, RemoteException;


	void transferSalesperson(SalesRepInfo salesInfo, String newDealerCode,
			Date transferDate, String sessionId) throws ApplicationException, RemoteException;
	
	void validUser(String pChannelCode, String pUserCode, String pPassword)throws ApplicationException, RemoteException;
	 
	void changeUserPassword(String channelCode,String userCode,String oldPassword,
			 String newPassword) throws ApplicationException, RemoteException;
	 
	void resetUserPassword(String channelCode,String userCode,String newPassword)   
	 	throws ApplicationException, RemoteException;
	
	CPMSDealerInfo getCPMSDealerInfo(String pChannelCode, String pUserCode) throws ApplicationException, RemoteException;

	
	CPMSDealerInfo getCPMSDealerByKBDealerCode(String pKBDealerCode,
			 String pKBSalesRepCode)  throws ApplicationException, RemoteException;

	
	CPMSDealerInfo getCPMSDealerByLocationTelephoneNumber(String pLocationTelephoneNumber)  
 		throws ApplicationException, RemoteException;
	
	long[] getChnlOrgAssociation(long chnlOrgId, String associateReasonCd)
		throws ApplicationException, RemoteException;

	long[] getChnlOrgAssociationSAPSoldToParty(long chnlOrgId) throws ApplicationException, RemoteException;
	
}
