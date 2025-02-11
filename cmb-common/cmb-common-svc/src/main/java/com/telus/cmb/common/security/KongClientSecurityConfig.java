package com.telus.cmb.common.security;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.util.StreamUtils;

import com.telus.cmb.common.util.AppConfiguration;

@Configuration
@PropertySource("classpath:kong-oauth2-client.properties")
public class KongClientSecurityConfig {
	private static final Logger logger = LoggerFactory.getLogger(KongClientSecurityConfig.class);

	@Autowired
	private Environment env;

	@Bean
	public OAuth2RestTemplate oauth2RestTemplate() {
		OAuth2RestTemplate oauth2RestTemplate = new OAuth2RestTemplate(cmbOAuth2ResourceDetails());
		ClientCredentialsAccessTokenProvider  accessTokenProvider = new ClientCredentialsAccessTokenProvider();
		oauth2RestTemplate.setAccessTokenProvider(accessTokenProvider);
        oauth2RestTemplate.getInterceptors().add(interceptorWithLog());
        
        if (logger.isDebugEnabled()) {
        	ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        	oauth2RestTemplate.setRequestFactory(factory);
        }
        
        return  oauth2RestTemplate;
	}
	
	@Bean
    public KongClientCredentialsResourceDetails cmbOAuth2ResourceDetails() {
		KongClientCredentialsResourceDetails resourceDetails = new KongClientCredentialsResourceDetails();
		resourceDetails.setKongEnv(AppConfiguration.getEnvironment());
		if (logger.isDebugEnabled()) {
			logger.debug("Kong environment:" + resourceDetails.getKongEnv());
		}
		String accessTokenUri = env.getProperty("accessTokenUri." + resourceDetails.getKongEnv());//"https://apigw-st.tsl.telus.com/st/token";
		String clientId = env.getProperty("clientId");
		String clientSecret = env.getProperty("clientSecret." + resourceDetails.getKongEnv());
		List<String> scope = Arrays.asList(env.getProperty("scopeList").split(","));
		resourceDetails.setAccessTokenUri(accessTokenUri);
		resourceDetails.setClientId(clientId);
		resourceDetails.setClientSecret(clientSecret);
		resourceDetails.setScope(scope);
		
		return resourceDetails;
	}
	
	/**
	 * Using Spring REST Interceptor to execute HTTP request, and to log its request and response.
	 */
	@Bean
    public ClientHttpRequestInterceptor interceptorWithLog() {
        return new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
            	
            	logRequest(httpRequest, body);
            	long callBegin = System.currentTimeMillis();
                ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, body);
                long timeTaken = System.currentTimeMillis() - callBegin;
                logResponse(response, httpRequest.getURI(), timeTaken);
                
                return response;
            }
        };
    }
	
	private void logRequest(HttpRequest request, byte[] body) throws IOException {
		if (logger.isDebugEnabled()) {
			StringBuilder forRequest = new StringBuilder("request for [");
			forRequest.append("URI:").append(request.getURI())
			.append(", Method:").append(request.getMethod())
			.append(", Headers:").append(request.getHeaders())
			.append(", Request body:").append((new String(body, "UTF-8")))
			.append("]");
			logger.debug(forRequest.toString());
		}
	}
  
	private void logResponse(ClientHttpResponse response, URI requestUri, long timeTaken) throws IOException {
		if (logger.isDebugEnabled()) {
			StringBuilder forResponse = new StringBuilder("response for [");
			forResponse.append("URI:").append(requestUri)
			.append(", Status code : ").append(response.getStatusCode())
			.append(", Headers : ").append(response.getHeaders())
			.append(", Response body: ").append(StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()))
			.append(", TimeTaken:").append(timeTaken).append(" milliseconds]")
			.append("]");
			logger.debug(forResponse.toString());
		}
	}
}
