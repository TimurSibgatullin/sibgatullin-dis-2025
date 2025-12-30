//package org.example.trash;
//
//import org.example.Client.BulletState;
//import org.example.Client.GameState;
//import org.example.Client.OrbState;
//import org.example.Client.PlayerState;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.Random;
//
//public class GamePanel extends JPanel {
//    private final GameState state;
//    private final int WORLD_SIZE = 4000;
//    private final int GRID_STEP = 50;
//
//    public GamePanel(GameState state) {
//        this.state = state;
//        setFocusable(true);
//        setBackground(Color.LIGHT_GRAY);
//    }
//
//    private void drawWorldBounds(Graphics g, PlayerState me) {
//        int cx = getWidth() / 2;
//        int cy = getHeight() / 2;
//
//        int left   = (int) (0 - me.x + cx);
//        int top    = (int) (0 - me.y + cy);
//        int right  = (int) (WORLD_SIZE - me.x + cx);
//        int bottom = (int) (WORLD_SIZE - me.y + cy);
//
//        g.setColor(Color.BLACK);
//        g.drawRect(left, top, right - left, bottom - top);
//    }
//
//    private void drawGrid(Graphics g, PlayerState me) {
//        int cx = getWidth() / 2;
//        int cy = getHeight() / 2;
//
//        g.setColor(Color.gray);
//
//        int startX = Math.max(0, (int) (me.x - cx));
//        int endX   = Math.min(WORLD_SIZE, (int) (me.x + cx));
//        int startY = Math.max(0, (int) (me.y - cy));
//        int endY   = Math.min(WORLD_SIZE, (int) (me.y + cy));
//
//        for (int x = (startX / GRID_STEP) * GRID_STEP; x <= endX; x += GRID_STEP) {
//            int sx = x - (int) me.x + cx;
//            g.drawLine(sx, (int) (cy - me.y), sx, (int) (WORLD_SIZE - me.y + cy));
//        }
//
//        for (int y = (startY / GRID_STEP) * GRID_STEP; y <= endY; y += GRID_STEP) {
//            int sy = y - (int) me.y + cy;
//            g.drawLine((int) (cx - me.x), sy, (int) (WORLD_SIZE - me.x + cx), sy);
//        }
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        PlayerState me = state.getMyPlayer();
//        if (me == null) return;
//
//        drawGrid(g, me);
//        drawWorldBounds(g, me);
//
//        int cx = getWidth() / 2;
//        int cy = getHeight() / 2;
//
//        for (PlayerState p : state.getPlayers()) {
//            int sx = (int) (p.x - me.x + cx);
//            int sy = (int) (p.y - me.y + cy);
//
//            g.setColor(p.id == me.id ? Color.GREEN : Color.RED);
//            g.fillOval(sx - 15, sy - 15, 30, 30);
//
//            int bx = (int) (sx + Math.cos(p.angle) * 20);
//            int by = (int) (sy + Math.sin(p.angle) * 20);
//            g.drawLine(sx, sy, bx, by);
//        }
//
//        g.setColor(Color.YELLOW);
//        for (BulletState b : state.getBullets()) {
//            int bx = (int) (b.x - me.x + cx);
//            int by = (int) (b.y - me.y + cy);
//            g.fillOval(bx - 3, by - 3, 6, 6);
//        }
//
//
//
//        Random r = new Random();
//        g.setColor(Color.gray);
//        for (OrbState o : state.getOrbs()) {
//            int sx = (int) (o.x - me.x + cx);
//            int sy = (int) (o.y - me.y + cy);
//
//            g.fillOval(
//                    sx - o.radius,
//                    sy - o.radius,
//                    o.radius * 2,
//                    o.radius * 2
//            );
//        }
//
//        drawHUD(g, me);
//    }
//
//
//    private void drawHUD(Graphics g, PlayerState me) {
//        g.setColor(Color.BLACK);
//        g.drawString("HP: " + me.hp, 10, 20);
//        g.drawString("LVL: " + me.level, 10, 40);
//    }
//}
//
