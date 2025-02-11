/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.eas.activitylog.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Contract;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.api.reference.PricePlan;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.eas.activitylog.queue.info.ActivityLoggingInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;


/**
 * @author Pavel Simonovsky
 *
 */
public class ChangeContractActivity extends ActivityLoggingInfo{

	private static final long serialVersionUID = 1L;
	
	private ContractHolder newContract = null;
	
	private ContractHolder oldContract = null;
	
	private Collection addedServices = new ArrayList();
	
	private Collection removedServices = new ArrayList();
	
	private Collection updatedServices = new ArrayList();
	
	private Collection updatedFeatures = new ArrayList();
	
	public ChangeContractActivity(ServiceRequestHeader header) {
		super(header);
	}
	
	public void setUpdatedFeatures(ContractFeature [] features) {
		updatedFeatures = createFeatureHolder(features);
	}
	
	public Collection getUpdatedFeatures() {
		return updatedFeatures;
	}
	
	public String getMessageType() {
		return MESSAGE_TYPE_CHANGE_CONTRACT;
	}
	
	private Collection createFeatureHolder(ContractFeature [] features) {
		Collection result = new ArrayList();
		for (int idx = 0; idx < features.length; idx++) {
			ContractFeature feature = features[idx];
			FeatureHolder holder = new FeatureHolder(feature.getCode(), 
					feature.getParameter(), feature.getServiceCode());
			result.add(holder);
		}
		return result;
	}

	private Collection createServiceHolder(ContractService [] services) throws TelusAPIException {
		Collection result = new ArrayList();
		for (int idx = 0; idx < services.length; idx++) {
			ServiceAgreementInfo service = (ServiceAgreementInfo) services[idx];
			ServiceHolder serviceHolder = new ServiceHolder(
					service.getCode(), service.getEffectiveDate(), 
					service.getExpiryDate(), service.getServiceType());
			result.add(serviceHolder);
		}
		return result;
	}
	
	public void setOldContract(Contract contract) throws TelusAPIException {
		this.oldContract = createContractHolder(contract);
	}

	public void setNewContract(Contract contract) throws TelusAPIException {
		this.newContract = createContractHolder(contract);
	}
	
	private ContractHolder createContractHolder(Contract contract) throws TelusAPIException {
		PricePlan pricePlan = contract.getPricePlan();
		
		ServiceHolder pricePlanHolder = new ServiceHolder(pricePlan.getCode(), 
				pricePlan.getEffectiveDate(), pricePlan.getExpiryDate(), 
				pricePlan.getService().getServiceType());
		
		ContractHolder contractHolder = new ContractHolder(
				contract.getEffectiveDate(), contract.getExpiryDate(), 
				contract.getCommitmentStartDate(), contract.getCommitmentEndDate(), 
				contract.getCommitmentMonths(), contract.getCommitmentReasonCode());
				
		contractHolder.setPricePlan(pricePlanHolder);
		
		return contractHolder;
	}
	
	/**
	 * @return the newContract
	 */
	public ContractHolder getNewContract() {
		return newContract;
	}

	/**
	 * @param newContract the newContract to set
	 */
	public void setNewContract(ContractHolder newContract) {
		this.newContract = newContract;
		
		// update expiration date on old contract
		
		if (oldContract != null) {
			oldContract.setExpiryDate(newContract.getEffectiveDate());
		}
	}

	/**
	 * @return the oldContract
	 */
	public ContractHolder getOldContract() {
		return oldContract;
	}

	/**
	 * @param oldContract the oldContract to set
	 */
	public void setOldContract(ContractHolder oldContract) {
		this.oldContract = oldContract;
	}

	/**
	 * @return the addedServices
	 */
	public Collection getAddedServices() {
		return addedServices;
	}

	public void setAddedServices(ContractService [] services) throws TelusAPIException {
		this.addedServices = createServiceHolder(services);
	}

	/**
	 * @return the removedServices
	 */
	public Collection getRemovedServices() {
		return removedServices;
	}

	public void setRemovedServices(ContractService [] services) throws TelusAPIException {
		this.removedServices = createServiceHolder(services);
	}

	/**
	 * @return the updatedServices
	 */
	public Collection getUpdatedServices() {
		return updatedServices;
	}

	public void setUpdatedServices(ContractService [] services) throws TelusAPIException {
		this.updatedServices = createServiceHolder(services);
	}

	public class ServiceHolder implements Serializable {
		
		private static final long serialVersionUID = 1L;

		private String code = null;
		
		private Date effectiveDate = null;
		
		private Date expiryDate = null;
		
		private String serviceType = null;

		public ServiceHolder(String code, Date effectiveDate, Date expiryDate, String serviceType) {
			this.code = code;
			this.effectiveDate = effectiveDate;
			this.expiryDate = expiryDate;
			this.serviceType = serviceType;
		}

		/**
		 * @return the code
		 */
		public String getCode() {
			return code;
		}

		/**
		 * @param code the code to set
		 */
		public void setCode(String code) {
			this.code = code;
		}

		/**
		 * @return the effectiveDate
		 */
		public Date getEffectiveDate() {
			if (effectiveDate == null) {
				effectiveDate = new Date();
			}
			return effectiveDate;
		}

		/**
		 * @param effectiveDate the effectiveDate to set
		 */
		public void setEffectiveDate(Date effectiveDate) {
			this.effectiveDate = effectiveDate;
		}

		/**
		 * @return the expiryDate
		 */
		public Date getExpiryDate() {
			return expiryDate;
		}

		/**
		 * @param expiryDate the expiryDate to set
		 */
		public void setExpiryDate(Date expiryDate) {
			this.expiryDate = expiryDate;
		}

		/**
		 * @return the serviceType
		 */
		public String getServiceType() {
			return serviceType;
		}

		/**
		 * @param serviceType the serviceType to set
		 */
		public void setServiceType(String serviceType) {
			this.serviceType = serviceType;
		}
		
	}

	public class ContractHolder implements Serializable {
		
		private static final long serialVersionUID = 1L;

		private ServiceHolder pricePlan = null;
		
		private Date effectiveDate = null;
		
		private Date expiryDate = null;
		
		private Date commitmentStartDate = null;
		
		private Date commitmentEndDate = null;
		
		private int commitmentMonths = 0;
		
		private String commitmentReasonCode = null;


		public ContractHolder(Date effectiveDate, Date expiryDate, Date commitmentStartDate, 
				Date commitmentEndDate, int commitmentMonths, String commitmentReasonCode) {

			this.effectiveDate = effectiveDate;
			this.expiryDate = expiryDate;
			this.commitmentStartDate = commitmentStartDate;
			this.commitmentEndDate = commitmentEndDate;
			this.commitmentMonths = commitmentMonths;
			this.commitmentReasonCode = commitmentReasonCode;
		}

		/**
		 * @return the pricePlan
		 */
		public ServiceHolder getPricePlan() {
			return pricePlan;
		}

		/**
		 * @param pricePlan the pricePlan to set
		 */
		public void setPricePlan(ServiceHolder pricePlan) {
			this.pricePlan = pricePlan;
		}

		/**
		 * @return the effectiveDate
		 */
		public Date getEffectiveDate() {
			if (effectiveDate == null) {
				effectiveDate = new Date();
			}
			return effectiveDate;
		}

		/**
		 * @param effectiveDate the effectiveDate to set
		 */
		public void setEffectiveDate(Date effectiveDate) {
			this.effectiveDate = effectiveDate;
		}

		/**
		 * @return the expiryDate
		 */
		public Date getExpiryDate() {
			return expiryDate;
		}

		/**
		 * @param expiryDate the expiryDate to set
		 */
		public void setExpiryDate(Date expiryDate) {
			this.expiryDate = expiryDate;
		}

		/**
		 * @return the commitmentStartDate
		 */
		public Date getCommitmentStartDate() {
			return commitmentStartDate;
		}

		/**
		 * @param commitmentStartDate the commitmentStartDate to set
		 */
		public void setCommitmentStartDate(Date commitmentStartDate) {
			this.commitmentStartDate = commitmentStartDate;
		}

		/**
		 * @return the commitmentEndDate
		 */
		public Date getCommitmentEndDate() {
			return commitmentEndDate;
		}

		/**
		 * @param commitmentEndDate the commitmentEndDate to set
		 */
		public void setCommitmentEndDate(Date commitmentEndDate) {
			this.commitmentEndDate = commitmentEndDate;
		}

		/**
		 * @return the commitmentMonths
		 */
		public int getCommitmentMonths() {
			return commitmentMonths;
		}

		/**
		 * @param commitmentMonths the commitmentMonths to set
		 */
		public void setCommitmentMonths(int commitmentMonths) {
			this.commitmentMonths = commitmentMonths;
		}

		/**
		 * @return the commitmentReasonCode
		 */
		public String getCommitmentReasonCode() {
			return commitmentReasonCode;
		}

		/**
		 * @param commitmentReasonCode the commitmentReasonCode to set
		 */
		public void setCommitmentReasonCode(String commitmentReasonCode) {
			this.commitmentReasonCode = commitmentReasonCode;
		}
	}
	
	public class FeatureHolder implements Serializable {
		
		private static final long serialVersionUID = 1L;

		private String code = null;
		
		private String parameter = null;
		
		private String serviceCode = null;

		public FeatureHolder(String code, String parameter, String serviceCode) {
			this.code = code;
			this.parameter = parameter;
			this.serviceCode = serviceCode;
		}
		
		/**
		 * @return the code
		 */
		public String getCode() {
			return code;
		}

		/**
		 * @param code the code to set
		 */
		public void setCode(String code) {
			this.code = code;
		}

		/**
		 * @return the parameter
		 */
		public String getParameter() {
			return parameter;
		}

		/**
		 * @param parameter the parameter to set
		 */
		public void setParameter(String parameter) {
			this.parameter = parameter;
		}

		/**
		 * @return the serviceCode
		 */
		public String getServiceCode() {
			return serviceCode;
		}

		/**
		 * @param serviceCode the serviceCode to set
		 */
		public void setServiceCode(String serviceCode) {
			this.serviceCode = serviceCode;
		}
		
	}
	
//	public boolean isPricePlanChanged() {
//		return !newContract.getPricePlan().getCode().equals(oldContract.getPricePlan().getCode());
//	}
//
//	public int getNumberOfAffectedServices() {
//		return addedServices.size() + removedServices.size() + updatedServices.size();
//	}
//	
//	public boolean isTermChanged() {
//		boolean startDateChanged = true;
//		if (oldContract.getCommitmentStartDate() == null && newContract.getCommitmentStartDate() == null) {
//			startDateChanged = false;
//		} else if (oldContract.getCommitmentStartDate() != null && newContract.getCommitmentStartDate() != null) {
//			startDateChanged = !oldContract.getCommitmentStartDate().equals(newContract.getCommitmentStartDate());
//		}
//		
//		boolean durationChanged = newContract.getCommitmentMonths() != oldContract.getCommitmentMonths();
//		
//		return startDateChanged || durationChanged;
//	}
}
