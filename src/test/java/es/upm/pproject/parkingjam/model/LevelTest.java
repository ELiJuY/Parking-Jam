package es.upm.pproject.parkingjam.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

class LevelTest {

    private Level level;

    @BeforeEach
    void setUp() throws LevelException,IOException {
        level = new Level();
        level.loadLevel("src/test/resources/levels/levelTest.txt");
    }

    @Test
    @DisplayName("Load valid level ")
    void testLoadLevelValid() throws IOException {
  
        assertEquals("Test Level", level.getName());
        assertEquals(8, level.getnRows());
        assertEquals(8, level.getnColumns());
    }

    @Test
    @DisplayName("Load level not found")
    void testLoadLevelNotFound() { 
        assertThrows(IOException.class, () -> {
            level.loadLevel("src/test/resources/levels/levelNoExiste.txt");
        });
    }
    
    @Test
    @DisplayName("Load level with invalid format")
    void testLoadInvalidLevel() { 
    	assertThrows(LevelException.class, () -> {
    		String filePath = "src/test/resources/levels/levelInvalidTest.txt";
    		level.loadLevel(filePath);
        });	
    }
    @Test
    @DisplayName("Load level with invalid format case 2")
    void testLoadInvalidLevel2() { 
    	assertThrows(LevelException.class, () -> {
    		String filePath = "src/test/resources/levels/levelInvalidTest2.txt";
    		level.loadLevel(filePath);
        });	
    }
    

    @Test
    @DisplayName("Car is at the exit")
    void testIsExit() {
        Coche coche = new Coche('*', 
        		new Casilla[]{new Casilla(5, 4), new Casilla(6, 4), new Casilla(7, 4)}, 
        		false);
        Casilla salida = level.getSalida();
        assertEquals(salida, coche.getCasillas()[coche.getSize()-1]);
        assertTrue(level.isExit(coche));
    }

    @Test
    @DisplayName("Valid move to the right")
    void testIsValidMove1() {
        Coche coche = level.getCoche('a');
        Movimiento movimiento = new Movimiento(coche, new Casilla(1, 3));
        assertFalse(level.isValidMove(movimiento));
 
    }
    @Test
    @DisplayName("Valid move to the left")
    void testIsValidMove2() {
        Coche coche = level.getCoche('a');
        Movimiento movimiento = new Movimiento(coche, new Casilla(1, 0));
        assertFalse(level.isValidMove(movimiento));
    }
    
    @Test
    @DisplayName("Valid move up")
    void testIsValidMove3() {
        Coche coche = level.getCoche('c');
        Movimiento movimiento = new Movimiento(coche, new Casilla(0, 6));
        assertFalse(level.isValidMove(movimiento));
 
    }
    
    @Test
    @DisplayName("Valid move down")
    void testIsValidMove4() {
        Coche coche = level.getCoche('c');
        Movimiento movimiento = new Movimiento(coche, new Casilla(3, 6));     
        assertTrue(level.isValidMove(movimiento));
    }
    
    
    @Test
    @DisplayName("Move cars successfully")
    void testMoveCar() {
        Coche coche = level.getCoche('c');
        Movimiento movimiento = new Movimiento(coche, new Casilla(3, 6));
        level.moveCar(movimiento);
        assertEquals(new Casilla(3, 6), coche.getCasillas()[0]);
        
        coche = level.getCoche('b');
        movimiento = new Movimiento(coche, new Casilla(1, 4));
        level.moveCar(movimiento);
        assertEquals(new Casilla(1, 4), coche.getCasillas()[0]);
        
    }
    
    @Test
    @DisplayName("Move cars unsuccessfully")
    void testMoveCarInvalid() {
    	 Coche coche = level.getCoche('c');
         Movimiento movimiento = new Movimiento(coche, new Casilla(0, 6));
         level.moveCar(movimiento);
         assertNotEquals(new Casilla(0, 6), coche.getCasillas()[0]);
         
         coche = level.getCoche('b');
         movimiento = new Movimiento(coche, new Casilla(1, 4));
         level.moveCar(movimiento);
         assertNotEquals(new Casilla(1, 4), coche.getCasillas()[0]);
    }

    @Test
    @DisplayName("Add a move to the stack")
    void testAddMovement() {
        Coche coche = new Coche('*', new Casilla[]{new Casilla(1, 2)}, true);
        Casilla origen = new Casilla(1, 2);
        assertTrue(level.getMovimientos().isEmpty());
        level.addMovement(coche, origen);
        assertFalse(level.getMovimientos().isEmpty());
    }

    @Test
    @DisplayName("Check if the last movement could be undone")
    void testUndoLastMove() {
        Coche coche = level.getCoche('c');
        Casilla origen = new Casilla(coche.getCasillas()[0].getX(), coche.getCasillas()[0].getY());
        Movimiento movimiento = new Movimiento(coche, new Casilla(3, 6));
        level.moveCar(movimiento);
        level.addMovement(coche, origen);
        Coche movedCoche = level.undoLastMove();
        assertNotNull(movedCoche);
        assertEquals(coche.getCasillas()[0], movedCoche.getCasillas()[0]);
    }

    @Test
    @DisplayName("Save and load a level successfully")
    void testSaveAndLoadLevel() throws IOException,LevelException {
        String filePath = "src/test/resources/levels/savedLevelTest.txt";
        Coche coche = level.getCoche('c');
        Casilla origen = new Casilla(coche.getCasillas()[0].getX(), coche.getCasillas()[0].getY());
        Movimiento movimiento = new Movimiento(coche, new Casilla(3, 6));
        level.moveCar(movimiento);
        level.addMovement(coche, origen);
        level.saveLevel(filePath, 20);
        Level loadedLevel = new Level();
        int scoreTotalBase = loadedLevel.loadSavedLevel(filePath);
        assertEquals(level.getName(), loadedLevel.getName());
        assertEquals(level.getnRows(), loadedLevel.getnRows());
        assertEquals(level.getnColumns(), loadedLevel.getnColumns());
        assertEquals(20, scoreTotalBase);
    }
    
    @Test
    @DisplayName("Load a saved level that does not exist")
    void testLoadNonExistentSavedLevel(){
    	assertThrows(IOException.class, () -> {
    		String filePath = "src/test/resources/levels/missing.txt";
        	Level loadedLevel = new Level();
        	loadedLevel.loadSavedLevel(filePath);
        });	
    }
    
    @Test
    @DisplayName("Load a saved level with invalid format")
    void testLoadInvalidSavedLevel() { 
    	assertThrows(LevelException.class, () -> {
    		String filePath = "src/test/resources/levels/savedInvalidLevelTest.txt";
        	Level loadedLevel = new Level();
        	loadedLevel.loadSavedLevel(filePath);
        });	
    }
    @Test
    @DisplayName("Load a saved level with invalid format case 2")
    void testLoadInvalidSavedLevel2() { 
    	assertThrows(LevelException.class, () -> {
    		String filePath = "src/test/resources/levels/savedInvalidLevelTest2.txt";
        	Level loadedLevel = new Level();
        	loadedLevel.loadSavedLevel(filePath);
        });
    }
    
}
