package es.upm.pproject.parkingjam.controller;

import java.awt.Point;
import java.io.IOException;
import java.util.*;
import javax.swing.JOptionPane;


import es.upm.pproject.parkingjam.model.*;
import es.upm.pproject.parkingjam.view.ErrorMessage;
import es.upm.pproject.parkingjam.view.MenuInicio;


import es.upm.pproject.parkingjam.view.VentanaLevel;

import sounds.Sound;

public class Controller implements IController{
	private MenuInicio menu;
	private VentanaLevel ventanaLevel;
	private Game game;
	
	private static final int N = 50;

	private Sound mainThemeSound;

	public Controller(MenuInicio menu,Game game) {
		mainThemeSound = new Sound("src/main/resources/sounds/MainTheme.wav");
		this.menu = menu;
		this.game = game;

	}


	public boolean isValidMove(Coche coche, Casilla destino) {
		Movimiento m = new Movimiento(coche, destino);
		return game.getLevel().isValidMove(m);

	}
	
	
	public void incrementScore() {

		game.incrementScore();
		ventanaLevel.updateScore(game.getLevel().getScoreNivel(),game.getScoreTotal());
	}

	public void move(Coche coche, Casilla destino) {
		Movimiento m = new Movimiento(coche, destino);
		game.getLevel().moveCar(m);

	}

	public void addMovement(Coche coche, Casilla origen) {
		game.getLevel().addMovement(coche,origen);
	}

	public void undo() {
		Coche coche = game.getLevel().undoLastMove();
		if(coche != null) {
			int x = coche.getCasillas()[0].getX();
			int y = coche.getCasillas()[0].getY();
			ventanaLevel.updateLevelPanel(coche.getId(), new Point(N*y,N*x));
		}
	}

	public void loadSavedGame(String filePath)  {

		try {
			game.loadSavedGame(filePath);
			enableLevels();

			String nivelNombre = game.getLevel().getName();
			
			updateVentana(nivelNombre);
		}catch(IOException e) { 
			JOptionPane.showMessageDialog(menu, "Error reading the file: Unable to open the specified file");
		}catch(LevelException e) {
			JOptionPane.showMessageDialog(menu, "Error reading the file: The format is invalid");
		}	
	}

	public void saveGame(String filePath) throws IOException{
		game.saveGame(filePath);
	}

	public void isExit(Coche c) {
			if(game.isExit(c)) {
				iniciarPartida(game.getNLevel()+1);
			}
	

	}
	private void enableLevels() {
		for(int i = 1; i <= game.getNLevel(); i++)
			menu.enableLevel(i);
	}


	public void showMenu() {
		menu.setVisible(true);
	}

	public void stopMainTheme() {
		pauseMainTheme();
		if (mainThemeSound != null) {
			ventanaLevel.setMusicPlaying(false);
		}
	}

	public void playMainTheme() {
		if (mainThemeSound != null) {
			game.getMainThemeSound().play(); 
			ventanaLevel.setMusicPlaying(true);
		}
	}
	
    public void pauseMainTheme() {
    	 if (mainThemeSound != null) {
         	game.getMainThemeSound().stop();
         }
    }

	
	public void restart () {
		try {
			ventanaLevel.updateScore(0,game.getScoreTotalBase());
			game.restartGame(ventanaLevel.isMusicPlaying());
			String nivelNombre = game.getLevel().getName();
			updateVentana(nivelNombre);
		} catch (IOException | LevelException e) {
			JOptionPane.showMessageDialog(menu, "Error restarting the level");
		}

	}

	public void backToMenu () {
		pauseMainTheme();
		showMenu();
		ventanaLevel=null;
		game = new Game(game.getPath());

	}

	public void iniciarPartida(int nLevel) {
		try {
			boolean playMusic = ventanaLevel == null || ventanaLevel.isMusicPlaying();
			game.iniciarPartida(nLevel, playMusic);
			enableLevels();
			String nivelNombre = game.getLevel().getName();
			
			updateVentana(nivelNombre);
		}catch(IOException e){ 
			if(nLevel > getTotalLevels() ) {
				if(ventanaLevel != null)
					ventanaLevel.juegoCompletado();
			} else {
				JOptionPane.showMessageDialog(menu, "Non-existing file");
			}
		}catch(LevelException e){
			new ErrorMessage("Error reading level: " + nLevel + ". Invalid format.\n Moving to the next level.");
			iniciarPartida(game.getNLevel()+1);
		} 
	}
	
	private void updateVentana(String nivelNombre) {
		menu.setVisible(false);
		if(ventanaLevel !=null) {
			ventanaLevel.remove(ventanaLevel.getMainPanel());
			ventanaLevel.remove(ventanaLevel.getBackgroundPanel());
			ventanaLevel.setTitle(nivelNombre);
			ventanaLevel.anadirPanel();
			
		}
		else {
			ventanaLevel = new VentanaLevel( this, nivelNombre);
			ventanaLevel.setVisible(true);		
		}
	}

	public Game getGame() {
		return game;
	}
	
	public int getTotalLevels() {
		return Game.getTotalLevels();
	}

	public List<Coche> getCoches(){
		
		return game.getLevel().getCoches();
	}
	public Coche getCocheRojo() {
		
		return game.getLevel().getCocheRojo();
	}
	public Casilla getSalida() {
		
		return game.getLevel().getSalida();
	}
	public List<Casilla> getMuros(){
		
		return game.getLevel().getMuros();
	}
	public Set<Casilla> getBlancos(){
		
		return game.getLevel().getBlancos();
	}
}
