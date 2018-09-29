package bshpvs.api.stub;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GreetingTest {

    @Test
    void getContent() {
        Greeting testGreet = new Greeting("Test");
        assertEquals("Test", testGreet.getContent());
    }
}