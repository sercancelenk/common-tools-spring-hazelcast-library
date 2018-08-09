package common.tools.hazelcast.util.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author byzasttech
 */

public class HazelcastCacheMapManager {
    public static Map<String, MapInfo> hazelcastMapInstanceTypes = new HashMap();
    public static final String HAZELCAST_INSTANCE = "hzInstance";

    public enum MapType {
        LOCAL,
        REPLICATED,
        DISTRIBUTED,
    }

    public static class MapInfo {

        private MapType mapType;
        private int timeToLiveSeconds;
        private TimeUnit timeUnit = TimeUnit.SECONDS;

        public MapInfo(MapType mapType) {
            this.mapType = mapType;
            this.timeToLiveSeconds = TimeUnits.UNLIMITED;
        }

        public MapInfo(MapType mapType, int timeToLiveSeconds) {
            this.mapType = mapType;
            this.timeToLiveSeconds = timeToLiveSeconds;
        }

        public MapType getMapType() {
            return mapType;
        }

        public int getTimeToLiveSeconds() {
            return timeToLiveSeconds;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }
    }

    public static class TimeUnits {
        public static final int UNLIMITED = -1;
        public static final int TEN_SECONDS_TTL  = 10;
        public static final int FIFTEEN_SECONDS_TTL = 15;
        public static final int ONE_MINUTE_TTL = 60;
        public static final int TWO_MINUTES_TTL = 2 * ONE_MINUTE_TTL;
        public static final int THREE_MINUTES_TTL = 3 * ONE_MINUTE_TTL;
        public static final int FIVE_MINUTES_TTL = 5 * ONE_MINUTE_TTL;
        public static final int TEN_MINUTES_TTL = 10 * ONE_MINUTE_TTL;
        public static final int ONE_HOUR_TTL = 60 * ONE_MINUTE_TTL;
        public static final int FOUR_HOURS_TTL = 4 * ONE_HOUR_TTL;
        public static final int TWELVE_HOURS_TTL = 12 * ONE_HOUR_TTL;
        public static final int ONE_DAY_TTL = 24 * ONE_HOUR_TTL;
        public static final int ONE_WEEK_TTL = 7 * ONE_DAY_TTL;
    }

    public static class HazelcastCacheNames {
        public interface HZ_COMMON_CACHE_MANAGER {
            String name = "HZ_COMMON_CACHE_MANAGER";
        }
        public interface HZ_COMMON_CACHE_MANAGER_REPLICATED {
            String name = "HZ_COMMON_CACHE_MANAGER_REPLICATED";
        }

        public interface DEFAULT_MAP_FIVE_MINUTES_TTL {
            String name = "DEFAULT_MAP_FIVE_MINUTES_TTL";
            MapInfo mapInfo = new MapInfo(MapType.DISTRIBUTED, TimeUnits.FIVE_MINUTES_TTL);
        }

        public interface DEFAULT_MAP_TEN_MINUTES_TTL {
            String name = "DEFAULT_MAP_TEN_MINUTES_TTL";
            MapInfo mapInfo = new MapInfo(MapType.DISTRIBUTED, TimeUnits.TEN_MINUTES_TTL);
        }
        public interface DEFAULT_MAP_TWELVE_HOURS_TTL {
            String name = "DEFAULT_MAP_TWELVE_HOURS_TTL";
            MapInfo mapInfo = new MapInfo(MapType.DISTRIBUTED, TimeUnits.TWELVE_HOURS_TTL);
        }

        public interface HZ_ARTICLE_REPLICATED_MAP_TEN_MINUTES_TTL {
            String name = "HZ_ARTICLE_REPLICATED_MAP_TEN_MINUTES_TTL";
            MapInfo mapInfo = new MapInfo(MapType.REPLICATED, TimeUnits.TEN_MINUTES_TTL);
        }

        public interface HZ_ARTICLE_REPLICATED_MAP_THREE_MINUTES_TTL {
            String name = "HZ_ARTICLE_REPLICATED_MAP_THREE_MINUTES_TTL";
            MapInfo mapInfo = new MapInfo(MapType.REPLICATED, TimeUnits.THREE_MINUTES_TTL);
        }

        public interface HZ_ARTICLE_REPLICATED_MAP_TWELVE_HOURS_TTL {
            String name = "HZ_ARTICLE_REPLICATED_MAP_TWELVE_HOURS_TTL";
            MapInfo mapInfo = new MapInfo(MapType.REPLICATED, TimeUnits.TWELVE_HOURS_TTL);
        }

        public interface HZ_MAGAZINE_MAP_TEN_MINUTES_TTL {
            String name = "HZ_MAGAZINE_MAP_TEN_MINUTES_TTL";
            MapInfo mapInfo = new MapInfo(MapType.DISTRIBUTED, TimeUnits.TEN_MINUTES_TTL);
        }

        public interface HZ_PROPERTY_REPLICATED_MAP_TEN_MINUTES_TTL {
            String name = "HZ_PROPERTY_REPLICATED_MAP_TEN_MINUTES_TTL";
            MapInfo mapInfo = new MapInfo(MapType.REPLICATED, TimeUnits.TEN_MINUTES_TTL);
        }

        public interface HZ_PROPERTY_REPLICATED_MAP_TWELVE_HOURS_TTL {
            String name = "HZ_PROPERTY_REPLICATED_MAP_TWELVE_HOURS_TTL";
            MapInfo mapInfo = new MapInfo(MapType.REPLICATED, TimeUnits.TWELVE_HOURS_TTL);
        }

    }

    public static void prepareHazelcastMapInstancesTypes() {
        // distrubuted maps
        hazelcastMapInstanceTypes.put(HazelcastCacheNames.DEFAULT_MAP_FIVE_MINUTES_TTL.name , HazelcastCacheNames.DEFAULT_MAP_FIVE_MINUTES_TTL.mapInfo);
    }

    public static MapInfo getMapInfo(String cacheName) {
        MapInfo mapInfo = hazelcastMapInstanceTypes.get(cacheName);
        if (Objects.nonNull(mapInfo)) {
            return  mapInfo;
        } else {
            //add log
            return null;
        }
    }


}
