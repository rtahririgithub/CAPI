package com.telus.cmb.ldiftool.test;

import java.io.IOException;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.util.Assert;
import org.testng.annotations.Test;

import com.telus.cmb.ldiftool.RAPiDLdifExtractor;
import com.telus.cmb.ldiftool.constants.EnvEnum;

public class RAPiDLdifExtractorTest {

	//	50298 - 2017 Feb Wireless Major
	//	50299 - 2017 Apr Wireless Major
	//	50300 - 2017 Jul Wireless Major
	//	50370 - 2018 Jan Wireless Major
	//	50363 - 2018 Apr Wireless Major	
	private static final int APR_WLS_MAJOR_2018 = 50363;
	
	@Test
	public void getLdifBuildFilesTest() throws NamingException, IOException {
		RAPiDLdifExtractor rapidLdifExtractor = new RAPiDLdifExtractor(APR_WLS_MAJOR_2018);
		List<String> buildFileList = rapidLdifExtractor.getLdifBuildFiles(EnvEnum.PT168.getName(), true);
		Assert.notEmpty(buildFileList);
		for (String buildFile: buildFileList) {
			System.out.println(buildFile);
		}
	}	
}
