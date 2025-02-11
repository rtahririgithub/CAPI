package com.telus.cmb.tool.services.log.connector;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;

public abstract class ChannelReader {

	private static Logger logger = Logger.getLogger(ChannelReader.class);
	
	/**
	 * Reads InputStream from Channel into a String[] called 'list'
	 * 
	 * @param channel
	 * @param inStream
	 * @param command
	 * @return
	 * @throws IOException
	 * @throws JSchException
	 */
	public static String[] listStream(Channel channel, InputStream inStream, String command) throws IOException, JSchException {
		((ChannelExec) channel).setCommand(command);
		channel.connect();

		byte[] temp = new byte[1024];
		String output = "";
		while (true) {
			while (inStream.available() > 0) {
				int i = inStream.read(temp, 0, 1024);
				if (i < 0) {
					break;
				}
				output = output.concat(new String(temp, 0, i));
			}
			if (channel.isClosed()) {
				if (inStream.available() > 0) {
					continue;
				}
				logger.info("Exit-status: " + channel.getExitStatus());
				break;
			}
			try {
				Thread.sleep(1500);
			} catch (Exception ee) {

			}
		}
		channel.disconnect();

		String[] list = output.split("\n");
		return list;
	}

	/**
	 * Reads InputStream in directly into S.o.println
	 * 
	 * @param channel
	 * @param in
	 * @param command
	 * @throws IOException
	 * @throws JSchException
	 */
	public static void readStream(Channel channel, InputStream in, String command) throws IOException, JSchException {
		((ChannelExec) channel).setCommand(command);
		channel.connect();
		byte[] temp = new byte[1024];
		String output = "";
		while (true) {
			while (in.available() > 0) {
				int i = in.read(temp, 0, 1024);
				if (i < 0) {
					break;
				}
				output = output.concat(new String(temp, 0, i));
			}
			if (channel.isClosed()) {
				logger.debug(output);
				System.out.println(output);
				if (in.available() > 0) {
					continue;
				}
				logger.info("Exit-status: " + channel.getExitStatus());
				break;
			}
			try {
				Thread.sleep(1500);
			} catch (Exception ee) {

			}
		}
		channel.disconnect();
	}
}
