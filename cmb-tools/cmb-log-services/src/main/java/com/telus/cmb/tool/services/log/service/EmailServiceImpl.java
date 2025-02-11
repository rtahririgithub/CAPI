package com.telus.cmb.tool.services.log.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.telus.cmb.tool.services.log.tasks.notify.EmailComposer;
import com.telus.cmb.tool.services.log.tasks.notify.EmailComposerFactory;
import com.telus.cmb.tool.services.log.tasks.notify.EmailContent;
import com.telus.cmb.tool.services.log.tasks.notify.EmailTemplateEnum;

@Service
public class EmailServiceImpl implements EmailService {

	private static Logger logger = Logger.getLogger(EmailServiceImpl.class);
	private static final String SENDER_ADDRESS = "capi.mailer@telus.com";
	private static final String SENDER_NAME = "capi.mailer"; 
	private static final String SMTP_HOST = "mailhost.tmi.telus.com";
	private static final String SMTP_PROP_NAME = "mail.smtp.host";

	@Override
	public void sendEmail(List<String> recipientEmails, String subject, String body) {
		sendEmail(getEmailSession(), recipientEmails, subject, body, SENDER_ADDRESS, SENDER_NAME, false);
	}

	@Override
	public void sendEmail(List<String> recipientEmails, String subject, String body, String fromEmail, String fromName, boolean bcc) {
		sendEmail(getEmailSession(), recipientEmails, subject, body, fromEmail, fromName, bcc);
	}

	@Override
	public void sendEmail(List<String> recipientEmails, EmailTemplateEnum template, EmailContent emailContent) {
		EmailComposer composer = EmailComposerFactory.getEmailComposer(template);
		sendEmail(getEmailSession(), recipientEmails, composer.getEmailSubject(emailContent), composer.getEmailBody(emailContent), SENDER_ADDRESS, SENDER_NAME, false);		
	}

	private Session getEmailSession() {
		Properties props = System.getProperties();
		String smtpHostServer = SMTP_HOST;
		props.put(SMTP_PROP_NAME, smtpHostServer);
		return Session.getInstance(props, null);
	}

	private void sendEmail(Session session, List<String> recipientEmails, String subject, String body, String fromEmail, String fromName, boolean bcc) {

		try {
			MimeMessage msg = createEmailMessage(session, recipientEmails, subject, body, fromEmail, fromName, bcc);
			logger.debug("Message is ready");
			Transport.send(msg);
			logger.debug("Email sent successfully!!");
		} catch (Exception e) {
			logger.error("Email failed to send: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private MimeMessage createEmailMessage(Session session, List<String> recipientEmails, String subject, String body, String fromEmail, String fromName, boolean bcc)
			throws MessagingException, UnsupportedEncodingException, AddressException {

		MimeMessage msg = new MimeMessage(session);
		//set message headers
		msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		msg.addHeader("format", "flowed");
		msg.addHeader("Content-Transfer-Encoding", "8bit");
		msg.setFrom(new InternetAddress(fromEmail, fromName));
		msg.setReplyTo(InternetAddress.parse(fromEmail, false));
		msg.setSubject(subject, "UTF-8");
		msg.setContent(body, "text/html; charset=utf-8");
		msg.setSentDate(new Date());
		msg.setRecipients(bcc ? Message.RecipientType.BCC : Message.RecipientType.TO, InternetAddress.parse(StringUtils.join(new LinkedHashSet<String>(recipientEmails), ","), false));

		return msg;
	}

}
