package com.telus.cmb.account.informationhelper.task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.telus.api.account.AccountSummary;
import com.telus.cmb.account.informationhelper.mapping.AccountRetrievalMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.PostpaidBoxedConsumerAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessDealerAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessOfficialAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessPersonalAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.account.info.PostpaidCorporatePersonalAccountInfo;
import com.telus.eas.account.info.PostpaidCorporateRegularAccountInfo;
import com.telus.eas.account.info.PostpaidEmployeeAccountInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.WesternPrepaidConsumerAccountInfo;

public class AccountRetrievalTask {
	private boolean postpaidConsumer = false;
	private boolean postpaidBusinessRegular = false;
	private boolean postpaidBusinessPersonal = false;
	private boolean postpaidBusinessDealer = false;
	private boolean postpaidBusinessOffical = false;
	private boolean prepaidConsumer = false;
	private boolean quebecTelPrepaidConsumer = false;
	private boolean westernPrepaidConsumer = false;
	private boolean IDENCorporateVPN = false;
	private boolean IDENCorporate = false;
	private boolean postpaidLikeBusinessRegular = false;
	private boolean postpaidBoxedConsumer = false;
	private boolean postpaidCorporateRegional = false;
	private boolean postpaidConsumerEmployee = false;
	private boolean postpaidConsumerEmployeeNew = false;
	private boolean postpaidCorporateAutotel = false;
	private boolean postpaidCorporatePersonal = false;
	private boolean postpaidCorporateRegular = false;
	protected AccountInfo accountInfo;
	private char accountType;
	private char accountSubType;
	protected ResultSet resultSet;
	protected Date logicalDate;
	
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
	
	public AccountRetrievalTask (char accountType, char accountSubType, ResultSet resultSet) {
		this.accountType = accountType;
		this.accountSubType = accountSubType;
		this.resultSet = resultSet;
	}

	public void determineAccountType() {
		if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR)){
			postpaidConsumer = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_TELUS_EMPLOYEE )) {
			postpaidConsumerEmployee = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE_NEW || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL )) {
			postpaidConsumerEmployeeNew = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR ||accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR || accountSubType==AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_REGULAR_MEDIUM || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_CONNECT_REGULAR)) {
			postpaidBusinessRegular = true ;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_OFFICAL)	{
			postpaidBusinessOffical = true ;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS &&( accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_PERSONAL|| accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_CONNECT_PERSONAL)) {
			postpaidBusinessPersonal = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_DEALER || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_DEALER)) {
			postpaidBusinessDealer = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID) {
			prepaidConsumer = true ;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_QUEBECTEL) { 
			quebecTelPrepaidConsumer = true ;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_WESTERN_PREPAID)	{
			westernPrepaidConsumer = true ;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR) {
			IDENCorporate = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PRIVATE_NETWORK_PLUS) {
			IDENCorporateVPN = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_BOXED)	{
			postpaidBoxedConsumer = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE  && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGIONAL) {
			postpaidCorporateRegional = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_EARS)) {
			postpaidCorporateAutotel = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_INDIVIDUAL || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_EMPLOYEE)) {
			postpaidCorporatePersonal = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE  &&
				(accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_FEDERAL_GOVERNMENT ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ENTERPRISE ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CORPORATE ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ABORIGINAL ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_FUSION_EAST_CONV ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_KEY ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_OFFICIAL ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CNBS ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_GOVERNMENT ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_NATIONAL_STRATEGIC ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_REGIONAL_STRATEGIC ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_TMI_AFFILIATE ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_TMI_DIVISION || 
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_RESELLER)) {
			postpaidCorporateRegular = true;
		} else {
			postpaidLikeBusinessRegular = true;
		}
	}
	
	private boolean isIDEN() {
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
	
	public AccountInfo createAccountInfoInstance() {	
		if (isPostpaidConsumerType()) {

			if (postpaidConsumerEmployee) {
				accountInfo = PostpaidEmployeeAccountInfo.newPCSInstance0();
			} else if (postpaidConsumerEmployeeNew && !isIDEN()) {
				accountInfo = PostpaidEmployeeAccountInfo.newPCSInstance1();
			} else if (postpaidConsumerEmployeeNew && isIDEN()) {
				accountInfo = PostpaidEmployeeAccountInfo.newIDENInstance1();
			} else if (isIDEN()) {
				accountInfo = PostpaidConsumerAccountInfo.newIDENInstance();
			} else if (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR) {
				accountInfo = PostpaidConsumerAccountInfo.newPagerInstance();
			} else if (postpaidBoxedConsumer) {
				accountInfo = PostpaidBoxedConsumerAccountInfo.newPagerInstance();
			} else if (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR) {
				accountInfo = PostpaidConsumerAccountInfo.newAutotelInstance();
			} else {
				accountInfo = PostpaidConsumerAccountInfo.newPCSInstance();
			}
		}else if (isPostpaidPersonalType()) {
			if (isIDEN()) {
				accountInfo = PostpaidBusinessPersonalAccountInfo.newIDENInstance();
			} if (postpaidCorporatePersonal){
				accountInfo = PostpaidCorporatePersonalAccountInfo.newInstance0(accountSubType);
			}  else {
				accountInfo = PostpaidBusinessPersonalAccountInfo.newPCSInstance();
			}
		}else if (isPrepaidConsumerType()) {

			if (prepaidConsumer) {
				accountInfo = PrepaidConsumerAccountInfo.newPCSInstance(AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID);
			} else if (quebecTelPrepaidConsumer) {
				accountInfo = PrepaidConsumerAccountInfo.newPCSInstance(AccountSummary.ACCOUNT_SUBTYPE_PCS_QUEBECTEL);
			} else {
				accountInfo = WesternPrepaidConsumerAccountInfo.newPCSInstance();
			}
		}else if (isPostpaidBusinessType() || isPostpaidCorporateType()) {

			if (isIDEN()) {
				if (IDENCorporateVPN) {
					accountInfo = PostpaidCorporateRegularAccountInfo.newInstance(AccountSummary.ACCOUNT_SUBTYPE_IDEN_PRIVATE_NETWORK_PLUS);
				} else if (IDENCorporate) {
					accountInfo = PostpaidCorporateRegularAccountInfo.newInstance(accountSubType);
				} else if (postpaidBusinessRegular || postpaidLikeBusinessRegular) {
					accountInfo = PostpaidBusinessRegularAccountInfo.newIDENInstance();
				} else if (postpaidBusinessDealer) {
					accountInfo = PostpaidBusinessDealerAccountInfo.newIDENInstance0();
				}
			} else if (postpaidBusinessOffical) {
				accountInfo = PostpaidBusinessOfficialAccountInfo.newPCSInstance0();
			} else if (postpaidBusinessDealer) {
				accountInfo = PostpaidBusinessDealerAccountInfo.newPCSInstance0();
			} else if (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR) {
				accountInfo = PostpaidBusinessRegularAccountInfo.newPagerInstance();
			} else if (postpaidCorporateRegional) {
				accountInfo = PostpaidCorporateRegularAccountInfo.newInstance(AccountSummary.ACCOUNT_SUBTYPE_PCS_REGIONAL);
			} else if (postpaidCorporateAutotel) {
				accountInfo = PostpaidCorporateRegularAccountInfo.newInstance(accountSubType);
			} else if (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR) {
				accountInfo = PostpaidBusinessRegularAccountInfo.newAutotelInstance();
			} else if (postpaidCorporateRegular) {
				accountInfo = PostpaidCorporateRegularAccountInfo.newInstance0(accountSubType);
			} else {
				accountInfo = PostpaidBusinessRegularAccountInfo.newPCSInstance();
			}
		}
		
		return accountInfo;
	}
	
	private boolean isPostpaidConsumerType() {
		return (postpaidConsumer || postpaidBoxedConsumer || postpaidConsumerEmployee || postpaidConsumerEmployeeNew);
	}
	
	private boolean isPostpaidPersonalType() {
		return (postpaidBusinessPersonal || postpaidCorporatePersonal);
	}
	
	private boolean isPrepaidConsumerType() {
		return (prepaidConsumer || quebecTelPrepaidConsumer || westernPrepaidConsumer);
	}
	
	private boolean isPostpaidBusinessType() {
		return (postpaidBusinessRegular || postpaidLikeBusinessRegular || postpaidBusinessDealer || postpaidBusinessOffical );
	}
	
	private boolean isPostpaidCorporateType() {
		return (IDENCorporateVPN || IDENCorporate || postpaidCorporateRegional || postpaidCorporateAutotel || postpaidCorporateRegular);
	}
	
	public void mapData() throws SQLException {
		AccountRetrievalMapper mapper = new AccountRetrievalMapper (accountInfo, resultSet, logicalDate);
		
		if (isPostpaidConsumerType() || isPostpaidPersonalType()) {
			PostpaidConsumerAccountInfo postpaidConsumerAccount = (PostpaidConsumerAccountInfo)accountInfo;
			
			mapper.setPaymentMethod(postpaidConsumerAccount.getPaymentMethod0());
			mapper.setPersonalCreditInfo(postpaidConsumerAccount.getPersonalCreditInformation0());
			mapper.setConsumerNameInfo(postpaidConsumerAccount.getName0());
		}else if (isPrepaidConsumerType()) {
			PrepaidConsumerAccountInfo prepaidConsumerAccount = (PrepaidConsumerAccountInfo) accountInfo;
			
			mapper.setConsumerNameInfo(prepaidConsumerAccount.getName0());
			mapper.setPrepaidSpecificInfo();
		}else if (isPostpaidBusinessType() || isPostpaidCorporateType()) {
			PostpaidBusinessRegularAccountInfo postpaidBusinessRegulaAccount = (PostpaidBusinessRegularAccountInfo) accountInfo;
			
			mapper.setPaymentMethod(postpaidBusinessRegulaAccount.getPaymentMethod0());
			mapper.setPersonalCreditInfo(postpaidBusinessRegulaAccount.getPersonalCreditInformation0());
			mapper.setBusinessDetails();
		}
		mapper.setCommonDetails();
	}

	public Date getLogicalDate() {
		return logicalDate;
	}

	public void setLogicalDate(Date logicalDate) {
		this.logicalDate = logicalDate;
	}
	
	
}
