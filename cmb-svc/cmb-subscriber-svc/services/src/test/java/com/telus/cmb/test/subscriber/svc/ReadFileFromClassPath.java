package com.telus.cmb.test.subscriber.svc;

import java.io.InputStream;

public class ReadFileFromClassPath {
	public InputStream openFile(String fileName) {
		return this.getClass().getResourceAsStream(fileName);
	}
}
