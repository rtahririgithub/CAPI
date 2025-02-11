package com.telus.eas.equipment.info;

/**
 * Title: Telus Domain Project -KB61 Description: Copyright: Copyright (c) 2002
 * Company:
 * 
 * @author @version 1.0
 */

import java.util.ArrayList;
import com.telus.api.InvalidPasswordException;
import com.telus.api.TelusAPIException;
import com.telus.api.dealer.CPMSDealer;
import com.telus.api.dealer.HoursOfOperation;
import com.telus.eas.framework.info.Info;

public class CPMSDealerInfo extends Info implements CPMSDealer {

	static final long serialVersionUID = 1L;

	private String provinceCode;

	private String channelCode;

	private String channelDesc;

	private String userCode;

	private String userDesc;

	private String channelOrgTypeCode;

	private String description;

	private String descriptionFrench;

	private String phone;

	private String[] address;

	private ArrayList hoursOfOperation = new ArrayList();

	private boolean highPriority;
	
	private int[] brandIds;

	// constructors
	public CPMSDealerInfo() {
	}

	public CPMSDealerInfo(String dealerCode) {
		this.channelCode = dealerCode;
	}

	public CPMSDealerInfo(String dealerCode, String salesRepCode) {
		this.channelCode = dealerCode;
		this.userCode = salesRepCode;
	}

	// getters
	public String getProvinceCode() {
		return this.provinceCode;
	}

	public String getChannelCode() {
		return this.channelCode;
	}

	public String getChannelDesc() {
		return this.channelDesc;
	}

	public String getUserCode() {
		return this.userCode;
	}

	public String getUserDesc() {
		return this.userDesc;
	}

	public String getChannelOrgTypeCode() {
		return this.channelOrgTypeCode;
	}

	/**
	 * @return Returns the address.
	 */
	public String[] getAddress() {
		return this.address;
	}

	/**
	 * @return Returns the hoursOfOperation.
	 */
	public HoursOfOperation[] getHoursOfOperation() {
		return getHoursOfOperation0();
	}

	public HoursOfOperationInfo[] getHoursOfOperation0() {
		
	
	HoursOfOperationInfo[] hrs = new HoursOfOperationInfo [hoursOfOperation.size()];
		for (int i = 0; i < hoursOfOperation.size(); i++) {
			hrs[i] = (HoursOfOperationInfo)hoursOfOperation.get(i);
		}
		return hrs;
	}

	/**
	 * @return Returns the phone.
	 */
	public String getPhone() {
		return this.phone;
	}

	// setters
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public void setChannelDesc(String channelDesc) {
		this.channelDesc = channelDesc;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

	public void setChannelOrgTypeCode(String channelOrgTypeCode) {
		this.channelOrgTypeCode = channelOrgTypeCode;
	}

	/**
	 * @param address
	 *            The address to set.
	 */
	public void setAddress(String[] address) {
		this.address = address;
	}

	/**
	 * @param pHoursOfOperation
	 *            The hoursOfOperation[] to set.
	 */
	public void setHoursOfOperation(HoursOfOperationInfo[] pHoursOfOperation) {
		for (int i = 0; i < pHoursOfOperation.length; i++) {
			hoursOfOperation.add(pHoursOfOperation[i]);
			
		}
	}

	/**
	 * @param phone
	 *            The phone to set.
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	// decorator implements these
	public void validUser(String password) throws InvalidPasswordException,
			TelusAPIException {
		throw new java.lang.UnsupportedOperationException(
				"Method not implemented");
	}

	public void resetUserPassword(String newPassword)
			throws InvalidPasswordException, TelusAPIException {
		throw new java.lang.UnsupportedOperationException(
				"Method not implemented");
	}

	public void changeUserPassword(String oldPassword, String newPassword)
			throws InvalidPasswordException, TelusAPIException {
		throw new java.lang.UnsupportedOperationException(
				"Method not implemented");
	}

	public String getCode() {
		return channelCode + '.' + userCode;
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("CPMSDealerInfo:{\n");
		s.append("    provinceCode=[").append(provinceCode).append("]\n");
		s.append("    channelCode=[").append(channelCode).append("]\n");
		s.append("    channelDesc=[").append(channelDesc).append("]\n");
		s.append("    userCode=[").append(userCode).append("]\n");
		s.append("    userDesc=[").append(userDesc).append("]\n");
		s.append("    channelOrgTypeCode=[").append(channelOrgTypeCode).append(
				"]\n");
		s.append("    phone=[").append(phone).append("]\n");
		if (address == null) {
			s.append("    address=[null]\n");
		} else if (address.length == 0) {
			s.append("    address={}\n");
		} else {
			for (int i = 0; i < address.length; i++) {
				s.append("    address[").append(i).append("]=[").append(
						address[i]).append("]\n");
			}
		}
		if (hoursOfOperation == null) {
			s.append("    hoursOfOperation=[null]\n");
		} else if (hoursOfOperation.size() == 0) {
			s.append("    hoursOfOperation={}\n");
		} else {
			//Object[] hours = hoursOfOperation.toArray();
			for (int i = 0; i < hoursOfOperation.size(); i++) {
				s.append("    hoursOfOperation[").append(i).append("]=[")
						.append(hoursOfOperation.get(i)).append("]\n");
			}
		}
		if (brandIds == null) {
			s.append("    brandIds=[null]\n");
		} else if (brandIds.length == 0) {
			s.append("    brandIds={}\n");
		} else {
			for (int i = 0; i < brandIds.length; i++) {
				s.append("    brandIds[").append(i).append("]=[").append(
						brandIds[i]).append("]\n");
			}
		}
		s.append("    description=[").append(description).append("]\n");
		s.append("    descriptionFrench=[").append(descriptionFrench).append(
				"]\n");
		s.append("}");

		return s.toString();
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptionFrench() {
		return descriptionFrench;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}

	public void setHighPriority(boolean highPriority) {
		this.highPriority = highPriority;
	}

	public boolean isHighPriority() {
		return highPriority;
	}
	
	public int[] getBrandIds() {
		return brandIds;
	}
	
	public void setBrandIds(int[] brandIds) {
		this.brandIds = brandIds;
	}

}