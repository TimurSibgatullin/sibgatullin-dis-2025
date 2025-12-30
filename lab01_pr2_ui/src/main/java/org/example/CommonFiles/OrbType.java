package org.example.CommonFiles;

import java.awt.*;

public enum OrbType {
    SMALL(7, 20, 20, new Color(201, 132, 4)),
    MEDIUM(10, 50, 50, new Color(125, 21, 163)),
    BIG(14, 100, 100, new Color(240, 217, 17)),
    MEGA_BIG(22, 1000, 1000, new Color(86, 181, 27));

    public final int radius;
    public final int hp;
    public final int exp;
    public final Color color;

    OrbType(int radius, int hp, int exp, Color color) {
        this.radius = radius;
        this.hp = hp;
        this.exp = exp;
        this.color = color;
    }
}
