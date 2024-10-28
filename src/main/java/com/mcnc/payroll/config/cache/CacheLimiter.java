package com.mcnc.payroll.config.cache;

import java.util.LinkedList;
import java.util.concurrent.Callable;

import org.springframework.cache.Cache;

public class CacheLimiter implements Cache {

	private final Cache cache;
	private final int maxKeys;
	private final LinkedList<Object> keyOrder = new LinkedList<>();

	public CacheLimiter(Cache cache, int maxKeys) {
		this.cache = cache;
		this.maxKeys = maxKeys;
	}

	@Override
	public String getName() {
		return cache.getName();
	}

	@Override
	public Object getNativeCache() {
		return cache.getNativeCache();
	}

	@Override
	public ValueWrapper get(Object key) {
		return cache.get(key);
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		return cache.get(key, type);
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		return cache.get(key, valueLoader);
	}

	@Override
	public void put(Object key, Object value) {
		while (keyOrder.size() >= maxKeys) {
			Object oldestKey = keyOrder.removeFirst();
			cache.evict(oldestKey);
		}
		keyOrder.addLast(key);
		cache.put(key, value);
	}

	@Override
	public void evict(Object key) {
		keyOrder.remove(key);
		cache.evict(key);
	}

	@Override
	public void clear() {
		keyOrder.clear();
		cache.clear();
	}

}
