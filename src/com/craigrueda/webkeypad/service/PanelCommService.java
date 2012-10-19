package com.craigrueda.webkeypad.service;

import java.util.List;

public interface PanelCommService {
	public List<ZoneStatus> getCurrentStatusList();
	public ZoneStatus getCurrentStatus();
	public void armAway(String userCode);
	public void armStay(String userCode);
	public void armNight(String userCode);
	public void disarm(String userCode);
}
