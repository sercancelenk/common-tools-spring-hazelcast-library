package common.tools.hazelcast.annotation;

import common.tools.hazelcast.config.HazelcastCommonConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author byzasttech
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({HazelcastCommonConfiguration.class})
public @interface EnableCacheLibrary {
}
