package com.jbeckford.paxos.messenger.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jbeckford.paxos.messenger.model.Message;
import com.jbeckford.paxos.repository.MessageRepository;

public class MessageService {

	public MessageService(){
//		addMessage("The current time:" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())); 
//		addMessage("Never odd or even");
	}
	
	public Message addMessage(String messageText){
		Message message = new Message(messageText);
		return addMessage(message);
	}
	
	public Message addMessage(Message message){
		MessageRepository.addMessage(message.getMessageHash().getDigest(), message);
		return message;
	}

	public List<Message> getMessages(){
		List<Message> messages = MessageRepository.getMessages();
		return messages;
	}
	
	public Message getMessage(String messageHash){
		return MessageRepository.getMessage(messageHash);
	}
	
	public Message updateMessage(String messageHash, String messageText){
		return MessageRepository.updateMessage(messageHash, new Message(messageText));
	}
	
	public Message updateMessage(Message message){
		return message == null ? null : MessageRepository.updateMessage(message.getMessageHash().getDigest(), new Message(message.getText()));
	}

	public Message deleteMessage(String messageHash){
		return MessageRepository.deleteMessage(messageHash);
	}
}
