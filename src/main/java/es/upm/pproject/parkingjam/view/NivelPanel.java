package es.upm.pproject.parkingjam.view;

import javax.swing.*;

import org.apache.log4j.Logger;

import es.upm.pproject.parkingjam.controller.IController;
import es.upm.pproject.parkingjam.model.Casilla;
import es.upm.pproject.parkingjam.model.Coche;
import es.upm.pproject.parkingjam.model.RandomVehicle;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;

public class NivelPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int N = 50; 

    private transient IController controller;
    
    private transient Map<Character, BufferedImage> imagenes;
    private Map<Character, Point> imagenesPos;
    private Map<Character, Boolean> dragging;
    private Map<Point, Character> muros;
    private Point prevMousePos;
    private Point puntoInicial;
    private static final Logger logger = Logger.getLogger(NivelPanel.class);
    
    
    public NivelPanel(IController controller) {
        this.controller = controller;
        cargarImagenes();
        asignarPosiciones();
        asignarMovimientos();
        for (Boolean isDragging : dragging.values()) {
            if (Boolean.TRUE.equals(isDragging)) {
            	logger.info("Dragging car");
            }
        }
    }

    private void cargarImagenes() {
    	imagenes = new HashMap<>();
    	dragging = new HashMap<>();
        try {
        	BufferedImage image1;
            char id = '+';
            String imagen = "wall.png";
            image1 = ImageIO.read(new File("src/main/resources/images/" + imagen));
            image1 = getScaledImage(image1, N, N);
            imagenes.put(id, image1);

            for (Coche c : controller.getCoches()) {
                id = c.getId();
                int size = c.getSize();
                boolean isHorizontal = c.isHorizontal();
                if (c.isRedCar()) {
                    imagen = c.isHorizontal() ? "redCar2x1_H.png" : "redCar2x1.png";
                } else {
                    imagen = new RandomVehicle().randomVehicle(size, isHorizontal, c);
                }

                image1 = ImageIO.read(new File("src/main/resources/images/" + imagen));
                if (isHorizontal)
                    image1 = getScaledImage(image1, N * size, N);
                else
                    image1 = getScaledImage(image1, N, N * size);
                imagenes.put(id, image1);
                dragging.put(id, false);  // Inicializar estado de arrastre
            }
        } catch (IOException e) {
        	logger.error("Error loading the images");
        }
    }

    private void asignarPosiciones() {
    	imagenesPos = new HashMap<>();
        for (Coche coche : controller.getCoches()) {
            char id = coche.getId();
            int x = saca(coche.getCasillas(), 0).getX();
            int y = saca(coche.getCasillas(), 0).getY();
            Point imagePosCoche = new Point(N * y, N * x);
            imagenesPos.put(id, imagePosCoche);
        }
        muros = new HashMap<>();
        for (Casilla m : controller.getMuros()) {
            int width = m.getX();
            int height = m.getY();
            Point imagePosMuro = new Point(N * height, N * width);
            muros.put(imagePosMuro, '+');
        }
    }

    private void asignarMovimientos() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (Coche coche : controller.getCoches()) {
                    char id = coche.getId();
                    BufferedImage imageCoche = imagenes.get(id);
                    Point imageCochePos = imagenesPos.get(id);
                    prevMousePos = e.getPoint();
                    if (isPointInImage(e.getPoint(), imageCoche, imageCochePos)) {
                        dragging.put(id, true);
                        puntoInicial = new Point(imageCochePos);
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                for (Coche coche : controller.getCoches()) {
                    char id = coche.getId();
                    if (Boolean.TRUE.equals(dragging.get(id))) {
                        dragging.put(id, false);
                        snapToGrid(imagenesPos.get(id));
                        snapToGrid(puntoInicial);
                        if (!puntoInicial.equals(imagenesPos.get(id))) {
                            controller.incrementScore();
                            Casilla origen = new Casilla(puntoInicial.y / N, puntoInicial.x / N);
                            controller.addMovement(coche, origen);
                        }
                        repaint();
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                for (Coche coche : controller.getCoches()) {
                    char id = coche.getId();
                    BufferedImage imageCoche = imagenes.get(id);
                    Point imageCochePos = imagenesPos.get(id);
                    if (Boolean.TRUE.equals(dragging.get(id))) {
                        Point mousePos = e.getPoint();
                        int dx = mousePos.x - prevMousePos.x;
                        int dy = mousePos.y - prevMousePos.y;

                        if (!coche.isHorizontal()) {
                            Point newImagePos = new Point(imageCochePos.x, imageCochePos.y + dy);
                            Point puntoAjustado = ajustarPuntoGrid(newImagePos);
                            Casilla destino = new Casilla(puntoAjustado.y / N, puntoAjustado.x / N);
                            if (controller.isValidMove(coche, destino)) {
                                int newY = imageCochePos.y + dy;
                               
                                    controller.isExit(coche);
                              
                                newY = Math.max(0, Math.min(newY, getHeight() - imageCoche.getHeight()));
                                if (!destino.equals(coche.getCasillas()[0]))
                                    controller.move(coche, destino);
                                imageCochePos.setLocation(imageCochePos.x, newY);
                            }
                            if (!isCollision(newImagePos, imageCoche, imagenesPos, imagenes))
                                repaint();
                        } else {
                            Point newImagePos = new Point(imageCochePos.x + dx, imageCochePos.y);
                            Point puntoAjustado = ajustarPuntoGrid(newImagePos);
                            Casilla destino = new Casilla(puntoAjustado.y / N, puntoAjustado.x / N);
                            if (controller.isValidMove(coche, destino)) {
                                int newX = imageCochePos.x + dx;
                                
                                    controller.isExit(coche);
                                
                                newX = Math.max(0, Math.min(newX, getWidth() - imageCoche.getWidth()));
                                if (!destino.equals(coche.getCasillas()[0]))
                                    controller.move(coche, destino);
                                imageCochePos.setLocation(newX, imageCochePos.y);
                            }
                            if (!isCollision(newImagePos, imageCoche, imagenesPos, imagenes))
                                repaint();
                        }
                        prevMousePos = mousePos;
                        break;
                    }
                }
            }
        });
    }

    public void updateCar(char id, Point imagePos) {
        imagenesPos.put(id, imagePos);
        repaint();
    }

    private void snapToGrid(Point imagePos) {
        imagePos.setLocation(roundToGrid(imagePos.x), roundToGrid(imagePos.y));
    }

    private int roundToGrid(int value) {
        return Math.round((float) value / N) * N;
    }

    private Point ajustarPuntoGrid(Point imagePos) {
        return new Point(roundToGrid(imagePos.x), roundToGrid(imagePos.y));
    }

    private boolean isPointInImage(Point p, BufferedImage img, Point imgPos) {
        return p.x >= imgPos.x && p.x <= imgPos.x + img.getWidth() &&
                p.y >= imgPos.y && p.y <= imgPos.y + img.getHeight();
    }

    private boolean isCollision(Point pos1, BufferedImage img1, Map<Character, Point> positions, Map<Character, BufferedImage> images) {
        Rectangle rect1 = new Rectangle(pos1.x, pos1.y, img1.getWidth(), img1.getHeight());
        for (Map.Entry<Character, Point> entry : positions.entrySet()) {
            Character key = entry.getKey();
            Point pos2 = entry.getValue();
            BufferedImage img2 = images.get(key);
            if (img1 == img2 && pos1.equals(pos2)) continue;
            Rectangle rect2 = new Rectangle(pos2.x, pos2.y, img2.getWidth(), img2.getHeight());
            if (rect1.intersects(rect2)) return true;
        }
        BufferedImage muroImg = images.get('+');
        for (Map.Entry<Point, Character> entry : muros.entrySet()) {
            Point pos2 = entry.getKey();
            Rectangle rect2 = new Rectangle(pos2.x, pos2.y, muroImg.getWidth(), muroImg.getHeight());
            if (rect1.intersects(rect2)) return true;
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        dibujarMuros(g);
        dibujarCoches(g);
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        int width = getWidth();
        int height = getHeight();
        for (int i = 0; i < width; i += N) {
            g.drawLine(i, 0, i, height);
        }
        for (int i = 0; i < height; i += N) {
            g.drawLine(0, i, width, i);
        }
    }

    private void dibujarCoches(Graphics g) {
        for (Coche c : controller.getCoches()) {
            char id = c.getId();
            BufferedImage imageCoche = imagenes.get(id);
            Point imagePosCoche = imagenesPos.get(id);
            g.drawImage(imageCoche, imagePosCoche.x, imagePosCoche.y, this);
        }
    }

    private void dibujarMuros(Graphics g) {
        BufferedImage muroImg = imagenes.get('+');
        for (Casilla m : controller.getMuros()) {
            int width = m.getX();
            int height = m.getY();
            g.drawImage(muroImg, height * N, width * N, this);
        }
    }

    private BufferedImage getScaledImage(BufferedImage src, int width, int height) {
        Image tmp = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    public Map<Character, Point> getImagenesPos() {
        return imagenesPos;
    }

    public void setImagenesPos(Map<Character, Point> imagenesPos) {
        this.imagenesPos = imagenesPos;
    }

    private Casilla saca(Casilla[] casillas, int y) {
        return casillas[y];
    }
}
