package com.telus.ait.fwk.logging;

import com.telus.ait.fwk.util.XmlUtils;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;

import static net.serenitybdd.core.Serenity.setSessionVariable;

public class LoggingHandler implements SOAPHandler<SOAPMessageContext> {
	private static final AITLogger log = new AITLogger(LoggingHandler.class);
    private static final String NEWLINE = System.getProperty("line.separator");

    public boolean handleMessage(SOAPMessageContext context) {
		SOAPMessage message = context.getMessage();

		boolean request = ((Boolean) context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();

		try {
            String formattedXml;
			if (request) {
                formattedXml = logMessage(message, NEWLINE + "REQUEST");
                setSessionVariable("messageRequest").to(formattedXml);

			} else {
                formattedXml = logMessage(message, NEWLINE + "RESPONSE");
                setSessionVariable("messageResponse").to(formattedXml);
			}
		} catch (Exception e) {
			log.error("Exception in LoggingHandler.handleMessage", e);
		}
		return true;
	}

	public boolean handleFault(SOAPMessageContext context) {
		SOAPMessage message = context.getMessage();
		try {
            String formattedXml = logMessage(message, "fault");
            setSessionVariable("messageResponse").to(formattedXml);

		} catch (Exception e) {
			log.error("Exception in LoggingHandler.handleFault", e);
		}
		return true;
	}

	private String logMessage(SOAPMessage message, String type) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		message.writeTo(bout);
		String msg = bout.toString("UTF-8");
        String prettyXmlString = XmlUtils.prettyFormat(msg);
        log.debug("soap payload", type, NEWLINE + prettyXmlString);
        return prettyXmlString;
	}

	public void close(MessageContext context) {

	}

	public Set<QName> getHeaders() {

		return Collections.emptySet();
	}
}
