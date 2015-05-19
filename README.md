# SimpleCache

A Cache framework for java and android application， objects are store in key-value pair in memory,value can be any java object like String, List, Map etc. you can define your store logic by implement Cache interface and CacheFactoryStrategy,when cache is full ,LRU is used .

用Java实现的轻量级缓存框架，你可以实现Cache和CacheFactoryStrategy接口来自定义你的缓存逻辑。使用HashMap进行存储，键为String，值可以为任意对象类型，如String,List,Map等。当缓存的大小大于最大缓存容量的0.97倍时，会对缓存中的数据进行清理。缓存使用了两个LinkedList保存插入的数据，一个按照**对象的创建顺序**来保存，对象被创建之后放在链表最前面，当空间不足时从链表尾部开始清理那些过期的数据。另一个LinkedList按照**缓存的访问顺序**来排序，每当对象被访问就把对象插入到链表的最前面，同时框架使用了Public的LinkedListNode，Cache中保存对链表节点的引用，可以快速删除数据。

# how to use

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
