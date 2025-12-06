package org.example.ball.client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GraphDemoClient extends JFrame {

    public GraphDemoClient() throws IOException {
        super("game");
        setSize(700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        GComponentClient comp = new GComponentClient();
        add(comp, BorderLayout.CENTER);

        setVisible(true);
        SwingUtilities.invokeLater(comp::requestFocusInWindow);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { new GraphDemoClient(); }
            catch (IOException e) { throw new RuntimeException(e); }
        });
    }
}
