package com.telus.cmb.testsuite.shakedown;

import org.junit.Test;

public class VersionTest {

	@Test
	public void testST101() throws Throwable {
		ST101Test test = new ST101Test();
		test.testAllVersions();
	}
	
	@Test
	public void testPT148() throws Throwable {
		PT148Test test = new PT148Test();
		test.testAllVersions();
	}
}
