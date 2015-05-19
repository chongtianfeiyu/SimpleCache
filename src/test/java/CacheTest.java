import com.lippi.cache.Cache;
import com.lippi.cache.CacheFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.locks.Lock;

/**
 * Created by lippi on 15-5-19.
 */
public class CacheTest {

    public CacheTest(){}
    public static void main(String[] args) {
        Cache<String, Object> objectCache = CacheFactory.createCache("objects");
        //set max cache size
        objectCache.setMaxCacheSize(1024 * 1024);
        //set max lifetime for cache objects
        objectCache.setMaxLifetime(60 * 60 * 1000);

        objectCache.put("object1", new Object());
        objectCache.put("object2", new Object());
        objectCache.put("object3", new Object());

        Object o1 = objectCache.get("object1");
        if(o1 == null){
            //get data from file or datebase
        }
        //get cache hits
        long cacheHits = objectCache.getCacheHits();
        //get cache miss
        long cacheMiss = objectCache.getCacheMisses();
        //remove cache
        Object o2 = objectCache.remove("object1");
        if(o2 != null) {o2 = null;}

        //you can also use Collection as cache value

        Cache<String, Collection<String>> collectionCache = CacheFactory.createCache("collections");
        collectionCache.put("collection1", Arrays.asList("hello", "simple cache"));
        //you can get lock for this collection
        Lock lock = CacheFactory.getLock("collection1", collectionCache);
        lock.lock();
        //do something
        lock.unlock();

        //delete cache
        CacheFactory.destroyCache("object1");
        CacheFactory.destroyCache("collection1");

    }

}
