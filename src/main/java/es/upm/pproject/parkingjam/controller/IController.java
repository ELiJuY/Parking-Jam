package es.upm.pproject.parkingjam.controller;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import es.upm.pproject.parkingjam.model.*;

public interface IController {
    boolean isValidMove(Coche coche, Casilla destino);
    void incrementScore();
    void move(Coche coche, Casilla destino);
    void addMovement(Coche coche, Casilla origen);
    void undo();
    void loadSavedGame(String filePath) throws IOException;
    void saveGame(String filePath) throws IOException;
    void isExit(Coche c);
    void showMenu();
    void stopMainTheme();
    void playMainTheme();
    void restart();
    void backToMenu();
    void iniciarPartida(int nLevel);

    Game getGame();
    int getTotalLevels();

    List<Coche> getCoches();
    Coche getCocheRojo();
    Casilla getSalida();
    List<Casilla> getMuros();
    Set<Casilla> getBlancos();
}
