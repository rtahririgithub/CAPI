
package com.telus.eas.utility.info;

import com.telus.eas.framework.info.Info;

public class MultilingualTextInfo extends Info {

	public static final long serialVersionUID = 1L;
	
	public static final String LOCALE_EN = "EN";
	public static final String LOCALE_FR = "FR";

	private String locale;
	private String text;

	public MultilingualTextInfo() {	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer(128);
		
		s.append("MultilingualTextInfo: {\n");
		s.append("    locale=[").append(getLocale()).append("]\n");
		s.append("    text=[").append(getText()).append("]\n");
		s.append("}");
		
		return s.toString();
	}
	
}