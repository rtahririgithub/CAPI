package com.telus.cmb.jws;

/**
 * SchemaValidation
 */
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.xml.ws.developer.ValidationErrorHandler;

public class ServiceSchemaValidator extends ValidationErrorHandler {

	public static final String ERROR = "SchemaValidationError";
	
	@Override
	public void error(SAXParseException exception) throws SAXException {
		packet.invocationProperties.put(ERROR, exception);
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		packet.invocationProperties.put(ERROR, exception);
	}

	@Override
	public void warning(SAXParseException exception) throws SAXException {
	}

}
