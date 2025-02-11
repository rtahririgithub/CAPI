package com.telus.cmb.common.kafka;

import java.util.LinkedHashMap;
import java.util.Map;

public class KafkaMessage {

	private Map<String, String> metadata = new LinkedHashMap<String, String>();
	
	private String content;

	/**
	 * @return the metadata
	 */
	public Map<String, String> getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public void addMetadata(String key, String value) {
		metadata.put(key, value);
	}

	public void addMetadata(String key, int value) {
		addMetadata(key, Integer.toString(value));
	}
	
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
}
