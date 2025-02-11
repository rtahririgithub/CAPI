package com.telus.cmb.tool.logServices.test.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.tool.services.log.config.FilePathConfig;
import com.telus.cmb.tool.services.log.domain.FileInfo;
import com.telus.cmb.tool.services.log.domain.ListFileResult;
import com.telus.cmb.tool.services.log.domain.LogFilePaths;
import com.telus.cmb.tool.services.log.domain.LogServerInfo;
import com.telus.cmb.tool.services.log.service.LogFileService;
import com.telus.cmb.tool.services.log.utils.EncryptionUtil;

@Test
@ContextConfiguration("classpath:application-context-test.xml")
public class ProviderUsageTest extends AbstractTestNGSpringContextTests {

	FilePathConfig filePathConfig = FilePathConfig.getInstance();

	@Autowired
	LogFileService logFileService;
	
	@Test(enabled = false)
	public void get_provider_usage_test() throws Exception {
		
		// Enter your credentials here (use the EncryptionUtil encoding method for your passwords)		
		// --------------------------------------------------------
		// ======> DON'T check in your passwords!!!!
		// --------------------------------------------------------
		String username = "x162017";		
		String unixEncodedPassword = "J7xj3j8Mk/6vMNLqQr0IsQ==";
								
		System.out.println("Running provider usage test...");
		String unixPassword = EncryptionUtil.decryptPassword(username, unixEncodedPassword);
		String environment = "prb";
		String application = "capiejbs";
		String component = "CMB Account EJB";
		String date = "2020-03-31";
		String operationKey = "UsageDaoImpl";
		
		List<LogFilePaths> logFilePaths = filePathConfig.getFilePaths(environment, application, component);
		LogServerInfo logServer = null;
		List<ListFileResult> fileLists = new ArrayList<>();		
		for (LogFilePaths logFilePath : logFilePaths) {
			logServer = logFilePath.getLogServer();
			fileLists.addAll(logFileService.getFilenames(logFilePath.getFilepaths(), logFilePath.getLogServer(), username, unixPassword, ".txt"));
		}
		
		List<String> perfFiles = new ArrayList<>();
		for (ListFileResult fileList : fileLists) {
			for (FileInfo file : fileList.getFileInfoList()) {
				if (file.getFileName().matches(".*perfstats_.*" + date + ".*.txt")) {
					perfFiles.add(fileList.getFolder() + "/" + file.getFileName());
				}
			}
		}
		
		Map<String, Map<String, Integer>> opDateUsageMap = new HashMap<>();
		for (String perfFile : perfFiles) {
			List<String> contents = logFileService.readLogFile(logServer, username, unixPassword, perfFile);
			String fileDate = perfFile.split("_")[2];
			fileDate = fileDate.substring(0, fileDate.indexOf('.'));
			for (String line : contents) {
				if (line.matches(".*" + operationKey + ".*") && !line.contains("TotalNumberOfInvocations")) {
					String[] row = line.split("\\|");
					String operation = row[8];
					int opCount = Integer.parseInt(row[5]);
					Map<String, Integer> dateUsageMap = opDateUsageMap.get(operation);
					if (dateUsageMap == null) {
						dateUsageMap = new HashMap<>();
					}
					Integer count = dateUsageMap.get(fileDate);
					count = (count == null) ? opCount : count + opCount;
					dateUsageMap.put(fileDate, count);
					opDateUsageMap.put(operation, dateUsageMap);
				}
//				if (line.matches(".*\\.UsageDaoImpl.*")) {
//					System.out.println(line);
//				}
				
			}
		}
		
		for (String operationName : opDateUsageMap.keySet()) {
			Map<String, Integer> usageMap = opDateUsageMap.get(operationName);
			for (String fileDate : usageMap.keySet()) {
				System.out.println(operationName + "|" + usageMap.get(fileDate) + "|" + fileDate);	
			}
		}
		
	}
	
}
