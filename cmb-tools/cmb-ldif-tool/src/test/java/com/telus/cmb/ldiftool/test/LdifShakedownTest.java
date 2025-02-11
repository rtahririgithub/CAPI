package com.telus.cmb.ldiftool.test;

import java.io.IOException;

import javax.naming.NamingException;

import org.testng.annotations.Test;

import com.telus.cmb.ldiftool.LdifShakedown;
import com.telus.cmb.ldiftool.constants.EnvEnum;

public class LdifShakedownTest {

	//	50298 - 2017 Feb Wireless Major
	//	50299 - 2017 Apr Wireless Major
	//	50300 - 2017 Jul Wireless Major
	//	50370 - 2018 Jan Wireless Major
	//	50363 - 2018 Apr Wireless Major	
	
	@Test
	public void shakedown_pra() throws NamingException, IOException {
		LdifShakedown shakedown = new LdifShakedown(EnvEnum.PRA);
		shakedown.shakedown(50370);
	}
	
	@Test
	public void shakedown_prb() throws NamingException, IOException {
		LdifShakedown shakedown = new LdifShakedown(EnvEnum.PRB);
		shakedown.shakedown(50370);
	}
	
	@Test
	public void shakedown_st101a() throws NamingException, IOException {
		LdifShakedown shakedown = new LdifShakedown(EnvEnum.ST101A);
		shakedown.shakedown(50370);
	}
	
	@Test
	public void shakedown_pt168() throws NamingException, IOException {
		LdifShakedown shakedown = new LdifShakedown(EnvEnum.PT168);
		shakedown.shakedown(50363);
	}	
}
