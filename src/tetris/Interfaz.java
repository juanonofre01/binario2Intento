package tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Interfaz extends JPanel implements KeyListener, MouseListener, MouseMotionListener {

	//Assets
    /**
     *ajcvslahcvalshcva
     */
    private static final long serialVersionUID = 1L;

    private BufferedImage pausa, refresh;

	//board dimensions (the playing area)
    private final int boardHeight = 20, boardWidth = 10;

	// block size
    public static final int blockSize = 30;

	// field
    private Color[][] board = new Color[boardHeight][boardWidth];

	// array with all the possible shapes
    private Forma[] shapes = new Forma[7];

	// currentShape
    private static Forma currentShape, nextShape;

	// game loop
    private Timer looper;

    private int FPS = 60;

    private int delay = 1000 / FPS;

	// mouse events variables
    private int mouseX, mouseY;

    private boolean leftClick = false;

    private Rectangle stopBounds, refreshBounds;

    private boolean gamePaused = false;

    private boolean gameOver = false;
    
    private Color[] colors = {Color.decode("#ed1c24"), Color.decode("#ff7f27"), Color.decode("#fff200"), 
        Color.decode("#22b14c"), Color.decode("#00a2e8"), Color.decode("#a349a4"), Color.decode("#3f48cc")};
    private Random random = new Random();
	// buttons press lapse
    private Timer buttonLapse = new Timer(300, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            buttonLapse.stop();
        }
    });

	// score
    private int puntos = 0;

    public Interfaz() {

        pausa = Imagen.loadImage("/pause.png");
        refresh = Imagen.loadImage("/refresh.png");

        mouseX = 0;
        mouseY = 0;

        stopBounds = new Rectangle(350, 500, pausa.getWidth(), pausa.getHeight() + pausa.getHeight() / 2);
        refreshBounds = new Rectangle(350, 500 - refresh.getHeight() - 20, refresh.getWidth(),
                refresh.getHeight() + refresh.getHeight() / 2);

		// create game looper
        looper = new Timer(delay, new GameLooper());

		// create shapes
        shapes[0] = new Forma(new int[][]{
            {1, 1, 1, 1} // I shape;
        }, this, colors[0]);

        shapes[1] = new Forma(new int[][]{
            {1, 1, 1},
            {0, 1, 0}, // T shape;
        }, this, colors[1]);

        shapes[2] = new Forma(new int[][]{
            {1, 1, 1},
            {1, 0, 0}, // L shape;
        }, this, colors[2]);

        shapes[3] = new Forma(new int[][]{
            {1, 1, 1},
            {0, 0, 1}, // J shape;
        }, this, colors[3]);

        shapes[4] = new Forma(new int[][]{
            {0, 1, 1},
            {1, 1, 0}, // S shape;
        }, this, colors[4]);

        shapes[5] = new Forma(new int[][]{
            {1, 1, 0},
            {0, 1, 1}, // Z shape;
        }, this, colors[5]);

        shapes[6] = new Forma(new int[][]{
            {1, 1},
            {1, 1}, // O shape;
        }, this, colors[6]);

    }

    private void update() {
        if (stopBounds.contains(mouseX, mouseY) && leftClick && !buttonLapse.isRunning() && !gameOver) {
            buttonLapse.start();
            gamePaused = !gamePaused;
        }

        if (refreshBounds.contains(mouseX, mouseY) && leftClick) {
            startGame();
        }

        if (gamePaused || gameOver) {
            return;
        }
        currentShape.actualizar();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {

                if (board[row][col] != null) {
                    g.setColor(board[row][col]);
                    g.fillRect(col * blockSize, row * blockSize, blockSize, blockSize);
                }

            }
        }
        g.setColor(nextShape.getColor());
        for (int row = 0; row < nextShape.getCoordenadas().length; row++) {
            for (int col = 0; col < nextShape.getCoordenadas()[0].length; col++) {
                if (nextShape.getCoordenadas()[row][col] != 0) {
                    g.fillRect(col * 30 + 320, row * 30 + 50, Interfaz.blockSize, Interfaz.blockSize);
                }
            }
        }
        currentShape.enviar(g);

        if (stopBounds.contains(mouseX, mouseY)) {
            g.drawImage(pausa.getScaledInstance(pausa.getWidth() + 3, pausa.getHeight() + 3, BufferedImage.SCALE_DEFAULT), stopBounds.x + 3, stopBounds.y + 3, null);
        } else {
            g.drawImage(pausa, stopBounds.x, stopBounds.y, null);
        }

        if (refreshBounds.contains(mouseX, mouseY)) {
            g.drawImage(refresh.getScaledInstance(refresh.getWidth() + 3, refresh.getHeight() + 3,
                    BufferedImage.SCALE_DEFAULT), refreshBounds.x + 3, refreshBounds.y + 3, null);
        } else {
            g.drawImage(refresh, refreshBounds.x, refreshBounds.y, null);
        }

        if (gamePaused) {
            String gamePausedString = "JUEGO EN PAUSA";
            g.setColor(Color.WHITE);
            g.setFont(new Font("Georgia", Font.BOLD, 30));
            g.drawString(gamePausedString, 35, Ventana.HEIGHT / 2);
        }
        if (gameOver) {
            String gameOverString = "GAME OVER";
            g.setColor(Color.WHITE);
            g.setFont(new Font("Georgia", Font.BOLD, 30));
            g.drawString(gameOverString, 50, Ventana.HEIGHT / 2);
        }
        g.setColor(Color.WHITE);

        g.setFont(new Font("Georgia", Font.BOLD, 20));

        g.drawString("PUNTAJE ", Ventana.WIDTH - 125, Ventana.HEIGHT / 2);
        g.drawString(puntos + "", Ventana.WIDTH - 125, Ventana.HEIGHT / 2 + 30);

        g.setColor(Color.WHITE);

        for (int i = 0; i <= boardHeight; i++) {
            g.drawLine(0, i * blockSize, boardWidth * blockSize, i * blockSize);
        }
        for (int j = 0; j <= boardWidth; j++) {
            g.drawLine(j * blockSize, 0, j * blockSize, boardHeight * 30);
        }
    }

    public void setNextShape() {
        int index = random.nextInt(shapes.length);
        int colorIndex = random.nextInt(colors.length);
        nextShape = new Forma(shapes[index].getCoordenadas(), this, colors[colorIndex]);
    }

    public void setCurrentShape() {
        currentShape = nextShape;
        setNextShape();

        for (int row = 0; row < currentShape.getCoordenadas().length; row++) {
            for (int col = 0; col < currentShape.getCoordenadas()[0].length; col++) {
                if (currentShape.getCoordenadas()[row][col] != 0) {
                    if (board[currentShape.getY() + row][currentShape.getX() + col] != null) {
                        gameOver = true;
                    }
                }
            }
        }

    }

    public Color[][] getBoard() {
        return board;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            currentShape.rotarForma();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            currentShape.setDeltaX(1);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            currentShape.setDeltaX(-1);
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentShape.velocidadSubir();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentShape.velocidadBajar();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void startGame() {
        detenerJuego();
        setNextShape();
        setCurrentShape();
        gameOver = false;
        looper.start();

    }

    public void detenerJuego() {
        puntos = 0;

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = null;
            }
        }
        looper.stop();
    }

    class GameLooper implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            update();
            repaint();
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClick = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClick = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void addPuntos() {
        puntos++;
    }
    
    
    
    
    
    
     private void escribirBinario() throws IOException {
        try {
            FileOutputStream archivo= new FileOutputStream("puntos.bin");
            ObjectOutputStream escritura = new ObjectOutputStream(archivo);
            
            escritura.writeObject(new puntos(19, "Juan Angel"));
            System.out.println("Objeto añadido con exito");
            escritura.close();
            
        } catch (FileNotFoundException error) {
            error.printStackTrace(System.out);
        }
        
    }
    
    public static void main(String[] args) throws IOException {
        Interfaz test= new Interfaz();
        test.escribirBinario();
    }

    
}
