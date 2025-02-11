/*

 * $Id$



 * %E% %W%



 * Copyright (c) Telus Mobility Inc. All Rights Reserved.



 */

package com.telus.api.equipment;

import com.telus.api.TelusAPIException;
import com.telus.api.account.IDENSubscriber;
import com.telus.api.account.InvalidWarrantyTransferException;
import com.telus.api.account.Subscriber;
import com.telus.api.servicerequest.ServiceRequestHeader;

@Deprecated
public interface MuleEquipment

extends IDENEquipment {

	public static final String DUMMY_REPAIR_ID = "DUMMY0";

	/**
	 * 
	 * Test transferWarranty() method.
	 * Use <CODE>getEquipment()</CODE> to obtain an instance of MuleEquipment.
	 * 
	 * @param subscriber
	 *            IDENSubscriber who wants the warranty transfer on the Mule
	 * @param destinationMuleEquipment
	 * @param dealerCode
	 * @param salesRepCode
	 * @param repairId
	 *            Repair ID is mandatory except for 'Replacement' or SIM to SIM
	 *            swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps
	 *            performed by clients.
	 * @param repair
	 *            True if repair, and false if loaner.
	 * 
	 * @deprecated
	 * @see Subscriber#getEquipment
	 */
	void testTransferWarranty(IDENSubscriber subscriber,
								MuleEquipment destinationMuleEquipment,
								String dealerCode,
								String salesRepCode,
								String requestorId,
								String repairId,
								boolean repair) throws TelusAPIException, InvalidWarrantyTransferException;

	/**
	 * 
	 * Moves the current mule's warranty onto the one specified.
	 * Use <CODE>getEquipment()</CODE> to obtain an instance of MuleEquipment.
	 * 
	 * @param subscriber
	 *            IDENSubscriber who wants the warranty transfer on the Mule
	 * @param destinationMuleEquipment
	 * @param dealerCode
	 * @param salesRepCode
	 * @param repairId
	 *            Repair ID is mandatory except for 'Replacement' or SIM to SIM
	 *            swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps
	 *            performed by clients.
	 * @param repair
	 *            True if repair, and false if loaner.
	 * 
	 * @deprecated
	 * @see Subscriber#getEquipment
	 * @see Subscriber#changeEquipment
	 */
	void transferWarranty(IDENSubscriber subscriber,
							MuleEquipment destinationMuleEquipment,
							String dealerCode,
							String salesRepCode,
							String requestorId,
							String repairId,
							boolean repair) throws TelusAPIException, InvalidWarrantyTransferException;

	/**
	 * 
	 * Get the associated SIM card number of this mule equipment.
	 */
	String getSIMCardNumber() throws TelusAPIException;

	/**
	 * 
	 * This is the new method for mule/rmule swaps. It supports replacement swap
	 * type in addition to repair and loaner repair.
	 * 
	 * @param subscriber
	 *            IDENSubscriber
	 * @param destinationMuleEquipment
	 *            MuleEquipment
	 * @param dealerCode
	 *            String
	 * @param salesRepCode
	 *            String
	 * @param requestorId
	 *            String
	 * @param repairId
	 *            String
	 * @param swapType
	 *            String
	 * @throws TelusAPIException
	 * @throws InvalidWarrantyTransferException
	 */
	void change(IDENSubscriber subscriber,
				MuleEquipment destinationMuleEquipment,
				String dealerCode,
				String salesRepCode,
				String requestorId,
				String repairId,
				String swapType) throws TelusAPIException, InvalidWarrantyTransferException;

	/**
	 * This is the new method for mule/rmule swaps and update SRPDS if header is
	 * not null. It supports replacement swap type in addition to repair and
	 * loaner repair.
	 * 
	 * @param subscriber
	 *            IDENSubscriber
	 * @param destinationMuleEquipment
	 *            MuleEquipment
	 * @param dealerCode
	 *            String
	 * @param salesRepCode
	 *            String
	 * @param requestorId
	 *            String
	 * @param repairId
	 *            String
	 * @param swapType
	 *            String
	 * @param header
	 *            <code>ServiceRequestHeader</code> if not null, will be write
	 *            to SRPDS
	 * @throws TelusAPIException
	 * @throws InvalidWarrantyTransferException
	 */
	void change(IDENSubscriber subscriber,
			MuleEquipment destinationMuleEquipment, String dealerCode,
			String salesRepCode, String requestorId, String repairId,
			String swapType, ServiceRequestHeader header)
			throws TelusAPIException, InvalidWarrantyTransferException;

	/**
	 * 
	 * This is the new testChange method.
	 * 
	 * @param subscriber
	 *            IDENSubscriber
	 * @param destinationMuleEquipment
	 *            MuleEquipment
	 * @param dealerCode
	 *            String
	 * @param salesRepCode
	 *            String
	 * @param requestorId
	 *            String
	 * @param repairId
	 *            String
	 * @param swapType
	 *            String
	 * @throws TelusAPIException
	 * @throws InvalidWarrantyTransferException
	 */
	void testChange(IDENSubscriber subscriber,
					MuleEquipment destinationMuleEquipment,
					String dealerCode,
					String salesRepCode,
					String requestorId,
					String repairId,
					String swapType) throws TelusAPIException, InvalidWarrantyTransferException;

	/**
	 * Returns RIM pin.
	 * @return String
	 */
	String getRIMPin();
}
