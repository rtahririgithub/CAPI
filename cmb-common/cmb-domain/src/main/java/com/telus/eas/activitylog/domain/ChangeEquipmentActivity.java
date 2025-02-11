/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.eas.activitylog.domain;

import java.io.Serializable;

import com.telus.api.equipment.Equipment;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.eas.activitylog.queue.info.ActivityLoggingInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class ChangeEquipmentActivity extends ActivityLoggingInfo {

	private static final long serialVersionUID = 1L;
	
	private EquipmentHolder oldEquipment = null;
	
	private EquipmentHolder newEquipment = null;
	
	private String repairId = null;
	
	private String swapType = null;
	
	private EquipmentHolder oldAssociatedMuleEquipment = null;
	
	private EquipmentHolder newAssociatedMuleEquipment = null;

	
	public ChangeEquipmentActivity(ServiceRequestHeader header) {
		super(header);
	}
	
	public String getMessageType() {
		return MESSAGE_TYPE_CHANGE_EQUIPMENT;
	}

	/**
	 * @return the oldEquipment
	 */
	public EquipmentHolder getOldEquipment() {
		return oldEquipment;
	}

	public void setOldEquipment(Equipment equipment) {
		if (equipment != null) {
			this.oldEquipment = new EquipmentHolder(equipment);
		}
	}

	/**
	 * @param oldEquipment the oldEquipment to set
	 */
	public void setOldEquipment(EquipmentHolder oldEquipment) {
		this.oldEquipment = oldEquipment;
	}

	/**
	 * @return the newEquipment
	 */
	public EquipmentHolder getNewEquipment() {
		return newEquipment;
	}

	public void setNewEquipment(Equipment equipment) {
		if (equipment != null) {
			this.newEquipment = new EquipmentHolder(equipment);
		}
	}
	
	/**
	 * @param newEquipment the newEquipment to set
	 */
	public void setNewEquipment(EquipmentHolder equipment) {
		this.newEquipment = equipment;
	}

	/**
	 * @return the repairId
	 */
	public String getRepairId() {
		return repairId;
	}

	/**
	 * @param repairId the repairId to set
	 */
	public void setRepairId(String repairId) {
		this.repairId = repairId;
	}

	/**
	 * @return the swapType
	 */
	public String getSwapType() {
		return swapType;
	}

	/**
	 * @param swapType the swapType to set
	 */
	public void setSwapType(String swapType) {
		this.swapType = swapType;
	}

	/**
	 * @return the oldAssociatedMuleEquipment
	 */
	public EquipmentHolder getOldAssociatedMuleEquipment() {
		return oldAssociatedMuleEquipment;
	}

	public void setOldAssociatedMuleEquipment(Equipment equipment) {
		if (equipment != null) {
			this.oldAssociatedMuleEquipment = new EquipmentHolder(equipment);
		}
	}

	/**
	 * @param oldAssociatedMuleEquipment the oldAssociatedMuleEquipment to set
	 */
	public void setOldAssociatedMuleEquipment(EquipmentHolder oldAssociatedMuleEquipment) {
		this.oldAssociatedMuleEquipment = oldAssociatedMuleEquipment;
	}

	/**
	 * @return the newAssociatedMuleEquipment
	 */
	public EquipmentHolder getNewAssociatedMuleEquipment() {
		return newAssociatedMuleEquipment;
	}

	public void setNewAssociatedMuleEquipment(Equipment equipment) {
		if (equipment != null) {
			this.newAssociatedMuleEquipment = new EquipmentHolder(equipment);
		}
	}

	/**
	 * @param newAssociatedMuleEquipment the newAssociatedMuleEquipment to set
	 */
	public void setNewAssociatedMuleEquipment(EquipmentHolder newAssociatedMuleEquipment) {
		this.newAssociatedMuleEquipment = newAssociatedMuleEquipment;
	}

	public class EquipmentHolder implements Serializable {
		
		private static final long serialVersionUID = 1L;

		private String serialNumber = null;
		
		private String techType = null;
		
		private String productCode = null;
		
		private String productStatusCode = null;
		
		private String productClassCode = null;
		
		private String productGroupTypeCode = null;

		public EquipmentHolder(Equipment equipment) {
			this(equipment.getSerialNumber(), 
				equipment.getTechType(), equipment.getProductCode(), equipment.getProductStatusCode(), 
				equipment.getProductClassCode(), equipment.getProductGroupTypeCode());
		}
		
		public EquipmentHolder(String serialNumber, String techType, String productCode, String productStatusCode, String productClassCode, String productGroupTypeCode) {
			this.serialNumber = serialNumber;
			this.techType = techType;
			this.productCode = productCode;
			this.productStatusCode = productStatusCode;
			this.productClassCode = productClassCode;
			this.productGroupTypeCode = productGroupTypeCode;
		}

		/**
		 * @return the serialNumber
		 */
		public String getSerialNumber() {
			return serialNumber;
		}

		/**
		 * @param serialNumber the serialNumber to set
		 */
		public void setSerialNumber(String serialNumber) {
			this.serialNumber = serialNumber;
		}

		/**
		 * @return the techType
		 */
		public String getTechType() {
			return techType;
		}

		/**
		 * @param techType the techType to set
		 */
		public void setTechType(String techType) {
			this.techType = techType;
		}

		/**
		 * @return the productCode
		 */
		public String getProductCode() {
			return productCode;
		}

		/**
		 * @param productCode the productCode to set
		 */
		public void setProductCode(String productCode) {
			this.productCode = productCode;
		}

		/**
		 * @return the productStatusCode
		 */
		public String getProductStatusCode() {
			return productStatusCode;
		}

		/**
		 * @param productStatusCode the productStatusCode to set
		 */
		public void setProductStatusCode(String productStatusCode) {
			this.productStatusCode = productStatusCode;
		}

		/**
		 * @return the productClassCode
		 */
		public String getProductClassCode() {
			return productClassCode;
		}

		/**
		 * @param productClassCode the productClassCode to set
		 */
		public void setProductClassCode(String productClassCode) {
			this.productClassCode = productClassCode;
		}

		/**
		 * @return the productGroupTypeCode
		 */
		public String getProductGroupTypeCode() {
			return productGroupTypeCode;
		}

		/**
		 * @param productGroupTypeCode the productGroupTypeCode to set
		 */
		public void setProductGroupTypeCode(String productGroupTypeCode) {
			this.productGroupTypeCode = productGroupTypeCode;
		}
		
	}
}
