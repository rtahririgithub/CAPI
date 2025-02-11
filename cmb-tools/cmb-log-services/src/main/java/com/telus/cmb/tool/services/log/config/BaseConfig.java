package com.telus.cmb.tool.services.log.config;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public abstract class BaseConfig {

	protected static final String XML_CONFIG_FOLDER = "config/xml";

	protected <T> T parseConfigXml(Class<T> xmlClass) throws JAXBException {
		return parseConfigXml(xmlClass, getConfigXmlFile(xmlClass));
	}

	protected <T> File getConfigXmlFile(Class<T> xmlClass) {
		String fileName = Character.toLowerCase(xmlClass.getSimpleName().charAt(0)) + xmlClass.getSimpleName().substring(1) + ".xml";
		File file = new File(getRootXmlConfigFolder() + File.separator + fileName);
		if (file.exists()) {
			return file;
		}
		return null;
	}

	protected String getRootXmlConfigFolder() {
		return getClass().getClassLoader().getResource(XML_CONFIG_FOLDER).getPath().replace("%20", " ");
	}

	@SuppressWarnings("unchecked")
	protected <T> T parseConfigXml(Class<T> xmlClass, File xmlFile) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(xmlClass);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		if (xmlFile != null && xmlFile.exists()) {
			return (T) unmarshaller.unmarshal(xmlFile);
		}
		return null;
	}

}
