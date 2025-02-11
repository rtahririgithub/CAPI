package com.telus.cmb.tool.services.log.domain.usage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

public class MonthlyUsage {

	private String id;
	private List<Usage> usageList;

	private static final SimpleDateFormat SDF_KEY = new SimpleDateFormat("yyyy-MM");
	private static final SimpleDateFormat SDF_DESCRIPTION = new SimpleDateFormat("MMMMM yyyy");

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Usage> getUsageList() {
		return usageList;
	}

	public void setUsageList(List<Usage> usageList) {
		this.usageList = usageList;
	}

	public String getDescription() {
		try {
			return SDF_DESCRIPTION.format(SDF_KEY.parse(this.id));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public int getTotalUsage() {
		int totalUsage = 0;
		if (CollectionUtils.isNotEmpty(this.usageList)) {
			for (Usage usage : this.usageList) {
				totalUsage += usage.getVolume();
			}
		}
		return totalUsage;
	}
	
	public int getAverageUsage() {
		if (CollectionUtils.isEmpty(this.usageList)) {
			return 0;
		}
		return getTotalUsage()/this.usageList.size();
	}

}
