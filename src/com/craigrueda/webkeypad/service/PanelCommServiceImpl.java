package com.craigrueda.webkeypad.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class PanelCommServiceImpl implements PanelCommService, InitializingBean, SerialPortEventListener {
	private static final Log log = LogFactory.getLog(PanelCommServiceImpl.class);
	private static final Pattern BITFIELD_PATTERN = Pattern.compile("\\[([0-9]+)\\-+\\]\\,([0-9a-fA-F]+)\\,\\[([0-9a-fA-F]+)\\]\\,\\\"([^\\\"]+)\\\"");
	private static final String DEFAULT_COM_PORT_NAME = "COM4";
	private static final int DEFAULT_COM_PORT_BAUD = 115200;
	private static final int ZONE_STATUS_AGE_MS = 12000; // 12 secs
	private static final String DEFAULT_NIGHT_STAY = "3";
	private static final String AD2USB_REBOOT_CMD = "=";
	private static final int ERROR_MSG_SENDING_INTERVAL_MIN = 240;
	
	@Autowired
	private MessagingService messagingService;
	private String comPortName = DEFAULT_COM_PORT_NAME;
	private int comPortBaud = DEFAULT_COM_PORT_BAUD;
	private InputStream is;
	private OutputStream os;
	private ZoneStatus lastStatus;
	private Map<Integer, ZoneStatus> zoneStatusMap = new TreeMap<Integer, ZoneStatus>();
	private String nightStayCode = DEFAULT_NIGHT_STAY;
	private boolean disableJniFeatures = false;
	private Boolean inAlarmState = false;
	private Date lastAlarmStateHeard = null;
	private SerialPort serialPort = null;
	private Thread zoneStatusEntryFlushingThread;
	private Thread alarmStateCheckingThread;
	private Thread periodicComPortRefresher;
	private boolean continueBackgroundProcessing = true;
	private Lock resetLock = new ReentrantLock();
	private Date lastErrorMsgSentAt = DateUtils.addMinutes(new Date(), 
			ERROR_MSG_SENDING_INTERVAL_MIN * -1); 

	@Override
	public void afterPropertiesSet() throws Exception {
		if (disableJniFeatures) return;
		
		try {
			resetLock.lock();
			
			initComPort();
			
			initBackgroundThreads();
		}
		finally {
			resetLock.unlock();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void initComPort() throws Exception {
		Enumeration<CommPortIdentifier> e = CommPortIdentifier.getPortIdentifiers();
		CommPortIdentifier portIdentifier = null;
		
		while (e.hasMoreElements()) {
			portIdentifier = e.nextElement();
			
			if (comPortName.equalsIgnoreCase(portIdentifier.getName()))
				break;
			else
				portIdentifier = null;
		}
		
		if (portIdentifier == null)
			throw new Exception("Could not find COM port with name " + comPortName);
		
		if (portIdentifier.isCurrentlyOwned())
			throw new Exception("COM port " + comPortName + " is already in use.");
		
		CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
		
		if (commPort instanceof SerialPort) {
            serialPort = (SerialPort) commPort;
            serialPort.setSerialPortParams(comPortBaud, SerialPort.DATABITS_8,
            		SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            
            is = serialPort.getInputStream();
            os = serialPort.getOutputStream();
            
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        }
        else
        	throw new Exception("Only serial ports are supported");
	}
	
	private void initBackgroundThreads() {
		continueBackgroundProcessing = true;
		
		zoneStatusEntryFlushingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (continueBackgroundProcessing) {
					try {
						Thread.sleep(1000);
						
						Date entryFlushThreshold = 
							DateUtils.addMilliseconds(new Date(), ZONE_STATUS_AGE_MS * -1
								);
						
						synchronized (zoneStatusMap) {
							Set<Entry<Integer, ZoneStatus>> entries = zoneStatusMap.entrySet();
							List<Entry<Integer, ZoneStatus>> toRemove = new ArrayList<Entry<Integer, ZoneStatus>>();
							
							for (Entry<Integer, ZoneStatus> status : entries) {
								if (status.getValue().getCreatedAt().before(entryFlushThreshold))
									toRemove.add(status);
							}
							
							for (Entry<Integer, ZoneStatus> status : toRemove)
								zoneStatusMap.remove(status.getKey());
						}
					} 
					catch (InterruptedException e) {
						log.error("", e);
					}
				}
			}}, "Zone Map Cleanup Thread");
		zoneStatusEntryFlushingThread.setDaemon(true);
		zoneStatusEntryFlushingThread.start();
		
		alarmStateCheckingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (continueBackgroundProcessing) {
					try {
						Thread.sleep(1000);
						
						Date alarmStateThreshold = 
							DateUtils.addMilliseconds(new Date(), ZONE_STATUS_AGE_MS * 2 * -1
								);
						
						synchronized (this) {
							if (lastAlarmStateHeard != null && lastAlarmStateHeard.before(alarmStateThreshold))
								inAlarmState = false;
						}
					}
					catch (Exception ex) {
						log.error("", ex);
					}
				}
				
			}}, "Alarm State Checking Thread");
		alarmStateCheckingThread.setDaemon(true);
		alarmStateCheckingThread.start();
		
		periodicComPortRefresher = new Thread(new Runnable() {
			@Override
			public void run() {
				while (continueBackgroundProcessing) {
					try {
						Thread.sleep(DateUtils.MILLIS_PER_HOUR);
						
						resetComport(true);
					} 
					catch (InterruptedException e) { }
				}
			}}, "Periodic COM Port Refresher");
		periodicComPortRefresher.setDaemon(true);
		periodicComPortRefresher.start();
	}
	
	private void resetComport(boolean tryReboot) {
		if (resetLock.tryLock()) {
			try {
				continueBackgroundProcessing = false;
				
				Thread.sleep(2000);
				
				if (tryReboot)
					sendCommand(AD2USB_REBOOT_CMD);
				
				Thread.sleep(30000);
			} 
			catch (Exception e) {
				log.error("", e);
			}
			
			try {
				log.info("Resetting com port: " + serialPort);
				
				if (serialPort != null) {
					try {
						serialPort.close();
					}
					catch (Exception ex) {
						log.error("Failed to close com port.", ex);
					}
				}
				
				try {
					initComPort();
					
					initBackgroundThreads();
				} 
				catch (Exception e) {
					log.error("", e);
				}
			}
			finally {
				resetLock.unlock();
			}
		}
	}
	
	@Override
	public void serialEvent(SerialPortEvent arg0) {
		int data;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	      
        try {
            while ( ( data = is.read()) > -1 )
            {
                if ( data == '\n' )
                    break;

                buffer.write(data);
            }
            
            String msg = new String(buffer.toByteArray());
            
            if (log.isDebugEnabled())
            	log.debug(msg);
            
            handleMessage(msg);
        }
        catch (Exception e) {
            log.error("Failed to handle serial port event", e);
            
            synchronized (lastErrorMsgSentAt) {
            	if (new Date().after(
            			DateUtils.addMinutes(
            					lastErrorMsgSentAt, ERROR_MSG_SENDING_INTERVAL_MIN))) {
            		messagingService.sendErrorMessage("Failed to handle serial port event\n" +
                			ExceptionUtils.getFullStackTrace(e));
            		
            		lastErrorMsgSentAt = new Date();
            	}
            		
            }
            
            resetComport(true);
        }    
	}
	
	@Override
	public List<ZoneStatus> getCurrentStatusList() {
		List<ZoneStatus> ret = new ArrayList<ZoneStatus>();
		
		synchronized (zoneStatusMap) {
			Set<Entry<Integer, ZoneStatus>> entries = zoneStatusMap.entrySet();
			
			for (Entry<Integer, ZoneStatus> status : entries)
				ret.add(status.getValue());
		}
		
		Collections.sort(ret, new Comparator<ZoneStatus>() {
			@Override
			public int compare(ZoneStatus left, ZoneStatus right) {
				return left.getZoneNumber().compareTo(right.getZoneNumber());
			}});
		
		return ret;
	}
	
	@Override
	public ZoneStatus getCurrentStatus() {
		return lastStatus;
	}
	
	@Override
	public void armAway(String userCode) {
		sendCommand(userCode + "2");
	}

	@Override
	public void armNight(String userCode) {
		sendCommand(computeCode(nightStayCode));
	}

	@Override
	public void armStay(String userCode) {
		sendCommand(userCode + "3");
	}

	@Override
	public void disarm(String userCode) {
		sendCommand(userCode + "1");
	}
	
	private void handleMessage(String msg) throws DecoderException {
		Matcher m = BITFIELD_PATTERN.matcher(msg);
		
		if (!m.find() || m.groupCount() < 4) return;
		
		String bitField = m.group(1);
		String zoneField = m.group(2);
		String rawMsgData = m.group(3);
		String msgData = m.group(4);
		
		ZoneStatus newStatus = new ZoneStatus(bitField, zoneField, rawMsgData, msgData);
		
		if (newStatus.isProgrammingMode()) return;
		
		if (newStatus.isHitStarMsg())
			sendCommand("*");
		
		handleStatusChange(newStatus, lastStatus);
		
		lastStatus = newStatus;
		
		if (newStatus.getZoneNumber() == 8) return;
			
		synchronized (zoneStatusMap) {
			if (!ZoneStatus.Status.NOT_READY.equals(lastStatus.getStatus()))
				zoneStatusMap.clear();
			
			zoneStatusMap.put(lastStatus.getZoneNumber(), lastStatus);
		}
	}
	
	private void handleStatusChange(ZoneStatus newStatus, ZoneStatus oldStatus) {
		synchronized (this) {
			if (newStatus.isAlarmTriggered()) {
				if (!inAlarmState)
					messagingService.sendAlarmAlert(newStatus);
				
				inAlarmState = true;
				lastAlarmStateHeard = new Date();
			}
		}
	}
	
	private String computeCode(String code) {
		if (code.startsWith("F"))
			return "" + (char) Integer.valueOf(code.replaceAll("F", "")).intValue();
		
		return code;
	}
	
	private void sendCommand(String command) {
		try {
			os.write(command.getBytes());
			os.flush();
		}
		catch (Exception ex) {
			log.error("Failed to send command: " + command, ex);
			
			resetComport(false);
		}
	}

	public void setComPortName(String comPortName) {
		this.comPortName = comPortName;
	}

	public void setComPortBaud(int comPortBaud) {
		this.comPortBaud = comPortBaud;
	}

	public void setNightStayCode(String nightStayCode) {
		this.nightStayCode = nightStayCode;
	}

	public void setDisableJniFeatures(boolean disableJniFeatures) {
		this.disableJniFeatures = disableJniFeatures;
	}

	public Boolean getInAlarmState() {
		return inAlarmState;
	}
}
