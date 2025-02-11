/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.domain.identity;

import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author Pavel Simonovsky	
 *
 */
public class ConfigDocumentReader {

	private static final Logger logger = LoggerFactory.getLogger(ConfigDocumentReader.class);
	
	public ConfigDocument read(String source) {
		return read( new StringReader(source));
	}
	
	public ConfigDocument read(Reader reader) {
		try {
			
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.parse( new InputSource(reader));
			
			ConfigDocument result = new ConfigDocument();
			
			ConfigEntry entry = processNode(document.getDocumentElement());
			result.setRootEntry(entry);
			
			return result;
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse xml content: " + e.getMessage(), e);
		}
	}
	
	private ConfigEntry processNode(Element node) {
		
		ConfigEntry entry = new ConfigEntry(node.getAttribute("name"));

		entry.setDescription(node.getAttribute("description"));
		
		String value = node.getAttribute("value");
		if (StringUtils.isNotEmpty(value)) {
			entry.addValue("*", value);
			logger.debug("Adding value: [*] = [{}]", value);
		}
		
		NodeList nodeList = node.getChildNodes();
		for (int idx = 0; idx < nodeList.getLength(); idx++) {
			Node childNode = nodeList.item(idx);
			if (childNode instanceof Element) {
				
				Element childElement = (Element) childNode;
				
				if (StringUtils.equalsIgnoreCase(childNode.getNodeName(), "entry")) {
					ConfigEntry childEntry = processNode((Element) childNode);
					entry.getChildren().add(childEntry);
				} else if (StringUtils.equalsIgnoreCase(childNode.getNodeName(), "value")) {

					String environment = childElement.getAttribute("environment"); 
					String environmentValue = childElement.getAttribute("value");
					
					if (StringUtils.isNotEmpty(environment)) { 
						entry.addValue(environment, environmentValue);
						logger.debug("Adding value: [{}] = [{}]", environment, environmentValue);
					}
				}
			}
		}
		
		logger.debug("Processing node: {}", entry.getName());
		
		return entry;
	}
}
