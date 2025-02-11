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

import java.util.Collection;

/**
 * @author Pavel Simonovsky	
 *
 */
public interface ArtifactNotificationRepository {

	Collection<ArtifactNotification> getNotifications();
	
}
