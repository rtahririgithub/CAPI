package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.svc.kong.KongApiConsumer;

/**
 * An ApiConsumer for CommunicationSuiteMgmtSvc(Scope id of 547).
 *
 */
@Deprecated
@Component
public class KongApiCommunicationSuiteMgmtConsumer extends KongApiConsumer {

	private static final Logger logger = Logger.getLogger(KongApiCommunicationSuiteMgmtConsumer.class);
    
	public void removeFromCommunicationSuite(final int ban, final String companionPhoneNumber, final String primaryPhoneNumber) throws ApplicationException {
		
		try {			
	    	HttpHeaders httpHeaders = composeHttpHeader();
	    	UnLinkRequest httpBody = new UnLinkRequest(ban, companionPhoneNumber, primaryPhoneNumber);
			HttpEntity<UnLinkRequest> httpEntity = new HttpEntity<UnLinkRequest>(httpBody, httpHeaders);

			// URI(URL) parameters
			Map<String, String> urlParams = new HashMap<String, String>();
			String unlinkId = new StringBuilder()
					.append(ban).append("-").append(companionPhoneNumber).toString();//"70922766-4161966330" for example.
			urlParams.put("id", unlinkId);

			// Build Full URI
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(composeUnLinkUri());
			if (logger.isDebugEnabled()) {
				logger.debug("UriComponentsBuilder uri: " + builder.buildAndExpand(urlParams).toUri());
			}

			// Call to API Provider and get the result
			ResponseEntity<String> result = oauth2RestTemplate.exchange(builder.buildAndExpand(urlParams).toUri(), HttpMethod.POST, httpEntity, String.class);
			
			if (logger.isDebugEnabled()) {
				logger.debug("removeFromCommunicationSuite() result:" + result);
			}
			if (result == null || HttpStatus.NO_CONTENT != result.getStatusCode()) {
				logger.warn("removeFromCommunicationSuite() response :" + result == null ? "null response" : result.getStatusCode());
			}
			
        } catch (Exception e) {
        	logger.error("failed in removeFromCommunicationSuite()...", e);
        	throw new ApplicationException(SystemCodes.CMB_SLF_DAO, e.getMessage(), "");
        }		
	}
	
	@Override
	public String getApiUri() {		
		if ("pr".equalsIgnoreCase(cmbOAuth2ResourceDetails.getKongEnv())) {
			return env.getProperty("apiUriKong.CommunicationSuiteMgmt.prod");
		} else {
			return env.getProperty("apiUriKong.CommunicationSuiteMgmt.nonProd");
		}		
	}
	
	@Override
	protected String getComponentName() {
		return "CMB-SLF";
	}

	private String composeUnLinkUri() {		
		String unLinkUri = "communicationSuite/{id}/unlink";
		return getApiUri().concat(unLinkUri);
	}

	/**
	 * Represents Request Body for "/unlink" call.
	 */
	private static class UnLinkRequest {
		@JsonProperty("billingAccountNumber")
		private int ban;
		@JsonProperty("companionSubscriber")
		private String companionPhoneNumber;
		@JsonProperty("primarySubscriber")
		private String primaryPhoneNumber;
		
		public UnLinkRequest(int ban, String companionPhoneNumber, String primaryPhoneNumber) {
			this.ban = ban;
			this.companionPhoneNumber = companionPhoneNumber;
			this.primaryPhoneNumber = primaryPhoneNumber; 
		}
	}

	
}
