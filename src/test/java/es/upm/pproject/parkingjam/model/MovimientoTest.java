package es.upm.pproject.parkingjam.model;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class MovimientoTest {

	    private Coche cocheHorizontal;
	    private Coche cocheVertical;
	    private Casilla[] casillasHorizontales;
	    private Casilla[] casillasVerticales;
	    private Casilla destinoHorizontal;
	    private Casilla destinoVertical;

	    @BeforeEach
	    void setUp() {
	        
	        casillasHorizontales = new Casilla[]{
	                new Casilla(0, 0),
	                new Casilla(0, 1),
	                new Casilla(0, 2)
	        };
	        cocheHorizontal = new Coche('a',casillasHorizontales, true);

	       
	        casillasVerticales = new Casilla[]{
	                new Casilla(0, 0),
	                new Casilla(1, 0),
	                new Casilla(2, 0)
	        };
	        cocheVertical = new Coche('b',casillasVerticales, false);

	        
	        destinoHorizontal = new Casilla(0, 5);
	        destinoVertical = new Casilla(5, 0);
	    }

	    @Test
	    @DisplayName("Horizontal movement was successful")
	    void testEjecutarMovimientoHorizontal() {
	        Movimiento movimiento = new Movimiento(cocheHorizontal, destinoHorizontal);
	        movimiento.ejecutarMovimiento();

	        Casilla[] casillas = cocheHorizontal.getCasillas();
	        assertEquals(destinoHorizontal.getX(), casillas[0].getX());
	        assertEquals(destinoHorizontal.getY(), casillas[0].getY());
	    }

	    @Test
	    @DisplayName("Vertical movement was successful")
	    void testEjecutarMovimientoVertical() {
	        Movimiento movimiento = new Movimiento(cocheVertical, destinoVertical);
	        movimiento.ejecutarMovimiento();

	        Casilla[] casillas = cocheVertical.getCasillas();
	        assertEquals(destinoVertical.getX(), casillas[0].getX());
	        assertEquals(destinoVertical.getY(), casillas[0].getY());
	    }
	    
	    @Test
	    @DisplayName("Test set car to move")
	    void testSetCoche() {
	        Coche nuevoCoche = cocheVertical; 
	        Movimiento movimiento = new Movimiento(cocheHorizontal, destinoHorizontal);
	        assertNotEquals(nuevoCoche, movimiento.getCoche());
	        movimiento.setCoche(nuevoCoche);
	        assertEquals(nuevoCoche, movimiento.getCoche());
	    }

	    @Test
	    @DisplayName("Test set destination to move")
	    void testSetDestino() {
	        Casilla nuevaCasilla = new Casilla(3, 4); 
	        Movimiento movimiento = new Movimiento(cocheHorizontal, destinoHorizontal);
	        assertNotEquals(nuevaCasilla, movimiento.getDestino());
	        movimiento.setDestino(nuevaCasilla);
	        assertEquals(nuevaCasilla, movimiento.getDestino());
	    }
	    
}
