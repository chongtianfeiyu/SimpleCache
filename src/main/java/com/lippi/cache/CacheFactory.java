
package com.lippi.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

/**
 * Creates Cache objects. The returned caches will either be local or clustered
 * depending on the clustering enabled setting and a user's license.
 *
 * <p>When clustered caching is turned on, cache usage statistics for all caches
 * that have been created are periodically published to the clustered cache
 * named "opt-$cacheStats".</p>
 *
 */
@SuppressWarnings("rawtypes")
public class CacheFactory {

    private static final Logger log = LoggerFactory.getLogger(CacheFactory.class);

    /**
     * Storage for all caches that get created.
     */
    private static Map<String, Cache> caches = new ConcurrentHashMap<String, Cache>();
    private static CacheFactoryStrategy cacheFactoryStrategy = new DefaultLocalCacheStrategy();

    public static final int DEFAULT_MAX_CACHE_SIZE = 1024 * 256;
    public static final long DEFAULT_MAX_CACHE_LIFETIME = 6 * 60 * 60 * 1000;

    private CacheFactory() {
    }


    /**
     * Returns an array of all caches in the system.
     * @return an array of all caches in the system.
     */
    public static Cache[] getAllCaches() {
        List<Cache> values = new ArrayList<Cache>();
        for (Cache cache : caches.values()) {
            values.add(cache);
        }
        return values.toArray(new Cache[values.size()]);
    }

    /**
     * Returns the named cache, creating it as necessary.
     *
     * @param name         the name of the cache to create.
     * @return the named cache, creating it as necessary.
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T extends Cache> T createCache(String name) {
        T cache = (T) caches.get(name);
        if (cache != null) {
            return cache;
        }
        cache = (T) cacheFactoryStrategy.createCache(name);

        log.info("Created cache [" + cacheFactoryStrategy.getClass().getName() + "] for " + name);

        return wrapCache(cache, name);
    }


    /**
     * Destroys the cache for the cache name specified.
     *
     * @param name the name of the cache to destroy.
     */
    public static synchronized void destroyCache(String name) {
        Cache cache = caches.remove(name);
        if (cache != null) {
            cacheFactoryStrategy.destroyCache(cache);
        }
    }

    /**
     * Returns an existing {@link Lock} on the specified key or creates a new one
     * if none was found. This operation is thread safe. Successive calls with the same key may or may not
     * return the same {@link Lock}. However, different threads asking for the
     * same Lock at the same time will get the same Lock object.<p>
     *
     * @param key the object that defines the visibility or scope of the lock.
     * @param cache the cache used for holding the lock.
     * @return an existing lock on the specified key or creates a new one if none was found.
     */
    public static synchronized Lock getLock(Object key, Cache cache) {
        return cacheFactoryStrategy.getLock(key, cache);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Cache> T wrapCache(T cache, String name) {
        cache = (T) new CacheWrapper(cache);
        cache.setName(name);
        caches.put(name, cache);
        return cache;
    }


    public synchronized static void clearCaches() {
        for (String cacheName : caches.keySet()) {
            Cache cache = caches.get(cacheName);
            cache.clear();
        }
    }

    public static int getMaxCacheSize() {
        return DEFAULT_MAX_CACHE_SIZE;
    }

    public static long getMaxCacheLifetime() {
        return DEFAULT_MAX_CACHE_LIFETIME;
    }
}


