package com.telus.cmb.tool.services.log.config.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RootFolders {

	private List<RootFolder> rootFolders;

	@XmlElement(name = "rootFolder")
	public List<RootFolder> getRootFolders() {
		return rootFolders;
	}

	public void setRootFolders(List<RootFolder> rootFolders) {
		this.rootFolders = rootFolders;
	}

}
