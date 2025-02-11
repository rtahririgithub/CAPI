package com.telus.cmb.tool.services.log.tasks.notify;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

import com.telus.cmb.tool.services.log.domain.LineInfo;
import com.telus.cmb.tool.services.log.domain.LogSearchResult;

public class SimpleEmailComposer implements EmailComposer {

	private static final int SHOW_RESULTS = 5;
	
	@Override
	public String getEmailSubject(EmailContent emailContent) {
		String today = new SimpleDateFormat("MMM dd, yyyy").format(Calendar.getInstance().getTime());
		String emailSubject = StringUtils.isBlank(emailContent.getSubjectText()) ? "PR Log Monitoring: Simple task triggered" : emailContent.getSubjectText();		
		return emailSubject + " (" + today + ")";
	}

	@Override
	public String getEmailBody(EmailContent emailContent) {
		
		StringBuffer body = new StringBuffer();		
		body.append("Found " + emailContent.getTotalResults() + " occurrences of the following criteria today");
		body.append(" @ ").append("<big>").append(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime())).append("</big>").append(":<br/>");
		
		body.append("<ul>");
		for (String criterion : emailContent.getCriteria()) {
			body.append("<li><b>" + criterion + "</b></li>");
		}
		body.append("</ul><br/>");
		body.append("<br/>Here are some examples:<br/>");
		body.append("<style type='text/css'><!--.tab { margin-left: 40px; }--></style>");
		
		for (String component : emailContent.getResultMap().keySet()) {
			for (LogSearchResult result : emailContent.getResultMap().get(component)) {
				if (!result.getResults().isEmpty()) {
					body.append("<b><u><i>" + component + "</i></u></b>:<br/>");
					body.append("<p class='tab'>");
					body.append("<b>").append(result.getFilePath()).append("</b><br/>");
					int count = 1;
					for (LineInfo lineInfo : result.getResults()) {
						body.append("<small><i>").append(lineInfo.getLineContent()).append("</i> [Line ").append(lineInfo.getLineNumber()).append("]</small><br/>");
						if (count > SHOW_RESULTS) {
							break;
						}
					}
					body.append("<br/>");
					break;
				}
			}
			body.append("</p>");
		}		

		return body.toString();
	}

}
