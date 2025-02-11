package com.telus.cmb.jws.mapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.jws.mapping.enterprisecommontypes_v9.CreditCardMapper;
import com.telus.eas.account.info.CreditCardHolderInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.cmb.jws.CreditCardHolder;
import com.telus.cmb.jws.CreditCardTransaction;


public class WirelessPaymentMapper {

	public static CreditCardHolderMapper getCreditCardHolderMapper() {
		return CreditCardHolderMapper.getInstance();
	}

	public static CreditCardTransactionMapper getCreditCardTransactionMapper() {
		return CreditCardTransactionMapper.getInstance();
	}
	
	public static CreditCardMapper getCreditCardMapper() {
		return CreditCardMapper.getInstance();
	}

	public static class CreditCardHolderMapper extends AbstractSchemaMapper<CreditCardHolder, CreditCardHolderInfo> {

		private static CreditCardHolderMapper INSTANCE;

		private CreditCardHolderMapper() {
			super(CreditCardHolder.class, CreditCardHolderInfo.class);
		}

		public static CreditCardHolderMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditCardHolderMapper();
			}
			return INSTANCE;
		}

		@Override
		public CreditCardHolderInfo performDomainMapping(CreditCardHolder source, CreditCardHolderInfo target) {

			if (source == null) {
				return null;
			}

			if (source.getAccountSubType() != null) {
				target.setAccountSubType(source.getAccountSubType());
			}
			if (source.getAccountType() != null) {
				target.setAccountType(source.getAccountType().value());
			}
			if (source.getActivationDate() != null) {
				target.setActivationDate(source.getActivationDate());
			}
			if (source.getApartmentNumber() != null) {
				target.setApartmentNumber(source.getApartmentNumber());
			}
			if (source.getBirthDate() != null) {
				target.setBirthDate(source.getBirthDate());
			}
			if (source.getBusinessRole() != null) {
				target.setBusinessRole(source.getBusinessRole());
			}
			if (source.getCity() != null) {
				target.setCity(source.getCity());
			}
			if (source.getCivicStreetNumber() != null) {
				target.setCivicStreetNumber(source.getCivicStreetNumber());
			}
			if (source.getClientId() != null) {
				target.setClientID(source.getClientId());
			}
			if (source.getFirstName() != null) {
				target.setFirstName(source.getFirstName());
			}
			if (source.getHomePhone() != null) {
				target.setHomePhone(source.getHomePhone());
			}
			if (source.getLastName() != null) {
				target.setlastName(source.getLastName());
			}
			if (source.getPostalCode() != null) {
				target.setPostalCode(source.getPostalCode());
			}
			if (source.getProvince() != null) {
				target.setProvince(source.getProvince().value());
			}
			if (source.getStreetName() != null) {
				target.setStreetName(source.getStreetName());
			}

			return super.performDomainMapping(source, target);
		}

	}

	public static class CreditCardTransactionMapper extends AbstractSchemaMapper<CreditCardTransaction, CreditCardTransactionInfo> {

		private static CreditCardTransactionMapper INSTANCE;

		private CreditCardTransactionMapper() {
			super(CreditCardTransaction.class, CreditCardTransactionInfo.class);
		}

		public static CreditCardTransactionMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditCardTransactionMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CreditCardTransactionInfo performDomainMapping(CreditCardTransaction source, CreditCardTransactionInfo target) {

			target.setAmount(Double.parseDouble(source.getAmount()));
			target.setBan(Integer.parseInt(source.getBanId()));
			target.setBrandId(source.getBrandId());
			target.setChargeAuthorizationNumber(source.getChargeAuthorizationNumber());
			target.setCreditCardHolderInfo(getCreditCardHolderMapper().mapToDomain(source.getCreditCardHolder()));
			target.setCreditCardInfo(getCreditCardMapper().mapToDomain(source.getCreditCard()));
			target.setTransactionType(source.getTransactionType());

			return super.performDomainMapping(source, target);
		}

	}

}
