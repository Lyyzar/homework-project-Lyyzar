package rokafogo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CoordinateTest {

    Coordinate coordinate;

    @BeforeEach
    void init(){
        coordinate = new Coordinate(3,4);
    }

    @Test
    void getXTest(){
        assertEquals(3,coordinate.getX());
        assertNotEquals(4,coordinate.getX());
    }


    @Test
    void getYTest(){
        assertEquals(4,coordinate.getY());
        assertNotEquals(3,coordinate.getY());
    }

}
