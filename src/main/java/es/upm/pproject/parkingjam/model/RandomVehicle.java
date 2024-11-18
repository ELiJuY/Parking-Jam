package es.upm.pproject.parkingjam.model;

public class RandomVehicle {
    String[] v2x1 = {"greenCar", "redTruck", "yellowTruck"};
    String[] v3x1 = {"whiteTruck", "blueTruck"};

    public String randomVehicle(int size, boolean isHorizontal, Coche coche) {
        int eleccion;
        String chosen;
        switch (size) {
            case 2:
                eleccion = (coche.getId()) % 3;
                chosen = v2x1[eleccion];
                break;
            case 3:
                eleccion = (coche.getId()) % 2;
                chosen = v3x1[eleccion];
                break;
            default:
                chosen = "Not found";
                break;
        }
        if (!"Not found".equals(chosen)) {
            chosen += isHorizontal ? "_H.png" : ".png";
        }
        return chosen;
    }
}
