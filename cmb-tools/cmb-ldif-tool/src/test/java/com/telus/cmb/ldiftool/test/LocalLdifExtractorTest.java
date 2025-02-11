package com.telus.cmb.ldiftool.test;

import java.io.IOException;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.util.Assert;
import org.testng.annotations.Test;

import com.telus.cmb.ldiftool.LocalLdifExtractor;
import com.telus.cmb.ldiftool.constants.EnvEnum;

public class LocalLdifExtractorTest {
	
	@Test
	public void getLdifBuildFilesTest() throws NamingException, IOException {
		LocalLdifExtractor localLdifExtractor = new LocalLdifExtractor();
		List<String> buildFileList = localLdifExtractor.getLdifBuildFiles(EnvEnum.PT168.getName(), true);
		Assert.notEmpty(buildFileList);
		for (String buildFile: buildFileList) {
			System.out.println(buildFile);
		}
	}	
}
