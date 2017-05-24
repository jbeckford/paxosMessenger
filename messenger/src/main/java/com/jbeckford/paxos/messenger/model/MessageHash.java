package com.jbeckford.paxos.messenger.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageHash {
	private String digest;

	public MessageHash(){
	}
	
	public MessageHash(String messageText) throws NoSuchAlgorithmException{
		this.setDigest(MessageHash.generateHash(messageText));		
	}
	
	public static String generateHash(String messageText) throws NoSuchAlgorithmException{
		String messageHash = null;
		
		if(messageText != null) {
			MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("SHA-256");
				byte[] hashBytes = digest.digest(messageText.getBytes(StandardCharsets.UTF_8));
				
				StringBuffer sbHash = new StringBuffer();
		
				for (int i=0; i<hashBytes.length; i++) {
					sbHash.append(Integer.toHexString(0xFF & hashBytes[i])); 	
				} 	
				
				messageHash = sbHash.toString();
			} catch (NoSuchAlgorithmException e) {
				throw e;
			}
		}
		
		return messageHash;	
	}
	
	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}
}
