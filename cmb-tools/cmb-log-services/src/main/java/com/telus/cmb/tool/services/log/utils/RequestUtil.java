package com.telus.cmb.tool.services.log.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.telus.cmb.tool.services.log.domain.DateRangeFilter;

public class RequestUtil {

	public static final int DEFAULT_SEARCH_LIMIT = 5;	
	public static final int DEFAULT_NUM_LINES_BEFORE = 100;
	public static final int DEFAULT_NUM_LINES_AFTER = 100;
	
	private static final SimpleDateFormat DATE_SELECT_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
	private static final String DEFAULT_DATE_FORMAT = "dd MMM yyyy";
	
	public static Set<String> getSearchCriteria(HttpServletRequest request) {
		Set<String> criteria = new HashSet<String>();
		for (int n = 1; n < 4; n++) {
			String criterion = request.getParameter("search_crit" + n);
			if (criterion != null && !criterion.trim().equals("")) {
				criteria.add(criterion);
			}
		}
		return criteria;
	}
	
	public static int getSearchLimit(HttpServletRequest request) {
		String numResults = request.getParameter("numResults");
		return (numResults != null && !numResults.trim().equals("")) ? Integer.parseInt(numResults) : DEFAULT_SEARCH_LIMIT;
	}
	 
	public static int getReadNumLinesBefore(HttpServletRequest request) {
		String linesBefore = request.getParameter("linesBefore");
		return (linesBefore != null && !linesBefore.trim().equals("")) ? Integer.parseInt(linesBefore) : DEFAULT_NUM_LINES_BEFORE;
	}
	
	public static int getReadNumLinesAfter(HttpServletRequest request) {
		String linesAfter = request.getParameter("linesAfter");
		return (linesAfter != null && !linesAfter.trim().equals("")) ? Integer.parseInt(linesAfter) : DEFAULT_NUM_LINES_AFTER;
	}
	
	public static DateRangeFilter getDateRange(HttpServletRequest request, String overrideDateFormat) {
		String[] dateRange = request.getParameter("dateSelect").split("-");
		if (dateRange.length < 2) {
			return null;
		}
		try {
			DateRangeFilter dateRangeFilter = new DateRangeFilter();
			dateRangeFilter.setStartDate(DATE_SELECT_FORMAT.parse(dateRange[0].trim()));
			dateRangeFilter.setEndDate(DATE_SELECT_FORMAT.parse(dateRange[1].trim()));
			if (StringUtils.isBlank(overrideDateFormat)) {
				dateRangeFilter.setDateFormat(DEFAULT_DATE_FORMAT);				
			} else {
				dateRangeFilter.setDateFormat(overrideDateFormat);
			}
			return dateRangeFilter;
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date getDate(HttpServletRequest request) {
		String dateSelect = request.getParameter("dateSelect");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			return sdf.parse(dateSelect.trim());
		} catch (ParseException e) {
			return null;
		}
	}
	
}
