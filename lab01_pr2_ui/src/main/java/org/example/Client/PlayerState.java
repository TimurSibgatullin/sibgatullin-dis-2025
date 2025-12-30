package org.example.Client;

public class PlayerState {
    public String nickname;
    public int id = -1;
    public float x, y;
    public float angle = 0f;
    public int maxHp;
    public int hp;
    public int lvl;
    public int xp;
    public boolean isActive;

    public PlayerState() {
    }

    public void setId(int id) {
        this.id = id;
    }
}