package org.example.Client.display;

import java.awt.*;

public class Camera {

    private float x, y;
    private int screenW, screenH;

    public void update(float playerX, float playerY, int w, int h) {
        this.x = playerX;
        this.y = playerY;
        this.screenW = w;
        this.screenH = h;
    }

    public int worldToScreenX(float worldX) {
        return (int) (worldX - x + screenW / 2f);
    }

    public int worldToScreenY(float worldY) {
        return (int) (worldY - y + screenH / 2f);
    }

    public Rectangle worldToScreenRect(float x, float y, float w, float h) {
        return new Rectangle(
                worldToScreenX(x),
                worldToScreenY(y),
                (int) w,
                (int) h
        );
    }

    public float worldLeft()   { return x - screenW / 2f; }
    public float worldRight()  { return x + screenW / 2f; }
    public float worldTop()    { return y - screenH / 2f; }
    public float worldBottom() { return y + screenH / 2f; }
}