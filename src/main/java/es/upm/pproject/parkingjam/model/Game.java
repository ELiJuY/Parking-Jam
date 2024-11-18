package es.upm.pproject.parkingjam.model;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import org.apache.log4j.Logger;

import sounds.Sound;

public class Game {
    private Level level;
    private int scoreTotal;
    private int scoreTotalBase;
    private int nLevel;
    
    public static int getTotalLevels() {
		return totalLevels;
	}

	private static int totalLevels=countLevels();
    private Sound mainThemeSound;
    private String path;

    private static final Logger logger = Logger.getLogger(Game.class);

    public String getPath() {
        return path;
    }

    public final void setPath(String path) {
        this.path = path;
    }

    public Game(String path) {
        setScoreTotal(0);
        setScoreTotalBase(0);
        setMainThemeSound(new Sound("src/main/resources/sounds/MainTheme.wav"));
        setPath(path);
        logger.info("Game initialized with path: " + path);
        
    }

    public void iniciarPartida(int nLevel, boolean playMusic) throws IOException, LevelException {
        boolean i = loadLevel(nLevel, getPath(), playMusic);
        if (!i) {
            logger.error("Failed to load level: " + nLevel + ". Invalid level format");
            throw new LevelException();
        }
        logger.info("Game started at level: " + nLevel);
    }

    public boolean loadLevel(int nLevel, String path, boolean music) throws IOException,LevelException {
        String nameLevel = path + nLevel + ".txt";
        setNLevel(nLevel);
        level = new Level();
        level.loadLevel(nameLevel);
        level.setNivel(nLevel);

        if (!isLevelValid()) {
            logger.warn("Level " + nLevel + " is not valid");
            return false;
        }

        if (music) {
            getMainThemeSound().play();
        }

        logger.info("Level " + nLevel + " loaded successfully");
        return true;
    }

    public boolean loadSavedGame(String filePath) throws IOException,LevelException {
        level = new Level();
        this.scoreTotalBase = level.loadSavedLevel(filePath);
        this.nLevel = level.getNivel();
        this.scoreTotal = scoreTotalBase + level.getScoreNivel();
        if (!isLevelValid()) {
            logger.error("Saved game at " + filePath + " is not valid");
            throw new LevelException();
        }

        getMainThemeSound().play();
        logger.info("Saved game loaded correctly from " + filePath);
        return true;
    }

    public void saveGame(String filePath) throws IOException {
        level.saveLevel(filePath, scoreTotalBase);
        logger.info("Game saved to " + filePath);
    }
    
    public void restartGame(boolean music) throws IOException, LevelException {
    	logger.info("Restarting level: " + nLevel);
    	iniciarPartida(nLevel,music);
    }

    public int getScoreTotalBase() {
        return scoreTotalBase;
    }

    public final void setScoreTotalBase(int scoreTotalBase) {
        this.scoreTotalBase = scoreTotalBase;
    }

    public boolean isExit(Coche c) {
    	if(getLevel().isExit(c)) {
    		setScoreTotalBase(getScoreTotal());
    		return true;
    	}
    	return false;
    }

    private boolean isLevelValid() {
        if (level == null) return false;
        else
            if (level.getSalida() == null) return false;
            else {
                if (level.getCocheRojo() == null || level.getCocheRojo().getSize() != 2) {
                    return false;
                }

                for (Coche coche : level.getCoches()) {
                    if (!isVehicleValid(coche)) {
                        return false;
                    }
                }
                return level.getCocheRojo().isHorizontal() ? level.getCocheRojo().getCasillas()[0].getX() == level.getSalida().getX() : level.getCocheRojo().getCasillas()[0].getY() == level.getSalida().getY();
            }
    }

    private boolean isVehicleValid(Coche coche) {
        Casilla[] casillas = coche.getCasillas();

        if (coche.isHorizontal()) {
            for (int i = 1; i < casillas.length; i++) {
                if (casillas[i].getX() != saca(casillas, 0).getX()) {
                    return false;
                }
            }
        } else {
            for (int i = 1; i < casillas.length; i++) {
                if (casillas[i].getY() != saca(casillas, 0).getY()) {
                    return false;
                }
            }
        }

        if (casillas.length < 2) {
            return false;
        }
        for (int i = 1; i < casillas.length; i++) {
            if (coche.isHorizontal()) {
                if (casillas[i].getY() != casillas[i - 1].getY() + 1) {
                    return false;
                }
            } else {
                if (casillas[i].getX() != casillas[i - 1].getX() + 1) {
                    return false;
                }
            }
        }

        int neighbourCount = 0;
        char[][] casillasOcupNoRep = charMCopy(level.getParkingGrid());
        for (Casilla casilla : casillas) {
            neighbourCount += countNeighbours(casilla, casillasOcupNoRep);
        }
        boolean lastresult = true;
        if (neighbourCount < 2) lastresult = false;

        return lastresult;
    }

    public static char[][] charMCopy(char[][] original) {
        if (original == null) return original;

        final char[][] result = new char[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

    private int countNeighbours(Casilla casilla, char[][] casillas) {
        int x = casilla.getX();
        int y = casilla.getY();
        char e = casillas[x][y];
        int n = 0;
        int[] casX = {-1, 0, 1, 1, 1, 0, -1, -1};
        int[] casY = {1, 1, 1, 0, -1, -1, -1, 0};
        for (int i = 0; i < 8; i++) {
            int xA = x + casX[i];
            int yA = y + casY[i];
            char eA = casillas[xA][yA];
            if (isOccupied(eA) && eA != e) {
                n++;
                casillas[xA][yA] = ' ';
            }
        }
        return n;
    }

    private boolean isOccupied(char cell) {
        return cell != ' ' && cell != '@';
    }

    public void incrementScore() {
        setScoreNivel(getScoreNivel() + 1);
        setScoreTotal(getScoreTotalBase() + getLevel().getScoreNivel());
        logger.info("Score incremented. Total Score: " + getScoreTotal() + ", Level Score: " + getLevel().getScoreNivel());
    }
    
    private static int countLevels() {
		String directoryPath = "src/main/resources/levels";
		int levelCount = 0;
		while (true) {
			String fileName = "level_" + (levelCount + 1) + ".txt";
			Path filePath = Paths.get(directoryPath, fileName);
			if (Files.exists(filePath)) {
				levelCount++;
			} else {
				break;
			}
		}
		return levelCount;
	}

    private void setScoreNivel(int scoreNivel) {
        getLevel().setScoreNivel(scoreNivel);
    }

    private int getScoreNivel() {
        return getLevel().getScoreNivel();
    }

    public Level getLevel() {
        return level;
    }

    public int getScoreTotal() {
        return scoreTotal;
    }

    public final void setScoreTotal(int scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public int getNLevel() {
        return nLevel;
    }

    public void setNLevel(int nLevel) {
        this.nLevel = nLevel;
    }

    public Sound getMainThemeSound() {
        return mainThemeSound;
    }

    public final void setMainThemeSound(Sound mainThemeSound) {
        this.mainThemeSound = mainThemeSound;
    }

    private Casilla saca(Casilla[] casillas, int y) {
        return casillas[y];
    }
}
