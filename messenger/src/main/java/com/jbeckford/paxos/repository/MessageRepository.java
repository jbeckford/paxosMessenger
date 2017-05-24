package com.jbeckford.paxos.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jbeckford.paxos.messenger.model.Message;


public class MessageRepository {

	private static Map<String, Message> repository = new ConcurrentHashMap<String, Message>();
	
	public static Map<String, Message> getRepository() {
		return repository;
	}
	
	public static Message addMessage(String key, Message message){
		return repository.put(key, message);		
	}
	
	public static Message getMessage(String key){
		return repository.get(key);
	}
	
	public static Message updateMessage(String key, Message message){
		return addMessage(key, message);
	}
	
	public static Message deleteMessage(String key){
		Message message = repository.remove(key);
		return message;
	}

	public static List<Message> getMessages() {
		List<Message> messages = new ArrayList<Message>();
		messages.addAll(repository.values());
		return messages;
	}
}
