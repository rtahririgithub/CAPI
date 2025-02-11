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

/**
 * @author Pavel Simonovsky	
 *
 */
public class ArtifactOverallStatus {

	private int correctCount;
	
	private int incorrectCount;
	
	private  int unresponsiveCount;
	
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

	public int getUnknownReferenceCount() {
		return unknownReferenceCount;
	}

	public void setUnknownReferenceCount(int unknownReferenceCount) {
		this.unknownReferenceCount = unknownReferenceCount;
	}

}
