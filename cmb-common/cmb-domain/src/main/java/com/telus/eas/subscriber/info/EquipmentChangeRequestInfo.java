/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.subscriber.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telus.api.account.EquipmentChangeRequest;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.IDENEquipment;
import com.telus.api.equipment.MuleEquipment;
import com.telus.eas.framework.exception.WarningFaultInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.message.info.ApplicationMessageInfo;

public class EquipmentChangeRequestInfo extends Info implements EquipmentChangeRequest {

	static final long serialVersionUID = 1L;

    private Equipment newEquipment;
    private String dealerCode;
    private String salesRepCode;
    private String requestorId;
    private String repairId;
    private String swapType;
    //this is the new one
    private MuleEquipment associatedMuleEquipment;
    private boolean preserveDigitalServices = false;
    private Equipment[] secondaryEquipments;//this is the new
    private String[] secondaryEquipmentSerialNumberList;
    private Equipment associatedHandset; //this is the new HSPA handset
    
    private char allowDuplicateSerialNumber=Subscriber.SWAP_DUPLICATESERIALNO_DONOTALLOW;
    
    //the new equipment serial number, can be serial number of new primary equipment, new iDEN Mule or new HSPA handset
    private String newEquipmentSerialNumber;
    //the new iDEN Mule or HSPA handset serial number
    private String newAssociatedHandsetSerialNumber;  
    private String audienceType;
    
    //subscriber's current primary equipment
    private Equipment currentEquipment; 
    //subscriber's current hand set, only suitable for IDEN and HSPA subscriber
    private Equipment currentAssociatedHandset;  
    
    private List applicationMessageList = new ArrayList(); //list of ApplicationMessageInfo. List of application level warning messages.  Example of application level warnings is a list of warnings raised during equipment swap.
    private List systemWarningList = new ArrayList(); //list of WarningFaultInfo. List of sub-system exceptions that were ignored by business logic.
    
    private boolean invokeAPNFix = true; //true by default. This should be used within CMB EJB only.

    public boolean preserveDigitalServices() {
        return preserveDigitalServices;
    }

    public void setPreserveDigitalServices(boolean preserveDigitalServices) {
        this.preserveDigitalServices = preserveDigitalServices;
    }

    public EquipmentChangeRequestInfo() {
    }

    public Equipment getNewEquipment() {
        return newEquipment;
    }

    public void setNewEquipment(Equipment newEquipment) {
        this.newEquipment = newEquipment;
    }
    
    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public String getSalesRepCode() {
        return salesRepCode;
    }

    public void setSalesRepCode(String salesRepCode) {
        this.salesRepCode = salesRepCode;
    }

    public String getRequestorId() {
        return requestorId;
    }

    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getSwapType() {
        return swapType;
    }

    public void setSwapType(String swapType) {
        this.swapType = swapType;
    }

    public MuleEquipment getAssociatedMuleEquipment() {
        return associatedMuleEquipment;
    }

    public void setAssociatedMuleEquipment(MuleEquipment associatedMuleEquipment) {
        this.associatedMuleEquipment = associatedMuleEquipment;
    }

    public Equipment[] getSecondaryEquipments() {
        return secondaryEquipments;
    }

    public void setSecondaryEquipments(Equipment[] secondaryEquipments) {
        this.secondaryEquipments = secondaryEquipments;
    }
    
    public void setNewAssoicatedHandset(Equipment newAssoicatedHandset) {
    	this.associatedHandset = newAssoicatedHandset;
    }
    
	public Equipment getNewAssoicatedHandset() {
		return associatedHandset;
	}
    
    @Deprecated
    public void setAssociatedHandset(Equipment associatedHandset) {
    	this.associatedHandset = associatedHandset;
    }
    
    @Deprecated
	public Equipment getAssociatedHandset() {
		return associatedHandset;
	}

    public String toString() {
        StringBuffer s = new StringBuffer(128);
        s.append("EquipmentChangeRequestInfo:[\n");
        s.append("    requestorId=[").append(requestorId).append("]\n");
        s.append("    repairId=[").append(repairId).append("]\n");
        s.append("    swapType=[").append(swapType).append("]\n");
        s.append("    newEquipment=[").append(newEquipment).append("]\n");
        s.append("    associatedMuleEquipment=[").append(associatedMuleEquipment).append("]\n");

        if (secondaryEquipments != null) {
            for (int i = 0; i < secondaryEquipments.length; i++)
                s.append("    secondaryEquipments[").append(i).append("]=[").append(secondaryEquipments[i].getSerialNumber()).append("]\n");
        }

        if (associatedHandset != null) {
        	s.append("    associatedHandset=[").append(associatedHandset).append("]\n");
        }
        
        s.append("    allowDuplicateSerialNumber=[").append(allowDuplicateSerialNumber).append("]\n");
        s.append("    newEquipmentSerialNumber=[").append(newEquipmentSerialNumber).append("]\n");
        s.append("    newAssociatedHandsetSerialNumber=[").append(newAssociatedHandsetSerialNumber).append("]\n");
        if (currentEquipment!=null ) {
        	s.append("    currentEquipment=[").append(currentEquipment).append("]\n");
        }
        if (currentAssociatedHandset!=null ) {
        	s.append("    currentAssociatedHandset=[").append(currentAssociatedHandset).append("]\n");
        }
        
        s.append("]");
        return s.toString();
    }

	public char getAllowDuplicateSerialNumber() {
		return allowDuplicateSerialNumber;
	}

	public void setAllowDuplicateSerialNumber(char allowDuplicateSerialNumber) {
		this.allowDuplicateSerialNumber = allowDuplicateSerialNumber;
	}

	public String getNewEquipmentSerialNumber() {
		return newEquipmentSerialNumber;
	}

	public void setNewEquipmentSerialNumber(String newEquipmentSerialNumber) {
		this.newEquipmentSerialNumber = newEquipmentSerialNumber;
	}

	public String getNewAssociatedHandsetSerialNumber() {
		return newAssociatedHandsetSerialNumber;
	}

	public void setNewAssociatedHandsetSerialNumber(String newHandsetSerialNumber) {
		this.newAssociatedHandsetSerialNumber = newHandsetSerialNumber;
	}

	public Equipment getCurrentEquipment() {
		return currentEquipment;
	}

	public void setCurrentEquipment(Equipment currentEquipment) {
		this.currentEquipment = currentEquipment;
	}

	public Equipment getCurrentAssociatedHandset() {
		return currentAssociatedHandset;
	}

	public void setCurrentAssociatedHandset(Equipment currentAssociatedHandset) {
		this.currentAssociatedHandset = currentAssociatedHandset;
	}
	
	/**
	 * This is a helper method trying return the new device in this equipment change request object. 
	 * Device, here, refers to handset in most cases. SIM Card and USIM card are not treated as device.
	 *  
	 * @return equipment object if we find device in this object, otherwise return null.
	 */
	public Equipment deriveNewDevice() {
		
		Equipment newDevice = null;
		
		if ( associatedMuleEquipment!=null) {
			newDevice = associatedMuleEquipment;
		} else if (associatedHandset!=null ) {
			newDevice = associatedHandset;
		} else {
			//now trying to figure out if the newEquipment is a device
			if ( newEquipment.isSIMCard() ) {
				//SIM card is not a device, return null
			}
			else if ( newEquipment.isUSIMCard() ) {
				//HSPA USIM card is not a device, return null
			}
			else if ( newEquipment.isHandset() ){
				newDevice = newEquipment;
			}
			else if ( newEquipment.isRIM() ) {
				newDevice = newEquipment;
			}
			else if ( newEquipment.isIDEN() ) {
				if (((IDENEquipment) newEquipment).isMule()) {
					//the newEquipment itself is a mule equipment
					newDevice = newEquipment;
				}
			} else if (newEquipment.isCDMA() ) {
				newDevice = newEquipment;
			} 

			//anything else that we didn't check, we return null, 
			
		}
		return newDevice;
	}

	public List getApplicationMessageList() {
		return applicationMessageList;
	}

	public void setApplicationMessageList(List applicationMessageList) {
		this.applicationMessageList = applicationMessageList;
	}
	
	public void addApplicationMessageList(ApplicationMessageInfo[] applicationMessages) {
		this.applicationMessageList.addAll(Arrays.asList(applicationMessages));
	}
	
	public void addApplicationMessage(ApplicationMessageInfo applicationMessage) {
		if (applicationMessage != null) {
			applicationMessageList.add(applicationMessage);
		}
	}

	public List getSystemWarningList() {
		return systemWarningList;
	}

	public void setSystemWarningList(List systemWarningList) {
		this.systemWarningList = systemWarningList;
	}
	
	public void addSystemWarning(WarningFaultInfo systemWarning) {
		if (systemWarning != null) {
			systemWarningList.add(systemWarning);
		}
	}
	
	public void addSystemWarningList(WarningFaultInfo[] systemWarnings) {
		this.systemWarningList.addAll(Arrays.asList(systemWarnings));
	}

	public String getAudienceType() {
		return audienceType;
	}

	public void setAudienceType(String audienceType) {
		this.audienceType = audienceType;
	}

	/**
	 * @return the secondaryEquipmentSerialNumberList
	 */
	public String[] getSecondaryEquipmentSerialNumberList() {
		return secondaryEquipmentSerialNumberList;
	}

	/**
	 * @param secondaryEquipmentSerialNumberList the secondaryEquipmentSerialNumberList to set
	 */
	public void setSecondaryEquipmentSerialNumberList(String[] secondaryEquipmentSerialNumberList) {
		this.secondaryEquipmentSerialNumberList = secondaryEquipmentSerialNumberList;
	}

	/**
	 * @return the preserveDigitalServices
	 */
	public boolean isPreserveDigitalServices() {
		return preserveDigitalServices;
	}

	public boolean isInvokeAPNFix() {
		return invokeAPNFix;
	}

	public void setInvokeAPNFix(boolean invokeAPNFix) {
		this.invokeAPNFix = invokeAPNFix;
	}
}




