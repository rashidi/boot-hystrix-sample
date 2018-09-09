package rz.sample.boot.hystrix.client.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Rashidi Zin
 */
@Service
public class HelloService {

    private final RestTemplate restTemplate;

    public HelloService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @HystrixCommand(fallbackMethod = "greetFallback")
    public String greet(String name) {
        return restTemplate.getForEntity(
                "http://localhost:1511/greet?greet=Hello&name={name}", String.class, name
        )
                .getBody();
    }

    @SuppressWarnings("unused")
    private String greetFallback(String name) {
        return "Sorry " + name + ". Service is currently unavailable.";
    }

}
