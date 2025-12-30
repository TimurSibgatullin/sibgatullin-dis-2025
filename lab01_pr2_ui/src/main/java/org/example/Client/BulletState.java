package org.example.Client;

public class BulletState {
    public float x, y;
    public float angle;
    public float speed = 10f;
    public int ownerId;

    public BulletState(int ownerId, float x, float y, float angle) {
        this.ownerId = ownerId;
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public void update() {
        x += Math.cos(angle) * speed;
        y += Math.sin(angle) * speed;
    }
}