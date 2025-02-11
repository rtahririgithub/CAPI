/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.domain.shared;


/**
 * @author x113300
 *
 */
public enum Brand {

	TELUS(1);

	private int id;
	
	private Brand(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static Brand fromValue(int id) {
		for (Brand brand : values()) {
			if (brand.id == id) {
				return brand;
			}
		}
		return null;
	}
	
}
