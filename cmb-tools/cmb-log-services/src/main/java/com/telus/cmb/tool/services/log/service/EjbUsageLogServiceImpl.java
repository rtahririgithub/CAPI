package com.telus.cmb.tool.services.log.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import com.telus.cmb.tool.services.log.config.FilePathConfig;
import com.telus.cmb.tool.services.log.domain.ListFileResult;
import com.telus.cmb.tool.services.log.domain.LogFilePaths;
import com.telus.cmb.tool.services.log.utils.EjbUsageUtil;
import com.telus.cmb.tool.services.log.utils.ZipUtils;

@Service	
public class EjbUsageLogServiceImpl implements EjbUsageLogService {

	private FilePathConfig filePathConfig = FilePathConfig.getInstance();
	private Logger logger = Logger.getLogger(EjbUsageLogServiceImpl.class);
	private LogFilePaths ejbLogFiles;
	
	@Value("${temp.root}")
	private String tempFolderPath;
	
	@Value("${temp.ejb.folder}")
	private String ejbLogFolderName;	
	
	@Autowired
	private LogFileService logFileService;
	
	private static final String[] ENVIRONMENTS = new String[] { "pra", "prb" };
	private static final String[] APPLICATIONS = new String[] { "capiejbs" };
	private static final String[] COMPONENTS = new String[] { "CMB Account EJB", "CMB Subscriber EJB" }; // exclude , "CMB Reference EJB" for now

	@PostConstruct
	public void initializeEjbFiles() {
		loadEjbLogFiles();
	}

	private void loadEjbLogFiles() {
		ejbLogFiles = new LogFilePaths();
		for (String environment : ENVIRONMENTS) {
			for (String application : APPLICATIONS) {
				for (String component : COMPONENTS) {
					List<LogFilePaths> filePaths = filePathConfig.getFilePaths(environment, application, component);
					if (filePaths != null) {
						updateEjbLogFiles(filePaths);
					}
				}
			}
		}
	}

	private void updateEjbLogFiles(List<LogFilePaths> filePaths) {
		for (LogFilePaths filePath : filePaths) {
			if (ejbLogFiles.getLogServer() == null) {
				ejbLogFiles.setLogServer(filePath.getLogServer());
			}
			if (ejbLogFiles.getFilepaths() == null) {
				ejbLogFiles.setFilepaths(new ArrayList<String>());
			}
			ejbLogFiles.getFilepaths().addAll(filePath.getFilepaths());
		}
	}
	
	/**
	 * Method to download EJB performance monitoring log files to a local folder.  If the month that's being update is the current month, then a partial folder will be created
	 * until the month rolls over.  When the logs are updated in the next month, this partial folder will be cleaned up (deleted).  The method returns the local folder as a File object.  
	 * 
	 * @param key
	 * @param user
	 * @param password
	 * @param localFolder
	 * @return
	 */
	@Override
	public File updateEjbUsageLogFiles(String key, String user, String password) {
		
		File localFolder = new File(getLocalEjbLogFolderPath(key));
		try {
			if (localFolder.exists()) {
				// If it's still within the month, only download the missing dates
				if (StringUtils.contains(localFolder.getCanonicalPath(), EjbUsageUtil.PARTIAL_SUFFIX)) {
					// Find the last log file
					int lastDay = 1;
					for (String localFile : localFolder.list()) {
						int day = EjbUsageUtil.getDayOfMonthFromPerfLogFile(localFile, key);
						lastDay = day > lastDay ? day : lastDay;
					}
					
					for (String perfFile : findEjbPerfFilepathList(key, user, password)) {
						// Only download files after the last date					
						if (EjbUsageUtil.getDayOfMonthFromPerfLogFile(perfFile, key) >= lastDay) {
							downloadFile(key, perfFile, user, password);
						}
					}
				}
			} else {		
				// If temporary folder for the month doesn't exist, download files
				boolean folderCreated = localFolder.mkdir();
				if (folderCreated) {
					for (String perfFile : findEjbPerfFilepathList(key, user, password)) {
						downloadFile(key, perfFile, user, password);
					}
					cleanIncompleteFolder(localFolder.getCanonicalPath());
				} else {
					logger.info("Unable to create temporary folder for: " + localFolder.getPath());
				}
			}
		} catch (IOException e) {
			logger.error("Error loading up local folder path: " + e.getMessage());
		}
		
		return localFolder;
	}

	/**
	 * Method to archive old folders past a certain month.
	 */
	@Override
	public void archiveOldFolders(int numOfMonths) {

		LocalDate archiveDate = LocalDate.now().minusMonths(numOfMonths);
		File rootFolder = new File(getLocalEjbLogFolderPath());
		try {
			for (File folder : rootFolder.listFiles()) {
				if (folder.isDirectory() && folder.getName().length() > 6) {
					String key = folder.getName().substring(0, 7);
					LocalDate folderDate = LocalDate.of(Integer.parseInt(EjbUsageUtil.getYearFromKey(key)), Month.of(Integer.parseInt(EjbUsageUtil.getMonthFromKey(key))), 1);
					if (folderDate.isBefore(archiveDate)) {
						//Archive then delete
						if (archiveFolder(folder)) {						
							for (File logFile : folder.listFiles()) {
								logFile.delete();
							}
							folder.delete();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean archiveFolder(File folder) {

		String zipFile = getLocalEjbLogFolderPath() + "/" + folder.getName() + ".zip";
		logger.info("Archiving " + zipFile + " ...");
		try {
			ZipUtils.zipFolderAndFilesNonRecursively(zipFile, folder);
		} catch (Exception e) {
			logger.error("Error occurred when trying to archive " + folder.getName() + ": " + e.getMessage());
			return false;
		} 
		
		return true;
	}

	private void cleanIncompleteFolder(String localFolderPath) {
		
		// If the new folder isn't a partial folder, check if there was one before
		if (!localFolderPath.contains(EjbUsageUtil.PARTIAL_SUFFIX)) {
			String localPartialPath = localFolderPath + EjbUsageUtil.PARTIAL_SUFFIX;
			File localPartialFolder = new File(localPartialPath);
			// If there was a previous partial folder, clean it up
			if (localPartialFolder.exists()) {
				logger.info("Cleaning up partial folder (full month is available now): " + localPartialPath);
				for (File partialFile : localPartialFolder.listFiles()) {
					partialFile.delete();
				}
				localPartialFolder.delete();
			}
		}
	}
	
	private List<String> findEjbPerfFilepathList(String key, String user, String password) {
		
		List<ListFileResult> fileLists = new ArrayList<>();
		try {
			fileLists.addAll(logFileService.getFilenames(ejbLogFiles.getFilepaths(), ejbLogFiles.getLogServer(), user, password, ".txt"));
		} catch (Exception e) {
			logger.error("Unable to find performance monitoring logs for: " + key);
		}
		
		return EjbUsageUtil.getPerfFileList(key, fileLists);
	}

	private void downloadFile(String key, String filePath, String user, String password) {
		try {
			File logFile = new File(getLocalEjbLogFolderPath(key) + "/" + EjbUsageUtil.getTempLogFilename(key, filePath));
			logFile.createNewFile();
			logFileService.downloadFile(ejbLogFiles.getLogServer(), user, password, filePath, new FileOutputStream(logFile));
		} catch (Exception e) {
			logger.error("Unable to download file: " + filePath);
		}
	}

	private String getLocalEjbLogFolderPath() {
		return tempFolderPath + ejbLogFolderName;
	}
	
	private String getLocalEjbLogFolderPath(String key) {
		return getLocalEjbLogFolderPath() + "/" + EjbUsageUtil.getTempFoldername(key);
	}
	
}
