
package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HelloControllerTest {

    @Test
    void helloShouldReturnGreeting() {
        HelloController controller = new HelloController();
        String response = controller.hello();
        assertEquals("Hello there, ModMed CI/CD!", response);
    }
}
