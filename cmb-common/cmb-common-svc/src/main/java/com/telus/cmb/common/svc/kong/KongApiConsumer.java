package com.telus.cmb.common.svc.kong;

import java.util.Collections;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.security.KongClientCredentialsResourceDetails;

/**
 * Base class for any API Consumer.
 *
 */
public abstract class KongApiConsumer {
	private static final Logger logger = Logger.getLogger(KongApiConsumer.class);
	private static final String HEADER_ENV = "env";
	private static final String HEADER_TRACE_ID = "X-capi-trace-id";

	@Autowired
	protected Environment env;	
    @Autowired
    protected OAuth2RestTemplate oauth2RestTemplate;    
    @Autowired
    protected KongClientCredentialsResourceDetails cmbOAuth2ResourceDetails;
    
	public abstract String getApiUri();
	protected abstract String getComponentName();

	protected HttpHeaders composeHttpHeader() {
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(HEADER_ENV, cmbOAuth2ResourceDetails.getKongEnv());
        httpHeaders.set(HEADER_TRACE_ID, generateTraceId());
        
		return httpHeaders;
	}

	private static String generateTxId() {
		String uuid = UUID.randomUUID().toString();
		String txId = uuid.substring(0, 8);
		
		return txId;
	}
	
	private String generateTraceId() {
		String traceId = getComponentName()+"-"+generateTxId();
		
		logger.info(HEADER_TRACE_ID+"="+traceId);
		
		return traceId;
	}
	
	protected String ping () throws ApplicationException{
		try {			
	    	HttpHeaders httpHeaders = composeHttpHeader();
	    	httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			HttpEntity<Object> httpEntity = new HttpEntity<Object>( httpHeaders);

			// Call to API Provider and get the result
			ResponseEntity<String> result = oauth2RestTemplate.exchange(getApiUri().concat("/ping"), HttpMethod.GET, httpEntity, String.class);
			
			
			if (logger.isDebugEnabled()) {
				logger.debug("ping() result:" + result);
			}
			if (result == null || HttpStatus.OK != result.getStatusCode()) {
				logger.warn("ping() response :" + result == null ? "null response" : result.getStatusCode() );
			}
			
			return result.getBody();
			
        } catch (Exception e) {
        	logger.error("failed in ping()...", e);
        	throw new ApplicationException(SystemCodes.CMB_EJB, e.getMessage(), "");
        }	
	}
	
	
}
