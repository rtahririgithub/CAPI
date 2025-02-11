package com.telus.cmb.tool.services.log.domain.usage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Usage implements Comparable<Usage> {

	private Date date;
	private int volume;

	private static final SimpleDateFormat SDF_DISPLAY = new SimpleDateFormat("MMMMM dd, yyyy");

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public String getDateDisplay() {
		return SDF_DISPLAY.format(this.date);
	}

	@Override
	public int compareTo(Usage o) {		
		return this.date.compareTo(o.getDate());
	}
}
