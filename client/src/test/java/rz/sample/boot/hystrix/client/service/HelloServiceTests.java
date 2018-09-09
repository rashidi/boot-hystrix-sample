package rz.sample.boot.hystrix.client.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author Rashidi Zin
 */
@RunWith(SpringRunner.class)
@RestClientTest(HelloService.class)
public class HelloServiceTests {

    @Autowired
    private HelloService service;

    @Autowired
    private MockRestServiceServer server;

    @Test
    public void successfulGreet() {
        server
                .expect(requestTo("http://localhost:1511/greet?greet=Hello&name=Rashidi"))
                .andRespond(withSuccess("Hello Rashidi", APPLICATION_JSON));

        assertThat(service.greet("Rashidi"))
                .isEqualTo("Hello Rashidi");
    }
}