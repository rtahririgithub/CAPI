package com.telus.provider.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.telus.api.TelusAPIException;

public final class DOMTransformer { //implements ConfigurationChangeListener {
	
	public DOMTransformer() {
		
	}

	public Document newWarrantyDocument(EquipmentWarranty warranty) throws TelusAPIException {

		Document document = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		//factory.setValidating(false);

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();

			Element root = (Element) document.createElement("warranty");
			Element logoPath = (Element) document.createElement("logoPath");
			Element faxNumber = (Element) document.createElement("faxNumber");
			Element serialNumberLabel = (Element) document.createElement("serialNumberLabel");
			Element serialNumber = (Element) document.createElement("serialNumber");
			Element model = (Element) document.createElement("model");
			Element status = (Element) document.createElement("status");
			Element clientName = (Element) document.createElement("clientName");
			Element doaDate = (Element) document.createElement("doaDate");
			Element expiryDate = (Element) document.createElement("expiryDate");
			Element message = (Element) document.createElement("message");
			Element sentDate = (Element) document.createElement("sentDate");

			// append ...
			document.appendChild(root);
			root.appendChild(logoPath);
			root.appendChild(faxNumber);
			root.appendChild(serialNumberLabel);
			root.appendChild(serialNumber);
			root.appendChild(model);
			root.appendChild(status);
			root.appendChild(clientName);
			root.appendChild(doaDate);
			root.appendChild(expiryDate);
			root.appendChild(message);
			root.appendChild(sentDate);

			logoPath.appendChild(document.createTextNode("cid:" + getRemoteResourceURL("logo_" + warranty.getBrandId() + ".jpg", "images").getFile()));
			faxNumber.appendChild(document.createTextNode(warranty.getFaxNumber()));
			serialNumberLabel.appendChild(document.createTextNode(warranty.getEquipmentType()));
			serialNumber.appendChild(document.createTextNode(warranty.getSerialNumber()));
			model.appendChild(document.createTextNode(warranty.getModel()));
			status.appendChild(document.createTextNode(warranty.getStatus()));
			clientName.appendChild(document.createTextNode(warranty.getClientName()));
			doaDate.appendChild(document.createTextNode(warranty.getDoaDate()));
			expiryDate.appendChild(document.createTextNode(warranty.getExpiryDate()));
			message.appendChild(document.createTextNode(warranty.getMessage()));
			sentDate.appendChild(document.createTextNode(warranty.getSentDate()));

			// Normalizing the DOM
			document.getDocumentElement().normalize();

		} catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built
			throw new TelusAPIException(pce);
		}

		return document;

	}

	public Document newMikeSharedFleetApplicationDocument(MikeSharedFleetApplication application) throws TelusAPIException {

		Document document = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		//factory.setValidating(false);

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();

			Element root = (Element) document.createElement("mikeSharedFleetApplication");

			//Element sectionA = (Element) document.createElement("sectionA");
			Element sectionB = (Element) document.createElement("sectionB");
			//Element sectionC = (Element) document.createElement("sectionC");            
			Element legalBusinessName = (Element) document.createElement("legalBusinessName");
			Element address = (Element) document.createElement("address");
			Element primaryLine = (Element) document.createElement("primaryLine");
			Element city = (Element) document.createElement("city");
			Element province = (Element) document.createElement("province");
			Element postalCode = (Element) document.createElement("postalCode");
			Element ban = (Element) document.createElement("ban");
			Element contactName = (Element) document.createElement("contactName");
			Element businessPhoneNumber = (Element) document.createElement("businessPhoneNumber");
			Element faxNumber = (Element) document.createElement("faxNumber");

			// Append sections
			// Note: Sections A and C of the form is not required at this time
			document.appendChild(root);

			// Root
			//root.appendChild(sectionA);
			root.appendChild(sectionB);
			//root.appendChild(sectionC);

			// Address
			address.appendChild(primaryLine);
			address.appendChild(city);
			address.appendChild(province);
			address.appendChild(postalCode);

			// Section B
			sectionB.appendChild(legalBusinessName);
			sectionB.appendChild(address);
			sectionB.appendChild(ban);
			sectionB.appendChild(contactName);
			sectionB.appendChild(businessPhoneNumber);
			sectionB.appendChild(faxNumber);

			// Fill in the field values
			legalBusinessName.appendChild(document.createTextNode(application.getLegalBusinessName()));
			primaryLine.appendChild(document.createTextNode(application.getPrimaryLine()));
			city.appendChild(document.createTextNode(application.getCity()));
			province.appendChild(document.createTextNode(application.getProvince()));
			postalCode.appendChild(document.createTextNode(application.getPostalCode()));
			ban.appendChild(document.createTextNode(application.getBan()));
			contactName.appendChild(document.createTextNode(application.getContactName()));
			businessPhoneNumber.appendChild(document.createTextNode(application.getBusinessPhoneNumber()));
			faxNumber.appendChild(document.createTextNode(application.getFaxNumber()));

			// Normalizing the DOM
			document.getDocumentElement().normalize();

		} catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built
			Logger.debug0(pce);
			throw new TelusAPIException(pce);
		}

		return document;
	}

	public byte[] transform(Document document, String form, String language) throws TelusAPIException {

		if (document == null || form == null || language == null)
			throw new java.lang.IllegalArgumentException("document, form, and language cannot be null");

		byte[] product = null;
		ByteArrayOutputStream out = null;

		try {
			// Use a Transformer for output
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer(new StreamSource(getRemoteResource(form, language)));
			out = new ByteArrayOutputStream();

			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(out);
			//StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);

			product = out.toByteArray();

		} catch (TransformerConfigurationException tce) {
			throw new TelusAPIException("TransformerConfigurationException occurred", tce);

		} catch (TransformerException te) {
			throw new TelusAPIException("TransformerException occurred", te);

		} finally {
			try {
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (IOException ioe) { }
		}

		return product;

	}

	public InputStream getRemoteResource(String fileName, String subDirectoryPath) throws TelusAPIException {

		URL url;
		try {
			String formURL = AppConfiguration.getTMFormURL();
			url = new URL(formURL + "/" + subDirectoryPath.toLowerCase() + "/" + fileName);
			return url.openStream();

		} catch (IOException e) {
			throw new TelusAPIException(e);
		}
	}

	public URL getRemoteResourceURL(String fileName, String subDirectoryPath) throws TelusAPIException {

		try {
			String formURL = AppConfiguration.getTMFormURL();
			return new URL(formURL + "/" + subDirectoryPath.toLowerCase() + "/" + fileName);

		} catch (IOException e) {
			throw new TelusAPIException(e);
		}
	}

}
