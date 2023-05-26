package tetris;

import java.awt.Color;
import java.awt.Graphics;

public class Forma {

    private Color color;

    private int x, y;

    private long tiempo, ultimaVez;

    private int normal = 600, fast = 50;

    private int delay;

    private int[][] coordenadas;

    private int[][] referencia;

    private int deltaX;

    private Interfaz board;

    private boolean choque = false, moverX = false;

    private int tiempoTranscurridDesdeChoque = -1;

    public Forma(int[][] coordenadas, Interfaz board, Color color) {
        this.coordenadas = coordenadas;
        this.board = board;
        this.color = color;
        deltaX = 0;
        x = 4;
        y = 0;
        delay = normal;
        tiempo = 0;
        ultimaVez = System.currentTimeMillis();
        referencia = new int[coordenadas.length][coordenadas[0].length];

        System.arraycopy(coordenadas, 0, referencia, 0, coordenadas.length);

    }

    long deltaTime;

    public void actualizar() {
        moverX = true;
        deltaTime = System.currentTimeMillis() - ultimaVez;
        tiempo += deltaTime;
        ultimaVez = System.currentTimeMillis();

        if (choque && tiempoTranscurridDesdeChoque > 500) {
            for (int row = 0; row < coordenadas.length; row++) {
                for (int col = 0; col < coordenadas[0].length; col++) {
                    if (coordenadas[row][col] != 0) {
                        board.getBoard()[y + row][x + col] = color;
                    }
                }
            }
            checkLine();
            board.addPuntos();
            board.setCurrentShape();
            tiempoTranscurridDesdeChoque = -1;
        }

        // check moving horizontal
        if (!(x + deltaX + coordenadas[0].length > 10) && !(x + deltaX < 0)) {

            for (int row = 0; row < coordenadas.length; row++) {
                for (int col = 0; col < coordenadas[row].length; col++) {
                    if (coordenadas[row][col] != 0) {
                        if (board.getBoard()[y + row][x + deltaX + col] != null) {
                            moverX = false;
                        }

                    }
                }
            }

            if (moverX) {
                x += deltaX;
            }

        }

        // Check position + height(number of row) of shape
        if (tiempoTranscurridDesdeChoque == -1) {
            if (!(y + 1 + coordenadas.length > 20)) {

                for (int row = 0; row < coordenadas.length; row++) {
                    for (int col = 0; col < coordenadas[row].length; col++) {
                        if (coordenadas[row][col] != 0) {

                            if (board.getBoard()[y + 1 + row][x + col] != null) {
                                collision();
                            }
                        }
                    }
                }
                if (tiempo > delay) {
                    y++;
                    tiempo = 0;
                }
            } else {
                collision();
            }
        } else {
            tiempoTranscurridDesdeChoque += deltaTime;
        }

        deltaX = 0;
    }

    private void collision() {
        choque = true;
        tiempoTranscurridDesdeChoque = 0;
    }

    public void enviar(Graphics g) {

        g.setColor(color);
        for (int row = 0; row < coordenadas.length; row++) {
            for (int col = 0; col < coordenadas[0].length; col++) {
                if (coordenadas[row][col] != 0) {
                    g.fillRect(col * 30 + x * 30, row * 30 + y * 30, Interfaz.blockSize, Interfaz.blockSize);
                }
            }
        }

//        for (int row = 0; row < referencia.length; row++) {
//            for (int col = 0; col < referencia[0].length; col++) {
//                if (referencia[row][col] != 0) {
//                    g.fillRect(col * 30 + 320, row * 30 + 160, Interfaz.blockSize, Interfaz.blockSize);
//                }
//
//            }
//
//        }

    }

    private void checkLine() {
        int size = board.getBoard().length - 1;

        for (int i = board.getBoard().length - 1; i > 0; i--) {
            int count = 0;
            for (int j = 0; j < board.getBoard()[0].length; j++) {
                if (board.getBoard()[i][j] != null) {
                    count++;
                }

                board.getBoard()[size][j] = board.getBoard()[i][j];
            }
            if (count < board.getBoard()[0].length) {
                size--;
            }
        }
    }

    public void rotarForma() {

        int[][] rotarForma = null;

        rotarForma = transponerMatrix(coordenadas);

        rotarForma = invertirFilas(rotarForma);

        if ((x + rotarForma[0].length > 10) || (y + rotarForma.length > 20)) {
            return;
        }

        for (int row = 0; row < rotarForma.length; row++) {
            for (int col = 0; col < rotarForma[row].length; col++) {
                if (rotarForma[row][col] != 0) {
                    if (board.getBoard()[y + row][x + col] != null) {
                        return;
                    }
                }
            }
        }
        coordenadas = rotarForma;
    }

    private int[][] transponerMatrix(int[][] matrix) {
        int[][] temp = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp[j][i] = matrix[i][j];
            }
        }
        return temp;
    }

    private int[][] invertirFilas(int[][] matrix) {

        int middle = matrix.length / 2;

        for (int i = 0; i < middle; i++) {
            int[] temp = matrix[i];

            matrix[i] = matrix[matrix.length - i - 1];
            matrix[matrix.length - i - 1] = temp;
        }

        return matrix;

    }

    public Color getColor() {
        return color;
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    public void velocidadSubir() {
        delay = fast;
    }

    public void velocidadBajar() {
        delay = normal;
    }

    public int[][] getCoordenadas() {
        return coordenadas;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
