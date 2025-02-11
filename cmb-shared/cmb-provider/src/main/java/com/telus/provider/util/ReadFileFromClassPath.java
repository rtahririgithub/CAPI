package com.telus.provider.util;

import java.io.InputStream;

public class ReadFileFromClassPath {
	public InputStream openFile(String fileName) {
		return this.getClass().getResourceAsStream(fileName);
	}
}
