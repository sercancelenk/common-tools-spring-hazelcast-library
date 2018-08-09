# Common-Tools
## Hazelcast-Library for Spring Projects

Use this repo for hazelcast utilities in spring projects.

### Install
Add this library as a depency to pom.xml

### Usage
1. Add below annotation to @Configuration class

    `@EnableHazelcastLibrary`

2. Add your cachestore

    `@Bean(name = HazelcastCacheMapManager.HazelcastCacheNames.DEFAULT_MAP_FIVE_MINUTES_TTL.name)

     public HazelcastCacheStore getHzDefaultMapCacheStoreFiveMinutesTTL(@Autowired @Qualifier(HazelcastCacheMapManager.HAZELCAST_INSTANCE) HazelcastInstance hzInstance) {

        return new HazelcastCacheStore<String, Object>(hzInstance, HazelcastCacheMapManager.HazelcastCacheNames.DEFAULT_MAP_FIVE_MINUTES_TTL.name);

     }`


3. Lastly, inject your cachestore to class

    `@Autowired

     @Qualifier(HazelcastCacheMapManager.HazelcastCacheNames.DEFAULT_MAP_FIVE_MINUTES_TTL.name)

     private CacheStore cacheStore;`
     
