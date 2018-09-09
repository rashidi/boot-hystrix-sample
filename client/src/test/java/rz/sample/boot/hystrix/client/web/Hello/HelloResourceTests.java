package rz.sample.boot.hystrix.client.web.Hello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import rz.sample.boot.hystrix.client.service.HelloService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Rashidi Zin
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class HelloResourceTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private HelloService service;

    @Test
    public void greet() {
        doReturn("Hello Rashidi")
                .when(service).greet("Rashidi");

        ResponseEntity<String> response = restTemplate.getForEntity(
                "/hello?name={name}", String.class, "Rashidi"
        );

        assertThat(response)
                .extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
                .containsExactly(OK, "Hello Rashidi");
    }
}