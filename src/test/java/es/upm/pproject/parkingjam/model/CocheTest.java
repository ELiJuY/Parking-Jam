package es.upm.pproject.parkingjam.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CocheTest {

    @Test
    @DisplayName("Test creating a car instance and accessing its methods")
    void testCarInstanceAndMethods() {
        Casilla[] casillas = { new Casilla(0, 0), new Casilla(0, 1) }; 
        Coche coche = new Coche('C', casillas, true); 

        char id = coche.getId();
        Casilla[] casillasObtenidas = coche.getCasillas();
        int size = coche.getSize();
        boolean isHorizontal = coche.isHorizontal();
        boolean isRedCar = coche.isRedCar();

        assertEquals('C', id);
        assertArrayEquals(casillas, casillasObtenidas);
        assertEquals(2, size); 
        assertTrue(isHorizontal); 
        assertFalse(isRedCar); 
    }
    
    
    @Test
    @DisplayName("Test setting new casillas to the car")
    void testSetCasillas() {
        Casilla[] casillas = { new Casilla(0, 0) }; 
        Coche coche = new Coche('C', casillas, true); 

        Casilla[] nuevasCasillas = { new Casilla(1, 0), new Casilla(2, 0), new Casilla(3, 0) };
        coche.setCasillas(nuevasCasillas);

        assertArrayEquals(nuevasCasillas, coche.getCasillas());
        assertEquals(3, coche.getSize()); 
    }
    @Test
    @DisplayName("Test setters and getters")
    void testSettersAndGetters() {
        Casilla[] casillas = { new Casilla(0, 0), new Casilla(0, 1) };
        Coche coche = new Coche('C', casillas, true); 

        coche.setId('D');
        coche.setHorizontal(false);
        coche.setRedCar(true);

        assertEquals('D', coche.getId());
        assertFalse(coche.isHorizontal()); 
        assertTrue(coche.isRedCar()); 
    }
}
