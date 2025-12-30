package org.example.Server;

import org.example.CommonFiles.PlayerStatsLevels;

import java.net.InetAddress;

public class Player {
    public class PlayerStats {
        public float maxHp = 100;
        public float healing = 1.0f;
        public float damage = 10;
        public float defense = 0;
        public float fireRate = 10;
        public float speed = 3;
        public float ramDamage = 5;
        public float bulletSpeed = 3;

        void updateStats() {
            maxHp += statsLevels.healing * 0.66f;
            healing += statsLevels.healing * 0.66f;
            damage += statsLevels.damage * 0.66f;
            defense += statsLevels.defense * 0.66f;
            fireRate += statsLevels.fireRate * 0.66f;
            speed += statsLevels.speed * 0.66f;
            ramDamage = 5;
            bulletSpeed += statsLevels.bulletSpeed * 0.66f;
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
    public int lvl = 0;
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
        xa = 0;
        ya = 0;
        hp = 100;
        stats.maxHp = 100;
        stats.healing = 1.0f;
        stats.damage = 10;
        stats.defense = 0;
        stats.fireRate = 10;
        stats.speed = 3;
        stats.ramDamage = 5;
        stats.bulletSpeed = 3;
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
