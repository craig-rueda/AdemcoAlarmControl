package com.craigrueda.webkeypad.service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class MessagingServiceImpl implements MessagingService {
	private static final Log log = LogFactory.getLog(MessagingServiceImpl.class);
	
	@Autowired
	private MailSender mailSender;
	private List<String> txtMsgRecips = Collections.emptyList();
	private List<String> emailRecips = Collections.emptyList();
	private SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy h:mm a");
	
	@Override
	public void sendAlarmAlert(ZoneStatus status) {
		SimpleMailMessage txtMsg = new SimpleMailMessage();
		SimpleMailMessage emailMsg = new SimpleMailMessage();
		
		txtMsg.setTo(txtMsgRecips.toArray(new String[txtMsgRecips.size()]));
		emailMsg.setTo(emailRecips.toArray(new String[emailRecips.size()]));
		
		txtMsg.setFrom("WebKeypadAlert@craigrueda.com");
		emailMsg.setFrom("WebKeypadAlert@craigrueda.com");
		
		txtMsg.setText("------ALERT------\nYour alarm has been activated!\n(" + 
				format.format(new Date()) + ")\n" + (status == null ? "" : status.getMsgData()));
		
		emailMsg.setSubject("------ALERT------ Your alarm has been activated!");
		emailMsg.setText("------ALERT------\nYour alarm has been activated!\n(" + 
				format.format(new Date()) + ")\n" + (status == null ? "" : status.getMsgData()));
		
		if (!txtMsgRecips.isEmpty()) {
			try {
				mailSender.send(txtMsg);
			}
			catch (Exception ex) {
				log.error("", ex);
			}
		}
		
		if (!emailRecips.isEmpty()) {
			try {
				mailSender.send(emailMsg);
			}
			catch (Exception ex) {
				log.error("", ex);
			}
		}
	}
	
	@Override
	public void sendErrorMessage(String msg) {
		SimpleMailMessage emailMsg = new SimpleMailMessage();
		
		emailMsg.setTo(emailRecips.toArray(new String[emailRecips.size()]));
		emailMsg.setSubject("An ERROR Has Occurred");
		emailMsg.setText(msg);
		
		if (!emailRecips.isEmpty()) {
			try {
				mailSender.send(emailMsg);
			}
			catch (Exception ex) {
				log.error("", ex);
			}
		}
	}
	
	public void setTxtMsgRecips(List<String> txtMsgRecips) {
		this.txtMsgRecips = txtMsgRecips;
	}

	public void setEmailRecips(List<String> emailRecips) {
		this.emailRecips = emailRecips;
	}
}
