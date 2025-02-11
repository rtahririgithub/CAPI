package com.telus.cmb.tool.services.log.tasks;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.threeten.bp.LocalDate;

import com.telus.cmb.tool.services.log.service.EjbUsageLogService;
import com.telus.cmb.tool.services.log.utils.EjbUsageUtil;
import com.telus.cmb.tool.services.log.utils.EncryptionUtil;

public class EjbUsageCacheTask extends BaseTask {

	@Autowired
	private EjbUsageLogService ejbUsageLogService;

	private static Logger logger = Logger.getLogger(EjbUsageCacheTask.class);	
	private static final int DEFAULT_ARCHIVE_MONTHS = 6;

	@Value("${temp.root}")
	private String tempFolderLocation;
	
	@Override
	public void run() {

		TaskCredentials credentials = TaskCredentials.getInstance();
		if (credentials.isPRInitialized()) {
			// Download the logs for last month
			String username = credentials.getUnixUsername();
			LocalDate lastMonth = LocalDate.now().minusMonths(1);
			String key = EjbUsageUtil.getKey(lastMonth.getYear(), lastMonth.getMonthValue());
			ejbUsageLogService.updateEjbUsageLogFiles(key, username, EncryptionUtil.decryptPassword(username, credentials.getPassword(true)));
			ejbUsageLogService.archiveOldFolders(getTaskParam("archiveMonths", DEFAULT_ARCHIVE_MONTHS));			
		} else {
			logger.info("Credentials aren't ready, skipping cache refresh task");
		}
	}
	
	@Override
	public String getDescription() {
		return "Downloads the Client API EJB performance monitoring log files to local storage so statistics are readily available (executed "
				+ "at the beginning of each month).  The task also auto-archives very old log files older than " 
				+ getTaskParam("archiveMonths", DEFAULT_ARCHIVE_MONTHS) + " months.";
	}
	
}
