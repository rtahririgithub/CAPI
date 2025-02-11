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

import java.io.Writer;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Pavel Simonovsky	
 *
 */
public class ConfigDocumentWriter {

	public void write(ConfigDocument document, Writer writer) {
		try {
			
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = processEntry(document.getRootEntry(), doc);
			doc.appendChild(root);
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			
		} catch (Exception e) {
			throw new RuntimeException("Error writing document: " + e.getMessage(), e);
		}
	}
	
	private Element processEntry(ConfigEntry entry, Document doc) {
		Element entryElement = doc.createElement("entry");
		
		if (StringUtils.isNotEmpty(entry.getDescription())) {
			entryElement.setAttribute("description", entry.getDescription());
		}
		
		if (StringUtils.isNotEmpty(entry.getName())) {
			entryElement.setAttribute("name", entry.getName());
		}

		if (StringUtils.isNotEmpty(entry.getValue())) {
			entryElement.setAttribute("value", entry.getValue());
		}
		
		for (ConfigEntry child : entry.getChildren()) {
			entryElement.appendChild(processEntry(child, doc));
		}
		
		for (Map.Entry<String, String> valuePair : entry.getValues().entrySet()) {
			Element valueElement = doc.createElement("value");
			if (!StringUtils.equals("*", valuePair.getKey())) {
				valueElement.setAttribute("environment", valuePair.getKey());
				valueElement.setAttribute("value", valuePair.getValue());
				entryElement.appendChild(valueElement);
			}
		}
		
		
		return entryElement;
	}
}
