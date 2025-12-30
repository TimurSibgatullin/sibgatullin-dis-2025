package org.example.Client.display;
import org.example.Client.GameClient;
import org.example.CommonFiles.Statistics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameFrame extends JFrame {

    private final JLayeredPane layers;
    private final GamePanel gamePanel;
    private final GameClient client;
    public StartOverlay overlay;

    public GameFrame(GameClient client, GamePanel panel) {
        setTitle("Tank.io");
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.client = client;
        gamePanel = panel;

        layers = new JLayeredPane();
        layers.setLayout(null);

        overlay = new StartOverlay(client, null);

        layers.add(panel, JLayeredPane.DEFAULT_LAYER);
        layers.add(overlay, JLayeredPane.MODAL_LAYER);

        setContentPane(layers);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeLayers();
            }
        });

        resizeLayers();
        setVisible(true);
    }

    public StartOverlay getOverlay() {
        return overlay;
    }

    private void resizeLayers() {
        Dimension size = getContentPane().getSize();
        gamePanel.setBounds(0, 0, size.width, size.height);
        overlay.setBounds(0, 0, size.width, size.height);
    }

    public void showOverlay(Statistics ps) {
        layers.remove(overlay);
        overlay = new StartOverlay(client, ps);
        layers.add(overlay, JLayeredPane.MODAL_LAYER);
        resizeLayers();
        layers.revalidate();
        layers.repaint();
        overlay.setVisible(true);
        overlay.requestFocusInWindow();
    }

    public void hideOverlay() {
        overlay.setVisible(false);
        gamePanel.requestFocusInWindow();
    }
}