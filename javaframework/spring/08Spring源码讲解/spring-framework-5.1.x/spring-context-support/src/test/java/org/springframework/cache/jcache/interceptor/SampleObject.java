package org.springframework.cache.jcache.interceptor;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Stephane Nicoll
 */
class SampleObject {

	// Simple

	@CacheResult(cacheName = "simpleCache")
	public SampleObject simpleGet(Long id) {
		return null;
	}

	@CachePut(cacheName = "simpleCache")
	public void simplePut(Long id, @CacheValue SampleObject instance) {
	}

	@CacheRemove(cacheName = "simpleCache")
	public void simpleRemove(Long id) {
	}

	@CacheRemoveAll(cacheName = "simpleCache")
	public void simpleRemoveAll() {
	}

	@CacheResult(cacheName = "testSimple")
	public SampleObject anotherSimpleGet(String foo, Long bar) {
		return null;
	}

	// @CacheKey

	@CacheResult
	public SampleObject multiKeysGet(@CacheKey Long id, Boolean notUsed,
			@CacheKey String domain) {
		return null;
	}

	// @CacheValue

	@CachePut(cacheName = "simpleCache")
	public void noCacheValue(Long id) {
	}

	@CachePut(cacheName = "simpleCache")
	public void multiCacheValues(Long id, @CacheValue SampleObject instance,
			@CacheValue SampleObject anotherInstance) {
	}

	// Parameter annotation

	@CacheResult(cacheName = "simpleCache")
	public SampleObject annotatedGet(@CacheKey Long id, @Value("${foo}") String foo) {
		return null;
	}

	// Full config

	@CacheResult(cacheName = "simpleCache", skipGet = true,
			cachedExceptions = Exception.class, nonCachedExceptions = RuntimeException.class)
	public SampleObject fullGetConfig(@CacheKey Long id) {
		return null;
	}

	@CachePut(cacheName = "simpleCache", afterInvocation = false,
			cacheFor = Exception.class, noCacheFor = RuntimeException.class)
	public void fullPutConfig(@CacheKey Long id, @CacheValue SampleObject instance) {
	}

}
