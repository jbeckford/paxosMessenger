package com.jbeckford.paxos.messenger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataBase<K, T> {
	private Map<K, T> repository = new ConcurrentHashMap<K, T>();
	
	public T addItem(K key, T item){
		return repository.put(key, item);		
	}
	
	public T getItem(K key){
		return repository.get(key);
	}
	
	public T updateItem(K key, T item){
		return addItem(key, item);
	}
	
	public T deleteItem(K key){
		T item = repository.remove(key);
		return item;
	}

	public List<T> getItems() {
		List<T> items = new ArrayList<T>();
		items.addAll(repository.values());
		return items;
	}
}
