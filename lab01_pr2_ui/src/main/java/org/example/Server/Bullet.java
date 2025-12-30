package org.example.Server;

import static org.example.Server.GameServer.WORLD_SIZE;

public class Bullet {
    public float x, y;
    public float vx, vy;
    public float angle;
    public float speed;
    public int ownerId;
    public long timeToLive;

    public Bullet(Player owner, float x, float y, float angle) {
        this.ownerId = owner.id;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.speed = owner.stats.bulletSpeed;
        this.timeToLive = (long) (owner.stats.bulletSpeed * 1000L);
    }

    public Boolean update() {
        vx = (float) (Math.cos(angle) * speed);
        vy = (float) (Math.sin(angle) * speed);
        return !(x + vx < 0 || y + vy < 0 || x + vx > WORLD_SIZE || y + vy > WORLD_SIZE);
    }
}