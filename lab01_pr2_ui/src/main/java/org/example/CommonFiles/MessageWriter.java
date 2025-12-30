package org.example.CommonFiles;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MessageWriter {

    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    private final DataOutputStream out = new DataOutputStream(bos);

    public void writeByte(int v) throws IOException {
        out.writeByte(v);
    }

    public void writeInt(int v) throws IOException {
        out.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        out.writeLong(v);
    }

    public void writeFloat(float v) throws IOException {
        out.writeFloat(v);
    }

    public void writeString(String s) throws IOException {
        byte[] data = s.getBytes();
        out.writeInt(data.length);
        out.write(data);
    }

    public byte[] toBytes() {
        return bos.toByteArray();
    }
}
