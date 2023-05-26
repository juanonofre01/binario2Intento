package tetris;

import javax.swing.JFrame;

public class Ventana {
    public static final int WIDTH = 445, HEIGHT = 629;

    private Interfaz board;
    private Titulo titulo;
    private JFrame ventana;

    public Ventana() {

        ventana = new JFrame("Tetris");
        ventana.setSize(WIDTH, HEIGHT);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);

        board = new Interfaz();
        titulo = new Titulo(this);

        ventana.addKeyListener(board);
        ventana.addKeyListener(titulo);

        ventana.add(titulo);

        ventana.setVisible(true);
    }

    public void startTetris() {
        ventana.remove(titulo);
        ventana.addMouseMotionListener(board);
        ventana.addMouseListener(board);
        ventana.add(board);
        board.startGame();
        ventana.revalidate();
    }

    public static void main(String[] args) {
        new Ventana();
    }

}
