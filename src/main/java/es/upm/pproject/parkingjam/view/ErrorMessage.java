package es.upm.pproject.parkingjam.view;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;

public class ErrorMessage {
    private static final Logger logger = Logger.getLogger(ErrorMessage.class);
    
	public ErrorMessage(String message) {
		logger.error("Displaying error message: " + message);
		JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = optionPane.createDialog("Error al leer nivel!");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dispose();
            }
        }, 7000);

        dialog.setVisible(true);
    }
}
