package es.upm.pproject.parkingjam.model;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomVehicleTest {

    @Test
    @DisplayName("Test randomVehicle method with size 2 and horizontal orientation")
    void testRandomVehicleSize2Horizontal() {
        
        RandomVehicle randomVehicle = new RandomVehicle();
        Casilla[] casillas = { new Casilla(0, 0) }; 
        Coche coche = new Coche('A', casillas, true); 

        
        String result = randomVehicle.randomVehicle(2, true, coche);

       
        assertNotNull(result);
        assertTrue(result.startsWith("greenCar_H.png") ||
                   result.startsWith("redTruck_H.png") ||
                   result.startsWith("yellowTruck_H.png"));
    }

    @Test
    @DisplayName("Test randomVehicle method with size 3 and vertical orientation")
    void testRandomVehicleSize3Vertical() {
     
        RandomVehicle randomVehicle = new RandomVehicle();
        Casilla[] casillas = { new Casilla(0, 0), new Casilla(1, 0), new Casilla(2, 0) }; 
        Coche coche = new Coche('B', casillas, false); 

  
        String result = randomVehicle.randomVehicle(3, false, coche);

   
        assertNotNull(result);
        assertTrue(result.startsWith("whiteTruck.png") ||
                   result.startsWith("blueTruck.png"));
    }

    @Test
    @DisplayName("Test randomVehicle method with invalid size")
    void testRandomVehicleInvalidSize() {
        RandomVehicle randomVehicle = new RandomVehicle();
        Casilla[] casillas = { new Casilla(0, 0) }; 
        Coche coche = new Coche('C', casillas, true); 

        String result = randomVehicle.randomVehicle(4, true, coche);

        assertEquals("Not found", result);
    }
}
