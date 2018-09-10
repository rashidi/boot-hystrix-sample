# Hystrix With Spring Boot [![Build Status](https://travis-ci.org/rashidi/boot-hystrix-sample.svg?branch=master)](https://travis-ci.org/rashidi/boot-hystrix-sample)
Sample application that demonstrates usage of Hystrix on top of Spring Boot.

There are two applications bundled in this sample; [service][1] and [client][2].

## Service
Service is a simple web application that contains [GreetResource][3] and [runs on port 1511][4]. Implementation are
as the following:

### GreetResource.java
```java
@RestController
public class GreetResource {

    @GetMapping("/greet")
    private String greet(@RequestParam String greet, @RequestParam String name) {
        return greet + " " + name;
    }

}
```

### application.properties
```properties
server.port=1511
```

The idea here is to have client application as the middle application between users and service application. Client
application should be able to inform the users when service application is not available. For this we will use Hystrix.

## Client
In order to include Hystrix in our application we will need to include `spring-cloud-starter-netflix-hystrix` as a
dependency.

### [build.gradle][5]
```text
ext {
    springCloudVersion = 'Finchley.SR1'
}

dependencies {
    compile('org.springframework.cloud:spring-cloud-starter-netflix-hystrix')
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}
```

Next is to enable Circuit Break and Hystrix for the application.

### [HystrixConfiguration.java][6]
```java
@Configuration
@EnableCircuitBreaker
@EnableHystrix
public class HystrixConfiguration {
}
```

We will then proceed by implementing a service which will communicate with service application with
Hystrix enabled.

### [HelloService.java][7]
```java
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
```

In the implementation above. We are informing Hystrix that should an error occurred when communicating
with service application, we will fallback to offer an apology.

Finally a REST resource which will be used by the users.

### [HelloResource.java][8]
```java
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
```

## Running the Application
We will run both client and service application. Both applications are available at port 1511 and 8080
respectively.

### Both applications are running
Once both are up and running we will trigger a REST request to `http://localhost:8080/hello` with parameter `name=Rashidi`

```http request
GET http://localhost:8080/hello?name=Rashidi
Accept: */*
Cache-Control: no-cache
```

Since both applications are up we will get a successful response:

```text
Hello Rashidi
```

### Service application not available
Next is to demonstrate that service application is not running. We will first stop service application.
Then trigger the same `GET` request:

```http request
GET http://localhost:8080/hello?name=Rashidi
Accept: */*
Cache-Control: no-cache
```

Due to service application unavailable, the users will be given the following response:

```text
Sorry Rashidi. Service is currently unavailable.
```

## License
```text
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org/>
```

[1]: service
[2]: client
[3]: service/src/main/java/rz/sample/boot/hystrix/service/web/GreetResource.java
[4]: service/src/main/resources/application.properties
[5]: client/build.gradle
[6]: client/src/main/java/rz/sample/boot/hystrix/client/configuration/HystrixConfiguration.java
[7]: client/src/main/java/rz/sample/boot/hystrix/client/service/HelloService.java
[8]: client/src/main/java/rz/sample/boot/hystrix/client/web/HelloResource.java