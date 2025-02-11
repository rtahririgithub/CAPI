package com.telus.cmb.tool.services.log.domain.usage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperationUsage {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy");
	private static final int IDX_OPERATION_NAME = 8;
	private static final int IDX_START_TIME = 1;
	private static final int IDX_TOTAL_INVOCATIONS = 5;

	private String operationName;
	private Map<String, Usage> usageMap;

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public static String getOperationName(String content) {
		String[] columns = content.split("\\|");
		return columns[IDX_OPERATION_NAME];
	}

	public Map<String, Usage> getUsageMap() {
		return usageMap;
	}

	public void setUsageMap(Map<String, Usage> usageMap) {
		this.usageMap = usageMap;
	}

	public void parseContent(String content) throws ParseException {
		String[] columns = content.split("\\|");
		initalize(columns[IDX_OPERATION_NAME]);
		addVolumeToUsage(getDateFromStartTime(columns[IDX_START_TIME]), Integer.parseInt(columns[IDX_TOTAL_INVOCATIONS]));
	}

	private void initalize(String name) {
		if (this.operationName == null) {
			this.operationName = name;
		}
		if (this.usageMap == null) {
			this.usageMap = new HashMap<>();
		}
	}

	private void addVolumeToUsage(String startDate, int volume) throws ParseException {
		Usage usage = this.usageMap.get(startDate);
		if (usage == null) {
			usage = new Usage();
			usage.setDate(SDF.parse(startDate));
		}
		usage.setVolume(usage.getVolume() + volume);
		this.usageMap.put(startDate, usage);
	}

	private String getDateFromStartTime(String startTime) {
		return startTime.substring(0, startTime.indexOf(" "));
	}

	public List<Usage> getUsageList() {
		List<Usage> usageList = new ArrayList<>();
		if (this.usageMap != null) {
			usageList = Arrays.asList(this.usageMap.values().toArray(new Usage[0]));
		}
		Collections.sort(usageList);
		return usageList;
	}

}
