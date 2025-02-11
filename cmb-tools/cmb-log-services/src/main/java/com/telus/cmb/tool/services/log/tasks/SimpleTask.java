package com.telus.cmb.tool.services.log.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.tool.services.log.config.FilePathConfig;
import com.telus.cmb.tool.services.log.config.domain.app.Application;
import com.telus.cmb.tool.services.log.config.domain.task.TaskApplication;
import com.telus.cmb.tool.services.log.config.domain.task.TaskComponent;
import com.telus.cmb.tool.services.log.domain.DateRangeFilter;
import com.telus.cmb.tool.services.log.domain.LogFilePaths;
import com.telus.cmb.tool.services.log.domain.LogSearchResult;
import com.telus.cmb.tool.services.log.domain.LogServerInfo;
import com.telus.cmb.tool.services.log.service.EmailService;
import com.telus.cmb.tool.services.log.tasks.notify.EmailContent;
import com.telus.cmb.tool.services.log.tasks.notify.EmailTemplateEnum;
import com.telus.cmb.tool.services.log.utils.EncryptionUtil;

public class SimpleTask extends BaseTask {

	private FilePathConfig filePathConfig = FilePathConfig.getInstance();
	private static final String DESCRIPTION = "description";
	private static final String EMAIL_SUBJECT = "emailSubject";
	private static final String FREQUENCY = "frequency";
	private static final String TEMPLATE = "emailTemplate";
	private static final String EXCLUDE_SEARCH = "excludeSearch";
	private static final String DEFAULT_DESCRIPTION = "Checks if a particular string occurs {frequency} or more times in the logs and sends out a notification.";
	private static final int DEFAULT_FREQUENCY = 10;

	private static Logger logger = Logger.getLogger(SimpleTask.class);

	@Autowired
	private EmailService emailService;
	
	@Override
	public void run() {

		TaskCredentials credentials = TaskCredentials.getInstance();
		if (credentials.isInitialized()) {
			try {				
				// Execute search and check if results match all the expected hosts
				Map<String, List<LogSearchResult>> resultMap = new HashMap<>();
				Set<String> criteria = new HashSet<>();
				Date searchDate = Calendar.getInstance().getTime();
				for (TaskApplication taskApplication : taskEnvironment.getTaskApplications()) {
					for (TaskComponent taskComponent : taskApplication.getTaskComponents()) {						
						List<LogSearchResult> results = new ArrayList<>();
						List<LogFilePaths> logFilePathList = filePathConfigRT.getFilePaths(taskEnvironment.getShortname(), taskApplication.getShortname(), taskComponent.getName());
						criteria.addAll(taskComponent.getGrepCriteria());
						DateRangeFilter dateFilter = getDateRangeFilter(taskApplication, taskComponent);
						searchDate = dateFilter.getStartDate();
						performRegularFileSearch(logFilePathList, credentials, taskComponent.getGrepCriteria(), dateFilter, results);
						performArchivedFileSearch(logFilePathList, credentials, taskComponent.getGrepCriteria(), dateFilter, results);
						resultMap.put(taskComponent.getName(), results);
					}
				}

				int totalResults = 0;
				int frequency = getTaskParam(FREQUENCY, DEFAULT_FREQUENCY);
				for (List<LogSearchResult> results : resultMap.values()) {
					totalResults += results.size();
				}
				
				if (totalResults >= frequency) {
					EmailContent emailContent = createEmailContent(resultMap, criteria, totalResults, searchDate);
					EmailTemplateEnum template = EmailTemplateEnum.getTemplate(getTaskParam(TEMPLATE, EmailTemplateEnum.simple.name()));
					emailService.sendEmail(taskEnvironment.getNotificationEmails(), template, emailContent);
				} else {
					logger.info("Only found " + totalResults + " result(s) which is under the threshold for notification (" + frequency + ").");
				}
			} catch (Exception e) {
				logger.error("Exception occurred when running the frequency monitoring service: " + e.getMessage());
			}
		} else {
			logger.info("Credentials aren't ready, skipping cache refresh task");
		}
	}

	private void performRegularFileSearch(List<LogFilePaths> logFilePathList, TaskCredentials credentials, Set<String> criteria, DateRangeFilter dateFilter, List<LogSearchResult> results)
			throws Exception {
		for (LogFilePaths logFilePaths : logFilePathList) {
			LogServerInfo logServer = logFilePaths.getLogServer();
			String username = credentials.getUsername(logServer.usesUnixLogin());
			String excludeSearch = getTaskParam(EXCLUDE_SEARCH, null);
			Set<String> searchExcludeList = excludeSearch == null ? null : new HashSet<>(Arrays.asList(excludeSearch.split(";"))); 
			results.addAll(logFileService.getSearchResultsForTask(logFilePaths.getFilepaths(), logServer, username,
					EncryptionUtil.decryptPassword(username, credentials.getPassword(logServer.usesUnixLogin())), criteria, dateFilter, 50, searchExcludeList));
		}
	}

	private void performArchivedFileSearch(List<LogFilePaths> logFilePathList, TaskCredentials credentials, Set<String> criteria, DateRangeFilter dateFilter, List<LogSearchResult> results) {
		// TODO: Download file
		// TODO: Unzip file
		// TODO: Search results
		// TODO: Finally - Cleanup Zip File, unzipped files 
	}
	
	private EmailContent createEmailContent(Map<String, List<LogSearchResult>> resultMap, Set<String> criteria, int totalResults, Date searchDate) {
		EmailContent emailContent = new EmailContent();
		emailContent.setSubjectText(getTaskParam(EMAIL_SUBJECT, ""));
		emailContent.setCriteria(criteria);
		emailContent.setSearchDate(searchDate);
		emailContent.setTotalResults(totalResults);
		emailContent.setResultMap(resultMap);
		return emailContent;
	}
	
	@Override
	public String getDescription() {
		int frequency = getTaskParam(FREQUENCY, DEFAULT_FREQUENCY);
		String description = getTaskParam(DESCRIPTION, DEFAULT_DESCRIPTION).replace("{frequency}", String.valueOf(frequency));
		return description + "  Target applications: " + getTaskApplicationList() + ".";
	}
	
	private String getTaskApplicationList() {
		List<String> applicationList = new ArrayList<String>();
		for (TaskApplication taskApplication : taskEnvironment.getTaskApplications()) {
			Application application = filePathConfig.getApplication(taskEnvironment.getShortname(), taskApplication.getShortname());
			applicationList.add(application.getName());
		}
		return StringUtils.join(applicationList, ",");
	}
		
}
