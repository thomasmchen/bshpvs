package bshpvs.stub;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnitTestStubTest {

    private static UnitTestStub subject;

    @BeforeAll
    public static void setup() {
        subject = new UnitTestStub();
    }

    @Test
    public void testGetMessage() {
        String input = "Hello World!";
        assertEquals(input, subject.getMessage(input));
    }
}

