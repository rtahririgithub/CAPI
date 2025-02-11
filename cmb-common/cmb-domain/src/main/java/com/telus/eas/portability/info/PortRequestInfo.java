package com.telus.eas.portability.info;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestAddress;
import com.telus.api.portability.PortRequestException;
import com.telus.api.portability.PortRequestName;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.subscriber.info.SubscriberInfo;


public class PortRequestInfo extends Info implements PortRequest {
	private static final long serialVersionUID = 1L;
	private String portDirectionIndicator;
	private PortRequestAddressInfo portRequestAddress = new PortRequestAddressInfo();
	private PortRequestNameInfo portRequestName = new PortRequestNameInfo();
	private String businessName;
	private String  agencyAuthorizationName;
	private Date  agencyAuthorizationDate;
	private String  agencyAuthorizationIndicator;
	private String  alternateContactNumber;
	private Date desiredDateTime;
	private String oSPSerialNumber;
	private String oSPAccountNumber;
	private String oSPPin;
	private String remarks;
	private boolean  autoActivate;
	private String phoneNumber;
	private int banId;
	private String portRequestId;
	private String type;
	private String statusCategory;
	private String  statusCode;
	private String  statusReasonCode;
	private Date creationDate;
	private boolean canBeActive;
	private boolean canBeSubmit;
	private boolean canBeCancel;
	private boolean canBeModify;
	private SubscriberInfo subscriber;
	private int incomingBrandId;
	private int outgoingBrandId;
	
	private String expedite;
	private String dslInd;
	private Integer dslLineNumber;
	private boolean endUserMovingInd;
	private String oldReseller;
	private AccountInfo account;
	private EquipmentInfo equipment;
	private int platformId = PortInEligibility.PORT_PLATFORM_TELUS;
	private boolean platformIdUpdated = false; //this is added for performance reason and allows overriding only.

	public AccountInfo getAccount() {
		return account;
	}
	public void setAccount(AccountInfo account) {
		this.account = account;
	}
	public EquipmentInfo getEquipment() {
		return equipment;
	}
	public void setEquipment(EquipmentInfo equipment) {
		this.equipment = equipment;
	}
	public SubscriberInfo getSubscriber() {
		return subscriber;
	}
	public void setSubscriber(SubscriberInfo subscriber) {
		this.subscriber = subscriber;
	}
	public String getPortDirectionIndicator(){
		return portDirectionIndicator;
	}
	public void setPortDirectionIndicator(String portDirectionIndicator){
		this.portDirectionIndicator = portDirectionIndicator;
	}
	public PortRequestAddress getPortRequestAddress(){
		return portRequestAddress;
	}
	public void setPortRequestAddress (PortRequestAddress portRequestAddress){
		this.portRequestAddress = (PortRequestAddressInfo)portRequestAddress;
	}
	public PortRequestName getPortRequestName(){
		return portRequestName;
	}
	public void setPortRequestName (PortRequestName portRequestName){
		this.portRequestName = (PortRequestNameInfo)portRequestName;
	}
	public String getBusinessName(){
		return businessName;
	}
	public void setBusinessName(String businessName){
		this.businessName = businessName;
	}
	public String getAgencyAuthorizationName(){
		return agencyAuthorizationName;
	}
	public void  setAgencyAuthorizationName(String  agencyAuthorizationName){
		this.agencyAuthorizationName = agencyAuthorizationName;
	}
	public Date   getAgencyAuthorizationDate(){
		return agencyAuthorizationDate;
	}
	public void  setAgencyAuthorizationDate(Date  agencyAuthorizationDate){
		this.agencyAuthorizationDate = agencyAuthorizationDate;
	}
	public String getAuthorizationIndicator(){
		return agencyAuthorizationIndicator;
	}
	public void  setAgencyAuthorizationIndicator(String  agencyAuthorizationIndicator){
		this.agencyAuthorizationIndicator = agencyAuthorizationIndicator;
	}
	public String getAlternateContactNumber(){
		return alternateContactNumber;
	}
	public void  setAlternateContactNumber (String  alternateContactNumber){
		this.alternateContactNumber = alternateContactNumber;
	}
	public Date getDesiredDateTime(){
		return desiredDateTime;
	}
	public void setDesiredDateTime(Date desiredDateTime){
		this.desiredDateTime = desiredDateTime;
	}
	public String getOSPSerialNumber(){
		return oSPSerialNumber;
	}
	public void setOSPSerialNumber (String oSPSerialNumber){
		this.oSPSerialNumber = oSPSerialNumber;
	}
	public String getOSPAccountNumber(){
		return oSPAccountNumber;
	}
	public void setOSPAccountNumber (String oSPAccountNumber){
		this.oSPAccountNumber = oSPAccountNumber;
	}
	public String getOSPPin(){
		return oSPPin;
	}
	public void setOSPPin (String oSPPin){
		this.oSPPin = oSPPin;
	}
	public String getRemarks(){
		return remarks;
	}
	public void setRemarks(String remarks){
		this.remarks = remarks;
	}
	public boolean isAutoActivate(){
		return autoActivate;
	}
	public void setAutoActivate(boolean  autoActivate){
		this.autoActivate = autoActivate;
	}

	public void validate() throws PortRequestException, TelusAPIException{
		throw new UnsupportedOperationException("Method not implemented here");
	}
	public void activate() throws PortRequestException, TelusAPIException{
		throw new UnsupportedOperationException("Method not implemented here");
	}
	// from PortRequestSummary
	public String getPhoneNumber(){
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}
	public int getBanId(){
		return banId;
	}
	public void setBanId(int banId){
		this.banId = banId;
	}
	public String getPortRequestId(){
		return portRequestId;
	}
	public void setPortRequestId(String portRequestId){
		this.portRequestId = portRequestId;
	}
	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type = type;
	}
	public String getStatusCategory(){
		return statusCategory;
	}
	public void setStatusCategory(String statusCategory){
		this.statusCategory = statusCategory;
	}
	public String  getStatusCode(){
		return statusCode;
	}
	public void setStatusCode(String statusCode){
		this.statusCode = statusCode;
	}
	public String  getStatusReasonCode(){
		return statusReasonCode;
	}
	public void  setStatusReasonCode(String statusReasonCode){
		this.statusReasonCode = statusReasonCode;
	}
	public Date getCreationDate(){
		return creationDate;
	}
	public void setCreationDate(Date creationDate){
		this.creationDate = creationDate;
	}
	public boolean canBeActivated(){
		return canBeActive;
	}
	public void setCanBeActivate(boolean canBeActive){
		this.canBeActive = canBeActive;
	}
	public boolean canBeSubmitted(){
		return canBeSubmit;
	}
	public void setCanBeSubmit(boolean canBeSubmit){
		this.canBeSubmit = canBeSubmit;
	}
	public boolean canBeCanceled(){
		return canBeCancel;
	}
	public void setCanBeCancel(boolean canBeCancel){
		this.canBeCancel = canBeCancel;
	}
	public boolean canBeModified(){
		return canBeModify;
	}
	public void setCanBeModify(boolean canBeModify){
		this.canBeModify = canBeModify;
	}
	public int getIncomingBrandId() {
		return incomingBrandId;
	}
	public void setIncomingBrandId(int incomingBrandId) {
		this.incomingBrandId = incomingBrandId;
	}
	public int getOutgoingBrandId() {
		return outgoingBrandId;
	}
	public void setOutgoingBrandId(int outgoingBrandId) {
		this.outgoingBrandId = outgoingBrandId;
	}	
	
	public String toString() {
		StringBuffer s = new StringBuffer(128);

		s.append("PortRequestInfo:[\n");

		s.append("    portDirectionIndicator=[").append(portDirectionIndicator).append("]\n");
		s.append("    portRequestAddress=[").append(portRequestAddress).append("]\n");
		s.append("    portRequestName=[").append(portRequestName).append("]\n");
		s.append("    businessName=[").append(businessName).append("]\n");
		s.append("    agencyAuthorizationName=[").append(agencyAuthorizationName).append("]\n");
		s.append("    agencyAuthorizationIndicator=[").append(agencyAuthorizationIndicator).append("]\n");
		s.append("    alternateContactNumber=[").append(alternateContactNumber).append("]\n");
		s.append("    desiredDateTime=[").append(desiredDateTime).append("]\n");
		s.append("    oSPSerialNumber=[").append(oSPSerialNumber).append("]\n");
		s.append("    oSPAccountNumber=[").append(oSPAccountNumber).append("]\n");
		s.append("    oSPPin=[").append(oSPPin).append("]\n");
		s.append("    remarks=[").append(remarks).append("]\n");
		s.append("    autoActivate=[").append(autoActivate).append("]\n");
		s.append("    phoneNumber=[").append(phoneNumber).append("]\n");
		s.append("    banId=[").append(banId).append("]\n");
		s.append("    type=[").append(type).append("]\n");
		s.append("    statusCategory=[").append(statusCategory).append("]\n");
		s.append("    statusCode=[").append(statusCode).append("]\n");
		s.append("    statusReasonCode=[").append(statusReasonCode).append("]\n");
		s.append("    creationDate=[").append(creationDate).append("]\n");
		s.append("    canBeActive=[").append(canBeActive).append("]\n");
		s.append("    canBeSubmit=[").append(canBeSubmit).append("]\n");
		s.append("    canBeCancel=[").append(canBeCancel).append("]\n");
		s.append("    canBeModify=[").append(canBeModify).append("]\n");
		s.append("    incomingBrandId=[").append(incomingBrandId).append("]\n");
		s.append("    outgoingBrandId=[").append(outgoingBrandId).append("]\n");
		s.append("    expedite=[").append(expedite).append("]\n");
		s.append("    dslInd=[").append(dslInd).append("]\n");
		s.append("    dslLineNumber=[").append(dslLineNumber).append("]\n");
		s.append("    endUserMovingInd=[").append(endUserMovingInd).append("]\n");
		s.append("    oldReseller=[").append(oldReseller).append("]\n");
		s.append("    platformId=[").append(platformId).append("]\n");
		s.append("]");

		return s.toString();
	}
	public String getExpedite() {
		return expedite;
	}
	public void setExpedite(String expedite) {
		this.expedite = expedite;
	}
	public String getDslInd() {
		return dslInd;
	}
	public void setDslInd(String dslInd) {
		this.dslInd = dslInd;
	}
	public Integer getDslLineNumber() {
		return dslLineNumber;
	}
	public void setDslLineNumber(Integer dslLineNumber) {
		this.dslLineNumber = dslLineNumber;
	}
	public boolean getEndUserMovingInd() {
		return endUserMovingInd;
	}
	public void setEndUserMovingInd(boolean endUserMovingInd) {
		this.endUserMovingInd = endUserMovingInd;
	}
	public String getOldReseller() {
		return oldReseller;
	}
	public void setOldReseller(String oldReseller) {
		this.oldReseller = oldReseller;
	}
	public int getPlatformId() {
		return this.platformId;
	}
	public void setPlatformId(int platformId) {
		this.platformId = platformId;
		this.platformIdUpdated = true;
	}
	/**
	 * The platformId is accurate only after calling PortRequestRetrievalService. If we need to use
	 * the platformId, we should invoke the service to have it updated. 
	 */
	public boolean isPortInFromMVNE() {
		return this.platformId != PortInEligibility.PORT_PLATFORM_TELUS;
	}
	
	public boolean isPlatformIdUpdated() {
		return this.platformIdUpdated;
	}
}
