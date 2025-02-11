package com.telus.cmb.tool.services.log.tasks.notify;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.telus.cmb.tool.services.log.domain.LogSearchResult;
import com.telus.cmb.tool.services.log.domain.task.MismatchEvent;

public class EmailContent {

	// Simple task fields
	private Map<String, List<LogSearchResult>> resultMap;
	private Set<String> criteria;
	private int totalResults;
	private String subjectText;

	// Cache Refresh task fields
	private boolean taskCompleted;
	private Set<String> nodes;
	private Set<String> nodesFound;

	// Welcome Email task fields
	private List<MismatchEvent> mismatches;
	private Date searchDate;

	public Map<String, List<LogSearchResult>> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, List<LogSearchResult>> resultMap) {
		this.resultMap = resultMap;
	}

	public Set<String> getCriteria() {
		return criteria;
	}

	public void setCriteria(Set<String> criteria) {
		this.criteria = criteria;
	}

	public int getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

	public String getSubjectText() {
		return subjectText;
	}

	public void setSubjectText(String subjectText) {
		this.subjectText = subjectText;
	}

	public boolean isTaskCompleted() {
		return taskCompleted;
	}

	public void setTaskCompleted(boolean taskCompleted) {
		this.taskCompleted = taskCompleted;
	}

	public Set<String> getNodes() {
		return nodes;
	}

	public void setNodes(Set<String> nodes) {
		this.nodes = nodes;
	}

	public Set<String> getNodesFound() {
		return nodesFound;
	}

	public void setNodesFound(Set<String> nodesFound) {
		this.nodesFound = nodesFound;
	}

	public List<MismatchEvent> getMismatches() {
		return mismatches;
	}

	public void setMismatches(List<MismatchEvent> mismatches) {
		this.mismatches = mismatches;
	}

	public Date getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

}
