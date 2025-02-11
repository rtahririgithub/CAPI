package com.telus.eas.account.credit.info;

import java.nio.charset.Charset;

import com.telus.eas.framework.info.Info;

public class CreditBureauDocumentInfo extends Info {

	static final long serialVersionUID = 1L;
	
	// ISO8859_1 is required to preserve French characters when doing decoding - this constant is placed here because it is needed by multiple mappers
	public static final Charset charsetForDecoding = Charset.forName("ISO8859_1");

	private String documentID;
	private String documentPathText;
	private String documentTypeCode;
	private String document;

	public String getDocumentID() {
		return documentID;
	}

	public void setDocumentID(String documentID) {
		this.documentID = documentID;
	}

	public String getDocumentPathText() {
		return documentPathText;
	}

	public void setDocumentPathText(String documentPathText) {
		this.documentPathText = documentPathText;
	}

	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();

		s.append("CreditBureauDocumentInfo: {\n");
		s.append("    documentID=[").append(getDocumentID()).append("]\n");
		s.append("    documentPathText=[").append(getDocumentPathText()).append("]\n");
		s.append("    documentTypeCode=[").append(getDocumentTypeCode()).append("]\n");
		s.append("    document=[").append(getDocument()).append("]\n");
		s.append("}");

		return s.toString();
	}

}