/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.endpoint;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.telus.cmb.framework.application.ApplicationRuntimeHelper;
import com.telus.cmb.framework.application.ApplicationServiceLocator;
import com.telus.cmb.framework.perfmon.MethodInvocationStatistics;
import com.telus.cmb.framework.perfmon.PerformanceMonitor;
import com.telus.cmb.framework.util.XmlUtil;
import com.telus.framework.config.ConfigContext;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.BuildInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.DependencyStatus;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.DependencyStatusList;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.NameValuePair;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.OperationStats;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingStats;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.StatsInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class PingResponseBuilder {

	public PingResponse buildResponse(Ping request, EndpointProvider endpoint) {

		ApplicationServiceLocator serviceLocator = ApplicationServiceLocator.getInstance();
		ApplicationRuntimeHelper runtimeHelper = serviceLocator.getApplicationRuntimeHelper();

		PingResponse response = new PingResponse();
		PingStats stats = new PingStats();

		stats.setServiceName(runtimeHelper.getApplicationName());

		// dependencies

		DependencyStatusList dependencyStatuses = new DependencyStatusList();

		Map<String, Object> dependencies = endpoint.enumerateRuntimeResources(new HashMap<String, Object>());
		for (Map.Entry<String, Object> dependency : dependencies.entrySet()) {

			DependencyStatus status = new DependencyStatus();
			status.setName(dependency.getKey());
			status.setStatus(dependency.getValue().toString());

			dependencyStatuses.getDependencyStatusList().add(status);
		}

		stats.setDependencyStatus(dependencyStatuses);

		// build info

		BuildInfo buildInfo = new BuildInfo();

		NameValuePair buildDatePair = new NameValuePair();
		buildDatePair.setName("buildDate");
		String buildDate = runtimeHelper.getBuildDate();
		buildDatePair.setValue(StringUtils.isEmpty(buildDate) ? "Unknown" : buildDate);
		buildInfo.getBuildProperty().add(buildDatePair);

		NameValuePair buildLabelPair = new NameValuePair();
		buildLabelPair.setName("buildLabel");
		String buildLabel = runtimeHelper.getBuildLabel();
		buildLabelPair.setValue(StringUtils.isEmpty(buildLabel) ? "Unknown" : buildLabel);
		buildInfo.getBuildProperty().add(buildLabelPair);

		stats.setBuildInfo(buildInfo);

		// statistics

		PerformanceMonitor monitor = serviceLocator.getPerformanceMonitor();

		for (MethodInvocationStatistics statistics : monitor.getStatistics()) {
			OperationStats operationStats = new OperationStats();

			StatsInfo statsInfo = new StatsInfo();
			statsInfo.setLastRqstDate(new Date(statistics.getLastInvocationTime()));
			statsInfo.setAvgSuccessExecTimeMilliSecs(statistics.getAverageInvocationTime());
			statsInfo.setErrCount((int) statistics.getNumberOfUnsuccessfullInvocations());
			statsInfo.setExecCount((int) statistics.getTotalNumberOfInvocations());

			operationStats.setStatsName(statistics.getMethodName());

			operationStats.setStatsInfo(statsInfo);

			stats.getOperationStatsList().add(operationStats);
		}

		response.setPingStats(stats);

		return response;
	}

	public com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse buildResponse(com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping request,
			EndpointProvider endpoint) {
		com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse response = new com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse();
		try {
			ApplicationServiceLocator serviceLocator = ApplicationServiceLocator.getInstance();
			ApplicationRuntimeHelper runtimeHelper = serviceLocator.getApplicationRuntimeHelper();
			PerformanceMonitor monitor = serviceLocator.getPerformanceMonitor();
			Document statsDocument = XmlUtil.parse(monitor.getReport());
			Document runtimeInfoDocument = XmlUtil.newDocument();
			String serviceName = serviceLocator.getApplicationName();
			String serviceVersion = ConfigContext.getApplicationVersion();

			// service info
			Element rootElement = runtimeInfoDocument.createElement("service-runtime-info");
			rootElement.setAttribute("name", serviceName);
			rootElement.setAttribute("version", serviceVersion);

			// environment
			Element environmentElement = runtimeInfoDocument.createElement("environment");
			environmentElement.setAttribute("domain", runtimeHelper.getDomainName());
			environmentElement.setAttribute("node", runtimeHelper.getNodeName());
			rootElement.appendChild(environmentElement);

			// performance statistics
			rootElement.appendChild(runtimeInfoDocument.importNode(statsDocument.getDocumentElement(), true));
			runtimeInfoDocument.appendChild(rootElement);
			response.setVersion(XmlUtil.toString(runtimeInfoDocument));
		} catch (Exception e) {
			//TODO: log error or throw exception
		}
		return response;
	}

}
