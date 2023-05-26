package tetris;

import java.awt.Color;
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
import javax.swing.JPanel;
import javax.swing.Timer;

public class Titulo extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private BufferedImage instrucciones;
	private Ventana ventana;
	private BufferedImage[] playButton = new BufferedImage[2];
	private Timer tiempo;
	
	
	public Titulo(Ventana ventana){
                instrucciones = Imagen.loadImage("/Flechas.jpg");
		tiempo = new Timer(1000/60, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
			
		});
		tiempo.start();
		this.ventana = ventana;
		
		
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
		
		g.fillRect(0, 0, Ventana.WIDTH, Ventana.HEIGHT);
		
		
		g.drawImage(instrucciones, Ventana.WIDTH/2 - instrucciones.getWidth()/2,
				30 - instrucciones.getHeight()/2 + 150, null);
		
                g.setColor(Color.WHITE);
		g.drawString("Presione espacio para iniciar el juego!", 150, Ventana.HEIGHT / 2 + 100);
		
		
	}	

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == KeyEvent.VK_SPACE) {
            ventana.startTetris();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
