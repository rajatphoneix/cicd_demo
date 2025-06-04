
package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {

    @Test
    void helloShouldReturnGreeting() {
        HelloController controller = new HelloController();
        String response = controller.hello();
        assertEquals("Hello, ModMed CI/CD!", response);
    }
}
