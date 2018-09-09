package rz.sample.boot.hystrix.service.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Rashidi Zin
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class GreetResourceTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void greet() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/greet?greet={greet}&name={name}", String.class, "Hello", "Rashidi Zin"
        );

        assertThat(response)
                .extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
                .containsExactly(OK, "Hello Rashidi Zin");
    }
}