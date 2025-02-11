package com.telus.cmb.common.kafka;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.telus.cmb.common.util.AppConfiguration;

public class KafkaMessagePublisherImpl implements KafkaMessagePublisher {
	
	private static final Logger logger = LoggerFactory.getLogger("kafkaLogger");

	public static final String ENCODING = "UTF-8";

	public static final String KAFKA_EVENTS_ALL = "KAFKA_EVENTS_ALL";
	public static final String KAFKA_ACCOUNT_EVENTS = "KAFKA_ACCOUNT_EVENTS";
	public static final String KAFKA_SUBSCRIBER_EVENTS = "KAFKA_SUBSCRIBER_EVENTS";
	
	private List<String> enabledEventLdapKeys = new ArrayList<String>();
	private List<KafkaEventType> enabledEventTypes = new ArrayList<KafkaEventType>();

	private String credential;

	private String endpointUrl;

	private boolean enabled = true;
	
	@PostConstruct
	public void init() {
		setEnabled(AppConfiguration.isKafkaEventPublisherEnabled());
		loadKafkaEnabledEventTypes();
		setCredential(AppConfiguration.getKafkaCredential());
	}
	
	public void setEnabledEventLdapKeys(ArrayList<String> enabledEventLdapKeys) {
		this.enabledEventLdapKeys = enabledEventLdapKeys;
	}
	
	public void setEnabledEventTypes(List<KafkaEventType> enabledEventTypes) {
		this.enabledEventTypes.addAll(enabledEventTypes);
	}
	
	public void setCredential(String credential) {
		this.credential = credential;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public void publish(KafkaMessageBuilder builder, KafkaEventType eventType,KafkaContentType contentType) {
		
		if (enabled && (enabledEventTypes.contains(eventType))) {
		
			logger.debug("Publishing message to kafka...");
			
			KafkaMessage message = new KafkaMessage();
			
			try {
				
				builder.populate(message);
				
				logger.info("Message content: [{}]", message.getContent());
				logger.info("Message metadata: [{}]", message.getMetadata());
				
				post(message,contentType.getValue());
	
				logger.debug("Message published successfully.");
				
			} catch (Throwable e) {
				logger.error("Error publishing message to kafka: {}", e.getMessage(), e);
			}
			
		} else {
			logger.info("Kafka message publishing is disabled. eventType=["+String.valueOf(eventType)+"]");
		}
	}

	/**[ 2019-January Welcome email project ]- we have added this method to load the kafka enabled events based on ldap keys passed from bean initialize
	 *  to minimize the impact on the existing code i.e in  Account EJB and CIS otherwise it throws an error if new ldap entry not found in KafkaEventType enum class*/
	 
	private void loadKafkaEnabledEventTypes() {
		
		if (enabledEventLdapKeys.isEmpty() || enabledEventLdapKeys.contains(KAFKA_EVENTS_ALL)) {
			setEnabledEventTypes(AppConfiguration.getKafkaEnabledAccountEventTypes());
			setEnabledEventTypes(AppConfiguration.getKafkaEnabledSubscriberEventTypes());
			return;
		}
		
		if (enabledEventLdapKeys.contains(KAFKA_ACCOUNT_EVENTS)) {
			setEnabledEventTypes(AppConfiguration.getKafkaEnabledAccountEventTypes());
		}

		if (enabledEventLdapKeys.contains(KAFKA_SUBSCRIBER_EVENTS)) {
			setEnabledEventTypes(AppConfiguration.getKafkaEnabledSubscriberEventTypes());
		}
	}
	
	private void post(KafkaMessage message,String contentType) throws Exception {

		HttpURLConnection connection = null;
		
		try {
		
			URL url = getRequestUrl(message.getMetadata());
			
			logger.debug("Using connection URL: {}", url);
			
			connection = setConnectionProperties(url,contentType);
			
			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(message.getContent().getBytes());
			outputStream.flush();
			
			if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				logger.error("Failed to connect to URL ["+url + "]: Response Code="+ connection.getResponseCode());
				throw new RuntimeException("Failed to connect to URL ["+url + "]: Response Code="+ connection.getResponseCode());
			}
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			String output;
			while((output = bufferedReader.readLine()) != null){
				logger.info("OUTPUT.../n" + output);
			}
			
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private HttpURLConnection setConnectionProperties(URL url,String contentType) throws Exception {
		HttpURLConnection conn;
		conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",contentType);
		conn.setConnectTimeout(15000);
		conn.setReadTimeout(15000);

		conn.setRequestProperty("Authorization", "Basic " + credential);
		
		return conn;
	}
	
	private URL getRequestUrl(Map<String, String> parameters) throws Exception {
		
		StringBuffer buffer = new StringBuffer(endpointUrl);
		
		if (!parameters.isEmpty()) {
			buffer.append('?');

			for(Map.Entry<String, String> param : parameters.entrySet()) {
				if(buffer.toString().endsWith("?")){
					buffer.append(URLEncoder.encode(param.getKey(), ENCODING) + "=" + URLEncoder.encode(param.getValue(), ENCODING));
				}
				else {
					buffer.append("&").append(URLEncoder.encode(param.getKey(), ENCODING) + "=" + URLEncoder.encode(param.getValue(), ENCODING));
				}
			}
		}
		return new URL(buffer.toString());
	}
	
}
