package com.telus.cmb.common.perfmon;

public class RegexpTest {

	public static void main(String[] args) {
		String src = "/logs/%domainName%/";
		System.out.println(src.replaceAll("%domainName%", "AA_bb"));
	}
}
