package com.telus.eas.utility.info;

import java.util.List;

import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceSet;

public class PDAServiceSetInfo extends ServiceSetInfo {
	public PDAServiceSetInfo(List pdaServiceList) {
		setCode(ServiceSet.CODE_PDA);
		setDescription("PDA Mandatory Services");
		setDescriptionFrench("Services obligatoires liés à l’appareil PDA");

		setServices((Service[]) pdaServiceList
				.toArray(new Service[pdaServiceList.size()]));

	}
}
