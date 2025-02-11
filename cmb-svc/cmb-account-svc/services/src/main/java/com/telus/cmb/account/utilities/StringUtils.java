package com.telus.cmb.account.utilities;

public class StringUtils {

	public StringUtils() {
	}
	
	public static int compare(Object o1, Object o2)
	{
		if(o1 == o2)
		{
			return 0;
		}
		else if(o1 == null)
		{
			return -1;
		}
		else if(o2 == null)
		{
			return 1;
		}
		else
		{
			return o1.toString().compareToIgnoreCase(o2.toString());
		}
	}
}
