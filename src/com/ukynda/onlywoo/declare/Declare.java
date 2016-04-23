package com.ukynda.onlywoo.declare;


import android.app.Application;

public class Declare extends Application {
	private  boolean m_power = false;
	private  String mobile; 
	private boolean isConnected = false; 
	
	public void setPower(boolean power)
	{
		this.m_power = power;
	}
	public boolean getPower() {
		return this.m_power;
		
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public boolean isConnected() {
		return isConnected;
	}
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
}
