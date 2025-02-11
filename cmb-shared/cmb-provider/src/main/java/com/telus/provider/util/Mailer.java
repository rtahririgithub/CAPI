package com.telus.provider.util;

import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.telus.api.TelusAPIException;

public final class Mailer  { //implements ConfigurationChangeListener {

	private static final int FAX = 1;
	private static final int EMAIL = 2;
	
	public Mailer() {
		
	}

	// Send fax methods
	public void sendFax(String faxNumber, byte[] content) throws TelusAPIException {
		send(FAX, new FaxHeader(faxNumber), null, content, null, null, null, null);
	}

	public void sendFax(String faxNumber, byte[] content, File[] attachments) throws TelusAPIException {
		send(FAX, new FaxHeader(faxNumber), null, content, attachments, null, null, null);
	}

	public void sendFax(String faxNumber, byte[] content, URL[] attachments) throws TelusAPIException {
		send(FAX, new FaxHeader(faxNumber), null, content, null, null, attachments, null);
	}
	
	public void sendFax(String faxNumber, byte[] content, URL[] attachments, URL[] embeddedAttachments) throws TelusAPIException {
		send(FAX, new FaxHeader(faxNumber), null, content, null, null, attachments, embeddedAttachments);
	}

	public void sendFax(String faxNumber, String recipient, byte[] content) throws TelusAPIException {
		send(FAX, new FaxHeader(faxNumber, recipient), null, content, null, null, null, null);
	}

	public void sendFax(String faxNumber, String recipient, String company, byte[] content) throws TelusAPIException {
		send(FAX, new FaxHeader(faxNumber, recipient, company), null, content, null, null, null, null);
	}

	public void sendFax(String faxNumber, String recipient, String company, byte[] content, File[] attachments) throws TelusAPIException {
		send(FAX, new FaxHeader(faxNumber, recipient, company), null, content, attachments, null, null, null);
	}
	
	public void sendFax(String faxNumber, String recipient, String company, byte[] content, URL[] attachments) throws TelusAPIException {
		send(FAX, new FaxHeader(faxNumber, recipient, company), null, content, null, null, attachments, null);
	}
	
	public void sendFax(String faxNumber, String recipient, String company, byte[] content, 
			File[] attachments, File[] embeddedAttachments) 
	throws TelusAPIException {
		send(FAX, new FaxHeader(faxNumber, recipient, company), null, content, attachments, embeddedAttachments, null, null);
	}
	
	public void sendFax(String faxNumber, String recipient, String company, byte[] content, 
			URL[] attachments, URL[] embeddedAttachments) 
	throws TelusAPIException {
		send(FAX, new FaxHeader(faxNumber, recipient, company), null, content, null, null, attachments, embeddedAttachments);
	}
	
	// Send email methods
	public void sendEmail(Address sender, Address[] recipients, String subject, byte[] content) throws TelusAPIException {
		send(EMAIL, null, new EmailHeader(sender, recipients, subject), content, null, null, null, null);
	}
	
	public void sendEmail(Address sender, Address[] recipients, String subject, byte[] content, File[] attachments) throws TelusAPIException {
		send(EMAIL, null, new EmailHeader(sender, recipients, subject), content, attachments, null, null, null);
	}

	public void sendEmail(Address sender, Address[] recipients, String subject, byte[] content, 
			File[] attachments, File[] embeddedAttachments) 
	throws TelusAPIException {
		send(EMAIL, null, new EmailHeader(sender, recipients, subject), content, attachments, embeddedAttachments, null, null);
	}

	public void sendEmail(Address sender, Address[] recipients, String subject, byte[] content, URL[] attachments) throws TelusAPIException {
		send(EMAIL, null, new EmailHeader(sender, recipients, subject), content, null, null, attachments, null);
	}

	public void sendEmail(Address sender, Address[] recipients, String subject, byte[] content, 
			URL[] attachments, URL[] embeddedAttachments) 
	throws TelusAPIException {
		send(EMAIL, null, new EmailHeader(sender, recipients, subject), content, null, null, attachments, embeddedAttachments);
	}

	private void send(final int TYPE, FaxHeader faxHeader, EmailHeader emailHeader, byte[] content, 
			File[] fileAttachments, File[] embeddedFileAttachments,
			URL[] urlAttachments, URL[] embeddedUrlAttachments) 
	throws TelusAPIException {
		
		try {
			MimeMultipart multipart = new MimeMultipart();

			//BodyPart inlineMessage = new MimeBodyPart(new ByteArrayInputStream(content));
			BodyPart inlineMessage = new MimeBodyPart(new InternetHeaders(), content);
			inlineMessage.setDisposition(Part.INLINE);
			
			// set Content-Type
			inlineMessage.addHeader("Content-Type", "text/html"); // text/html
			multipart.addBodyPart(inlineMessage);

			BodyPart attachment;
			DataHandler dataHandler;

			// Add files as attachments
			if (fileAttachments != null && fileAttachments.length > 0) {

				File f;
				FileDataSource fileDataSource;

				for (int i = 0; i < fileAttachments.length; i++) {
					f = fileAttachments[i];
					fileDataSource = new FileDataSource(f);
					dataHandler = new DataHandler(fileDataSource);

					attachment = new MimeBodyPart();
					attachment.setFileName(f.getName());
					attachment.setDisposition(Part.ATTACHMENT);
					attachment.setDescription("Attachment: " + f.getName());
					attachment.setDataHandler(dataHandler);

					multipart.addBodyPart(attachment);
				}
				
			}
			
			// Add embedded files
			if (embeddedFileAttachments != null && embeddedFileAttachments.length > 0) {

				File f;
				FileDataSource fileDataSource;

				for (int i = 0; i < embeddedFileAttachments.length; i++) {
					f = embeddedFileAttachments[i];
					fileDataSource = new FileDataSource(f);
					dataHandler = new DataHandler(fileDataSource);

					attachment = new MimeBodyPart();
					attachment.setFileName(f.getName());
					attachment.setText("Embedded file attachment " + i);
					attachment.setHeader("Content-ID", "<" + f.getName() + ">");
					attachment.setDisposition(Part.INLINE);
					attachment.setDescription("Embedded attachment: " + f.getName());
					attachment.setDataHandler(dataHandler);

					multipart.addBodyPart(attachment);
				}
				
			}
			
			// Add URL resources as attachments
			if (urlAttachments != null && urlAttachments.length > 0) {

				URLDataSource urlDataSource = null;

				for (int i = 0; i < urlAttachments.length; i++) {
					urlDataSource = new URLDataSource(urlAttachments[i]);
					dataHandler = new DataHandler(urlDataSource);

					attachment = new MimeBodyPart();
					attachment.setFileName(urlDataSource.getName());
					attachment.setDisposition(Part.ATTACHMENT);
					attachment.setDescription("Attachment: " + urlDataSource.getName());
					attachment.setDataHandler(dataHandler);

					multipart.addBodyPart(attachment);
				}
			}
			
			// Add embedded URL resources
			if (embeddedUrlAttachments != null && embeddedUrlAttachments.length > 0) {

				URLDataSource urlDataSource = null;

				for (int i = 0; i < embeddedUrlAttachments.length; i++) {
					urlDataSource = new URLDataSource(embeddedUrlAttachments[i]);
					dataHandler = new DataHandler(urlDataSource);

					attachment = new MimeBodyPart();
					attachment.setFileName(urlDataSource.getName());
					attachment.setText("Embedded URL resource attachment " + i);
					attachment.setHeader("Content-ID", "<" + urlDataSource.getName() + ">");
					attachment.setDisposition(Part.INLINE);
					attachment.setDescription("Embedded attachment: " + urlDataSource.getName());
					attachment.setDataHandler(dataHandler);

					multipart.addBodyPart(attachment);
				}
			}

			Properties props = System.getProperties();
			props.setProperty("mail.transport.protocol", "smtp"); // optional
			if (FAX == TYPE) {
				props.setProperty("mail.smtp.host", getFaxServer());
			} else {
				props.setProperty("mail.smtp.host", getMailServer());
			}

			Message message = new MimeMessage(Session.getDefaultInstance(props, null));
			if (FAX == TYPE) {

				//compose to destination email address
				
				//if FAX stop working due to FAX server change or destination address pattern change
				//please talk to Vigna Thanikaslam  David Blackmore
				
				ArrayList args = new ArrayList();
				//{0} in the pattern
				args.add(faxHeader.faxNumber ); 
				//{1} in the pattern
				if ( faxHeader.recipient!=null ) args.add( faxHeader.recipient );
				//{2} in the pattern
				if ( faxHeader.company!=null ) args.add( faxHeader.company );
				String to = MessageFormat.format(getFaxAddrPattern(), args.toArray() );
				
				String returnAddr = "rfaxrec@telus.com";

				Address recipient = new InternetAddress(to);
				message.setRecipient(Message.RecipientType.TO, recipient);
				message.setFrom(new InternetAddress(returnAddr));
				
				System.err.println(">>>> Sending FAX through SMTP host[" + getFaxServer() + "],  destination:" + to);

			} else {
				message.setFrom(emailHeader.sender);
				message.setRecipients(Message.RecipientType.TO, emailHeader.recipients);
				message.setSubject(emailHeader.subject);
			}

			message.setContent(multipart);
			message.setSentDate(new Date()); // optional
			message.setHeader("X-Mailer", "JavaMailer"); // optional

			// Send the message
			Transport.send(message);

		} catch(Throwable t) {
			throw new TelusAPIException(t);
		}
	}

	public class FaxHeader {
		
		String faxNumber;
		String recipient;
		String company;

		public FaxHeader(String faxNumber) {
			this(faxNumber, "", "");
		}

		public FaxHeader(String faxNumber, String recipient) {
			this(faxNumber, recipient, "");
		}

		public FaxHeader(String faxNumber, String recipient, String company) {
			this.faxNumber = marshalFaxNumber( faxNumber );
			this.recipient = recipient;
			this.company = company;
		}
	}

	public class EmailHeader {
		Address sender;
		Address[] recipients;
		String subject = "";

		public EmailHeader(Address sender, Address[] recipients, String subject) {
			this.recipients = recipients;
			this.sender = sender;
			this.subject = subject;
		}
	}

	//helper method: prefix the number with '1' when the number is 10 digits
	public static String marshalFaxNumber( String faxNumber ) {
		String newNumber = null;
		
		if ( faxNumber!=null) {
			newNumber = faxNumber.trim();
			if ( newNumber.trim().length()==10) {
				newNumber = "1" + newNumber;   
			}
		}
		return newNumber;
	}
	
	private String getFaxServer() {
		return AppConfiguration.getFaxServer();
	}
	
	private String getMailServer() {
		return AppConfiguration.getMailServer();
	}
	
	private String getFaxAddrPattern() {
		return AppConfiguration.getFaxAddrPattern();
	}
	
}
