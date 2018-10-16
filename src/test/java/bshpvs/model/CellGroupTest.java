package bshpvs.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellGroupTest {
    private static CellType ct;

    @BeforeAll
    private static void setup() {
        ct = new Cell().getType();
    }


    @Test
    void getText() {
        assertEquals("TERRAIN",ct.getGroup().getText());

    }

    @Test
    void getValue() {
        assertEquals(0,ct.getGroup().getValue());
    }
}