package com.telus.cmb.jws.mapping.customer_management_common_50;

import weblogic.wsee.util.StringUtil;

import com.telus.api.TelusAPIException;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.BankAccountInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.ChequeInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.Address;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.BankAccount;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.BusinessCreditIdentity;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.BusinessCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.Cheque;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.ConsumerName;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.CreditCard;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.CreditCheckResult;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.CreditCheckResultDeposit;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.CreditDecision;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.NameFormat;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.PaymentMethod;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.PersonalCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.ReferToCreditAnalyst;

public class CustomerManagementCommonMapper {
	public static AddressMapper AddressMapper() {
		return AddressMapper.getInstance();
	}
	
	public static BankAccountMapper BankAccountMapper() {
		return BankAccountMapper.getInstance();
	}
	
	public static BusinessCreditIdentityMapper BusinessCreditIdentityMapper() {
		return BusinessCreditIdentityMapper.getInstance();
	}
	
	public static BusinessCreditInformationMapper BusinessCreditInformationMapper() {
		return BusinessCreditInformationMapper.getInstance();
	}
	
	public static ChequeMapper ChequeMapper() {
		return ChequeMapper.getInstance();
	}
	
	public static ConsumerNameMapper ConsumerNameMapper() {
		return ConsumerNameMapper.getInstance();
	}
	
	public static CreditCardMapper CreditCardMapper() {
		return CreditCardMapper.getInstance();
	}
	
	public static CreditCheckResultMapper CreditCheckResultMapper() {
		return CreditCheckResultMapper.getInstance();
	}
	public static PaymentMethodMapper PaymentMethodMapper() {
		return PaymentMethodMapper.getInstance();
	}
	
	public static PersonalCreditInformationMapper PersonalCreditInformationMapper() {
		return PersonalCreditInformationMapper.getInstance();
	}
	
	public static ReferToCreditAnalystMapper ReferToCreditAnalystMapper() {
		return ReferToCreditAnalystMapper.getInstance();
	}
	
	public static class AddressMapper extends AbstractSchemaMapper<Address, AddressInfo> {
		private static AddressMapper INSTANCE = null;
		
		private AddressMapper(){
			super(Address.class, AddressInfo.class);
		}
		
		private static synchronized AddressMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new AddressMapper();
			}
			
			return INSTANCE;
		}

		@Override
		protected AddressInfo performDomainMapping(Address source, AddressInfo target) {
			if (source.getAddressType() != null) {
				target.setAddressType(source.getAddressType().value());
			}
			target.setAttention(source.getAttention());
			target.setPrimaryLine(source.getPrimaryLine());
			target.setSecondaryLine(source.getSecondaryLine());
			target.setCity(source.getCity());
			if (source.getProvince() != null) {
				target.setProvince(source.getProvince().value());
				target.setProvince0(source.getProvince().value());
			}
			target.setPostalCode(source.getPostalCode());
			target.setCountry(source.getCountry());
			target.setCountry0(source.getCountry());
			target.setStreetDirection(source.getStreetDirection());
			target.setStreetName(source.getStreetName());
			target.setStreetType(source.getStreetType());
			target.setUnit(source.getUnit());
			target.setUnitType(source.getUnitType());
			target.setPoBox(source.getPoBox());
			target.setRuralLocation(source.getRuralLocation());
			target.setRuralQualifier(source.getRuralQualifier());
			target.setRuralSite(source.getRuralSite());
			target.setRuralCompartment(source.getRuralCompartment());
			target.setRuralDeliveryType(source.getRuralDeliveryType());
			target.setRuralGroup(source.getRuralGroup());
			target.setRuralNumber(source.getRuralNumber());
			target.setRuralType(source.getRuralType());
			target.setZipGeoCode(source.getZipGeoCode());
			target.setForeignState(source.getForeignState());
			
			if((!StringUtil.isEmpty(source.getCivicNo()) && !StringUtil.isEmpty(source.getStreetNumber()))||
					(!StringUtil.isEmpty(source.getCivicNo()) && StringUtil.isEmpty(source.getStreetNumber()))){
				target.setCivicNo(source.getCivicNo());
			} else if (StringUtil.isEmpty(source.getCivicNo()) && !StringUtil.isEmpty(source.getStreetNumber())){
				target.setCivicNo(source.getStreetNumber());
			} else {
				target.setCivicNo(null);
			}
					
			if((!StringUtil.isEmpty(source.getCivicNoSuffix()) && !StringUtil.isEmpty(source.getStreetNumberSuffix()))||
					(!StringUtil.isEmpty(source.getCivicNoSuffix()) && StringUtil.isEmpty(source.getStreetNumberSuffix()))){
				target.setCivicNoSuffix(source.getCivicNoSuffix());
			} else if (StringUtil.isEmpty(source.getCivicNoSuffix()) && !StringUtil.isEmpty(source.getStreetNumberSuffix())){
				target.setCivicNoSuffix(source.getStreetNumberSuffix());
			} else {
				target.setCivicNoSuffix(null);
			}
			return super.performDomainMapping(source, target);
		}
		
	}
	
	public static class BankAccountMapper extends AbstractSchemaMapper<BankAccount, BankAccountInfo> {
		private static BankAccountMapper INSTANCE = null;
		
		private BankAccountMapper(){
			super(BankAccount.class, BankAccountInfo.class);
		}

		private static synchronized BankAccountMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new BankAccountMapper();
			}
			
			return INSTANCE;
		}
		
		@Override
		protected BankAccountInfo performDomainMapping(BankAccount source, BankAccountInfo target) {
			target.setBankAccountHolder(source.getBankAccountHolder());
			target.setBankAccountNumber(source.getBankAccountNumber());
			target.setBankAccountType(source.getBankAccountType());
			target.setBankBranchNumber(source.getBankBranchNumber());
			target.setBankCode(source.getBankCode());

			return super.performDomainMapping(source, target);
		}
	}
	
	public static class BusinessCreditIdentityMapper extends AbstractSchemaMapper<BusinessCreditIdentity, BusinessCreditIdentityInfo> {
		private static BusinessCreditIdentityMapper INSTANCE = null;
			
		private BusinessCreditIdentityMapper() {
			super(BusinessCreditIdentity.class, BusinessCreditIdentityInfo.class);
		}
		
		private static synchronized BusinessCreditIdentityMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new BusinessCreditIdentityMapper();
			}
			
			return INSTANCE;
		}
		
		@Override
		protected BusinessCreditIdentityInfo performDomainMapping(BusinessCreditIdentity source, BusinessCreditIdentityInfo target) {

				target.setCompanyName(source.getCompanyName());
				target.setMarketAccount(source.getMarketAccount());
		      
			return super.performDomainMapping(source, target);
		}

		@Override
		protected BusinessCreditIdentity performSchemaMapping(BusinessCreditIdentityInfo source, BusinessCreditIdentity target) {
			
				target.setCompanyName(source.getCompanyName());
				target.setMarketAccount(source.getMarketAccount());
			
			return super.performSchemaMapping(source, target);
		}
	}
	
	public static class BusinessCreditInformationMapper extends AbstractSchemaMapper<BusinessCreditInformation, BusinessCreditInfo> {

		private static BusinessCreditInformationMapper INSTANCE = null;
			
		private BusinessCreditInformationMapper(){
			super(BusinessCreditInformation.class, BusinessCreditInfo.class);
		}

		private static synchronized BusinessCreditInformationMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new BusinessCreditInformationMapper();
			}
			
			return INSTANCE;
		}

		

		@Override
		protected BusinessCreditInfo performDomainMapping(BusinessCreditInformation source, BusinessCreditInfo target) {
	        target.setIncorporationDate(source.getIncorporationDate());
	        target.setIncorporationNumber(source.getIncorporationNumber());
			
			return super.performDomainMapping(source, target);
		}
	}
	
	
	public static class ChequeMapper extends AbstractSchemaMapper<Cheque, ChequeInfo> {
		private static ChequeMapper INSTANCE = null;

		private ChequeMapper(){
			super(Cheque.class, ChequeInfo.class);
		}
		
		private static synchronized ChequeMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ChequeMapper();
			}
			
			return INSTANCE;
		}

		@Override
		protected ChequeInfo performDomainMapping(Cheque source, ChequeInfo target) {

			BankAccountInfo bankAccountInfo = new BankAccountMapper().mapToDomain(source.getBankAccount());
			if (bankAccountInfo != null)
				target.setBankAccount(bankAccountInfo);
			if (source.getNumber() != null)
				target.setChequeNumber(source.getNumber());

			return super.performDomainMapping(source, target);
		}

	}

	public static class ConsumerNameMapper extends AbstractSchemaMapper<ConsumerName, ConsumerNameInfo> {
		private static ConsumerNameMapper INSTANCE = null;
		
		private ConsumerNameMapper(){
			super(ConsumerName.class, ConsumerNameInfo.class);
		}
		
		public synchronized static ConsumerNameMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ConsumerNameMapper();
			}
			
			return INSTANCE;
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected ConsumerName performSchemaMapping(ConsumerNameInfo source, ConsumerName target) {
			if (source != null) {
				target.setAdditionalLine(source.getAdditionalLine());
				target.setFirstName(source.getFirstName());
				if (target.getFirstName()==null) target.setFirstName("");
				target.setGeneration(source.getGeneration());
				target.setLastName(source.getLastName());
				if (target.getLastName()==null) target.setLastName("");
				target.setMiddleInitial(source.getMiddleInitial());
				target.setNameFormat(toEnum(source.getNameFormat(), NameFormat.class));
				target.setTitle(source.getTitle());
			}
			return super.performSchemaMapping(source, target);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected ConsumerNameInfo performDomainMapping(ConsumerName source, ConsumerNameInfo target) {
			target.setAdditionalLine(source.getAdditionalLine());
			target.setFirstName(source.getFirstName());
			target.setGeneration(source.getGeneration());
			target.setLastName(source.getLastName());
			target.setMiddleInitial(source.getMiddleInitial());
			if (source.getNameFormat() != null) {
				target.setNameFormat(source.getNameFormat().value());
			}
			target.setTitle(source.getTitle());
			return super.performDomainMapping(source, target);
		}
	}
	
	
	public static class CreditCardMapper extends AbstractSchemaMapper<CreditCard, CreditCardInfo> {
		private static CreditCardMapper INSTANCE = null;
		
		private CreditCardMapper(){
			super(CreditCard.class, CreditCardInfo.class);
		}

		public synchronized static CreditCardMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditCardMapper();
			}
			
			return INSTANCE;
		}
			
		@Override
		protected CreditCardInfo performDomainMapping(CreditCard source, CreditCardInfo target) {
			if (source == null) {
				return null;
			}
			target.setExpiryMonth(source.getExpiryMonth());
			target.setExpiryYear(source.getExpiryYear());
			target.setHolderName(source.getHolderName());
			target.setToken(source.getToken());
			target.setLeadingDisplayDigits(source.getFirst6());
			target.setTrailingDisplayDigits(source.getLast4());
			target.setCardVerificationData(source.getCardVerificationData());
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class CreditCheckResultMapper extends AbstractSchemaMapper<CreditCheckResult, CreditCheckResultInfo> {
		private static CreditCheckResultMapper INSTANCE = null;
		
		private CreditCheckResultMapper(){
			super(CreditCheckResult.class, CreditCheckResultInfo.class);
		}

		public synchronized static CreditCheckResultMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditCheckResultMapper();
			}
			
			return INSTANCE;
		}
		
		@Override
		protected CreditCheckResultInfo performDomainMapping(CreditCheckResult source, CreditCheckResultInfo target) {

		      target.setCreditClass(source.getCreditClass());
		      target.setCreditScore(source.getCreditScore());
		      target.setDeposits(mapCreditCheckDeposits(source.getDepositList().toArray(new CreditCheckResultDeposit[0])));
		      target.setLimit(source.getCreditLimit());
		      target.setMessage(source.getCreditDecision().getCreditDecisionMessage());
		      target.setMessageFrench(source.getCreditDecision().getCreditDecisionMessageFrench());
		      target.setReferToCreditAnalyst(source.getReferToCreditAnalyst().isReferToCreditAnalystInd());
		      if(source.getReferToCreditAnalyst()!=null){
			      target.getReferToCreditAnalyst().setReferToCreditAnalyst(source.getReferToCreditAnalyst().isReferToCreditAnalystInd());
			      target.getReferToCreditAnalyst().setReasonCode(source.getReferToCreditAnalyst().getReasonCode());
			      target.getReferToCreditAnalyst().setReasonMessage(source.getReferToCreditAnalyst().getReasonMessage());
		     }
		      
			return super.performDomainMapping(source, target);
		}

		private CreditCheckResultDepositInfo[] mapCreditCheckDeposits(CreditCheckResultDeposit[] deposits) {
			CreditCheckResultDepositInfo[] depositInfos = null;
			if (deposits != null && deposits.length > 0) {
				depositInfos = new CreditCheckResultDepositInfo[deposits.length];
				for (int i = 0; i < deposits.length; i++) {
					CreditCheckResultDepositInfo depositInfo = new CreditCheckResultDepositInfo();
					depositInfo.setDeposit(deposits[i].getDepositAmount());
					depositInfo.setProductType(deposits[i].getProductType());
					depositInfos[i] = depositInfo;
				}
			}

			return depositInfos;
		}
		
		@Override
		protected CreditCheckResult performSchemaMapping(CreditCheckResultInfo source, CreditCheckResult target) {
			target.setCreditClass(source.getCreditClass());
			target.setCreditScore(source.getCreditScore());
			CreditCheckResultDepositInfo[] creditCheckDeposits = (CreditCheckResultDepositInfo[]) source.getDeposits();
			if (creditCheckDeposits != null) {
				for (int i = 0; i < creditCheckDeposits.length; i++) {
					CreditCheckResultDeposit deposit = new CreditCheckResultDeposit();
					deposit.setDepositAmount(creditCheckDeposits[i].getDeposit());
					deposit.setProductType(creditCheckDeposits[i].getProductType());
					target.getDepositList().add(deposit);
				}
			}
			
			target.setCreditLimit(source.getLimit());
			if(source.getMessage()!=null || source.getMessageFrench()!=null){
				CreditDecision creditDecision= new CreditDecision();
				creditDecision.setCreditDecisionMessage(source.getMessage());
				creditDecision.setCreditDecisionMessageFrench(source.getMessageFrench());
				target.setCreditDecision(creditDecision);
			}
			
			target.setReferToCreditAnalyst(new ReferToCreditAnalystMapper().mapToSchema(source.getReferToCreditAnalyst()));
			if(source.getBureauFile()!=null)
				target.setBureauFile(source.getBureauFile().getBytes());

			return super.performSchemaMapping(source, target);
		}
	}
	
	public static class PaymentMethodMapper extends AbstractSchemaMapper<PaymentMethod, PaymentMethodInfo> {
		private static PaymentMethodMapper INSTANCE = null;
		
		private PaymentMethodMapper(){
			super(PaymentMethod.class, PaymentMethodInfo.class);
		}

		public synchronized static PaymentMethodMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new PaymentMethodMapper();
			}
			
			return INSTANCE;
		}
		
		@Override
		protected PaymentMethodInfo performDomainMapping(PaymentMethod source, PaymentMethodInfo target) {
			
			ChequeInfo chequeInfo = new ChequeMapper().mapToDomain(source.getCheque());
			target.setCheque0(chequeInfo);
			
			CreditCardInfo creditCardInfo = new CreditCardMapper().mapToDomain(source.getCreditCard());
			target.setCreditCard0(creditCardInfo);
			
			if (source.getEndDate() != null)
				target.setEndDate(source.getEndDate());
			target.setPaymentMethod(source.getPaymentMethod());
			if (source.getStartDate() != null)
				target.setStartDate(source.getStartDate());
			target.setStatus(source.getStatus());
			target.setStatusReason(source.getStatusReason());
			if (source.isSuppressReturnEnvelopeInd() != null)
				target.setSuppressReturnEnvelope(source.isSuppressReturnEnvelopeInd());

			return super.performDomainMapping(source, target);
		}
	}
	
	public static class PersonalCreditInformationMapper extends AbstractSchemaMapper<PersonalCreditInformation, PersonalCreditInfo> {
		
		private static PersonalCreditInformationMapper INSTANCE = null;
		
		private PersonalCreditInformationMapper(){
			super(PersonalCreditInformation.class, PersonalCreditInfo.class);
		}
		
		private static synchronized PersonalCreditInformationMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new PersonalCreditInformationMapper();
			}
			return INSTANCE;
		}
		
		@Override
		protected PersonalCreditInfo performDomainMapping(PersonalCreditInformation source, PersonalCreditInfo target) {
			
			
			if (source.getCreditCard() != null) {
				String token = source.getCreditCard().getToken();
				String first6 = source.getCreditCard().getFirst6();
				String last4 = source.getCreditCard().getLast4();
				if (token != null && !token.equals("")  &&
					first6 != null && !first6.equals("")  &&	
					last4 != null && !last4.equals("")  ) {
				
					try {
						target.getCreditCard().setToken(token,first6 , last4);
					} catch (TelusAPIException e) {
						e.printStackTrace();
					}
				}
				target.getCreditCard().setExpiryMonth(source.getCreditCard().getExpiryMonth());
				target.getCreditCard().setExpiryYear(source.getCreditCard().getExpiryYear());
				if(source.getCreditCard().getCardVerificationData()!=null)
					target.getCreditCard().setCardVerificationData(source.getCreditCard().getCardVerificationData());
				if(source.getCreditCard().getHolderName()!=null)
					target.getCreditCard().setHolderName(source.getCreditCard().getHolderName());
			}
			
			if (source.getBirthDate() != null)
				target.setBirthDate(source.getBirthDate());
			target.setSin(source.getSin());
			target.setDriversLicense(source.getDriversLicense());
			if (source.getDriversLicenseExpiry() != null)
				target.setDriversLicenseExpiry(source.getDriversLicenseExpiry());
			if (source.getDriversLicenseProvince() != null)
				target.setDriversLicenseProvince(source.getDriversLicenseProvince().value());
					
			return super.performDomainMapping(source, target);
		}
	}

	
	public static class ReferToCreditAnalystMapper extends AbstractSchemaMapper<ReferToCreditAnalyst, com.telus.eas.account.info.ReferToCreditAnalystInfo> {
		private static ReferToCreditAnalystMapper INSTANCE = null;
		
		private ReferToCreditAnalystMapper(){
			super(ReferToCreditAnalyst.class, com.telus.eas.account.info.ReferToCreditAnalystInfo.class);
		}
		
		private static synchronized ReferToCreditAnalystMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ReferToCreditAnalystMapper();
			}
			return INSTANCE;
		}
		
		@Override
		protected ReferToCreditAnalyst performSchemaMapping(com.telus.eas.account.info.ReferToCreditAnalystInfo  source, ReferToCreditAnalyst target) {
			
			target.setReferToCreditAnalystInd(source.isReferToCreditAnalyst());
			target.setReasonCode(source.getReasonCode());
			target.setReasonMessage(source.getReasonMessage());
			
			return super.performSchemaMapping(source, target);
		}

		
		
	}

}
