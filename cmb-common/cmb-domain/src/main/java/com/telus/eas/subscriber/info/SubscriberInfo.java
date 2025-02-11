package com.telus.eas.subscriber.info;

import com.telus.api.*;
import com.telus.api.message.ApplicationMessage;
import com.telus.api.account.*;
import com.telus.api.equipment.*;
import com.telus.api.portability.PRMSystemException;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestException;
import com.telus.api.portability.PortRequestSummary;
import com.telus.api.reference.*;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.eas.framework.info.Info;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.equipment.info.*;

import java.util.Date;

/**
 * Title:         Telus - Amdocs Domain Beans
 * Description:
 * Copyright:     Copyright (c) 2002
 * Company:       Telus Mobility
 * 
 * @version       6.1
 */
public class SubscriberInfo extends Info implements Subscriber {

	public static final long serialVersionUID = 6707192916270904938L;


	public static final String EDMONTON_PCS_NUMBERGROUP = "EDM";
	public static final String EDMONTON_IDEN_NUMBERGROUP = "ED1";

	public SubscriberInfo() {
	}

	private int banId;
	private String language = "";
	private String serialNumber = "";
	private String[] secondarySerialNumbers;
	private String equipmentType = "D";
	private char status;
	private String productType = "";
	private String pricePlan = "";
	private String dealerCode = "";
	private String salesRepId = "";
	private Date birthDate;
	private String emailAddress = "";
	private String faxNumber = "";
	private Date createDate;
	private Date startServiceDate;
	private String marketProvince = "";
	private String activityReasonCode = "";
	private String activityCode = "";
	private String subscriberId;
	private String phoneNumber;
	private String userValueRating;
	private NumberGroup numberGroup;
	private EquipmentInfo equipment = new EquipmentInfo();
	private boolean isGSTExempt;
	private boolean isPSTExempt;
	private boolean isHSTExempt;
	private Date gstExemptionEffectiveDate;
	private Date pstExemptionEffectiveDate;
	private Date hstExemptionEffectiveDate;
	private Date gstExemptionExpiryDate;
	private Date pstExemptionExpiryDate;
	private Date hstExemptionExpiryDate;
	private String gstCertificateNumber;
	private String pstCertificateNumber;
	private String hstCertificateNumber;
	private Date statusUpdateDate;
	private String invoiceCallSortOrderCode;
	private String oldRole;
	private CommitmentInfo commitment;
	private ConsumerNameInfo consumerName = new ConsumerNameInfo();
	private String[] multiRingPhoneNumbers;
	private boolean hotlined;
	private Date migrationDate;
	private MigrationType migrationType;
	private String migrationTypeCode;
	private double securityDeposit;
	private String portType;
	private Date portDate;
	private boolean portInd; // needed only for releaseSubscriber during destroy session
	private String voiceMailLanguage;
	private int brandId;
	private boolean internalUse = false;
	private boolean isHSPA ;
	private boolean hasDummyESN;
	private long subscriptionId;
	private SeatData seatData = null;
	private AddressInfo newAddressInfo = null;

	public Object clone() {
		
		SubscriberInfo o = (SubscriberInfo)super.clone();

		o.birthDate = cloneDate(birthDate);
		o.createDate = cloneDate(createDate);
		o.startServiceDate = cloneDate(startServiceDate);
		o.gstExemptionEffectiveDate = cloneDate(gstExemptionEffectiveDate);
		o.pstExemptionEffectiveDate = cloneDate(pstExemptionEffectiveDate);
		o.hstExemptionEffectiveDate = cloneDate(hstExemptionEffectiveDate);
		o.gstExemptionExpiryDate = cloneDate(gstExemptionExpiryDate);
		o.pstExemptionExpiryDate = cloneDate(pstExemptionExpiryDate);
		o.hstExemptionExpiryDate = cloneDate(hstExemptionExpiryDate);
		o.statusUpdateDate = cloneDate(statusUpdateDate);

		return o;
	}

	public void clear() {
		
		banId = 0;
		language = "";
		serialNumber = "";
		equipmentType = "D";
		status = 0;
		productType = "";
		pricePlan = "";
		dealerCode = "";
		salesRepId = "";
		birthDate = null;
		emailAddress = "";
		faxNumber = "";
		createDate = null;
		startServiceDate = null;
		marketProvince = "";
		activityReasonCode = "";
		activityCode = "";
		subscriberId = null;
		phoneNumber = null;
		userValueRating = null;
		numberGroup = null;
		equipment = new EquipmentInfo();
		isGSTExempt = false;
		isPSTExempt = false;
		isHSTExempt = false;
		gstExemptionEffectiveDate = null;
		pstExemptionEffectiveDate = null;
		hstExemptionEffectiveDate = null;
		gstExemptionExpiryDate = null;
		pstExemptionExpiryDate = null;
		hstExemptionExpiryDate = null;
		gstCertificateNumber = null;
		pstCertificateNumber = null;
		hstCertificateNumber = null;
		statusUpdateDate = null;
		invoiceCallSortOrderCode = null;
		oldRole = null;
		consumerName = new ConsumerNameInfo();
		securityDeposit = 0;
		portType = "";
		portDate = null;
		subscriptionId = 0;
	}

	// Operations

	/**
	 * @deprecated
	 * @see #getConsumerName
	 */
	public String getFirstName() {
		return nullToString(consumerName.getFirstName());
	}
	/**
	 * @deprecated
	 * @see #getConsumerName
	 */
	public void setFirstName(String newFirstName) {
		consumerName.setFirstName(newFirstName);
	}

	/**
	 * @deprecated
	 * @see #getConsumerName
	 */
	public String getMiddleInitial() {
		return nullToString(consumerName.getMiddleInitial());
	}
	/**
	 * @deprecated
	 * @see #getConsumerName
	 */
	public void setMiddleInitial(String newMiddleInitial) {
		consumerName.setMiddleInitial(newMiddleInitial);
	}

	/**
	 * @deprecated
	 * @see #getConsumerName
	 */
	public String getLastName() {
		return nullToString(consumerName.getLastName());
	}
	/**
	 * @deprecated
	 * @see #getConsumerName
	 */
	public void setLastName(String newLastName) {
		consumerName.setLastName(newLastName);
	}

	public ConsumerName getConsumerName() {
		return consumerName;
	}

	public void setLanguage(String newLanguage) {
		language = toUpperCase(newLanguage);
	}

	public String getLanguage() {
		return language;
	}

	public void setSerialNumber(String newSerialNumber) {
		serialNumber = newSerialNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public String[] getSecondarySerialNumbers() {
		return secondarySerialNumbers;
	}

	public void setSecondarySerialNumbers(String[] secondarySerialNumbers) {
		this.secondarySerialNumbers = secondarySerialNumbers;
	}

	public Subscriber retrieveSubscriber(String phoneNumber) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public String getVoiceMailLanguage() {
		return voiceMailLanguage;
	}

	public void setVoiceMailLanguage(String voiceMailLanguage) {
		this.voiceMailLanguage = voiceMailLanguage;
	}


	public void setProductType(String newProductType) {
		productType = newProductType;
	}

	public String getProductType() {
		return productType;
	}

	public void setPricePlan(String newPricePlan) {
		pricePlan = newPricePlan;
	}

	public String getPricePlan() {
		return pricePlan;
	}

	public void setDealerCode(String newDealerCode) {
		dealerCode = newDealerCode;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void save() throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method save() not implemented in server class.");
	}

	public void save(boolean activate) throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method save() not implemented in server class.");
	}

	public void save(Date startServiceDate) throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method save() not implemented in server class.");
	}

	public void refresh() throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method refresh() not implemented in server class.");
	}

	public void saveContract(Contract newContract) throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method saveContract() not implemented in server class.");
	}

	public void saveContract(Contract newContract, String dealerCode, String salesRepCode)
	throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method saveContract() not implemented in server class.");
	}

	public void activate() throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method activate() not implemented in server class.");
	}

	public void activate(Date startServiceDate) throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method activate() not implemented in server class.");
	}

	public void activate(String reason) throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method activate() not implemented in server class.");
	}

	public void activate(String reason, Date startServiceDate) throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method activate() not implemented in server class.");
	}

	public void activate(String reason, String memoText) throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method activate() not implemented in server class.");
	}

	public void activate(String reason, Date startServiceDate, String memoText) throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method activate() not implemented in server class.");
	}

	public void unreserve() throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method not implemented here");
	}

	public void reserveMobileNumber(PhoneNumberReservation phoneNumberReservation) 
	throws TelusAPIException, NumberMatchException {
		throw new java.lang.UnsupportedOperationException("Method reserveMobileNumber() not implemented in server class.");
	}

	public int getBanId() {
		return banId;
	}

	public void setBanId(int banId) {
		this.banId = banId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getSalesRepId() {
		return salesRepId;
	}

	public void setSalesRepId(String salesRepId) {
		this.salesRepId = salesRepId;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Account getAccount() throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method getAccount() not implemented in server class.");
	}

	public Contract getContract() {
		throw new java.lang.UnsupportedOperationException("Method getContract() not implemented in server class.");
	}

	public void setContract(Contract contract) {
		throw new java.lang.UnsupportedOperationException("Method setContract() not implemented in server class.");
	}

	public Equipment getEquipment() {
		throw new java.lang.UnsupportedOperationException("Method getEquipment() not implemented in server class.");
	}

	public EquipmentInfo getEquipment0() {
		return equipment;
	}

	public void setEquipment(EquipmentInfo equipment) {
		this.equipment = equipment;
		if (equipment != null) {
			setHSPA(equipment.isHSPA());
		}else {
			setHSPA(false);
		}
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public void setNumberGroup(NumberGroup numberGroup) {
		this.numberGroup = numberGroup;
	}

	public NumberGroup getNumberGroup() {
		return numberGroup;
	}

	public String getOldRole() {
		return oldRole;
	}

	public void setOldRole(String role) {
		this.oldRole = role;
	}

	public void copyFrom(SubscriberInfo o) {
		
		consumerName.copyFrom(o.consumerName);
		banId = o.banId;
		language = o.language;
		serialNumber = o.serialNumber;
		equipmentType = o.equipmentType;
		phoneNumber = o.phoneNumber;
		subscriberId = o.subscriberId;
		status = o.status;
		productType = o.productType;
		pricePlan = o.pricePlan;
		dealerCode = o.dealerCode;
		salesRepId = o.salesRepId;
		birthDate = cloneDate(o.birthDate);
		emailAddress = o.emailAddress;
		faxNumber = o.faxNumber;
		createDate = cloneDate(o.createDate);
		startServiceDate = cloneDate(o.startServiceDate);
		marketProvince = o.marketProvince;
		numberGroup = o.numberGroup;
		isGSTExempt = o.isGSTExempt;
		isPSTExempt = o.isPSTExempt;
		isHSTExempt = o.isHSTExempt;
		gstExemptionEffectiveDate = o.gstExemptionEffectiveDate;
		pstExemptionEffectiveDate = o.pstExemptionEffectiveDate;
		hstExemptionEffectiveDate = o.hstExemptionEffectiveDate;
		gstExemptionExpiryDate = o.gstExemptionExpiryDate;
		pstExemptionExpiryDate = o.pstExemptionExpiryDate;
		hstExemptionExpiryDate = o.hstExemptionExpiryDate;
		gstCertificateNumber = o.gstCertificateNumber;
		pstCertificateNumber = o.pstCertificateNumber;
		hstCertificateNumber = o.hstCertificateNumber;
		statusUpdateDate = o.statusUpdateDate;
		invoiceCallSortOrderCode = o.invoiceCallSortOrderCode;
		oldRole = o.oldRole;
		commitment = (CommitmentInfo) ((CommitmentInfo) o.getCommitment()).clone();
		this.hotlined = o.hotlined;
		securityDeposit = o.securityDeposit;
		portType = o.portType;
		portDate = o.portDate;
		portInd = o.portInd;
		voiceMailLanguage = o.voiceMailLanguage;
		this.subscriptionId = o.subscriptionId; // Fixed August 27 2010 - Hilton
		
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer(128);

		s.append("SubscriberInfo:[\n");
		s.append("    firstName=[").append(consumerName.getFirstName()).append("]\n");
		s.append("    middleInitial=[").append(consumerName.getMiddleInitial()).append("]\n");
		s.append("    lastName=[").append(consumerName.getLastName()).append("]\n");
		s.append("    title=[").append(consumerName.getTitle()).append("]\n");
		s.append("    generation=[").append(consumerName.getGeneration()).append("]\n");
		s.append("    additionalLine=[").append(consumerName.getAdditionalLine()).append("]\n");
		s.append("    banId=[").append(banId).append("]\n");
		s.append("    language=[").append(language).append("]\n");
		s.append("    voice Mail language=[").append(voiceMailLanguage).append("]\n");
		s.append("    serialNumber=[").append(serialNumber).append("]\n");

		if (secondarySerialNumbers != null)
			for (int i = 0; i < secondarySerialNumbers.length; i++)
				s.append("    secondarySerialNumber[").append(i).append("]=[").append(secondarySerialNumbers[i]).append("]\n");

		s.append("    equipmentType=[").append(equipmentType).append("]\n");
		s.append("    status=[").append(status).append("]\n");
		s.append("    productType=[").append(productType).append("]\n");
		s.append("    pricePlan=[").append(pricePlan).append("]\n");
		s.append("    dealerCode=[").append(dealerCode).append("]\n");
		s.append("    salesRepId=[").append(salesRepId).append("]\n");
		s.append("    birthDate=[").append(birthDate).append("]\n");
		s.append("    emailAddress=[").append(emailAddress).append("]\n");
		s.append("    faxNumber=[").append(faxNumber).append("]\n");
		s.append("    createDate=[").append(createDate).append("]\n");
		s.append("    startServiceDate=[").append(startServiceDate).append("]\n");
		s.append("    marketProvince=[").append(marketProvince).append("]\n");
		s.append("    activityReasonCode=[").append(activityReasonCode).append(
		"]\n");
		s.append("    activityCode=[").append(activityCode).append("]\n");
		s.append("    subscriberId=[").append(subscriberId).append("]\n");
		s.append("    phoneNumber=[").append(phoneNumber).append("]\n");
		s.append("    numberGroup=[").append(numberGroup).append("]\n");
		s.append("    isGSTExempt=[").append(isGSTExempt).append("]\n");
		s.append("    isPSTExempt=[").append(isPSTExempt).append("]\n");
		s.append("    isHSTExempt=[").append(isHSTExempt).append("]\n");
		s.append("    GSTExemptionEffectiveDate=[").append(gstExemptionEffectiveDate).append("]\n");
		s.append("    PSTExemptionEffectiveDate=[").append(pstExemptionEffectiveDate).append("]\n");
		s.append("    HSTExemptionEffectiveDate=[").append(hstExemptionEffectiveDate).append("]\n");
		s.append("    GSTExemptionExpiryDate=[").append(gstExemptionExpiryDate).append("]\n");
		s.append("    PSTExemptionExpiryDate=[").append(pstExemptionExpiryDate).append("]\n");
		s.append("    HSTExemptionExpiryDate=[").append(hstExemptionExpiryDate).append("]\n");
		s.append("    GSTCertificateNumber=[").append(gstCertificateNumber).append("]\n");
		s.append("    PSTCertificateNumber=[").append(pstCertificateNumber).append("]\n");
		s.append("    HSTCertificateNumber=[").append(hstCertificateNumber).append("]\n");
		s.append("    statusUpdateDate=[").append(statusUpdateDate).append("]\n");
		s.append("    invoiceCallSortOrderCode=[").append(invoiceCallSortOrderCode).append("]\n");
		s.append("    oldRole=[").append(oldRole).append("]\n");
		s.append("    commitment=[").append(commitment).append("]\n");
		s.append("    securityDeposit=[").append(securityDeposit).append("]\n");
		s.append("    portType=[").append(portType).append("]\n");
		s.append("    portDate=[").append(portDate).append("]\n");
		s.append("    isHSPA=[").append(isHSPA()).append("]\n");
		if (seatData != null) {
			s.append("Business Connect seatData=[").append(seatData.toString()).append("]\n");
		}
		s.append("]");

		return s.toString();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date newCreateDate) {
		createDate = newCreateDate;
	}

	public Date getStartServiceDate() {
		return startServiceDate;
	}

	public void setStartServiceDate(Date newStartServiceDate) {
		startServiceDate = newStartServiceDate;
	}

	public void setMarketProvince(String newMarketProvince) {
		marketProvince = newMarketProvince;
	}

	public String getMarketProvince() {
		return marketProvince;
	}

	public String getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(String newEquipmentType) {
		equipmentType = newEquipmentType;
	}

	public void setActivityReasonCode(String newActivityReasonCode) {
		activityReasonCode = newActivityReasonCode;
	}

	public String getActivityReasonCode() {
		return activityReasonCode;
	}

	public void setActivityCode(String newActivityCode) {
		activityCode = newActivityCode;
	}

	public String getActivityCode() {
		return activityCode;
	}

	/**
	 * PCS or MiKE equipment swap
	 */
	 public ApplicationMessage[] changeEquipment(
			 Equipment newEquipment,
			 String dealerCode,
			 String salesRepCode,
			 String requestorId,
			 String repairId,
			 String swapType) 
	 throws TelusAPIException, SerialNumberInUseException {
		 throw new java.lang.UnsupportedOperationException("Method changeEquipment() not implemented in server class.");
	 }

	 /**
	  * PCS or MiKE equipment swap
	  */
	 public ApplicationMessage[] changeEquipment(
			 Equipment newEquipment,
			 String dealerCode,
			 String salesRepCode,
			 String requestorId,
			 String repairId,
			 String swapType,
			 boolean preserveDigitalServices) 
	 throws TelusAPIException, SerialNumberInUseException {
		 throw new java.lang.UnsupportedOperationException("Method changeEquipment() not implemented in server class.");
	 }

	 public ApplicationMessage[] changeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices, boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		 throw new java.lang.UnsupportedOperationException("Method changeEquipment() not implemented in server class.");
	 }

	 /**
	  * SIM card swap
	  */
	 public void changeEquipment(
			 IDENEquipment newIDENEquipment,
			 String dealerCode,
			 String salesRepCode,
			 String requestorId,
			 String repairId,
			 String swapType,
			 MuleEquipment associatedMuleEquipment) 
	 throws TelusAPIException, SerialNumberInUseException {
		 throw new java.lang.UnsupportedOperationException("Method changeEquipment() not implemented in server class.");
	 }

	 public void changeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment, boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		 throw new java.lang.UnsupportedOperationException("Method changeEquipment() not implemented in server class.");
	 }

	 public void resetVoiceMailPassword() throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method getAccount() not implemented in server class.");
	 }

	 public int getProvisioningPlatformId() throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method not implemented here");
	 }

	 /**
	  * NO-OP
	  */
	 public CallList getBilledCalls(int billSeqNo) throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method not implemented here");
	 }

	 /**
	  * NO-OP
	  */
	 public CallList getBilledCalls(int billSeqNo, Date from, Date to, boolean getAll) throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method not implemented here");
	 }

	 public CallList getBilledCalls(int billSeqNo, char callType) throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method not implemented here");
	 }

	 public CallList getBilledCalls(int billSeqNo, char callType, Date from, Date to, boolean getAll) throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method not implemented here");
	 }

	 /**
	  * NO-OP
	  */
	 public CallList getUnbilledCalls() throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method not implemented here");
	 }

	 public ProvisioningTransaction[] getProvisioningTransactions( Date startDate, Date endDate ) throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method not implemented here");
	 }

	 public PricePlanSummary[] getSuspensionPricePlans(String reasonCode) throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method not implemented here");
	 }

	 public NumberGroup[] getAvailableNumberGroups() throws TelusAPIException  {
		 throw new java.lang.UnsupportedOperationException("Method getAvailableNumberGroups() not implemented in server class.");
	 }

	 public NumberGroup[] getAvailableNumberGroups(String marketArea) throws
	 TelusAPIException
	 {
		 throw new java.lang.UnsupportedOperationException(
		 "Method getAvailableNumberGroups() not implemented in server class.");
	 }

	 public NumberGroup[] getAvailableNumberGroupsGivenNumberLocation(String numberLocation) throws TelusAPIException  {
		 throw new java.lang.UnsupportedOperationException("Method getAvailableNumberGroupsByNumberLocation() not implemented in server class.");
	 }

	 public FollowUp newFollowUp() throws TelusAPIException
	 {
		 throw new java.lang.UnsupportedOperationException();
	 }

	 public Memo newMemo() throws TelusAPIException
	 {
		 throw new java.lang.UnsupportedOperationException();
	 }

	 public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, boolean changeOtherNumbers) throws TelusAPIException, PhoneNumberException, PhoneNumberInUseException {
		 throw new java.lang.UnsupportedOperationException(
				 "Method not implemented in server class.");
	 }

	 public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, boolean changeOtherNumbers, String dealerCode, String salesRepCode) throws TelusAPIException, PhoneNumberException, PhoneNumberInUseException {
		 throw new java.lang.UnsupportedOperationException(
		 "Method not implemented in server class.");
	 }

	 public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, boolean changeOtherNumbers, String dealerCode, String salesRepCode, String reasonCode) throws TelusAPIException, PhoneNumberException, PhoneNumberInUseException {
		 throw new java.lang.UnsupportedOperationException(
		 "Method not implemented in server class.");
	 }

	 public void reserveAdditionalPhoneNumber(AvailablePhoneNumber
			 availablePhoneNumber) throws
			 TelusAPIException, PhoneNumberException, PhoneNumberInUseException
			 {
		 throw new java.lang.UnsupportedOperationException(
		 "Method not implemented in server class.");
			 }

	 public AvailablePhoneNumber[] findAvailablePhoneNumbers(
			 PhoneNumberReservation phoneNumberReservation, int maximum) throws
			 TelusAPIException, PhoneNumberException
			 {
		 throw new java.lang.UnsupportedOperationException(
		 "Method not implemented in server class.");
			 }

	 public boolean isIDEN()
	 {
		 return productType.equalsIgnoreCase(Subscriber.PRODUCT_TYPE_IDEN);
//		 throw new java.lang.UnsupportedOperationException("Method isIDEN() not implemented in server class.");
	 }

	 public boolean isPCS()
	 {
		 return productType.equalsIgnoreCase(Subscriber.PRODUCT_TYPE_PCS);
//		 throw new java.lang.UnsupportedOperationException("Method isPCS() not implemented in server class.");
	 }

	 public boolean isPager()
	 {
		 return productType.equalsIgnoreCase(Subscriber.PRODUCT_TYPE_PAGER);
	 }

	 public boolean isTango() {
		 return productType.equalsIgnoreCase(Subscriber.PRODUCT_TYPE_TANGO);
	 }

	 public boolean isCDPD() {
		 return productType.equalsIgnoreCase(Subscriber.PRODUCT_TYPE_CDPD);
	 }

	 public Contract newContract(PricePlan pricePlan, int term) throws TelusAPIException
	 {
		 throw new java.lang.UnsupportedOperationException(
				 "Method getAccount() not implemented in server class.");
	 }

	 public Contract newContract(PricePlan pricePlan, int term, boolean dispatchOnly) throws
	 TelusAPIException
	 {
		 throw new java.lang.UnsupportedOperationException(
		 "Method not implemented here");
	 }

	 public Contract newContract(PricePlan pricePlan, int term,
			 EquipmentChangeRequest equipmentChangeRequest) throws
			 TelusAPIException
			 {
		 throw new java.lang.UnsupportedOperationException(
		 "Method getAccount() not implemented in server class.");
			 }

	 public Contract newContract(PricePlan pricePlan, int term, boolean dispatchOnly,
			 EquipmentChangeRequest equipmentChangeRequest) throws
			 TelusAPIException
			 {
		 throw new java.lang.UnsupportedOperationException(
		 "Method not implemented here");
			 }

	 public Contract renewContract(int term) throws TelusAPIException
	 {
		 throw new java.lang.UnsupportedOperationException(
		 "Method not implemented here");
	 }

	 public Contract renewContract(PricePlan pricePlan, int term) throws
	 InvalidPricePlanChangeException, TelusAPIException
	 {
		 throw new java.lang.UnsupportedOperationException(
		 "Method not implemented here");
	 }

	 public Contract renewContract(PricePlan pricePlan, int term,
			 EquipmentChangeRequest equipmentChangeRequest) throws
			 InvalidPricePlanChangeException, TelusAPIException
			 {
		 throw new java.lang.UnsupportedOperationException(
		 "Method not implemented here");
			 }

	 public Contract renewContract(PricePlan pricePlan, int term, boolean dispatchOnly) throws
	 InvalidPricePlanChangeException, TelusAPIException
	 {
		 throw new java.lang.UnsupportedOperationException(
		 "Method not implemented here");
	 }

	 public Contract renewContract(PricePlan pricePlan, int term, boolean dispatchOnly,
			 EquipmentChangeRequest equipmentChangeRequest) throws
			 InvalidPricePlanChangeException, TelusAPIException
			 {
		 throw new java.lang.UnsupportedOperationException(
		 "Method not implemented here");
			 }

	 public void reservePhoneNumber(PhoneNumberReservation phoneNumberReservation) throws
	 TelusAPIException, NumberMatchException
	 {
		 //todo: Implement this com.telus.api.account.Subscriber method
		 throw new java.lang.UnsupportedOperationException(
		 "Method reservePhoneNumber() not yet implemented.");
	 }

	 public Charge newCharge() throws TelusAPIException
	 {
		 throw new java.lang.UnsupportedOperationException(
		 "Method getAccount() not implemented in server class.");
	 }

	 public Credit newCredit() throws TelusAPIException
	 {
		 throw new java.lang.UnsupportedOperationException(
		 "Method getAccount() not implemented in server class.");
	 }

	 public Credit newCredit(boolean taxable) throws TelusAPIException
	 {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public Credit newCredit(char taxOption) throws TelusAPIException
	 {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public Discount newDiscount() throws TelusAPIException
	 {
		 throw new java.lang.UnsupportedOperationException(
		 "Method getAccount() not implemented in server class.");
	 }

	 public Discount[] getDiscounts() throws TelusAPIException
	 {
		 throw new java.lang.UnsupportedOperationException(
		 "Method not implemented here");
	 }

	 public java.lang.String getSubscriberId()
	 {
		 return subscriberId;
	 }

	 public void setSubscriberId(String subscriberId)
	 {
		 this.subscriberId = subscriberId;
	 }

	 public java.lang.String getPhoneNumber()
	 {
		 return phoneNumber;
	 }

	 public void setPhoneNumber(String phoneNumber)
	 {
		 this.phoneNumber = phoneNumber;
	 }

	 public ContractChangeHistory[] getContractChangeHistory(Date from, Date to) throws
	 TelusAPIException, HistorySearchException
	 {
		 return null;
	 }

	 public HandsetChangeHistory[] getHandsetChangeHistory(Date from, Date to) throws
	 TelusAPIException, HistorySearchException
	 {
		 return null;
	 }

	 public PricePlanChangeHistory[] getPricePlanChangeHistory(Date from, Date to) throws
	 TelusAPIException, HistorySearchException
	 {
		 return null;
	 }

	 public ServiceChangeHistory[] getServiceChangeHistory(Date from, Date to) throws
	 TelusAPIException, HistorySearchException
	 {
		 return null;
	 }

	 public ServiceChangeHistory[] getServiceChangeHistory(Date from, Date to, boolean includeAllServices) throws
	 TelusAPIException, HistorySearchException
	 {
		 return null;
	 }

	 public ResourceChangeHistory[] getResourceChangeHistory(String type, Date from, Date to) throws
	 TelusAPIException, HistorySearchException
	 {
		 return null;
	 }

	 public void setUserValueRating(String userValueRating)
	 {
		 this.userValueRating = userValueRating;
	 }

	 public String getUserValueRating()
	 {
		 return userValueRating;
	 }

	 public boolean isWesternMarketProvince()
	 {
		 return this.marketProvince.equalsIgnoreCase("BC") ||
		 this.marketProvince.equalsIgnoreCase("AB");
	 }

	 public void applyCredit(Card card) throws TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public Card[] getCards() throws TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public Card[] getCards(String cardType) throws TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public PricePlanSummary[] getAvailablePricePlans() throws TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public PricePlanSummary[] getAvailablePricePlans(String equipmentType) throws TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }
	 
	 /**
	  * @deprecated
	  */
	 public PricePlanSummary[] getAvailablePricePlans(boolean telephonyEnabled,
			 boolean dispatchEnabled, boolean webEnabled,
			 boolean clientActivation, boolean dealerActivation) throws
			 TelusAPIException
			 {
		 throw new UnsupportedOperationException("method not implemented here");
			 }
	 
	  public PricePlanSummary[] getAvailablePricePlans(boolean getAll, String equipmentType) throws TelusAPIException {
		  // TODO Implement method
		  return null;
	  }

	  public PricePlanSummary[] getAvailablePricePlans(boolean telephonyEnabled, boolean dispatchEnabled, boolean webEnabled, int term, 
	  boolean isCurrentOnly, boolean isActivationOnly, String equipmentType) throws TelusAPIException {
		  // TODO Implement method
		  return null;
	  }

	  public ContractService[] testContractAfterEquipmentChange(Equipment newEquipment) throws TelusAPIException {
		  // TODO Implement Method
		  return null;
	  }

	 public PricePlan getAvailablePricePlan(String pricePlanCode) throws
	 TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public PricePlan getAvailablePricePlan(ServiceSummary pricePlan) throws
	 TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public ApplicationMessage[] testChangeEquipment(Equipment newEquipment, String dealerCode,
			 String salesRepCode, String requestorId,
			 String repairId, String swapType) throws
			 TelusAPIException, SerialNumberInUseException,
			 InvalidEquipmentChangeException
			 {
		 throw new UnsupportedOperationException("method not implemented here");
			 }

	 public ApplicationMessage[] testChangeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public void testChangeEquipment(IDENEquipment newIDENEquipment,
			 String dealerCode, String salesRepCode,
			 String requestorId, String repairId,
			 String swapType,
			 MuleEquipment associatedMuleEquipment) throws
			 TelusAPIException, SerialNumberInUseException,
			 InvalidEquipmentChangeException
			 {
		 throw new UnsupportedOperationException("method not implemented here");
			 }

	 public void testChangeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment, boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public EquipmentChangeRequest newEquipmentChangeRequest(Equipment
			 newEquipment, String dealerCode, String salesRepCode, String requestorId,
			 String repairId, String swapType) throws TelusAPIException,
			 SerialNumberInUseException, InvalidEquipmentChangeException
			 {
		 throw new UnsupportedOperationException("method not implemented here");
			 }

	 public EquipmentChangeRequest newEquipmentChangeRequest(Equipment
			 newEquipment, String dealerCode, String salesRepCode, String requestorId,
			 String repairId, String swapType, boolean preserveDigitalServices) throws TelusAPIException,
			 SerialNumberInUseException, InvalidEquipmentChangeException
			 {
		 throw new UnsupportedOperationException("method not implemented here");
			 }

	 public EquipmentChangeRequest newEquipmentChangeRequest(IDENEquipment
			 newIDENEquipment, String dealerCode, String salesRepCode,
			 String requestorId, String repairId,
			 String swapType, MuleEquipment associatedMuleEquipment) throws
			 TelusAPIException, SerialNumberInUseException,
			 InvalidEquipmentChangeException
			 {
		 throw new UnsupportedOperationException("method not implemented here");
			 }

	 public ActivationCredit findAvailableActivationCredit(String creditType) throws
	 TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public ActivationCredit[] findAvailableActivationCredits() throws
	 TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public VoiceUsageSummary getVoiceUsageSummary() throws VoiceUsageSummaryException, TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public VoiceUsageSummary getVoiceUsageSummary(String featureCode) throws VoiceUsageSummaryException, TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }


	 public WebUsageSummary getWebUsageSummary() throws TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public String getSLALevel() throws TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public SubscriptionRole getSubscriptionRole() throws
	 TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public void setSubscriptionRole(SubscriptionRole subscriptionRole) throws TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public SubscriptionRole newSubscriptionRole() throws TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public double getTerminationFee() throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public void cancel(String reason, char depositReturnMethod) throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public void cancel(String reason, char depositReturnMethod, String waiverReason) throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public void cancel(Date activityDate, String reason, char depositReturnMethod, String waiverReason, String memoText) throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public void suspend(String reason) throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public void restore(String reason) throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public void restore(Date activityDate, String reason, String memoText) throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public void suspend(Date activityDate, String reason, String memoText) throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public ReasonType[] getAvailableCancellationReasons() throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public ReasonType[] getAvailableSuspensionReasons() throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public ReasonType[] getAvailableResumptionReasons() throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public char getAccountStatusChangeAfterCancel() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public char getAccountStatusChangeAfterSuspend() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public boolean isShareablePricePlanPrimary()  throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public boolean isShareablePricePlanSecondary() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 /************************************************
	  * TAX EXEMPTION
	  ************************************************/

	 /**
	  * NO-OP
	  */
	 public TaxExemption getTaxExemption() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 /**
	  * Determines if subscriber is GST exempt
	  * @return boolean
	  */
	 public boolean isGSTExempt() {
		 return isGSTExempt;
	 }

	 /**
	  * Determines if subscriber is PST exempt
	  * @return boolean
	  */
	 public boolean isPSTExempt() {
		 return isPSTExempt;
	 }

	 /**
	  * Determines if subscriber is HST exempt
	  * @return boolean
	  */
	 public boolean isHSTExempt() {
		 return isHSTExempt;
	 }

	 /**
	  * Sets if subscriber is GST exempt
	  * @param value
	  */
	 public void isGSTExempt(boolean value) {
		 isGSTExempt = value;
	 }

	 /**
	  * Sets if subscriber is PST exempt
	  * @param value
	  */
	 public void isPSTExempt(boolean value) {
		 isPSTExempt = value;
	 }

	 /**
	  * Sets if subscriber is HST exempt
	  * @param value
	  */
	 public void isHSTExempt(boolean value) {
		 isHSTExempt = value;
	 }

	 /**
	  * Gets GST exemption expiry date
	  * @return Date
	  */
	 public Date getGSTExemptionExpiryDate() {
		 return gstExemptionExpiryDate;
	 }

	 /**
	  * Gets PST exemption expiry date
	  * @return Date
	  */
	 public Date getPSTExemptionExpiryDate() {
		 return pstExemptionExpiryDate;
	 }

	 /**
	  * Gets HST exemption expiry date
	  * @return Date
	  */
	 public Date getHSTExemptionExpiryDate() {
		 return hstExemptionExpiryDate;
	 }

	 /**
	  * Sets GST exemption expiry date
	  * @param value
	  */
	 public void setGSTExemptionExpiryDate(Date value) {
		 gstExemptionExpiryDate = value;
	 }

	 /**
	  * Sets PST exemption expiry date
	  * @param value
	  */
	 public void setPSTExemptionExpiryDate(Date value) {
		 pstExemptionExpiryDate = value;
	 }

	 /**
	  * Sets HST exemption expiry date
	  * @param value
	  */
	 public void setHSTExemptionExpiryDate(Date value) {
		 hstExemptionExpiryDate = value;
	 }

	 /**
	  * NO-OP
	  */
	 public Memo getLastMemo(String memoType) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public SubscriberHistory[] getHistory(Date from, Date to) throws
	 TelusAPIException, HistorySearchException
	 {
		 return null;
	 }

	 /**
	  * NO-OP
	  */
	 public void move(Account account, boolean transferOwnership, String reasonCode, String memoText) throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public void move(Account account, boolean transferOwnership, String reasonCode, String memoText, String dealerCode, String salesRepCode ) throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public Date getStatusDate()
	 {
		 return statusUpdateDate;
	 }

	 public void setStatusDate(Date value)
	 {
		 statusUpdateDate = value;
	 }

	 public void sendFax(final int form, String faxNumber, String language) throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method not implemented here");
	 }


	 public void sendEmail(final int form, String email, String language) throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method not implemented here");
	 }

	 public void refreshSwitch() throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method not implemented here");
	 }

	 public String getProvisioningStatus() throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method not implemented here");
	 }

	 public CancellationPenalty getCancellationPenalty() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public String getSupportLevel() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }



	 public String getInvoiceCallSortOrderCode() {
		 return invoiceCallSortOrderCode;
	 }

	 public void setInvoiceCallSortOrderCode(String invoiceCallSortOrderCode) {
		 this.invoiceCallSortOrderCode = invoiceCallSortOrderCode;
	 }


	 public InvoiceCallSortOrderType getInvoiceCallSortOrder() {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public void setInvoiceCallSortOrder(String invoiceCallSortOrder) {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public Date getGSTExemptEffectiveDate() {
		 return gstExemptionEffectiveDate;
	 }

	 public void setGSTExemptEffectiveDate(Date GSTExemptEffectiveDate){
		 this.gstExemptionEffectiveDate = GSTExemptEffectiveDate;
	 }

	 public Date getPSTExemptEffectiveDate() {
		 return pstExemptionEffectiveDate;
	 }

	 public void setPSTExemptEffectiveDate(Date PSTExemptEffectiveDate){
		 this.pstExemptionEffectiveDate = PSTExemptEffectiveDate;
	 }

	 public Date getHSTExemptEffectiveDate() {
		 return hstExemptionEffectiveDate;
	 }

	 public void setHSTExemptEffectiveDate(Date HSTExemptEffectiveDate){
		 this.hstExemptionEffectiveDate = HSTExemptEffectiveDate;
	 }

	 public String getGSTCertificateNumber() {
		 return gstCertificateNumber;
	 }

	 public void setGSTCertificateNumber(String GSTCertificateNumber){
		 this.gstCertificateNumber = GSTCertificateNumber;
	 }

	 public String getPSTCertificateNumber() {
		 return pstCertificateNumber;
	 }

	 public void setPSTCertificateNumber(String PSTCertificateNumber){
		 this.pstCertificateNumber = PSTCertificateNumber;
	 }

	 public String getHSTCertificateNumber() {
		 return hstCertificateNumber;
	 }

	 public void setHSTCertificateNumber(String HSTCertificateNumber){
		 this.hstCertificateNumber = HSTCertificateNumber;
	 }

	 public void createDeposit(double Amount, String memoText) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

//	 public LMSLetterRequest newLMSLetterRequest(Letter letter) throws TelusAPIException {
//		 throw new UnsupportedOperationException("Method not implemented here");
//	 }

	 public DepositHistory[] getDepositHistory() throws
	 TelusAPIException, HistorySearchException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public DiscountPlan[] getAvailableDiscountPlans() throws TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public Credit[] getPromotionalCredits() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public Credit[] getCredits(Date from, Date to, String billState) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public Credit[] getCreditsByReasonCode(Date from, Date to, String billState, String reasonCode) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }
	 
	 public Credit[] getCreditsByReasonCode(Date from, Date to, String billState, String reasonCode, String knowbilityOperatorId) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public Address getAddress()  {
		 return newAddressInfo;
	 }

	 public Address getAddress(boolean refresh) throws AddressNotFoundException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public void setAddress(Address newAddress) throws TelusAPIException {
		this.newAddressInfo = (AddressInfo) newAddress;
	 }

	 public void removeFutureDatedPricePlanChange() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 /**
	  * Deprecated this method since SD will call SEMS new Web Service for PCS equipment change history. 
	  * 
	  * @deprecated
	  */
	 public EquipmentChangeHistory[] getEquipmentChangeHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		 return null;
	 }

	 public SubscriberCommitment getCommitment() {
		 if (commitment == null)
			 commitment = new CommitmentInfo();

		 return commitment;
	 }

	 public void setCommitment(CommitmentInfo commitment) {
		 this.commitment = commitment;
	 }

	 public InvoiceTax getInvoiceTax(int billSeqNo) throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method not implemented here!!!");
	 }

	 public String[] getMultiRingPhoneNumbers() {
		 return this.multiRingPhoneNumbers;
	 }

	 public void setMultiRingPhoneNumbers(String[] multiRingPhoneNumbers) {
		 this.multiRingPhoneNumbers = multiRingPhoneNumbers;
	 }

	 public UsageProfileListsSummary getUsageProfileListsSummary(int billSeqNo) throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method getUsageProfileListsSummary not implemented here.");
	 }

	 public boolean isHotlined() {
		 return hotlined;
	 }

	 public void setHotlined(String hotlined) {
		 if ("Y".equals(hotlined))
			 this.hotlined = true;
	 }

	 public Date getMigrationDate() {
		 return migrationDate;
	 }

	 public void setMigrationDate(Date migrationDate) {
		 this.migrationDate = migrationDate;
	 }

	 public MigrationType getMigrationType() {
		 return migrationType;
	 }

	 public void setMigrationType(MigrationType migrationType) {
		 this.migrationType = migrationType;
	 }

	 public String getMigrationTypeCode() {
		 return migrationTypeCode;
	 }

	 public void setMigrationTypeCode(String migrationTypeCode) {
		 this.migrationTypeCode = migrationTypeCode;
	 }

	 /**
	  * @deprecated
	  */
	 public boolean isValidMigrationForPhoneNumber(MigrationType migrationType) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }
	 
	 public boolean isValidMigrationForPhoneNumber(MigrationType migrationType, String sourceNetowrkType, String targetNetworkType) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }
	 
	 public MigrationRequest newMigrationRequest(Account account, Equipment newEquipment, String pricePlanCode)throws InvalidEquipmentChangeException, TelusAPIException{
		 throw new UnsupportedOperationException("Method not implemented here");
	 }
	 
	 public MigrationRequest newMigrationRequest(Account account, IDENEquipment newEquipment, MuleEquipment associatedEquipment, String pricePlanCode)throws InvalidEquipmentChangeException, TelusAPIException{
		 throw new UnsupportedOperationException("Method not implemented here");
	 }
	 
	 public MigrationRequest newMigrationRequest(Account newAccount, Equipment newEquipment, Equipment newAssociatedHandset, String pricePlanCode) throws InvalidMigrationRequestException, UnsupportedEquipmentException, TelusAPIException {
		 // TODO Implement method
		 return null;
	 }

	 public Subscriber migrate(MigrationRequest migrationRequest, String dealerCode, String salesRepCode, String requestorId) throws InvalidMigrationRequestException, TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }
	 
	 public void testMigrate(MigrationRequest migrationRequest, String dealerCode, String salesRepCode, String requestorId) throws InvalidMigrationRequestException, TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public PricePlanSummary[] getAvailablePricePlans(boolean getAll) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public QueueThresholdEvent[] getQueueThresholdEvents(Date from, Date to)
	 throws TelusAPIException {
		 // TODO Auto-generated method stub
		 return null;
	 }
	 
	 public long getSubscriptionId() {
		 return subscriptionId;
	 }
	 
	 public void setSubscriptionId(long subscriptionId) {
		 this.subscriptionId = subscriptionId;
	 }
	 
	 public double getRequestedSecurityDeposit() {
		 return securityDeposit;
	 }

	 public void setSecurityDeposit(double securityDeposit) {
		 this.securityDeposit = securityDeposit;
	 }

	 public void save(boolean activate, ActivationOption selectedOption) throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method save() not implemented in server class.");
	 }

	 public void save(Date startServiceDate, ActivationOption selectedOption) throws TelusAPIException {
		 throw new java.lang.UnsupportedOperationException("Method save() not implemented in server class.");
	 }

	 public PortRequest  getPortRequest() throws PRMSystemException,  TelusAPIException{
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public PortRequest  newPortRequest(String phoneNumber, String NPDirectionIndicator, boolean prePopulate) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public String getPortType(){
		 return portType;
	 }

	 public Date  getPortDate(){
		 return portDate;
	 }

	 public void setPortDate(Date portDate) {
		 this.portDate = portDate;
	 }

	 public void setPortType(String portType) {
		 this.portType = portType;
	 }

	 public PortRequestSummary getPortRequestSummary()throws PRMSystemException,  TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public void reservePhoneNumber(PhoneNumberReservation phoneNumberReservation, boolean portIn)  throws PortRequestException, TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public void reservePhoneNumber(PhoneNumberReservation phoneNumberReservation, PortInEligibility portInEligibility)  throws PortRequestException, TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public void save(boolean activate, ActivationOption selectedOption, PortInEligibility portInEligibility) throws PortRequestException, TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, String reasonCode, boolean changeOtherNumbers, String dealerCode, String salesRepCode, PortInEligibility portInEligibility) throws PhoneNumberException, PhoneNumberInUseException, PortRequestException, PRMSystemException, TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public void activate(String reason, Date startServiceDate, String memoText, boolean isPortIn, boolean modifyPortRequest) throws PortRequestException, PRMSystemException, TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public void activate(String reason, Date startServiceDate, String memoText, boolean isPortIn, boolean modifyPortRequest, ServiceRequestHeader header) throws PortRequestException, PRMSystemException, TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public void unreserve(boolean cancelPortIn) throws PRMSystemException, TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public void restore(Date activityDate, String reason, String memoText, String portOption, PortInEligibility portInEligibility) throws PortRequestException, PRMSystemException, TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public ApplicationMessage[] changeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public ApplicationMessage[] changeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices, char allowDuplicateSerialNo, ServiceRequestHeader header) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public void changeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public ApplicationMessage[] testChangeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public void testChangeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }
	 public void move(Account account, boolean transferOwnership, String reasonCode, String memoText, ActivationOption selectedOption) throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public void move(Account account, boolean transferOwnership, String reasonCode, String memoText, String dealerCode, String salesRepCode, ActivationOption selectedOption) throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public double getPaidSecurityDeposit() throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public boolean isPortInd() {
		 return portInd;
	 }

	 public void setPortInd(boolean portInd) {
		 this.portInd = portInd;
	 }

	 public CallingCirclePhoneList[] getCallingCirclePhoneNumberListHistory( Date from, Date to) {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public int getBrandId() {    
		 return brandId;
	 }

	 public void setBrandId(int brandId) {
		 this.brandId = brandId;
	 }

	 public void save(boolean activate, ActivationOption selectedOption, ServicesValidation srvValidation)throws TelusAPIException{
		 throw new UnsupportedOperationException("Method not implemented here"); 
	 }

	 public void save(Date startServiceDate, ActivationOption selectedOption, ServicesValidation srvValidation)throws TelusAPIException{
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public void activate(ServicesValidation srvValidation, String reason, String memoText)throws TelusAPIException{
		 throw new UnsupportedOperationException("Method not implemented here"); 
	 }

	 public void activate(String reason, Date startServiceDate, String memoText, ServicesValidation srvValidation)throws TelusAPIException{
		 throw new UnsupportedOperationException("Method not implemented here"); 
	 }

	 public FeatureParameterHistory[] getFeatureParameterHistory(String[] parameterNames, Date from, Date to) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here"); 
	 }

	 public FeatureParameterHistory[] getFeatureParameterChangeHistory(Date from, Date to) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here"); 
	 }
	 
	 public boolean isInternalUse() {
		 return internalUse;
	 }

	 public void setInternalUse(boolean internalUse) {
		 this.internalUse = internalUse;
	 }

	 public String[] getSubscriberInterBrandPortActivityReasonCodes() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here"); 
	 }

	 public void save(boolean activate, ActivationOption selectedOption, PortInEligibility portInEligibility ,ServicesValidation srvValidation) throws PortRequestException, TelusAPIException{
		 throw new UnsupportedOperationException("Method not implemented here");  
	 }

	 public void activate(String reason, Date startServiceDate, String memoText, boolean isPortIn, boolean modifyPortRequest, ServicesValidation srvValidation)throws PortRequestException, PRMSystemException, TelusAPIException{
		 throw new UnsupportedOperationException("Method not implemented here"); 
	 }

	 public VendorServiceChangeHistory[] getVendorServiceChangeHistory(String SOC) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	public void activate(String reason, Date startServiceDate, String memoText,
			ServiceRequestHeader header) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	}

	public void cancel(Date activityDate, String reason,
			char depositReturnMethod, String waiverReason, String memoText,
			ServiceRequestHeader header) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	}

	public void changeEquipment(IDENEquipment newIDENEquipment,
			String dealerCode, String salesRepCode, String requestorId,
			String repairId, String swapType,
			MuleEquipment associatedMuleEquipment, char allowDuplicateSerialNo,
			ServiceRequestHeader header) throws TelusAPIException,
			SerialNumberInUseException, InvalidEquipmentChangeException {
		 throw new UnsupportedOperationException("Method not implemented here");
	}

	public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber,
			boolean changeOtherNumbers, String dealerCode, String salesRepCode,
			String reasonCode, ServiceRequestHeader header)
			throws TelusAPIException, PhoneNumberException,
			PhoneNumberInUseException {
		 throw new UnsupportedOperationException("Method not implemented here");
	}

	public void move(Account account, boolean transferOwnership,
			String reasonCode, String memoText, String dealerCode,
			String salesRepCode, ActivationOption selectedOption,
			ServiceRequestHeader header) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	}

	public void restore(Date activityDate, String reason, String memoText,
			ServiceRequestHeader header) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	}

	public boolean isHSPA() {
		return isHSPA;
	}

	public void setHSPA(boolean isHSPA) {
		this.isHSPA = isHSPA;
	} 

	public boolean hasDummyESN() {
		return hasDummyESN;
	}

	public void setHasDummyESN(boolean hasDummyESN) {
		this.hasDummyESN = hasDummyESN;
	}

	public SubscriptionPreference getSubscriptionPreference(
			int preferenceTopicId) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	}

	public boolean getDealerHasDeposit(){
		throw new UnsupportedOperationException("Method not implemented here"); 
	}

	public void setDealerHasDeposit(boolean dealerHasDeposit) {
		throw new UnsupportedOperationException("Method not implemented here");
		
	}

	public Contract getContractForEquipmentChange(
			EquipmentChangeRequest equipmentChangeRequest)
			throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public EquipmentChangeRequest newEquipmentChangeRequest(
			Equipment newEquipment, String dealerCode, String salesRepCode,
			String requestorId, String repairId, String swapType,
			boolean preserveDigitalServices, char allowDuplicateSerialNo)
			throws TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	
	public double getAirtimeRate() throws InvalidAirtimeRateException, 	InvalidSubscriberStatusException, TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method not implemented here");
	}
	
	public void setSeatData(SeatData seatData) {
		this.seatData = seatData;
	}

	public SeatData getSeatData() {
		return seatData;
	}

	public boolean isSeatSubscriber() {
		return (seatData != null && !seatData.getSeatType().equals(SeatType.SEAT_TYPE_MOBILE));	
	}


}
