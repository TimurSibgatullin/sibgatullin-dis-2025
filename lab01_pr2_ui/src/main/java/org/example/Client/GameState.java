package org.example.Client;
import org.example.CommonFiles.PlayerStatsLevels;
import org.example.Server.Player;

import java.util.*;

public class GameState {

    private Map<Integer, PlayerState> players = new HashMap<>();
    private List<BulletState> bullets = new ArrayList<>();
    private final Object bulletsLock = new Object();
    Map<Integer, OrbState> orbs = new HashMap<>();
    private final Object orbsLock = new Object();
    protected PlayerState me = new PlayerState();
    private final Object playersLock = new Object();
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

    public Collection<OrbState> getOrbs() {
        synchronized (orbsLock) {
            return new ArrayList<>(orbs.values());
        }
    }

    public void setPlayers(Map<Integer, PlayerState> newPlayers) {
        synchronized (playersLock) {
            this.players = new HashMap<>(newPlayers);
        }
    }

    public Collection<PlayerState> getPlayers() {
        synchronized (playersLock) {
            return new ArrayList<>(players.values());
        }
    }

    public PlayerState getMyPlayer() {
        synchronized (playersLock) {
            return me;
        }
    }

    public List<BulletState> getBullets() {
        synchronized (bulletsLock) {
            return new ArrayList<>(bullets);
        }
    }

    public void addBullets(ArrayList<BulletState> newBullets) {
        synchronized (bulletsLock) {
            this.bullets = new ArrayList<>(newBullets);
        }
    }
}