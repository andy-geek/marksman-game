package com.davydov.marksmangame.server;

import com.davydov.marksmangame.client.ClientModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class ServerContact extends Thread {
    public Server server;
    public Gson gson = new Gson();
    public ArrayList<Connection> connections = new ArrayList<>();

    public ServerContact(Server server) throws IOException {
        this.server = server;
    }

    public void run() {
        try {
            while (true) {
                for (Connection connection : connections) {
                    if (connection.dataFromClient.available() > 0) {
                        var clientModel = gson.fromJson(connection.dataFromClient.readUTF(), ClientModel.class);

                        if (server.clientModels.size() == connections.indexOf(connection)) {
                            server.clientModels.add(clientModel);
                        }
                        else {
                            server.clientModels.set(connections.indexOf(connection), clientModel);
                        }
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        } catch (IOException e) {
        }
    }

    public void sendMsg() throws IOException {
        for (Connection connection : connections) {
            connection.dataToClient.flush();
            connection.dataToClient.writeUTF(gson.toJson(server.serverModel));
        }
    }
}
