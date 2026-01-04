package org.example.Client;
import org.example.CommonFiles.PlayerStatsLevels;
import org.example.Server.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameState {

    private final Map<Integer, PlayerState> players = new HashMap<>();
    private final List<BulletState> bullets = new ArrayList<>();
    Map<Integer, OrbState> orbs = new ConcurrentHashMap<>();
    private final Object orbsLock = new Object();
    protected PlayerState me = new PlayerState();
    public PlayerStatsLevels statsLevels = new PlayerStatsLevels();
    public boolean isOverlayVisible = true;

    public void setMyId(int id) {
        this.me.id = id;
    }

    public void setMe(PlayerState me) {
        this.me = me;
    }

    public void updateOrbs(Map<Integer, OrbState> newOrbs) {
        synchronized (orbsLock) {
            this.orbs = new HashMap<>(newOrbs);
        }
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