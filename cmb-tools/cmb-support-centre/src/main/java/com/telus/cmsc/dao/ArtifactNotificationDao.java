/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.dao;

import java.util.Collection;

import com.telus.cmsc.domain.artifact.ArtifactNotification;

/**
 * @author Pavel Simonovsky	
 *
 */
public interface ArtifactNotificationDao {

	Collection<ArtifactNotification> getNotifications(String logicalEnvironmentName);
	
}
