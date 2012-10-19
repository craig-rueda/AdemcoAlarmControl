package com.craigrueda.webkeypad.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

public class PasswordUtils {
	private static final Random rand = new Random();
	
	private PasswordUtils() { }
	
	public static String hashPassword(String plaintext) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            messageDigest.update(plaintext.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        byte raw[] = messageDigest.digest();
        String hash = new String(Base64.encodeBase64(raw));
        return hash;
    }
	
	public static String generateRandomPwd(int length) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < length; i++)
			sb.append(rand.nextInt(9));
		
		return sb.toString();
	}
}
