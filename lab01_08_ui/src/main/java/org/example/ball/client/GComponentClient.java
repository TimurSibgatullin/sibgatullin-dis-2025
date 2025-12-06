package org.example.ball.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GComponentClient extends JComponent {

    int x, y, bounds = 64;
    int bYS = 300;
    volatile int bYC = 300;
    int speed = 8;

    Image image;
    Socket socket;
    volatile boolean gameOver = false;

    private DataInputStream dis;
    private DataOutputStream dos;

    public GComponentClient() throws IOException {
        socket = new Socket("127.0.0.1", 50000);
        image = new ImageIcon(getClass().getResource("/ball.png")).getImage();
        setDoubleBuffered(true);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) { bYC = e.getY(); }
            @Override
            public void mouseDragged(MouseEvent e) { bYC = e.getY(); }
        });

        setFocusable(true);
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                requestFocusInWindow();
            }
        });

        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());

        new Thread(() -> {
            try {
                while (true) {
                    bYS = dis.readInt();
                    x = dis.readInt();
                    y = dis.readInt();
                    gameOver = dis.readBoolean();
                    if (gameOver) {
                        System.out.println("Game over!");
                    }
                    dos.writeInt(bYC);
                    dos.flush();
                    SwingUtilities.invokeLater(this::repaint);
                }
            } catch (IOException e) { System.out.println("Connection lost"); }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!gameOver && image != null) g2d.drawImage(image, x, y, null);
        g2d.fillRect(10, bYS, 10, 200);
        g2d.fillRect(getWidth() - 20, bYC, 10, 200);
    }
}
