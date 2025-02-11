/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.infrastructure.adapter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean;

import com.telus.cmb.wsclient.EnterpriseConsumerProfileRegistrationPort;
import com.telus.rdm.domain.identityprofile.IdentityProfileRegistrationServiceAdapter;

/**
 * @author x113300
 *
 */

@Configuration
public class AdaptersConfiguration {

	@Bean
	public IdentityProfileRegistrationServiceAdapter consumerProfileRegistrationServiceAdapter() throws Exception {
		
		JaxWsPortProxyFactoryBean factory = new JaxWsPortProxyFactoryBean();
		
		factory.setEndpointAddress("https://soa-mp-kidc-dv01.tsl.telus.com:443/v1/cmo/informationmgmt/registration/EnterpriseConsumerProfileRegistrationSvc_v1_1_vs0");
		factory.setUsername("ClientAPI_EJB");
		factory.setPassword("soaorgid");
		
		factory.setServiceInterface(EnterpriseConsumerProfileRegistrationPort.class);
		factory.setWsdlDocumentResource( new ClassPathResource("wsdls/EnterpriseConsumerProfileRegistrationSvc_v1_1.wsdl"));
		factory.setServiceName("EnterpriseConsumerProfileRegistrationSvc_v1_1");
		factory.setLookupServiceOnStartup(false);
		factory.afterPropertiesSet();
		
		ConsumerProfileRegistrationServiceAdapterImpl adapter = new ConsumerProfileRegistrationServiceAdapterImpl();
		adapter.setPort((EnterpriseConsumerProfileRegistrationPort) factory.getObject());
		
		return adapter;
	}
}
