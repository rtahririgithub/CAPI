package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.CallingCircleFeatureInfo;
import amdocs.APILink.datatypes.ContractInfo;
import amdocs.APILink.datatypes.ContractRenewalInfo;
import amdocs.APILink.datatypes.ProductServicesInfo;
import amdocs.APILink.datatypes.ServiceFeatureInfo;
import amdocs.APILink.datatypes.ServiceInfo;
import amdocs.APILink.datatypes.SocInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.UpdateCellularConv;
import amdocs.APILink.sessions.interfaces.UpdateIdenConv;
import amdocs.APILink.sessions.interfaces.UpdateProductConv;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.account.CallingCircleParameters;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.subscriber.lifecyclemanager.dao.ServiceAgreementDao;
import com.telus.cmb.subscriber.utilities.AmdocsConvBeanClassFactory;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.subscriber.info.CallingCircleParametersInfo;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.NumberGroupInfo;

public class ServiceAgreementDaoImpl extends AmdocsDaoSupport implements ServiceAgreementDao {
	
	private final Logger LOGGER = Logger.getLogger(ServiceAgreementDaoImpl.class);
	private boolean newAPIallowed = false;
	
	
	public void setNewAPIallowed (boolean newAPIallowed){
		  this.newAPIallowed = newAPIallowed;
	}
	
	@Override
	public void changePricePlan(final SubscriberInfo subscriberInfo, final SubscriberContractInfo subscriberContractInfo, 
			final String dealerCode, final String salesRepCode, final PricePlanValidationInfo pricePlanValidation, String sessionId)throws ApplicationException{
		
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				String methodName = "changePricePlan";
				UpdateIdenConv amdocsUpdateIdenConv = null;
				ProductServicesInfo productServicesInfo = new ProductServicesInfo();
				 
				setNewAPIallowed(newAPIallowed);
				 
				 
				 UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean
				 (AmdocsConvBeanClassFactory.getUpdateProductConvBean(subscriberInfo.getProductType()));
				
				 // Set ProductPK (which also retrieves the BAN)
			      amdocsUpdateProductConv.setProductPK(subscriberInfo.getBanId(),subscriberInfo.getSubscriberId());

			      // for IDEN: set numbergroup in case it is needed for reserving additional phone #s (msisdn feature parameter)
			      try {
			        if (subscriberInfo.isIDEN()) {
			          amdocsUpdateIdenConv = (UpdateIdenConv)amdocsUpdateProductConv;
			          amdocsUpdateIdenConv.setNumberGroup(subscriberInfo.getNumberGroup().getCode());
			          amdocsUpdateProductConv = amdocsUpdateIdenConv;
			        }
			      } catch (ValidateException ve){
			        // 1110560 - Invalid Network_Id/NumberGroup
			        // this exception indicates that there are no available phone #s in this numbergroup and
			        // since we only do setNumberGroup() for the case where a service is added that needs
			        // an additional phone # we ignore this exception at this time. In the case where a phone #
			        // is required we are then catching and handling the error 'NumberGroup required'
			        if ( ve.getErrorInd() == 1110560) {
			          LOGGER.error("("+getClass().getName()+"."+methodName+") ValidateException occurred (" + ve.getErrorInd() + " " + ve.getErrorMsg() + ")");
			          LOGGER.error("("+getClass().getName()+"."+methodName+") ValidateException ignored for now - processing continued");
			        } else {
			          throw new ApplicationException(SystemCodes.AMDOCS, ve.getErrorMsg(), "");
			        }
			      }
			      
				if (pricePlanValidation != null && LOGGER.isDebugEnabled()) {
					LOGGER.debug("("+getClass().getName()+"."+methodName+") isModified=" + pricePlanValidation.isModified() + ",=validateCurrent=" + pricePlanValidation.validateCurrent() + ",validateEquipmentServiceMatch="
							+ pricePlanValidation.validateEquipmentServiceMatch() + ",validateForSale=" + pricePlanValidation.validateForSale() + ",validatePricePlanServiceGrouping="
							+ pricePlanValidation.validatePricePlanServiceGrouping() + ",validateProvinceServiceMatch=" + pricePlanValidation.validateProvinceServiceMatch());
				}
					 
			   // change Priceplan (and remove/add regular socs if required)
			      // ----------------------------------------------------------
			      boolean modified = pricePlanValidation.isModified() || newAPIallowed;
			      productServicesInfo = DaoSupport.mapTelusContractToAmdocsProductServices(amdocsUpdateProductConv, subscriberInfo, subscriberContractInfo, true, true, modified, false);
			      LOGGER.debug("("+getClass().getName()+"."+methodName+") Calling changePricePlan()...");

			      ContractInfo contractInfo = new ContractInfo();
				  if (dealerCode != null && !dealerCode.equals("")){
					  contractInfo.dealerCode = dealerCode;
					  contractInfo.salesCode = salesRepCode == null ? "" : salesRepCode;
				  } else 
					  contractInfo = null; 
				  
			      if (modified){
			    	  LOGGER.debug("("+getClass().getName()+"."+methodName+") newAPIallowed = TRUE");
			    	  LOGGER.debug("("+getClass().getName()+"."+methodName+") Calling changePricePlan() with dealerCode = " + contractInfo.dealerCode + " and salesCode = " + contractInfo.salesCode + "...");
			    	  amdocs.APILink.datatypes.PricePlanValidationInfo   ppValidationInfo = new amdocs.APILink.datatypes.PricePlanValidationInfo();
			    	  if (modified){
			    		  ppValidationInfo.currentSoc = pricePlanValidation.validateCurrent(); 
			    		  ppValidationInfo.equipmentType = pricePlanValidation.validateEquipmentServiceMatch();	  
			       		  ppValidationInfo.forSale = pricePlanValidation.validateForSale();
			     		  ppValidationInfo.ppSocGrouping = pricePlanValidation.validatePricePlanServiceGrouping();
			      		  ppValidationInfo.province = pricePlanValidation.validateProvinceServiceMatch();
			    	  }
			    	  if (subscriberInfo.isIDEN())
			    		  ((UpdateIdenConv)amdocsUpdateProductConv).changeProductServices(productServicesInfo,null,contractInfo,null,ppValidationInfo );
			    	  else
			    		  ((UpdateCellularConv)amdocsUpdateProductConv).changeProductServices(productServicesInfo,null,contractInfo,null,ppValidationInfo);
			    	  
			      } else {
			    	  if (contractInfo != null){
			    		  LOGGER.debug("("+getClass().getName()+"."+methodName+") Calling changePricePlan() with dealerCode = " + contractInfo.dealerCode + " and salesCode = " + contractInfo.salesCode + "...");
			    		  amdocsUpdateProductConv.changePricePlan(productServicesInfo,contractInfo);
			    	  } else {
			    		  LOGGER.debug("("+getClass().getName()+"."+methodName+") Calling changePricePlan()...");
			    		  amdocsUpdateProductConv.changePricePlan(productServicesInfo);
			    	  }
			      }
				return null;
			}
		});
	}

	@Override
	public void changeServiceAgreement(final SubscriberInfo pSubscriberInfo,
			final SubscriberContractInfo pSubscriberContractInfo,
			final NumberGroupInfo pNumberGroupInfo, final String pDealerCode,
			final String pSalesRepCode, final PricePlanValidationInfo pricePlanValidation,
			String sessionId) throws ApplicationException {
		
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				
				UpdateIdenConv amdocsUpdateIdenConv = null;
			    ProductServicesInfo productServicesInfo = new ProductServicesInfo();
			    String methodName = "changeServiceAgreement";
			    setNewAPIallowed(newAPIallowed);
				
			    UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean
				 (AmdocsConvBeanClassFactory.getUpdateProductConvBean(pSubscriberInfo.getProductType()));
				
			      // Set ProductPK (which also retrieves the BAN)
			      amdocsUpdateProductConv.setProductPK(pSubscriberInfo.getBanId(),pSubscriberInfo.getSubscriberId());

			      // for IDEN: set numbergroup in case it is needed for reserving additional phone #s (msisdn feature parameter)
			      try {
			        if (pSubscriberInfo.isIDEN()) {
			          amdocsUpdateIdenConv = (UpdateIdenConv)amdocsUpdateProductConv;
			          amdocsUpdateIdenConv.setNumberGroup(pNumberGroupInfo.getCode());
			          amdocsUpdateProductConv = amdocsUpdateIdenConv;
			        }
			      } catch (ValidateException ve){
			       
			        if ( ve.getErrorInd() == 1110560) {
			        	 LOGGER.warn("("+getClass().getName()+"."+methodName+")  ValidateException occurred (" + ve.getErrorInd() + " " + ve.getErrorMsg() + ")");
			        	 LOGGER.debug("("+getClass().getName()+"."+methodName+")  ValidateException ignored for now - processing continued");
			        } else {
			        	throw new ApplicationException(SystemCodes.AMDOCS, ve.getErrorMsg(), "");
			        }
			      }
			      pSubscriberContractInfo.synchronizeCallingCircleParameter();
			      
			      // build serviceInfo including features and feature parameters
			      ServiceInfo[] serviceInfo = DaoSupport.buildServiceInfo(amdocsUpdateProductConv, pSubscriberInfo, pSubscriberContractInfo);
			      ServiceInfo[] promoServiceInfo = DaoSupport.buildPromotionalServiceInfo(amdocsUpdateProductConv, pSubscriberInfo, serviceInfo);
			      
			      LOGGER.info( DaoSupport.extractServiceInfosToString(pSubscriberInfo, serviceInfo, promoServiceInfo));
			    
			   // populate ContractInfo
			      ContractInfo contractInfo = new ContractInfo();
				  if (pDealerCode != null && !pDealerCode.equals("")){
					  contractInfo.dealerCode = pDealerCode;
					  contractInfo.salesCode = pSalesRepCode == null ? "" : pSalesRepCode;
				  } else 
					  contractInfo = null; 
				  
				  boolean modified = pricePlanValidation.isModified();
			      if ((newAPIallowed || modified) && (pSubscriberInfo.isIDEN() || pSubscriberInfo.isPCS())){    	  
			    	  amdocs.APILink.datatypes.PricePlanValidationInfo   ppValidationInfo = new amdocs.APILink.datatypes.PricePlanValidationInfo();
			    	  if (modified){
			    		  ppValidationInfo.currentSoc = pricePlanValidation.validateCurrent(); 
			    		  ppValidationInfo.equipmentType = pricePlanValidation.validateEquipmentServiceMatch();	  
			       		  ppValidationInfo.forSale = pricePlanValidation.validateForSale();
			     		  ppValidationInfo.ppSocGrouping = pricePlanValidation.validatePricePlanServiceGrouping();
			      		  ppValidationInfo.province = pricePlanValidation.validateProvinceServiceMatch();
			    	  }
			    	  
			    	  productServicesInfo.addtnlSrvs = serviceInfo;
			    	  if (pSubscriberInfo.isIDEN()) {
			    		  ((UpdateIdenConv)amdocsUpdateProductConv).changeProductServices(productServicesInfo,promoServiceInfo,contractInfo,null,ppValidationInfo );
			    	  } else {
			    		  ((UpdateCellularConv)amdocsUpdateProductConv).changeProductServices(productServicesInfo,promoServiceInfo,contractInfo,null,ppValidationInfo);
			    	  }
			    	       
			      } else {
			    	  if (contractInfo != null){
			    		  if (promoServiceInfo.length > 0) {
			    			  amdocsUpdateProductConv.changeServiceAgreement(serviceInfo,promoServiceInfo,contractInfo);
			    		  } else {
			    			  amdocsUpdateProductConv.changeServiceAgreement(serviceInfo,contractInfo);
			    		  }
			    	  } else {
			    		  if (promoServiceInfo.length > 0)
			    			  amdocsUpdateProductConv.changeServiceAgreement(serviceInfo,promoServiceInfo);
			    		  else
			    			  amdocsUpdateProductConv.changeServiceAgreement(serviceInfo);
			    	  }
			      }
			    return null;
			}
		});
	}
	
	@Override
	public void deleteFutureDatedPricePlan(final int ban, final String subscriberId, final String productType, String sessionId)
			throws ApplicationException {
		

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			 
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				

			    UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean
				 (AmdocsConvBeanClassFactory.getUpdateProductConvBean(productType));
    
		          amdocsUpdateProductConv.setProductPK(ban, subscriberId);
		          amdocsUpdateProductConv.deleteFuturePricePlan();
				return null;
			}
		});
	}

	@Override
	public CallingCircleParameters retrieveCallingCircleParameters(final int banId,
			final String subscriberNo, final String soc, final String featureCode,
			final String productType, String sessionId) throws ApplicationException {
		
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<CallingCircleParameters>() {
			 
		@Override
		public CallingCircleParameters doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
		
			 UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean
			 (AmdocsConvBeanClassFactory.getUpdateProductConvBean(productType));

			 amdocsUpdateProductConv.setProductPK(banId, subscriberNo);
	          
	          SocInfo socInfo = new SocInfo();
	          socInfo.soc = soc;
	          
	          ServiceFeatureInfo[] features = new ServiceFeatureInfo[1];
	          features[0] = new ServiceFeatureInfo();
	          features[0].featureCode = featureCode;
	          
	          ServiceInfo serviceInfo = new ServiceInfo();
	          serviceInfo.soc = socInfo;
	          serviceInfo.feature = features;

	          CallingCircleFeatureInfo ccfInfo = amdocsUpdateProductConv.getCallingCircleParams(serviceInfo);
	          CallingCircleParametersInfo params = new CallingCircleParametersInfo();
	          
	          {
	        	  CallingCirclePhoneListInfo phoneList = new  CallingCirclePhoneListInfo();
	        	  phoneList.setEffectiveDate( ccfInfo.currentEffDate );
	        	  phoneList.setExpiryDate(ccfInfo.currentExpDate);
	        	  phoneList.setPhoneNumberList( (ccfInfo.currentPhoneNumberList==null)? new String[0]: ccfInfo.currentPhoneNumberList );
	        	  params.setCallingCircleCurrentPhoneNumberList(phoneList);
	          }
	          {
	        	  CallingCirclePhoneListInfo phoneList = new  CallingCirclePhoneListInfo();
	        	  phoneList.setEffectiveDate( ccfInfo.futureEffDate );
	        	  phoneList.setExpiryDate(ccfInfo.futureExpDate);
	        	  phoneList.setPhoneNumberList( (ccfInfo.futurePhoneNumberList==null)? new String[0]: ccfInfo.futurePhoneNumberList );
	        	  params.setCallingCircleFuturePhoneNumberList(phoneList);
	          }
	          
	          return params;
			
			}
		});
	}

	@Override
	public void updateCommitment(final SubscriberInfo pSubscriberInfo,
			final CommitmentInfo pCommitmentInfo, final String dealerCode,
			final String salesRepCode,String sessionId) throws ApplicationException {
		 
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<CallingCircleParameters>() {
			 
			@Override
			public CallingCircleParameters doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				 
				ContractRenewalInfo contractRenewalInfo = new ContractRenewalInfo();
				 
				 UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean
				 (AmdocsConvBeanClassFactory.getUpdateProductConvBean(pSubscriberInfo.getProductType()));

				 amdocsUpdateProductConv.setProductPK(pSubscriberInfo.getBanId(),pSubscriberInfo.getSubscriberId());

			      contractRenewalInfo.dealerCode = dealerCode != null ? dealerCode : pSubscriberInfo.getDealerCode();
			      contractRenewalInfo.salesCode = salesRepCode != null ? salesRepCode : pSubscriberInfo.getSalesRepId();
			      contractRenewalInfo.commitmentReasonCode = pCommitmentInfo.getReasonCode() == null ? "PPD" : pCommitmentInfo.getReasonCode();  // 'PPD' = 'Standard'
			      contractRenewalInfo.commitmentMonths = (short)pCommitmentInfo.getMonths();
			      contractRenewalInfo.commitmentStartDate = pCommitmentInfo.getStartDate();
			      contractRenewalInfo.residualEffectIndicator = (byte)'N';

			      amdocsUpdateProductConv.renewContractInfo(contractRenewalInfo);

			      return null;
			}
		});
	}
	
}
