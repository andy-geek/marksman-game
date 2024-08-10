package com.davydov.marksmangame.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {
    public DataOutputStream dataToClient;
    public DataInputStream dataFromClient;

    public Connection(Socket clientSocket) throws IOException {
        this.dataToClient = new DataOutputStream(clientSocket.getOutputStream());
        this.dataFromClient = new DataInputStream(clientSocket.getInputStream());
    }
}
