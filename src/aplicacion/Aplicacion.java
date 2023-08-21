package aplicacion;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class Aplicacion {

    public static void main(String[] args) {
        Thread songThread = new Thread(new SongPlayer());
        Thread imageThread = new Thread(new ImageDrawer());

        songThread.start();
        imageThread.start();
    }
    
    static class SongPlayer implements Runnable {
    @Override
    public void run() {
        try {
            File songFile = new File("cancion.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(songFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

            Thread.sleep(clip.getMicrosecondLength() / 1000);  // Espera hasta que termine la canción
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    
static class ImageDrawer implements Runnable {
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    private static int imageX = 0;
    private static int imageY = 0;
    private static int imageSpeedX = 5;
    private static int imageSpeedY = 5;

    @Override
    public void run() {
        JFrame frame = new JFrame("Imagen en Movimiento");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setVisible(true);

        JPanel panel = new ImagePanel();
        frame.add(panel);

        while (true) {
            imageX += imageSpeedX;
            imageY += imageSpeedY;

            if (imageX > SCREEN_WIDTH || imageX < 0) {
                imageSpeedX = -imageSpeedX;
            }
            if (imageY > SCREEN_HEIGHT || imageY < 0) {
                imageSpeedY = -imageSpeedY;
            }

            panel.repaint();

            try {
                Thread.sleep(30);  // Actualiza cada 30 milisegundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ImagePanel extends JPanel {
        private BufferedImage image;

        public ImagePanel() {
            try {
                image = ImageIO.read(new File("imagen.jpg"));  // Reemplaza con la ruta de tu imagen
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, ImageDrawer.imageX, ImageDrawer.imageY, null);
        }
    }
}
}
