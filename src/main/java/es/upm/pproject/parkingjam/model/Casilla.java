package es.upm.pproject.parkingjam.model;

import java.util.Objects;

public class Casilla {
    private int x;
    private int y;

    public Casilla(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Casilla casilla = (Casilla) o;
        return x == casilla.x && y == casilla.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
