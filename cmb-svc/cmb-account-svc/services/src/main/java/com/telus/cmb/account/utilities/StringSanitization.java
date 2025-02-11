package com.telus.cmb.account.utilities;

import com.telus.cmb.common.util.AttributeTranslator;

public class StringSanitization {

	public static String sanitize(String input) {
		String output = input;
		output = AttributeTranslator.replaceString(output.trim().toUpperCase()," ","");
		output = AttributeTranslator.replaceString(output,",","");
		output = AttributeTranslator.replaceString(output,".","");
		output = AttributeTranslator.replaceString(output,"-","");
		output = AttributeTranslator.replaceString(output,"'","");
		output = AttributeTranslator.replaceString(output,"/","");
		output = AttributeTranslator.replaceString(output,"(","");
		output = AttributeTranslator.replaceString(output,")","");
		
		return output;
	}
}
