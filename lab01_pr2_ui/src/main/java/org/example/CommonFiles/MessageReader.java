package org.example.CommonFiles;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class MessageReader {

    private final DataInputStream in;

    public MessageReader(byte[] data, int length) {
        this.in = new DataInputStream(
                new ByteArrayInputStream(data, 0, length)
        );
    }

    public byte readByte() throws IOException {
        return in.readByte();
    }

    public int readInt() throws IOException {
        return in.readInt();
    }

    public long readLong() throws IOException {
        return in.readLong();
    }

    public float readFloat() throws IOException {
        return in.readFloat();
    }

    public String readString() throws IOException {
        int len = in.readInt();
        byte[] buf = new byte[len];
        in.readFully(buf);
        return new String(buf);
    }
}