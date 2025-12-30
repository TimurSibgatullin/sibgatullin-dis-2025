package org.example.Server;

import org.example.CommonFiles.MessageWriter;

import java.net.InetAddress;

public class Command {
    MessageWriter messageWriter;
    InetAddress address;
    int port;

    public Command(MessageWriter messageWriter, InetAddress address, int port) {
        this.messageWriter = messageWriter;
        this.address = address;
        this.port = port;
    }
}
