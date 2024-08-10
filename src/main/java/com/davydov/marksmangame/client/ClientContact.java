package com.davydov.marksmangame.client;

import com.davydov.marksmangame.server.Leader;
import com.davydov.marksmangame.server.ServerModel;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientContact extends Thread {
    public ClientModel clientModel;
    public ServerModel serverModel;

    public DataOutputStream dataToServer;
    public DataInputStream dataFromServer;

    public Gson gson = new Gson();

    public ClientContact(ClientModel clientModel, Socket clientSocket) throws IOException {
        this.clientModel = clientModel;
        this.dataToServer = new DataOutputStream(clientSocket.getOutputStream());
        this.dataFromServer = new DataInputStream(clientSocket.getInputStream());
    }

    public void run() {
        try {
            while (true) {
                if (dataFromServer.available() > 0) {
                    serverModel = gson.fromJson(dataFromServer.readUTF(), ServerModel.class);
                    LeaderboardController.records = (ArrayList<Leader>) serverModel.leaders;
                }
                dataToServer.flush();
                dataToServer.writeUTF(gson.toJson(clientModel, ClientModel.class));

                clientModel.isArrowFlying = false;

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        } catch (IOException e) {
        }
    }
}
