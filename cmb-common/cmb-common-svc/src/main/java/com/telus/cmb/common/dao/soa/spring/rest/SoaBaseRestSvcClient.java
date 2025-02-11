/*
 *  Copyright (c) 2013 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.dao.soa.spring.rest;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;

public abstract class SoaBaseRestSvcClient {

	@Autowired
	SoaBasicAuth soaBasicAuth;
	
	private RestTemplate template = new RestTemplate();
	private String endpointUri;
	private SoaRestResponseHandler responseHandler;
	private SoaRestExceptionHandler exceptionHandler;
		
	public SoaBaseRestSvcClient() {
		this(null, null);
	}

	public SoaBaseRestSvcClient(SoaRestResponseHandler responseHandler, SoaRestExceptionHandler exceptionHandler) {
		this.setResponseHandler(responseHandler == null ? new SoaDefaultRestResponseHandler() : responseHandler);
		this.setExceptionHandler(exceptionHandler == null ? new SoaDefaultRestExceptionHandler() : exceptionHandler);
	}

	public void setSoaBasicAuth(SoaBasicAuth soaBasicAuth) {
		this.soaBasicAuth = soaBasicAuth;
	}
	
	public String getEndpointUri() {
		return endpointUri;
	}

	public void setEndpointUri(String endpointUri) {
		this.endpointUri = endpointUri;
	}

	public SoaRestResponseHandler getResponseHandler() {
		return responseHandler;
	}

	public void setResponseHandler(SoaRestResponseHandler responseHandler) {
		this.responseHandler = responseHandler;
	}

	public SoaRestExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(SoaRestExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	protected <T> T get(String resource, Map<String, String> uriParams, Map<String, String> requestHeaders, Class<T> responseType) throws ApplicationException {
		try {			
			return responseHandler.handleResponse(template.exchange(getUrl(resource), HttpMethod.GET, createHttpEntity(requestHeaders), responseType, uriParams), getComponentName());				
		} catch (RestClientException e) {
			exceptionHandler.handleRestException(e, getComponentName());
		}
		
		return null;
	}

	protected <T> T get(String resource, Map<String, String> uriParams, Map<String, String> queryParams, Map<String, String> requestHeaders, Class<T> responseType) throws ApplicationException {
		if (queryParams == null || queryParams.isEmpty()) {
			return get(resource, uriParams, requestHeaders, responseType);
		}
		
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUrl(resource));
			for (String queryKey : queryParams.keySet()) {
				builder.queryParam(queryKey, queryParams.get(queryKey));
			}
			return responseHandler.handleResponse(template.exchange(builder.buildAndExpand(uriParams).toUri(), HttpMethod.GET, createHttpEntity(requestHeaders), responseType), getComponentName());
		} catch (RestClientException e) {
			exceptionHandler.handleRestException(e, getComponentName());
		}
		
		return null;
	}

	protected <T> T post(String resource, Map<String, String> params, Map<String, String> requestHeaders, Object requestBody, Class<T> responseType) throws ApplicationException {
		try {
			return responseHandler.handleResponse(template.exchange(getUrl(resource), HttpMethod.POST, createHttpEntity(requestHeaders, requestBody), responseType, params), getComponentName());
		} catch (RestClientException e) {
			exceptionHandler.handleRestException(e, getComponentName());
		}
		return null;
	}

	protected <T> T put(String resource, Map<String, String> params, Map<String, String> requestHeaders, Object requestBody, Class<T> responseType) throws ApplicationException {
		try {
			return responseHandler.handleResponse(template.exchange(getUrl(resource), HttpMethod.PUT, createHttpEntity(requestHeaders, requestBody), responseType, params), getComponentName());
		} catch (RestClientException e) {
			exceptionHandler.handleRestException(e, getComponentName());
		}
		return null;
	}

	protected void put(String resource, Map<String, String> params, Map<String, String> requestHeaders, Object requestBody) throws ApplicationException {
		try {
			responseHandler.handleResponse(template.exchange(getUrl(resource), HttpMethod.PUT, createHttpEntity(requestHeaders, requestBody), Void.class, params), getComponentName());
		} catch (RestClientException e) {
			exceptionHandler.handleRestException(e, getComponentName());
		}
	}

	protected void delete(String resource, Map<String, String> params, Map<String, String> requestHeaders) throws ApplicationException {
		try {
			responseHandler.handleResponse(template.exchange(getUrl(resource), HttpMethod.DELETE, createHttpEntity(requestHeaders), Void.class, params), getComponentName());
		} catch (RestClientException e) {
			exceptionHandler.handleRestException(e, getComponentName());
		}
	}

	abstract public String ping() throws ApplicationException;
	
	protected String getUrl(String resource) throws ApplicationException {

		String endpoint = getEndpointUri() == null ? "" : getEndpointUri().trim();
		if (StringUtils.isBlank(endpoint)) {
			throw new ApplicationException(SystemCodes.SOA_SPRING, "The rest endpoint is null.", StringUtils.EMPTY);
		}
		if (endpoint.charAt(endpoint.length() - 1) != '/' && (resource != null && resource.charAt(0) != '/')) {
			endpoint += "/";
		}

		return endpoint + resource;
	}

	protected HttpEntity<Object> createHttpEntity(Map<String, String> requestHeaders) {
		return new HttpEntity<Object>(createRequestHeader(requestHeaders));
	}

	protected HttpEntity<Object> createHttpEntity(Map<String, String> requestHeaders, Object requestBody) {
		return new HttpEntity<Object>(requestBody, createRequestHeader(requestHeaders));
	}

	private HttpHeaders createRequestHeader(Map<String, String> requestHeaders) {

		HttpHeaders requestHeader = new HttpHeaders();
		String auth = soaBasicAuth.getUsername() + ":" + soaBasicAuth.getPassword();
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
		requestHeader.add("Authorization", "Basic " + new String(encodedAuth));
		if (requestHeaders != null) {
			for (String headerKey : requestHeaders.keySet()) {
				requestHeader.add(headerKey, requestHeaders.get(headerKey));
			}
		}

		return requestHeader;
	}

	protected String getComponentName() {
		return getClass().getSimpleName();
	}
	
}