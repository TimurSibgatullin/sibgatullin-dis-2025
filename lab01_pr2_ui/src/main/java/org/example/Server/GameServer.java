package org.example.Server;

import org.example.CommonFiles.*;
import org.example.Server.database.StatsRepository;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GameServer {

    public static final int WORLD_SIZE = 3000;

    DatagramSocket socket;
    int port = 50000;

    Map<Integer, Player> players = new ConcurrentHashMap<>();

    List<Bullet> bullets = new CopyOnWriteArrayList<>();
    Map<Integer, Orb> orbs = new ConcurrentHashMap<>();
    StatsRepository stats = new StatsRepository();

    int nextId = 1;
    AtomicInteger orbIdGen = new AtomicInteger();

    public GameServer() throws Exception {
        socket = new DatagramSocket(port);
        System.out.println("Server started on port " + port);
        spawnInitialOrbs(300);

        new Thread(this::receiveLoop).start();
        new Thread(this::updateLoop).start();
    }


    private void receiveLoop() {
        byte[] buf = new byte[32768];
        while (true) {
            try {
                DatagramPacket p = new DatagramPacket(buf, buf.length);
                socket.receive(p);

                MessageReader r = new MessageReader(p.getData(), p.getLength());
                Protocol type = Protocol.fromByte(r.readByte());

                if (type == Protocol.CONNECT) {
                    int id = nextId++;
                    Player player = new Player(id, p.getAddress(), p.getPort());
                    players.put(id, player);
                    MessageWriter w = new MessageWriter();
                    w.writeByte(Protocol.CONNECT.code);
                    w.writeInt(player.id);
                    send(w, player.address, player.port);
                    sendTopList(player);
                }
                if (type == Protocol.START_WITH_NAME) {
                    Player player = players.get(r.readInt());
                    if (player.lifetime > 0) {
                        player.resetPlayer();
                    }
                    String nick = r.readString();
                    player.nickname = nick;
                    player.isActive = true;
                    player.spawnTime = System.currentTimeMillis();
                }

                if (type == Protocol.INPUT) {
                    int id = r.readInt();
                    float dx = r.readFloat();
                    float dy = r.readFloat();
                    float angle = r.readFloat();

                    Player player = players.get(id);
                    if (player != null) {
                        if (dx > 0) {
                            player.xa = 1;
                        } else if (dx < 0) {
                            player.xa = -1;
                        } else {
                            player.xa = 0;
                        }
                        if (dy > 0) {
                            player.ya = 1;
                        } else if (dy < 0) {
                            player.ya = -1;
                        } else {
                            player.ya = 0;
                        }
                        player.angle = angle;
                    }
                }
                if (type == Protocol.SHOOT) {
                    int id = r.readInt();
                    Player pl = players.get(id);
                    if (pl.isActive) {
                        byte isShooting = r.readByte();
                        if (pl != null & isShooting != 0)
                            pl.shooting = true;
                        else {
                            pl.shooting = false;
                        }
                    }
                }

                if (type == Protocol.UPGRADE_STATS) {
                    int id = r.readInt();
                    Player pl = players.get(id);
                    pl.statsLevels.healing = r.readInt();
                    pl.statsLevels.damage = r.readInt();
                    pl.statsLevels.defense = r.readInt();
                    pl.statsLevels.fireRate = r.readInt();
                    pl.statsLevels.speed = r.readInt();
                    pl.statsLevels.bulletSpeed = r.readInt();
                    pl.stats.updateStats();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateLoop() {
        while (true) {
            try {
                long now = System.currentTimeMillis();
                for (Player p : players.values()) {
                    if (p != null) {
                        int x = p.x + (int) (p.xa * p.stats.speed);
                        if (0 < x & x < WORLD_SIZE) {
                            p.x = x;
                        }
                        int y = p.y + (int) (p.ya * p.stats.speed);
                        if (0 < y & y < WORLD_SIZE) {
                            p.y = y;
                        }
                    }
                }

                for (Player p : players.values()) {
                    if (p.isActive) {
                        if (p.shooting) {
                            if (now - p.lastShotTime >= p.stats.fireRate) {
                                p.lastShotTime = now;
                                bullets.add(new Bullet(p, (float) (p.x + Math.cos(p.angle) * 22), (float) (p.y + Math.sin(p.angle) * 22), p.angle));
                            }
                        }
                        if (p.hp < p.stats.maxHp) {
                            if (now - p.lastHealTime >= 1000) {
                                p.lastHealTime = now;
                                if (p.hp + p.stats.healing <= p.stats.maxHp) {
                                    p.hp += p.stats.healing;
                                } else {
                                    p.hp = (int) p.stats.maxHp;
                                }
                            }
                        }
                    }
                }

                List<Bullet> bulletsToRemove = new ArrayList<>();
                for (Bullet b : bullets) {
                    if (b.update()) {
                        float dx = b.vx;
                        float dy = b.vy;

                        float dist = (float) Math.sqrt(dx * dx + dy * dy);
                        int steps = Math.max(1, (int)(dist / 3f));

                        float stepX = dx / steps;
                        float stepY = dy / steps;

                        boolean removed = false;

                        for (int i = 0; i < steps; i++) {
                            float nx = b.x + stepX;
                            float ny = b.y + stepY;

                            for (Orb orb : orbs.values()) {
                                float dxo = nx - orb.x;
                                float dyo = ny - orb.y;

                                if (dxo * dxo + dyo * dyo <= orb.radius * orb.radius) {
                                    orb.hp -= (int) players.get(b.ownerId).stats.damage;
                                    bulletsToRemove.add(b);
                                    removed = true;

                                    if (orb.hp <= 0) {
                                        Player owner = players.get(b.ownerId);
                                        if (owner != null) {
                                            owner.xp += orb.exp;
                                            calculateLvl(owner);
                                            owner.orbs++;
                                        }
                                        orbs.remove(orb.id);
                                    }
                                    break;
                                }
                            }

                            if (removed) break;

                            for (Player player : players.values()) {
                                if (!player.isActive || player.id == b.ownerId)
                                    continue;

                                float dxp = nx - player.x;
                                float dyp = ny - player.y;

                                if (dxp * dxp + dyp * dyp <= 15 * 15) {
                                    if (players.get(b.ownerId).stats.damage > player.stats.defense) {
                                        player.hp = (int) (player.hp - players.get(b.ownerId).stats.damage + player.stats.defense);
                                    }
                                    if (player.hp <= 0) {
                                        Player killer = players.get(b.ownerId);
                                        killer.xp += (int)(player.xp * 0.9f);
                                        calculateLvl(killer);
                                        killer.kills++;

                                        long lifetime = (System.currentTimeMillis() - player.spawnTime) / 1000;
                                        stats.save(player.nickname, player.xp, lifetime, player.kills, player.orbs);

                                        player.isActive = false;
                                        player.lifetime = lifetime;
                                        sendDeadMessage(player.id);
                                    }

                                    bulletsToRemove.add(b);
                                    removed = true;
                                    break;
                                }
                            }

                            if (removed) break;
                            b.x = nx;
                            b.y = ny;
                        }
                    } else {
                        bulletsToRemove.add(b);
                    }
                }
                bullets.removeAll(bulletsToRemove);
                if (orbs.size() < 270) {
                    spawnInitialOrbs(30);
                }


                for (Player player : players.values()) {
                    MessageWriter w = new MessageWriter();
                    w.writeByte(Protocol.WORLD_SNAPSHOT.code);

                    w.writeInt(players.size() );
                    for (Player ps : players.values()) {
                        w.writeInt(ps.id);
                        if (ps.nickname != null) {
                            w.writeString(ps.nickname);
                        } else {
                            w.writeString("Tank.io");
                        }
                        w.writeFloat(ps.x);
                        w.writeFloat(ps.y);
                        w.writeFloat(ps.angle);
                        w.writeInt((int) ps.stats.maxHp);
                        w.writeInt(ps.hp);
                        w.writeInt(ps.xp);
                        w.writeInt(ps.lvl);
                        if (ps.isActive) {
                            w.writeByte(1);
                        } else {
                            w.writeByte(0);
                        }
                    }

                    w.writeInt(bullets.size());
                    for (Bullet b : bullets) {
                        w.writeInt(b.ownerId);
                        w.writeFloat(b.x);
                        w.writeFloat(b.y);
                        w.writeFloat(b.angle);
                    }

                    w.writeInt(orbs.size());
                    for (Orb o : orbs.values()) {
                        w.writeInt(o.id);
                        w.writeFloat(o.x);
                        w.writeFloat(o.y);
                        w.writeInt(o.radius);
                        w.writeInt(o.hp);
                        w.writeInt(o.color.getRed());
                        w.writeInt(o.color.getGreen());
                        w.writeInt(o.color.getBlue());
                    }

                    send(w, player.address, player.port);
                }
                Thread.sleep(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void calculateLvl(Player player) {
        int xp = player.xp;
        int lvl = 0;
        while (xp >= xpToNext(lvl)) {
            xp -= xpToNext(lvl);
            lvl++;
        }

        player.lvl = lvl;
    }

    int xpToNext(int level) {
        return (int) (6.5 * Math.pow(level, 1.85) + 4);
    }

    private void spawnInitialOrbs(int k) {
        Random r = new Random();

        for (int i = 0; i < k; i++) {
            OrbType type = OrbType.values()[r.nextInt(4)];

            float x = r.nextInt(WORLD_SIZE);
            float y = r.nextInt(WORLD_SIZE);

            int id = orbIdGen.incrementAndGet();

            orbs.put(id, new Orb(
                    id,
                    x,
                    y,
                    type.radius,
                    type.hp,
                    type.exp,
                    type.color
            ));
        }
    }

    private void sendTopList(Player p) {
        try {
            List<Statistics> top = stats.top(10);

            MessageWriter w = new MessageWriter();
            w.writeByte(Protocol.TOP_LIST.code);
            w.writeInt(top.size());

            for (Statistics s : top) {
                w.writeString(s.nickname);
                w.writeString(s.date);
                w.writeInt(s.level);
                w.writeLong(s.lifetime);
                w.writeInt(s.kills);
                w.writeInt(s.orbs);
            }
            send(w, p.address, p.port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendDeadMessage(int id) {
        try {
            MessageWriter w = new MessageWriter();
            w.writeByte(Protocol.PLAYER_DIED.code);
            Player player = players.get(id);
            w.writeString(player.nickname);
            w.writeInt(player.xp);
            w.writeLong(player.lifetime);
            w.writeInt(player.kills);
            w.writeInt(player.orbs);
            send(w, player.address, player.port);
            sendTopList(player);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    boolean segmentCircle(
            float x1, float y1,
            float x2, float y2,
            float cx, float cy,
            float r
    ) {
        float dx = x2 - x1;
        float dy = y2 - y1;

        float fx = x1 - cx;
        float fy = y1 - cy;

        float a = dx*dx + dy*dy;
        float b = 2 * (fx*dx + fy*dy);
        float c = fx*fx + fy*fy - r*r;

        float discriminant = b*b - 4*a*c;
        return discriminant >= 0;
    }
    private void send(MessageWriter w, InetAddress addr, int port) throws Exception {
        byte[] data = w.toBytes();
        socket.send(new DatagramPacket(data, data.length, addr, port));
    }

    public static void main(String[] args) throws Exception {
        new GameServer();
    }
}
