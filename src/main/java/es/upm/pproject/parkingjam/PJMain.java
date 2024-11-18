package es.upm.pproject.parkingjam;




import javax.swing.SwingUtilities;

import es.upm.pproject.parkingjam.controller.*;
import es.upm.pproject.parkingjam.view.MenuInicio;

import es.upm.pproject.parkingjam.model.Game;
public class PJMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {        
            MenuInicio menuInicio = new MenuInicio(); 
            Game game = new Game("src/main/resources/levels/level_");
            IController controller;
            
                controller = new Controller(menuInicio,game);
                menuInicio.setController(controller); 
                menuInicio.setVisible(true);
            
        });
    }
}
