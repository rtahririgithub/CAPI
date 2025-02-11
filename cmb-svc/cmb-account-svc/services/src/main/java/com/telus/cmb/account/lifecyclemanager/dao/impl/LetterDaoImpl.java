//package com.telus.cmb.account.lifecyclemanager.dao.impl;
//
//import java.util.Date;
//
////import com.telus.api.ApplicationException;
////import com.telus.cmb.account.lifecyclemanager.dao.LetterDao;
////import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
////import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
////import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
////import com.telus.eas.account.info.LMSLetterRequestInfo;
////import com.telus.eas.framework.info.LMSRequestInfo;
//
////import amdocs.APILink.datatypes.LetterRequestInfo;
////import amdocs.APILink.datatypes.ManualVariablesInfo;
////import amdocs.APILink.sessions.interfaces.LetterManagementServices;
//
//@Deprecated
//public class LetterDaoImpl extends AmdocsDaoSupport implements LetterDao {
//
////	@Override
////	@Deprecated
////	public void createManualLetterRequest(final LMSLetterRequestInfo letterRequestInfo, String sessionId )
////	throws ApplicationException {
////		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
////
////			@Override
////			public Object doInTransaction(AmdocsTransactionContext transactionContext)	throws Exception {
////
////				LetterManagementServices letterManagementService = transactionContext.createBean(LetterManagementServices.class);				
////
////				LetterRequestInfo amdocsLetterRequestInfo = new LetterRequestInfo();
////				amdocsLetterRequestInfo.ban = letterRequestInfo.getBanId();
////				amdocsLetterRequestInfo.subscriberNumber = letterRequestInfo.getSubscriberId();
////				amdocsLetterRequestInfo.category = letterRequestInfo.getLetterCategory();
////				amdocsLetterRequestInfo.letterCode = letterRequestInfo.getLetterCode();
////				amdocsLetterRequestInfo.productionDate = letterRequestInfo.getProductionDate() == null ? new Date() : letterRequestInfo.getProductionDate() ;
////				amdocsLetterRequestInfo.productionType = (byte)letterRequestInfo.getProductionType();
////
////				String[] variables = letterRequestInfo.getManualVariableNames();
////				amdocsLetterRequestInfo.manualVariablesInfo = new ManualVariablesInfo[variables.length];
////				for (int i=0; i < variables.length; i++) {
////					amdocsLetterRequestInfo.manualVariablesInfo[i] = new ManualVariablesInfo();
////					amdocsLetterRequestInfo.manualVariablesInfo[i].variableName = variables[i];
////					amdocsLetterRequestInfo.manualVariablesInfo[i].variableValue = letterRequestInfo.getManualVariable(variables[i]);
////				}
////				// create Manual Letter request
////				letterManagementService.createManualLetterRequest(amdocsLetterRequestInfo);
////				return null;
////			}
////		});		
////	}
////
////	@Override
////	@Deprecated
////	public void removeManualLetterRequest(final int banId, final int requestNumber,
////			String sessionId) throws ApplicationException {
////		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
////
////			@Override
////			public Object doInTransaction(AmdocsTransactionContext transactionContext)	throws Exception {
////
////				LetterManagementServices letterManagementService = transactionContext.createBean(LetterManagementServices.class);				
////				letterManagementService.deleteLetterRequest(banId, requestNumber);
////				return null;
////			}
////		});	
////		
////	}
////
////	@Override
////	@Deprecated
////	public void createManualLetterRequest(final LMSRequestInfo lmsRequestInfo,
////			String sessionId) throws ApplicationException {
////		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
////
////			@Override
////			public Object doInTransaction(AmdocsTransactionContext transactionContext)	throws Exception {
////
////				LetterManagementServices letterManagementService = transactionContext.createBean(LetterManagementServices.class);				
////
////				LetterRequestInfo amdocsLetterRequestInfo = new LetterRequestInfo();  
////				amdocsLetterRequestInfo.ban = lmsRequestInfo.getBan();
////			      amdocsLetterRequestInfo.subscriberNumber = lmsRequestInfo.getSubscriberId();
////			      amdocsLetterRequestInfo.category = lmsRequestInfo.getCategory();
////			      amdocsLetterRequestInfo.letterCode = lmsRequestInfo.getLetterCode();
////			      amdocsLetterRequestInfo.productionDate = lmsRequestInfo.getProductionDate() == null ? new Date() : lmsRequestInfo.getProductionDate() ;
////			      amdocsLetterRequestInfo.productionType = lmsRequestInfo.getProductionType();
////			      amdocsLetterRequestInfo.manualVariablesInfo = new ManualVariablesInfo[lmsRequestInfo.getVariables().length];
////			      for (int i=0; i < lmsRequestInfo.getVariables().length; i++) {
////			        amdocsLetterRequestInfo.manualVariablesInfo[i] = new ManualVariablesInfo();
////			        amdocsLetterRequestInfo.manualVariablesInfo[i].variableName = lmsRequestInfo.getVariables()[i].getName();
////			        amdocsLetterRequestInfo.manualVariablesInfo[i].variableValue = lmsRequestInfo.getVariables()[i].getValue();
////			      }
////				// create Manual Letter request
////				letterManagementService.createManualLetterRequest(amdocsLetterRequestInfo);
////				return null;
////			}
////		});			
////	}
//
//}
