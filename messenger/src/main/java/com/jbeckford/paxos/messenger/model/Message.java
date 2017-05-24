package com.jbeckford.paxos.messenger.model;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Message {
	private String text;
	private MessageHash messageHash;
	private Date created;

	public Message(){
	}
	
	public Message(String text){
		this.setText(text);
		this.setCreated();
		this.setMessageHash();
	}

	public MessageHash getMessageHash() {
		return messageHash;
	}
	
	public void setMessageHash(MessageHash messageHash) {
		this.messageHash = messageHash;
	}
	
	public void setMessageHash() {
		try {
			this.setMessageHash(new MessageHash(this.getText()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public String getText() {
		return this.text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public void setCreated(){
		this.setCreated(new Date());
	}
}
