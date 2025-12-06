package org.example.ball.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GraphDemoServer extends JFrame {

    public GraphDemoServer() {
        super("game");

        setSize(700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(50000)) {
                System.out.println("Server: waiting for client...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Server: client connected");

                SwingUtilities.invokeLater(() -> {
                    try {
                        GComponentServer comp = new GComponentServer(clientSocket);
                        add(comp, BorderLayout.CENTER);
                        revalidate();
                        comp.requestFocusInWindow();

                        this.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyPressed(KeyEvent e) {
                                if (e.getKeyCode() == KeyEvent.VK_ENTER && comp.gameOver) {
                                    comp.resetGame();
                                }
                            }
                        });
                        this.setFocusable(true);
                        this.requestFocusInWindow();

                    } catch (Exception ex) { ex.printStackTrace(); }
                });

            } catch (IOException e) { e.printStackTrace(); }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GraphDemoServer::new);
    }
}
