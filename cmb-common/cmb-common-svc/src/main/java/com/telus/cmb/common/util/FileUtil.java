/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * @author Pavel Simonovsky
 *
 */
public class FileUtil {

	public static String readFile(InputStream is) throws IOException {
		InputStreamReader reader = new InputStreamReader(is);
		StringWriter writer = new StringWriter();
		char [] buffer = new char[1024];
		int count = 0;
		while ((count = reader.read(buffer)) != -1) {
			writer.write(buffer, 0, count);
		}
		return writer.toString();
	}
	
	public static String readFile(String filename) {
		File file = new File( filename );
		InputStream in = null;
		try {
			if ( file.exists()) {
				in = new FileInputStream(file);
			} else {
				in = FileUtil.class.getResourceAsStream(filename);
				if ( in==null )
					in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
			}
			
			return readFile( in );
		}
		catch (IOException e ) {
			throw new RuntimeException ( e );
		} finally {
			if ( in!=null ) {
				try {in.close();} catch( IOException e ) {} 
			}
		}
	}
}
