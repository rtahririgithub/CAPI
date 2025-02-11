package com.telus.cmb.tool.services.log.tasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.tool.services.log.config.domain.task.TaskApplication;
import com.telus.cmb.tool.services.log.config.domain.task.TaskComponent;
import com.telus.cmb.tool.services.log.dao.SubscriberInfoDao;
import com.telus.cmb.tool.services.log.domain.LineDetail;
import com.telus.cmb.tool.services.log.domain.LineInfo;
import com.telus.cmb.tool.services.log.domain.LogFilePaths;
import com.telus.cmb.tool.services.log.domain.LogSearchResult;
import com.telus.cmb.tool.services.log.domain.LogServerInfo;
import com.telus.cmb.tool.services.log.domain.task.MismatchEvent;
import com.telus.cmb.tool.services.log.domain.task.SubscriberInfo;
import com.telus.cmb.tool.services.log.service.EmailService;
import com.telus.cmb.tool.services.log.tasks.notify.EmailContent;
import com.telus.cmb.tool.services.log.tasks.notify.EmailTemplateEnum;
import com.telus.cmb.tool.services.log.utils.EncryptionUtil;

public class WelcomeEmailTask extends BaseTask {

	public static final String STRING_SUFFIX_START = "\":\"";
	public static final String STRING_END = "\"";
	public static final String INT_SUFFIX_START = "\":";
	public static final String INT_END = ",";	
	public static final int MAX_RESULTS = 100;
	private static final String MAP_KEY_DELIMITER = "|";

	private static Logger logger = Logger.getLogger(WelcomeEmailTask.class);

	@Autowired
	private SubscriberInfoDao subscriberInfoDao;
	
	@Autowired
	private EmailService emailService;
	
	@Override
	public void run() {

		TaskCredentials credentials = TaskCredentials.getInstance();
		if (credentials.isInitialized()) {

			try {
				// Welcome email tasks should only occur in the Subscriber EJB
				TaskApplication taskApplication = taskEnvironment.getTaskApplications().get(0);
				TaskComponent taskComponent = taskApplication.getTaskComponents().get(0);

				// Search the Kafka logs for Subscriber Activation events (and map them on server + timestamp)
				List<LogSearchResult> results = findSearchResults(credentials, taskApplication, taskComponent);
				Map<String, List<LineDetail>> resultMap = mapSearchResults(results);
				
				// Find multiple events on the same minute and check if any ban/sub are mismatched
				List<MismatchEvent> mismatches = new ArrayList<MismatchEvent>();
				for (String key : resultMap.keySet()) {
					if (CollectionUtils.size(resultMap.get(key)) > 1) {
						String[] keySlices = key.split(MAP_KEY_DELIMITER);
						logger.info("Found multiple calls on " + keySlices[0] + " during same minute of " + keySlices[1]);
						for (LineDetail lineDetail : resultMap.get(key)) {
							LineInfo line = lineDetail.getLineInfo();
							String timestamp = line.getLineContent().substring(0, 20);
							String ban = extractField(line.getLineContent(), "banId", false);
							String phoneNumber = extractField(line.getLineContent(), "phoneNumber", true);
							SubscriberInfo subscriberInfo = subscriberInfoDao.getSubscriberBySubscriberId(phoneNumber);
							if (validateBanMatchesSub(ban, subscriberInfo)) {
								logger.info("Ban[" + ban + "] matches sub[" + phoneNumber + "] - OK!");
							} else if (isCancelledSubscriber(subscriberInfo)) {
								logger.info("Skipping mismatch because sub[" + phoneNumber + "] is cancelled - OK!");
							} else {
								mismatches.add(createMismatchEvent(timestamp, ban, phoneNumber, lineDetail.getFilePath(), line.getLineNumber(), line.getLineContent()));
								logger.info("Ban/sub mismatch found on " + timestamp + " - ban[" + ban + "], sub[" + phoneNumber + "] - added to warning list.");
							}
						}
					}
				}

				// Send the notifications
				if (CollectionUtils.isNotEmpty(mismatches)) {
					logger.info(mismatches.size() + " mismatches for welcome email found in Kafka logs today.  Sending out report...");
					EmailContent emailContent = new EmailContent();
					emailContent.setMismatches(mismatches);
					emailContent.setSearchDate(taskComponent.getStartDate());
					emailService.sendEmail(taskEnvironment.getNotificationEmails(), EmailTemplateEnum.welcomeEmail, emailContent);	
				} else {
					logger.info("No mismatches for welcome email found in Kafka logs today.");
				}
			} catch (Exception e) {
				logger.error("Exception occurred when running the welcome email monitoring service: " + e.getMessage());
			}
		} else {
			logger.info("Credentials aren't ready, skipping welcome email task");
		}
	}

	@Override
	public String getDescription() {
		return "Checks if there are potential issues for simultaneous welcome email messages pushed to Kafka.";
	}

	private List<LogSearchResult> findSearchResults(TaskCredentials credentials, TaskApplication taskApplication, TaskComponent taskComponent) throws Exception {

		List<LogSearchResult> results = new ArrayList<LogSearchResult>();
		for (LogFilePaths logFilePaths : filePathConfigRT.getFilePaths(taskEnvironment.getShortname(), taskApplication.getShortname(), taskComponent.getName())) {
			LogServerInfo logServer = logFilePaths.getLogServer();
			String username = credentials.getUsername(logServer.usesUnixLogin());
			for (String filePath : logFilePaths.getFilepaths()) {
				results.addAll(logFileService.getSearchResultsForTask(Arrays.asList(filePath), logServer, username,
						EncryptionUtil.decryptPassword(username, credentials.getPassword(logServer.usesUnixLogin())), taskComponent.getGrepCriteria(), 
						getDateRangeFilter(taskApplication, taskComponent), 1000, null));	
			}
		}

		return results;
	}
	
	private Map<String, List<LineDetail>> mapSearchResults(List<LogSearchResult> results) {
		
		Map<String, List<LineDetail>> resultMap = new HashMap<String, List<LineDetail>>();
		if (CollectionUtils.isEmpty(results)) {
			logger.info("It's weird, no instance of subscriber activation found in Kafka logs.");
		} else {
			logger.info("Found " + results.size() + " activation calls in Kafka logs.  Now grouping by timestamp...");
		}
		for (LogSearchResult result : results) {
			for (LineInfo line : result.getResults()) {
				if (validateEntry(line.getLineContent(), result.getFilePath())) {
					String key = createMapKey(result, line);
					List<LineDetail> lines = resultMap.get(key);
					if (lines == null) {
						lines = new ArrayList<LineDetail>();
					}					
					lines.add(new LineDetail(result.getFilePath(), line));
					resultMap.put(key, lines);					
				}				
			}
		}
		
		return resultMap;
	}

	private String createMapKey(LogSearchResult result, LineInfo line) {
		String filepath = result.getFilePath();
		int hostIdxStart = filepath.indexOf("/", 2) + 1;
		String serverHost = filepath.substring(hostIdxStart, filepath.indexOf("/", hostIdxStart));
		String timestamp = line.getLineContent().substring(0, 16);
		return serverHost + MAP_KEY_DELIMITER + timestamp;
	}
	
	private boolean validateEntry(String line, String filepath) {
		// Temporarily defaulting to "yesterday" - add support for multi-day check
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);		
		return StringUtils.length(line) > 18 && new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()).equals(StringUtils.substring(line, 0, 10));
	}

	private String extractField(String content, String attributeName, boolean isString) {
		String startIdxTxt = attributeName + (isString ? STRING_SUFFIX_START : INT_SUFFIX_START);
		if (content.contains(startIdxTxt)) {
			int startIdx = content.indexOf(startIdxTxt) + startIdxTxt.length();
			int endIdx = content.indexOf(isString ? STRING_END : INT_END, startIdx);
			return StringUtils.trim(content.substring(startIdx, endIdx));
		}	
		return null;
	}
	
	private boolean isCancelledSubscriber(SubscriberInfo subscriber) {
		return StringUtils.equals(subscriber.getStatus(), "C");
	}
	
	private boolean validateBanMatchesSub(String ban, SubscriberInfo subscriber) {
		return subscriber != null && StringUtils.equals(ban, String.valueOf(subscriber.getBan()));
	}
	
	private MismatchEvent createMismatchEvent(String timestamp, String ban, String phoneNumber, String logLocation, Long lineNumber, String eventDetail) {
		
		MismatchEvent mismatch = new MismatchEvent();
		mismatch.setTimestamp(timestamp);
		mismatch.setBan(ban);
		mismatch.setPhoneNumber(phoneNumber);
		mismatch.setLogLocation(logLocation);
		mismatch.setLineNumber(lineNumber.toString());
		mismatch.setEventDetail(eventDetail);

		return mismatch;
	}

	/*
	private String getEmailSubject(Date date) {
		String today = new SimpleDateFormat("MMM dd, yyyy").format(date.getTime());
		return "Activation Kafka mismatches found (" + today + ")";
	}

	private String getEmailBody(List<MismatchEvent> mismatches, Date date) {

		StringBuffer body = new StringBuffer();
		body.append("The following potential ban/sub mismatches were found in the Kafka logs for the Subscriber Activation flows for <b>");
		body.append(new SimpleDateFormat("MMM dd, yyyy").format(date.getTime()));
		body.append("</b>:<br><br>");
		body.append("<table><th>Timestamp</th><th>Ban</th><th>Phone Number</th><th>Log Location</th><th>Line #</th>");

		for (MismatchEvent mismatch : mismatches) {
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
*/
	
}