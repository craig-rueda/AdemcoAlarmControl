package com.craigrueda.webkeypad.service;

import java.util.Date;

import org.apache.commons.lang.WordUtils;

public class ZoneStatus {
	public enum Status {
		READY, ARMED_AWAY, ARMED_HOME, NOT_READY;
	}
	
	private String bitField;
	private String zoneField;
	private String rawMsgData;
	private String msgData;
	private Integer zoneNumber;
	private Date createdAt = new Date();
	private Status status = Status.NOT_READY;
	private boolean backLight = false;
	private boolean programmingMode = false;
	private boolean zonesBypassed = false;
	private boolean acPower = false;
	private boolean chimeMode = false;
	private boolean alarmTriggered = false;
	private boolean batteryLow = false;
	
	public ZoneStatus(String bitField, String zoneField, String rawMsgData,
			String msgData) {
		this.bitField = bitField;
		this.zoneField = zoneField;
		this.rawMsgData = rawMsgData;
		this.msgData = WordUtils.capitalize(msgData.trim());
		
		parseBitField();
		zoneNumber = Integer.parseInt(zoneField, 16);
	}

	private void parseBitField() {
		String[] split = bitField.split("");
		
		if ("1".equals(split[1]))
			status = Status.READY;
		else if ("1".equals(split[2]))
			status = Status.ARMED_AWAY;
		else if ("1".equals(split[3]))
			status = Status.ARMED_HOME;
		
		backLight = "1".equals(split[4]);
		programmingMode = "1".equals(split[5]);
		zonesBypassed = "1".equals(split[7]);
		acPower = "1".equals(split[8]);
		chimeMode = "1".equals(split[9]);
		alarmTriggered = "1".equals(split[10]);
		batteryLow = "1".equals(split[12]);
	}
	
	public Integer getZoneNumber() {
		return zoneNumber;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Status getStatus() {
		return status;
	}

	public boolean isBackLight() {
		return backLight;
	}

	public boolean isProgrammingMode() {
		return programmingMode;
	}

	public boolean isZonesBypassed() {
		return zonesBypassed;
	}

	public String getBitField() {
		return bitField;
	}

	public String getZoneField() {
		return zoneField;
	}

	public String getRawMsgData() {
		return rawMsgData;
	}

	public String getMsgData() {
		return msgData;
	}

	public boolean isAcPower() {
		return acPower;
	}

	public boolean isChimeMode() {
		return chimeMode;
	}

	public boolean isAlarmTriggered() {
		return alarmTriggered;
	}

	public boolean isBatteryLow() {
		return batteryLow;
	}
	
	public boolean isFaulted() {
		return Status.NOT_READY.equals(status);
	}
	
	public boolean isArmed() {
		return Status.ARMED_AWAY.equals(status) || Status.ARMED_HOME.equals(status);
	}
	
	public boolean isHitStarMsg() {
		return msgData != null && msgData.indexOf("Hit *") > 0;
	}
}
