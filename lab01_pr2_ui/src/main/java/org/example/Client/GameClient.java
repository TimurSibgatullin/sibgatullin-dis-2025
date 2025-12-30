package org.example.Client;


import org.example.Client.display.GameFrame;
import org.example.Client.display.GamePanel;
import org.example.CommonFiles.*;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingQueue;

public class GameClient {

    DatagramSocket socket;
    InetAddress server;
    int port = 50000;


    InputController input;
    public GameState state = new GameState();
    GamePanel panel;
    GameFrame frame;

    private final LinkedBlockingQueue<MessageWriter> commandQueue = new LinkedBlockingQueue<>();

    public GameClient() throws Exception {
        socket = new DatagramSocket();
        server = InetAddress.getByName("localhost");

        panel = new GamePanel(state);
        frame = new GameFrame(this, panel);

        input = new InputController(this);
        panel.addKeyListener(input);
        panel.addMouseListener(input);
        panel.addMouseMotionListener(input);

        sendConnect();

        new Thread(this::receiveLoop).start();
        new Thread(this::commandLoop).start();
        new Thread(this::inputLoop).start();

        new Timer(16, e -> panel.repaint()).start();
    }

    private void sendConnect() throws Exception {
        MessageWriter w = new MessageWriter();
        w.writeByte(Protocol.CONNECT.code);
        commandQueue.add(w);
    }

    private void commandLoop() {
        while (true) {
            try {
                MessageWriter w = commandQueue.poll();
                if (w != null)
                    send(w);
                Thread.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void inputLoop() {
        while (true){
            try {
                float dx = 0, dy = 0;
                if (input.up) dy -= 1;
                if (input.down) dy += 1;
                if (input.left) dx -= 1;
                if (input.right) dx += 1;
                if (state.statsLevels.getSum() < state.me.lvl) {
                    if (input.one | input.two | input.three | input.four | input.five | input.six) {
                        if (input.one){
                            if (state.statsLevels.healing < 10) {
                                state.statsLevels.healing += 1;
                                input.one = false;
                            }
                        }
                        if (input.two) {
                            if (state.statsLevels.damage < 10) {
                                state.statsLevels.damage += 1;
                                input.two = false;
                            }
                        }
                        if (input.three) {
                            if (state.statsLevels.defense < 10) {
                                state.statsLevels.defense += 1;
                                input.three = false;
                            }
                        }
                        if (input.four) {
                            if (state.statsLevels.fireRate < 10) {
                                state.statsLevels.fireRate += 1;
                                input.four = false;
                            }
                        }
                        if (input.five) {
                            if (state.statsLevels.speed < 10) {
                                state.statsLevels.speed += 1;
                                input.five = false;
                            }
                        }
                        if (input.six) {
                            if (state.statsLevels.bulletSpeed < 10) {
                                state.statsLevels.bulletSpeed += 1;
                                input.six = false;
                            }
                        }
                        MessageWriter w = new MessageWriter();
                        w.writeByte(Protocol.UPGRADE_STATS.code);
                        w.writeInt(state.getMyPlayer().id);
                        w.writeInt(state.statsLevels.healing);
                        w.writeInt(state.statsLevels.damage);
                        w.writeInt(state.statsLevels.defense);
                        w.writeInt(state.statsLevels.fireRate);
                        w.writeInt(state.statsLevels.speed);
                        w.writeInt(state.statsLevels.bulletSpeed);
                        commandQueue.add(w);
                    }
                }

                MessageWriter w = new MessageWriter();
                w.writeByte(Protocol.INPUT.code);
                w.writeInt(state.getMyPlayer().id);
                w.writeFloat(dx);
                w.writeFloat(dy);
                w.writeFloat(state.getMyPlayer().angle);
                commandQueue.add(w);
                Thread.sleep(20);
            } catch (Exception e) {}
        }
    }

    public void updateMouse(int x, int y) {
        int cx = panel.getWidth() / 2;
        int cy = panel.getHeight() / 2;
        float mx = x - cx;
        float my = y - cy;

        state.getMyPlayer().angle = (float) Math.atan2(my, mx);
    }

    private void receiveLoop() {
        byte[] buf = new byte[32768];
        while (true) {
            try {
                DatagramPacket p = new DatagramPacket(buf, buf.length);
                socket.receive(p);

                MessageReader r = new MessageReader(p.getData(), p.getLength());
                Protocol type = Protocol.fromByte(r.readByte());

                if (type == Protocol.WORLD_SNAPSHOT) {
                    state.clearPlayers();
                    state.clearBullets();

                    int playerCount = r.readInt();
                    for (int i = 0; i < playerCount; i++) {
                        PlayerState ps = new PlayerState();
                        ps.id = r.readInt();
                        ps.nickname = r.readString();
                        ps.x = r.readFloat();
                        ps.y = r.readFloat();
                        ps.angle = r.readFloat();
                        ps.maxHp = r.readInt();
                        ps.hp = r.readInt();
                        ps.xp = r.readInt();
                        ps.lvl = r.readInt();
                        if (r.readByte() == 1) {
                            ps.isActive = true;
                        } else {
                            ps.isActive = false;
                        }
                        if (ps.id == state.getMyPlayer().id) {
                            state.setMe(ps);
                        }
                        state.setPlayer(ps);
                    }

                    int bulletCount = r.readInt();
                    for (int i = 0; i < bulletCount; i++) {
                        BulletState b = new BulletState(
                                r.readInt(),
                                r.readFloat(),
                                r.readFloat(),
                                r.readFloat()
                        );
                        state.addBullet(b);
                    }

                    int orbCount = r.readInt();
                    state.clearOrbs();
                    for (int i = 0; i < orbCount; i++) {
                        OrbState o = new OrbState();
                        o.id = r.readInt();
                        o.x = r.readFloat();
                        o.y = r.readFloat();
                        o.radius = r.readInt();
                        o.hp = r.readInt();
                        o.color = new Color(r.readInt(), r.readInt(), r.readInt());
                        state.addOrb(o);
                    }
                }

                if (type == Protocol.CONNECT) {;
                    state.setMyId(r.readInt());
                }

                if (type == Protocol.TOP_LIST) {
                    int n = r.readInt();
                    List<Statistics> list = new ArrayList<>();

                    for (int i = 0; i < n; i++) {
                        list.add(new Statistics(
                                r.readString(),
                                r.readString(),
                                r.readInt(),
                                r.readLong(),
                                r.readInt(),
                                r.readInt()
                        ));
                    }
                    frame.getOverlay().updateTop(list);
                }

                if (type == Protocol.PLAYER_DIED) {
                    Statistics ps = new Statistics(
                            r.readString(),
                                r.readInt(),
                                r.readLong(),
                                r.readInt(),
                                r.readInt()
                        );
                    frame.showOverlay(ps);
                }

            } catch (Exception e) {
            }
        }
    }

    public void sendShootStart() {
        try {
            MessageWriter w = new MessageWriter();
            w.writeByte(Protocol.SHOOT.code);
            w.writeInt(state.getMyPlayer().id);
            w.writeByte(1);
            commandQueue.add(w);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendShootStop() {
        try {
            MessageWriter w = new MessageWriter();
            w.writeByte(Protocol.SHOOT.code);
            w.writeInt(state.getMyPlayer().id);
            w.writeByte(0);
            commandQueue.add(w);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send(MessageWriter w) throws Exception {
        byte[] data = w.toBytes();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(data.length);
        dos.write(data);

        socket.send(new DatagramPacket(data, data.length, server, port));
    }

    public static String timeFormat(long seconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedTime = sdf.format(new Date(seconds * 1000));
        return formattedTime;
    }

    public void startWithName(String text) {
        try {
            MessageWriter w = new MessageWriter();
            w.writeByte(Protocol.START_WITH_NAME.code);
            w.writeInt(state.getMyPlayer().id);
            w.writeString(text);
            commandQueue.add(w);
            state.statsLevels = new PlayerStatsLevels();
            frame.hideOverlay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}