package org.example.CommonFiles;

public class PlayerStatsLevels {
    public int healing = 0;
    public int damage = 0;
    public int defense = 0;
    public int fireRate = 0;
    public int speed = 0;
    public int bulletSpeed = 0;

    public int getSum() {
        return healing + damage + defense + fireRate + speed + bulletSpeed;
    }
}
