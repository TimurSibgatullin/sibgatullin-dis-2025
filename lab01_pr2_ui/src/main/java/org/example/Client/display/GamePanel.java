package org.example.Client.display;

import org.example.Client.BulletState;
import org.example.Client.GameState;
import org.example.Client.OrbState;
import org.example.Client.PlayerState;
import org.example.CommonFiles.OrbType;
import org.example.CommonFiles.PlayerStatsLevels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public static final int WORLD_SIZE = 3000;
    public static final int GRID_STEP = 50;

    private static final Color GRID_COLOR = new Color(210, 210, 210);
    private static final Color BORDER_COLOR = Color.BLACK;
    private static final Color BULLET_COLOR = Color.YELLOW;
    private static final Logger log = LoggerFactory.getLogger(GamePanel.class);

    private final GameState state;
    private final Camera camera;

    public GamePanel(GameState state) {
        this.state = state;
        this.camera = new Camera();

        setDoubleBuffered(true);
        setFocusable(true);
        setBackground(Color.LIGHT_GRAY);
        setDoubleBuffered(true);

        new Timer(16, e -> repaint()).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        PlayerState me = state.getMyPlayer();
        if (me == null) return;

        camera.update(me.x, me.y, getWidth(), getHeight());


        drawGrid(g);
        drawWorldBounds(g);
        drawOrbs(g);
        drawBullets(g);
        drawPlayers(g);
        if (!state.isOverlayVisible) {
            drawLevelBar(g, me);
            drawUpgrades(g, state.statsLevels);
        }
    }


    private void drawWorldBounds(Graphics g) {
        g.setColor(BORDER_COLOR);

        Rectangle r = camera.worldToScreenRect(
                0, 0,
                WORLD_SIZE, WORLD_SIZE
        );

        g.drawRect(r.x, r.y, r.width, r.height);
    }

    private void drawGrid(Graphics g) {
        g.setColor(GRID_COLOR);

        int startX = Math.max(0, (int) camera.worldLeft());
        int endX   = Math.min(WORLD_SIZE, (int) camera.worldRight());
        int startY = Math.max(0, (int) camera.worldTop());
        int endY   = Math.min(WORLD_SIZE, (int) camera.worldBottom());

        for (int x = (startX / GRID_STEP) * GRID_STEP; x <= endX; x += GRID_STEP) {
            int sx = camera.worldToScreenX(x);
            g.drawLine(sx, camera.worldToScreenY(startY),
                    sx, camera.worldToScreenY(endY));
        }

        for (int y = (startY / GRID_STEP) * GRID_STEP; y <= endY; y += GRID_STEP) {
            int sy = camera.worldToScreenY(y);
            g.drawLine(camera.worldToScreenX(startX), sy,
                    camera.worldToScreenX(endX), sy);
        }
    }


    private void drawPlayers(Graphics g) {
        PlayerState me = state.getMyPlayer();
        Graphics2D g2 = (Graphics2D) g;

        for (PlayerState p : state.getPlayers()) {
            if (!p.isActive) continue;
            int sx;
            int sy;
            if (p.id == me.id) {
                sx = getWidth() / 2;
                sy = getHeight() / 2;
            } else {
                sx = camera.worldToScreenX(p.x);
                sy = camera.worldToScreenY(p.y);
            }

            if (p.id == me.id) {
                g.setColor(new Color(7, 148, 54));
            } else {
                g.setColor(new Color(161, 17, 11));
            }
            g.fillOval(sx - 15, sy - 15, 30, 30);

            double angle = p.angle;

            int gunLength = 15;
            int gunWidth = 7;

            Graphics2D gGun = (Graphics2D) g2.create();
            gGun.translate(sx, sy);
            gGun.rotate(angle);

            gGun.setColor(Color.DARK_GRAY);
            gGun.fillRect(10, -gunWidth / 2, gunLength, gunWidth);

            gGun.dispose();

            int barWidth = 30;
            int barHeight = 3;
            int barX = sx - barWidth / 2;
            int barY = sy - 25;

            g.setColor(new Color(40, 40, 40));
            g.fillRect(barX, barY, barWidth, barHeight);

            float hpPercent = Math.max(0f, Math.min(1f, p.hp / (float) p.maxHp));
            int hpFill = (int) (barWidth * hpPercent);

            g.setColor(new Color(120, 0, 0));
            g.fillRect(barX, barY, hpFill, barHeight);

            g.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics();

            String nick = p.nickname;
            int nickWidth = fm.stringWidth(nick);
            g.drawString(nick, sx - nickWidth / 2, sy + 28);

            String xpText = formatXp(p.xp);
            int xpWidth = fm.stringWidth(xpText);
            g.drawString(xpText, sx - xpWidth / 2, sy + 42);
        }
    }

    private String formatXp(int xp) {
        if (xp >= 1_000_000)
            return String.format("%.1fM", xp / 1_000_000f);
        if (xp >= 1_000)
            return String.format("%.1fk", xp / 1_000f);
        return String.valueOf(xp);
    }

    private void drawBullets(Graphics g) {
        g.setColor(BULLET_COLOR);

        for (BulletState b : state.getBullets()) {
            int sx = camera.worldToScreenX(b.x);
            int sy = camera.worldToScreenY(b.y);
            g.setColor(new Color(13, 147, 209));
            g.fillOval(sx - 3, sy - 3, 6, 6);
        }
    }

    private void drawOrbs(Graphics g) {
        for (OrbState o : state.getOrbs()) {
            g.setColor(o.color);
            int sx = camera.worldToScreenX(o.x);
            int sy = camera.worldToScreenY(o.y);

            g.fillOval(
                    sx - o.radius,
                    sy - o.radius,
                    o.radius * 2,
                    o.radius * 2
            );

            int barWidth = 25;
            int barHeight = 3;
            int barX = sx - barWidth / 2;
            int barY = sy - o.radius - 5;

            g.setColor(new Color(40, 40, 40));
            g.fillRect(barX, barY, barWidth, barHeight);
            float hpPercent = 0;
            if (o.radius == OrbType.SMALL.radius) {
                hpPercent = Math.max(0f, Math.min(1f, o.hp / (float) 20));
            } else if (o.radius == OrbType.MEDIUM.radius) {
                hpPercent = Math.max(0f, Math.min(1f, o.hp / (float) 50));
            } else if (o.radius == OrbType.BIG.radius) {
                hpPercent = Math.max(0f, Math.min(1f, o.hp / (float) 100));
            } else if (o.radius == OrbType.MEGA_BIG.radius) {
                hpPercent = Math.max(0f, Math.min(1f, o.hp / (float) 1000));
            }
            int hpFill = (int) (barWidth * hpPercent);

            g.setColor(new Color(120, 0, 0));
            g.fillRect(barX, barY, hpFill, barHeight);

            g.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics();
        }
    }

    private void drawLevelBar(Graphics g, PlayerState me) {
        int x = getWidth() / 2 - 100;
        int y = 20;
        int w = 200;
        int h = 16;

        float progress = (float) calculateXp(me.xp) / xpToNext(me.lvl);
        g.setColor(Color.BLACK);
        g.fillRect(x, y, w, h);
        g.setColor(Color.RED);
        g.fillRect(x, y, (int)(w * progress), h);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, w, h);
        g.setColor(Color.BLACK);
        g.drawString("LVL " + me.lvl, x + w + 10, y + h - 3);
    }

    private int calculateXp(int xp) {
        int lvl = 0;
        while (xp >= xpToNext(lvl)) {
            xp -= xpToNext(lvl);
            lvl++;
        }
        return xp;
    }

    int xpToNext(int level) {
        return (int) (6.5 * Math.pow(level, 1.85) + 4);
    }

    private void drawUpgradeBar(
            Graphics g,
            int x, int y,
            int width, int height,
            int level,
            String name
    ) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        int filled = (int)((level / 10f) * width);

        g.setColor(Color.RED);
        g.fillRect(x + 1, y + 1, filled - 1, height - 1);

        g.setColor(Color.BLACK);
        g.drawString(name + " " + level + "/10", x + 5, y + height - 4);
    }

    private void drawUpgrades(Graphics g, PlayerStatsLevels lvl) {
        int x = 20;
        int y = 20;
        int w = 160;
        int h = 16;
        int gap = 22;

        drawUpgradeBar(g, x, y, w, h, lvl.healing, "1 Heal");
        y += gap;
        drawUpgradeBar(g, x, y, w, h, lvl.damage, "2 Damage");
        y += gap;
        drawUpgradeBar(g, x, y, w, h, lvl.defense, "3 Defense");
        y += gap;
        drawUpgradeBar(g, x, y, w, h, lvl.fireRate, "4 Fire Rate");
        y += gap;
        drawUpgradeBar(g, x, y, w, h, lvl.speed, "5 Speed");
        y += gap;
        drawUpgradeBar(g, x, y, w, h, lvl.bulletSpeed, "6 Bullet Speed");
    }
}
