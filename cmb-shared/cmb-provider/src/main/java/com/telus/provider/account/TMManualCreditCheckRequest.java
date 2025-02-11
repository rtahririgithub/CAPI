package com.telus.provider.account;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.Address;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.ManualCreditCheckRequest;
import com.telus.api.account.PersonalCredit;
import com.telus.api.account.PostpaidBusinessRegularAccount;
import com.telus.api.account.PostpaidConsumerAccount;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PostpaidBoxedConsumerAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessPersonalAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.account.info.PostpaidCorporatePersonalAccountInfo;
import com.telus.eas.account.info.PostpaidEmployeeAccountInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.util.Logger;

public class TMManualCreditCheckRequest implements ManualCreditCheckRequest {

	private ConsumerNameInfo consumerName = new ConsumerNameInfo();
	private PersonalCredit personalCredit;
	private TMAddress address;
	protected String businessPhone;
	protected String homePhone;
	protected final TMProvider provider;
	private TMAccount originalAccount;
	private Date incorporationDate;
	private String incorporationNumber;
	protected boolean transformed = false;
	private String companyName;

	public TMManualCreditCheckRequest(TMProvider provider, Account account) {
		this.provider = provider;
		originalAccount = (TMAccount) account;
		address = new TMAddress(provider, new AddressInfo());
		personalCredit = new PersonalCreditInfo();
	}

	public void setBusinessPhone(String phoneNumber) {
		businessPhone = phoneNumber;
	}

	public void setHomePhone(String phoneNumber) {
		homePhone = phoneNumber;
	}

	public int getBanId() {
		return originalAccount.getBanId();
	}

	public char getAccountType() {
		return originalAccount.getAccountType();
	}

	public char getAccountSubType() {
		return originalAccount.getAccountSubType();
	}

	public char getStatus() {
		return originalAccount.getStatus();
	}

	public ConsumerName getConsumerName() {
		return consumerName;
	}

	public PersonalCredit getPersonalCreditInformation() {
		return personalCredit;
	}

	public int getBrandId() {
		return originalAccount.getBrandId();
	}

	public String getBusinessPhone() {
		return businessPhone;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public Address getAddress() {
		return address;
	}

	public void setIncorporationDate(Date date) {
		incorporationDate = date;
	}

	public Date getIncorporationDate() {
		return incorporationDate;
	}

	public void setIncorporationNumber(String number) {
		incorporationNumber = number;
	}

	public String getIncorporationNumber() {
		return incorporationNumber;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public PostpaidConsumerAccount transformToPostpaidConsumerAccount() throws TelusAPIException {
		
		Logger.debug("transformToPostpaidConsumerAccount " + this);
		
		PostpaidConsumerAccountInfo consumerAccountInfo = null;
		if (originalAccount.isPostpaidConsumer()) {
			consumerAccountInfo = PostpaidConsumerAccountInfo.getNewInstance(originalAccount.getAccountSubType());
		} else if (originalAccount.isPostpaidBoxedConsumer()) {
			consumerAccountInfo = PostpaidBoxedConsumerAccountInfo.newPagerInstance0();
		} else if (originalAccount.isPostpaidBusinessPersonal()) {
			consumerAccountInfo = PostpaidBusinessPersonalAccountInfo.getNewInstance0(originalAccount.getAccountSubType());
		} else if (originalAccount.isPostpaidCorporatePersonal()) {
			consumerAccountInfo = PostpaidCorporatePersonalAccountInfo.newInstance0(originalAccount.getAccountSubType());
		} else if (originalAccount.isPostpaidEmployee()) {
			consumerAccountInfo = PostpaidEmployeeAccountInfo.getNewInstance0(originalAccount.getAccountSubType());
		} else {
			consumerAccountInfo = PostpaidConsumerAccountInfo.newPCSInstance();
			consumerAccountInfo.setAccountType(originalAccount.getAccountType());
			consumerAccountInfo.setAccountSubType(originalAccount.getAccountSubType());
		}
		consumerAccountInfo.setBanId(originalAccount.getBanId());
		consumerAccountInfo.setBrandId(originalAccount.getBrandId());
		consumerAccountInfo.setStatus(originalAccount.getStatus());
		consumerAccountInfo.setEvaluationProductType(originalAccount.getDelegate0().getEvaluationProductType());
		consumerAccountInfo.setAddress0(address.getDelegate());
		consumerAccountInfo.setCreateDate(originalAccount.getCreateDate());
		consumerAccountInfo.getPersonalCreditInformation().setBirthDate(personalCredit.getBirthDate());
		consumerAccountInfo.getPersonalCreditInformation().setDriversLicense(personalCredit.getDriversLicense());
		consumerAccountInfo.getPersonalCreditInformation().setDriversLicenseExpiry(personalCredit.getDriversLicenseExpiry());
		consumerAccountInfo.getPersonalCreditInformation().setSin(personalCredit.getSin());

		TMPostpaidConsumerAccount consumerAccount = new TMPostpaidConsumerAccount(provider, consumerAccountInfo);
		consumerAccount.manualCreditRequest = this;
		consumerAccount.setBusinessPhone(businessPhone);
		consumerAccount.setHomePhone(homePhone);
		consumerAccount.getName().setFirstName(consumerName.getFirstName());
		consumerAccount.getName().setAdditionalLine(consumerName.getAdditionalLine());
		consumerAccount.getName().setGeneration(consumerName.getGeneration());
		consumerAccount.getName().setLastName(consumerName.getLastName());
		consumerAccount.getName().setMiddleInitial(consumerName.getMiddleInitial());
		consumerAccount.getName().setNameFormat(consumerName.getNameFormat());
		consumerAccount.getName().setTitle(consumerName.getTitle());
		// PCI change: replace set number, excpiryMonth and expiryYear by setting Credit Card;
		consumerAccount.getPersonalCreditInformation().getCreditCard().copyFrom(personalCredit.getCreditCard());
		
		transformed = true;

		return consumerAccount;
	}

	public PostpaidBusinessRegularAccount transformToBusinessRegularAccount() throws TelusAPIException {
		
		Logger.debug("transformToBusinessRegularAccount " + this);
		
		PostpaidBusinessRegularAccountInfo businessAccountInfo = PostpaidBusinessRegularAccountInfo.newPCSInstance();
		businessAccountInfo.setBanId(originalAccount.getBanId());
		businessAccountInfo.setBrandId(originalAccount.getBrandId());
		businessAccountInfo.setAccountType(originalAccount.getAccountType());
		businessAccountInfo.setAccountSubType(originalAccount.getAccountSubType());
		businessAccountInfo.setStatus(originalAccount.getStatus());
		businessAccountInfo.setEvaluationProductType(originalAccount.getDelegate0().getEvaluationProductType());
		businessAccountInfo.setAddress0(address.getDelegate());
		businessAccountInfo.getContactName().setLastName(companyName); //company name		
		businessAccountInfo.setCreateDate(originalAccount.getCreateDate());

		TMPostpaidBusinessRegularAccount businessAccount = new TMPostpaidBusinessRegularAccount(provider, businessAccountInfo);
		businessAccount.manualCreditRequest = this;
		businessAccount.setBusinessPhone(businessPhone);
		businessAccount.setHomePhone(homePhone);
		businessAccount.getCreditInformation().setIncorporationDate(incorporationDate);
		businessAccount.getCreditInformation().setIncorporationNumber(incorporationNumber);
		businessAccount.setLegalBusinessName(companyName);

		transformed = true;

		return businessAccount;
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer(128);

		s.append("TMManualCreditCheckRequest:[\n");
		s.append("    ban=[").append(originalAccount.getBanId()).append("]\n");
		s.append("    brandId=[").append(originalAccount.getBrandId()).append("]\n");
		s.append("    accType=[").append(originalAccount.getAccountType()).append("]\n");
		s.append("    accSubType=[").append(originalAccount.getAccountSubType()).append("]\n");
		s.append("    status=[").append(originalAccount.getStatus()).append("]\n");
		try {
			s.append("    evaluationProductType=[").append(originalAccount.getDelegate0().getEvaluationProductType()).append("]\n");
		} catch (TelusAPIException tapie) {
			Logger.debug("TMManualCreditCheckRequest: ban=" + originalAccount.getBanId() + ", error in getEvaluationProductType.");
		}
		s.append("    consumerName=[").append(consumerName).append("]\n");
		s.append("    personalCredit=[").append(personalCredit).append("]\n");
		s.append("    address=[").append(address.getDelegate()).append("]\n");
		s.append("    businessPhone=[").append(businessPhone).append("]\n");
		s.append("    homePhone=[").append(homePhone).append("]\n");
		s.append("    incorporationDate=[").append(incorporationDate).append("]\n");
		s.append("    incorporationNumber=[").append(incorporationNumber).append("]\n");
		s.append("    companyName=[").append(companyName).append("]\n");
		s.append("    transformed=[").append(transformed).append("]\n");
		s.append("]");

		return s.toString();
	}

}