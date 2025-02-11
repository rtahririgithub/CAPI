package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.CellularEquipmentInfo;
import amdocs.APILink.datatypes.EquipmentInfo;
import amdocs.APILink.datatypes.FeatureInfo;
import amdocs.APILink.datatypes.FleetSecuredInfo;
import amdocs.APILink.datatypes.ImsiInfo;
import amdocs.APILink.datatypes.MultiRingParameterInfo;
import amdocs.APILink.datatypes.PagerEquipmentInfo;
import amdocs.APILink.datatypes.ProductServicesInfo;
import amdocs.APILink.datatypes.ServiceFeatureInfo;
import amdocs.APILink.datatypes.ServiceInfo;
import amdocs.APILink.datatypes.SocInfo;
import amdocs.APILink.datatypes.SocProfileInfo;
import amdocs.APILink.datatypes.UpdateFleetInfo;
import amdocs.APILink.datatypes.UrbanFleetId;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.NewIdenConv;
import amdocs.APILink.sessions.interfaces.ProductBaseConv;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;
import amdocs.APILink.sessions.interfaces.UpdateIdenConv;
import amdocs.APILink.datatypes.AddressInfo;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.fleet.Fleet;
import com.telus.api.util.DateUtil;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.framework.exception.TelusApplicationException;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.ExceptionInfo;
import com.telus.eas.subscriber.info.BaseAgreementInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.IdenResourcesInfo;
import com.telus.eas.subscriber.info.MultiRingInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.VoiceToTextOptionsInfo;

public class DaoSupport {

	private static final Logger LOGGER = Logger.getLogger(DaoSupport.class);
	private static final byte MODE_UPDATE = (byte)'U';
	private static AccountInformationHelper accountInformationHelper = null;
	private static AccountLifecycleManager accountLifecycleManager = null;
	
	public static ProductServicesInfo mapTelusContractToAmdocsProductServices(
			ProductBaseConv pProductBaseConv,
			SubscriberInfo pSubscriberInfo,
			SubscriberContractInfo pSubscriberContractInfo,
			boolean processIncludedSocs,
			boolean processRegularSocs, boolean portInMSISDN) throws RemoteException, ValidateException, ApplicationException {
		
		ProductServicesInfo productServicesInfo = DaoSupport.mapToAmdocsProductServices(pProductBaseConv,pSubscriberInfo,pSubscriberContractInfo,processIncludedSocs,processRegularSocs, false, portInMSISDN);
		
		LOGGER.info(extractProductServicesInfoToString(pSubscriberInfo, productServicesInfo));
		
		return productServicesInfo;
	}

	private static ProductServicesInfo mapToAmdocsProductServices(
			ProductBaseConv pProductBaseConv,
			SubscriberInfo pSubscriberInfo,
			SubscriberContractInfo pSubscriberContractInfo,
			boolean processIncludedSocs,
			boolean processRegularSocs, boolean pricePlanModified, boolean portInMSISDN) throws ApplicationException, RemoteException, ValidateException {
		
		ServiceInfo pricePlan = new ServiceInfo();
		pricePlan.multiRingParamInfo = null;

		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.multiRingParamInfo = null;

		Vector<ServiceInfo> additionalServices = new Vector<ServiceInfo>();
		Vector<ServiceInfo> promotionalServices = new Vector<ServiceInfo>();

		ProductServicesInfo productServicesInfo = new ProductServicesInfo();
		SocProfileInfo promotionalSoc = null;

		ServiceAgreementInfo[] includedSocs = new ServiceAgreementInfo[0];

		ServiceAgreementInfo[] regularAndPromotionalSocs = new ServiceAgreementInfo[0];

		// Get Get included/regular services
		if (pSubscriberContractInfo == null) {
			throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.SUBSCRIBER_CONTACT_INFO_REQUIRED, "SubscriberContactInfo cannot be null.","");
		} else {
			pSubscriberContractInfo.synchronizeCallingCircleParameter();

			includedSocs = pSubscriberContractInfo.getIncludedServices0(true);
			regularAndPromotionalSocs = pSubscriberContractInfo.getOptionalServices0(true);
		}

		// 3 steps to setup product services
		// I) handle priceplan
		// - look for features that need additional parameters and set feature parameter if applicable
		pricePlan.soc.soc = pSubscriberContractInfo.getPricePlanCode();
		pricePlan.soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_INSERT;

		if (pSubscriberContractInfo.getEffectiveDate() != null) pricePlan.soc.effDate = pSubscriberContractInfo.getEffectiveDate();
		if (pSubscriberContractInfo.getExpiryDate() != null) pricePlan.soc.expDate = pSubscriberContractInfo.getExpiryDate();
		pricePlan.feature = buildServiceFeature(pSubscriberContractInfo.getFeatures0(true));

		/**TODO: Voice 2 Text CR 091207 changes have been rolled back for February release.  To re-introduce the changes back in, please replace the line of code 
		 * following this block with the following:
		 * 
		 *     pricePlan = findAndSetAdditionalFeatureParameters(pProductBaseConv, pricePlan,pSubscriberInfo,ServiceAgreementInfo.SERVICE_TYPE_PRICEPLAN,true,true,false,true,null);
		 */
		pricePlan = findAndSetAdditionalFeatureParameters(pProductBaseConv, pricePlan,pSubscriberInfo,ServiceAgreementInfo.SERVICE_TYPE_PRICEPLAN,true,true,false,true, portInMSISDN);
		if (pSubscriberContractInfo.isDispatchOnly())
			pricePlan = findAndRemoveSpecificFeatures(pProductBaseConv, pricePlan, true);

		// II) handle included SOCs
		// - check if requested
		// - look for features that need additional parameters and set feature parameter if applicable
		if (processIncludedSocs && includedSocs != null && includedSocs.length > 0) {
			for (int i=0; i < includedSocs.length; i++) {
				serviceInfo = new ServiceInfo();

				if (containsMultiRingFeature(pSubscriberInfo, includedSocs[i], pSubscriberContractInfo.getMultiRingInfos())) {
					serviceInfo.multiRingParamInfo = convertMultiRingInfos(pSubscriberContractInfo.getMultiRingInfos());
				} else {
					serviceInfo.multiRingParamInfo = null;
				}
				serviceInfo.soc.soc = includedSocs[i].getServiceCode().trim();
				serviceInfo.soc.transactionType = includedSocs[i].getTransaction();

				if (includedSocs[i].getEffectiveDate() != null) serviceInfo.soc.effDate = includedSocs[i].getEffectiveDate();
				if (includedSocs[i].getExpiryDate() != null) serviceInfo.soc.expDate = includedSocs[i].getExpiryDate();
				serviceInfo.feature = buildServiceFeature(includedSocs[i].getFeatures0(true));

				// look for features that need additional parameters and set feature parameter if applicable

				/**TODO: Voice 2 Text CR 091207 changes have been rolled back for February release.  To re-introduce the changes back in, please replace the line of code 
				 * following this block with the following:
				 * 
				 * 		serviceInfo = findAndSetAdditionalFeatureParameters(pProductBaseConv, serviceInfo,pSubscriberInfo,includedSocs[i].getServiceType(),true,true,true,true,null);
				 */
				serviceInfo = findAndSetAdditionalFeatureParameters(pProductBaseConv, serviceInfo,pSubscriberInfo,includedSocs[i].getServiceType(),true,true,true,true, portInMSISDN);
				if (pSubscriberContractInfo.isDispatchOnly())
					serviceInfo = findAndRemoveSocsWithSpecificFeatures(pProductBaseConv, serviceInfo, true);
				if (serviceInfo.feature.length > 0 || serviceInfo.soc.expDate != null || serviceInfo.soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_DELETE) {
					additionalServices.add(serviceInfo);
					continue;
				}
			}
		}

		// III) handle regular (and, when changing a contract, promotional) SOCS
		// - check if requested
		// - skip socs that are not of type 'Regular' or 'Regular Auto-Expire'
		// - look for features that need additional parameters and set feature parameter if applicable
		// - add them to additionalServices vector
		// - if SOC has promotional SOC:
		//    - look for features that need additional parameters and set feature parameter if applicable
		if (processRegularSocs && regularAndPromotionalSocs != null && regularAndPromotionalSocs.length > 0) {
			for (int i=0; i < regularAndPromotionalSocs.length; i++) {

				// only handle certain service types:
				// - SERVICE_TYPE_REGULAR = "R"
				// - SERVICE_TYPE_REGULAR_AUTO_EXPIRE = "G"
				if (!regularAndPromotionalSocs[i].getServiceType().equals(
						ServiceAgreementInfo.SERVICE_TYPE_REGULAR) &&
						!regularAndPromotionalSocs[i].getServiceType().equals(
								ServiceAgreementInfo.SERVICE_TYPE_REGULAR_AUTO_EXPIRE))
					continue;

				serviceInfo = new ServiceInfo();
				if (containsMultiRingFeature(pSubscriberInfo, regularAndPromotionalSocs[i], pSubscriberContractInfo.getMultiRingInfos())) {
					serviceInfo.multiRingParamInfo = convertMultiRingInfos(pSubscriberContractInfo.getMultiRingInfos());
				} else {
					serviceInfo.multiRingParamInfo = null;
				}
				serviceInfo.soc.soc = regularAndPromotionalSocs[i].getServiceCode().trim();
				serviceInfo.soc.transactionType = regularAndPromotionalSocs[i].getTransaction();

				serviceInfo.feature = buildServiceFeature(regularAndPromotionalSocs[i].getFeatures0(true));        
				/**TODO: Voice 2 Text CR 091207 changes have been rolled back for February release.  To re-introduce the changes back in, please replace the line of code 
				 * following this block with the following:
				 * 
				 * 	serviceInfo = findAndSetAdditionalFeatureParameters(pProductBaseConv, serviceInfo,pSubscriberInfo,regularAndPromotionalSocs[i].getServiceType(),true,true,true,true, null);
				 */
				serviceInfo = findAndSetAdditionalFeatureParameters(pProductBaseConv, serviceInfo,pSubscriberInfo,regularAndPromotionalSocs[i].getServiceType(), true, true, true, true, portInMSISDN);
				if (regularAndPromotionalSocs[i].getEffectiveDate() != null) serviceInfo.soc.effDate = regularAndPromotionalSocs[i].getEffectiveDate();
				if (regularAndPromotionalSocs[i].getExpiryDate() != null) serviceInfo.soc.expDate = regularAndPromotionalSocs[i].getExpiryDate();
				if(regularAndPromotionalSocs[i].getTransaction() == BaseAgreementInfo.DELETE && 
						pricePlanModified && pSubscriberContractInfo.getEffectiveDate() != null){

					if(regularAndPromotionalSocs[i].getEffectiveDate() != null && simpleDateCompare(pSubscriberContractInfo.getEffectiveDate(), regularAndPromotionalSocs[i].getEffectiveDate()) == 1){ 
						serviceInfo.soc.expDate =  pSubscriberContractInfo.getEffectiveDate();
						serviceInfo.soc.transactionType = MODE_UPDATE;
					}
				}
				additionalServices.add(serviceInfo);

				if (serviceInfo.soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_INSERT ||
						serviceInfo.soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_UPDATE) {
					promotionalSoc = pProductBaseConv.getPromotionalSoc(regularAndPromotionalSocs[i].getServiceCode());
					if (promotionalSoc != null) {
						/**TODO: Voice 2 Text CR 091207 changes have been rolled back for February release.  To re-introduce the changes back in, please replace the following 
						 * 5 lines of code following this block with the following:
						 *
						 *	ServiceInfo promoServiceInfo = new ServiceInfo();
						 *	promoServiceInfo.multiRingParamInfo = null;
						 *	promoServiceInfo.soc.soc = promotionalSoc.soc;
						 * 	promoServiceInfo.soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_INSERT;
						 *
						 * 	serviceInfo = findAndSetAdditionalFeatureParameters(pProductBaseConv, promoServiceInfo,pSubscriberInfo,AttributeTranslator.stringFrombyte(promotionalSoc.serviceType),true,true,true,true,serviceInfo);
						 */
						serviceInfo = new ServiceInfo();
						serviceInfo.multiRingParamInfo = null;
						serviceInfo.soc.soc = promotionalSoc.soc;
						serviceInfo.soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_INSERT;

						serviceInfo = findAndSetAdditionalFeatureParameters(pProductBaseConv, serviceInfo
								,pSubscriberInfo,AttributeTranslator.stringFrombyte(promotionalSoc.serviceType)
								,true,true,true,true, portInMSISDN);
						promotionalServices.add(serviceInfo);
					}
				}
			}
		}

		// populate ProductServicesInfo
		// - Price Plan
		productServicesInfo.pricePlan = pricePlan;

		// - Additonal Services
		if (additionalServices.size() > 0) {
			productServicesInfo.addtnlSrvs = new ServiceInfo[additionalServices.size()];
			int count = 0;
			Iterator<ServiceInfo> i = additionalServices.iterator();
			while (i.hasNext()){
				productServicesInfo.addtnlSrvs[count] = i.next();
				count++;
			}
			// remove services that have not changed
			productServicesInfo.addtnlSrvs = removeNoChangeServices(productServicesInfo.addtnlSrvs);
		}
		// - Promotional Services ?
		if (promotionalServices.size() > 0) {
			productServicesInfo.promPricePlan = new ServiceInfo[promotionalServices.size()];
			int count = 0;
			Iterator<ServiceInfo> i = promotionalServices.iterator();
			while (i.hasNext()){
				productServicesInfo.promPricePlan[count] = i.next();
				count++;
			}
			LOGGER.debug("promotionalServices added!");
		}

		
		return productServicesInfo;
	}

	private static ServiceInfo findAndSetAdditionalFeatureParameters(ProductBaseConv productBaseConv,
			ServiceInfo serviceInfo,
			SubscriberInfo subscriberInfo,
			String serviceType,
			boolean checkForVMparms,
			boolean checkForMSISDNrequired,
			boolean checkForFBCparms,
			boolean checkCallHomeFreeNumber,
			boolean portInMSISDN) throws RemoteException, ValidateException, ApplicationException {

		SimpleDateFormat df = new SimpleDateFormat("MMdd");
		Date birthDate = new Date();

		FeatureInfo[] featureList = null;
		ServiceInfo serviceInfoInternal = serviceInfo;
		amdocs.APILink.datatypes.ServiceFeatureInfo feature = null;
		String vmLanguage = null;
		if(subscriberInfo.getVoiceMailLanguage() != null && subscriberInfo.getVoiceMailLanguage().trim().length() != 0){
			vmLanguage = subscriberInfo.getVoiceMailLanguage();
		}else{
			vmLanguage = subscriberInfo.getLanguage() == null || subscriberInfo.getLanguage().trim().length() == 0 ? "EN" : subscriberInfo.getLanguage();
		}
		HashMap<String, ServiceFeatureInfo> features = new HashMap<String, ServiceFeatureInfo>();

		LOGGER.debug("Processing SOC: [" + serviceInfo.soc.soc + "] transaction type: " + AttributeTranslator.stringFrombyte(serviceInfo.soc.transactionType));

		// return if service transaction type is set to 'DELETE'
		if (serviceInfo.soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_DELETE) {
			LOGGER.debug("Skipping SOC: [" + serviceInfo.soc.soc + "] transaction type: " + AttributeTranslator.stringFrombyte(serviceInfo.soc.transactionType));
			return serviceInfoInternal;
		}

		// return if service transaction type is set to 'NO_CHANGE'
		// and not an included service and not a pricePlan
		if (serviceInfo.soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_NO_CHG &&
				!serviceType.equals(ServiceAgreementInfo.SERVICE_TYPE_OPTIONAL) &&
				!serviceType.equals(ServiceAgreementInfo.SERVICE_TYPE_PRICEPLAN) &&
				!serviceType.equals(ServiceAgreementInfo.SERVICE_TYPE_OPTIONAL_AUTO_EXPIRE)) {
			LOGGER.debug("Skipping not included/not priceplan SOC: [" + serviceInfo.soc.soc + "] transaction type: " + AttributeTranslator.stringFrombyte(serviceInfo.soc.transactionType));
			return serviceInfoInternal;
		}

		// populate HashMap with features currently on ServiceInfo
		if (serviceInfo.feature != null && serviceInfo.feature.length > 0) {
			for (int i=0; i < serviceInfo.feature.length; i++) {
				features.put(serviceInfo.feature[i].featureCode,serviceInfo.feature[i]);
			}
		}

		featureList = productBaseConv.getFeatureList(serviceInfoInternal.soc.soc);
		if (featureList != null && featureList.length > 0) {
			for (int j=0; j < featureList.length; j++) {

				// get feature from hashtable (ie. passed-in) or initialize
				feature = features.get(featureList[j].featureCode);
				if (feature == null) {
					feature = new amdocs.APILink.datatypes.ServiceFeatureInfo();
					feature.featureCode = featureList[j].featureCode;
					//RE-COMMENTED TO UNDO CODE CHANGE THAT FIXED DEFECT 49233 WHICH CAUSED DEFECT 54913
					//changed by Roman: un-commented to fix re-opened 49233.
					if (serviceType.equals(ServiceAgreementInfo.SERVICE_TYPE_OPTIONAL))
						feature.transactionType = amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_DELETE;
				}

				LOGGER.debug("Processing Feature: [" + feature.featureCode + "] transaction type: " + AttributeTranslator.stringFrombyte(feature.transactionType));

				//UN-COMMENTED TO UNDO CODE CHANGE THAT FIXED DEFECT 49233 WHICH CAUSED DEFECT 54913 -BEGIN
				// Commented out by Vladimir for fixing the defect #49233 - start
				// skip this feature if it is being deleted
				//changed by Roman. it skips all features but optional
				if (feature.transactionType == ServiceFeatureInfo.FTR_TRANSACTION_TYPE_DELETE &&
						!serviceType.equals(ServiceAgreementInfo.SERVICE_TYPE_OPTIONAL)){
					LOGGER.debug("Skipping Feature: [" + feature.featureCode + "] transaction type: " + AttributeTranslator.stringFrombyte(feature.transactionType));
					continue;
				}
				// Commented out by Vladimir for fixing the defect #49233 - end
				//UN-COMMENTED TO UNDO CODE CHANGE THAT FIXED DEFECT 49233 WHICH CAUSED DEFECT 54913 - END

		        //Defect PROD00183886 fix begin - M. Liao, Feb 21,2011
		        //Background: SOC MS25VMF8 contain both voice mail and FAX feature. when front-end trying to reset voice mail password by changing the
		        //VM feautre's parameter, the SOC's transaction type is UPDATE, the VM feature's transaction type is UPDATE; any others features' 
		        //transaction type is NO_CHANGE. When processing the features under a SOC, if SOC's transaction type is UPDATE,
		        //We were trying to process all itself features, this cause we re-allocate a new MSISDN number for fax number, and make fax feature's 
		        //transaction type as UPDATE. Due to unknown KB defect, updating FAX number cause Tuxedo error.  
		        // 
		        //Resolution: 
		    	//When a feautre's tranactionType is NO_CHANGE, but its parent SOC's transactionType is UPDATE, this means
		        // 1) the parent SOC's expiration date or MultiRingParameter get changed
		        // 2) the parent SOC's has no change, but some feature(s) in same SOC get changed by front-end
		        //In both case we shall skip processing this feature itself.
		        if (feature.transactionType == ServiceFeatureInfo.FTR_TRANSACTION_TYPE_NO_CHG
		        	&& serviceInfo.soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_UPDATE ){
		            LOGGER.debug("Skipping Feature: [" + feature.featureCode + "] transaction type: " + AttributeTranslator.stringFrombyte(feature.transactionType));
		            continue;
		        }
		        
		        //Defect PROD00183886 fix -end
				// Set VM parameter even on 'No_Change' features to work-around a database issue
				// - switch code = 'VM'
				// --------------------
				if (checkForVMparms &&
						feature.ftrParam.trim().equals("")) {         
					if (featureList[j].switchCode.equals(com.telus.eas.utility.info.FeatureInfo.SWITCH_CODE_VOICE_MAIL) && featureList[j].csmParamReqInd == (byte)'Y') {
						if (feature.transactionType == amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_NO_CHG)
							feature.transactionType = serviceInfo.soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_NO_CHG ? amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_UPDATE : serviceInfo.soc.transactionType;
						//feature.transactionType = feature.FTR_TRANSACTION_TYPE_UPDATE;
						/** TODO: Voice 2 Text CR 091207 changes have been rolled back for February release.  To re-introduce the changes back in, please add the following line
						 *  of code as is (remove entire TODO coment block):
						 * 
						 * if (pRegularSOC == null) { //Added for CR 91207 - February 2008 release.  pRegular SOC will only be not null for promotional SOC 
						 */

						feature.ftrParam = "LANGUAGE=" + vmLanguage + "@";
						/** TODO: Voice 2 Text CR 091207 changes have been rolled back for February release.  To re-introduce the changes back in, please add the following lines
						 *  of code as is (remove entire TODO coment block):
						 * 
						 *          } else {  //This should only be reached for Promotional SOC.  Need to copy ftrParam from regular SOC into promotional SOC's ftrParam
						 *      		  String newFtrParam = getParameterVal(featureList, pRegularSOC, com.telus.eas.utility.info.FeatureInfo.SWITCH_CODE_VOICE_MAIL, true);
						 *      		  if (!newFtrParam.trim().equals(""))
						 *      		    feature.ftrParam = newFtrParam;
						 *      		}
						 */
						features.put(feature.featureCode,feature);
						LOGGER.debug("Parmameter for soc [" + serviceInfoInternal.soc.soc + "] feature [" + feature.featureCode + "] added: " + feature.ftrParam + "/" + feature.msisdn + "!");
					}
				}



				// - MSISDN required (IDEN only and not if SOC is marked as NO_CHANGE)
				// -----------------
				if (checkForMSISDNrequired &&
						subscriberInfo.isIDEN() &&
						serviceInfo.soc.transactionType != SocInfo.SOC_TRANSACTION_TYPE_NO_CHG) {
					if (featureList[j].msisdnInd == (byte)'Y') {
						if(portInMSISDN || (subscriberInfo.getPortType() != null && subscriberInfo.getPortType().equals("I"))){
							String[] npanxx = subscriberInfo.getNumberGroup().getNpaNXX();
							if (npanxx != null && npanxx.length > 0){
								for(int i = 0; i < npanxx.length; i++){
									String npa = npanxx[i].substring(0,3);
									String nxx = npanxx[i].substring(3,6);
									LOGGER.debug("NPA = [" + npa + "] NXX = [" + nxx + "] ");
									
									try{
										if (productBaseConv instanceof amdocs.APILink.sessions.interfaces.NewIdenConv) {
											feature.msisdn = ((NewIdenConv)productBaseConv).reserveAdditionalResource(npa,nxx);
										} else {
											feature.msisdn = ((UpdateIdenConv)productBaseConv).reserveAdditionalResource(npa,nxx);
										}
				    		            break;
			    				    } catch (ValidateException ve){
										LOGGER.error("ValidateException occurred (" + ve.getErrorInd() + " " + ve.getErrorMsg() + ")");
										if ((i + 1) < npanxx.length) {
											continue;
										} else {
											throw ve;
										}
			    				    } catch(Throwable t){
			    				      LOGGER.error("Throwable occurred. ", t);
			    				    }
								}
							} else {
								throw new ApplicationException(SystemCodes.CMB_SLM_DAO, "No NPA NXX available for MSISDN reservation","");
								//								throw new TelusValidationException("SYS00003","No NPA NXX available for MSISDN reservation"); TODO: Verify
							}
						}else {
							if (productBaseConv instanceof amdocs.APILink.sessions.interfaces.NewIdenConv) {
								feature.msisdn = ((NewIdenConv)productBaseConv).reserveAdditionalResource(subscriberInfo.getPhoneNumber().substring(0,3),subscriberInfo.getPhoneNumber().substring(3,6));
							} else {
								feature.msisdn = ((UpdateIdenConv)productBaseConv).reserveAdditionalResource(subscriberInfo.getPhoneNumber().substring(0,3),subscriberInfo.getPhoneNumber().substring(3,6));
							}
						}
						if (feature.transactionType == amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_NO_CHG)
							feature.transactionType = serviceInfo.soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_NO_CHG ? amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_UPDATE : serviceInfo.soc.transactionType;
						//feature.transactionType = feature.FTR_TRANSACTION_TYPE_UPDATE;
						features.put(feature.featureCode,feature);
						LOGGER.debug("Parmameter for soc [" + serviceInfoInternal.soc.soc + "] feature [" + feature.featureCode + "] added: " + feature.ftrParam + "/" + feature.msisdn + "!");
					}
				}

				//// skip this feature if it is not being changed
				//if (feature.transactionType == feature.FTR_TRANSACTION_TYPE_NO_CHG) {
				//print(getClass().getName(),methodName,"Skipping Feature: [" + feature.featureCode + "] transaction type: " + AttributeTranslator.stringFrombyte(feature.transactionType));
				//continue;
				//}


				// - 'FBC' feature (PCS only)
				// (use initial activation date if no birthdate available)
				// -------------------------------------------------------
				birthDate = subscriberInfo.getBirthDate() == null ? subscriberInfo.getStartServiceDate() : subscriberInfo.getBirthDate();
				if (checkForFBCparms && birthDate != null &&
						subscriberInfo.isPCS() &&
						feature.ftrParam.trim().equals(""))  {
					if (featureList[j].featureCode.equals("FBC")) {
						if (feature.transactionType == amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_NO_CHG)
							feature.transactionType = serviceInfo.soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_NO_CHG ? amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_UPDATE : serviceInfo.soc.transactionType;
						//feature.transactionType = feature.FTR_TRANSACTION_TYPE_UPDATE;
						/** TODO: Voice 2 Text CR 091207 changes have been rolled back for February release.  To re-introduce the changes back in, please add the following line
						 *  of code as is (remove entire TODO coment block):
						 * 
						 *              if (pRegularSOC == null) { //Added for CR 91207 - February 2008 release.  pRegular SOC will only be not null for promotional SOC
						 */              
						feature.ftrParam = "DATE-OF-BIRTH=" + df.format(birthDate) + "@";
						/** TODO: Voice 2 Text CR 091207 changes have been rolled back for February release.  To re-introduce the changes back in, please add the following lines
						 *  of code as is (remove entire TODO coment block):
						 *                 
						 *      		  } else {  //This should only be reached for Promotional SOC.  Need to copy ftrParam from regular SOC into promotional SOC's ftrParam
						 *      			String newFtrParam = getParameterVal(featureList, pRegularSOC, "FBC", false);
						 *      			if (!newFtrParam.trim().equals(""))
						 *      		      feature.ftrParam = newFtrParam;
						 *      		  }
						 */
						features.put(feature.featureCode,feature);
						LOGGER.debug("Parmameter for soc [" + serviceInfoInternal.soc.soc + "] feature [" + feature.featureCode + "] added: " + feature.ftrParam + "/" + feature.msisdn + "!");
					}
				}

				// Set CHF (Call Home Free) parameter even on 'No_Change' features to work-around a database issue
				// - switch code = 'CHF'
				// --------------------
				if (checkCallHomeFreeNumber &&
						feature.ftrParam.trim().equals("")) {
					if (featureList[j].switchCode.equals("CHF") && featureList[j].csmParamReqInd == (byte)'Y') {
						if (feature.transactionType == amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_NO_CHG)
							feature.transactionType = serviceInfo.soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_NO_CHG ? amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_UPDATE : serviceInfo.soc.transactionType;
						/** TODO: Voice 2 Text CR 091207 changes have been rolled back for February release.  To re-introduce the changes back in, please add the following line
						 *  of code as is (remove entire TODO coment block):
						 * 
						 *            if (pRegularSOC == null) { //Added for CR 91207 - February 2008 release.  pRegular SOC will only be not null for promotional SOC
						 */
						feature.ftrParam = "CALLHOMEFREE=1111111111@";
						/** TODO: Voice 2 Text CR 091207 changes have been rolled back for February release.  To re-introduce the changes back in, please add the following lines
						 *  of code as is (remove entire TODO coment block):
						 *               
						 *    		} else {  //This should only be reached for Promotional SOC.  Need to copy ftrParam from regular SOC into promotional SOC's ftrParam
						 *      		  String newFtrParam = getParameterVal(featureList, pRegularSOC, "CHF", true);
						 *      		  if (!newFtrParam.trim().equals(""))
						 *      		    feature.ftrParam = newFtrParam;
						 *      		}
						 */              
						features.put(feature.featureCode,feature);
						LOGGER.debug("Parmameter for soc [" + serviceInfoInternal.soc.soc + "] feature [" + feature.featureCode + "] added: " + feature.ftrParam + "/" + feature.msisdn + "!");
					}
				}

				// Set Calling-circle parameter even on 'No_Change' features to work-around a database issue
				// - switch code = 'CLCRCL'
				if (feature.ftrParam.trim().equals("")) {
					if (featureList[j].switchCode.equals(com.telus.eas.utility.info.FeatureInfo.SWITCH_CODE_CALLING_CIRCLE ) ) {
						if (feature.transactionType == amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_NO_CHG)
							feature.transactionType = serviceInfo.soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_NO_CHG ? amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_UPDATE : serviceInfo.soc.transactionType;
						/** TODO: Voice 2 Text CR 091207 changes have been rolled back for February release.  To re-introduce the changes back in, please add the following line
						 *  of code as is (remove entire TODO coment block):
						 * 
						 *                if (pRegularSOC == null) { //Added for CR 91207 - February 2008 release.  pRegular SOC will only be not null for promotional SOC
						 */
						feature.ftrParam = "CALLING-CIRCLE=@";
						/** TODO: Voice 2 Text CR 091207 changes have been rolled back for February release.  To re-introduce the changes back in, please add the following lines
						 *  of code as is (remove entire TODO coment block):
						 *                   
						 *        		} else {  //This should only be reached for Promotional SOC.  Need to copy ftrParam from regular SOC into promotional SOC's ftrParam
						 *          		  String newFtrParam = getParameterVal(featureList, pRegularSOC, com.telus.eas.utility.info.FeatureInfo.SWITCH_CODE_CALLING_CIRCLE, true);
						 *          		  if (!newFtrParam.trim().equals(""))
						 *          		    feature.ftrParam = newFtrParam;
						 *          		}
						 */
						features.put(feature.featureCode,feature);
						LOGGER.debug("Parmameter for soc [" + serviceInfoInternal.soc.soc + "] feature [" + feature.featureCode + "] added: " + feature.ftrParam + "/" + feature.msisdn + "!");
					}
				}

				// Set VoiceToText parameters to default values if VoiceToText feature is purchased with no parameters
				if (feature.ftrParam.trim().equals("")) {
					if (featureList[j].switchCode.trim().equals(com.telus.eas.utility.info.FeatureInfo.CATEGORY_CODE_VOICE2TEXT)) {
						if (feature.transactionType == amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_NO_CHG) {
							feature.transactionType = serviceInfo.soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_NO_CHG ? amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_UPDATE : serviceInfo.soc.transactionType;
						}
						feature.ftrParam = VoiceToTextOptionsInfo.SMS_OR_MMS_DELIVERY_KEY + "=Y@" + VoiceToTextOptionsInfo.EMAIL_DELIVERY_KEY + "=N@" +
						VoiceToTextOptionsInfo.ROLLING_VM_KEY + "=Y@" + VoiceToTextOptionsInfo.VOICE_FILE_KEY + "=N@";

						features.put(feature.featureCode, feature);
					}
				}

			}


			if (features.size() > 0) {
				serviceInfoInternal.soc = serviceInfo.soc;
				serviceInfoInternal.feature = new amdocs.APILink.datatypes.ServiceFeatureInfo[features.size()];

				// turn Hashmap of features in ServiceFeatureInfo[] on ServiceInfo
				Collection<ServiceFeatureInfo> featureArray = new ArrayList<ServiceFeatureInfo>();
				featureArray = features.values();
				serviceInfoInternal.feature = featureArray.toArray(serviceInfoInternal.feature);
			} else {
				serviceInfoInternal = serviceInfo;
			}
		}
		return serviceInfoInternal;
	}

	private static ServiceInfo findAndRemoveSpecificFeatures(ProductBaseConv productBaseConv
			, ServiceInfo serviceInfo, boolean processForDispatchOnly) throws RemoteException, ValidateException {

		FeatureInfo[] featureList = null;
		ServiceInfo serviceInfoInternal = new ServiceInfo();
		serviceInfoInternal.multiRingParamInfo = null;
		amdocs.APILink.datatypes.ServiceFeatureInfo feature = null;
		HashMap<String, ServiceFeatureInfo> features = new HashMap<String, ServiceFeatureInfo>();

		// populate HashMap with features currently on ServiceInfo
		for (int i=0; i < serviceInfo.feature.length; i++) {
			features.put(serviceInfo.feature[i].featureCode,serviceInfo.feature[i]);
		}

		featureList = productBaseConv.getFeatureList(serviceInfo.soc.soc);
		if (featureList != null && featureList.length > 0) {
			for (int j = 0; j < featureList.length; j++) {

				// For Dispatch-only
				// - remove features with switch code = 'VM' or 'CFW'
				// --------------------------------------------------
				if (processForDispatchOnly &&
						(featureList[j].switchCode.equals("VM") || featureList[j].switchCode.equals("CFW"))) {
					feature = new amdocs.APILink.datatypes.ServiceFeatureInfo();
					feature.featureCode = featureList[j].featureCode;
					feature.transactionType = ServiceFeatureInfo.FTR_TRANSACTION_TYPE_DELETE;
					features.put(feature.featureCode,feature);
				}
			}

			if (features.size() > 0) {
				serviceInfoInternal.soc = serviceInfo.soc;
				serviceInfoInternal.feature = new amdocs.APILink.datatypes.ServiceFeatureInfo[features.size()];

				// turn Hashmap of features in ServiceFeatureInfo[] on ServiceInfo
				Collection<ServiceFeatureInfo> featureArray = new ArrayList<ServiceFeatureInfo>();
				featureArray = features.values();
				serviceInfoInternal.feature = featureArray.toArray(serviceInfoInternal.feature);
			} else {
				serviceInfoInternal = serviceInfo;
			}
		}
		return serviceInfoInternal;
	}

	private static boolean containsMultiRingFeature(SubscriberInfo subscriber, ServiceAgreementInfo soc, MultiRingInfo[] multiRingInfo) {
		if (multiRingInfo != null && subscriber.isPCS() && soc != null) {
			for (int i = 0; i < multiRingInfo.length; i++) {
				if (soc.getCode().equalsIgnoreCase(multiRingInfo[i].getSocCode())) {
					LOGGER.debug("containsMultiRingFeature TRUE");
					return true;
				}
			}
		}
		LOGGER.debug("containsMultiRingFeature FALSE");
		return false;
	}

	private static MultiRingParameterInfo[] convertMultiRingInfos(MultiRingInfo[] infos) {
		MultiRingParameterInfo[] amdocsInfos = new MultiRingParameterInfo[infos.length];
		MultiRingParameterInfo info = null;
		for(int i=0; i<infos.length; i++) {
			info = new MultiRingParameterInfo();
			info.phoneNumber = infos[i].getPhone();
			info.transactionType = infos[i].getMode();
			amdocsInfos[i] = info;
			LOGGER.debug(amdocsInfos[i].phoneNumber + " | " + (char)amdocsInfos[i].transactionType);
		}

		return amdocsInfos;
	}

	private static ServiceInfo findAndRemoveSocsWithSpecificFeatures(ProductBaseConv pProductBaseConv, ServiceInfo pServiceInfo, boolean processForDispatchOnly) throws RemoteException, ValidateException {

		FeatureInfo[] featureList = null;
		ServiceInfo serviceInfo = pServiceInfo;
		amdocs.APILink.datatypes.ServiceFeatureInfo feature = null;
		Vector<ServiceFeatureInfo> features = new Vector<ServiceFeatureInfo>();

		featureList = pProductBaseConv.getFeatureList(serviceInfo.soc.soc);
		if (featureList != null && featureList.length > 0) {
			for (int j = 0; j < featureList.length; j++) {

				// For Dispatch-only
				// - remove features with switch code = 'VM' or 'CFW'
				// --------------------------------------------------
				if (processForDispatchOnly &&
						(featureList[j].switchCode.equals("VM") || featureList[j].switchCode.equals("CFW"))) {
					feature = new amdocs.APILink.datatypes.ServiceFeatureInfo();
					feature.featureCode = featureList[j].featureCode;
					feature.transactionType = ServiceFeatureInfo.FTR_TRANSACTION_TYPE_DELETE;
					features.add(feature);
					break;
				}
			}

			if (features.size() > 0) {
				serviceInfo.soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_DELETE;
			}
		}
		return serviceInfo;
	}

	private static int simpleDateCompare(Date d1, Date d2) {

		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		Date temp = c1.getTime(); // force refresh

		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);
		c2.set(Calendar.HOUR_OF_DAY, 0);
		c2.set(Calendar.MINUTE, 0);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MILLISECOND, 0);
		Date temp2 = c2.getTime(); // force refresh

		return temp.compareTo(temp2);
	}

	private static ServiceInfo[] removeNoChangeServices(ServiceInfo[] services) {

		Collection<ServiceInfo> servicesInternal = new ArrayList<ServiceInfo>();
		ServiceInfo[] serviceInfoArray = new ServiceInfo[0];

		// Loop thru services and remove services with
		// - soc transaction type 'No-Change' AND
		// - no features OR all features have transaction type 'No-Change'
		for (int i=0; i < services.length; i++) {

			// keep services that are marked as 'Insert', 'Remove', 'Update'
			if (services[i].soc.transactionType != SocInfo.SOC_TRANSACTION_TYPE_NO_CHG) {
				servicesInternal.add(services[i]);
				continue;
			}

			// skip services that are marked as 'No-Change' and have 0 features
			if (services[i].soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_NO_CHG &&
					services[i].feature == null || services[i].feature.length == 0) {
				continue;
			}

			// keep services that have at least one feature marked as 'Insert','Remove' or 'Update'
			for (int j=0; j < services[i].feature.length; j++) {
				if (services[i].feature[j].transactionType != amdocs.APILink.datatypes.ServiceFeatureInfo.FTR_TRANSACTION_TYPE_NO_CHG) {
					servicesInternal.add(services[i]);
					break;
				}
			}

			//print(getClass().getName(), "removeNoChangeServices", "SOC = " + pServices[i].soc.soc + " | " + (char)pServices[i].soc.transactionType);
			// keep multi-ring SOC even SOC mode = 'N', since we need to update multi-ring phone numbers
			if (services[i].multiRingParamInfo != null && services[i].multiRingParamInfo.length > 0) {
				servicesInternal.add(services[i]);
				LOGGER.debug("multi-ring SOC, keep it ...");
			}

		}

		// convert ArrayList to ServiceInfo[]
		if (servicesInternal.size() > 0) {
			serviceInfoArray = new ServiceInfo[servicesInternal.size()];
			serviceInfoArray = servicesInternal.toArray(serviceInfoArray);
		}

		return serviceInfoArray;
	}

	private static String extractProductServicesInfoToString ( SubscriberInfo subscriberInfo, ProductServicesInfo productServicesInfo ) {
		StringBuilder sb = new StringBuilder();

		sb.append("ProductServices being set to for ");
		appendSubscriberInfo(sb, subscriberInfo).append("\r\n");

		appendServiceInfo(sb, "PricePlan", productServicesInfo.pricePlan );
		appendtServiceInfoArray(sb, "addtnlSrvs" , productServicesInfo.addtnlSrvs );
		appendtServiceInfoArray(sb, "promPricePlan" , productServicesInfo.promPricePlan );
		return sb.toString();
	}

	private static amdocs.APILink.datatypes.ServiceFeatureInfo[] buildServiceFeature(com.telus.eas.subscriber.info.ServiceFeatureInfo[] serviceFeatureInfo) {

		amdocs.APILink.datatypes.ServiceFeatureInfo[] serviceFeatures = null;

		if (serviceFeatureInfo != null && serviceFeatureInfo.length > 0) {
			LOGGER.debug("Processing " + serviceFeatureInfo.length + " features...");
			serviceFeatures = new amdocs.APILink.datatypes.ServiceFeatureInfo[serviceFeatureInfo.length];

			for (int i = 0; i < serviceFeatureInfo.length; i++) {
				// populate features
				serviceFeatures[i] = new amdocs.APILink.datatypes.ServiceFeatureInfo();
				serviceFeatures[i].featureCode = serviceFeatureInfo[i].getFeatureCode().trim();
				serviceFeatures[i].ftrParam = AttributeTranslator.emptyFromNull(serviceFeatureInfo[i].getParameter());

				// TODO: verify getParameter can be null
				if (serviceFeatureInfo[i].getParameter() != null && serviceFeatureInfo[i].getParameter().trim().length() == 0) {
					if (serviceFeatureInfo[i].getSwitchCode() !=null && serviceFeatureInfo[i].getSwitchCode().trim().equals(com.telus.eas.utility.info.FeatureInfo.CATEGORY_CODE_VOICE2TEXT)) {
						serviceFeatures[i].ftrParam = AttributeTranslator.emptyFromNull(serviceFeatureInfo[i].getParameterDefault());
					}
				}
				
				serviceFeatures[i].transactionType = serviceFeatureInfo[i].getTransaction();

				// delete feature if the expiry date is <= today
				if (serviceFeatureInfo[i].getExpiryDate() != null && !serviceFeatureInfo[i].getExpiryDate().after(new Date())) {
					serviceFeatures[i].transactionType = ServiceFeatureInfo.FTR_TRANSACTION_TYPE_DELETE;
				}

				// KB CR543 changes ( CDR Calling-circle BR )
				if (serviceFeatures[i].transactionType == ServiceFeatureInfo.FTR_TRANSACTION_TYPE_UPDATE && (serviceFeatureInfo[i].isCcParameterChanged())) {
					serviceFeatures[i].isFtrParamUpdate = true;
					if (serviceFeatureInfo[i].getCallingCircleCommitmentAttributeData() != null) {
						serviceFeatures[i].ftrParamEffDate = serviceFeatureInfo[i].getCallingCircleCommitmentAttributeData().getEffectiveDate();
					}
				}
			}
		}
		
		return serviceFeatures != null ? serviceFeatures : new amdocs.APILink.datatypes.ServiceFeatureInfo[0];
	}

	private static StringBuilder appendtServiceInfoArray(StringBuilder sb, String socType, ServiceInfo[] serviceInfos) {
		if (serviceInfos != null) {
			for (int i = 0; i < serviceInfos.length; i++) {
				appendServiceInfo(sb, socType, serviceInfos[i]);
			}
		}
		return sb;
	}

	private static StringBuilder  appendServiceInfo(StringBuilder sb, String socType, ServiceInfo serviceInfo) {
		sb.append( socType).append(" SOC    : " ).append( serviceInfo.soc.soc )
		.append(" transactionType:" ).append(AttributeTranslator.stringFrombyte(serviceInfo.soc.transactionType))
		.append( "  effDate:" + serviceInfo.soc.effDate).append( "  expDate:").append( serviceInfo.soc.expDate) 
		.append("\r\n");

		appendFeatureInfos(sb, serviceInfo.feature);
		appendMultiRingParameters(sb, serviceInfo.multiRingParamInfo);
		return sb;
	}



	private static StringBuilder  appendFeatureInfos(StringBuilder sb, amdocs.APILink.datatypes.ServiceFeatureInfo[] serviceFeatureInfos) {
		if (serviceFeatureInfos != null) {
			for (int i=0; i < serviceFeatureInfos.length; i++) {
				amdocs.APILink.datatypes.ServiceFeatureInfo serviceFeatureInfo = serviceFeatureInfos[i];
				sb.append("     Feature: ").append( serviceFeatureInfo.featureCode )
				.append(" transactionType:").append(AttributeTranslator.stringFrombyte(serviceFeatureInfo.transactionType))
				.append(" ftrParam:").append(serviceFeatureInfo.ftrParam).append( " msisdn:").append(serviceFeatureInfo.msisdn)
				.append(" ftrParamEffDate :").append(serviceFeatureInfo.ftrParamEffDate ).append( " isFtrParamUpdate :").append(serviceFeatureInfo.isFtrParamUpdate )
				.append("\r\n");
			}
		}
		return sb;
	}

	private static StringBuilder  appendMultiRingParameters(StringBuilder sb, MultiRingParameterInfo[] multiRingParamInfos) {
		if ( multiRingParamInfos!=null)	{
			for( int j=0; j<multiRingParamInfos.length ; j++ ) {
				MultiRingParameterInfo multiRingParamInfo = multiRingParamInfos[j];
				if (multiRingParamInfo!=null) {
					sb.append( "     MultiRingParam: " ).append(multiRingParamInfo.phoneNumber )
					.append(" transactionType:").append(AttributeTranslator.stringFrombyte( multiRingParamInfo.transactionType))
					.append("\r\n");
				}
			}
		}
		return sb;
	}

	public static StringBuilder appendSubscriberInfo( StringBuilder sb, SubscriberInfo subInfo )
	{
		if ( subInfo!=null ) {
			sb.append( "  Subscriber[ban:").append(subInfo.getBanId());
			if ( subInfo.getSubscriberId()!=null && subInfo.getSubscriberId().trim().length()>0)
				sb.append(", subId:").append( subInfo.getSubscriberId() );
			sb.append( ", phone:").append( subInfo.getPhoneNumber() )
			.append("]");
		}
		return sb;
	}

	public static void increaseExpectedNumberOfSubscribers(UpdateBanConv pUpdateBanConv
			, int pBan, int pUrbanId, int pFleetId) throws ValidateException, RemoteException, TelusException {

		UpdateFleetInfo amdocsUpdateFleetInfo = new UpdateFleetInfo();

		// Set BanPK (which also retrieves the BAN)
		pUpdateBanConv.setBanPK(pBan);

		// increase the number of subscribers by 1
		// - get list of fleets associated with this ban
		// - find fleet in question
		// - increase expectedSubscriberNumber by 1
		// (NOTE: future improvment would be to get a method that returns a specific fleet)
		amdocs.APILink.datatypes.FleetInfo[] associatedFleets = pUpdateBanConv.getFleetList();
		int fleetFound = -1;
		if (associatedFleets != null) {
			for (int i=0; i < associatedFleets.length; i++) {
				if (associatedFleets[i].urbanId == pUrbanId &&
						associatedFleets[i].fleetId == pFleetId) {
					fleetFound = i;
					break;
				}
			}
		}
		if (fleetFound < 0)
			throw new TelusApplicationException(new ExceptionInfo("VAL10011","Fleet not associated to BAN!"));

		// Populated UpdateFleetInfo
		amdocsUpdateFleetInfo.urbanId = associatedFleets[fleetFound].urbanId;
		amdocsUpdateFleetInfo.fleetId = associatedFleets[fleetFound].fleetId;
		amdocsUpdateFleetInfo.expectedSubscriberNumber = associatedFleets[fleetFound].expectedSubscriberNumber + 1;
		amdocsUpdateFleetInfo.scchInd = (byte)'S';
		LOGGER.debug("Excecuting modifyFleet() - start...");
		if (associatedFleets[fleetFound].fleetType == Fleet.TYPE_PUBLIC) {
			pUpdateBanConv.modifyFleet(amdocsUpdateFleetInfo);
		} else {
			FleetSecuredInfo amdocsFleetSecuredInfo = new FleetSecuredInfo();
			pUpdateBanConv.modifyFleet(amdocsUpdateFleetInfo,amdocsFleetSecuredInfo);
		}

		LOGGER.debug("Excecuting modifyFleet() - end...");
		LOGGER.debug("Leaving...");
		return;
	}

	public static ServiceInfo[] buildServiceInfo(ProductBaseConv productBaseConv, SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo) 
			throws RemoteException, ValidateException, ApplicationException {

		LOGGER.debug("Entering buildServiceInfo...");

		//ServiceInfo[] serviceInfos = null;
		ServiceAgreementInfo[] services = subscriberContractInfo.getServices0(true);

		// Size the array as # of SOCs plus priceplan SOC
		ArrayList<ServiceInfo> list = new ArrayList<ServiceInfo>(services.length + 1);
		//serviceInfo = new ServiceInfo[services.length + 1];

		// Handle all SOCs except priceplan SOC
		if (services != null) {
			for (ServiceAgreementInfo service : services) {

				// Disregard promotional services from the list
				if (StringUtils.equals(service.getServiceType(), "S")) {
					continue;
				}

				// TODO: May need to set parameter here for V2T CR
				LOGGER.debug("Processing SOC=[" + service.getServiceCode().trim() + "]...");
				SocInfo soc = new SocInfo();
				soc.soc = service.getServiceCode().trim();
				soc.transactionType = service.isWPS() ? SocInfo.SOC_TRANSACTION_TYPE_NO_CHG : service.getTransaction();
				if (isServiceEffectiveDateChange(service) && service.getTransaction() == BaseAgreementInfo.UPDATE && service.isWPS() == false) {
					// If the above conditions are met, the UPDATE transaction needs to be broken up into component ADD and DELETE transactions
					// for this particular service (i.e., this is what Amdocs expects)
					soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_INSERT; //BaseAgreementInfo.ADD;
					SocInfo originalSoc = new SocInfo();
					originalSoc.soc = service.getServiceCode().trim();
					originalSoc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_DELETE; //BaseAgreementInfo.DELETE;
					list.add(populateSocInformation(productBaseConv, subscriberInfo, subscriberContractInfo, service, originalSoc));
				}
				list.add(populateSocInformation(productBaseConv, subscriberInfo, subscriberContractInfo, service, soc));
			}
		}	
		
		// Handle priceplan SOC
		LOGGER.debug("Processing Price Plan=[" + subscriberContractInfo.getPricePlanCode().trim() + "]...");
		SocInfo soc = new SocInfo();
		soc.soc = subscriberContractInfo.getPricePlanCode().trim();
		soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_NO_CHG;

		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.multiRingParamInfo = null;
		serviceInfo.soc = soc;
		serviceInfo.feature = buildServiceFeature(subscriberContractInfo.getFeatures0(true));
		/**TODO: Voice 2 Text CR 091207 changes have been rolled back for February release.  To re-introduce the changes back in, please replace the line of code 
		 * following this block with the following:
		 *     
		 *    serviceInfo = findAndSetAdditionalFeatureParameters(pProductBaseConv, serviceInfo, pSubscriberInfo, ServiceAgreementInfo.SERVICE_TYPE_PRICEPLAN, true, true, true, true, null);
		 */
		serviceInfo = findAndSetAdditionalFeatureParameters(productBaseConv, serviceInfo, subscriberInfo, ServiceAgreementInfo.SERVICE_TYPE_PRICEPLAN, true, true, true, true, false);
		list.add(serviceInfo);

		ServiceInfo[] serviceInfos = list.toArray(new ServiceInfo[list.size()]);
		// Update SOC transaction type if necessary
		if (serviceInfos[serviceInfos.length - 1].feature != null) {
			for (int j = 0; j < serviceInfos[serviceInfos.length - 1].feature.length; j++) {
				if (serviceInfos[serviceInfos.length - 1].feature[j].transactionType != ServiceFeatureInfo.FTR_TRANSACTION_TYPE_NO_CHG) {
					if (soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_NO_CHG) {
					soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_UPDATE;
					}
					break;
				}
			}
		}

		// Remove services that have not changed
		serviceInfos = removeNoChangeServices(serviceInfos);
		
		LOGGER.debug("Leaving buildServiceInfo...");

		return serviceInfos;
	}
	
	private static boolean isServiceEffectiveDateChange(ServiceAgreementInfo service) {
		return (service.getEffectiveDate() != null && service.getOriginalEffectiveDate() != null && !service.getEffectiveDate().equals(service.getOriginalEffectiveDate()))
				|| (service.getEffectiveDate() == null && service.getOriginalEffectiveDate() != null);
	}
    		
	private static ServiceInfo populateSocInformation(ProductBaseConv productBaseConv, SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo,
			ServiceAgreementInfo info, SocInfo soc) throws RemoteException, ValidateException, ApplicationException {

		populateSocExpirationDate(soc, info, productBaseConv.getProductServices());
		soc.effDate = DateUtil.clearTimePortion(info.getEffectiveDate());
		if (info.getTransaction() == BaseAgreementInfo.DELETE || (info.getTransaction() == BaseAgreementInfo.UPDATE && soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_DELETE)) {
			// For DELETE transactions, we need to set the effective date to the original effective date (i.e., prior to any potential changes to the effective date that may have been applied
			// as part of this workflow), otherwise Amdocs will not be able to find the SOC
			soc.effDate = DateUtil.clearTimePortion(info.getOriginalEffectiveDate());
		}

		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.multiRingParamInfo = null;
		serviceInfo.soc = soc;
		serviceInfo.feature = buildServiceFeature(info.getFeatures0(true));
		// TODO: Voice 2 Text CR 091207 changes have been rolled back for February release.  To re-introduce the changes back in, please replace the line of code following this block with the following:
		//serviceInfo = findAndSetAdditionalFeatureParameters(productBaseConv, serviceInfo, subscriberInfo, services[i].getServiceType(), true, true, true, true, null);
		serviceInfo = findAndSetAdditionalFeatureParameters(productBaseConv, serviceInfo, subscriberInfo, info.getServiceType(), true, true, true, true, false);
		if (containsMultiRingFeature(subscriberInfo, info, subscriberContractInfo.getMultiRingInfos())) {
			serviceInfo.multiRingParamInfo = convertMultiRingInfos(subscriberContractInfo.getMultiRingInfos());
			LOGGER.debug("Service: multiRingInfos NOT NULL.");
		}

		return serviceInfo;
	}

	private static void populateSocExpirationDate(SocInfo socInfo, ServiceAgreementInfo serviceAgreementInfo, ProductServicesInfo productServicesInfo) {

		//socInfo.expDate = saInfo.getExpiryDate() == null ? socInfo.transactionType == SocInfo.SOC_TRANSACTION_TYPE_UPDATE ? SocInfo.NEVER_EXPIRE_DATE : null : saInfo.getExpiryDate();
		
		// Defect PROD00141040 background:
		// When front-end didn't change expirationDate, but change some feature's parameter for that SOC, the transaction type would still be 'update', so the above statement cause
		// SocInfo.NEVER_EXPIRE_DATE to be sent to KB. NEVER_EXPIRE_DATE has special meaning to KB, which intended for unexpiring an expired SOC, during unexpiring process, KB will
		// also make some rate proration adjustment, as such unwanted credit adjustment get applied for calling-circle SOC.
		
		// The following block is intended to fix issue for above statement.
		socInfo.expDate = null;
		if (serviceAgreementInfo.getExpiryDate() != null) {
			if (isServiceEffectiveDateChange(serviceAgreementInfo) && socInfo.transactionType == SocInfo.SOC_TRANSACTION_TYPE_INSERT
					&& StringUtils.equals(serviceAgreementInfo.getBillCycleTreatmentCode(), "BCIC")) {
				// Special handling for BCIC SOCs when there is a change in the effective date (i.e., future-dated SOC change), otherwise Amdocs does not recalculate the expiration date
				socInfo.expDate = null;
			} else {
				socInfo.expDate = DateUtil.clearTimePortion(serviceAgreementInfo.getExpiryDate());
			}
		} else {
			if (socInfo.transactionType == SocInfo.SOC_TRANSACTION_TYPE_UPDATE) {
				// If this is an update transaction, we need to further check the current expiration date
				Date currentExpDate = getExpiryDate(productServicesInfo, serviceAgreementInfo.getCode().trim());
				if (currentExpDate != null) {
					// When the current expiration date is not null, then we unexpire the SOC
					socInfo.expDate = DateUtil.clearTimePortion(SocInfo.NEVER_EXPIRE_DATE);
				}
			}
		}
	}

	private static Date getExpiryDate(ProductServicesInfo productServicesInfo, String socCode) {
		
		ServiceInfo[] services = productServicesInfo.addtnlSrvs;
		for (ServiceInfo service : services) {
			SocInfo soc = service.soc;
			if (soc.soc.trim().endsWith(socCode)) {
				return soc.expDate;
			}
		}
		
		return null;
	}

	public static void associateFleetAndTGsToBan(UpdateBanConv amdocsUpdateBanConv, IDENSubscriberInfo pIdenSubscriberInfo, com.telus.eas.account.info.TalkGroupInfo[] pTalkGroups, String principal,
			String credential, String application) throws ValidateException, RemoteException, ApplicationException {

		IdenResourcesInfo idenResourcesInfo = new IdenResourcesInfo();

		com.telus.eas.account.info.FleetInfo fleetInfo = new com.telus.eas.account.info.FleetInfo();

		fleetInfo.getIdentity0().setUrbanId(pIdenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().getUrbanId());
		fleetInfo.getIdentity0().setFleetId(pIdenSubscriberInfo.getMemberIdentity0().getFleetIdentity0().getFleetId());

		LOGGER.debug("Checking fleet: " + fleetInfo.getIdentity0().getUrbanId() + "*" + fleetInfo.getIdentity0().getFleetId() + " ...");

		// Set BanPK (which also retrieves the BAN)
		amdocsUpdateBanConv.setBanPK(pIdenSubscriberInfo.getBanId());

		// get list of fleet associated to this ban
		LOGGER.debug("Calling getFleetList()...");
		boolean fleetAlreadyAssociated = false;
		amdocs.APILink.datatypes.FleetInfo[] associatedFleets = amdocsUpdateBanConv.getFleetList();

		// check that fleet has been associate to this ban
		if (associatedFleets != null) {
			LOGGER.debug("# of associated fleets: " + associatedFleets.length);
			for (int i = 0; i < associatedFleets.length; i++) {
				LOGGER.debug("associated fleet: " + associatedFleets[i].urbanId + "*" + associatedFleets[i].fleetId);
				if (associatedFleets[i].urbanId == fleetInfo.getIdentity0().getUrbanId() && associatedFleets[i].fleetId == fleetInfo.getIdentity0().getFleetId()) {
					fleetAlreadyAssociated = true;
					break;
				}
			}
		}

		LOGGER.debug("fleet already associated: " + fleetAlreadyAssociated);

		// Retrieve the identity associated to this current session.
		String sessionId = getAccountLifecycleManager().openSession(principal, credential, application);

		// Fleet not associated to BAN
		// - associate fleet
		// - associate all talk groups to BAN
		if (fleetAlreadyAssociated == false) {

			// associate fleet to BAN
			LOGGER.debug("Calling addFleet()...");

			getAccountLifecycleManager().addFleet(pIdenSubscriberInfo.getBanId(), (short) idenResourcesInfo.getNetwork(), fleetInfo, 1, sessionId);

			// associate all talk groups to BAN (if talk groups have been passed in)
			LOGGER.debug("Associate all talk groups to BAN...");
			for (int i = 0; i < pTalkGroups.length; i++) {
				com.telus.eas.account.info.TalkGroupInfo talkGroupInfo = new com.telus.eas.account.info.TalkGroupInfo();
				talkGroupInfo.getFleetIdentity().setUrbanId(fleetInfo.getIdentity0().getUrbanId());
				talkGroupInfo.getFleetIdentity().setFleetId(fleetInfo.getIdentity0().getFleetId());
				talkGroupInfo.setTalkGroupId(pTalkGroups[i].getTalkGroupId());

				LOGGER.debug("Calling addTalkGroup() for id: " + talkGroupInfo.getTalkGroupId() + "...");
				getAccountLifecycleManager().addTalkGroup(pIdenSubscriberInfo.getBanId(), talkGroupInfo, sessionId);
			}
		}

		// Fleet alread associated to BAN
		// - check all TGs passed in and associate them to BAN if necessary
		if (fleetAlreadyAssociated && pTalkGroups.length > 0) {
			// get all talkgroups currently associated
			UrbanFleetId urbanFleetId = new UrbanFleetId();
			urbanFleetId.urbanId = fleetInfo.getIdentity0().getUrbanId();
			urbanFleetId.fleetId = fleetInfo.getIdentity0().getFleetId();

			LOGGER.debug("Getting talk groups by ban..");

			TalkGroupInfo[] associatedTalkGroups = (TalkGroupInfo[]) getAccountInformationHelper().retrieveTalkGroupsByBan(pIdenSubscriberInfo.getBanId()).toArray(new TalkGroupInfo[0]);
			associatedTalkGroups = associatedTalkGroups == null ? new TalkGroupInfo[0] : associatedTalkGroups;

			for (TalkGroupInfo talkGroupInfo : associatedTalkGroups) {
				LOGGER.debug(talkGroupInfo.getFleetIdentity().getUrbanId() + "*" + talkGroupInfo.getFleetIdentity().getFleetId() + "*" + talkGroupInfo.getTalkGroupId());
			}

			// associate all talk groups to BAN
			LOGGER.debug("Associate all talk groups to BAN...");
			for (int i = 0; i < pTalkGroups.length; i++) {
				int j;
				for (j = 0; j < associatedTalkGroups.length; j++) {
					// associate talkgroug if not already associated
					if (pTalkGroups[i].getFleetIdentity().getUrbanId() == associatedTalkGroups[j].getFleetIdentity().getUrbanId()
							&& pTalkGroups[i].getFleetIdentity().getFleetId() == associatedTalkGroups[j].getFleetIdentity().getFleetId()
							&& (short) pTalkGroups[i].getTalkGroupId() == associatedTalkGroups[j].getTalkGroupId())
						break;
				}

				if (j > associatedTalkGroups.length - 1) {
					com.telus.eas.account.info.TalkGroupInfo talkGroupInfo = new com.telus.eas.account.info.TalkGroupInfo();
					talkGroupInfo.getFleetIdentity().setUrbanId(fleetInfo.getIdentity0().getUrbanId());
					talkGroupInfo.getFleetIdentity().setFleetId(fleetInfo.getIdentity0().getFleetId());
					talkGroupInfo.setTalkGroupId(pTalkGroups[i].getTalkGroupId());

					LOGGER.debug("Calling addTalkGroup() for fleet: " + talkGroupInfo.getFleetIdentity().getUrbanId() + "*" + talkGroupInfo.getFleetIdentity().getFleetId() + " id: "
							+ talkGroupInfo.getTalkGroupId() + "...");
					getAccountLifecycleManager().addTalkGroup(pIdenSubscriberInfo.getBanId(), talkGroupInfo, sessionId);
				}
			}
		}
	}

	public static AccountInformationHelper getAccountInformationHelper() {
		if (accountInformationHelper == null) {
			accountInformationHelper = EJBUtil.getHelperProxy(AccountInformationHelper.class, EJBUtil.TELUS_CMBSERVICE_ACCOUNT_INFORMATION_HELPER );
		}
		return accountInformationHelper;
	}


	public static AccountLifecycleManager getAccountLifecycleManager() {
		if (accountLifecycleManager == null) {
			accountLifecycleManager = EJBUtil.getHelperProxy(AccountLifecycleManager.class, EJBUtil.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_MANAGER);
		}
		return accountLifecycleManager;
	}	

	public static ServiceInfo[] buildPromotionalServiceInfo(ProductBaseConv pProductBaseConv, SubscriberInfo pSubscriberInfo, 
			ServiceInfo[] pRegularServices) throws RemoteException, ValidateException, ApplicationException  {

		ServiceInfo[] serviceInfoArray = null;
		Vector<ServiceInfo> amdocsServices = new Vector<ServiceInfo>();
		ServiceInfo serviceInfo = null;

		if (pRegularServices != null){
			for (int i = 0; i < pRegularServices.length; i++) {
				if (pRegularServices[i].soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_NO_CHG ||
						pRegularServices[i].soc.transactionType == SocInfo.SOC_TRANSACTION_TYPE_DELETE)
					continue;

				SocProfileInfo promotionalSoc = pProductBaseConv.getPromotionalSoc(pRegularServices[i].soc.soc);
				if (promotionalSoc != null) {
					serviceInfo = new ServiceInfo();
					serviceInfo.multiRingParamInfo = null;
					serviceInfo.soc.soc = promotionalSoc.soc;
					/**TODO: Voice 2 Text CR 091207 changes have been rolled back for February release.  
					 * To re-introduce the changes back in, please replace the 2 lines of code 
					 * following this block with the following:
					 *            
					 *          serviceInfo.soc.transactionType = pRegularServices[i].soc.transactionType;
					 *          serviceInfo = findAndSetAdditionalFeatureParameters(pProductBaseConv, serviceInfo,pSubscriberInfo,
					 *          AttributeTranslator.stringFrombyte(promotionalSoc.serviceType),true,true,true,true,pRegularServices[i]);
					 */
					serviceInfo.soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_INSERT;
					serviceInfo = findAndSetAdditionalFeatureParameters(pProductBaseConv, serviceInfo,pSubscriberInfo,
							AttributeTranslator.stringFrombyte(promotionalSoc.serviceType),true,true,true,true, false);
					amdocsServices.add(serviceInfo);
				}
			}
		}
		serviceInfoArray = amdocsServices.toArray(new ServiceInfo[amdocsServices.size()]);

		// remove services that have not changed
		serviceInfoArray = removeNoChangeServices(serviceInfoArray);
		return serviceInfoArray;
	}

	public static CellularEquipmentInfo[] populateChangeEquipmentArray(
			CellularEquipmentInfo[] oldEquipmentInfoArray,
			com.telus.eas.equipment.info.EquipmentInfo newPrimaryEquipmentInfo,
			com.telus.eas.equipment.info.EquipmentInfo [] newSecondaryEquipmentInfo,
			com.telus.eas.equipment.info.EquipmentInfo oldEquipmentInfo) {

		TreeMap<String, CellularEquipmentInfo> equipmentHash = null;
		String currentPrimaryEquipmentSerialNumber = "";

		boolean processPrimaryEquipmentOnly = newSecondaryEquipmentInfo == null;


		// put old/current equipment into hash table
		// - default equipment to NO_CHANGE or DELETE depending on whether secondary equipment
		//   was passed in or not
		// - also store current primary equipment serial number
		LOGGER.debug("Current Equipment ...");
		equipmentHash = new TreeMap<String, CellularEquipmentInfo>();
		for (int i=0; i < oldEquipmentInfoArray.length; i++) {
			LOGGER.debug(
					"  oldEquipmentInfoArray[" + i + "]: " +
					"serialNumber=[" + oldEquipmentInfoArray[i].serialNumber + "] " +
					"activateInd=[" + oldEquipmentInfoArray[i].activateInd + "] " +
					"primaryInd=[" + oldEquipmentInfoArray[i].primaryInd + "]");

			if ( newPrimaryEquipmentInfo.isHSPA() ) {
				//defect PROD00130518, there is no secondary equipment in HSPA world, mark all equipmentMode as DELETE 
				oldEquipmentInfoArray[i].equipmentMode = amdocs.APILink.datatypes.EquipmentInfo.DELETE;
			} else {
				oldEquipmentInfoArray[i].equipmentMode =
					processPrimaryEquipmentOnly && !oldEquipmentInfoArray[i].primaryInd ?
							amdocs.APILink.datatypes.EquipmentInfo.NO_CHANGE :
								amdocs.APILink.datatypes.EquipmentInfo.DELETE;
			}
			if (oldEquipmentInfoArray[i].primaryInd == true) currentPrimaryEquipmentSerialNumber = oldEquipmentInfoArray[i].serialNumber;
			equipmentHash.put(oldEquipmentInfoArray[i].serialNumber, oldEquipmentInfoArray[i]);
		}

		ImsiInfo[] imsiList = null;
		boolean isNetworkChange = false;

		// I. primary equipment
		// --------------------
		String newPrimarySerial = newPrimaryEquipmentInfo.getSerialNumber();
		if ( newPrimaryEquipmentInfo.isUSIMCard() ) {
			newPrimarySerial = com.telus.eas.equipment.info.EquipmentInfo.DUMMY_ESN_FOR_USIM;
		}

		if (equipmentHash.get(newPrimarySerial) != null) {
			// if new primary equipment exists
			// - set equipmentMode to UPDATE
			// - set primary indicator to true

			CellularEquipmentInfo cellularEquipmentInfo = equipmentHash.get(newPrimarySerial);
			cellularEquipmentInfo.equipmentMode = amdocs.APILink.datatypes.EquipmentInfo.UPDATE;
			cellularEquipmentInfo.activateInd = true;
			cellularEquipmentInfo.primaryInd = true;

			if ( newPrimaryEquipmentInfo.isUSIMCard() ) { 
				//HSPA to HSPA 
				imsiList = extractImsiInfos(newPrimaryEquipmentInfo, oldEquipmentInfo);
			}
			else { 
				//CDMA to CDMA
				//do nothing special
			}

		} else {
			// new primary equipment does not exist
			// - add to hash map as INSERT
			// - change current primary to DELETE

			CellularEquipmentInfo newPrimaryCellularEquipmentInfo = new CellularEquipmentInfo();
			newPrimaryCellularEquipmentInfo.equipmentMode = amdocs.APILink.datatypes.EquipmentInfo.INSERT;
			newPrimaryCellularEquipmentInfo.serialNumber = newPrimarySerial;
			newPrimaryCellularEquipmentInfo.activateInd = true;
			newPrimaryCellularEquipmentInfo.primaryInd = true;
			newPrimaryCellularEquipmentInfo.base = CellularEquipmentInfo.EQUIPMENT_BASE_DECIMAL;
			newPrimaryCellularEquipmentInfo.equipmentType = AttributeTranslator.byteFromString(newPrimaryEquipmentInfo.getEquipmentType());
			newPrimaryCellularEquipmentInfo.possession = (byte)'P';

			CellularEquipmentInfo currntPrimaryEquipmentInfo = equipmentHash.get(currentPrimaryEquipmentSerialNumber);
			currntPrimaryEquipmentInfo.equipmentMode = amdocs.APILink.datatypes.EquipmentInfo.DELETE;
			currntPrimaryEquipmentInfo.activateInd = false;
			currntPrimaryEquipmentInfo.primaryInd = false;


			if ( currentPrimaryEquipmentSerialNumber.equals( com.telus.eas.equipment.info.EquipmentInfo.DUMMY_ESN_FOR_USIM) ) {
				//HSPA to CDMA
				isNetworkChange=true;
				imsiList = extractImsiInfo(oldEquipmentInfo, ImsiInfo.DELETE );
			} 
			else if ( newPrimaryEquipmentInfo.isUSIMCard() ) { 
				//CDMA to HSPA
				isNetworkChange=true;
				imsiList = extractImsiInfo( newPrimaryEquipmentInfo, ImsiInfo.INSERT );
			}

			equipmentHash.put(newPrimaryCellularEquipmentInfo.serialNumber, newPrimaryCellularEquipmentInfo);
		}

		// II. secondary equipment
		// -----------------------
		if (!processPrimaryEquipmentOnly) {
			for (int i=0; i < newSecondaryEquipmentInfo.length; i++) {
				// if new secondary equipment exists
				// - set equipmentMode to UPDATE
				// - set primary indicator to false UNLESS this equipment became the primary
				if (equipmentHash.get(newSecondaryEquipmentInfo[i].getSerialNumber()) != null) {
					CellularEquipmentInfo cellularEquipmentInfo = equipmentHash.get(newSecondaryEquipmentInfo[i].getSerialNumber());
					cellularEquipmentInfo.equipmentMode = amdocs.APILink.datatypes.EquipmentInfo.UPDATE;
					cellularEquipmentInfo.activateInd = true;
					cellularEquipmentInfo.primaryInd =
						newSecondaryEquipmentInfo[i].getSerialNumber().equals(newPrimarySerial)
						? true : false;
					equipmentHash.put(cellularEquipmentInfo.serialNumber, cellularEquipmentInfo);

					// if new primary equipment does not exist
					// - add to hash map as INSERT
				} else {
					CellularEquipmentInfo cellularEquipmentInfo = new CellularEquipmentInfo();
					cellularEquipmentInfo.equipmentMode = amdocs.APILink.datatypes.EquipmentInfo.INSERT;
					cellularEquipmentInfo.serialNumber = newSecondaryEquipmentInfo[i].getSerialNumber();
					cellularEquipmentInfo.activateInd = true;
					cellularEquipmentInfo.primaryInd = false;
					cellularEquipmentInfo.base = CellularEquipmentInfo.EQUIPMENT_BASE_DECIMAL;
					cellularEquipmentInfo.equipmentType = AttributeTranslator.byteFromString(newSecondaryEquipmentInfo[i].getEquipmentType());
					cellularEquipmentInfo.possession = (byte)'P';
					equipmentHash.put(cellularEquipmentInfo.serialNumber, cellularEquipmentInfo);
				}
			}
		}

		CellularEquipmentInfo[] equipments = equipmentHash.values().toArray(
				new CellularEquipmentInfo[equipmentHash.size()]);

		if ( imsiList!=null) {
			//This means there are IMSI need to be added or deleted

			//As per KB 9.9, always put imsiList and isNetworkChagne flag to the first element in the array
			//and it seem the equipmentMode affect the processing of imsiList, so we have to make sure
			//that the first element is not equipmentMode=D.

			//sort the CellularEquipment by equipmentMode in following order: 'I'- first; 'U'-second; 'D'-last
			Arrays.sort( equipments, new CellularEquipmentModeComparator() );

			//put imsiList and isNetworkChagne flag to the first element in the array
			equipments[0].isNetworkChange = isNetworkChange;

			//when change HSPA to CDMA, if we send imsi list with 'DELETE', we will get a error:
			//   id=1116460; No need to provide the IMSI list for this change
			//not sure this is per KB design or a bug.
			//work-around: only when new equipment is USIMCard, then we send imsiList
			if ( newPrimaryEquipmentInfo.isUSIMCard() )
				equipments[0].imsiList = imsiList;
		}

		return equipments;
	}

	public static ImsiInfo[] extractImsiInfo(com.telus.eas.equipment.info.EquipmentInfo equipmentInfo, byte transactionType) {
		/**
		 *  V3SIM requirement - if remote IMSI is null or "", do not add into return list.
		 */
		ImsiInfo localIMSI = new ImsiInfo();
		localIMSI.imsi = equipmentInfo.getProfile().getLocalIMSI();
		localIMSI.transactionType = transactionType;

		ImsiInfo remoteIMSI = new ImsiInfo();
		remoteIMSI.imsi = equipmentInfo.getProfile().getRemoteIMSI();
		remoteIMSI.transactionType = transactionType;
		
		return (remoteIMSI.imsi != null && remoteIMSI.imsi.trim().length() > 0) ?
			new ImsiInfo[]{localIMSI, remoteIMSI} : new ImsiInfo[]{localIMSI};
	}

	public static String extractCellularEquipmentInfoArray( SubscriberInfo subscriberInfo, CellularEquipmentInfo[] equipments ) {
		StringBuilder sb = new StringBuilder();

		sb.append("CellularEquipmentInfo array being passed to Amdocs to for ");
		appendSubscriberInfo(sb, subscriberInfo).append("\r\n");
		for (int i=0; i < equipments.length; i++) {
			sb.append("  CellularEquipmentInfo[").append( i ).append( "]: ").append("\r\n    ");
			appendEquipmentInfo( sb, equipments[i]);
		}
		return sb.toString();
	}

	private static StringBuilder appendEquipmentInfo ( StringBuilder sb, CellularEquipmentInfo equipment ) {

		sb.append("equipmentMode=[").append(AttributeTranslator.stringFrombyte(equipment.equipmentMode)).append( "] ") //this equals to transactionType
		.append("serialNumber=[").append( equipment.serialNumber).append( "] ")
		.append("primaryInd=[").append( equipment.primaryInd ).append( "] ")
		.append("isNetworkChange=[").append( equipment.isNetworkChange).append( "] ")
		.append("equipmentType=[" ).append( AttributeTranslator.stringFrombyte(equipment.equipmentType) ).append( "] ")
		.append("activateInd=[").append( equipment.activateInd ).append( "] ")
		.append("base=[" ).append( equipment.base ).append( "] ")
		.append("possession=[" ).append( equipment.possession ).append( "] ")
		.append("activityReason=[").append( equipment.activityReason).append("] ");
		sb.append("\r\n");
		if ( equipment.imsiList==null ) {
			sb.append("      imsiList[]=null\r\n");
		} else {
			ImsiInfo[] imsiInfos =equipment.imsiList; 
			for( int i=0; i<imsiInfos.length; i++  ) {
				ImsiInfo imsiInfo = imsiInfos[i];
				sb.append("      ismiInfo[").append(i).append("]: ")
				.append( "imsi[" ).append( imsiInfo.imsi).append("] ")
				.append( "transactionType[" ).append( AttributeTranslator.stringFrombyte(imsiInfo.transactionType)).append("] ")
				//.append( "ctnStatus[" ).append( imsiInfo.ctnStatus).append("] ")
				.append( "\r\n");
			}
		}
		return sb;
	}

	public static ImsiInfo[] extractImsiInfos(
			com.telus.eas.equipment.info.EquipmentInfo newPrimaryEquipmentInfo,
			com.telus.eas.equipment.info.EquipmentInfo oldEquipmentInfo) {
		ImsiInfo[] imsiToBeRemoved = extractImsiInfo( oldEquipmentInfo, ImsiInfo.DELETE );
		ImsiInfo[] imsiToBeAdded = extractImsiInfo( newPrimaryEquipmentInfo, ImsiInfo.INSERT );
		ImsiInfo[] imsiToBeSent = new ImsiInfo[ imsiToBeRemoved.length + imsiToBeAdded.length ];
		System.arraycopy( imsiToBeRemoved, 0, imsiToBeSent, 0, imsiToBeRemoved.length );
		System.arraycopy( imsiToBeAdded, 0, imsiToBeSent, imsiToBeRemoved.length,imsiToBeAdded.length );
		return imsiToBeSent;
	}
	
	public static class CellularEquipmentModeComparator implements java.util.Comparator<CellularEquipmentInfo> {

		public int compare(CellularEquipmentInfo o1, CellularEquipmentInfo o2) {
			CellularEquipmentInfo e1 = ( CellularEquipmentInfo) o1;
			CellularEquipmentInfo e2 = ( CellularEquipmentInfo) o2;
			if (e1.equipmentMode==e2.equipmentMode ) {
				return 0;
			}
			else if ( e1.equipmentMode==CellularEquipmentInfo.INSERT ) {
				return -1;
			}
			else if ( e1.equipmentMode==CellularEquipmentInfo.UPDATE) {
				if ( e2.equipmentMode == CellularEquipmentInfo.INSERT )
					return 1;
				else
					return -1;
			}
			else if ( e1.equipmentMode==CellularEquipmentInfo.NO_CHANGE) {
				if ( e2.equipmentMode == CellularEquipmentInfo.INSERT  || e2.equipmentMode == CellularEquipmentInfo.UPDATE)
					return 1;
				else 
					return -1;
			}
			else { //CellularEquipmentInfo.DELETE)
				return 1;			
			}
		}	
	}

	public static PagerEquipmentInfo[] populateChangeEquipmentArray(PagerEquipmentInfo[] oldEquipmentInfoArray,
		    com.telus.eas.equipment.info.EquipmentInfo newPrimaryEquipmentInfo,
		    com.telus.eas.equipment.info.EquipmentInfo[] newSecondaryEquipmentInfo) {

		    boolean processPrimaryEquipmentOnly = newSecondaryEquipmentInfo == null;

		    // put old/current equipment into hash table.
		    // - default equipment to NO_CHANGE or DELETE depending on whether secondary equipment was passed in or not.
		    // - also store current primary equipment serial number.
		    LOGGER.debug("Current Equipment ...");

		    String currentPrimaryEquipmentSerialNumber = "";
		    TreeMap<String, PagerEquipmentInfo> equipmentHash = new TreeMap<String, PagerEquipmentInfo>();

		    for (int i = 0; i < oldEquipmentInfoArray.length; i++) {
		      StringBuffer sb = new StringBuffer();
		      sb.append("oldEquipmentInfoArray[").append(i).append("] {\n");
		      sb.append("    serialNumber=[").append(oldEquipmentInfoArray[i].serialNumber).append("]\n");
		      sb.append("    coverageRegion=[").append(oldEquipmentInfoArray[i].coverageRegion).append("]\n");
		      sb.append("    encodingFormat=[").append(oldEquipmentInfoArray[i].encodingFormat).append("]\n");
		      sb.append("    activateInd=[").append(oldEquipmentInfoArray[i].activateInd).append("]\n");
		      sb.append("    primaryInd=[").append(oldEquipmentInfoArray[i].primaryInd).append("]\n");
		      sb.append("}");
		      LOGGER.debug(sb.toString());

		      oldEquipmentInfoArray[i].equipmentMode =
		        processPrimaryEquipmentOnly && !oldEquipmentInfoArray[i].primaryInd ? EquipmentInfo.NO_CHANGE : EquipmentInfo.DELETE;

		      if (oldEquipmentInfoArray[i].primaryInd)
		        currentPrimaryEquipmentSerialNumber = oldEquipmentInfoArray[i].serialNumber;

		      equipmentHash.put(oldEquipmentInfoArray[i].serialNumber, oldEquipmentInfoArray[i]);
		    }

		    // I. primary equipment
		    // --------------------
		    // if new primary equipment exists
		    // - set equipmentMode to UPDATE
		    // - set primary indicator to true
		    PagerEquipmentInfo pagerEquipmentInfo = equipmentHash.get(newPrimaryEquipmentInfo.getFormattedCapCode());
		    if (pagerEquipmentInfo != null) {
		      pagerEquipmentInfo.equipmentMode = EquipmentInfo.UPDATE;
		      pagerEquipmentInfo.equipmentType = AttributeTranslator.byteFromString(newPrimaryEquipmentInfo.getEquipmentType());
		      pagerEquipmentInfo.coverageRegion = Integer.parseInt(newPrimaryEquipmentInfo.getCurrentCoverageRegionCode());
		      pagerEquipmentInfo.encodingFormat = newPrimaryEquipmentInfo.getEncodingFormat();
		      pagerEquipmentInfo.possession = newPrimaryEquipmentInfo.getPossession() != null ? (byte) newPrimaryEquipmentInfo.getPossession().charAt(0) : (byte) 'P';
		      pagerEquipmentInfo.activateInd = true;
		      pagerEquipmentInfo.primaryInd = true;
		      equipmentHash.put(pagerEquipmentInfo.serialNumber, pagerEquipmentInfo);
		    }
		    else {
		      // if new primary equipment does not exist
		      // - add to hash map as INSERT
		      pagerEquipmentInfo = new PagerEquipmentInfo();
		      pagerEquipmentInfo.equipmentMode = EquipmentInfo.INSERT;
		      pagerEquipmentInfo.equipmentType = AttributeTranslator.byteFromString(newPrimaryEquipmentInfo.getEquipmentType());
		      pagerEquipmentInfo.serialNumber = newPrimaryEquipmentInfo.getFormattedCapCode();
		      pagerEquipmentInfo.coverageRegion = Integer.parseInt(newPrimaryEquipmentInfo.getCurrentCoverageRegionCode());
		      pagerEquipmentInfo.encodingFormat = newPrimaryEquipmentInfo.getEncodingFormat();
		      pagerEquipmentInfo.serialNumber = newPrimaryEquipmentInfo.getFormattedCapCode();
		      pagerEquipmentInfo.activateInd = true;
		      pagerEquipmentInfo.primaryInd = true;
		      pagerEquipmentInfo.possession = newPrimaryEquipmentInfo.getPossession() != null ? (byte) newPrimaryEquipmentInfo.getPossession().charAt(0) : (byte) 'P';
		      equipmentHash.put(pagerEquipmentInfo.serialNumber, pagerEquipmentInfo);

		      // - change current primary to DELETE
		      pagerEquipmentInfo = equipmentHash.get(currentPrimaryEquipmentSerialNumber);
		      pagerEquipmentInfo.equipmentMode = EquipmentInfo.DELETE;
		      pagerEquipmentInfo.activateInd = false;
		      pagerEquipmentInfo.primaryInd = false;
		      equipmentHash.put(pagerEquipmentInfo.serialNumber, pagerEquipmentInfo);
		    }

		    // II. secondary equipment
		    if (!processPrimaryEquipmentOnly) {
		      for (int i = 0; i < newSecondaryEquipmentInfo.length; i++) {
		        // if new secondary equipment exists
		        // - set equipmentMode to UPDATE
		        // - set primary indicator to false UNLESS this equipment became the primary
		        pagerEquipmentInfo = equipmentHash.get(newSecondaryEquipmentInfo[i].getFormattedCapCode());
		        if (pagerEquipmentInfo != null) {
		          pagerEquipmentInfo.equipmentMode = EquipmentInfo.UPDATE;
		          pagerEquipmentInfo.equipmentType = AttributeTranslator.byteFromString(newSecondaryEquipmentInfo[i].getEquipmentType());
		          pagerEquipmentInfo.coverageRegion = Integer.parseInt(newSecondaryEquipmentInfo[i].getCurrentCoverageRegionCode());
		          pagerEquipmentInfo.encodingFormat = newSecondaryEquipmentInfo[i].getEncodingFormat();
		          pagerEquipmentInfo.possession = newSecondaryEquipmentInfo[i].getPossession() != null ? (byte) newSecondaryEquipmentInfo[i].getPossession().charAt(0) : (byte) 'P';
		          pagerEquipmentInfo.activateInd = true;
		          pagerEquipmentInfo.primaryInd = newSecondaryEquipmentInfo[i].getFormattedCapCode().equals(newPrimaryEquipmentInfo.getFormattedCapCode());
		          equipmentHash.put(pagerEquipmentInfo.serialNumber, pagerEquipmentInfo);
		        }
		        else {
		          // if new primary equipment does not exist
		          // - add to hash map as INSERT
		          pagerEquipmentInfo = new PagerEquipmentInfo();
		          pagerEquipmentInfo.equipmentMode = EquipmentInfo.INSERT;
		          pagerEquipmentInfo.equipmentType = AttributeTranslator.byteFromString(newSecondaryEquipmentInfo[i].getEquipmentType());
		          pagerEquipmentInfo.serialNumber = newSecondaryEquipmentInfo[i].getFormattedCapCode();
		          pagerEquipmentInfo.coverageRegion = Integer.parseInt(newSecondaryEquipmentInfo[i].getCurrentCoverageRegionCode());
		          pagerEquipmentInfo.encodingFormat = newSecondaryEquipmentInfo[i].getEncodingFormat();
		          pagerEquipmentInfo.activateInd = true;
		          pagerEquipmentInfo.primaryInd = false;
		          pagerEquipmentInfo.possession = newSecondaryEquipmentInfo[i].getPossession() != null ? (byte) newSecondaryEquipmentInfo[i].getPossession().charAt(0) : (byte) 'P';
		          equipmentHash.put(pagerEquipmentInfo.serialNumber, pagerEquipmentInfo);
		        }
		      }
		    }
		    // convert hash to new equipment info array
		    Collection<PagerEquipmentInfo> newEquipmentInfoList = equipmentHash.values();

		    return newEquipmentInfoList.toArray(new PagerEquipmentInfo[newEquipmentInfoList.size()]);
		  }
	
	public static String extractServiceInfosToString( SubscriberInfo subscriberInfo, ServiceInfo[] regularServiceInfos, ServiceInfo[] promoServiceInfos ) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("Services passed into for ");
		appendSubscriberInfo(sb, subscriberInfo).append("\r\n");
		appendtServiceInfoArray(sb, "Regular", regularServiceInfos);
		appendtServiceInfoArray(sb, "Promo", promoServiceInfos);
		return sb.toString();
	}
	
	private static void clearServiceFeatureParameterDate(ProductServicesInfo productServicesInfo) {
		clearServiceFeatureParameterDate(productServicesInfo.pricePlan.feature);
		for (ServiceInfo serviceInfo : productServicesInfo.addtnlSrvs) {
			clearServiceFeatureParameterDate(serviceInfo.feature);
		}
	}
	
	private static void clearServiceFeatureParameterDate(ServiceFeatureInfo[] serviceFeatures) {
		for (ServiceFeatureInfo serviceFeature : serviceFeatures) {
			serviceFeature.ftrParamEffDate = null;
		}
	}
	
	public static ProductServicesInfo mapTelusContractToAmdocsProductServicesForActivation(
			ProductBaseConv pProductBaseConv,
			SubscriberInfo pSubscriberInfo,
			SubscriberContractInfo pSubscriberContractInfo,
			boolean processIncludedSocs,
			boolean processRegularSocs, boolean portInMSISDN) throws RemoteException, ValidateException, ApplicationException {
		
		ProductServicesInfo productServicesInfo = DaoSupport.mapToAmdocsProductServices(pProductBaseConv, pSubscriberInfo, pSubscriberContractInfo, processIncludedSocs, processRegularSocs, false, portInMSISDN);
		
		// November 08,2012 mliao defect fix: 
		// In the following scenario, the activation will fail
		// 1)	future dated activation 
		// 2)	contract contain myFave service ( included or optional )
		// 3)	myFave phone number list is specified
		// 
		// Root cause: 
		// At the time we calculate the CC feature parameter effective date, we don't know if this is future dated activation or not, 
		// so the parameter effective date is probably set to current logical date. If activation is future dated, this will result the parameter effective
		// date is earlier than the activate service effective date , which fails KB-API's validation rule
		//  
		// Solution:
		//
		//  clear out the feature parameter effective date for all activation
		clearServiceFeatureParameterDate(productServicesInfo);

		// print ProductServices
		LOGGER.info(extractProductServicesInfoToString(pSubscriberInfo, productServicesInfo));

		return productServicesInfo;
		
	}

	public static ProductServicesInfo mapTelusContractToAmdocsProductServices(
			ProductBaseConv pProductBaseConv,
			SubscriberInfo pSubscriberInfo,
			SubscriberContractInfo pSubscriberContractInfo,
			boolean processIncludedSocs,
			boolean processRegularSocs, boolean pricePlanModified, boolean portInMSISDN ) throws ApplicationException, RemoteException, ValidateException {
		
		ProductServicesInfo productServicesInfo = DaoSupport.mapToAmdocsProductServices(pProductBaseConv, pSubscriberInfo, pSubscriberContractInfo, processIncludedSocs, processRegularSocs, pricePlanModified, portInMSISDN);

		LOGGER.info(extractProductServicesInfoToString(pSubscriberInfo, productServicesInfo));

		return productServicesInfo;

	}

	public static AddressInfo mapTelusAddressInfoToAmdocsAddressInfoForActivation(com.telus.eas.account.info.AddressInfo addressInfo) {
		// map Telus class to Amdocs class
        AddressInfo amdocsAddressInfo = new AddressInfo();
        amdocsAddressInfo.type = (byte) addressInfo.getAddressType().charAt(0);
        amdocsAddressInfo.city = addressInfo.getCity() != null ? addressInfo.getCity() : "";
        amdocsAddressInfo.province = addressInfo.getProvince() != null ? addressInfo.getProvince() : "";
        amdocsAddressInfo.postalCode = addressInfo.getPostalCode() != null ? addressInfo.getPostalCode() : "";
        amdocsAddressInfo.country = addressInfo.getCountry() != null ? addressInfo.getCountry() : "";
        amdocsAddressInfo.zipGeoCode = addressInfo.getZipGeoCode() != null ? addressInfo.getZipGeoCode() : "";
        amdocsAddressInfo.foreignState = addressInfo.getForeignState() != null ? addressInfo.getForeignState() : "";
        amdocsAddressInfo.civicNo = addressInfo.getCivicNo() != null ? addressInfo.getCivicNo() : "";
        amdocsAddressInfo.civicNoSuffix = addressInfo.getCivicNoSuffix() != null ? addressInfo.getCivicNoSuffix() : "";
        amdocsAddressInfo.streetDirection = addressInfo.getStreetDirection() != null ? addressInfo.getStreetDirection() : "";
        amdocsAddressInfo.streetName = addressInfo.getStreetName() != null ? addressInfo.getStreetName() : "";
        amdocsAddressInfo.streetType = addressInfo.getStreetType() != null ? addressInfo.getStreetType() : "";
        amdocsAddressInfo.primaryLine = addressInfo.getPrimaryLine() != null ? addressInfo.getPrimaryLine() : "";
        amdocsAddressInfo.secondaryLine = addressInfo.getSecondaryLine() != null ? addressInfo.getSecondaryLine() : "";
        amdocsAddressInfo.rrDesignator = addressInfo.getRrDesignator() != null ? addressInfo.getRrDesignator() : "";
        amdocsAddressInfo.rrIdentifier = addressInfo.getRrIdentifier() != null ? addressInfo.getRrIdentifier() : "";
        amdocsAddressInfo.rrBox = addressInfo.getPoBox() != null ? addressInfo.getPoBox() : "";
        amdocsAddressInfo.unitDesignator = addressInfo.getUnitDesignator() != null ? addressInfo.getUnitDesignator() : "";
        amdocsAddressInfo.unitIdentifier = addressInfo.getUnitIdentifier() != null ? addressInfo.getUnitIdentifier() : "";
        amdocsAddressInfo.rrAreaNumber = addressInfo.getRrAreaNumber() != null ? addressInfo.getRrAreaNumber() : "";
        amdocsAddressInfo.rrQualifier = addressInfo.getRuralQualifier() != null ? addressInfo.getRuralQualifier() : "";
        amdocsAddressInfo.rrSite = addressInfo.getRuralSite() != null ? addressInfo.getRuralSite() : "";
        amdocsAddressInfo.rrCompartment = addressInfo.getRrCompartment() != null ? addressInfo.getRrCompartment() : "";
        amdocsAddressInfo.attention = addressInfo.getAttention() != null ? addressInfo.getAttention() : "";
        amdocsAddressInfo.rrDeliveryType = addressInfo.getRrDeliveryType() != null ? addressInfo.getRrDeliveryType() : "";
        amdocsAddressInfo.rrGroup = addressInfo.getRrGroup() != null ? addressInfo.getRrGroup() : "";
        return amdocsAddressInfo;
	}
}
