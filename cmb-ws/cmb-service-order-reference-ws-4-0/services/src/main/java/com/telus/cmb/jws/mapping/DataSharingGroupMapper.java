package com.telus.cmb.jws.mapping;


import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.DataSharingGroupInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.DataSharingGroup;

public class DataSharingGroupMapper extends ReferenceMapper<DataSharingGroup, DataSharingGroupInfo> {

	public DataSharingGroupMapper() {
		super(DataSharingGroup.class,DataSharingGroupInfo.class);
	}

}
