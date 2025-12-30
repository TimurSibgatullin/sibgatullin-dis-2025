package org.example.CommonFiles;

public enum Protocol {
    CONNECT(0),
    INPUT(1),
    WORLD_SNAPSHOT(2),
    SHOOT(3),
    UPGRADE_STATS(4),
    UPGRADE_WEAPON(5),
    START_WITH_NAME(6),
    TOP_LIST(7),
    PLAYER_DIED(8),
    DISCONNECT(9);

    public final byte code;

    Protocol(int code) {
        this.code = (byte) code;
    }

    public static Protocol fromByte(byte b) {
        for (Protocol p : values()) {
            if (p.code == b)
                return p;
        }
        return null;
    }
}
