/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.util;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import com.telus.cmsc.domain.user.UserPreferences;
import com.telus.cmsc.domain.user.UserProfile;

/**
 * @author Pavel Simonovsky	
 *
 */
public class UserProfileContextMapper implements UserDetailsContextMapper {

	/* (non-Javadoc)
	 * @see org.springframework.security.ldap.userdetails.UserDetailsContextMapper#mapUserFromContext(org.springframework.ldap.core.DirContextOperations, java.lang.String, java.util.Collection)
	 */
	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
		
		UserProfile profile = new UserProfile(username, "", new ArrayList<GrantedAuthority>());
		profile.setPreferences( new UserPreferences());
		
		profile.setDisplayName(ctx.getStringAttribute("displayName"));
		
		return profile;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.ldap.userdetails.UserDetailsContextMapper#mapUserToContext(org.springframework.security.core.userdetails.UserDetails, org.springframework.ldap.core.DirContextAdapter)
	 */
	@Override
	public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
		throw new UnsupportedOperationException("The operation is not supported");

	}

}
