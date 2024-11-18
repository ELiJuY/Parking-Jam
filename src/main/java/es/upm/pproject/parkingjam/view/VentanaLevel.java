package es.upm.pproject.parkingjam.view;

import es.upm.pproject.parkingjam.controller.*;


import javax.swing.*;


import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
public class VentanaLevel extends JFrame{

	private static final long serialVersionUID = 1L;
	private transient IController controller;
    private NivelPanel levelPanel;
    private JPanel menuOptions;
    private BackgroundPanel backgrnd;
    private JTextField mensajeFelicitacion;
    private JLabel scoreLabelNivel;
    private int scoreNivel;
    private JLabel scoreLabelTotal;
    private int scoreTotal;
    private boolean musicPlaying;
    private ImageIcon scaledPlayMusicIcon;
    private ImageIcon scaledStopMusicIcon;
    private JButton musicButton;
    private static final Logger logger = Logger.getLogger(VentanaLevel.class.getName());


    public int getScoreNivel() {
        return scoreNivel;
    }
    public void setScoreNivel(int scoreNivel) {
        this.scoreNivel = scoreNivel;
    }
    public NivelPanel getLevelPanel() {
        return levelPanel;
    }
    JPanel mainPanel;
    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Component getBackgroundPanel() {
        return backgrnd;
    }

    public VentanaLevel(IController controller, String nivelNombre) {
    	this.musicPlaying=true;
        this.controller = controller;
        int height = controller.getGame().getLevel().getnRows();
        int width = controller.getGame().getLevel().getnColumns();

        int pixeles = 50;
        setTitle(nivelNombre);
        setSize(pixeles*width + 100 , pixeles*height + 100 );
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		
        this.anadirPanel();
        logger.info("VentanaLevel initialized ");




		setLocationRelativeTo(null);
		setVisible(true);
		
		

      
	}
	public final void anadirPanel() {
		scoreNivel = controller.getGame().getLevel().getScoreNivel();
		scoreTotal = controller.getGame().getScoreTotalBase() + scoreNivel;
		int pixeles = 50;
		int height = controller.getGame().getLevel().getnRows();
        int width = controller.getGame().getLevel().getnColumns();
		mainPanel = new JPanel(null);
		mainPanel.setOpaque(false); 
        mainPanel.setBounds(0, 0, getWidth(), getHeight());
        add(mainPanel);
		
		levelPanel= new NivelPanel(controller);
	    levelPanel.setBackground(Color.LIGHT_GRAY);
	    Dimension fixedSize = new Dimension(pixeles*width, pixeles*height);
	    levelPanel.setPreferredSize(fixedSize);
	    levelPanel.setSize(fixedSize);
	    mainPanel.add(levelPanel);
	   
	    JPanel menuOpciones = new JPanel(new GridBagLayout());
	    String font = "Arial";
	    scoreLabelNivel = new JLabel("LevelScore: " + scoreNivel);
        scoreLabelNivel.setFont(new Font(font, Font.BOLD, 16));
        scoreLabelTotal = new JLabel("TotalScore: " + scoreTotal);
        scoreLabelTotal.setFont(new Font(font, Font.BOLD, 16));
        this.setMenuOpciones(menuOpciones);
        int menuOpcionesWidth = 12;
        menuOpciones.setBackground(Color.LIGHT_GRAY);
        menuOpciones.setBounds(((getWidth() - levelPanel.getWidth()) / 2 + ((width-menuOpcionesWidth)/2)*pixeles + 10), (getHeight() - levelPanel.getHeight()) / 2 - (int) (pixeles * 1.5), pixeles * menuOpcionesWidth - 20, pixeles);
        GridBagConstraints buttonGBC = new GridBagConstraints();
        buttonGBC.insets = new Insets(5, 5, 5, 5); 
 
        ImageIcon iconRestart = new ImageIcon("src/main/resources/images/restartButton.png");
        Image imageRestart = iconRestart.getImage();
        Image scaledImageRestart = imageRestart.getScaledInstance((int)(pixeles/1.5),(int)(pixeles/1.5), java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledRestartIcon = new ImageIcon(scaledImageRestart);
       
        JButton restartButton = new JButton(scaledRestartIcon);
        restartButton.setPreferredSize(new Dimension((int)(pixeles/1.5), (int)(pixeles/1.5))); 
        menuOpciones.add(restartButton, buttonGBC);
        
        restartButton.addActionListener(e -> {
            controller.restart();
            logger.info("Game restarted");
        });
        
        ImageIcon iconBackMenu = new ImageIcon("src/main/resources/images/backToMenuButton.png");
        Image imageBackMenu = iconBackMenu.getImage();
        Image scaledImageBackMenu = imageBackMenu.getScaledInstance((int)(pixeles/1.5),(int)(pixeles/1.5), java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledBackMenuIcon = new ImageIcon(scaledImageBackMenu);
       
        JButton backMenuButton = new JButton(scaledBackMenuIcon);
        backMenuButton.setPreferredSize(new Dimension((int)(pixeles/1.5), (int)(pixeles/1.5)));
        menuOpciones.add(backMenuButton, buttonGBC);
        
        backMenuButton.addActionListener(e -> { 
        	controller.backToMenu(); 
        	logger.info("Back to menu action performed");
        	dispose();
        });
        
        ImageIcon iconUndo = new ImageIcon("src/main/resources/images/undoButton.png");
        Image imageUndo = iconUndo.getImage();
        Image scaledImageUndo = imageUndo.getScaledInstance((int)(pixeles/1.5),(int)(pixeles/1.5), java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledUndoIcon = new ImageIcon(scaledImageUndo);
       
        JButton undoButton = new JButton(scaledUndoIcon);
        undoButton.setPreferredSize(new Dimension((int)(pixeles/1.5), (int)(pixeles/1.5))); 
        menuOpciones.add(undoButton, buttonGBC);
        
        undoButton.addActionListener(e -> {
            controller.undo();
        	logger.info("Undo action performed");

        });
        
        
        ImageIcon iconSave = new ImageIcon("src/main/resources/images/saveButton.png");
        Image imageSave = iconSave.getImage();
        Image scaledImageSave = imageSave.getScaledInstance((int)(pixeles/1.5),(int)(pixeles/1.5), java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledSaveIcon = new ImageIcon(scaledImageSave);
       
        JButton saveButton = new JButton(scaledSaveIcon);
        saveButton.setPreferredSize(new Dimension((int)(pixeles/1.5), (int)(pixeles/1.5))); 
        menuOpciones.add(saveButton, buttonGBC);
        


        saveButton.addActionListener(e -> { 
       	 abrirExploradorArchivos();
       	 logger.info("Game saved");
       	 
       });

        ImageIcon iconStopMusic = new ImageIcon("src/main/resources/images/stop_music.png");

        Image imageStopMusic = iconStopMusic.getImage();
        Image scaledImageStopMusic = imageStopMusic.getScaledInstance((int)(pixeles/1.5),(int)(pixeles/1.5), java.awt.Image.SCALE_SMOOTH);
        scaledStopMusicIcon = new ImageIcon(scaledImageStopMusic);
       
        ImageIcon iconPlayMusic = new ImageIcon("src/main/resources/images/play_music.png");
        Image imagePlayMusic = iconPlayMusic.getImage();
        Image scaledImagePlayMusic = imagePlayMusic.getScaledInstance((int)(pixeles/1.5),(int)(pixeles/1.5), java.awt.Image.SCALE_SMOOTH);
        scaledPlayMusicIcon = new ImageIcon(scaledImagePlayMusic);
        
        musicButton = new JButton(isMusicPlaying() ? scaledStopMusicIcon : scaledPlayMusicIcon);
        musicButton.setPreferredSize(new Dimension((int)(pixeles/1.5), (int)(pixeles/1.5))); 
        menuOpciones.add(musicButton, buttonGBC);
        
        musicButton.addActionListener(e -> { 
        	if (musicPlaying) {
            	controller.stopMainTheme();
            	logger.info("Music stopped");
        	}
        	else {
        		controller.playMainTheme();
        		logger.info("Music started");
        	}

        });
        
        GridBagConstraints buttonGBC2 = new GridBagConstraints();
        buttonGBC2.insets = new Insets(15, 15, 15, 15);
        menuOpciones.add(scoreLabelNivel,buttonGBC2);
        menuOpciones.add(scoreLabelTotal,buttonGBC2);
        mainPanel.add(menuOpciones);
        
        int titlePanelWidth = 10;
        int titlePanelHeight = 25;
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.LIGHT_GRAY);
        titlePanel.setBounds(((getWidth() - levelPanel.getWidth()) / 2 + (width*pixeles/2 - getTitle().length()*titlePanelWidth / 2)), (getHeight() - levelPanel.getHeight()) / 2 - (pixeles * 3), getTitle().length()*titlePanelWidth, titlePanelHeight);

        
        JLabel titleLabel = new JLabel();
        titleLabel.setText(getTitle());
        titleLabel.setFont(new Font(font, Font.BOLD, 16));
       
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel);
        

        setLocationRelativeTo(null);
		setVisible(true);
	   
		backgrnd = new BackgroundPanel(width, height, pixeles, levelPanel.getLocation().x, levelPanel.getLocation().y);
		backgrnd.setBounds(0, 0, getWidth(), getHeight());
		add(backgrnd);
		
	   addComponentListener(new ComponentAdapter() {
           @Override
           public void componentResized(ComponentEvent evt) {
               
               int frameWidth = getWidth();
               int frameHeight = getHeight();
               int panelWidth = levelPanel.getWidth();
               int panelHeight = levelPanel.getHeight();

               
               int newX = (frameWidth - panelWidth) / 2;
               int newY = (frameHeight - panelHeight) / 2;

              
               backgrnd.setCoords(newX, newY);
               levelPanel.setLocation(newX, newY);
               menuOpciones.setLocation(newX + ((width-menuOpcionesWidth)/2)*pixeles+10, (int) (newY - pixeles * 1.5));
               titlePanel.setLocation(newX + width*pixeles/2 - getTitle().length()*titlePanelWidth / 2, newY - pixeles * 3);

               
               mainPanel.setBounds(0, 0, frameWidth, frameHeight);
               revalidate();
               
               repaint();
           }
       });

       
	   
       getComponentListeners()[0].componentResized(null);

	}
	

	public boolean isMusicPlaying() {
		return musicPlaying;
	}
	
	public void setMusicPlaying(boolean musicPlaying) {
		this.musicPlaying = musicPlaying;
    	if (musicButton!=null)
    		musicButton.setIcon(musicPlaying ? scaledStopMusicIcon : scaledPlayMusicIcon);
	}
	
	private void abrirExploradorArchivos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            
            try {
            	controller.saveGame(filePath);
            } catch (IOException e) {
            	JOptionPane.showMessageDialog(this, "Error loading the file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
		
	 public void juegoCompletado() {
	        getContentPane().removeAll();
	        JuegoCompletadoPanel juegoCompletadoPanel = new JuegoCompletadoPanel(scoreTotal, controller);
	        juegoCompletadoPanel.setPreferredSize(getContentPane().getSize());
	        juegoCompletadoPanel.setSize(getContentPane().getSize());
	        setLayout(new BorderLayout());
	        add(juegoCompletadoPanel, BorderLayout.CENTER);
	        revalidate();
	        repaint();
	    }
	 
	public void updateScore(int scoreNivel1, int scoreTotal1) {
		scoreNivel = scoreNivel1 ;
        scoreLabelNivel.setText("LevelScore: " + scoreNivel);
        scoreTotal = scoreTotal1;
        scoreLabelTotal.setText("TotalScore: " + scoreTotal);
        revalidate();
        
        repaint();

    }
	
	public void updateLevelPanel(char id, Point imagePos) {
		levelPanel.updateCar(id, imagePos);
	}
	
	public JTextField getMensajeFelicitacion() {
		return mensajeFelicitacion;
	}
	public void setMensajeFelicitacion(JTextField mensajeFelicitacion) {
		this.mensajeFelicitacion = mensajeFelicitacion;
	}
	public JPanel getMenuOpciones() {
		return menuOptions;
	}
	public void setMenuOpciones(JPanel menuOpciones) {
		this.menuOptions = menuOpciones;
	}

}
