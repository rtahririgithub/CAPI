package com.telus.cmb.tool.services.log.tasks.notify;

import java.text.SimpleDateFormat;

import com.telus.cmb.tool.services.log.domain.task.MismatchEvent;

public class WelcomeEmailComposer implements EmailComposer {

	@Override
	public String getEmailSubject(EmailContent emailContent) {
		String today = new SimpleDateFormat("MMM dd, yyyy").format(emailContent.getSearchDate().getTime());
		return "Activation Kafka mismatches found (" + today + ")";
	}

	@Override
	public String getEmailBody(EmailContent emailContent) {
		
		StringBuffer body = new StringBuffer();
		body.append("The following potential ban/sub mismatches were found in the Kafka logs for the Subscriber Activation flows for <b>");
		body.append(new SimpleDateFormat("MMM dd, yyyy").format(emailContent.getSearchDate().getTime()));
		body.append("</b>:<br><br>");
		body.append("<table><th>Timestamp</th><th>Ban</th><th>Phone Number</th><th>Log Location</th><th>Line #</th>");

		for (MismatchEvent mismatch : emailContent.getMismatches()) {
			body.append("<tr>");
			body.append("<td>" + mismatch.getTimestamp() + "</td>");
			body.append("<td>" + mismatch.getBan() + "</td>");
			body.append("<td>" + mismatch.getPhoneNumber() + "</td>");
			body.append("<td>" + mismatch.getLogLocation() + "</td>");
			body.append("<td>" + mismatch.getLineNumber() + "</td>");
			body.append("</tr>");
		}
		body.append("</table>");

		return body.toString();
	}

}
