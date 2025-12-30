package org.example.Server;

import org.example.CommonFiles.PlayerStatsLevels;

import java.net.InetAddress;

public class Player {
    public static class DefaultPlayerStats {
        public static float maxHp = 100;
        public static float healing = 1.36f;
        public static float damage = 10;
        public static float defense = 0;
        public static float fireRate = 800;
        public static float speed = 3;
        public static float ramDamage = 5;
        public static float bulletSpeed = 6;
    }

    public class PlayerStats {
        public float maxHp = DefaultPlayerStats.maxHp;
        public float healing = DefaultPlayerStats.healing;
        public float damage = DefaultPlayerStats.damage;
        public float defense = DefaultPlayerStats.defense;
        public float fireRate = DefaultPlayerStats.fireRate;
        public float speed = DefaultPlayerStats.speed;
        public float ramDamage = DefaultPlayerStats.ramDamage;
        public float bulletSpeed = DefaultPlayerStats.bulletSpeed;

        void updateStats() {
            maxHp = DefaultPlayerStats.maxHp + statsLevels.healing * 10.00f;
            healing = DefaultPlayerStats.healing + statsLevels.healing * 0.196f;
            damage = DefaultPlayerStats.damage + statsLevels.damage * 5.66f;
            defense = DefaultPlayerStats.defense + statsLevels.defense * 3.33f;
            fireRate = DefaultPlayerStats.fireRate - statsLevels.fireRate * 40.00f;
            speed = DefaultPlayerStats.speed + statsLevels.speed * 0.2f;
            ramDamage = 5;
            bulletSpeed = DefaultPlayerStats.bulletSpeed + statsLevels.bulletSpeed * 0.6f;
        }

        void resetStats() {
            maxHp = DefaultPlayerStats.maxHp;
            healing = DefaultPlayerStats.healing;
            damage = DefaultPlayerStats.damage;
            defense = DefaultPlayerStats.defense;
            fireRate = DefaultPlayerStats.fireRate;
            speed = DefaultPlayerStats.speed;
            ramDamage = 5;
            bulletSpeed = DefaultPlayerStats.bulletSpeed;
        }
    }

    public InetAddress address;
    public int port;
    public int id;

    public String nickname;
    public long spawnTime = 0;
    public int kills = 0;
    public int orbs = 0;
    public long lifetime = 0;

    public int x;
    public int y;
    public int xa;
    public int ya;
    public float angle;
    public int hp = 100;
    public int lvl = 100;
    public int xp = 0;
    public boolean shooting = false;
    public long lastShotTime = 0;
    public long lastHealTime = 0;
    public boolean isActive = false;

    public PlayerStatsLevels statsLevels = new PlayerStatsLevels();
    public PlayerStats stats = new PlayerStats();


    public Player(int id, InetAddress address, int port) {
        this.id = id;
        this.address = address;
        this.port = port;

        this.x = (int) (Math.random() * 2000);
        this.y = (int) (Math.random() * 2000);
    }

    public void resetPlayer() {
        this.x = (int) (Math.random() * 2000);
        this.y = (int) (Math.random() * 2000);
        statsLevels = new PlayerStatsLevels();
        stats.resetStats();
        xa = 0;
        ya = 0;
        hp = (int) DefaultPlayerStats.maxHp;
        lvl = 0;
        xp = 0;
        kills = 0;
        orbs = 0;
        isActive = false;
        shooting = false;
    }

    @Override
    public String toString() {
        return "Player{" +
                "address=" + address +
                ", port=" + port +
                ", id=" + id +
                ", nickname='" + nickname + '\'' +
                ", spawnTime=" + spawnTime +
                ", kills=" + kills +
                ", orbs=" + orbs +
                ", x=" + x +
                ", y=" + y +
                ", xa=" + xa +
                ", ya=" + ya +
                ", angle=" + angle +
                ", hp=" + hp +
                ", level=" + lvl +
                ", exp=" + xp +
                ", shooting=" + shooting +
                ", lastShotTime=" + lastShotTime +
                ", isActive=" + isActive +
                ", stats=" + stats +
                '}';
    }
}
