/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.web.model;

import java.util.Comparator;

/**
 * @author Pavel Simonovsky	
 *
 */
public class LabelComparator implements Comparator<LabelProvider> {

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(LabelProvider o1, LabelProvider o2) {
		
		String l1 = o1 == null ? "" : o1.getLabel();
		String l2 = o2 == null ? "" : o2.getLabel();
		
		return l1.compareTo(l2);
	}
}
