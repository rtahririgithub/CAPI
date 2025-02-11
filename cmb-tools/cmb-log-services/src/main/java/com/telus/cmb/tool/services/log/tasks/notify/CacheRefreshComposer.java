package com.telus.cmb.tool.services.log.tasks.notify;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.telus.cmb.tool.services.log.domain.LineInfo;
import com.telus.cmb.tool.services.log.domain.LogSearchResult;

public class CacheRefreshComposer implements EmailComposer {

	private static Logger logger = Logger.getLogger(CacheRefreshComposer.class);
	
	@Override
	public String getEmailSubject(EmailContent emailContent) {
		String today = new SimpleDateFormat("MMM dd, yyyy").format(Calendar.getInstance().getTime());
		return "CAPI Cache Refresh " + (emailContent.isTaskCompleted() ? "COMPLETED!" : "Incomplete") + " (" + today + ")";
	}

	@Override
	public String getEmailBody(EmailContent emailContent) {
		
		StringBuffer body = new StringBuffer();
		body.append("Client API reference cache refresh ");

		if (emailContent.isTaskCompleted()) {
			body.append("<b><font color='green'><u>completed</u>");
			List<LogSearchResult> results = emailContent.getResultMap().values().iterator().next();
			Date completedTimestamp = getCompletedTimestamp(results);
			if (completedTimestamp != null) {
				body.append(" @ ").append("<big>").append(new SimpleDateFormat("HH:mm:ss").format(completedTimestamp)).append("</big>");
			}
			body.append("</font></b>!<br/><br/>Here are the details:<br/>");
		} else {
			body.append("was <b><font color='red'>incomplete</font></b>.<br/><br/>Results were missing from these nodes:<br/><ul>");
			Set<String> missingNodes = emailContent.getNodes();
			missingNodes.removeAll(emailContent.getNodesFound());
			for (String missingNode : missingNodes) {
				body.append("<li><b>").append(missingNode).append("</b></li>");
			}
			body.append("</ul><br/>Results found:<br/>");
		}

		body.append("<style type='text/css'><!--.tab { margin-left: 40px; }--></style><p class='tab'>");
		Set<String> duplicateCheck = new HashSet<String>();
		for (LogSearchResult result : emailContent.getResultMap().values().iterator().next()) {
			String node = getNodeFromFilePath(result.getFilePath());
			if (!duplicateCheck.contains(node)) {
				body.append("<b>").append(result.getFilePath()).append("</b><br/>");
				for (LineInfo lineInfo : result.getResults()) {
					body.append("<small><i>").append(lineInfo.getLineContent()).append("</i> [Line ").append(lineInfo.getLineNumber()).append("]</small><br/>");
				}
				body.append("<br/>");
				duplicateCheck.add(node);
			}
		}
		body.append("</p>");

		return body.toString();
	}

	private Date getCompletedTimestamp(List<LogSearchResult> results) {

		Date completedTimestamp = null;
		for (LogSearchResult result : results) {
			for (LineInfo line : result.getResults()) {
				Date timestamp = getTimeFromLog(line.getLineContent());
				if (timestamp != null && (completedTimestamp == null || completedTimestamp.before(timestamp))) {
					completedTimestamp = timestamp;
				}
			}
		}

		return completedTimestamp;
	}

	private Date getTimeFromLog(String content) {

		try {
			if (content.length() > 10) {
				return new SimpleDateFormat("dd MMM yyyy HH:mm:ss").parse(content.substring(0, 20));
			}
		} catch (ParseException e) {
			logger.error("Unable to parse the line " + content + " for the HH:mm:ss time due to error: " + e.getMessage());
		}

		return null;
	}

	private String getNodeFromFilePath(String filePath) {

		if (filePath.matches(".*logs/.*/applications.*")) {
			int startIdx = filePath.indexOf("logs/") + "logs/".length();
			int endIdx = filePath.indexOf("/app", startIdx);
			return filePath.substring(startIdx, endIdx);
		}

		return null;
	}

}
