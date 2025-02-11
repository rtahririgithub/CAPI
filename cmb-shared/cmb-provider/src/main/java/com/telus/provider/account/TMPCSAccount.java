
package com.telus.provider.account;


import java.util.HashMap;

import com.telus.api.TelusAPIException;
import com.telus.api.account.AccountManager;
import com.telus.api.account.Address;
import com.telus.api.account.InvalidSerialNumberException;
import com.telus.api.account.PCSAccount;
import com.telus.api.account.PCSSubscriber;
import com.telus.api.account.PagerSubscriber;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.SerialNumberInUseException;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownSerialNumberException;
import com.telus.api.account.UnsupportedEquipmentException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.NumberGroup;
import com.telus.api.reference.SeatType;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.PCSSubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.equipment.TMEquipment;
//import com.telus.provider.util.Logger;


public class TMPCSAccount extends BaseProvider implements PCSAccount {

	private static final long serialVersionUID = 1L;
	/**
	 * @link aggregation
	 */
	private final PCSAccount delegate;
	private final TMAddress alternateCreditCheckAddress;
	private HashMap pcsSubscriberNetworkCounts = null;
	/**
	 * @link aggregation
	 */
	private final TMAccount account;

	
	public TMPCSAccount(TMProvider provider, PCSAccount delegate, TMAccount account) {
		super(provider);
		this.delegate = delegate;
		this.account = account;
		alternateCreditCheckAddress = new TMAddress(provider, ((AccountInfo)delegate).getAlternateCreditCheckAddress0());
	}

	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------
	public Address getAlternateCreditCheckAddress() {
		return alternateCreditCheckAddress;
	}

	public PagerSubscriber newPagerSubscriber(String serialNumber) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
//		return account.newPagerSubscriber(AccountManager.PRODUCT_TYPE_PAGER, serialNumber, false, null);
		throw new UnsupportedOperationException("Decommissioned method");
	}

	public PagerSubscriber newPagerSubscriber(String serialNumber, String activationFeeChargeCode) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
//		return account.newPagerSubscriber(AccountManager.PRODUCT_TYPE_PAGER, serialNumber, false, activationFeeChargeCode);
		throw new UnsupportedOperationException("Decommissioned method");
	}

	@Override
	public PCSSubscriber newPCSSubscriber(String serialNumber,	String associatedHandsetIMEI, boolean dealerHasDeposit, String activationFeeChargeCode,	String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, UnsupportedEquipmentException, TelusAPIException {
		if (null == associatedHandsetIMEI) {
			throw new UnsupportedEquipmentException("AssociatedHandsetIMEI is null");
		}
		return newSubscriber(AccountManager.PRODUCT_TYPE_PCS, serialNumber, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage, associatedHandsetIMEI);
		
	}
	
	@Override
	public PCSSubscriber newPCSSubscriber(String serialNumber, boolean dealerHasDeposit,String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
		return newPCSSubscriber(serialNumber, dealerHasDeposit, null, voiceMailLanguage);
	}

	@Deprecated
	public PCSSubscriber newPCSPagerSubscriber(String serialNumber, boolean dealerHasDeposit) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
//		return newPCSPagerSubscriber(serialNumber, dealerHasDeposit, null);
		throw new UnsupportedOperationException("Decommissioned method");
	}

	@Override
	public PCSSubscriber newPCSSubscriber(String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode,String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
		return newSubscriber(AccountManager.PRODUCT_TYPE_PCS, serialNumber, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage, null);
	}

	@Override
	public PCSSubscriber newPCSSubscriber(String serialNumber, String[] secondarySerialNumbers, boolean dealerHasDeposit, String activationFeeChargeCode,String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, UnsupportedEquipmentException, TelusAPIException {
		account.assertAccountExists();

		try {
			Equipment equipment = provider.getEquipmentManager().validateSerialNumber(serialNumber);
			
			if (equipment.isHSPA()) {
				throw new UnsupportedEquipmentException("HSPA equipment is not allowed here.... call the proper method");
			}

			if (!(equipment.isAnalog() || equipment.isPCSHandset() || equipment.is1xRTT() || equipment.isRIM())) {
				throw new InvalidSerialNumberException(serialNumber, InvalidSerialNumberException.EQUIPMENT_NOT_PCS);
			}

			PCSSubscriberInfo info = new PCSSubscriberInfo();

			info.setBanId(account.getBanId());
			info.setSerialNumber(serialNumber);
			info.setSecondarySerialNumbers(secondarySerialNumbers);
			info.setProductType(equipment.getProductType());
			info.setEquipmentType(equipment.getEquipmentType());
			info.setDealerCode(account.getDealerCode());
			info.setSalesRepId(account.getSalesRepCode());
			info.setLanguage(account.getLanguage());
			info.setVoiceMailLanguage(voiceMailLanguage);

			return new TMPCSSubscriber(provider, info, true, activationFeeChargeCode, account, dealerHasDeposit, equipment);

		}
		catch(TelusAPIException e) {
			throw e;
		}
		catch(Throwable e) {
			throw new TelusAPIException(e);
		}
	}
	
	@Override
	public PCSSubscriber newPCSSubscriber(String phoneNumber, String serialNumber, boolean dealerHasDeposit,String voiceMailLanguage)  throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
		PCSSubscriber subscriber = newPCSSubscriber(serialNumber, dealerHasDeposit, null, voiceMailLanguage);
		initializeSubscriber(subscriber, phoneNumber, voiceMailLanguage);
		return subscriber;
	}

	private void initializeSubscriber(PCSSubscriber subscriber, String phoneNumber, String voiceMailLanguage) throws TelusAPIException {
		String[] partialReserverSubscribers = account.getProvider().getAccountManager0().findPartiallyReservedSubscribersByBan(account.getBanId(), 0);
		boolean isPartiallyReserved = false;
	
		for(int i=0; i< partialReserverSubscribers.length; i++) {
			if(partialReserverSubscribers[i].equals(phoneNumber)) {
				isPartiallyReserved = true;
				break;
			}
		}
		if(!isPartiallyReserved)
			throw new TelusAPIException("Reserved subscriber not found");
		
		NumberGroup numberGroup = provider.getReferenceDataHelperEJB().retrieveNumberGroupByPhoneNumberProductType(phoneNumber, subscriber.getProductType());
		
		if (numberGroup == null)
			throw new TelusAPIException("Invalid number group for subscriber.");

		if (subscriber instanceof TMSubscriber) {
			SubscriberInfo subInfo = ((TMSubscriber)subscriber).getDelegate();
			subInfo.setPhoneNumber(phoneNumber);
			subInfo.setSubscriberId(phoneNumber);
			subInfo.setNumberGroup(numberGroup);
			subInfo.setMarketProvince(numberGroup.getProvinceCode());
			subInfo.setStatus(Subscriber.STATUS_RESERVED);
			subscriber.setLanguage(voiceMailLanguage);
			((TMSubscriber)subscriber).waiveSearchFee = true;  //fix for defect # 170279 - need to waive search fee as this will be handled by SSF API
		}
	}
	
	public PCSSubscriber newPCSPagerSubscriber(String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
//		return newSubscriber(AccountManager.PRODUCT_TYPE_PAGER, serialNumber, dealerHasDeposit, activationFeeChargeCode, null, null);
		throw new UnsupportedOperationException("Decommissioned method");
	}

	protected TMPCSSubscriber newSubscriber(String productType, String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode, String voiceMailLanguage, String associatedHandsetIMEI) throws UnknownSerialNumberException,
	SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {

		account.assertAccountExists();
		try {
			Equipment equipment = provider.getEquipmentManager().validateSerialNumber(serialNumber);
			
			if (!equipment.isAnalog() && !equipment.isPCSHandset()	&& !equipment.is1xRTT() && !equipment.isRIM()&& !equipment.isHSPA()) { 
					throw new InvalidSerialNumberException(serialNumber,InvalidSerialNumberException.EQUIPMENT_NOT_PCS);					
				}
			
			PCSSubscriberInfo info = new PCSSubscriberInfo();

			info.setBanId(account.getBanId());
			info.setProductType(productType);
			info.setSerialNumber(serialNumber);
			info.setEquipmentType(equipment.getEquipmentType());
			info.setDealerCode(account.getDealerCode());
			info.setSalesRepId(account.getSalesRepCode());
			info.setLanguage(account.getLanguage());
			info.setVoiceMailLanguage(voiceMailLanguage);
			
			((TMEquipment)equipment).getDelegate().setAssociatedHandsetIMEI(associatedHandsetIMEI);

			return new TMPCSSubscriber(provider, info, true, activationFeeChargeCode, account, dealerHasDeposit, equipment);

		} catch (TelusAPIException e) {
			throw e;
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}


	@Override
	public PricePlanSubscriberCount[] getPricePlanSubscriberCount() throws TelusAPIException {
		// This method has been implemented in TMAccount to allow TMSubscriber
		// easier access to it.
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public PricePlanSubscriberCount getPricePlanSubscriberCount(String pricePlanCode) throws TelusAPIException {
		// This method has been implemented in TMAccount to allow TMSubscriber
		// easier access to it.
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public PricePlanSubscriberCount[] getShareablePricePlanSubscriberCount() throws TelusAPIException {
		// This method has been implemented in TMAccount to allow TMSubscriber
		// easier access to it.
		throw new UnsupportedOperationException("Method not implemented here");
	}
	
	@Override
	public PricePlanSubscriberCount[] getShareablePricePlanSubscriberCount(boolean refresh) throws TelusAPIException {
		// This method has been implemented in TMAccount to allow TMSubscriber
		// easier access to it.
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public PricePlanSubscriberCount getShareablePricePlanSubscriberCount(String pricePlanCode) throws TelusAPIException {
		// This method has been implemented in TMAccount to allow TMSubscriber
		// easier access to it.
		throw new UnsupportedOperationException("Method not implemented here");
	}

	
	/**
	 * This method is available from provider layer only.
	 * @return
	 * @throws TelusAPIException
	 */
	private HashMap getSubscriberNetworkCount() throws TelusAPIException {
		if (pcsSubscriberNetworkCounts == null) {
			account.assertAccountExists();
			try {
				pcsSubscriberNetworkCounts = provider.getAccountInformationHelper().retrievePCSNetworkCountByBan(account.getBanId());
			}catch (Throwable e) {
				provider.getExceptionHandler().handleException(e);
			} 
		}

		return pcsSubscriberNetworkCounts;
	}
	
	private int getSubscriberNetworkCount(String key) throws TelusAPIException {
		return (getSubscriberNetworkCount() != null && getSubscriberNetworkCount().get(key) != null ? 
				((Integer) getSubscriberNetworkCount().get(key)).intValue()  : 0);
	}
	@Override
	public boolean hasHSPASubscriberInBAN() throws TelusAPIException {
		return (getSubscriberNetworkCount(NetworkType.NETWORK_TYPE_HSPA) > 0 ? true : false); 
	}
	
	public int getPCSSubscriberCount(String networkType, char sub_status) throws TelusAPIException {
		return getSubscriberNetworkCount(networkType + "_" + sub_status);
	}
	
	public void refresh() {
		pcsSubscriberNetworkCounts = null;
	}

	@Override
	public PCSSubscriber newPCSSubscriber(String phoneNumber,
			String serialNumber, String associatedHandsetIMEI,
			boolean dealerHasDeposit, String activationFeeChargeCode,
			String voiceMailLanguage) throws UnknownSerialNumberException,
			SerialNumberInUseException, InvalidSerialNumberException,
			UnsupportedEquipmentException, TelusAPIException {
		PCSSubscriber subscriber = newPCSSubscriber(serialNumber, associatedHandsetIMEI, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage);
		initializeSubscriber(subscriber, phoneNumber, voiceMailLanguage);
		return subscriber;
	}

	@Override
	public PCSSubscriber newPCSSubscriber(String phoneNumber,
			String serialNumber, String[] secondarySerialNumbers,
			boolean dealerHasDeposit, String activationFeeChargeCode,
			String voiceMailLanguage) throws UnknownSerialNumberException,
			SerialNumberInUseException, InvalidSerialNumberException,
			TelusAPIException {
		PCSSubscriber subscriber = newPCSSubscriber(serialNumber, secondarySerialNumbers, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage);
		initializeSubscriber(subscriber, phoneNumber, voiceMailLanguage);
		return subscriber;
	}
	
	@Override
	public PCSSubscriber newPCSBCSubscriber(String seatType,String phoneNumber,String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode,String voiceMailLanguage,String associatedHandsetIMEI) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
		
		Equipment equipment = null ;
		
		if (SeatType.SEAT_TYPE_STARTER.equalsIgnoreCase(seatType) && (serialNumber == null || Equipment.DUMMY_ESN_FOR_HSIA.equals(serialNumber) ==  true )) {
			EquipmentInfo equipmentInfo = new EquipmentInfo();
			equipmentInfo.setSerialNumber(Equipment.DUMMY_ESN_FOR_HSIA);
			equipmentInfo.setEquipmentType(Equipment.EQUIPMENT_TYPE_HSIA);
			equipmentInfo.setNetworkType(NetworkType.NETWORK_TYPE_HSPA);
			serialNumber = Equipment.DUMMY_ESN_FOR_HSIA;
			equipment = new TMEquipment(provider, equipmentInfo);
		} else if (SeatType.SEAT_TYPE_OFFICE.equalsIgnoreCase(seatType)) {
			EquipmentInfo equipmentInfo = new EquipmentInfo();
			equipmentInfo.setSerialNumber(Equipment.DUMMY_ESN_FOR_VOIP);
			equipmentInfo.setEquipmentType(Equipment.EQUIPMENT_TYPE_VOIP);
			equipmentInfo.setNetworkType(NetworkType.NETWORK_TYPE_HSPA);
			serialNumber = Equipment.DUMMY_ESN_FOR_VOIP;
			equipment = new TMEquipment(provider, equipmentInfo);
		} else {
			equipment = provider.getEquipmentManager().validateSerialNumber(serialNumber);
			if (!equipment.isAnalog() && !equipment.isPCSHandset() && !equipment.is1xRTT() && !equipment.isRIM() && !equipment.isHSPA()) {
				throw new InvalidSerialNumberException(serialNumber,InvalidSerialNumberException.EQUIPMENT_NOT_PCS);
			}
		}
		
		PCSSubscriberInfo info = new PCSSubscriberInfo();
		info.setBanId(account.getBanId());
		info.setProductType(AccountManager.PRODUCT_TYPE_PCS);
		info.setSerialNumber(serialNumber);
		info.setEquipmentType(equipment.getEquipmentType());
		info.setDealerCode(account.getDealerCode());
		info.setSalesRepId(account.getSalesRepCode());
		info.setLanguage(account.getLanguage());
		info.setVoiceMailLanguage(voiceMailLanguage);
		
		
		((TMEquipment)equipment).getDelegate().setAssociatedHandsetIMEI(associatedHandsetIMEI); 

		PCSSubscriber pcsBCSubscriber =  new TMPCSSubscriber(provider, info, true, activationFeeChargeCode, account, dealerHasDeposit, equipment);
		if (phoneNumber != null){	
			initializeSubscriber(pcsBCSubscriber, phoneNumber,voiceMailLanguage);
		}
			
		return pcsBCSubscriber;
		
	}
	
}









