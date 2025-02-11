package com.telus.cmb.utility.dealermanager.dao.impl;

import java.util.Date;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.SalespersonInfo;
import amdocs.APILink.datatypes.TransferSalespersonInfo;
import amdocs.APILink.sessions.interfaces.GenericServices;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.utility.dealermanager.dao.DealerManagerDao;
import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.DealerInfo;
import com.telus.eas.utility.info.SalesRepInfo;



public class DealerManagerDaoImpl extends AmdocsDaoSupport implements DealerManagerDao {
	private static final Logger LOGGER = Logger.getLogger(DealerManagerDaoImpl.class);
	
	@Override
	public void addDealer(final DealerInfo dealerInfo, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {
			    amdocs.APILink.datatypes.DealerInfo amdocsDealerInfo = new amdocs.APILink.datatypes.DealerInfo();

		        // populate Amdocs Info classes
		        amdocsDealerInfo = (amdocs.APILink.datatypes.DealerInfo)mapTelusRefDataToAmdocs(dealerInfo,amdocsDealerInfo);
				LOGGER.debug("Executing addDealer - start..." );
				GenericServices amdocsGenericServices = transactionContext.createBean(GenericServices.class);
				if (amdocsGenericServices != null)
		            amdocsGenericServices.addDealer(amdocsDealerInfo);
		        LOGGER.debug("Executing addDealer - end..." );
				return null;
			}
		});
	}

	@Override
	public void addSalesperson(final SalesRepInfo salesRepInfo, String sessionId)	throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {
				SalespersonInfo amdocsSalesRepInfo = new SalespersonInfo();
		        amdocsSalesRepInfo = (SalespersonInfo)mapTelusRefDataToAmdocs(salesRepInfo,amdocsSalesRepInfo);
		        LOGGER.debug("Executing addSalesperson - start..." );
				GenericServices amdocsGenericServices = transactionContext.createBean(GenericServices.class);
				if (amdocsGenericServices != null)
		            amdocsGenericServices.addSalesperson(amdocsSalesRepInfo);
		        LOGGER.debug("Executing addSalesperson - end..." );
		        return null;
			}
		});
	}

	@Override
	public void changeDealerName(final String dealerCode, final String dealerName, String sessionId)
			throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
		        LOGGER.debug("Executing changeDealerName - start..." );
				GenericServices amdocsGenericServices = transactionContext.createBean(GenericServices.class);
		        if (amdocsGenericServices != null)
		        	// Must remove double quote dealer name after PROD00027288 fixed
		            amdocsGenericServices.changeDealerName( dealerCode,  doubleQuote(dealerName));
		        LOGGER.debug("Executing changeDealerName - end..." );
				return null;
			}
		});
	}

	@Override
	public void changeSalespersonName(final String dealerCode, final String salesCode,
			final String salesName, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				LOGGER.debug("Executing changeSalespersonName - start..." );
				GenericServices amdocsGenericServices = transactionContext.createBean(GenericServices.class);
				if (amdocsGenericServices != null)
					// Must remove double quote sales rep name after PROD00027288 fixed
					amdocsGenericServices.changeSalespersonName(dealerCode, salesCode, doubleQuote(salesName));
				LOGGER.debug("Executing changeSalespersonName - end..." );
				return null;
			}
		});
	}

	@Override
	public void expireDealer(final String dealerCode, final Date endDate, String sessionId)	throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {
				LOGGER.debug("Executing expireDealer - start..." );
				GenericServices amdocsGenericServices = transactionContext.createBean(GenericServices.class);
				if (amdocsGenericServices != null)
					amdocsGenericServices.expireDealer(dealerCode, endDate);
				LOGGER.debug("Executing expireDealer - end..." );
				return null;
			}
		} );
	}

	@Override
	public void expireSalesperson(final String dealerCode, final String salesCode,
			final Date endDate, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)	throws Exception {
				LOGGER.debug("Executing expireSalesperson - start..." );
				GenericServices amdocsGenericServices = transactionContext.createBean(GenericServices.class);
				if (amdocsGenericServices != null)
					amdocsGenericServices.expireSalesperson(dealerCode, salesCode, endDate);
				LOGGER.debug("Executing expireSalesperson - end..." );
				return null;
			}
		} );
	}

	@Override
	public void transferSalesperson(final SalesRepInfo salesInfo, final String newDealerCode, 
			final Date transferDate, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {
			    TransferSalespersonInfo amdocsTransferInfo = new TransferSalespersonInfo();
		        amdocsTransferInfo = (TransferSalespersonInfo)mapTelusRefDataToAmdocs(salesInfo, amdocsTransferInfo);
		        amdocsTransferInfo.newDealerCode = newDealerCode;
		        amdocsTransferInfo.newEffectiveDate = transferDate;

				LOGGER.debug("Executing transferSalesperson - start..." );
				GenericServices amdocsGenericServices = transactionContext.createBean(GenericServices.class);
		        if (amdocsGenericServices != null)
		            amdocsGenericServices.transferSalesperson(amdocsTransferInfo);
				LOGGER.debug("Executing transferSalesperson - end..." );
				return null;
			}
		});
	}

	@Override
	public void unexpireDealer(final String dealerCode, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {
				LOGGER.debug("Executing unexpireDealer - start..." );
				GenericServices amdocsGenericServices = transactionContext.createBean(GenericServices.class);
				if (amdocsGenericServices != null)
					amdocsGenericServices.unexpireDealer(dealerCode);
				LOGGER.debug("Executing unexpireDealer - end..." );
				return null;
			}
		});
	}

	@Override
	public void unexpireSalesperson(final String dealerCode, final String salesCode, String sessionId)
			throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {
				LOGGER.debug("Executing unexpireSalesperson - start..." );
				GenericServices amdocsGenericServices = transactionContext.createBean(GenericServices.class);
		        if (amdocsGenericServices != null)
		            amdocsGenericServices.unexpireSalesperson(dealerCode, salesCode);
				LOGGER.debug("Executing unexpireSalesperson - end..." );
		        return null;
			}
		});
	}

	/**
	 * Maps a single Telus data structue to a Amdocs datatype
	 * @param   Info    source - Telus info class
	 * @param   Object  target - Amdocs info class
	 * @return  Object  Amdocs info class
	 */
	private Object mapTelusRefDataToAmdocs(Info pSourceTelusInfoClass, 
			Object pTargetAmdocsInfoClass)  throws ApplicationException {

		LOGGER.debug("Processing Input: " + pSourceTelusInfoClass.getClass().getName());
		LOGGER.debug("          Output: " + pTargetAmdocsInfoClass.getClass().getName());

		// Telus API Info classes
		com.telus.eas.utility.info.DealerInfo  dealerInfo = new com.telus.eas.utility.info.DealerInfo();
		com.telus.eas.utility.info.SalesRepInfo salesRepInfo = new com.telus.eas.utility.info.SalesRepInfo();
		// Amdocs API Info classes
		amdocs.APILink.datatypes.DealerInfo amdocsDealerInfo = new amdocs.APILink.datatypes.DealerInfo();
		amdocs.APILink.datatypes.SalespersonInfo amdocsSalesRepInfo = new amdocs.APILink.datatypes.SalespersonInfo();

		if ((pSourceTelusInfoClass instanceof com.telus.eas.utility.info.DealerInfo)&&
				pTargetAmdocsInfoClass instanceof amdocs.APILink.datatypes.DealerInfo) {

			dealerInfo = (com.telus.eas.utility.info.DealerInfo)pSourceTelusInfoClass;
			amdocsDealerInfo = pTargetAmdocsInfoClass != null ? (amdocs.APILink.datatypes.DealerInfo)pTargetAmdocsInfoClass : new amdocs.APILink.datatypes.DealerInfo();
			amdocsDealerInfo.dealerCode = dealerInfo.getCode();
			// Must remove double quote dealer name after PROD00027288 fixed
			amdocsDealerInfo.dealerName = doubleQuote(dealerInfo.getName());
			amdocsDealerInfo.startDate =  dealerInfo.getEffectiveDate();
			amdocsDealerInfo.endDate =    dealerInfo.getExpiryDate();
			amdocsDealerInfo.numberLocationCode = dealerInfo.getNumberLocationCD();
			amdocsDealerInfo.departmentCode = dealerInfo.getDepartmentCode();

			return amdocsDealerInfo;
		}

		if ((pSourceTelusInfoClass instanceof com.telus.eas.utility.info.SalesRepInfo)&&
				(pTargetAmdocsInfoClass instanceof amdocs.APILink.datatypes.SalespersonInfo ||
						pTargetAmdocsInfoClass instanceof amdocs.APILink.datatypes.TransferSalespersonInfo))  {

			salesRepInfo = (com.telus.eas.utility.info.SalesRepInfo)pSourceTelusInfoClass;
			if (pTargetAmdocsInfoClass instanceof amdocs.APILink.datatypes.SalespersonInfo) {
				amdocsSalesRepInfo = pTargetAmdocsInfoClass != null ? (amdocs.APILink.datatypes.SalespersonInfo)pTargetAmdocsInfoClass : new amdocs.APILink.datatypes.SalespersonInfo();
			}
			if (pTargetAmdocsInfoClass instanceof amdocs.APILink.datatypes.TransferSalespersonInfo) {
				amdocsSalesRepInfo = pTargetAmdocsInfoClass != null ? (amdocs.APILink.datatypes.TransferSalespersonInfo)pTargetAmdocsInfoClass : new amdocs.APILink.datatypes.TransferSalespersonInfo();
			}
			amdocsSalesRepInfo.dealerCode = salesRepInfo.getDealerCode();
			amdocsSalesRepInfo.salesCode =  salesRepInfo.getCode();
			// Must remove double quote sales rep name after PROD00027288 fixed
			amdocsSalesRepInfo.salesName = doubleQuote(salesRepInfo.getName());
			amdocsSalesRepInfo.effectiveDate = salesRepInfo.getEffectiveDate();
			amdocsSalesRepInfo.expirationDate = salesRepInfo.getExpiryDate();
			//amdocsSalesRepInfo.salesStatus = ' ';
			return amdocsSalesRepInfo;
		}

		throw new ApplicationException(SystemCodes.CMB_DLRMGR_DAO, "APP10002","No Mapping performed.", "");

	}
	
	/**
	 * Replace all single quotes found in an input string by 2 single quotes.
	 * @param   inStr input string
	 * @return  outStr output string
	 */
	private String doubleQuote(String inStr)	{
		if (inStr != null) {
			String outStr = inStr.replaceAll("'", "''");
			if (outStr != null)
				return outStr;
		}
		return inStr;
	}	
}
