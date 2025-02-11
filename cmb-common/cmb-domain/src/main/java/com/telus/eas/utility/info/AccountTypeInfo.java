/**
 * Title:        AccountTypeInfo<p>
 * Description:  The AccountTypeInfo contains the account type information.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */
package com.telus.eas.utility.info;

import com.telus.api.account.AccountSummary;
import com.telus.api.reference.AccountType;
import com.telus.api.reference.Brand;
import com.telus.eas.framework.info.Info;

public class AccountTypeInfo extends Info implements AccountType, Cloneable{

	private static final long serialVersionUID = 2L;

	private static char[] accountSubtypeCorporateIden = {
		AccountSummary.ACCOUNT_SUBTYPE_IDEN_GOVERNMENT,
		AccountSummary.ACCOUNT_SUBTYPE_IDEN_FEDERAL_GOVENMENT,
		AccountSummary.ACCOUNT_SUBTYPE_IDEN_INDIVIDUAL,
		AccountSummary.ACCOUNT_SUBTYPE_IDEN_ENTERPRISE,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_IDEN_REGULAR,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_IDEN_PUBLIC_SAFETY,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_IDEN_REGIONAL_STRATEGIC,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_IDEN_NATIONAL_STRATEGIC,
		AccountSummary.ACCOUNT_SUBTYPE_IDEN_PRIVATE_NETWORK_PLUS,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_IDEN_DURHAM_POLICE
	};

	private static char[] accountSubtypeCorporatePCS = {
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_FEDERAL_GOVERNMENT,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ENTERPRISE,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CORPORATE,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ABORIGINAL,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_EMPLOYEE,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_FUSION_EAST_CONV,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_KEY,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_OFFICIAL,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CNBS,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_GOVERNMENT,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_INDIVIDUAL,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_NATIONAL_STRATEGIC,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_REGIONAL_STRATEGIC,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_TMI_AFFILIATE,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_TMI_DIVISION
	};	
	
	protected char accountType;
	protected char accountSubType ;
	protected String description ;
	protected String descriptionFrench ;
	protected String numberLocation ;
	protected String defaultDealer;
	protected String defaultSalesCode ;
	protected boolean duplicateBANCheck;
	protected boolean creditCheckRequired;
	private int minimumSubscribersForFleet;
	private char billingNameFormat;
	private int brandId = Brand.BRAND_ID_TELUS;

	public void setAccountType(char accountType) {
		this.accountType = accountType;
	}

	public void setAccountSubType(char accountSubType) {
		this.accountSubType = accountSubType;
	}	

	public void setDescription(String description) {
		this.description = description;
	}	

	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}

	public void setNumberLocation(String numberLocation) {
		this.numberLocation = numberLocation;
	}

	public void setDefaultDealer(String defaultDealer) {
		this.defaultDealer = defaultDealer;
	}

	public void setDefaultSalesCode(String defaultSalesCode) {
		this.defaultSalesCode = defaultSalesCode;
	}

	public void setDuplicateBANCheck(boolean duplicateBANCheck) {
		this.duplicateBANCheck = duplicateBANCheck;
	}

	public void setMinimumSubscribersForFleet(int minimumSubscribersForFleet) {
		this.minimumSubscribersForFleet = minimumSubscribersForFleet;
	}

	public void setBillingNameFormat(char billingNameFormat) {
		this.billingNameFormat = billingNameFormat;
	}

	public void setCreditCheckRequired(boolean creditCheckRequired) {
		this.creditCheckRequired = creditCheckRequired;
	}
	
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public char getAccountType() {
		return accountType;
	}

	public char getAccountSubType() {
		return accountSubType;
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptionFrench() {
		return descriptionFrench;
	}

	public String getNumberLocation() {
		return numberLocation;
	}

	public String getDefaultDealer() {
		return defaultDealer;
	}

	public String getDefaultSalesCode() {
		return defaultSalesCode;
	}	

	public String getCode() {
		return String.valueOf(accountType) + String.valueOf(accountSubType);
	}

	public int getMinimumSubscribersForFleet() {
		return minimumSubscribersForFleet;
	}

	public char getBillingNameFormat() {
		return billingNameFormat;
	}

	public boolean isCreditCheckRequired() {
		return creditCheckRequired;
	}

	public boolean isDuplicateBANCheck() {
		return duplicateBANCheck;
	}
	
	public int getBrandId() {
		return brandId;
	}

	public boolean isPostpaidEmployee() {
		return isPostpaidEmployee(accountType, accountSubType); 
	}
	
	public static boolean isPostpaidEmployee(char accountType, char accountSubType) {
		return accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER &&
		(accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_TELUS_EMPLOYEE ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE_NEW ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL  );
	}
	
	public boolean isPostpaidConsumer() {
		return isPostpaidConsumer(accountType, accountSubType);
	}

	public static boolean isPostpaidConsumer(char accountType, char accountSubType) {
		return accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER &&
		(accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_TELUS_EMPLOYEE ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE_NEW ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL );
	}

	public boolean isPostpaidBoxedConsumer() {
		return isPostpaidBoxedConsumer(accountType, accountSubType);
	}
	
	public static boolean isPostpaidBoxedConsumer(char accountType, char accountSubType) {
		return accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER &&
		(accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_BOXED);
	}

	public boolean isPostpaidBusinessRegular() {
		return isPostpaidBusinessRegular(accountType, accountSubType);
	}

	public static boolean isPostpaidBusinessRegular(char accountType, char accountSubType) {
		return accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS &&
		(accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_DEALER ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_DEALER ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_OFFICAL ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR);
	}

	public boolean isPostpaidOfficial(){
		return isPostpaidOfficial(accountType, accountSubType);
	}

	public static boolean isPostpaidOfficial(char accountType, char accountSubType){
		return accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_OFFICAL;
	}

	public boolean isPostpaidBusinessOfficial() {
		return isPostpaidBusinessOfficial(accountType, accountSubType);
	}

	public static boolean isPostpaidBusinessOfficial(char accountType, char accountSubType) {
		return accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS &&
		accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_OFFICAL;
	}

	public boolean isPostpaidBusinessPersonal() {
		return isPostpaidBusinessPersonal(accountType, accountSubType);
	}

	public static boolean isPostpaidBusinessPersonal(char accountType, char accountSubType) {
		return accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS &&
		(accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_PERSONAL ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL);
	}

	public boolean isPostpaidBusinessDealer() {
		return isPostpaidBusinessDealer(accountType, accountSubType);
	}

	public static boolean isPostpaidBusinessDealer(char accountType, char accountSubType) {
		return accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS &&
		(accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_DEALER ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_DEALER);
	}

	public static boolean isSuspendedDueToNonPayment(char accountType, char accountSubType) {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public boolean isPrepaidConsumer() {
		return isPrepaidConsumer(accountType, accountSubType);
	}

	public static boolean isPrepaidConsumer(char accountType, char accountSubType) {
		return accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER &&
		(accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_QUEBECTEL ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_WESTERN_PREPAID);

	}

	public boolean isPostpaid() {
		return isPostpaid(accountType, accountSubType);
	}

	public static boolean isPostpaid(char accountType, char accountSubType) {
		return  !isPrepaidConsumer(accountType, accountSubType)
		&& !isQuebectelPrepaidConsumer(accountType, accountSubType)
		&& !isWesternPrepaidConsumer(accountType, accountSubType);
	}

	public boolean isQuebectelPrepaidConsumer() {
		return isQuebectelPrepaidConsumer(accountType, accountSubType);
	}

	public static boolean isQuebectelPrepaidConsumer(char accountType, char accountSubType) {
		return accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER
		&& accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_QUEBECTEL
		;
	}

	public boolean isWesternPrepaidConsumer() {
		return isWesternPrepaidConsumer(accountType, accountSubType);
	}

	public static boolean isWesternPrepaidConsumer(char accountType, char accountSubType) {
		return accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER
		&& accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_WESTERN_PREPAID
		;
	}

	public boolean isIDEN() {
		return isIDEN(accountType, accountSubType);
	}
	
	public static boolean isIDEN(char accountType, char accountSubType) {
		if (AccountSummary.ACCOUNT_TYPE_CORPORATE == accountType) {
			for (int i = 0; i < accountSubtypeCorporateIden.length; i++) {
				if (accountSubType == accountSubtypeCorporateIden[i])
					return true;
			}
			if (accountSubType == '9') // PCS: Corporate - Autotel EARS
				return false;
		}
		return Character.isDigit(accountSubType);
	}

	public boolean isPCS() {
		return isPCS(accountType, accountSubType);
	}

	public boolean isPager() {
		return isPager(accountType, accountSubType);	
	}

	public static boolean isPCS(char accountType, char accountSubType) {
		return !isIDEN(accountType, accountSubType) && !isPager(accountType, accountSubType) && !isAutotel(accountType, accountSubType);
	}

	public static boolean isPager(char accountType, char accountSubType) {
		return ((accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER
				&& (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_BOXED))
				||
				(accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS
						&& accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR));
	}

	public boolean isAutotel() {
		return isAutotel(accountType, accountSubType);
	}

	public static boolean isAutotel(char accountType, char accountSubType) {
		return ((accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER
				&& accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR)
				||
				(accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS
						&& accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR)
						||
						(accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE
								&& (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_EARS)));
	}

	public boolean isCorporate() {
		return isCorporate(accountType, accountSubType);
	}

	public static boolean isCorporate(char accountType, char accountSubType) {
		return accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE;
	}

	public boolean isCorporateRegular() {
		return isCorporateRegular(accountType, accountSubType);
	}

	public static boolean isCorporateRegular(char accountType, char accountSubType) {
		return accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE &&
		accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR;
	}

	public boolean isCorporateRegional() {
		return isCorporateRegional(accountType, accountSubType);
	}
	
	public static boolean isCorporateRegional(char accountType, char accountSubType) {
		return accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE &&
		accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGIONAL;
	}

	public boolean isCorporatePrivateNetworkPlus() {
		return isCorporatePrivateNetworkPlus(accountType, accountSubType);
	}

	public static boolean isCorporatePrivateNetworkPlus(char accountType, char accountSubType) {
		return accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE &&
		accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PRIVATE_NETWORK_PLUS;
	}	

	 public boolean isPostpaidCorporatePersonal() {
		 return isPostpaidCorporatePersonal(accountType, accountSubType);
	 }

	 public static boolean isPostpaidCorporatePersonal(char accountType, char accountSubType) {
		return isPostpaid(accountType, accountSubType) && (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE) &&
		(accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_INDIVIDUAL ||
				accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_EMPLOYEE);
	}

	public boolean isPostpaidCorporateRegular() {
		return isPostpaidCorporateRegular(accountType, accountSubType);
	}
	
	public static boolean isPostpaidCorporateRegular(char accountType, char accountSubType) {
		return isPostpaid(accountType, accountSubType) && (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE) &&
		((isCorporatePCS(accountType, accountSubType) && !isPostpaidCorporatePersonal(accountType, accountSubType)) 
				|| isCorporateIDEN(accountType, accountSubType) || isAutotel(accountType, accountSubType));
	}
	
	public boolean isCorporatePCS() {
		return isCorporatePCS(accountType, accountSubType);
	}
	
	public static boolean isCorporatePCS(char accountType, char accountSubType) {
		if (isPCS(accountType, accountSubType) && accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE) {
			for (int i = 0; i < accountSubtypeCorporatePCS.length; i++) {
				if (accountSubType == accountSubtypeCorporatePCS[i])
					return true;
			}
		}
		return false;
	}

	public boolean isCorporateIDEN() {
		return isCorporateIDEN(accountType, accountSubType);
	}
	
	public static boolean isCorporateIDEN(char accountType, char accountSubType) {
		if (isIDEN(accountType, accountSubType) && accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE) {
			for (int i = 0; i < accountSubtypeCorporateIden.length; i++) {
				if (accountSubType == accountSubtypeCorporateIden[i])
					return true;
			}
		}
		return false;
	}
	
	public boolean isPCSPostpaidCorporateRegularAccount() {
		return isPCSPostpaidCorporateRegularAccount(accountType, accountSubType);
	}
	
	public static boolean isPCSPostpaidCorporateRegularAccount(char accountType, char accountSubType) {
		return isCorporatePCS(accountType, accountSubType) 
			   && isPostpaid(accountType, accountSubType) 
			   && !isPostpaidCorporatePersonal(accountType, accountSubType);
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("AccountType:{\n");
		s.append("    brandId=[").append(brandId).append("]\n");
		s.append("    accountType=[").append(accountType).append("]\n");
		s.append("    accountSubType=[").append(accountSubType).append("]\n");
		s.append("    description=[").append(description).append("]\n");
		s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
		s.append("    numberLocation=[").append(numberLocation).append("]\n");
		s.append("    defaultDealer=[").append(defaultDealer).append("]\n");
		s.append("    defaultSalesCode=[").append(defaultSalesCode).append("]\n");
		s.append("    duplicateBANCheck=[").append(duplicateBANCheck).append("]\n");
		s.append("    billingNameFormat=[").append(billingNameFormat).append("]\n");
		s.append("    billingNameFormat=[").append(creditCheckRequired).append("]\n");
		s.append("}");

		return s.toString();
	}
	
	public Object clone() {
		return super.clone();
	}
}
