package es.upm.pproject.parkingjam.view;

import javax.swing.*;
import java.awt.*;

import es.upm.pproject.parkingjam.controller.*;

import org.apache.log4j.Logger;

public class JuegoCompletadoPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(JuegoCompletadoPanel.class);

    public JuegoCompletadoPanel(int score, IController controller) {
        logger.info("Initializing JuegoCompletadoPanel with score: " + score);
    	setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel label = new JLabel("Game Completed!!", SwingConstants.CENTER);
        String font = "Serif";
        label.setFont(new Font(font, Font.BOLD, 20));
        add(label, gbc);

        gbc.gridy++;
        JLabel scoreLabel = new JLabel("Final Score: " + score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font(font, Font.PLAIN, 16));
        add(scoreLabel, gbc);

        gbc.gridy++;
        JButton menuButton = new JButton("Return to Menu");
        menuButton.setFont(new Font(font, Font.PLAIN, 16));
        menuButton.setPreferredSize(new Dimension(150, 30));
        add(menuButton, gbc);


        menuButton.addActionListener(e -> {
        	logger.info("Menu button clicked, returning to menu."); 
            Window window = SwingUtilities.getWindowAncestor(JuegoCompletadoPanel.this);
            if (window != null) {
                window.dispose();
            }
            controller.backToMenu();
        });


        
        controller.stopMainTheme();
        logger.info("Main theme stopped.");
    }
}
