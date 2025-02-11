package com.telus.cmb.tool.services.log.domain.usage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.telus.cmb.tool.services.log.utils.EjbUsageUtil;

public class UsageCache implements Comparable<UsageCache> {

	private String id;
	private boolean complete;
	private String lastDate;
	private List<OperationUsage> operationUsageList;
	
	private static final SimpleDateFormat SDF_DESCRIPTION = new SimpleDateFormat("MMMMM yyyy");
	private static final SimpleDateFormat SDF_LAST_DATE = new SimpleDateFormat("MMM dd");

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public List<OperationUsage> getOperationUsageList() {
		return operationUsageList;
	}

	public void setOperationUsageList(List<OperationUsage> operationUsageList) {
		this.operationUsageList = operationUsageList;
	}
	
	public String getDescription() {
		try {
			return SDF_DESCRIPTION.format(EjbUsageUtil.parseKeyAsDate(id));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public void setLastDate(Date lastDate) {
		if (!complete) {
			this.lastDate = SDF_LAST_DATE.format(lastDate);
		}
	}

	@Override
	public int compareTo(UsageCache o) {
		return this.id.compareTo(o.getId());
	}
}
