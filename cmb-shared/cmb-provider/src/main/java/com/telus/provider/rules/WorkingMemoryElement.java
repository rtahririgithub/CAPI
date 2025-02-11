package com.telus.provider.rules;

public class WorkingMemoryElement {
	
	private boolean modified = false;
	private Object element;
	private String elementType = "Object";
	
	public WorkingMemoryElement(String elementType, Object element) {
		this.elementType = elementType;
		this.element = element;
	}
	
	public WorkingMemoryElement(String elementType, Object element, boolean modified) {
		this.elementType = elementType;
		this.element = element;
		this.modified = modified;
	}
	
	public boolean isModified() {
		return modified;
	}
	
	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
	public Object getElement() {
		return element;
	}
	
	public void setElement(Object object) {
		this.element = object;
	}
	
	public String getElementType() {
		return elementType;
	}
	
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	
}
