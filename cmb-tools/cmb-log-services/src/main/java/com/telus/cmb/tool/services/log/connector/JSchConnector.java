package com.telus.cmb.tool.services.log.connector;

import java.io.OutputStream;
import java.util.List;

public interface JSchConnector {

	public String[] executeCommand(String host, String username, String password, String command) throws Exception;
	
	public boolean validateConnection(String host, String username, String password);

	public void sftp(String host, String username, String password, String file, OutputStream outputStream) throws Exception;

	public List<String> readFile(String host, String username, String password, String file);
}
