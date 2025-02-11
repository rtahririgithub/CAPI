package com.telus.cmsc.domain;

import java.io.Serializable;


/**
 * @author Pavel Simonovsky	
 *
 */

public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public abstract Integer getId();
	
	@Override
	public boolean equals(Object obj) {
		if (getClass().equals(obj.getClass())) {
			BaseEntity other = (BaseEntity) obj;
			if (getId() != null && other.getId() != null) {
				return getId().equals(other.getId());
			}
		}
		return false;
	}
}
