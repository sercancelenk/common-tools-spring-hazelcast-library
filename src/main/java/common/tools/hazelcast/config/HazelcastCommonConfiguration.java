package common.tools.hazelcast.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author byzasttech
 */

@Configuration
@ComponentScan(basePackages = {"common.tools.hazelcast"})
public class HazelcastCommonConfiguration {
}
