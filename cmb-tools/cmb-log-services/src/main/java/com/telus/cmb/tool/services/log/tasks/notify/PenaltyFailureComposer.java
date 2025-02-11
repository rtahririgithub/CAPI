package com.telus.cmb.tool.services.log.tasks.notify;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

import com.telus.cmb.tool.services.log.domain.LineInfo;
import com.telus.cmb.tool.services.log.domain.LogSearchResult;

public class PenaltyFailureComposer implements EmailComposer {

	private static final String LINE_DELIMITER = "\\|";
	
	@Override
	public String getEmailSubject(EmailContent emailContent) {
		String today = new SimpleDateFormat("MMM dd, yyyy").format(Calendar.getInstance().getTime());
		String emailSubject = StringUtils.isBlank(emailContent.getSubjectText()) ? "Penalty Failures Report" : emailContent.getSubjectText();		
		return emailSubject + " (" + today + ")";
	}

	@Override
	public String getEmailBody(EmailContent emailContent) {

		StringBuffer body = new StringBuffer();	
		
		
		body.append("<style type='text/css'><!--");
		body.append("p {font-family:'Calibri';} h3 {font-family:'Calibri';} ");
		body.append("table,th,td {font-family:'Calibri'; font-size:10px; border-collapse:collapse; border:1px solid black; padding:4px;} ");
		body.append("th {background-color:yellow;}");
		body.append("--></style>");		
		body.append("<p>The following penalty failures were found:<br/><br/>");
		
		for (String component : emailContent.getResultMap().keySet()) {			
			for (LogSearchResult result : emailContent.getResultMap().get(component)) {
				
				body.append("<h3><b>" + result.getFilePath() + "</b></h3>");
				
				body.append("<table>");
				body.append("<tr>");				
				body.append("<th>Date</th>");	
				body.append("<th>Time</th>");
				body.append("<th>Activity</th>");
				body.append("<th>Ban</th>");
				body.append("<th>Phone Number</th>");
				body.append("<th>Subscription Id</th>");
				body.append("<th>Additional Inputs</th>");
				body.append("<th>Error Code</th>");
				body.append("<th>Error Message</th>");
				body.append("</tr>");
				
				for (LineInfo line : result.getResults()) {
					String[] lineParts = line.getLineContent().split(LINE_DELIMITER);
					if (lineParts.length > 8) {
						body.append("<tr>");
						body.append("<td>" + lineParts[0] + "</td>");
						body.append("<td>" + lineParts[1] + "</td>");
						body.append("<td>" + lineParts[2] + "</td>");
						body.append("<td>" + lineParts[3] + "</td>");
						body.append("<td>" + lineParts[4] + "</td>");
						body.append("<td>" + lineParts[5] + "</td>");
						body.append("<td>" + lineParts[6] + "</td>");
						body.append("<td>" + lineParts[7] + "</td>");
						body.append("<td>" + lineParts[8] + "</td>");
						body.append("</tr>");
					}
				}				
				body.append("</table>");
			}			
		}
		body.append("</p>");
						
		return body.toString();
	}

}
