package org.example.Client;
import org.example.CommonFiles.PlayerStatsLevels;
import org.example.Server.Player;

import java.util.*;

public class GameState {

    private final Map<Integer, PlayerState> players = new HashMap<>();
    private final List<BulletState> bullets = new ArrayList<>();
    Map<Integer, OrbState> orbs = new HashMap<>();
    protected PlayerState me = new PlayerState();
    public PlayerStatsLevels statsLevels = new PlayerStatsLevels();

    public void setMyId(int id) {
        this.me.id = id;
    }

    public void setMe(PlayerState me) {
        this.me = me;
    }

    public synchronized void clearOrbs() {
        orbs.clear();
    }

    public synchronized void addOrb(OrbState o) {
        orbs.put(o.id, o);
    }

    public synchronized Collection<OrbState> getOrbs() {
        return new ArrayList<>(orbs.values());
    }

    public synchronized void setPlayer(PlayerState p) {
        players.put(p.id, p);
    }

    public synchronized void clearPlayers() {
        players.clear();
    }

    public synchronized Collection<PlayerState> getPlayers() {
        return new ArrayList<>(players.values());
    }

    public synchronized PlayerState getMyPlayer() {
        return me;
    }

    public synchronized void addBullet(BulletState b) {
        bullets.add(b);
    }

    public synchronized void clearBullets() {
        bullets.clear();
    }

    public synchronized List<BulletState> getBullets() {
        return new ArrayList<>(bullets);
    }

}