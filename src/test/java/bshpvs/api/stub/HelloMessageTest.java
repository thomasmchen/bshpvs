package bshpvs.api.stub;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloMessageTest {

    @Test
    void getName() {
        HelloMessage testMsg = new HelloMessage("test");
        assertEquals("test", testMsg.getName());
    }

    @Test
    void setName() {
        HelloMessage testMsg = new HelloMessage("test");
        testMsg.setName("new test");
        assertEquals("new test", testMsg.getName());
    }
}