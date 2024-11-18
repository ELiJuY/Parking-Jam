package es.upm.pproject.parkingjam.view;

import javax.swing.*;

import org.apache.log4j.Logger;



import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class BackgroundPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Image backgrnd;
	private transient Image roadRight;
	private transient Image roadLeft;
	private transient Image roadTop;
	private transient Image roadBottom;
	private int pixeles;

	private int ancho;
	private int alto;
	private int xCoord;
	private int yCoord;
    private static final Logger logger = Logger.getLogger(BackgroundPanel.class);


    public BackgroundPanel(int width, int height, int pixeles, int x, int y) {
    	try {
            logger.info("Loading background image.");
    		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    		Image originalImage = ImageIO.read(new File("src/main/resources/images/background.jpg"));
       		backgrnd = originalImage.getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
       		originalImage = ImageIO.read(new File("src/main/resources/images/fondo_izquierda.jpg"));
       		roadLeft = originalImage.getScaledInstance(pixeles * 2, (height + 2) * pixeles, Image.SCALE_SMOOTH);
       		originalImage = ImageIO.read(new File("src/main/resources/images/fondo_derecha.jpg"));
       		roadRight = originalImage.getScaledInstance(pixeles * 2, (height + 2) * pixeles, Image.SCALE_SMOOTH);
       		originalImage = ImageIO.read(new File("src/main/resources/images/fondo_arriba_f.jpg"));
       		roadTop = originalImage.getScaledInstance((width + 4) * pixeles, pixeles * 2, Image.SCALE_SMOOTH);
       		originalImage = ImageIO.read(new File("src/main/resources/images/fondo_abajo.jpg"));
       		roadBottom = originalImage.getScaledInstance(screenSize.width, pixeles * 4, Image.SCALE_SMOOTH);
       		this.pixeles=pixeles;
       		this.ancho=width;
       		this.alto=height;
       		setCoords(x,y);
    	} catch (IOException e) {

    		logger.error("Error loading images: " + e.getMessage());

    	}
}

    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        g.drawImage(backgrnd, 0, 0, this);
        g.drawImage(roadLeft, xCoord - pixeles*2, yCoord - pixeles,this);
        g.drawImage(roadRight, xCoord + pixeles*ancho, yCoord - pixeles,this);
        g.drawImage(roadTop, xCoord - pixeles*2, yCoord - pixeles*2,this);
        g.drawImage(roadBottom, 0, yCoord + pixeles*alto,this);
    }
    
    protected final void setCoords(int x, int y) {
   		this.xCoord=x;
		this.yCoord=y;
    }
   
}
