package com.telus.cmb.tool.services.log.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

	/**
	 * Method to zip only the folder specified and the non-directory files that is contained in that folder.
	 * We are only using this method to archive old log files - other folders and their files are not needed.
	 * 
	 * @param zipFilePath
	 * @param sourceFolder
	 * @throws IOException
	 */
	public static void zipFolderAndFilesNonRecursively(String zipFilePath, File sourceFolder) throws IOException {
		
		ZipOutputStream zos = null;
		try {
			FileOutputStream fos = new FileOutputStream(zipFilePath);
			zos = new ZipOutputStream(fos);			
			for (File file : sourceFolder.listFiles()) {
				addFileToZip(zos, file);
			}
			zos.closeEntry();
		} finally {
			if (zos != null) {
				zos.close();
			}			
		}
	}
	
	private static void addFileToZip(ZipOutputStream zos, File file) throws IOException {
		
		byte[] buffer = new byte[1024];
		FileInputStream in = null;
		try {
			ZipEntry ze = new ZipEntry(file.getName());
			zos.putNextEntry(ze);	
			in = new FileInputStream(file);	
			int len;
			while ((len = in.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}	
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}
	
	/**
	 * Method to unzip an archived file.
	 * @param archiveFile
	 * @param unzipDirectoryPath
	 * @throws IOException
	 */
	public void unzipFile(String archiveFile, String destinationPath) throws IOException {
		
        File destDir = new File(destinationPath);
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(archiveFile));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }
     
    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
    	
        File destFile = new File(destinationDir, zipEntry.getName());         
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();         
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
         
        return destFile;
    }
		
}
