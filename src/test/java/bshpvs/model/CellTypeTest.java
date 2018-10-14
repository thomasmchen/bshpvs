package bshpvs.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by thomas on 9/21/18.
 */
public class CellTypeTest {
    @Test
    public void testGetValue() {
        assertEquals(5, CellType.CARRIER.getValue());
        assertEquals(4, CellType.BATTLESHIP.getValue());
        assertEquals(3, CellType.SUBMARINE.getValue());
        assertEquals(3, CellType.CRUISER.getValue());
        assertEquals(2, CellType.DESTROYER.getValue());
    }
}
