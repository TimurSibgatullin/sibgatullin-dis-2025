package org.example.ball.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GComponentServer extends JComponent {

    private final DataInputStream dis;
    private final DataOutputStream dos;
    private Timer timer;

    int x, y, bounds = 64;
    boolean move_up, move_left;
    int speed = 4;

    int bYS = 300;
    volatile int bYC = 300;

    Image image;
    volatile boolean gameOver = false;

    public GComponentServer(Socket clientSocket) throws IOException {
        dis = new DataInputStream(clientSocket.getInputStream());
        dos = new DataOutputStream(clientSocket.getOutputStream());

        x = 300;
        y = 300;
        move_left = Math.random() > 0.5;
        move_up = Math.random() > 0.5;

        image = new ImageIcon(getClass().getResource("/ball.png")).getImage();
        setDoubleBuffered(true);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) { bYS = e.getY(); }
            @Override
            public void mouseDragged(MouseEvent e) { bYS = e.getY(); }
        });


        Thread reader = new Thread(() -> {
            try {
                while (true) {
                    bYC = dis.readInt();
                }
            } catch (IOException e) {
                gameOver = true;
            }
        });
        reader.setDaemon(true);
        reader.start();

        timer = new Timer(10, e -> updateGame());
        timer.start();
    }

    private void updateGame() {
        if (gameOver) return;

        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return;

        if (y < 0) move_up = false;
        if (y > h - bounds) move_up = true;

        x += move_left ? -speed : speed;
        y += move_up ? -speed : speed;

        Rectangle ball = new Rectangle(x, y, bounds, bounds);
        Rectangle paddleLeft = new Rectangle(10, bYS, 10, 200);
        Rectangle paddleRight = new Rectangle(w - 20, bYC, 10, 200);

        if (ball.intersects(paddleLeft)) move_left = false;
        if (ball.intersects(paddleRight)) move_left = true;

        if (x < -bounds) {
            System.out.println("CLIENT WINS!");
            gameOver = true;
        } else if (x > w) {
            System.out.println("SERVER WINS!");
            gameOver = true;
        }

        try {
            dos.writeInt(bYS);
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeBoolean(gameOver);
            dos.flush();
        } catch (IOException e) {
            gameOver = true;
        }

        if (gameOver) timer.stop();

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (image != null) g2d.drawImage(image, x, y, bounds, bounds, null);
        g2d.fillRect(10, bYS, 10, 200);
        g2d.fillRect(getWidth() - 20, bYC, 10, 200);
    }

    public void resetGame() {
        x = getWidth() / 2 - bounds / 2;
        y = getHeight() / 2 - bounds / 2;
        move_left = Math.random() > 0.5;
        move_up = Math.random() > 0.5;
        gameOver = false;
        timer.start();
    }
}