package com.telus.cmb.common.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.telus.api.SystemCodes;
import com.telus.api.SystemException;

/**
 * The purse of this class is to provide simplified API to JAXB, and using Spring framework to provide singleton of JAXContext.  
 *
 */
public class JAXBUtil  {
	
	/**
	 * This context must be created and initialized before this class can be used. we use spring configuration to initialize the JAXBContext. 
	 */
	private JAXBContext jaxbContext;
	
	public void setJaxbContext(JAXBContext context ) {
		jaxbContext = context;
	}
	
	private JAXBContext getJaxbContext() {
		if ( jaxbContext==null) {
			throw new SystemException ( SystemCodes.JAXBUtil, "JAXContext is not initialized.", "" );
		}
		return jaxbContext;
	}

	public <T> T xmlFileToObject ( String xmlFilename, Class<T> requiredType) {
		FileReader reader = null;
		try {
			reader = new FileReader( xmlFilename );
			return xmlToObject(reader, requiredType ); 
		} catch (FileNotFoundException e) {
			throw new SystemException ( SystemCodes.JAXBUtil, e.getMessage(), "", e );
		} finally {
			if (reader!=null) {
				try { reader.close(); } catch( IOException ie ) {}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T xmlToObject ( InputStream inputStream, Class<T> requiredType) {
		try {
			Unmarshaller jaxbUnmarshaller = getJaxbContext().createUnmarshaller();
			return (T) jaxbUnmarshaller.unmarshal(inputStream);
			
		} catch ( JAXBException e) {
			throw new SystemException ( SystemCodes.JAXBUtil, "Encountered error while unmarshalling:" + e.getMessage(), "", e );
		} finally {
			try { inputStream.close(); } catch(IOException ie ) {}
		}
	}
	
	public <T> T xmlToObject ( String xmlContent, Class<T> requiredType ) {
		return xmlToObject(new StringReader(xmlContent), requiredType ); 
	}
	
	@SuppressWarnings("unchecked")
	public <T> T xmlToObject ( Reader reader, Class<T> requiredType ) {
		
		try {
			Unmarshaller jaxbUnmarshaller = getJaxbContext().createUnmarshaller();
			return (T) jaxbUnmarshaller.unmarshal(reader);
			
		} catch ( JAXBException e) {
			throw new SystemException ( SystemCodes.JAXBUtil, "Encountered error while unmarshalling:" + e.getMessage(), "", e );
		} 
	}

	public String objToXML( Object content) {
		try {
			Marshaller jaxbMarshaller =  getJaxbContext().createMarshaller();
			
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter strWriter = new StringWriter();
			jaxbMarshaller.marshal(content, strWriter);		
			return strWriter.toString();
			
		} catch (JAXBException e) {
			throw new SystemException( SystemCodes.JAXBUtil, "Encountered error while mashalling class(" + content.getClass() + "):" + e.getMessage() , "",  e );
		}
	}
}
