package com.telus.cmb.tool.services.log.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.tool.services.log.config.domain.AppMap;
import com.telus.cmb.tool.services.log.config.domain.app.Application;
import com.telus.cmb.tool.services.log.config.domain.app.Artifact;
import com.telus.cmb.tool.services.log.config.domain.app.Component;
import com.telus.cmb.tool.services.log.config.domain.task.TaskApplication;
import com.telus.cmb.tool.services.log.config.domain.task.TaskComponent;
import com.telus.cmb.tool.services.log.dao.EmtoolsDao;
import com.telus.cmb.tool.services.log.domain.LogFilePaths;
import com.telus.cmb.tool.services.log.domain.LogSearchResult;
import com.telus.cmb.tool.services.log.domain.LogServerInfo;
import com.telus.cmb.tool.services.log.service.EmailService;
import com.telus.cmb.tool.services.log.tasks.notify.EmailContent;
import com.telus.cmb.tool.services.log.tasks.notify.EmailTemplateEnum;
import com.telus.cmb.tool.services.log.utils.EncryptionUtil;

public class CacheRefreshTask extends BaseTask {

	public static final int DEFAULT_RETRY_COUNT = 10;
	public static final long DEFAULT_RETRY_DELAY = 60000;

	private static Logger logger = Logger.getLogger(CacheRefreshTask.class);

	@Autowired
	private EmtoolsDao emtoolsDao;

	@Autowired
	private EmailService emailService;

	@Override
	public void run() {

		TaskCredentials credentials = TaskCredentials.getInstance();
		if (credentials.isInitialized()) {

			try {
				// Cache Refresh tasks should only have one application/component
				TaskApplication taskApplication = taskEnvironment.getTaskApplications().get(0);
				TaskComponent taskComponent = taskApplication.getTaskComponents().get(0);

				// Get the expected count (# of GU nodes) and setup retry count
				Set<String> nodes = getExpectedNodes(taskApplication.getShortname(), taskComponent.getName());
				int retries = getTaskParam("retryCount", DEFAULT_RETRY_COUNT);
				long retryDelay = getTaskParam("retryDelay", DEFAULT_RETRY_DELAY);
				List<LogSearchResult> results = null;
				Set<String> nodesFound = null;
				boolean foundOnAllHosts = false;

				// Execute until no retries left or all results have been found 
				do {
					// Execute search and check if results match all the expected hosts
					results = findSearchResults(credentials, taskApplication, taskComponent);
					nodesFound = getNodesFromResults(results, nodes);
					foundOnAllHosts = nodesFound.size() == nodes.size();
					logger.info("Expecting " + nodes.size() + " results and actual results = " + nodesFound.size());

					// Add some delay for the next retry (if not the last)
					if (retries > 0 && !foundOnAllHosts) {
						Thread.sleep(retryDelay);
					}
				} while (retries-- > 0 && !foundOnAllHosts);
				Map<String, List<LogSearchResult>> resultMap = new HashMap<>();
				resultMap.put(taskComponent.getName(), results);

				// Send the notification		
				EmailContent emailContent = createEmailContent(nodes, nodesFound, foundOnAllHosts, resultMap);
				emailService.sendEmail(taskEnvironment.getNotificationEmails(foundOnAllHosts), EmailTemplateEnum.cacheRefresh, emailContent);
			} catch (Exception e) {
				logger.error("Exception occurred when running the cache refresh monitoring service: " + e.getMessage());
			}
		} else {
			logger.info("Credentials aren't ready, skipping cache refresh task");
		}
	}

	private EmailContent createEmailContent(Set<String> nodes, Set<String> nodesFound, boolean foundOnAllHosts, Map<String, List<LogSearchResult>> resultMap) {		
		EmailContent emailContent = new EmailContent();
		emailContent.setTaskCompleted(foundOnAllHosts);
		emailContent.setNodes(nodes);
		emailContent.setNodesFound(nodesFound);
		emailContent.setResultMap(resultMap);
		return emailContent;
	}

	@Override
	public String getDescription() {
		return "Checks that cache refresh jobs are completed on all Client API GU nodes and sends out a notification.  Will retry " + getTaskParam("retryCount", DEFAULT_RETRY_COUNT) + " times with a "
				+ getTaskParam("retryDelay", DEFAULT_RETRY_DELAY) / 1000 + " second delay between retries.";
	}

	private List<LogSearchResult> findSearchResults(TaskCredentials credentials, TaskApplication taskApplication, TaskComponent taskComponent) throws Exception {

		List<LogSearchResult> results = new ArrayList<LogSearchResult>();
		for (LogFilePaths logFilePaths : filePathConfigRT.getFilePaths(taskEnvironment.getShortname(), taskApplication.getShortname(), taskComponent.getName())) {
			LogServerInfo logServer = logFilePaths.getLogServer();
			String username = credentials.getUsername(logServer.usesUnixLogin());
			results.addAll(logFileService.getSearchResults(logFilePaths.getFilepaths(), logServer, username,
					EncryptionUtil.decryptPassword(username, credentials.getPassword(logServer.usesUnixLogin())), taskComponent.getGrepCriteria(), 
					getDateRangeFilter(taskApplication, taskComponent), 1));
		}

		return results;
	}

	private Set<String> getNodesFromResults(List<LogSearchResult> results, Set<String> nodes) {

		Set<String> nodesFound = new HashSet<String>();
		for (String node : nodes) {
			for (LogSearchResult result : results) {
				logger.debug(result.getFilePath());
				if (result.getFilePath().contains(node)) {
					nodesFound.add(node);
					break;
				}
			}
		}

		return nodesFound;
	}

	private Set<String> getExpectedNodes(String appShortName, String componentName) {

		// First get all the appMaps
		Application application = filePathConfig.getApplication(taskEnvironment.getShortname(), appShortName);
		Component component = application.getComponent(componentName);
		List<AppMap> artifactAppMapList = new ArrayList<AppMap>();
		for (Artifact artifact : component.getArtifacts()) {
			artifactAppMapList.addAll(emtoolsDao.getAppMap(artifact.getAppMapName()));
		}

		// Get all the nodes for the current environment
		Set<String> nodes = new HashSet<String>();
		for (AppMap artifactAppMap : artifactAppMapList) {
			if (StringUtils.equalsIgnoreCase(artifactAppMap.getEnvironment(), taskEnvironment.getShortname())) {
				nodes.add(artifactAppMap.getNode());
			}
		}

		return nodes;
	}

	
//	private Date getCompletedTimestamp(List<LogSearchResult> results) {
//
//		Date completedTimestamp = null;
//		for (LogSearchResult result : results) {
//			for (LineInfo line : result.getResults()) {
//				Date timestamp = getTimeFromLog(line.getLineContent());
//				if (timestamp != null && (completedTimestamp == null || completedTimestamp.before(timestamp))) {
//					completedTimestamp = timestamp;
//				}
//			}
//		}
//
//		return completedTimestamp;
//	}

	
//	private String getEmailSubject(boolean completed) {
//		String today = new SimpleDateFormat("MMM dd, yyyy").format(Calendar.getInstance().getTime());
//		return "CAPI Cache Refresh " + (completed ? "COMPLETED!" : "Incomplete") + " (" + today + ")";
//	}
//
//	private String getEmailBody(boolean completed, Set<String> nodes, Set<String> nodesFound, List<LogSearchResult> results) {
//
//		StringBuffer body = new StringBuffer();
//		body.append("Client API reference cache refresh ");
//
//		if (completed) {
//			body.append("<b><font color='green'><u>completed</u>");
//			Date completedTimestamp = getCompletedTimestamp(results);
//			if (completedTimestamp != null) {
//				body.append(" @ ").append("<big>").append(new SimpleDateFormat("HH:mm:ss").format(completedTimestamp)).append("</big>");
//			}
//			body.append("</font></b>!<br/><br/>Here are the details:<br/>");
//		} else {
//			body.append("was <b><font color='red'>incomplete</font></b>.<br/><br/>Results were missing from these nodes:<br/><ul>");
//			Set<String> missingNodes = nodes;
//			missingNodes.removeAll(nodesFound);
//			for (String missingNode : missingNodes) {
//				body.append("<li><b>").append(missingNode).append("</b></li>");
//			}
//			body.append("</ul><br/>Results found:<br/>");
//		}
//
//		body.append("<style type='text/css'><!--.tab { margin-left: 40px; }--></style><p class='tab'>");
//		Set<String> duplicateCheck = new HashSet<String>();
//		for (LogSearchResult result : results) {
//			String node = getNodeFromFilePath(result.getFilePath());
//			if (!duplicateCheck.contains(node)) {
//				body.append("<b>").append(result.getFilePath()).append("</b><br/>");
//				for (LineInfo lineInfo : result.getResults()) {
//					body.append("<small><i>").append(lineInfo.getLineContent()).append("</i> [Line ").append(lineInfo.getLineNumber()).append("]</small><br/>");
//				}
//				body.append("<br/>");
//				duplicateCheck.add(node);
//			}
//		}
//		body.append("</p>");
//
//		return body.toString();
//	}
	

//	private String getNodeFromFilePath(String filePath) {
//
//		if (filePath.matches(".*logs/.*/applications.*")) {
//			int startIdx = filePath.indexOf("logs/") + "logs/".length();
//			int endIdx = filePath.indexOf("/app", startIdx);
//			return filePath.substring(startIdx, endIdx);
//		}
//
//		return null;
//	}

	
//	private Date getTimeFromLog(String content) {
//
//		try {
//			if (content.length() > 10) {
//				return new SimpleDateFormat("dd MMM yyyy HH:mm:ss").parse(content.substring(0, 20));
//			}
//		} catch (ParseException e) {
//			logger.error("Unable to parse the line " + content + " for the HH:mm:ss time due to error: " + e.getMessage());
//		}
//
//		return null;
//	}

}
