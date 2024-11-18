package es.upm.pproject.parkingjam.model;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;


class GameTest {
	Game game;
	String path = "src/test/resources/levels/level_";
    @BeforeEach
    public void setUp() throws IOException,LevelException {
        game = new Game(path);
        
        game.iniciarPartida(1, false);
        game.getMainThemeSound().close();
    }
    @Test
    @DisplayName("Test iniciarPartida when loading existing level")
    void testIniciarPartidaExistingLevel() throws LevelException, IOException {
        game.iniciarPartida(2, false);

        assertEquals(2, game.getNLevel());
    }

    @Test
    @DisplayName("Test iniciarPartida when loading non-existing level")
    void testIniciarPartidaNonExistingLevel() throws LevelException, IOException {
        
        assertThrows(IOException.class, () -> {
            game.iniciarPartida(999, false);
        });
    }
    
    @Test
    @DisplayName("Test iniciarPartida when loading invalid level")
    void testIniciarPartidaInvalidLevel() throws LevelException, IOException {
    	 assertThrows(LevelException.class, () -> {
             game.iniciarPartida(7, false);
         });
         
    }
    
    @Test
    @DisplayName("Load first level automatically ")
    void testLoadLevel() throws IOException,LevelException {
        boolean result = game.loadLevel(1,path,true);
        game.getMainThemeSound().close();
        assertTrue(result);
 
        assertEquals(game.getNLevel(), game.getLevel().getNivel());
    }
    
    @Test
    @DisplayName("Load nonexistent level automatically ")
    void testLoadNonexistentLevel() throws IOException { 
    	 assertThrows(IOException.class, () -> {
    		 game.loadLevel(888,path,true);
         });
    }
    
    @Test
    @DisplayName("Load invalid level automatically ")

    void testLoadInvalidLevel() throws IOException,LevelException {
    	assertFalse(game.loadLevel(7,path,true));

    }
    
    @Test
    @DisplayName("Test restart game")
    void testRestartGame() throws LevelException, IOException {
    	game.incrementScore();
    	int prevScoreLevel =  game.getLevel().getScoreNivel();
    	game.restartGame(false);
    	assertNotEquals(prevScoreLevel, game.getLevel().getScoreNivel());
    	assertEquals(0, game.getLevel().getScoreNivel());
    	
    }
    
    
    @Test
    @DisplayName("Test isExit when exit is reached")
    void testIsExitReached() throws LevelException, IOException {
        
        Coche redCar = game.getLevel().getCocheRojo();
        Casilla exit = game.getLevel().getSalida();
        exit.setX(redCar.getCasillas()[redCar.getSize()-1].getX());
        exit.setY(redCar.getCasillas()[redCar.getSize()-1].getY());
        
        
        boolean exitReached = game.isExit(redCar);

        
        assertTrue(exitReached);
        
    }

    @Test
    @DisplayName("Test isExit method when exit is not reached")
    void testIsExitNotReached() throws LevelException {
        
        Coche nonExitCar = game.getLevel().getCoches().get(1); 

        
        boolean exitReached = game.isExit(nonExitCar);

        
        assertFalse(exitReached);
        assertEquals(1, game.getNLevel()); 
    }
    


    @Test
    @DisplayName("Test incrementScore method")
    void testIncrementScore() {
        
        int initialScoreTotal = game.getScoreTotal();
        int initialScoreNivel = game.getLevel().getScoreNivel();

        game.incrementScore();

        assertEquals(initialScoreNivel + 1, game.getLevel().getScoreNivel());
        assertEquals(initialScoreTotal + 1, game.getScoreTotal());
    }
    
    @Test
    @DisplayName("Save and load a valid game")
    void testSaveAndLoadGame() throws IOException,LevelException {
    	String filePath = "src/test/resources/levels/savedLevelTest.txt";
    	game.saveGame(filePath);
    	Game gameSaved= new Game(path);
        gameSaved.loadSavedGame(filePath);
        assertEquals(gameSaved.getNLevel(), game.getNLevel());
        assertEquals(gameSaved.getScoreTotalBase(), game.getScoreTotalBase());
        assertEquals(gameSaved.getScoreTotal(), game.getScoreTotal()); 
        
    }
    
    @Test
    @DisplayName("Load a saved game that does not exist")
    void testLoadSavedNonExistentGame() throws IOException {
    	 assertThrows(IOException.class, () -> {
    		 game.loadSavedGame("src/test/resources/missing.txt");
         });
       
    }
    
    @Test
    @DisplayName("Load a saved game with invalid format")
    void testLoadSavedInvalidGame() throws IOException,LevelException { 
    	String filePath = "src/test/resources/levels/savedInvalidGameTest.txt";
    	assertThrows(LevelException.class, () -> {
   		 game.loadSavedGame(filePath);
        });
 
    }

   
}
