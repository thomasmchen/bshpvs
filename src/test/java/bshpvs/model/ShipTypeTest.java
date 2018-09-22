package bshpvs.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by thomas on 9/21/18.
 */
public class ShipTypeTest {
    @Test
    public void testGetValue() {
        assertEquals(5, ShipType.CARRIER.getValue());
        assertEquals(4, ShipType.BATTLESHIP.getValue());
        assertEquals(3, ShipType.SUBMARINE.getValue());
        assertEquals(3, ShipType.CRUISER.getValue());
        assertEquals(2, ShipType.DESTROYER.getValue());
    }
}
