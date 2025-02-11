package com.telus.cmb.tool.services.log.config.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AppMapRepo {

	private List<AppMap> appMapList;

	@XmlElement(name = "appMap")
	public List<AppMap> getAppMapList() {
		return appMapList;
	}

	public void setAppMapList(List<AppMap> appMapList) {
		this.appMapList = appMapList;
	}

}
