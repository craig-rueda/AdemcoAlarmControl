package com.craigrueda.webkeypad.service;

public interface MessagingService {
	public void sendAlarmAlert(ZoneStatus status);

	public void sendErrorMessage(String msg);
}
