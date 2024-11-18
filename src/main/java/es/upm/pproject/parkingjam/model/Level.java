package es.upm.pproject.parkingjam.model;

import java.io.BufferedReader;
import java.io.FileReader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.io.BufferedWriter;
import java.io.FileWriter;


public class Level {
	private static final Logger logger = Logger.getLogger(Level.class);

	private String name;
	private int nRows;
	private int nColumns;
	private char[][] parkingGrid;
	private List<Coche> coches;

	private List<Casilla> muros;
	private Set<Casilla> blancos;
	private Casilla salida;
	private Coche cocheRojo;
	private int nivel ;
	private  int scoreNivel;
	private Deque<Entry<Coche,Casilla>> movimientos;
	
	public Level() {
		movimientos = new ArrayDeque<>();
		blancos = new HashSet<>();
		muros = new ArrayList<>();
		this.coches = new ArrayList<>();
		setScoreNivel(0);
		logger.debug("Level instance created.");
	}

	public void loadLevel(String filePath) throws IOException,LevelException {
		logger.info("Loading level from file: " + filePath);
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line = reader.readLine();
			setName(line.trim());
			String nameLevel = line.trim();
			setName(nameLevel);
			line = reader.readLine();
			String[] dimensions = line.trim().split(" ");
			int rows = Integer.parseInt(dimensions[0]);
			int columns = Integer.parseInt(dimensions[1]);
			setnRows(rows);
			setnColumns(columns);

			this.parkingGrid = new char[nRows][nColumns];
			Map<Character, List<int[]>> carPositions = new HashMap<>();

			for (int i = 0; i < nRows; i++) {
				line = reader.readLine();
				parkingGrid[i] = line.toCharArray();
				for (int j = 0; j < nColumns; j++) {
					char cell = parkingGrid[i][j];
					if (cell != ' ' && cell != '+' && cell != '@') {
						carPositions.computeIfAbsent(cell, k -> new ArrayList<>()).add(new int[]{i, j});
					} else if (cell == '@') {
						if(salida != null) throw new LevelException();
						salida = new Casilla(i, j);
						blancos.add(salida);
					} else if (cell == '+') {
						Casilla cas = new Casilla(i, j);
						muros.add(cas);
					} else if (cell == ' ') {
						Casilla cas = new Casilla(i, j);
						blancos.add(cas);
					}
				}
			}
			createCars(carPositions);
			logger.info("Level loaded successfully: " + nameLevel);
		} catch (IOException e) {
			logger.error("Error loading level from file: " + filePath);
			throw e;
		} catch (NumberFormatException e) {
	        logger.error("Error parsing dimensions in level file: " + filePath);
	        throw new LevelException(e);
	    } catch (ArrayIndexOutOfBoundsException e) {
	        logger.error("Error reading dimensions in level file: " + filePath);
	        throw new LevelException(e);
	    }
	}

	private int saca(int[] x,int y) {
		return x[y];
	}

	private void createCars(Map<Character, List<int[]>> carPositions) {
		for (Map.Entry<Character, List<int[]>> entry : carPositions.entrySet()) {
			char carChar = entry.getKey();
			List<int[]> positions = entry.getValue();
			int size = positions.size();

			Casilla[] casillas = new Casilla[size];
			for (int i = 0; i < size; i++) {
				casillas[i] = new Casilla(saca(positions.get(i),0), saca(positions.get(i),1));
			}

			boolean horizontal = isHorizontal(positions);

			if (carChar == '*') {
				cocheRojo = new Coche(carChar, casillas, horizontal);
				cocheRojo.setRedCar(true);
				coches.add(cocheRojo);
			} else {
				coches.add(new Coche(carChar, casillas, horizontal));
			}
		}
	}

	private boolean isHorizontal(List<int[]> positions) {
		if (positions.size() < 2) return true; 
		return positions.get(0)[0] == positions.get(1)[0];
	}


	public boolean isExit(Coche c) {

		return c.getCasillas()[c.getCasillas().length-1].equals(salida);
	}


	public boolean isValidMove(Movimiento m) {
		Coche coche = m.getCoche();
		Casilla destino = m.getDestino();

		if (coche == null) return false; 

		
		if (!hasMoved(coche, destino)) return true;

		return isValidMoveAux(coche, destino);
	}

	private boolean hasMoved(Coche coche, Casilla destino) {
		Casilla[] casillas = coche.getCasillas();
		for (Casilla casilla : casillas) {
			if (coche.isHorizontal()) {
				if (casilla.getY() != destino.getY()) {
					return true;
				}
			} else {
				if (casilla.getX() != destino.getX()) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isValidMoveAux(Coche coche, Casilla destino) {
		int inicioX = coche.getCasillas()[0].getX();
		int inicioY = coche.getCasillas()[0].getY();
		int destinoX = destino.getX();
		int destinoY = destino.getY();
		if (inicioX < destinoX || inicioY < destinoY) {
			return isValidMovePositive(coche, destino);
		} else {
			return isValidMoveNegative(coche, destino);
		}
	}

	private boolean isValidMovePositive(Coche coche, Casilla destino) {
		boolean result = true;
		Casilla ultCasilla = coche.getCasillas()[coche.getSize() - 1];
		Casilla firstCasilla = coche.getCasillas()[0];
		int inicioX = firstCasilla.getX();
		int inicioY = firstCasilla.getY();
		int destinoX = destino.getX();
		int destinoY = destino.getY();
		int desplazamiento;
		if (coche.isHorizontal()) {
			desplazamiento = destinoY - inicioY;
		} else {
			desplazamiento = destinoX - inicioX;
		}
		for (int i = 0; i < desplazamiento && result; i++) {
			Casilla casillaIntermedia;
			if (coche.isHorizontal())
				casillaIntermedia = new Casilla(inicioX, ultCasilla.getY() + i + 1);
			else
				casillaIntermedia = new Casilla(ultCasilla.getX() + i + 1, inicioY);

			if (!isEmptySquare(casillaIntermedia)) {
				result = false;
			}
		}
		return result;
	}

	private boolean isValidMoveNegative(Coche coche, Casilla destino) {
		boolean result = true;
		Casilla firstCasilla = coche.getCasillas()[0];
		int inicioX = firstCasilla.getX();
		int inicioY = firstCasilla.getY();
		int destinoX = destino.getX();
		int destinoY = destino.getY();
		int desplazamiento;
		if (coche.isHorizontal()) {
			desplazamiento = inicioY - destinoY;
		} else {
			desplazamiento = inicioX - destinoX;
		}
		for (int i = 0; i < desplazamiento && result; i++) {
			Casilla casillaIntermedia;
			if (coche.isHorizontal())
				casillaIntermedia = new Casilla(inicioX, firstCasilla.getY() - i - 1);
			else
				casillaIntermedia = new Casilla(firstCasilla.getX() - i - 1, inicioY);

			if (!isEmptySquare(casillaIntermedia)) {
				result = false;
			}
		}
		return result;
	}





	public void moveCar(Movimiento m) {
		if (isValidMove(m)) {
			logger.info("Moving car: " + "\"" +m.getCoche().getId()+"\"" + " to destination: (" + m.getDestino().getX() + "," + m.getDestino().getY()+")");
			actualizarBlancos(m.getCoche(), m.getDestino());
			m.ejecutarMovimiento();
			updateGrid(m.getCoche().getCasillas(), m.getCoche().getId());
			logger.info("Car moved successfully.");
		} else {
			logger.warn("Invalid move attempted for car: " + m.getCoche().getId());
		}
	}

	private void updateGrid(Casilla[] casillas, char idCoche) {
		for(Casilla casilla : casillas) {
			parkingGrid[casilla.getX()][casilla.getY()] = idCoche;
		}
		for(Casilla casilla : blancos) {
			if(!casilla.equals(salida))
				parkingGrid[casilla.getX()][casilla.getY()] = ' ';
		}
	}

	public void addMovement(Coche c, Casilla origen) {
		Entry<Coche,Casilla> mov = new SimpleEntry<>(c, origen);
		movimientos.push(mov);

	}

	public Coche undoLastMove() {
		Coche coche = null;
		if(!movimientos.isEmpty()) {

			Entry<Coche, Casilla> move = movimientos.pop();
			Movimiento lastMovement = new Movimiento(move.getKey(), move.getValue()); 
			coche = lastMovement.getCoche();
			logger.info("Undoing last movement car: " +  "\"" +coche.getId()+"\"");
			moveCar(lastMovement); 

		}
		if(coche == null) logger.info("No moves to undo.");
		return coche;
	}

	private void actualizarBlancos(Coche coche, Casilla destino) {
		int inicioX = coche.getCasillas()[0].getX();
		int inicioY = coche.getCasillas()[0].getY();
		int destinoX = destino.getX();
		int destinoY = destino.getY();
		int desplazamiento;
		if(coche.isHorizontal()) {
			desplazamiento = destinoY - inicioY;
		}else {
			desplazamiento = destinoX - inicioX;
		}
		
		if(desplazamiento != 0) { 
			borrarBlancos(desplazamiento, coche);
			addBlancos(desplazamiento, coche);
		} 		
	}


	private void borrarBlancos(int desplazamiento, Coche coche) {
		for(Casilla cochePos: coche.getCasillas()) {
			Casilla deleteBlanco;
			if(coche.isHorizontal()) {
				deleteBlanco= new Casilla(cochePos.getX(), cochePos.getY()+desplazamiento);
			} else {
				deleteBlanco= new Casilla(cochePos.getX()+desplazamiento, cochePos.getY());
			}
			blancos.remove(deleteBlanco);
		}

	}



	
	private void addBlancos(int desplazamiento, Coche coche) {
		Casilla[] cochePos = coche.getCasillas();
		int desplAbsoluto = Math.abs(desplazamiento);
		boolean isHorizontal = coche.isHorizontal();
		int ultimaCas = cochePos.length - 1;

		for (int i = 0; i < desplAbsoluto && i < cochePos.length; i++) {
			Casilla nuevoBlanco = crearNuevaCasillaBlanco(cochePos, desplazamiento, isHorizontal, ultimaCas, i);
			blancos.add(nuevoBlanco);
		}
	}

	private Casilla crearNuevaCasillaBlanco(Casilla[] cochePos, int desplazamiento, boolean isHorizontal, int ultimaCas, int i) {
		if (desplazamiento > 0) { 
			return isHorizontal
					? new Casilla(cochePos[0].getX(), cochePos[0].getY() + i)
							: new Casilla(cochePos[0].getX() + i, cochePos[0].getY());
		} else { 
			return isHorizontal
					? new Casilla(cochePos[ultimaCas].getX(), cochePos[ultimaCas].getY() - i)
							: new Casilla(cochePos[ultimaCas].getX() - i, cochePos[ultimaCas].getY());
		}
	}



	public Coche getCoche(char id) {
		for(int i = 0; i < coches.size(); i ++) {
			Coche coche = coches.get(i);
			if(coche.getId() == id) {
				return coche;
			}
		}
		return null;
	}

	private boolean isEmptySquare(Casilla casilla) {
		return blancos.contains(casilla);
	}

	public void saveLevel(String filePath, int scoreTotalBase) throws IOException {
		logger.info("Saving level to file: " + filePath);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(String.valueOf(nivel));
			writer.newLine();
			writer.write(String.valueOf(scoreNivel));
			writer.newLine();
			writer.write(String.valueOf(scoreTotalBase));
			writer.newLine();
			writer.write(name);
			writer.newLine();
			writer.write(nRows + " " + nColumns);
			writer.newLine();
			for (int i = 0; i < nRows; i++) {
				writer.write(parkingGrid[i]);
				writer.newLine();
			}
			Deque<Entry<Coche, Casilla>> pilaAux = new ArrayDeque<>();
			while (!movimientos.isEmpty()) {
				Entry<Coche, Casilla> movimiento = movimientos.pop();
				pilaAux.push(movimiento);
				writer.write(movimiento.getKey().getId() + " " + movimiento.getValue().getX() + " " + movimiento.getValue().getY());
				writer.newLine();
			}
			while (!pilaAux.isEmpty()) {
				Entry<Coche, Casilla> movimiento = pilaAux.pop();
				movimientos.push(movimiento);
			}
			logger.info("Level saved successfully.");
		} catch (IOException e) {
			logger.error("Error saving level to file: " + filePath);
			throw e;
		}
	}

	public int loadSavedLevel(String filePath) throws IOException,LevelException {
		logger.info("Loading saved level from file: " + filePath);
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			this.nivel = Integer.parseInt(reader.readLine().trim());
			this.scoreNivel = Integer.parseInt(reader.readLine().trim());
			int scoreTotalBase = Integer.parseInt(reader.readLine().trim());
			String line = reader.readLine();
			this.name = line.trim();
			line = reader.readLine();
			String[] dimensions = line.trim().split(" ");
			this.nRows = Integer.parseInt(dimensions[0]);
			this.nColumns = Integer.parseInt(dimensions[1]);

			this.parkingGrid = new char[nRows][nColumns];
			Map<Character, List<int[]>> carPositions = new HashMap<>();

			for (int i = 0; i < nRows; i++) {
				line = reader.readLine();
				parkingGrid[i] = line.toCharArray();
				for (int j = 0; j < nColumns; j++) {
					char cell = parkingGrid[i][j];
					if (cell != ' ' && cell != '+' && cell != '@') {
						carPositions.computeIfAbsent(cell, k -> new ArrayList<>()).add(new int[]{i, j});
					} else if (cell == '@') {
						salida = new Casilla(i, j);
						blancos.add(salida);
					} else if (cell == '+') {
						Casilla cas = new Casilla(i, j);
						muros.add(cas);
					} else if (cell == ' ') {
						Casilla cas = new Casilla(i, j);
						blancos.add(cas);
					}
				}
			}
			createCars(carPositions);

			List<Entry<Coche, Casilla>> movimientosList = new ArrayList<>();
			line = reader.readLine();
			while (line != null) {
				String[] movimientoParts = line.split(" ");
				char carId = movimientoParts[0].charAt(0);
				int x = Integer.parseInt(movimientoParts[1]);
				int y = Integer.parseInt(movimientoParts[2]);
				Coche coche = getCoche(carId);
				Casilla origen = new Casilla(x, y);
				movimientosList.add(new SimpleEntry<>(coche, origen));
				line = reader.readLine();
			}

			movimientos = insertElementsInReverseOrder(movimientosList);
			logger.info("Saved level loaded successfully: " + name);
			return scoreTotalBase;
		} catch (IOException e) {
			logger.error("Error loading saved level from file: " + filePath);
			throw e;
		} catch (NumberFormatException e) {
	        logger.error("Error parsing numeric data in saved level file: " + filePath);
	        throw new LevelException(e);
	    } catch (ArrayIndexOutOfBoundsException e) {
	        logger.error("Error reading dimensions in saved level file: " + filePath);
	        throw new LevelException(e);
	    }
	}


	private Deque<Entry<Coche, Casilla>> insertElementsInReverseOrder(List<Entry<Coche, Casilla>> elements) {
		Deque<Entry<Coche, Casilla>> stack = new ArrayDeque<>();
		for (int i = elements.size() - 1; i >= 0; i--) {
			stack.push(elements.get(i));
		}
		return stack;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getnRows() {
		return nRows;
	}

	public void setnRows(int nRows) {
		this.nRows = nRows;
	}

	public int getnColumns() {
		return nColumns;
	}

	public void setnColumns(int nColumns) {
		this.nColumns = nColumns;
	}

	public char[][] getParkingGrid() {
		return parkingGrid;
	}

	public List<Coche> getCoches() {
		return coches;
	}

	public List<Casilla> getMuros() {
		return muros;
	}

	public Set<Casilla> getBlancos() {
		return blancos;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public Casilla getSalida() {
		return salida;
	}

	public Coche getCocheRojo() {
		return cocheRojo;
	}

	public int getScoreNivel() {
		return scoreNivel;
	}

	public final void setScoreNivel(int scoreNivel) {
		this.scoreNivel = scoreNivel;
	}
	public Deque<Entry<Coche, Casilla>> getMovimientos() {
		return movimientos;
	}

}

