package com.craigrueda.webkeypad.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.craigrueda.webkeypad.util.PasswordUtils;

public class PasswordHasher {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("Please enter the password you want hashed:  ");
		
		String hashed = PasswordUtils.hashPassword(reader.readLine());
		
		System.out.println();
		System.out.println();
		System.out.println("Your SHA384 hashed, Base64 encoded password:");
		System.out.println();
		System.out.println(hashed);
		System.in.read();
	}
}
