package com.telus.ait.fwk.datadriven;

import net.serenitybdd.core.Serenity;

/**
 * Created by x166950 on 05/21/2015.
 */
public class Telusity {
	public Telusity() {
	}

	public static Telusity.SessionVariableSetter setSessionVariable(Object key) {
		return new Telusity.SessionVariableSetter(key);
	}

	public static Object sessionVariableCalled(Object key) {
		Object value = Serenity.getCurrentSession().get(key);
		if (value instanceof String) {
			String stringValue = (String) Serenity.getCurrentSession().get(key);
			if ("NULL".equalsIgnoreCase(stringValue)) {
				return null;
			}
		}
		return value;
	}

	public static class SessionVariableSetter {
		final Object key;

		public SessionVariableSetter(Object key) {
			this.key = key;
		}

		public void to(Object value) {
			if(value != null) {
				Serenity.getCurrentSession().put(this.key, value);
			} else {
				Serenity.getCurrentSession().remove(this.key);
			}
		}
	}

}