package rz.sample.boot.hystrix.service.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rashidi Zin
 */
@RestController
public class GreetResource {

    @GetMapping("/greet")
    private String greet(@RequestParam String greet, @RequestParam String name) {
        return greet + " " + name;
    }

}
