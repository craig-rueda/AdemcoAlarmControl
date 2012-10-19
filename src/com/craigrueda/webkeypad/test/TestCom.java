package com.craigrueda.webkeypad.test;

import java.util.Enumeration;

import gnu.io.CommPortIdentifier;

public class TestCom {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Enumeration e = CommPortIdentifier.getPortIdentifiers();
		
		while (e.hasMoreElements()) {
			CommPortIdentifier id = (CommPortIdentifier ) e.nextElement();
			
			System.out.println(id.getName());
		}
	}	
}
