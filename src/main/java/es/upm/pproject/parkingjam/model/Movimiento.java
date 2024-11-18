package es.upm.pproject.parkingjam.model;

import org.apache.log4j.Logger;

public class Movimiento {
    private Coche coche;
    private Casilla destino;

    private static final Logger logger = Logger.getLogger(Movimiento.class);

    public Movimiento(Coche coche, Casilla destino) {
        this.coche = coche;
        this.destino = destino;
    }

    public void ejecutarMovimiento() {
        int destX = destino.getX();
        int destY = destino.getY();
        int origX = coche.getCasillas()[0].getX();
        int origY = coche.getCasillas()[0].getY();

        logger.info("Executing movement for car " + coche + " from (" + origX + ", " + origY + ") to (" + destX + ", " + destY + ")");

        if (coche.isHorizontal()) {
            ejecutarMovimientoHorizontal(destY, origX);
        } else {
            ejecutarMovimientoVertical(destX, origY);
        }

    }

    public void ejecutarMovimientoVertical(int destX, int origY) {
        logger.debug("Executing vertical movement to X: " + destX + " from Y: " + origY);
        int acum = destX;
        Casilla[] posCoche = coche.getCasillas();
        for (Casilla pos : posCoche) {
            pos.setX(acum);
            pos.setY(origY);
            acum++;
        }
    }

    public void ejecutarMovimientoHorizontal(int destY, int origX) {
        logger.debug("Executing horizontal movement to Y: " + destY + " from X: " + origX);
        Casilla[] posCoche = coche.getCasillas();
        int acum = destY;
        for (Casilla pos : posCoche) {
            pos.setX(origX);
            pos.setY(acum);
            acum++;
        }
    }

    public Coche getCoche() {
        return coche;
    }

    public void setCoche(Coche coche) {
        this.coche = coche;
        logger.info("Car set to " + coche);
    }

    public Casilla getDestino() {
        return destino;
    }

    public void setDestino(Casilla destino) {
        this.destino = destino;
        logger.info("Destination set to " + destino);
    }
}
