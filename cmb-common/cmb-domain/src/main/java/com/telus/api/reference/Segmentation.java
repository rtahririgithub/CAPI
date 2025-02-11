/**
 * Title:        Segmentation<p>
 * Description:  The Segmentation contains the Segmentation attributes information.<p>
 * Copyright:    Copyright (c) Tsz Chung Tong<p>
 * Company:      Telus Mobility Inc<p>
 * @author Tsz Chung Tong
 * @version 1.0
 */
package com.telus.api.reference;

public interface Segmentation extends Reference {
	
	String getCode();
	String getSegment ();
	String getSubSegment ();
}
