package es.upm.pproject.parkingjam.view;

import es.upm.pproject.parkingjam.controller.*;


import javax.imageio.ImageIO;
import javax.swing.*;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuInicio extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel nivelPanel; 
    private transient IController controller;
    private JButton[] levelButtons;
    private transient BufferedImage backgroundImage;
    
    private static final Logger logger = Logger.getLogger(MenuInicio.class);

    
    
    public MenuInicio() {
        logger.info("Initializing MenuInicio...");
        setTitle("Menú de Inicio");
        setSize(400, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/images/backgroundMenu.jpg"));
            logger.info("Background image loaded successfully.");

        } catch (IOException e) {

        	logger.error("Failed to load background image.");

        }

        JPanel panel = new MenuBackgroundPanel();
        panel.setLayout(new GridBagLayout());

        ImageIcon iconPlay = new ImageIcon("src/main/resources/images/playButton.png");
        Image imagePlay = iconPlay.getImage();
        Image scaledImagePlay = imagePlay.getScaledInstance(80,80, java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledPlayIcon = new ImageIcon(scaledImagePlay);
        scaledImagePlay = imagePlay.getScaledInstance(77,77, java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledPressedPlayIcon = new ImageIcon(scaledImagePlay);

        JButton playButton = new JButton(scaledPlayIcon);
        playButton.setOpaque(false);  
        playButton.setContentAreaFilled(false); 
        playButton.setBorderPainted(false);
        playButton.setRolloverIcon(scaledPressedPlayIcon);
        playButton.setPreferredSize(new Dimension(80,80)); 

       

        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5; 
        gbc.weighty = 0.5; 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.CENTER; 

        panel.add(playButton, gbc);

        gbc.gridy = 1;
        

        playButton.addActionListener(e -> {

        	 logger.info("Play button clicked.");
            getContentPane().remove(panel); // Eliminar el contenido actual del JFrame
            mostrarNiveles(); // Mostrar los niveles al presionar "Play"

        });


        setLocationRelativeTo(null);
        getContentPane().add(panel);
        setVisible(true);
    }

    private class MenuBackgroundPanel extends JPanel {
        
		private static final long serialVersionUID = 1L;

		@Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private void mostrarNiveles() {

    	logger.info("Displaying level selection panel.");
        nivelPanel = new JPanel(new GridBagLayout()); // Crea un nuevo panel distinto para la seleccion de nivel


        
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0; 
        labelConstraints.gridy = 0;
        labelConstraints.gridwidth = 3; 
        labelConstraints.insets = new Insets(0, 0, 20, 0); 
        JLabel label = new JLabel("Select a level:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));

        nivelPanel.add(label, labelConstraints);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        
        int nLevels = controller.getTotalLevels();
        levelButtons = new JButton [nLevels];
        for (int i = 1; i <= nLevels; i++) {
            JButton nivelButton = new JButton(String.valueOf(i));
            nivelButton.setPreferredSize(new Dimension(100, 50)); 
            
           
            gbc.gridx = (i - 1) % 3; 
            gbc.gridy = 1 + ((i - 1) / 3); 
            nivelPanel.add(nivelButton, gbc);
            levelButtons[i-1]=nivelButton;
            if (i!=1) nivelButton.setEnabled(false);
            nivelButton.addActionListener(e -> {
				try {
					int level = Integer.parseInt(nivelButton.getText());
                    logger.info("Starting level " + level);
					controller.iniciarPartida(Integer.parseInt(nivelButton.getText()));	
				} catch (NumberFormatException e1) {

					logger.error("Failed to parse level number from button text.");

				} 
			});
        }

       
        
    
        JButton loadButton = new JButton("Load Game");
        loadButton.setBackground(new Color(173, 216, 230));
        loadButton.setPreferredSize(new Dimension(200, 40)); 

        gbc.gridx = 0;
        gbc.gridy = 2 + (nLevels / 3);
        gbc.gridwidth = 3; 
        gbc.insets = new Insets(20, 5, 5, 5); 
        gbc.anchor = GridBagConstraints.CENTER;
        nivelPanel.add(loadButton, gbc);

        loadButton.addActionListener(e -> abrirExploradorArchivos());

        
        this.add(nivelPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    
	
	public void paintLevel () {
        nivelPanel = new JPanel(new GridBagLayout()); 
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
        setSize(screenSize.width, screenSize.height); 
        
        setLocationRelativeTo(null); 
        setBackground(Color.GRAY);
        this.repaint();

	}
	
	private void abrirExploradorArchivos() {
	        JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        int result = fileChooser.showOpenDialog(this);
	        if (result == JFileChooser.APPROVE_OPTION) {
	            File selectedFile = fileChooser.getSelectedFile();
	            String filePath = selectedFile.getAbsolutePath();
	            logger.info("Selected file for loading: " + filePath);
	           
	                try {
						controller.loadSavedGame(filePath);
					} catch (IOException e) {
						logger.error("Failed to load saved game from file: " + filePath);

					}
	            
	        }
	    }


	public void setController(IController controller) {
		this.controller = controller;
	}

	public void enableLevel(int i) {
		levelButtons[i-1].setEnabled(true);
	}
}

