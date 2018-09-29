package bshpvs.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CellTypeTest {

    @Test
    public void testShipValue() {
        assertEquals(1, CellType.SHIP.getValue());
    }

    @Test
    public void testWaterValue() {
        assertEquals(0, CellType.WATER.getValue());
    }
}
