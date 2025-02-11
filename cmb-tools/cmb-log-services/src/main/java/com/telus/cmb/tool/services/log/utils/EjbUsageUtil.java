package com.telus.cmb.tool.services.log.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;

import com.telus.cmb.tool.services.log.domain.FileInfo;
import com.telus.cmb.tool.services.log.domain.ListFileResult;
import com.telus.cmb.tool.services.log.domain.usage.OperationUsage;

public class EjbUsageUtil {

	public static final String PARTIAL_SUFFIX = "_partial";
	
	private static final SimpleDateFormat SDF_KEY = new SimpleDateFormat("yyyy-MM");

	private static final String ACCOUNT_EJB_NAME = "cmb-account-svc";
	private static final String SUBSCRIBER_EJB_NAME = "cmb-subscriber-svc";
	private static final String REFERENCE_EJB_NAME = "cmb-reference-svc";
	private static final String ACCOUNT_PREFIX = "acc";
	private static final String SUBSCRIBER_PREFIX = "sub";
	private static final String REFERENCE_PREFIX = "ref";
	private static final String DOMAIN_REGEX = "Management";
	private static final String HEADER_REGEX = "TotalNumberOfInvocations";
	private static final int LATEST_MONTHS = 3;

	public static String getKey(String year, String month) {
		return year + "-" + month;
	}

	public static String getKey(int year, int month) {
		return year + "-" + (month < 10 ? "0" : "") + month;
	}
	
	public static String getYearFromKey(String key) {
		return key.split("-")[0];
	}
	
	public static String getMonthFromKey(String key) {
		return key.split("-")[1];
	}

	public static Date parseKeyAsDate(String id) throws ParseException {
		return SDF_KEY.parse(id);
	}

	public static boolean validateKey(String year, String month) {
		if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month)) {
			LocalDate now = LocalDate.now();
			LocalDate keyDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
			Period period = Period.between(now, keyDate);
			// Cannot load logs that don't exist, cannot be in the future or too far in the past
			return now.isAfter(keyDate) && period.getDays() < 365;
		}
		return false;
	}

	public static boolean isCurrentMonth(String key) {
		return LocalDate.now().getYear() == Integer.parseInt(getYearFromKey(key)) && LocalDate.now().getMonthValue() == Integer.parseInt(getMonthFromKey(key));
	}
	
	public static boolean isMonthComplete(String key) {
		if (LocalDate.now().getYear() < Integer.parseInt(getYearFromKey(key))) {
			return false;
		} else if (LocalDate.now().getYear() == Integer.parseInt(getYearFromKey(key))) {
			return LocalDate.now().getMonthValue() > Integer.parseInt(getMonthFromKey(key));
		}
		return true;
	}
	
	public static List<String> getLatestKeys() {
		List<String> keys = new ArrayList<>();
		LocalDate today = LocalDate.now();
		// TODO: Let's start with the previous month for now, haven't implemented partial update yet
		for (int i = 1; i <= LATEST_MONTHS; i++) {
			today.minusMonths(i);
			keys.add(getKey(today.getYear(), today.getMonthValue()));	
		}
		return keys;
	}

	public static List<String> getPerfFileList(String id, List<ListFileResult> fileLists) {
		List<String> perfFiles = new ArrayList<>();
		for (ListFileResult fileList : fileLists) {
			for (FileInfo file : fileList.getFileInfoList()) {
				if (file.getFileName().matches(".*perfstats_.*" + id + ".*.txt")) {
					perfFiles.add(fileList.getFolder() + "/" + file.getFileName());
				}
			}
		}
		return perfFiles;
	}

	public static String getTempLogFilename(String id, String filePath) {
		int start = filePath.indexOf(DOMAIN_REGEX) + DOMAIN_REGEX.length();
		String domain = filePath.substring(start, filePath.indexOf("/", start));
		String logname = filePath.substring(filePath.lastIndexOf("/") + 1);
		return getEjbShortname(filePath) + domain + "_" + logname;
	}

	private static String getEjbShortname(String filepath) {

		if (StringUtils.contains(filepath, ACCOUNT_EJB_NAME)) {
			return ACCOUNT_PREFIX;
		} else if (StringUtils.contains(filepath, SUBSCRIBER_EJB_NAME)) {
			return SUBSCRIBER_PREFIX;
		} else if (StringUtils.contains(filepath, REFERENCE_EJB_NAME)) {
			return REFERENCE_PREFIX;
		}

		return "";
	}

	public static String getTempFoldername(String key) {
		return isCurrentMonth(key) ? key + PARTIAL_SUFFIX : key;
	}		

	public static int getDayOfMonthFromPerfLogFile(String filename, String key) {
		return Integer.parseInt(filename.substring(filename.indexOf(key) + key.length() + 1, filename.lastIndexOf('.')));
	}
	
	public static void getOperationUsagesFromFile(Map<String, OperationUsage> usageMap, File file) {

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(HEADER_REGEX)) {
					continue;
				}
				String operationName = OperationUsage.getOperationName(line);
				OperationUsage usage = usageMap.get(operationName);
				if (usage == null) {
					usage = new OperationUsage();
				}
				usage.parseContent(line);
				usageMap.put(operationName, usage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
			}
		}
	}

}
