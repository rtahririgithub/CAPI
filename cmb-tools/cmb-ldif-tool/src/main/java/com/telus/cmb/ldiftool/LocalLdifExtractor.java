package com.telus.cmb.ldiftool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.telus.cmb.ldiftool.utilities.LdifUtilities;

public class LocalLdifExtractor implements LdifSourceExtractor {

	private static Logger logger = Logger.getLogger(LocalLdifExtractor.class);
		
	@Override
	public List<String> getLdifBuildFiles(String environment, boolean saveLdifIndicator) {
		File file = new File(LdifShakedown.class.getResource("/ldif-list.txt").getFile());
		List<String> ldifList = new ArrayList<String>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			String ldifPath;
			while (scanner.hasNextLine()) {
				ldifPath = scanner.nextLine();
				if (!StringUtils.isEmpty(ldifPath)) {
					if (saveLdifIndicator) {
						ldifList.add(LdifUtilities.saveLdifFile(environment, ldifPath));	
					} else {
						ldifList.add(LdifUtilities.getLdifUrl(environment, ldifPath));					
					}
				}
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		return ldifList;
	}

}
