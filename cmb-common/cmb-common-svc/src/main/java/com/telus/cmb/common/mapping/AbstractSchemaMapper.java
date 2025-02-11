/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.mapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * @author Pavel Simonovsky
 *
 */
public abstract class AbstractSchemaMapper<S, T> implements SchemaMapper<S, T> {

//	private Log logger = LogFactory.getLog(getClass());
	
	private static DatatypeFactory datatypeFactory;

	private Class<S> schemaClass;
	
	private Class<T> domainClass;
	
	public AbstractSchemaMapper(Class<S> schemaClass, Class<T> domainClass) {
		this.schemaClass = schemaClass;
		this.domainClass = domainClass;
	}
	
	protected T newDomainInstance() {
		try {
			return domainClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to instantiate domain class " + domainClass, e);
		}
	}
	
	protected S newSchemaInstance() {
		try {
			return schemaClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to instantiate schema class " + schemaClass, e);
		}
	}
	
	protected T performDomainMapping(S source, T target) {
		return target;
	}

	protected S performSchemaMapping(T source, S target) {
		return target;
	}
	
	public T mapToDomain(S source) {
		if (source != null) {
			return performDomainMapping(source, newDomainInstance());	
		}
		return null;
	};
	
	public S mapToSchema(T source) {
		if (source != null) {
			return performSchemaMapping(source, newSchemaInstance());
		}
		return null;
	};
	
	
	public List<S> mapToSchema(Collection<T> domainObjects) {
		List<S> result = new ArrayList<S>();
		if (domainObjects != null) {
			for (T domainObject : domainObjects) {
				result.add(mapToSchema(domainObject));
			}
		}
		return result;
	}

	public List<S> mapToSchema(T [] domainObjects) {
		return mapToSchema(domainObjects == null ? null : Arrays.asList(domainObjects));
	}

	public List<T> mapToDomain(Collection<S> schemaObjects) {
		List<T> result = new ArrayList<T>();
		if (schemaObjects != null) {
			for (S schemaObject : schemaObjects) {
				result.add(mapToDomain(schemaObject));
			}
		}
		return result;
	}

	public List<T> mapToDomain(S [] schemaObjects) {
		return mapToDomain(schemaObjects == null ? null : Arrays.asList(schemaObjects));
	}
	
	protected String toString(char ch) {
		return Character.toString(ch);
	}
	
	protected XMLGregorianCalendar toXmlGregorianCalendar(Date date) {
		XMLGregorianCalendar result = null;
		
		if (date != null) {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			result = getDataTypeFactory().newXMLGregorianCalendar(calendar);
		}
		return result;
	}
	
	protected Calendar toCalendar(Date date) {
		Calendar result = null;
		
		if (date != null) {
			result = Calendar.getInstance();
			result.setTime(date);
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	protected <E> E toEnum(String value, Class<E> enumClass) {
		if (value == null) {
			return null;
		}
		try {
			
			Method method = enumClass.getMethod("fromValue", String.class);
			
			return (E) method.invoke(null, value);
		}  catch (NoSuchMethodException mex) {
//			logger.error("Value mapping error", mex);
		} catch (Throwable t) {
//			logger.warn("Value mapping error: " + t.getCause().getMessage());
		}
		return null;
	}	
	
	protected Collection<Integer> toCollection(int [] values) {
		List<Integer> result = new ArrayList<Integer>();
		if (values != null) {
			for (int value : values) {
				result.add(value);
			}
		}
		return result;
	}

	protected Collection<String> toCollection(String [] values) {
		List<String> result = new ArrayList<String>();
		if (values != null) {
			for (String value : values) {
				result.add(value);
			}
		}
		return result;
	}
	
	private static DatatypeFactory getDataTypeFactory() {
		if (datatypeFactory == null) {
			try {
				datatypeFactory = DatatypeFactory.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Error instantiating DataType factory", e);
			}
		}
		return datatypeFactory;
	}
}
