package com.telus.cmb.tool.services.log.service;

import java.util.List;

import com.telus.cmb.tool.services.log.tasks.notify.EmailContent;
import com.telus.cmb.tool.services.log.tasks.notify.EmailTemplateEnum;

public interface EmailService {
		
	public void sendEmail(List<String> recipientEmails, String subject, String body);
	
	public void sendEmail(List<String> recipientEmails, String subject, String body, String fromEmail, String fromName, boolean bcc);
	
	public void sendEmail(List<String> recipientEmails, EmailTemplateEnum template, EmailContent emailContent);
	
}
