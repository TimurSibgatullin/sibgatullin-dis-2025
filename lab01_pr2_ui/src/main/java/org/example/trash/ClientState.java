package org.example.trash;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientState {

    public int myId = -1;

    public float myX;
    public float myY;
    public float myAngle;

    public Map<Integer, RemotePlayer> players = new ConcurrentHashMap<>();
}