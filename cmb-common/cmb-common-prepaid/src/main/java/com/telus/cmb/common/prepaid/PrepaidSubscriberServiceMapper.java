package com.telus.cmb.common.prepaid;

import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.telus.eas.subscriber.info.PrepaidSubscriberInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.base_types_2_0.DialectType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.base_types_2_0.Individual;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.base_types_2_0.Language;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.base_types_2_0.LanguageAbility;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscribertypes_v4.RateProfile;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscribertypes_v4.RechargeableRealTimeBalance;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscribertypes_v4.Subscriber;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscribertypes_v4.Subscription;

public class PrepaidSubscriberServiceMapper {

	private static final String RATE_TYE = "local";
	private static final String  BALANCE_TYPE = "primary";

	public static Subscriber mapupdateSubscriberRateIdToPrepaidSchema(
			String phoneNumber, String userId, long rateId) {
		Subscriber subscriber = new Subscriber();
		Subscription subscription = new Subscription();
		RechargeableRealTimeBalance rechargeableRealTimeBalance = new RechargeableRealTimeBalance();
		RateProfile rateProfile = new RateProfile();
		rateProfile.setRateType(RATE_TYE);
		rateProfile.setRateID(BigInteger.valueOf(rateId));
		rechargeableRealTimeBalance.getRateProfiles().add(rateProfile);
		subscription.getBalances().add(rechargeableRealTimeBalance);
		subscription.setId(phoneNumber);
		subscriber.setSubscription(subscription);
		return subscriber;

	}
	
	
	public static Subscriber mapupdateSubscriberExpiryDateToPrepaidSchema(
			String phoneNumber, String userId, Date expiryDate)  {
		Subscriber subscriber = new Subscriber();
		Subscription subscription = new Subscription();
		RechargeableRealTimeBalance rechargeableRealTimeBalance = new RechargeableRealTimeBalance();
		rechargeableRealTimeBalance.setBalanceType(BALANCE_TYPE);
		rechargeableRealTimeBalance.setExpiryDate(expiryDate);
		subscription.getBalances().add(rechargeableRealTimeBalance);
		subscription.setId(phoneNumber);
		subscriber.setSubscription(subscription);
		return subscriber;
	}
	
	public static Subscriber mapupdateSubscriberLanguageToPrepaidSchema(
			String banId, String MDN,String serialNo, String prevLanguage, String language, String user) {
		Subscriber subscriber = new Subscriber();
		Individual individual = new Individual();
		LanguageAbility languageAbility = new LanguageAbility();
		languageAbility.setListeningProficiency(true);
		Language schemaLanguage= new Language();
		schemaLanguage.setDialectName(DialectType.fromValue(language));
		languageAbility.setLanguage(schemaLanguage);
		individual.getLanguageAbilities().add(languageAbility);
		subscriber.setIndividual(individual);
		Subscription subscription = new Subscription();
		subscription.setId(MDN);
		subscriber.setSubscription(subscription);
		return subscriber;
	}
	
	public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
		try
		{
		GregorianCalendar gCalendar = new GregorianCalendar();
		gCalendar.setTime(date);
		XMLGregorianCalendar xmlCalendar = null;
		xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
		return xmlCalendar;
		}
		catch (Exception e) {
			return null;
		}
	}

public static  Subscriber mapUpdateSubscriberToPrepaidSchema(PrepaidSubscriberInfo prepaidSubscriberInfo){
	
	Subscriber subscriber = new Subscriber();
	RechargeableRealTimeBalance rechargeableRealTimeBalance = new RechargeableRealTimeBalance();
	Subscription subscription = new Subscription();
	RateProfile rateProfile = new RateProfile();
	
	subscription.setId(prepaidSubscriberInfo.getPhoneNumber());
	
	// mapping the language
	
		if (prepaidSubscriberInfo.getLanguage() != null) {
			Individual individual = new Individual();
			LanguageAbility languageAbility = new LanguageAbility();
			Language schemaLanguage = new Language();
			languageAbility.setListeningProficiency(true);
			schemaLanguage.setDialectName(DialectType.fromValue(prepaidSubscriberInfo.getLanguage().toLowerCase()));
			languageAbility.setLanguage(schemaLanguage);
			individual.getLanguageAbilities().add(languageAbility);
			subscriber.setSubscription(subscription);
			subscriber.setIndividual(individual);
		}
	
	//map Expiry date 
		
		if (prepaidSubscriberInfo.getExpiryDate() != null) {
			rechargeableRealTimeBalance.setBalanceType(BALANCE_TYPE);
			rechargeableRealTimeBalance.setExpiryDate(prepaidSubscriberInfo.getExpiryDate());
			subscription.getBalances().add(rechargeableRealTimeBalance);
			subscriber.setSubscription(subscription);
		}
	//map rate id
	
		if (prepaidSubscriberInfo.getRateId() > 0) {
			rateProfile.setRateType(RATE_TYE);
			rateProfile.setRateID(BigInteger.valueOf(prepaidSubscriberInfo.getRateId()));
			rechargeableRealTimeBalance.getRateProfiles().add(rateProfile);
			subscription.getBalances().add(rechargeableRealTimeBalance);
			subscriber.setSubscription(subscription);
		}
		return subscriber;
	}

}
