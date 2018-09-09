package rz.sample.boot.hystrix.client.configuration;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rashidi Zin
 */
@Configuration
@EnableCircuitBreaker
@EnableHystrix
public class HystrixConfiguration {
}
