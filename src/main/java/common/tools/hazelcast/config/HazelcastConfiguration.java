package common.tools.hazelcast.config;

import com.hazelcast.config.*;
import com.hazelcast.core.*;
import com.hazelcast.map.merge.PutIfAbsentMapMergePolicy;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import common.tools.hazelcast.util.cache.HazelcastCacheMapManager;
import common.tools.hazelcast.util.cache.impl.HazelcastCacheStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author byzasttech
 */

@Configuration
@EnableCaching
@ComponentScan(basePackages = {"common.tools.hazelcast"})
public class HazelcastConfiguration {
    private static Logger logger = LoggerFactory.getLogger(HazelcastConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        logger.info("[CommonTools] HazelcastCacheConfig initialize successfully.");
    }

    @Value("${cache.hazelcast.multicast.enable:false}")
    private boolean isMulticastEnable;

    @Value("${cache.hazelcast.multicast.group:230.0.0.1}")
    private String multicastGroup;

    @Value("${cache.hazelcast.multicast.port:46666}")
    private int multicastPort;

    @Value("#{'${cache.hazelcast.tcp.members:127.0.0.1}'.split(',')}")
    private List<String> memberTcpAddresses;

    @Value("${cache.hazelcast.tcp.enable:false}")
    private boolean isTcpEnable;

    public HazelcastConfiguration() {
        HazelcastCacheMapManager.prepareHazelcastMapInstancesTypes();
        logger.info("[Common] Hazelcast Cache Config initialize successfully.");
    }

    private static void addMapToConfig(Config config,
                                       String mapName,
                                       int backupCount,
                                       InMemoryFormat inMemoryFormat,
                                       EvictionPolicy evictionPolicy,
                                       int maxIdleSeconds,
                                       boolean readBackupData) {
        MapConfig mapConfig = new MapConfig();
        mapConfig.setName(mapName);
        mapConfig.setBackupCount(backupCount);
        mapConfig.setInMemoryFormat(inMemoryFormat);
        mapConfig.setEvictionPolicy(evictionPolicy);
        mapConfig.setReadBackupData(readBackupData);
        mapConfig.setMaxIdleSeconds(maxIdleSeconds);
        mapConfig.setMaxSizeConfig(new MaxSizeConfig(MaxSizeConfig.DEFAULT_MAX_SIZE, MaxSizeConfig.MaxSizePolicy.PER_NODE));
        mapConfig.setMergePolicy(PutIfAbsentMapMergePolicy.class.getName());


        if (HazelcastCacheMapManager.getMapInfo(mapName).getTimeToLiveSeconds() > 0)
            mapConfig.setTimeToLiveSeconds(HazelcastCacheMapManager.getMapInfo(mapName).getTimeToLiveSeconds());

        config.addMapConfig(mapConfig);

        logger.info("[Common] [Hazelcast] Added {} DISTRIBUTED map to default instance. [MapConfig={}]", mapName, mapConfig);
    }

    private static void addReplicatedMapToConfig(Config config, String mapName, InMemoryFormat inMemoryFormat,
                                                 boolean asyncFillUp) {
        ReplicatedMapConfig replicatedMapConfig = new ReplicatedMapConfig();
        replicatedMapConfig.setAsyncFillup(asyncFillUp);
        replicatedMapConfig.setInMemoryFormat(inMemoryFormat);
        replicatedMapConfig.setName(mapName);

        config.addReplicatedMapConfig(replicatedMapConfig);

        logger.info("[Common] [Hazelcast] Added {} REPLICATED map to default instance. [ReplicatedMapConfig={}]", mapName, replicatedMapConfig);
    }

    private Config getBaseConfig(boolean addDefaultMap) {
        Config config = new Config();
        config.setProperty("hazelcast.logging.type", "slf4j");
        if (BooleanUtils.isTrue(addDefaultMap))
            addMapToConfig(config, HazelcastCacheMapManager.HazelcastCacheNames.DEFAULT_MAP_FIVE_MINUTES_TTL.name, 1, InMemoryFormat.OBJECT, EvictionPolicy.LRU, HazelcastCacheMapManager.TimeUnits.THREE_MINUTES_TTL,false);
        return config;
    }

    @Bean(value = HazelcastCacheMapManager.HAZELCAST_INSTANCE, destroyMethod = "shutdown")
    public HazelcastInstance getDefaultInstance() {

        Config config = getBaseConfig(Boolean.FALSE);
        config.getNetworkConfig().setPortAutoIncrement(true);

        if(isMulticastEnable){
            config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
            config.getNetworkConfig().getJoin().getAwsConfig().setEnabled(false);

            config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(isMulticastEnable);
            config.getNetworkConfig().getJoin().getMulticastConfig().setMulticastGroup(multicastGroup);
            config.getNetworkConfig().getJoin().getMulticastConfig().setMulticastPort(multicastPort);
            config.getNetworkConfig().getJoin().getMulticastConfig().setMulticastTimeToLive(1); // same subnet
        }else if(isTcpEnable){
            config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
            config.getNetworkConfig().getJoin().getAwsConfig().setEnabled(false);
            config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(isTcpEnable);

            if(CollectionUtils.isNotEmpty(memberTcpAddresses)){
                memberTcpAddresses
                        .stream()
                        .forEach(m -> {
                            config.getNetworkConfig().getJoin().getTcpIpConfig().addMember(m.trim());
                        });
            }
        }

        config.setInstanceName("[Magazine Service] Distrubuted Hazelcast Instance");

        HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);

        return instance;
    }

    @Primary
    @Bean(HazelcastCacheMapManager.HazelcastCacheNames.HZ_COMMON_CACHE_MANAGER.name)
    public CacheManager cacheManager() {
        return new HazelcastCacheManager(getDefaultInstance());
    }

}
