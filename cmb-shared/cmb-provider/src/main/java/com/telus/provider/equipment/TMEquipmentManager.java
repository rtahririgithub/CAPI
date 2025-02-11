/*
 * $Id$
 * %E% %W%
 * Copyright (c) TELUS Inc. All Rights Reserved.
 */

package com.telus.provider.equipment;

import com.telus.api.InvalidPINException;
import com.telus.api.TelusAPIException;
import com.telus.api.TooManyAttemptsException;
import com.telus.api.account.InvalidSerialNumberException;
import com.telus.api.account.SerialNumberInUseException;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownPagerException;
import com.telus.api.account.UnknownPhoneNumberException;
import com.telus.api.account.UnknownSerialNumberException;
import com.telus.api.account.UnknownSerialNumberPrefixException;
import com.telus.api.equipment.Card;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentManager;
import com.telus.api.equipment.ExistingPagerEquipmentException;
import com.telus.api.equipment.InvalidPagerEquipmentException;
import com.telus.api.equipment.MasterLockException;
import com.telus.api.equipment.PagerEquipment;
import com.telus.api.equipment.USIMCardEquipment;
import com.telus.api.reference.Brand;
import com.telus.api.reference.Province;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.util.AppConfiguration;
import com.telus.provider.util.Logger;
import com.telus.provider.util.ProviderEquipmentExceptionTranslator;

public class TMEquipmentManager extends BaseProvider implements EquipmentManager {

	private static final long serialVersionUID = 1L;

	public TMEquipmentManager(TMProvider provider) {
		super(provider);
	}

	public Equipment getEquipment(String serialNumber) throws UnknownSerialNumberException, TelusAPIException {
		try {
			 return decorate(provider.getProductEquipmentHelper().getEquipmentInfobySerialNo(serialNumber));
		}catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderEquipmentExceptionTranslator(serialNumber);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
		return null;
	}

	public Equipment getEquipment(String serialNumber,boolean checkPseudoESN) throws UnknownSerialNumberException, TelusAPIException {
		return getEquipments(serialNumber, checkPseudoESN)[0];
	}

	public Equipment[] getEquipments(String serialNumber, boolean checkPseudoESN) throws UnknownSerialNumberException, TelusAPIException {
		try {
			 return decorate(provider.getProductEquipmentHelper().getEquipmentInfobySerialNo(serialNumber, checkPseudoESN));
		}catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderEquipmentExceptionTranslator(serialNumber);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
		return null;
	}

	public Equipment getEquipmentByCapCode(String capCode, String encodingFormat) throws UnknownSerialNumberException, TelusAPIException {
		try {
			return decorate(provider.getProductEquipmentHelper().getEquipmentInfoByCapCode(capCode, encodingFormat));
		}catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderEquipmentExceptionTranslator(capCode);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
		return null;	
	}

	public Equipment getEquipmentByPhoneNumber(String phoneNumber) throws UnknownPhoneNumberException, TelusAPIException {

		try {
			 return decorate(provider.getProductEquipmentHelper().getEquipmentInfobyPhoneNo(phoneNumber));
		}catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderEquipmentExceptionTranslator(phoneNumber);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
		return null;
	}



	public Equipment validateSerialNumber(String serialNumber) throws TelusAPIException, SerialNumberInUseException,

	UnknownSerialNumberException {

		return validateSerialNumber(serialNumber, null, Brand.BRAND_ID_ALL );
	}

	public PagerEquipment getEquipment(String capCode, String coverageRegion) throws UnknownPagerException, TelusAPIException{
		return null;
	}

	public String getMasterLockbySerialNo(String pSerialNo, long pLockReasonID, String pUserID) throws MasterLockException, TelusAPIException {
		try {
			 return provider.getProductEquipmentManager().getMasterLockbySerialNo(pSerialNo, pUserID, pLockReasonID);
		}catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderEquipmentExceptionTranslator(pSerialNo);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
		return null;
	}

	public String getMasterLockbySerialNo(String pSerialNo, long pLockReasonID, long pOutletID, long pChnlOrgID, String pUserID) throws MasterLockException, TelusAPIException {
		try {
			 return provider.getProductEquipmentManager().getMasterLockbySerialNo(pSerialNo, pUserID, pLockReasonID, pOutletID, pChnlOrgID);
		}catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderEquipmentExceptionTranslator(pSerialNo);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
		return null;
	}

	public void addAnalogSerialNumber(String serialNumber) throws TelusAPIException, UnknownSerialNumberPrefixException,InvalidSerialNumberException {
		try {
			 provider.getProductEquipmentManager().insertAnalogEquipment(serialNumber, provider.getUser());
		}catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderEquipmentExceptionTranslator(serialNumber);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
	}

	public  void setSIMMule(String sim, String mule, java.util.Date activationDate, String eventType) throws TelusAPIException {
		try {
			provider.getProductEquipmentManager().setSIMMule(sim, mule, activationDate, eventType);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}

	}

	public void addPagerSerialNumber(String serialNumber, String capCode, String encodingFormat, String frequencyCode, String equipmentType, String userId) throws TelusAPIException, UnknownSerialNumberPrefixException {
		try {
			testAddPagerSerialNumber(serialNumber, capCode, encodingFormat, frequencyCode, equipmentType, userId);
			provider.getProductEquipmentManager().insertPagerEquipment(serialNumber, capCode, encodingFormat, frequencyCode, equipmentType, userId);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	public Card getCardBySerialNumber(String serialNumber) throws UnknownSerialNumberException, TelusAPIException{
		try {
			return decorate(provider.getProductEquipmentHelper().getCardBySerialNo(serialNumber));
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public Card getCardByCardNumber(String cardNumber, Subscriber subscriber) throws UnknownSerialNumberException, InvalidPINException, TooManyAttemptsException, TelusAPIException{
		try {
			return decorate(provider.getProductEquipmentHelper().getCardByFullCardNo(cardNumber, subscriber.getPhoneNumber(),
				subscriber.getEquipment().getSerialNumber(), provider.getUser()));
			// TODO: throw InvalidPINException & TooManyAttemptsException
		}catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderEquipmentExceptionTranslator(cardNumber);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
		return null;
	}

	public Card getAirCardByCardNumber(String cardNumber, Subscriber subscriber) throws UnknownSerialNumberException, InvalidPINException, TooManyAttemptsException, TelusAPIException{
		try {
			return decorate(provider.getProductEquipmentHelper().getAirCardByCardNo(cardNumber,	subscriber.getPhoneNumber(),
					subscriber.getEquipment().getSerialNumber(), provider.getUser()));
			// TODO: throw InvalidPINException & TooManyAttemptsException
		}catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderEquipmentExceptionTranslator(cardNumber);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
		return null;
	}

	public Card getAirCardByCardNumber(String cardNumber, String phoneNumber, String equipmentSerialNo) throws UnknownSerialNumberException, InvalidPINException, TooManyAttemptsException, TelusAPIException{  
		try {
			return decorate(provider.getProductEquipmentHelper().getAirCardByCardNo(cardNumber,	
					phoneNumber, equipmentSerialNo, provider.getUser()));
			// TODO: throw InvalidPINException & TooManyAttemptsException
		}catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderEquipmentExceptionTranslator(cardNumber);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
		return null;
	}

	public Card[] getCards(String phoneNumber) throws UnknownPhoneNumberException, TelusAPIException{
		try {
			return decorate(provider.getProductEquipmentHelper().getCards(phoneNumber));
		}catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderEquipmentExceptionTranslator(phoneNumber);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
		return null;
	}

	public Card[] getCards(String phoneNumber, String cardType) throws UnknownPhoneNumberException, TelusAPIException{
		try {
			return decorate(provider.getProductEquipmentHelper().getCards(phoneNumber, cardType));
		}catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderEquipmentExceptionTranslator(phoneNumber);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
		return null;
	}

	public double getBaseProductPrice(String serialNumber, String province, String npa) throws TelusAPIException {
		//      throw new AbstractMethodError("TODO");
		try {
			return provider.getProductEquipmentHelper().getBaseProductPrice(serialNumber, province, npa);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return 0;
	}


	public long getShippedToLocation(String serialNumber) throws TelusAPIException
	{
		try
		{
			return provider.getProductEquipmentHelper().getShippedToLocation(serialNumber);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return 0;
	}

	public Equipment getEquipmentbyProductCode(String pProductCode) throws TelusAPIException {

		try {
			return decorate(provider.getProductEquipmentHelper().getEquipmentInfobyProductCode(pProductCode));
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public void testAddPagerSerialNumber(String serialNumber, String capCode, String encodingFormat,
			String frequencyCode, String equipmentType, String userId) throws ExistingPagerEquipmentException, InvalidPagerEquipmentException, TelusAPIException {
		try {
			try {
				if (getEquipment(serialNumber) != null)
					throw new ExistingPagerEquipmentException(provider.getApplicationMessage(0), ExistingPagerEquipmentException.REASON_SERIAL_NUMBER_EXISTS);
			}
			catch(UnknownSerialNumberException e) {
				// do nothing - SN doesn't exists and can be added
			}

			try {
				if (getEquipmentByCapCode(capCode, encodingFormat) != null)
					throw new ExistingPagerEquipmentException(provider.getApplicationMessage(0), ExistingPagerEquipmentException.REASON_CAP_CODE_EXISTS);
			}
			catch(UnknownSerialNumberException e) {
				// do nothing - cap code doesn't exists and can be added
			}

			if (!Helper.isValidCapCodeFormat(encodingFormat, equipmentType, Helper.getFormattedCapCode(capCode, Province.PROVINCE_AB, encodingFormat, equipmentType)))
				throw new InvalidPagerEquipmentException(provider.getApplicationMessage(0), InvalidPagerEquipmentException.REASON_INVALID_CAP_CODE);
		}
		catch(TelusAPIException e) {
			throw e;
		}
		catch(Exception e) {
			throw new TelusAPIException(e);
		}
	}

	private TMEquipment [] decorate(EquipmentInfo [] info) {

		TMEquipment [] result = new TMEquipment [info.length];

		for (int idx = 0; idx < info.length; idx++) {
			result[idx] = decorate(info[idx]);
		}

		return result;
	}

	private TMEquipment decorate(EquipmentInfo info) {
		if (info.isIDEN()) {
			if (info.isMule()) {
				return new TMMuleEquipment(provider, info);
			} else if (info.isSIMCard()) {
				return new TMSIMCardEquipment(provider, info);
			} else {
				return new TMIDENEquipment(provider, info);
			}
		}
		else if (info.isUSIMCard()) {
			return new TMUSIMCardEquipment( provider, info );
		}
		else if (info.isAnalog()) {
			return new TMAnalogEquipment(provider, info);
		}
		else if (info.isPCSHandset()) {
			return new TMPCSEquipment(provider, info);
		}
		else if (info.is1xRTT()) {
			return new TMOneRTTEquipment(provider, info);
		}
		else if (info.isPager()) {
			return new TMPagerEquipment(provider, info);
		}
		else if (info.isRUIMCard()) {
			return new TMUIMCardEquipment(provider, info);
		}
		else if (info.isCellularDigital()) {
			return new TMCellularDigitalEquipment(provider, info);
		} else {
			return new TMEquipment(provider, info);
		}
	}

	private TMCard decorate(CardInfo info) {
		if (info.isGameCard()) {
			return new TMGameCard(provider, info);
		} else if (info.isMinuteCard()) {
			return new TMMinuteCard(provider, info);
		} else if (info.isFeatureCard()) {
			return new TMFeatureCard(provider, info);
		} else if (info.isAirtimeCard()) {
			return new TMAirtimeCard(provider, info);
		} else {
			return new TMCard(provider, info);
		}
	}

	private TMCard[] decorate(CardInfo[] info) {
		TMCard[] cards = new TMCard[info.length];
		for(int i=0; i < info.length; i++) {
			cards[i] = decorate(info[i]);
		}
		return cards;
	}
	public double getBaseProductPriceByProductCode(String productCode, String province, String npa) throws TelusAPIException{
		try {
			return provider.getProductEquipmentHelper().getBaseProductPriceByProductCode(productCode,province, npa);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return 0;
	}

	public Equipment validateSerialNumber(String serialNumber, int brandId) throws TelusAPIException, SerialNumberInUseException, UnknownSerialNumberException, InvalidSerialNumberException {
		return validateSerialNumber( serialNumber, null, brandId );
	}

	public Equipment validateSerialNumber(String serialNumber,long[] activationChannelOrgIds, int brandId) throws TelusAPIException, SerialNumberInUseException, UnknownSerialNumberException, InvalidSerialNumberException {
		Equipment e = getEquipment(serialNumber);
		if ( e.isHSPA() ) {
			if ( e.isUSIMCard() ) { 
				validateUSIMCard((USIMCardEquipment) e, brandId ,0);
			}
			else {
				// for non USIM HSPA equipment
				validateHSPASEquipment( e, activationChannelOrgIds, brandId );
			}
		}
		else {
			validateEquipment( e );
		}
		return e;
	}

	//this is the original logic in validateSerialNumber()
	private void validateEquipment( Equipment equipment ) throws SerialNumberInUseException , InvalidSerialNumberException {
		String serialNumber = equipment.getSerialNumber();
		if (equipment.isInUse()) {
			throw new SerialNumberInUseException("serialNumber (" + serialNumber + ") is in use by subscriber: " +
					equipment.getPhoneNumber(), serialNumber, InvalidSerialNumberException.EQUIPMENT_INUSE);
		} else if (equipment.isStolen()) {
			throw new SerialNumberInUseException("serialNumber (" + serialNumber + ") is stolen", serialNumber, InvalidSerialNumberException.EQUIPMENT_STOLEN);
		}
	}
	private void validateUSIMCard( USIMCardEquipment equipment, int brandId, long subscriptionId ) throws NumberFormatException, TelusAPIException {

		String serialNumber = equipment.getSerialNumber();

		if ( equipment.isInUse() ) 
			throw new SerialNumberInUseException("USIMCard (" + serialNumber + ") is in use by subscriber: " +
					equipment.getPhoneNumber(), serialNumber, InvalidSerialNumberException.EQUIPMENT_INUSE);

		if ( equipment.isStolen() )
			throw new SerialNumberInUseException("USIMCard (" + serialNumber + ") is stolen", serialNumber, InvalidSerialNumberException.EQUIPMENT_STOLEN );

		if ( equipment.isAssignable()==false )
			throw new InvalidSerialNumberException("USIMCard (" + serialNumber+") is not assignable", serialNumber, InvalidSerialNumberException.EQUIPMENT_NOT_ASSIGNABLE );

		if (brandId!=Brand.BRAND_ID_ALL) {
			validateEquipmentBrand(equipment, brandId);
		}

		if (equipment.isExpired()) {
			throw new InvalidSerialNumberException("USIMCard sn(" + serialNumber+") is expired.", 
					InvalidSerialNumberException.EQUIPMENT_ISEXPIRED);
		}
		if (equipment.isPreviouslyActivated()){
			if(subscriptionId >0){
				if (subscriptionId != Long.valueOf(equipment.getLastAssociatedSubscriptionId()).longValue()) {
					throw new SerialNumberInUseException("USIMCard sn(" + serialNumber +") is previously activated" +
							" with subscription id: " + equipment.getLastAssociatedSubscriptionId(),
							InvalidSerialNumberException.EQUIPMENT_ACTIVATED_ON_DIFF_SUB);
				}
			}
			else
				throw new SerialNumberInUseException("USIMCard sn(" + serialNumber +") is previously activated", 
						InvalidSerialNumberException.EQUIPMENT_ACTIVATED_ON_DIFF_SUB);
		}
	}

	private void validateHSPASEquipment( Equipment equipment, long[] activationChannelOrgIds, int brandId ) throws TelusAPIException {
		String serialNumber = equipment.getSerialNumber();

		if ( equipment.isStolen() )
			throw new SerialNumberInUseException("HSPA Handset (" + serialNumber + ") is stolen", serialNumber, InvalidSerialNumberException.EQUIPMENT_STOLEN );

		if ( activationChannelOrgIds!=null && activationChannelOrgIds.length>0 ) {
			long locationId = equipment.getShippedToLocation();
			boolean found=false;
			for( int i=0; i< activationChannelOrgIds.length; i++) {
				if ( locationId == activationChannelOrgIds[i] ) {
					found=true;
					break;
				}
			}
			if ( found==false) {
				throw new InvalidSerialNumberException("HSPA Handset (" + serialNumber+") shipToLocation not match channel id", serialNumber, InvalidSerialNumberException.CHANNEL_ID_NOT_MATCH );
			}
		}
		if (brandId!=Brand.BRAND_ID_ALL) {
			validateHandsetBrand(equipment, brandId);
		}
	}

	private void validateHandsetBrand(Equipment equipment, int brandId) throws UnknownSerialNumberException {
		int[] brands = equipment.getBrandIds();
		boolean isEquipTelusBrand = false;
		for (int i=0; i<brands.length; i++) {
			//Equipment brand matches account brand
			if (brands[i] == brandId) {return;}
			//Equipment supports TELUS brand
			if (brands[i] == Brand.BRAND_ID_TELUS) {isEquipTelusBrand = true;}
		}
		//Project Common Inventory: Koodo accounts can use TELUS brand equipment
		if (isEquipTelusBrand && brandId == Brand.BRAND_ID_KOODO) {return;}
		throw new UnknownSerialNumberException("equipment (" + equipment.getSerialNumber()+") not support brand(" + brandId +")", equipment.getSerialNumber(), InvalidSerialNumberException.EQUIPMENT_GREY_MARKET  );
	}
	
	private void validateEquipmentBrand(Equipment equipment, int brandId) throws UnknownSerialNumberException {
		int[] brands = equipment.getBrandIds();
		boolean found=false;
		for( int i=0; i<brands.length; i++ ) {
			if ( brands[i]==brandId ) {
				found=true;
				break;
			}
		}
		if ( found==false) {
			throw new UnknownSerialNumberException("equipment (" + equipment.getSerialNumber()+") not support brand(" + brandId +")", equipment.getSerialNumber(), InvalidSerialNumberException.EQUIPMENT_GREY_MARKET  );
		}
	}

	/**
	 * This method returns the proper equipment type to pass to ReferenceDataManager.
	 * It checks if the equipment passed in is HSPA (usually in the case of USIM equipment type), check
	 * 
	 * @param equipment
	 * @return
	 */
	public String translateEquipmentType(Equipment equipment) {
		if (equipment == null) return "";

		String equipmentType = String.valueOf(equipment.getEquipmentType());
		if (equipment.isHSPA() && Equipment.EQUIPMENT_TYPE_USIM.equals(equipmentType.trim())) {
			Equipment associatedHandset = null;
			try {
				associatedHandset = ((TMUSIMCardEquipment) equipment).getLastAssociatedHandset();
			}catch (TelusAPIException tae) {
				Logger.debug("getReferenceDataEquipmentType failed to retrieve last associated handset on equipment="+equipment);
			}

			equipmentType = (associatedHandset != null ? associatedHandset.getEquipmentType() : AppConfiguration.getDefaultHSPAEquipmentType());
		}

		return equipmentType;
	}

	public Equipment validateSerialNumber(String serialNumber, int brandId,
			long subscriptionId) throws TelusAPIException, SerialNumberInUseException, UnknownSerialNumberException, InvalidSerialNumberException {

		Equipment e = getEquipment(serialNumber);
		if (e.isUSIMCard() && subscriptionId > 0){ 
			validateUSIMCard((USIMCardEquipment)e, brandId, subscriptionId);
			return e; 
		}
		else
			return validateSerialNumber(serialNumber, brandId);

	}
}




