package com.telus.cmb.account.lifecyclemanager.dao.impl;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.CreditClassChangeInfo;
import amdocs.APILink.datatypes.CreditResultDeposit;
import amdocs.APILink.datatypes.CreditResultExt1;
import amdocs.APILink.datatypes.CreditResultExtInfo;
import amdocs.APILink.datatypes.CreditResultInfo;
import amdocs.APILink.datatypes.CreditResultUpdateInfo;
import amdocs.APILink.datatypes.NameInfo;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;

import com.telus.api.ApplicationException;
import com.telus.api.account.AccountSummary;
import com.telus.cmb.account.lifecyclemanager.dao.CreditCheckDao;
import com.telus.cmb.account.utilities.TelusToAmdocsAccountMapper;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTelusMapping;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;

public class CreditCheckDaoImpl extends AmdocsDaoSupport implements CreditCheckDao{
	private static final Logger LOGGER = Logger.getLogger(CreditCheckDaoImpl.class);	

	@Override
	public void saveCreditCheckInfo(final AccountInfo pAccountInfo, final CreditCheckResultInfo pCreditCheckResultInfo,
			final String pCreditParamType, final ConsumerNameInfo pConsumerNameInfo,
			final AddressInfo pAddressInfo, String sessionId)	throws ApplicationException {

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {		
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {

				CreditResultExtInfo amdocsCreditResultExtInfo = new CreditResultExtInfo();
				NameInfo amdocsCreditCheckNameInfo = new amdocs.APILink.datatypes.NameInfo();
				amdocs.APILink.datatypes.AddressInfo amdocsCreditCheckAddressInfo = new amdocs.APILink.datatypes.AddressInfo();
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(pAccountInfo.getBanId());

				// print input to this method
				LOGGER.debug("AccountInfo:" + pAccountInfo.toString());
				LOGGER.debug("CreditCheckResultInfo:" + pCreditCheckResultInfo.toString());

				// map telus info classes to amdocs class
				amdocsCreditResultExtInfo = (CreditResultExtInfo) TelusToAmdocsAccountMapper.mapTelusToAmdocs(
						pCreditCheckResultInfo, amdocsCreditResultExtInfo);

				PostpaidConsumerAccountInfo postpaidConsumerAccountInfo = (PostpaidConsumerAccountInfo) pAccountInfo;
				amdocsCreditResultExtInfo = (CreditResultExtInfo) TelusToAmdocsAccountMapper.mapTelusToAmdocs(
						postpaidConsumerAccountInfo.getPersonalCreditInformation0(),
						amdocsCreditResultExtInfo);

				// manually set certain parameters
				if (pCreditParamType != null)
					amdocsCreditResultExtInfo.creditParamType = AttributeTranslator
					.byteFromString(pCreditParamType); // 'Individual' or
				// 'Manual'
				else
					amdocsCreditResultExtInfo.creditParamType = (byte) 'I'; // 'Individual'

				if (pCreditCheckResultInfo.getErrorCode() != 0)
					amdocsCreditResultExtInfo.creditRequestStatus = (byte) 'E';
				else
					amdocsCreditResultExtInfo.creditRequestStatus = CreditResultExtInfo.CREDIT_REQUEST_STATUS_DONE;

				if (pConsumerNameInfo != null) {
					amdocsCreditCheckNameInfo = (NameInfo) TelusToAmdocsAccountMapper.mapTelusToAmdocs(
							pConsumerNameInfo, amdocsCreditCheckNameInfo);
					updateBanConv.setCreditName(amdocsCreditCheckNameInfo);
				}

				if (pAddressInfo != null) {
					amdocsCreditCheckAddressInfo = (amdocs.APILink.datatypes.AddressInfo) 
					TelusToAmdocsAccountMapper.mapTelusToAmdocs (pAddressInfo, amdocsCreditCheckAddressInfo);
					//					    Address - CreditCheck
					amdocs.APILink.datatypes.AddressInfo originalAmdocsCreditCheckAddressInfo = 
						updateBanConv.getCreditAddress();
					if (!amdocsCreditCheckAddressInfo.city.trim().equals("") &&
							(amdocsCreditCheckAddressInfo.type != originalAmdocsCreditCheckAddressInfo.type  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.attention,originalAmdocsCreditCheckAddressInfo.attention) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.primaryLine,originalAmdocsCreditCheckAddressInfo.primaryLine) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.secondaryLine,originalAmdocsCreditCheckAddressInfo.secondaryLine) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.city,originalAmdocsCreditCheckAddressInfo.city) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.province,originalAmdocsCreditCheckAddressInfo.province) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.postalCode,originalAmdocsCreditCheckAddressInfo.postalCode) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.civicNo,originalAmdocsCreditCheckAddressInfo.civicNo) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.civicNoSuffix,originalAmdocsCreditCheckAddressInfo.civicNoSuffix) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.streetDirection,originalAmdocsCreditCheckAddressInfo.streetDirection) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.streetName,originalAmdocsCreditCheckAddressInfo.streetName) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.streetType,originalAmdocsCreditCheckAddressInfo.streetType) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.unitDesignator,originalAmdocsCreditCheckAddressInfo.unitDesignator) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.unitIdentifier,originalAmdocsCreditCheckAddressInfo.unitIdentifier) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrAreaNumber,originalAmdocsCreditCheckAddressInfo.rrAreaNumber) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrBox,originalAmdocsCreditCheckAddressInfo.rrBox) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrCompartment,originalAmdocsCreditCheckAddressInfo.rrCompartment) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrDeliveryType,originalAmdocsCreditCheckAddressInfo.rrDeliveryType) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrDesignator,originalAmdocsCreditCheckAddressInfo.rrDesignator) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrGroup,originalAmdocsCreditCheckAddressInfo.rrGroup) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrIdentifier,originalAmdocsCreditCheckAddressInfo.rrIdentifier) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrQualifier,originalAmdocsCreditCheckAddressInfo.rrQualifier) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrSite,originalAmdocsCreditCheckAddressInfo.rrSite) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.country,originalAmdocsCreditCheckAddressInfo.country) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.zipGeoCode,originalAmdocsCreditCheckAddressInfo.zipGeoCode) != 0  ||
									AttributeTranslator.compare(amdocsCreditCheckAddressInfo.foreignState,originalAmdocsCreditCheckAddressInfo.foreignState) != 0 )) {

						LOGGER.debug("Calling setCreditAddress()..." );
						updateBanConv.setCreditAddress(amdocsCreditCheckAddressInfo);
					}
				}


				// print input to Amdocs
				printAmdocsCreditResultExtInfo(amdocsCreditResultExtInfo);

				// perform credit evaluation
				LOGGER.debug("Excecuting setCreditEvaluationInfo() - start..." );
				updateBanConv.setCreditEvaluationInfo(amdocsCreditResultExtInfo);
				LOGGER.debug("Excecuting setCreditEvaluationInfo() - end..." );
				return null;
			}
		});
	}

	@Override
	public void saveCreditCheckInfoForBusiness(final AccountInfo pAccountInfo,final BusinessCreditIdentityInfo[] listOfBusinesses,
			final BusinessCreditIdentityInfo selectedBusiness, final CreditCheckResultInfo pCreditCheckResultInfo,
			final String pCreditParamType, String sessionId)	throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {
				CreditResultExtInfo amdocsCreditResultExtInfo = new CreditResultExtInfo();
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(pAccountInfo.getBanId());

				// print input to this method
				LOGGER.debug("AccountInfo:" + pAccountInfo.toString());
				LOGGER.debug("Business CreditCheckResultInfo:" + pCreditCheckResultInfo.toString());

				// map telus info classes to amdocs class
				amdocsCreditResultExtInfo = (CreditResultExtInfo) TelusToAmdocsAccountMapper.mapTelusToAmdocs(
						pCreditCheckResultInfo, amdocsCreditResultExtInfo);

				PostpaidBusinessRegularAccountInfo postpaidBusinessRegularAccountInfo = (PostpaidBusinessRegularAccountInfo) pAccountInfo;

				amdocsCreditResultExtInfo = (CreditResultExtInfo) TelusToAmdocsAccountMapper.mapTelusToAmdocs(
						postpaidBusinessRegularAccountInfo.getCreditInformation0(),
						amdocsCreditResultExtInfo);

				// map the list of businesses
				// add the condition check for PCS Business Official Account
				if (listOfBusinesses != null && listOfBusinesses.length > 0) {
					amdocsCreditResultExtInfo.creditResultExt1 = new CreditResultExt1[listOfBusinesses.length];
					for (int i = 0; i < listOfBusinesses.length; i++) {
						amdocsCreditResultExtInfo.creditResultExt1[i] = new CreditResultExt1();
						amdocsCreditResultExtInfo.creditResultExt1[i].companyName = listOfBusinesses[i]
						                                                                             .getCompanyName();
						amdocsCreditResultExtInfo.creditResultExt1[i].marketAccount = listOfBusinesses[i]
						                                                                               .getMarketAccount();
					}
				}

				// add the condition check for PCS Business Official Account
				if (selectedBusiness != null) {
					// map the selected business
					amdocsCreditResultExtInfo.companyName = selectedBusiness
					.getCompanyName();
					amdocsCreditResultExtInfo.marketAccount = selectedBusiness
					.getMarketAccount();
				}

				// manually set certain parameters
				if (pCreditParamType != null)
					amdocsCreditResultExtInfo.creditParamType = AttributeTranslator
					.byteFromString(pCreditParamType); // 'Business' or
				// 'Manual'
				else
					amdocsCreditResultExtInfo.creditParamType = (byte) 'B'; // 'Business'

				if (pCreditCheckResultInfo.getErrorCode() != 0)
					amdocsCreditResultExtInfo.creditRequestStatus = (byte) 'E';
				else
					amdocsCreditResultExtInfo.creditRequestStatus = CreditResultExtInfo.CREDIT_REQUEST_STATUS_DONE;

				// print input to Amdocs
				printAmdocsCreditResultExtInfo(amdocsCreditResultExtInfo);

				LOGGER.debug("Excecuting setCreditEvaluationInfo() - start..." );
				updateBanConv.setCreditEvaluationInfo(amdocsCreditResultExtInfo);
				LOGGER.debug("Excecuting setCreditEvaluationInfo() - end..." );

				return null;
			}
		});

	}

	@Override
	public void updateCreditCheckResult(final int pBan, final String pCreditClass,
			final CreditCheckResultDepositInfo[] pCreditCheckResultDepositInfo,
			final String pDepositChangedReasonCode, final String pDepositChangeText,
			final String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {
				CreditResultUpdateInfo amdocsCreditResultUpdateInfo = new CreditResultUpdateInfo();
				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(pBan);

				// map telus info classes to amdocs class
				amdocsCreditResultUpdateInfo.creditClass = AttributeTranslator.byteFromString(pCreditClass);
				if (pCreditCheckResultDepositInfo != null) {
					amdocsCreditResultUpdateInfo.creditResultDeposit = new CreditResultDeposit[pCreditCheckResultDepositInfo.length];
					for (int i=0; i < pCreditCheckResultDepositInfo.length; i++ ) {
						amdocsCreditResultUpdateInfo.creditResultDeposit[i]= new CreditResultDeposit();
						amdocsCreditResultUpdateInfo.creditResultDeposit[i].depositArrProductType = AttributeTranslator.byteFromString(pCreditCheckResultDepositInfo[i].getProductType());
						amdocsCreditResultUpdateInfo.creditResultDeposit[i].depositAmt = pCreditCheckResultDepositInfo[i].getDeposit();
					}
				}
				amdocsCreditResultUpdateInfo.depositChangeReasonCd = pDepositChangedReasonCode;
				amdocsCreditResultUpdateInfo.depositChangeText = AttributeTranslator.emptyFromNull(pDepositChangeText);

				// print input to call to Amdocs
				LOGGER.debug("Input to updateCreditResult:" );
				LOGGER.debug("  creditClass=[" + amdocsCreditResultUpdateInfo.creditClass + "]");
				LOGGER.debug("  depositChangeReasonCd=[" + amdocsCreditResultUpdateInfo.depositChangeReasonCd + "]");
				LOGGER.debug("  depositChangeText=[" + amdocsCreditResultUpdateInfo.depositChangeText + "]");
				if (amdocsCreditResultUpdateInfo.creditResultDeposit != null) {
					for (int i = 0; i < amdocsCreditResultUpdateInfo.creditResultDeposit.length; i++ ) {
						LOGGER.debug("  deposit " + i + ": depositArrProductType=[" + 
								amdocsCreditResultUpdateInfo.creditResultDeposit[i].depositArrProductType + "]" + 
								" depositAmount=[" + amdocsCreditResultUpdateInfo.creditResultDeposit[i].depositAmt + "]");
					}
				}
				// update Credit Result
				LOGGER.debug("Excecuting updateCreditResult() - start..." );
				updateBanConv.updateCreditResult(amdocsCreditResultUpdateInfo);
				LOGGER.debug("Excecuting updateCreditResult() - end..." );

				return null;
			}
		});
	}

	@Override
	public void updateCreditProfile(final int ban, final String newCreditClass,	final double newCreditLimit, 
			final String memoText, final String sessionId)	throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(ban);

				CreditClassChangeInfo amdocsCreditClassChangeInfo = new CreditClassChangeInfo();
				amdocsCreditClassChangeInfo.newCreditClass =  AttributeTranslator.byteFromString(newCreditClass);
				amdocsCreditClassChangeInfo.newCreditLimit = newCreditLimit;
				amdocsCreditClassChangeInfo.memoText = AttributeTranslator.emptyFromNull(memoText);
				amdocsCreditClassChangeInfo.removeCreditLimit = (newCreditLimit==0);

				updateBanConv.changeCreditClass(amdocsCreditClassChangeInfo);
				return null;
			}
		});		
	}

	private void printAmdocsCreditResultExtInfo(CreditResultExtInfo pCreditResultExtInfo) {
		LOGGER.debug("CreditResultExtInfo=" );
		LOGGER.debug("  bankAccountNumber       =[" + pCreditResultExtInfo.bankAccountNumber + "]");
		LOGGER.debug("  bankBranchNumber        =[" + pCreditResultExtInfo.bankBranchNumber + "]");
		LOGGER.debug("  bankCode                =[" + pCreditResultExtInfo.bankCode + "]");
		LOGGER.debug("  beaconScore             =[" + pCreditResultExtInfo.beaconScore + "]");
		LOGGER.debug("  businessLSts            =[" + AttributeTranslator.stringFrombyte(pCreditResultExtInfo.businessLSts) + "]");
		LOGGER.debug("  companyName             =[" + pCreditResultExtInfo.companyName + "]");
		LOGGER.debug("  contactPhoneNumber      =[" + pCreditResultExtInfo.contactPhoneNumber + "]");
		LOGGER.debug("  crdCardOwnerInd         =[" + AttributeTranslator.stringFrombyte(pCreditResultExtInfo.crdCardOwnerInd) + "]");
		LOGGER.debug("  creditCardExpiryDate    =[" + pCreditResultExtInfo.creditCardExpiryDate + "]");
		LOGGER.debug("  crdConvId               =[" + pCreditResultExtInfo.crdConvId + "]");
		LOGGER.debug("  creditClass             =[" + AttributeTranslator.stringFrombyte(pCreditResultExtInfo.creditClass) + "]");
		LOGGER.debug("  creditClassChangeType   =[" + AttributeTranslator.stringFrombyte(pCreditResultExtInfo.creditClassChangeType) + "]");
		LOGGER.debug("  creditDate              =[" + pCreditResultExtInfo.creditDate + "]");
		LOGGER.debug("  creditLimit             =[" + pCreditResultExtInfo.creditLimit + "]");
		LOGGER.debug("  creditParamType         =[" + AttributeTranslator.stringFrombyte(pCreditResultExtInfo.creditParamType) + "]");
		LOGGER.debug("  creditRequestStatus     =[" + AttributeTranslator.stringFrombyte(pCreditResultExtInfo.creditRequestStatus) + "]");
		LOGGER.debug("  creditResult1           =[" + pCreditResultExtInfo.creditResult1 + "]");
		LOGGER.debug("  creditResult2           =[" + pCreditResultExtInfo.creditResult2 + "]");
		LOGGER.debug("  creditLimit             =[" + pCreditResultExtInfo.creditSequenceNumber + "]");
		LOGGER.debug("  dateOfBirth             =[" + pCreditResultExtInfo.dateOfBirth + "]");
		LOGGER.debug("  depositChangeReasonCode =[" + pCreditResultExtInfo.depositChangeReasonCode + "]");
		LOGGER.debug("  incorporationDate       =[" + pCreditResultExtInfo.incorporationDate + "]");
		LOGGER.debug("  incorporationNumber     =[" + pCreditResultExtInfo.incorporationNumber + "]");
		LOGGER.debug("  marketAccount           =[" + pCreditResultExtInfo.marketAccount + "]");
		LOGGER.debug("  node                    =[" + pCreditResultExtInfo.node + "]");
		LOGGER.debug("  performExistNegInd      =[" + AttributeTranslator.stringFrombyte(pCreditResultExtInfo.performExistNegInd) + "]");
		LOGGER.debug("  performHawkInd          =[" + AttributeTranslator.stringFrombyte(pCreditResultExtInfo.performHawkInd) + "]");
		LOGGER.debug("  pSelectBLbusinessLLine  =[" + pCreditResultExtInfo.pSelectBLbusinessLLine + "]");
		LOGGER.debug("  pSelectBLbusinessLSeqNo =[" + pCreditResultExtInfo.pSelectBLbusinessLSeqNo + "]");
		LOGGER.debug("  sin                     =[" + pCreditResultExtInfo.sin + "]");
		LOGGER.debug("  spouseBirthDate         =[" + pCreditResultExtInfo.spouseBirthDate + "]");
		LOGGER.debug("  spouseFirstName         =[" + pCreditResultExtInfo.spouseFirstName + "]");
		LOGGER.debug("  spouseLastName          =[" + pCreditResultExtInfo.spouseLastName + "]");
		LOGGER.debug("  spouseSin               =[" + pCreditResultExtInfo.spouseSin + "]");
		LOGGER.debug("  transAlert1             =[" + pCreditResultExtInfo.transAlert1 + "]");
		LOGGER.debug("  transAlert2             =[" + pCreditResultExtInfo.transAlert2 + "]");
		LOGGER.debug("  transAlert3             =[" + pCreditResultExtInfo.transAlert3 + "]");
		LOGGER.debug("  transAlert4             =[" + pCreditResultExtInfo.transAlert4 + "]");
		LOGGER.debug("  tuReturnInd             =[" + pCreditResultExtInfo.tuReturnInd + "]");
		for (int i = 0; i < pCreditResultExtInfo.creditResultDeposit.length; i++) {
			LOGGER.debug("  "
					+ i
					+ " depositArrProductType =["
					+ pCreditResultExtInfo.creditResultDeposit[i].depositArrProductType
					+ "] depositAmt =["
					+ pCreditResultExtInfo.creditResultDeposit[i].depositAmt
					+ "]");
		}
		for (int i = 0; i < pCreditResultExtInfo.creditResultExt1.length
		&& pCreditResultExtInfo.creditResultExt1[i] != null; i++) {
			LOGGER.debug("  "
					+ i
					+ " businessLSts =["
					+ AttributeTranslator.stringFrombyte(pCreditResultExtInfo.creditResultExt1[i].businessLSts)
					+ "] marketAccount =["
					+ pCreditResultExtInfo.creditResultExt1[i].marketAccount
					+ "] companyName =["
					+ pCreditResultExtInfo.creditResultExt1[i].companyName
					+ "]");
		}
		for (int i = 0; i < pCreditResultExtInfo.creditResultExt2.length
		&& pCreditResultExtInfo.creditResultExt2[i] != null; i++) {
			LOGGER.debug( "  " + i + " txtResult =[" + pCreditResultExtInfo.creditResultExt2[i].txtResult + "]");
		}
	}

	@Override
	public void updateCreditClass(final int ban, final String newCreditClass,
			final String memoText,String sessionId) throws ApplicationException{
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(ban);

				CreditClassChangeInfo amdocsCreditClassChangeInfo = new CreditClassChangeInfo();
				amdocsCreditClassChangeInfo.newCreditClass = AttributeTranslator.byteFromString(newCreditClass);
				amdocsCreditClassChangeInfo.memoText = AttributeTranslator.emptyFromNull(memoText);

				updateBanConv.changeCreditClass(amdocsCreditClassChangeInfo);
				return null;
			}
		});		


	}

	@Override
	public CreditCheckResultInfo retrieveAmdocsCreditCheckResult(final int ban, String sessionId)
	throws ApplicationException {
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<CreditCheckResultInfo>() {

			@Override
			public CreditCheckResultInfo doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateBanConv amdocsUpdateBanConv = transactionContext.createBean(UpdateBanConv.class);	
				
				CreditCheckResultInfo telusCreditCheckResultInfo = new CreditCheckResultInfo();

				// Set BanPK (which also retrieves the BAN)
				amdocsUpdateBanConv.setBanPK(ban);

				// print input to this method
				LOGGER.debug("banNumber:" + ban);

				char accType = (char)amdocsUpdateBanConv.getAccountTypeInfo().accountType;
				char accSubType = (char)amdocsUpdateBanConv.getAccountTypeInfo().accountSubType;
				LOGGER.debug("accType:" + accType);
				LOGGER.debug("accSubType:" + accSubType);

				CreditResultInfo amdocsCreditResultInfo = amdocsUpdateBanConv.getCreditEvalDetails();
				telusCreditCheckResultInfo = (CreditCheckResultInfo) AmdocsTelusMapping.mapAmdocsToTelus (amdocsCreditResultInfo, telusCreditCheckResultInfo);

				//Retrieve deposit information
				CreditResultDeposit[] deposits = amdocsCreditResultInfo.creditResultDeposit;
				if (deposits != null) {
					ArrayList<CreditCheckResultDepositInfo> depositList = new ArrayList<CreditCheckResultDepositInfo>();
					for (int i = 0; i < deposits.length; i++) {
						if (deposits[i] != null) {
							CreditCheckResultDepositInfo ccDeposit = new CreditCheckResultDepositInfo();
							ccDeposit.setDeposit(deposits[i].depositAmt);
							ccDeposit.setProductType(AttributeTranslator.stringFrombyte(deposits[i].depositArrProductType));
							depositList.add(ccDeposit);
						}
					}
					telusCreditCheckResultInfo.setDeposits((CreditCheckResultDepositInfo[]) depositList.toArray(new CreditCheckResultDepositInfo[depositList.size()]));
				}

				// Retrieve address used for the last credit check
				amdocs.APILink.datatypes.AddressInfo amdocsAddressInfo = amdocsUpdateBanConv.getCreditAddress();
				com.telus.eas.account.info.AddressInfo telusAddressInfo = new com.telus.eas.account.info.AddressInfo();
				telusAddressInfo = (com.telus.eas.account.info.AddressInfo) AmdocsTelusMapping.mapAmdocsToTelus (amdocsAddressInfo, telusAddressInfo);
				telusCreditCheckResultInfo.setLastCreditCheckAddress(telusAddressInfo);

				// Retrieve name used for the last credit check
				NameInfo amdocsNameInfo = amdocsUpdateBanConv.getCreditName();
				ConsumerNameInfo telusNameInfo = new ConsumerNameInfo();
				telusNameInfo = (ConsumerNameInfo) AmdocsTelusMapping.mapAmdocsToTelus (amdocsNameInfo, telusNameInfo);
				telusCreditCheckResultInfo.setLastCreditCheckName(telusNameInfo);					
				// Retrieve business info
				if ((accType == AccountSummary.ACCOUNT_TYPE_BUSINESS || accType == AccountSummary.ACCOUNT_TYPE_CORPORATE)&& 
						(accSubType != AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL || accSubType != AccountSummary.ACCOUNT_SUBTYPE_PCS_REGIONAL)) {							
					if (amdocsCreditResultInfo.creditResultExt1 != null) {
						ArrayList<BusinessCreditIdentityInfo> businessList = new ArrayList<BusinessCreditIdentityInfo>();
						for (int i = 0; i < amdocsCreditResultInfo.creditResultExt1.length; i++) {
							if (amdocsCreditResultInfo.creditResultExt1[i] != null) {
								BusinessCreditIdentityInfo business = new BusinessCreditIdentityInfo();
								business.setCompanyName(amdocsCreditResultInfo.creditResultExt1[i].companyName);
								business.setMarketAccount(amdocsCreditResultInfo.creditResultExt1[i].marketAccount);
								businessList.add(business);
							}
						}

						telusCreditCheckResultInfo.setLastCreditCheckListOfBusiness((BusinessCreditIdentityInfo[]) businessList.toArray(new BusinessCreditIdentityInfo[businessList.size()]));
					}
				}
				return telusCreditCheckResultInfo;
			}

		});
	}

}
