package com.telus.provider.rules;

import java.util.HashMap;
import java.util.Iterator;

public class WorkingMemory {
	
	private HashMap attributes = new HashMap();
	private HashMap facts = new HashMap();	
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	public void putAttribute(String key, WorkingMemoryElement element) {
		attributes.put(key, element);
	}
	
	public void removeAttribute(String key) {
		attributes.remove(key);
	}
	
	public Object getFact(String key) {
		return facts.get(key);
	}
	
	public void putFact(String key, ConditionResult result) {
		facts.put(key, result);
	}
	
	public void removeFact(String key) {
		facts.remove(key);
	}
	
	public void clear() {
		attributes.clear();
		facts.clear();
	}
	
	public void setModified(boolean modified) {
		Iterator i = attributes.values().iterator();
		while (i.hasNext()) {
			((WorkingMemoryElement)i.next()).setModified(modified);
		}
	}
	
}
