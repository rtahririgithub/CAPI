/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.perfmon;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;



/**
 * @author Pavel Simonovsky
 *
 */
public class PerformanceStatisticsMonitor implements PerformanceMonitor {
	
	private static final String DELIMITER = "|";
	private static final DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	private Date lastResetDate = new Date(); 
	
	private Map<String, MethodInvocationStatistics> invocationStatistics = new HashMap<String, MethodInvocationStatistics>();
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.framework.perfmon.PerformanceMonitor#getStatistics()
	 */
	@Override
	public Collection<MethodInvocationStatistics> getStatistics() {
		return invocationStatistics.values();
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.common.perfmon.PerformanceMonitor#handleMethodInvocation(com.telus.cmb.common.perfmon.MethodInvocationProfiler)
	 */
	@Override
	public void handleMethodInvocation(MethodInvocationProfiler profiler) {
		String name = profiler.getClassName() + "." + profiler.getMethodName();
		
		synchronized (invocationStatistics) {
			MethodInvocationStatistics statistic = invocationStatistics.get(name);
			
			if (statistic == null) {
				
				statistic = new MethodInvocationStatistics();
				
				statistic.setClassName(profiler.getClassName());
				statistic.setMethodName(profiler.getMethodName());
				invocationStatistics.put(name, statistic);
			}
//			statistic.add(profiler.getStartTime(), profiler.getStopTime(), profiler.getError() == null);
			statistic.add(profiler.getStartTime(), profiler.getStopTime(), profiler.getError() == null,lastResetDate);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.common.perfmon.PerformanceMonitor#reset()
	 */
	@Override
	public void reset() {
		synchronized(invocationStatistics) {
			invocationStatistics.clear();
			lastResetDate = new Date();
		}
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.common.perfmon.PerformanceMonitor#getTextReport()
	 */
	@Override
	public String getTextReport() {
		try {
			StringBuffer buffer = new StringBuffer();
			List<MethodInvocationStatistics> entries = new ArrayList<MethodInvocationStatistics>(invocationStatistics.values());
			
			Collections.sort(entries, new Comparator<MethodInvocationStatistics>() {

				@Override
				public int compare(MethodInvocationStatistics o1, MethodInvocationStatistics o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			
			for (MethodInvocationStatistics entry : entries) {

				String logLine = DELIMITER+dateFormatter.format(entry.getStartTime())+DELIMITER+dateFormatter.format(new Date()) +DELIMITER+ entry.getGMTOffset() +	DELIMITER + entry.getAverageInvocationTime() + DELIMITER + entry.getTotalNumberOfInvocations() + DELIMITER + 
				DELIMITER + DELIMITER +	entry.getMethodName()+DELIMITER +
				dateFormatter.format( new Date(entry.getFirstInvocationTime())) + DELIMITER +dateFormatter.format( new Date(entry.getLastInvocationTime()))+
				DELIMITER + Long.toString(entry.getMinimumInvocationTime()) + DELIMITER + Long.toString(entry.getMaximumInvocationTime()) + DELIMITER +
				Long.toString(entry.getTotalInvocationTime()) + DELIMITER + Long.toString(entry.getNumberOfSuccessfullInvocations()) + DELIMITER+
				Long.toString(entry.getNumberOfUnsuccessfullInvocations())  +"\n";
				buffer.append(logLine);
			}

			return buffer.toString();
		} catch (Exception e) {
			return "Error creating report: " + e.getMessage();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.common.perfmon.PerformanceMonitor#getReport()
	 */
	@Override
	public String getReport() {
		try {
			
			StringWriter writer = new StringWriter();
			
			StreamResult streamResult = new StreamResult(writer);
			
			SAXTransformerFactory transformerFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
			TransformerHandler handler = transformerFactory.newTransformerHandler();
			
			Transformer transformer = handler.getTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			
			AttributesImpl attrs = new AttributesImpl();
			
			List<MethodInvocationStatistics> entries = new ArrayList<MethodInvocationStatistics>(invocationStatistics.values());
			
			Collections.sort(entries, new Comparator<MethodInvocationStatistics>() {

				@Override
				public int compare(MethodInvocationStatistics o1, MethodInvocationStatistics o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			
			handler.setResult(streamResult);
			handler.startDocument();
			
			addAttribute(attrs, "fromTime", dateFormatter.format(lastResetDate));
			addAttribute(attrs, "toTime", dateFormatter.format(new Date()));

			startElement(handler, "performance-stats", attrs);
			
			for (MethodInvocationStatistics entry : entries) {
				addAttribute(attrs, "name", entry.getName());
				addAttribute(attrs, "className", entry.getClassName());
				addAttribute(attrs, "methodName", entry.getMethodName());
				addAttribute(attrs, "firstInvocation", dateFormatter.format( new Date(entry.getFirstInvocationTime())));
				addAttribute(attrs, "lastInvocation", dateFormatter.format( new Date(entry.getLastInvocationTime())));
				addAttribute(attrs, "minimumTime", Long.toString(entry.getMinimumInvocationTime()));
				addAttribute(attrs, "maximumTime", Long.toString(entry.getMaximumInvocationTime()));
				addAttribute(attrs, "averageTime", Long.toString(entry.getAverageInvocationTime()));
				addAttribute(attrs, "totalTime", Long.toString(entry.getTotalInvocationTime()));
				addAttribute(attrs, "successfullInvocations", Long.toString(entry.getNumberOfSuccessfullInvocations()));
				addAttribute(attrs, "failedInvocations", Long.toString(entry.getNumberOfUnsuccessfullInvocations()));
				addAttribute(attrs, "totalInvocations", Long.toString(entry.getTotalNumberOfInvocations()));
				
				startElement(handler, "method-stats", attrs);
				endElement(handler, "method-stats");

			}

			endElement(handler, "performance-stats");
			
			handler.endDocument();

			return writer.toString();
			
		} catch (Exception e) {
			return "Error creating report: " + e.getMessage();
		}
	}
	
	
	private void startElement(TransformerHandler handler, String name, AttributesImpl attrs) throws SAXException {
		handler.startElement("", "", name, attrs);
		attrs.clear();
	}

	private void endElement(TransformerHandler handler, String name) throws SAXException {
		handler.endElement("", "", name);
	}
	
	private void addAttribute(AttributesImpl attrs, String name, String value) {
		attrs.addAttribute("", "", name, "CDATA", value == null ? "" : value);
	}

}
