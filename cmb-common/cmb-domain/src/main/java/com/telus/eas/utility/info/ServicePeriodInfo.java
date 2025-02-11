package com.telus.eas.utility.info;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TELUS Mobility</p>
 * @author Michael Qin
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telus.api.reference.ServicePeriod;
import com.telus.api.reference.ServicePeriodHours;
import com.telus.api.reference.ServicePeriodIncludedMinutes;
import com.telus.api.reference.ServicePeriodUsageRates;
import com.telus.eas.framework.info.Info;

public class ServicePeriodInfo extends Info implements ServicePeriod {

	static final long serialVersionUID = 1L;

	private String code;
	private String periodName;
	private List servicePeriodHours = new ArrayList();
	private ServicePeriodIncludedMinutesInfo[] servicePeriodIncludedMinutes = new ServicePeriodIncludedMinutesInfo[0];
	private ServicePeriodUsageRatesInfo[] servicePeriodUsageRates = new ServicePeriodUsageRatesInfo[0];

	public String getDescription() {
		throw new java.lang.UnsupportedOperationException(
				"Method not implemented.");
	}

	public String getDescriptionFrench() {
		throw new java.lang.UnsupportedOperationException(
				"Method not implemented.");
	}

	// specific

	public ServicePeriodHours[] getServicePeriodHours() {
		return (ServicePeriodHours[]) servicePeriodHours.toArray(new ServicePeriodHours[servicePeriodHours.size()]);
	}

	public ServicePeriodIncludedMinutes[] getServicePeriodIncludedMinutes() {
		return servicePeriodIncludedMinutes;
	}

	public void setServicePeriodHours(ServicePeriodHoursInfo[] servicePeriodHours) {
		this.servicePeriodHours.clear();
		if ( servicePeriodHours!=null) {
			this.servicePeriodHours.addAll( Arrays.asList(servicePeriodHours ) );
		}
	}
	
	public void addServicePeriodHour( ServicePeriodHoursInfo hourInfo ) {
		this.servicePeriodHours.add( hourInfo );
	}

	public void setServicePeriodIncludedMinutes(
			ServicePeriodIncludedMinutesInfo[] servicePeriodIncludedMinutes) {
		this.servicePeriodIncludedMinutes = servicePeriodIncludedMinutes;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setServicePeriodUsageRates(
			ServicePeriodUsageRatesInfo[] servicePeriodUsageRates) {
		this.servicePeriodUsageRates = servicePeriodUsageRates;
	}

	public ServicePeriodUsageRates[] getServicePeriodUsageRates() {
		return servicePeriodUsageRates;
	}

	public String getPeriodName() {
		return periodName;
	}

	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}

}
