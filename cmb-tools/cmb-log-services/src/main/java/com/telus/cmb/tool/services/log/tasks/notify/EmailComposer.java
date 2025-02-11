package com.telus.cmb.tool.services.log.tasks.notify;

public interface EmailComposer {

	public String getEmailSubject(EmailContent emailContent);
	
	public String getEmailBody(EmailContent emailContent);
	
}
