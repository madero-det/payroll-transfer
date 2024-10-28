package com.mcnc.payroll.config.cache;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;

public class LimitedCacheManager implements CacheManager {

	private final RedisCacheManager redisCacheManager;
	private final int maxKeys;
	private final ConcurrentHashMap<String, CacheLimiter> cacheLimits = new ConcurrentHashMap<>();

	public LimitedCacheManager(RedisCacheManager redisCacheManager, int maxKeys) {
		this.redisCacheManager = redisCacheManager;
		this.maxKeys = maxKeys;
	}

	@Override
	public Cache getCache(String name) {
		return cacheLimits.computeIfAbsent(name, n -> new CacheLimiter(redisCacheManager.getCache(name), maxKeys));
	}

	@Override
	public Collection<String> getCacheNames() {
		return redisCacheManager.getCacheNames();
	}

}
