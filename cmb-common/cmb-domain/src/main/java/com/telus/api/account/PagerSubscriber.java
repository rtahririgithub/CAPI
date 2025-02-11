/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.PagerEquipment;
import com.telus.api.equipment.InvalidPagerEquipmentException;

@Deprecated
public interface PagerSubscriber extends Subscriber {

  String getCapCode();

  void setCoverageRegionCode(String code);

  String getCoverageRegionCode();

  void sendTestPage() throws TelusAPIException;

  Equipment[] getSecondaryEquipments() throws TelusAPIException;

  PagerEquipment newNetworkPagerEquipment(String capCode);

  String getNextPhoneNumber();

  Date getNextPhoneNumberChangeDate();

	/**
	 * @deprecated
	 */
  void testChangeEquipment(PagerEquipment newEquipment, PagerEquipment[] newSecondaryEquipments, String dealerCode, String salesRepCode, String requestorId, boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException, InvalidPagerEquipmentException;
	/**
	 * @param newEquipment
	 * @param newSecondaryEquipments
	 * @param dealerCode
	 * @param salesRepCode
	 * @param requestorId
	 * @param allowDuplicateSerialNo
	 *            one of value of SWAP allow duplicate serial no flag( see
	 *            contants in <tt>Subscriber</tt>) When
	 *            <tt>SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN</tt> is set, the
	 *            operation will bypass checking duplicate serial no. When
	 *            <tt>SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN</tt> is set,
	 *            <tt>SerialNumberInUseException</tt> will be thrown if
	 *            <tt>newEquipment</tt> is already assigned to another
	 *            subscriber in any other BAN When
	 *            <tt>SWAP_DUPLICATESERIALNO_DONOTALLOW</tt> is set,
	 *            <tt>SerialNumberInUseException</tt> will be thrown if
	 *            <tt>newEquipment</tt> is already assigned to another
	 *            subscriber in any BAN
	 * @throws TelusAPIException
	 * @throws SerialNumberInUseException
	 * @throws InvalidEquipmentChangeException
	 * @throws InvalidPagerEquipmentException
	 */
	void testChangeEquipment(PagerEquipment newEquipment,PagerEquipment[] newSecondaryEquipments, String dealerCode,String salesRepCode, String requestorId, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException,InvalidEquipmentChangeException, InvalidPagerEquipmentException;

	/**
	 * @param newEquipment
	 * @param newSecondaryEquipments
	 * @param dealerCode
	 * @param salesRepCode
	 * @param requestorId
	 * @param allowDuplicateSerialNo
	 *            one of value of SWAP allow duplicate serial no flag( see
	 *            contants in <tt>Subscriber</tt>) When
	 *            <tt>SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN</tt> is set, the
	 *            operation will bypass checking duplicate serial no. When
	 *            <tt>SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN</tt> is set,
	 *            <tt>SerialNumberInUseException</tt> will be thrown if
	 *            <tt>newEquipment</tt> is already assigned to another
	 *            subscriber in any other BAN When
	 *            <tt>SWAP_DUPLICATESERIALNO_DONOTALLOW</tt> is set,
	 *            <tt>SerialNumberInUseException</tt> will be thrown if
	 *            <tt>newEquipment</tt> is already assigned to another
	 *            subscriber in any BAN
	 * @throws TelusAPIException
	 * @throws SerialNumberInUseException
	 * @throws InvalidEquipmentChangeException
	 * @throws InvalidPagerEquipmentException
	 */
	void changeEquipment(PagerEquipment newEquipment,PagerEquipment[] newSecondaryEquipments, String dealerCode,String salesRepCode, String requestorId, char allowDuplicateSerialNo)throws TelusAPIException, SerialNumberInUseException,InvalidEquipmentChangeException, InvalidPagerEquipmentException;

}
