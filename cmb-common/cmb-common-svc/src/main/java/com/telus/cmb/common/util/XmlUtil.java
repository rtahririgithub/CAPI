/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * @author Pavel Simonovsky
 *
 */
public class XmlUtil {

	public static Document parse(String src) throws Exception {
		InputSource inputSource = new InputSource( new StringReader(src));
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource);
	}
	
	public static Document newDocument() throws Exception {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	}
	
	public static String toString(Document document) throws Exception {

		StringWriter writer = new StringWriter();
		
		TransformerFactory tfactory = TransformerFactory.newInstance();
		Transformer serializer;

		serializer = tfactory.newTransformer();

		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

		serializer.transform(new DOMSource(document), new StreamResult(writer));
		
		return writer.toString();
	}
	
	public static JAXBElement<String> toJAXBElement(QName qname, String value) {
		return new JAXBElement<String>(qname, String.class, null, value);
	}
	
	public static JAXBElement<XMLGregorianCalendar> toJAXBElement(QName qname, XMLGregorianCalendar value) {
		return new JAXBElement<XMLGregorianCalendar>(qname, XMLGregorianCalendar.class, null, value);
	}
	
}
