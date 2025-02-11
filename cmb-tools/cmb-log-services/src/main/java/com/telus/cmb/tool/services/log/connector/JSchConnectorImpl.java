package com.telus.cmb.tool.services.log.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Component
public class JSchConnectorImpl implements JSchConnector {

	private static final int JSCH_PORT = 22;
	private Logger logger = Logger.getLogger(JSchConnectorImpl.class);

	public String[] executeCommand(String host, String username, String password, String command) throws Exception {

		String[] results = new String[0];
		Session session = null;
		Channel channel = null;
		try {
			logger.debug("Establishing connection...");
			JSch jsch = new JSch();
			session = jsch.getSession(username, host, JSCH_PORT);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			logger.debug("Opening channel...");
			channel = session.openChannel("exec");
			channel.setInputStream(null);
			logger.debug("Executing command: " + command);
			results = ChannelReader.listStream(channel, channel.getInputStream(), command);
		} finally {
			if (channel != null) {
				logger.debug("Disconnecting channel...");
				channel.disconnect();
			}
			if (session != null) {
				logger.debug("Disconnecting connection...");
				session.disconnect();
			}
		}

		return results;
	}

	public boolean validateConnection(String host, String username, String password) {

		Session session = null;
		Channel channel = null;
		boolean connected = false;
		try {
			logger.debug("Establishing connection...");
			JSch jsch = new JSch();
			session = jsch.getSession(username, host, JSCH_PORT);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			logger.debug("Connected!");
			connected = true;
		} catch (JSchException e) {
			logger.debug(e.getMessage());
		} finally {
			if (channel != null) {
				logger.debug("Disconnecting channel...");
				channel.disconnect();
			}
			if (session != null) {
				logger.debug("Disconnecting connection...");
				session.disconnect();
			}
		}

		return connected;
	}

	public List<String> readFile(String host, String username, String password, String file) {
		
		List<String> contents = new ArrayList<>();
		Session session = null;
		Channel channel = null;
		try {
			logger.debug("Establishing connection...");
			JSch jsch = new JSch();
			session = jsch.getSession(username, host, JSCH_PORT);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			logger.debug("Opening channel...");
			channel = session.openChannel("sftp");
			channel.connect();

			ChannelSftp sftpChannel = (ChannelSftp) channel;
			InputStream inputStream = sftpChannel.get(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                contents.add(line);
            }
		} catch (IOException | JSchException | SftpException e) {
			logger.error("Error occurred when reading file (" + file + "): " + e.getMessage());
		} finally {
			if (channel != null) {
				logger.debug("Disconnecting channel...");
				channel.disconnect();
			}
			if (session != null) {
				logger.debug("Disconnecting connection...");
				session.disconnect();
			}
		}
		return contents;		
	}
	
	public void sftp(String host, String username, String password, String file, OutputStream outputStream) throws Exception {

		Session session = null;
		Channel channel = null;
		try {
			logger.debug("Establishing connection...");
			JSch jsch = new JSch();
			session = jsch.getSession(username, host, JSCH_PORT);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			logger.debug("Opening channel...");
			channel = session.openChannel("sftp");
			channel.connect();

			ChannelSftp sftpChannel = (ChannelSftp) channel;
			InputStream inputStream = sftpChannel.get(file);
			FileCopyUtils.copy(inputStream, outputStream);
		} finally {
			if (channel != null) {
				logger.debug("Disconnecting channel...");
				channel.disconnect();
			}
			if (session != null) {
				logger.debug("Disconnecting connection...");
				session.disconnect();
			}
		}
	}
	
}
