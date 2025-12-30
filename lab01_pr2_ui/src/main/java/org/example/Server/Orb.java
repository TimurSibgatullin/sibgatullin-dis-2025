package org.example.Server;

import java.awt.*;

public class Orb {
    public int id;
    public float x, y;
    public int radius;
    public int hp;
    public int exp;
    public Color color;

    public Orb(int id, float x, float y, int radius, int hp, int exp, Color color) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.hp = hp;
        this.exp = exp;
        this.color = color;
    }
}