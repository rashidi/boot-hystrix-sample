package rz.sample.boot.hystrix.client.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rz.sample.boot.hystrix.client.service.HelloService;

/**
 * @author Rashidi Zin
 */
@RestController
public class HelloResource {

    private final HelloService service;

    public HelloResource(HelloService service) {
        this.service = service;
    }

    @GetMapping("/hello")
    public String greet(@RequestParam String name) {
        return service.greet(name);
    }

}
