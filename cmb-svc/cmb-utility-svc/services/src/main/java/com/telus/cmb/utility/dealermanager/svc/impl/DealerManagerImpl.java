package com.telus.cmb.utility.dealermanager.svc.impl;

import java.sql.SQLException;
import java.util.Date;

import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.dao.amdocs.AmdocsSessionManager;
import com.telus.cmb.common.dao.testpoint.DataSourceTestPointDao;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.utility.dealermanager.dao.ChannelPartnerDao;
import com.telus.cmb.utility.dealermanager.dao.DealerManagerDao;
import com.telus.cmb.utility.dealermanager.svc.DealerManager;
import com.telus.cmb.utility.dealermanager.svc.DealerManagerTestPoint;
import com.telus.eas.equipment.info.CPMSDealerInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.utility.info.DealerInfo;
import com.telus.eas.utility.info.SalesRepInfo;
import com.telus.framework.config.ConfigContext;


@Stateless(name="DealerManager", mappedName="DealerManager")
@Remote({DealerManager.class, DealerManagerTestPoint.class})
@RemoteHome(DealerManagerHome.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)

@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

public class DealerManagerImpl implements DealerManager, DealerManagerTestPoint {
	public static final String CHNL_ORG_ASSOC_REASON_CD_SAP_SOLD_TO_PARTY = "SP";
	private static final Logger LOGGER = Logger.getLogger(DealerManagerImpl.class);

	@Autowired
	private DealerManagerDao dealerManagerDao;
	
	@Autowired
	private ChannelPartnerDao channelPartnerDao;
	
	@Autowired
	private AmdocsSessionManager amdocsSessionManager;
	
	@Autowired
	@Qualifier("dmTestPointDao")
	private DataSourceTestPointDao testPointDao;
	
	public AmdocsSessionManager getAmdocsSessionManager() {
		return amdocsSessionManager;
	}

	public void setAmdocsSessionManager(AmdocsSessionManager amdocsSessionManager) {
		this.amdocsSessionManager = amdocsSessionManager;
	}

	public void setDealerManagerDao(DealerManagerDao dealerManagerDao) {
		this.dealerManagerDao = dealerManagerDao;
	}

	public void setChannelPartnerDao(ChannelPartnerDao channelPartnerDao) {
		this.channelPartnerDao = channelPartnerDao;
	}

	public DealerManagerDao getDealerManagerDao() {
		return dealerManagerDao;
	}

	public ChannelPartnerDao getChannelPartnerDao() {
		return channelPartnerDao;
	}


	@Override
	public String openSession(String userId, String password, String applicationId) throws ApplicationException {
		ClientIdentity identity = new ClientIdentity(userId, password, applicationId);
		return amdocsSessionManager.openSession(identity);
	}
	
	@Override
	public void addDealer(DealerInfo dealerInfo, String sessionId) throws ApplicationException {
		dealerManagerDao.addDealer(dealerInfo, sessionId);
	}

	@Override
	public void addSalesperson(SalesRepInfo salesRepInfo, String sessionId)	throws ApplicationException {
		dealerManagerDao.addSalesperson(salesRepInfo, sessionId);
	}

	@Override
	public void changeDealerName(String dealerCode, String dealerName,
			String sessionId)throws ApplicationException {
		dealerManagerDao.changeDealerName(dealerCode, dealerName, sessionId);
	}

	@Override
	public void changeSalespersonName(String dealerCode, String salesCode,
			String salesName, String sessionId) throws ApplicationException {
		dealerManagerDao.changeSalespersonName(dealerCode, salesCode, salesName, sessionId);
	}
	
	@Override
	public void expireDealer(String dealerCode, Date endDate, 
			String sessionId)	throws ApplicationException {
		dealerManagerDao.expireDealer(dealerCode, endDate, sessionId);
	}

	@Override
	public void expireSalesperson(String dealerCode, String salesCode,
			Date endDate, String sessionId) throws ApplicationException {
		dealerManagerDao.expireSalesperson(dealerCode, salesCode, endDate, sessionId);
	}
	
	@Override
	public void unexpireDealer(String dealerCode, String sessionId) throws ApplicationException {
		dealerManagerDao.unexpireDealer(dealerCode, sessionId);
	}

	@Override
	public void unexpireSalesperson(String dealerCode, String salesCode, 
			String sessionId)throws ApplicationException {
		dealerManagerDao.unexpireSalesperson(dealerCode, salesCode, sessionId);
	}
	
	@Override
	public void transferSalesperson(SalesRepInfo salesInfo,String newDealerCode, 
			Date transferDate, String sessionId)throws ApplicationException {
		dealerManagerDao.transferSalesperson(salesInfo, newDealerCode, transferDate, sessionId);
	}

	@Override
	public void resetUserPassword(String channelCode, String userCode,
			String newPassword) throws ApplicationException {
		if (channelPartnerDao.resetUserPassword(channelCode, userCode, newPassword)) {
			LOGGER.error("Channel Password is not valid");
			throw new ApplicationException (SystemCodes.CMB_DLRMGR_EJB, "VAL10012", "Channel Password is not valid", "");
		}	
	}
	
	@Override
	public void changeUserPassword(String channelCode, String userCode,
			String oldPassword, String newPassword) throws ApplicationException {
		if (channelPartnerDao.changeUserPassword(channelCode, userCode, oldPassword, newPassword)) {
			LOGGER.error("Channel Password is not valid");
			throw new ApplicationException (SystemCodes.CMB_DLRMGR_EJB, "VAL10012", "Channel Password is not valid", "");
		}	
	}

	@Override
	public void validUser(String pChannelCode, String pUserCode,String pPassword) throws ApplicationException {
		if (channelPartnerDao.validUser(pChannelCode, pUserCode, pPassword)) {
			LOGGER.error("Channel Password is not valid");
			throw new ApplicationException (SystemCodes.CMB_DLRMGR_EJB, "VAL10012", "Channel Password is not valid", "");
		}	
	}
	
	@Override
	public CPMSDealerInfo getCPMSDealerByKBDealerCode(String pKBDealerCode, String pKBSalesRepCode) throws ApplicationException {
		CPMSDealerInfo pCPMSDealerInfo = channelPartnerDao.getCPMSDealerByKBDealerCode(pKBDealerCode, pKBSalesRepCode);
		if (pCPMSDealerInfo == null) {
			LOGGER.error("Knowbility Dealer Sales Rep Codes can not be mapped to CPMS Dealer Codes");
			throw new ApplicationException (SystemCodes.CMB_DLRMGR_EJB, "VAL10017", "Knowbility Dealer Sales Rep Codes can not be mapped to CPMS Dealer Codes", "");
		}
		return pCPMSDealerInfo;
	}

	@Override
	public CPMSDealerInfo getCPMSDealerByLocationTelephoneNumber(String pLocationTelephoneNumber) throws ApplicationException {
		CPMSDealerInfo pCPMSDealerInfo = channelPartnerDao.getCPMSDealerByLocationTelephoneNumber(pLocationTelephoneNumber);
		if (pCPMSDealerInfo == null) {
			LOGGER.error("Location telephone number can not be mapped to CPMS Outlet");
			throw new ApplicationException (SystemCodes.CMB_DLRMGR_EJB, "VAL10017", "Location telephone number can not be mapped to CPMS Outlet", "");
		}
		return pCPMSDealerInfo;
	}

	@Override
	public CPMSDealerInfo getCPMSDealerInfo(String pChannelCode,String pUserCode) throws ApplicationException {
		
		if (pChannelCode == null || pChannelCode.trim().isEmpty() || pUserCode == null || pUserCode.trim().isEmpty()) {
			throw new ApplicationException(SystemCodes.CMB_DLRMGR_EJB, "VAL10011", "Invalid Channel Code or User Code channelCode=["+pChannelCode+"] userCode=["+pUserCode+"]", "");
		}

		CPMSDealerInfo pCPMSDealerInfo = null;
		try {
			pCPMSDealerInfo = channelPartnerDao.getUserInformation2(pChannelCode, pUserCode);
		} catch (DataAccessException daex) {
			Throwable cause = daex.getMostSpecificCause();

			if (cause instanceof SQLException) {
				SQLException sqlex = (SQLException) cause;
				String parameterInfoString = "channelCode=["+pChannelCode+"] userCode=["+pUserCode+"]";
				if (sqlex.getErrorCode() == 20101) {
					throw new ApplicationException(SystemCodes.CMB_DLRMGR_EJB, "VAL10011", "Invalid Channel Code or User Code "+parameterInfoString, "");
				}else if (sqlex.getErrorCode() == 20000) {
					throw new ApplicationException(SystemCodes.CMB_DLRMGR_EJB, "VAL10011", "No channel_organization record found. "+parameterInfoString, "");
				}else if (sqlex.getErrorCode() == 20001) {
					throw new ApplicationException(SystemCodes.CMB_DLRMGR_EJB, "VAL10011", "No sales rep or outlet record found. "+parameterInfoString, "");
				}else if (sqlex.getErrorCode() == 20002) {
					throw new ApplicationException(SystemCodes.CMB_DLRMGR_EJB, "VAL10011", "No sales_representative record found. "+parameterInfoString, "");
				}
			}			
			throw daex;
		}	
		return pCPMSDealerInfo;
	}

	@Override
	public long[] getChnlOrgAssociation(long chnlOrgId, String associateReasonCd)throws ApplicationException {
		long[] chnlOrgIds = channelPartnerDao.getChnlOrgAssociation(chnlOrgId, associateReasonCd);
		if (chnlOrgIds == null) {
			LOGGER.error("getChnlOrgAssociation cannot return null");
			throw new ApplicationException(SystemCodes.CMB_DLRMGR_EJB, "","getChnlOrgAssociation cannot return null", "");
		}
		return chnlOrgIds;
	}
	
	@Override
	public long[] getChnlOrgAssociationSAPSoldToParty(long chnlOrgId)throws ApplicationException {
		return getChnlOrgAssociation(chnlOrgId, CHNL_ORG_ASSOC_REASON_CD_SAP_SOLD_TO_PARTY);
	}

	@Override
	public TestPointResultInfo testDistDataSource() {
		return testPointDao.testDistDataSource();
	}

	@Override
	public String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}
}
