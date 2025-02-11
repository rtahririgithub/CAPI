package com.telus.cmb.tool.services.log.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.telus.cmb.tool.services.log.domain.DateRangeFilter;

public class ConnectorUtil {

	private static final int SEARCH_MAX_RESULTS = 50;
	private static final int TASK_MAX_RESULTS = 500;

	public static String createListCommand(List<String> filePaths, String extension) {
		String command = "";
		for (String filePath : filePaths) {
			command += "ls -ldht --full-time " + getFolderPath(filePath) + "*" + extension +"; ";
		}

		return command;
	}

	private static String getFolderPath(String filePath) {
		return filePath.substring(0, filePath.lastIndexOf("/") + 1);
	}

	public static String getCommandForWordCount(String file) {
		return "wc -l " + file + " | cut -d' ' -f1";
	}
	
	public static String getCommandToReadFile(int lineStart, int lineEnd, String file) {
		return "sed -n '" + lineStart + "," + lineEnd + "p' " + file;
	}
	
	public static String createCommandToSearchCriteria(List<String> filePaths, Set<String> criteria, DateRangeFilter dateRangeFilter, int resultLimit, boolean isTask) {
		String regex = createRegex(criteria, dateRangeFilter);
		String command = "";
		for (String filePath : filePaths) {
			command += "grep -aHni --max-count=" + getMaxCount(resultLimit, isTask) + " '" + regex + "' " + filePath + "; ";
		}

		return command;
	}
	
	public static String createCommandToFilterOnDate(List<String> filePaths, String startDate) {
		String command = "";
		for (String filePath : filePaths) {
			command += "grep -aHni --max-count=1 '" + startDate + "' " + filePath + "; ";
		}

		return command;
	}

	public static String createFindCommand(int numResults, List<String> filePaths, String regex, String daysBefore) {
		String command = "";
		for (String filePath : filePaths) {
			command += "find " + filePath + "* " + "-type f -mtime -" + daysBefore + " - print0 | xargs -0 grep -n -i -m " + numResults + regex + ";";
		}

		return command;
	}

	private static int getMaxCount(int resultLimit, boolean isTask) {		
		return isTask ? Math.min(resultLimit, TASK_MAX_RESULTS) : Math.min(resultLimit, SEARCH_MAX_RESULTS);		
	}
	
	private static String createRegex(Set<String> criteria, DateRangeFilter dateRangeFilter) {
		
		String regex = "";
		String timestampStr = getTimestampString(dateRangeFilter);		
		int count = 0;
		for (String criterion : criteria) {
			if (count > 0) {
				regex += "\\|";
			}
			regex += timestampStr + ".*" + criterion;
			count++;
		}

		return regex;
	}

	private static String getTimestampString(DateRangeFilter dateRangeFilter) {		
		if (dateRangeFilter != null && DateUtils.truncatedCompareTo(dateRangeFilter.getEndDate(), dateRangeFilter.getStartDate(), Calendar.DATE) == 0) {
			return new SimpleDateFormat(dateRangeFilter.getDateFormat()).format(dateRangeFilter.getStartDate());
		}
		return "";
	}

	public static String escapeSpecialCharacters(String result) {
		return result.replaceAll("<", "&#60;").replaceAll(">", "&#62;").replaceAll("\t", "    ");
	}
	
	public static boolean hasResults(String[] results) {
		return results != null && results.length > 0 && StringUtils.isNotEmpty(results[0]);
	}
}
