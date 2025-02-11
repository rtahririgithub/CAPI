/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.domain.artifact;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Pavel Simonovsky	
 *
 */
public class ArtifactStatusSummary {

	private int correctCount;
	
	private int incorrectCount;
	
	private int unresponsiveCount;
	
	private int unknownCount;
	
	private int unknownReferenceCount;

	public int getCorrectCount() {
		return correctCount;
	}

	public void setCorrectCount(int correctCount) {
		this.correctCount = correctCount;
	}

	public int getIncorrectCount() {
		return incorrectCount;
	}

	public void setIncorrectCount(int incorrectCount) {
		this.incorrectCount = incorrectCount;
	}

	public int getUnresponsiveCount() {
		return unresponsiveCount;
	}

	public void setUnresponsiveCount(int unresponsiveCount) {
		this.unresponsiveCount = unresponsiveCount;
	}

	public int getUnknownCount() {
		return unknownCount;
	}

	public void setUnknownCount(int unknownCount) {
		this.unknownCount = unknownCount;
	}
	
	public int getUnknownReferenceCount() {
		return unknownReferenceCount;
	}
	
	public void setUnknownReferenceCount(int unknownReferenceCount) {
		this.unknownReferenceCount = unknownReferenceCount;
	}
	
	public void addSummary(ArtifactStatusSummary summary) {
		correctCount += summary.getCorrectCount();
		incorrectCount += summary.getIncorrectCount();
		unresponsiveCount += summary.getUnresponsiveCount();
		unknownCount += summary.getUnknownCount();
		unknownReferenceCount += summary.getUnknownReferenceCount();
	}
	
	public void addStatus(ArtifactInstanceStatus status) {
		switch (status) {
		case INCORRECT_VERSION:
			incorrectCount++;
			break;
		case UNKNOWN_REFERENCE:
			unknownReferenceCount++;
			break;
		case UNRESPONSIVE_INSTANCE:
			unresponsiveCount++;
			break;
		case UNREGISTERED_ARTIFACT:
			unknownCount++;
			break;
		case OK:
			correctCount++;
			break;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ReflectionToStringBuilder(this);
		return builder.toString();
	}
}
