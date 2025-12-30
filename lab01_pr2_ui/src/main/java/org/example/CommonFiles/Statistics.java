package org.example.CommonFiles;

public class Statistics {
    public String nickname;
    public String date;
    public int level;
    public long lifetime;
    public int kills;
    public int orbs;


    public Statistics(String n, String date, int level, long time, int kills, int orbs) {
        this.nickname = n;
        this.date = date;
        this.level = level;
        this.lifetime = time;
        this.kills = kills;
        this.orbs = orbs;
    }

    public Statistics(String nickname, int level, long lifetime, int kills, int orbs) {
        this.nickname = nickname;
        this.level = level;
        this.lifetime = lifetime;
        this.kills = kills;
        this.orbs = orbs;
    }

    @Override
    public String toString() {
        return "PlayerStat{" +
                "nickname='" + nickname + '\'' +
                ", date='" + date + '\'' +
                ", level=" + level +
                ", lifetime=" + lifetime +
                ", kills=" + kills +
                ", orbs=" + orbs +
                '}';
    }
}
