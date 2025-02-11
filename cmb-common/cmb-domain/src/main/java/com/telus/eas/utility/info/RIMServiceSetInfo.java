package com.telus.eas.utility.info;

import java.util.List;

import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceSet;

public class RIMServiceSetInfo extends ServiceSetInfo {
	
	public RIMServiceSetInfo(List rimServiceList) {
		setCode(ServiceSet.CODE_RIM);
		setDescription("RIM Mandatory Services");
		setDescriptionFrench("Services obligatoires liés à l’appareil RIM");

		setServices((Service[]) rimServiceList.toArray(new Service[rimServiceList.size()]));
	
	}

}
