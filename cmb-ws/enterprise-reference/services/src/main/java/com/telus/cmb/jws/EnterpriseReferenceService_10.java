/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.jws.mapping.reference.enterprise_10.CountryMapper;
import com.telus.cmb.jws.mapping.reference.enterprise_10.LanguageMapper;
import com.telus.cmb.jws.mapping.reference.enterprise_10.ProvinceMapper;
import com.telus.cmb.jws.mapping.reference.enterprise_10.StateMapper;
import com.telus.cmb.jws.mapping.reference.enterprise_10.UnitTypeMapper;
import com.telus.cmb.reference.svc.EnterpriseReferenceFacade;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.Country;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.Language;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.Province;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.State;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.UnitType;

/**
 * @author Pavel Simonovsky
 *
 */

@WebService(
		portName = "EnterpriseReferenceServicePort", 
		serviceName = "EnterpriseReferenceService_v1_0", 
		targetNamespace = "http://telus.com/wsdl/EO/KnowledgeMgmt/EnterpriseReferenceService_1", 
		wsdlLocation = "/wsdls/EnterpriseReferenceService_v1_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.EnterpriseReferenceServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class EnterpriseReferenceService_10 extends BaseService implements EnterpriseReferenceServicePort {
	
	@Autowired
	private EnterpriseReferenceFacade referenceFacade;
	
	/* (non-Javadoc)
	 * @see com.telus.wsdl.eo.knowledgemgmt.enterprisereferenceservice_1_0.EnterpriseReferenceServicePortType#getCountries()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_ERS_0001")
	public List<Country> getCountries() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<Country>>() {

			@Override
			public List<Country> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new CountryMapper().mapToSchema(referenceFacade.getCountries());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.eo.knowledgemgmt.enterprisereferenceservice_1_0.EnterpriseReferenceServicePortType#getLanguages()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_ERS_0002")
	public List<Language> getLanguages() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<Language>>() {

			@Override
			public List<Language> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new LanguageMapper().mapToSchema(referenceFacade.getLanguages());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.eo.knowledgemgmt.enterprisereferenceservice_1_0.EnterpriseReferenceServicePortType#getProvinces()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_ERS_0003")
	public List<Province> getProvinces() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<Province>>() {

			@Override
			public List<Province> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ProvinceMapper().mapToSchema(referenceFacade.getProvinces());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.eo.knowledgemgmt.enterprisereferenceservice_1_0.EnterpriseReferenceServicePortType#getStates()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_ERS_0004")
	public List<State> getStates() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<State>>() {

			@Override
			public List<State> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new StateMapper().mapToSchema(referenceFacade.getStates());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.eo.knowledgemgmt.enterprisereferenceservice_1_0.EnterpriseReferenceServicePortType#getUnitTypes()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_ERS_0005")
	public List<UnitType> getUnitTypes() throws PolicyException, ServiceException {
//		return execute( new ServiceInvocationCallback<List<UnitType>>() {
//
//			@Override
//			public List<UnitType> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
//				return new UnitTypeMapper().mapToSchema(referenceFacade.getUnitTypes());
//			}
//		});
		
		UnitType unitType = new UnitType();
		unitType.setCode("A");
		unitType.setDescription("EN Description");
		unitType.setDescriptionFrench("FR Description X");
		
		List<UnitType> result = new ArrayList<UnitType>();
		
		System.out.println("->");
		
		result.add(unitType);
		
		return result; 
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.reference.ReferenceService#enumerateRuntimeResources(java.util.Map)
	 */
	@Override
	protected Map<String, Object> enumerateRuntimeResources(Map<String, Object> resources) {
		resources.put("EnterpriseReferenceFacade", referenceFacade);
		return super.enumerateRuntimeResources(resources);
	}
	
}
