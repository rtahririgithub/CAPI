/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.ArrayList;
import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.InvalidEquipmentChangeException;
import com.telus.api.account.PagerSubscriber;
import com.telus.api.account.SerialNumberInUseException;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentManager;
import com.telus.api.equipment.InvalidPagerEquipmentException;
import com.telus.api.equipment.PagerEquipment;
import com.telus.api.util.SessionUtil;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.PagerSubscriberInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.equipment.TMEquipment;
import com.telus.provider.equipment.TMPagerEquipment;
import com.telus.provider.util.Logger;

@Deprecated
public class TMPagerSubscriber extends TMSubscriber implements PagerSubscriber {
	/**
	 * @link aggregation
	 */
	private final PagerSubscriberInfo delegate;

	// cached list of the secondary equipment list
	private Equipment[] secondaryEquipments = null;

	public TMPagerSubscriber(TMProvider provider, PagerSubscriberInfo delegate, boolean activation, String activationFeeChargeCode,
			TMAccountSummary accountSummary, boolean dealerHasDeposit, Equipment equipment) {
		super(provider, delegate, activation, activationFeeChargeCode, accountSummary, dealerHasDeposit, equipment);
		this.delegate = delegate;
	}

	public TMPagerSubscriber(TMProvider provider, PagerSubscriberInfo delegate, boolean activation, String activationFeeChargeCode,
			TMAccountSummary accountSummary, boolean dealerHasDeposit) {
		super(provider, delegate, activation, activationFeeChargeCode, accountSummary, dealerHasDeposit);
		this.delegate = delegate;
	}

	public TMPagerSubscriber(TMProvider provider, PagerSubscriberInfo delegate, boolean activation, String activationFeeChargeCode) {
		super(provider, delegate, activation, activationFeeChargeCode);
		this.delegate = delegate;
	}

	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	public void clear() {
		super.clear();
		delegate.clear();
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public String toString() {
		return delegate.toString();
	}

	public String getCapCode() {
		return delegate.getCapCode();
	}

	public String getCoverageRegionCode() {
		return delegate.getCoverageRegionCode();
	}

	public void setCoverageRegionCode(String code) {
		delegate.setCoverageRegionCode(code);
	}

	public String getNextPhoneNumber() {
		return delegate.getNextPhoneNumber();
	}

	public Date getNextPhoneNumberChangeDate() {
		return delegate.getNextPhoneNumberChangeDate();
	}

	public Equipment[] getSecondaryEquipments() throws TelusAPIException {
		if (secondaryEquipments == null) {
			String[] secondarySerialNumbers = getSecondarySerialNumbers();

			if (secondarySerialNumbers != null) {
				ArrayList secondaryEquipmentList = new ArrayList();

				for (int i = 0; i < secondarySerialNumbers.length; i++) {
					if (secondarySerialNumbers[i] != null) {
						TMEquipment equipment = getEquipment0(secondarySerialNumbers[i]);

						if (equipment != null) {
							equipment.setPrimary(false);
							secondaryEquipmentList.add(equipment);
						}
					}
				}

				secondaryEquipments = (Equipment[]) secondaryEquipmentList.toArray(new Equipment[secondaryEquipmentList.size()]);
			}
			else {
				secondaryEquipments = new Equipment[0];
			}
		}

		return secondaryEquipments;
	}

	public void sendTestPage() throws TelusAPIException {
		assertSubscriberExists();
		try {
			provider.getSubscriberManagerBean().sendTestPage(getAccount().getBanId(),
					getSubscriberId(), getProductType());
		}
		catch(Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
	}

	public PagerEquipment newNetworkPagerEquipment(String capCode) {
		EquipmentInfo equipmentInfo = new EquipmentInfo();
		equipmentInfo.setProductType(Subscriber.PRODUCT_TYPE_PAGER);
		equipmentInfo.setEncodingFormat(PagerEquipment.ENCODING_FORMAT_NETWORK);
		equipmentInfo.setCapCode(capCode);
		equipmentInfo.setFormattedCapCode(capCode);
		
		return new TMPagerEquipment(provider, equipmentInfo);
	}

	 public void changeEquipment(PagerEquipment newEquipment, PagerEquipment[] newSecondaryEquipments, String dealerCode, String salesRepCode, String requestorId, char allowDuplicateSerialNo) throws  TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException, InvalidPagerEquipmentException {
		 TMPagerEquipment oldEquipmentTM = (TMPagerEquipment) getEquipment();
		 TMPagerEquipment newEquipmentTM = (TMPagerEquipment) newEquipment;

		 try {
			 // 1. Test Equipment Change Ability if new equipment is different from the current one.
			 testChangeEquipment(newEquipmentTM, newSecondaryEquipments, dealerCode, salesRepCode, requestorId, allowDuplicateSerialNo);

			 // 2. Change equipment
			 int newSecondaryEquipmentsSz = newSecondaryEquipments != null ? newSecondaryEquipments.length : 0;

			 EquipmentInfo[] newSecondaryEquipmentsInfo = new EquipmentInfo[newSecondaryEquipmentsSz];

			 for (int i = 0; i < newSecondaryEquipmentsSz; i++)
				 newSecondaryEquipmentsInfo[i] = ((TMEquipment) newSecondaryEquipments[i]).getDelegate();
			 PricePlanValidationInfo   ppValidationInfo = new PricePlanValidationInfo();
			 
			 provider.getSubscriberLifecycleFacade().changeEquipment(delegate,
					 oldEquipmentTM.getDelegate(),
					 newEquipmentTM.getDelegate(),
					 newSecondaryEquipmentsInfo,
					 dealerCode,
					 salesRepCode,
					 requestorId,
					 Equipment.SWAP_TYPE_REPLACEMENT,
					 null,
					 ppValidationInfo,
					 null, true, null,
					 SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));

			 // 3. If lost - update to found - the swap was previously checked and allowed.
			 if (newEquipment.isStolen())
				 newEquipment.reportFound();

			 // Do the same for secondary equipments
			 for (int i = 0; i < newSecondaryEquipmentsSz; i++)
				 if (newSecondaryEquipments[i].isStolen())
					 newSecondaryEquipments[i].reportFound();

			 provider.getInteractionManager0().subscriberChangeEquipment(this,
					 oldEquipmentTM,
					 newEquipmentTM,
					 dealerCode,
					 salesRepCode,
					 requestorId,
					 null,
					 Equipment.SWAP_TYPE_REPLACEMENT,
					 null);

			 this.equipment = newEquipment;

			 // make sure the subscriber on the contract is fresh
			 // and new equipment is on the subscriber already at this point
			 if (contract == null)
				 contract = (TMContract) getContract();

			 contract.setSubscriber(this);

			 // 6. Save the contract
			 try {
				 String[] KBDealer = getKBDealer(dealerCode, salesRepCode);
				 contract.save(KBDealer[0], KBDealer[1]);
			 }
			 catch(Throwable t) {
				 Logger.debug("contract.save(): SubscriberManagerEJB().changeServiceAgreement() failed: " + t);
			 }
		 }
		 catch(Throwable t) {
			 provider.getExceptionHandler().handleException(t);
		 }
	 }

	 /**
	  * Deprecated in the interface
	  * @deprecated
	  */
	 public void testChangeEquipment(PagerEquipment newEquipment, PagerEquipment[] newSecondaryEquipments, String dealerCode, String salesRepCode, String requestorId, boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException, InvalidPagerEquipmentException {
		 char allowDuplicateSerialNo = convertAllowDuplicateFlag(ignoreSerialNoInUse);
		 testChangeEquipment(newEquipment, newSecondaryEquipments, dealerCode, salesRepCode, requestorId, allowDuplicateSerialNo);
	 }

	 public void testChangeEquipment(PagerEquipment newEquipment, PagerEquipment[] newSecondaryEquipments, String dealerCode, String salesRepCode, String requestorId, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException, InvalidPagerEquipmentException {
		 // test primary equipment
		 if (newEquipment == null || newEquipment.getSerialNumber() == null)
			 throw new InvalidEquipmentChangeException("newEquipment is null.", InvalidEquipmentChangeException.MANDATORY_EQUIPMENT_INFO_NULL);

		 if (newEquipment.isVirtual())
			 throw new InvalidEquipmentChangeException("newEquipment is virtual.", InvalidEquipmentChangeException.IMPOSSIBLE_SWAP_TYPES);

		 if (newEquipment.getProductType() == null || !newEquipment.getProductType().equals(getProductType())) {
			 String exceptionMsg = "The new equipment's ProductType, [" + newEquipment.getProductType() + "], is different from the subscriber's one, [" + getProductType() + "].";
			 throw new InvalidEquipmentChangeException(exceptionMsg, InvalidEquipmentChangeException.IMPOSSIBLE_SWAP_TYPES);
		 }

		 if (newEquipment.getFormattedCapCode() == null || newEquipment.getFormattedCapCode().equals(""))
			 ((TMPagerEquipment) newEquipment).setFormattedCapCode(EquipmentManager.Helper.getFormattedCapCode(newEquipment.getCapCode(), getProvince(), newEquipment.getEncodingFormat(), newEquipment.getEquipmentType()));

		 if (!EquipmentManager.Helper.isValidCapCodeFormat(newEquipment.getEncodingFormat(), newEquipment.getEquipmentType(), newEquipment.getFormattedCapCode()))
			 throw new InvalidPagerEquipmentException(provider.getApplicationMessage(0), InvalidPagerEquipmentException.REASON_INVALID_CAP_CODE);

		 if (!EquipmentManager.Helper.isValidEquipmentType(newEquipment.getModelType(), newEquipment.getEquipmentType()))
			 throw new InvalidPagerEquipmentException(provider.getApplicationMessage(0), InvalidPagerEquipmentException.REASON_INVALID_EQUIPMENT_TYPE);

		 if (!EquipmentManager.Helper.isValidCoverageRegionCode(newEquipment.getEncodingFormat(), newEquipment.getCurrentCoverageRegionCode()))
			 throw new InvalidPagerEquipmentException(provider.getApplicationMessage(0), InvalidPagerEquipmentException.REASON_INVALID_COVERAGE_REGION);

		 if (checkEquipmentInUse((TMEquipment) newEquipment, allowDuplicateSerialNo)) {
			 String exceptionMsg = "The new secondary equipment's serial number, [" + newEquipment.getSerialNumber() + "], is in use.";
			 throw new SerialNumberInUseException(exceptionMsg, newEquipment.getSerialNumber());
		 }

		 if (newEquipment.isStolen() && !((TMEquipment) newEquipment).isInUseOnBan(getBanId(), false))
			 throw new InvalidEquipmentChangeException("The new equipment is lost or stolen.", InvalidEquipmentChangeException.NEW_EQUIPMENT_IS_LOST_STOLEN);

		 // test secondary equipment
		 if (newSecondaryEquipments != null) {
			 Equipment[] oldSecondaryEquipments = getSecondaryEquipments();
			 int oldSecondaryEquipmentsSz = oldSecondaryEquipments != null ? oldSecondaryEquipments.length : 0;

			 for (int i = 0; i < newSecondaryEquipments.length; i++) {
				 if (newSecondaryEquipments[i].isVirtual())
					 throw new InvalidEquipmentChangeException("new secondary equipment is virtual.", InvalidEquipmentChangeException.IMPOSSIBLE_SWAP_TYPES);

				 if (newSecondaryEquipments[i].getFormattedCapCode() == null || newSecondaryEquipments[i].getFormattedCapCode().equals(""))
					 ((TMPagerEquipment) newSecondaryEquipments[i]).setFormattedCapCode(EquipmentManager.Helper.getFormattedCapCode(newSecondaryEquipments[i].getCapCode(), getProvince(), newSecondaryEquipments[i].getEncodingFormat(), newSecondaryEquipments[i].getEquipmentType()));

				 if (!EquipmentManager.Helper.isValidCapCodeFormat(newSecondaryEquipments[i].getEncodingFormat(), newSecondaryEquipments[i].getEquipmentType(), newSecondaryEquipments[i].getFormattedCapCode()))
					 throw new InvalidPagerEquipmentException(provider.getApplicationMessage(0), InvalidPagerEquipmentException.REASON_INVALID_CAP_CODE);

				 if (!EquipmentManager.Helper.isValidEquipmentType(newSecondaryEquipments[i].getModelType(), newSecondaryEquipments[i].getEquipmentType()))
					 throw new InvalidPagerEquipmentException(provider.getApplicationMessage(0), InvalidPagerEquipmentException.REASON_INVALID_EQUIPMENT_TYPE);

				 if (!EquipmentManager.Helper.isValidCoverageRegionCode(newSecondaryEquipments[i].getEncodingFormat(), newSecondaryEquipments[i].getCurrentCoverageRegionCode()))
					 throw new InvalidPagerEquipmentException(provider.getApplicationMessage(0), InvalidPagerEquipmentException.REASON_INVALID_COVERAGE_REGION);

				 boolean isNew = true;

				 for (int j = 0; j < oldSecondaryEquipmentsSz; j++)
					 isNew &= !newSecondaryEquipments[i].equals(oldSecondaryEquipments[j]);

				 if (isNew) {
					 // check if the equipment's product type is the same as the subscriber's one
					 if (newSecondaryEquipments[i].getProductType() == null || !newSecondaryEquipments[i].getProductType().equals(getProductType())) {
						 String exceptionMsg = "The new secondary equipment's ProductType, [" + newEquipment.getProductType() +
						 "], is different from the subscriber's one, [" + getProductType() + "].";

						 throw new InvalidEquipmentChangeException(exceptionMsg, InvalidEquipmentChangeException.IMPOSSIBLE_SWAP_TYPES);
					 }

					 // check if the secondary equipment is already is use
					 if (checkEquipmentInUse( (TMEquipment) newSecondaryEquipments[i], allowDuplicateSerialNo)) {
						 String exceptionMsg = "The new secondary equipment's serial number, [" + newSecondaryEquipments[i].getSerialNumber() + "], is in use.";

						 throw new SerialNumberInUseException(exceptionMsg, newSecondaryEquipments[i].getSerialNumber());
					 }

					 // check lost/stolen
					 if (newSecondaryEquipments[i].isStolen() && !((TMEquipment) newSecondaryEquipments[i]).isInUseOnBan(getBanId(), false))
						 throw new InvalidEquipmentChangeException("The new equipment is lost or stolen.", InvalidEquipmentChangeException.NEW_EQUIPMENT_IS_LOST_STOLEN);
				 }
			 }
		 }
	 }
}




