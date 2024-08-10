package com.davydov.marksmangame.server;

import com.davydov.marksmangame.client.ClientModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    int port = 3124;
    public ServerContact serverContact;
    public ServerModel serverModel = new ServerModel();
    public ArrayList<ClientModel> clientModels = new ArrayList<>();

    double mainPanelWidth = 700.0;
    double mainPanelHeight = 500.0;

    int winScore = 3;

    double aimOneRadius = 50.0;
    double aimTwoRadius = 25.0;
    double aimOneX = 520.0;
    double aimTwoX = 650.0;
    double aimOneSpeed = 0.75;
    double aimTwoSpeed = 1.5;

    double arrowStartX = 50.0;
    double arrowErrX = 80.0;
    double arrowSpeed = 7.5;
    double[] arrowsY =  new double[] {185.0, 295.0, 75.0, 405.0};

    DBh dbh = new DBh();

    public void startServer() throws IOException {
        serverModel.leaders = dbh.getAllRecords();

        ServerSocket serverSocket = new ServerSocket(port);
        new Thread(() -> {
            try {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    Connection connection = new Connection(clientSocket);

                    if (serverContact == null) {
                        serverContact = new ServerContact(this);
                        serverContact.start();
                    }
                    serverContact.connections.add(connection);
                }
            } catch (IOException e) {
            }
        }).start();

        while (true) {
            updateServerModel();
            if (serverModel.gameIsRunning) {
                moveAims();
                moveArrows();
                checkHits();
            }

            if (serverContact != null) {
                serverContact.sendMsg();
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
    }

    public void restartGame() {
        serverModel.aimOneY = mainPanelHeight / 2.0;
        serverModel.aimTwoY = mainPanelHeight / 2.0;

        for (int i = 0; i < serverModel.playerNames.size(); i++) {
            serverModel.playerScores.set(i, 0);
            serverModel.playerShots.set(i, 0);
        }

        serverModel.winnerName = null;
    }

    public void moveAims() {
        serverModel.aimOneY += aimOneSpeed;
        serverModel.aimTwoY += aimTwoSpeed;

        if (serverModel.aimOneY <= aimOneRadius || serverModel.aimOneY >= mainPanelHeight - aimOneRadius) {
            aimOneSpeed *= -1;
        }
        if (serverModel.aimTwoY <= aimTwoRadius || serverModel.aimTwoY >= mainPanelHeight - aimTwoRadius) {
            aimTwoSpeed *= -1;
        }
    }

    public void moveArrows() {
        for (int i = 0; i < serverModel.arrowsFired.size(); i++) {
            if (serverModel.arrowsFired.get(i)) {
                double arrowX = serverModel.arrowsX.get(i) + arrowSpeed;
                if (arrowX + arrowErrX >= mainPanelWidth) {
                    serverModel.arrowsFired.set(i, false);
                    arrowX = arrowStartX;
                }
                serverModel.arrowsX.set(i, arrowX);
            }
        }
    }

    public void checkHits() {
        for (int i = 0; i < serverModel.arrowsX.size(); i++) {
            double arrowX = serverModel.arrowsX.get(i);
            int score;

            if (hitCalculation(arrowX, arrowsY[i], aimOneX, serverModel.aimOneY, aimOneRadius)) {
                serverModel.arrowsX.set(i, arrowStartX);
                serverModel.arrowsFired.set(i, false);

                score = serverModel.playerScores.get(i) + 1;
                serverModel.playerScores.set(i, score);

                if (score >= winScore) {
                    serverModel.winnerName = serverModel.playerNames.get(i);
                    serverModel.playerWins.set(i, serverModel.playerWins.get(i) + 1);
                    dbh.addOrUpdateRec(new Leader(serverModel.winnerName, serverModel.playerWins.get(i)));
                    serverModel.leaders = dbh.getAllRecords();
                }
            }
            else if (hitCalculation(arrowX, arrowsY[i], aimTwoX, serverModel.aimTwoY, aimTwoRadius)) {
                serverModel.arrowsX.set(i, arrowStartX);
                serverModel.arrowsFired.set(i, false);

                score = serverModel.playerScores.get(i) + 2;
                serverModel.playerScores.set(i, score);

                if (score >= winScore) {
                    serverModel.winnerName = serverModel.playerNames.get(i);
                    serverModel.playerWins.set(i, serverModel.playerWins.get(i) + 1);
                    dbh.addOrUpdateRec(new Leader(serverModel.winnerName, serverModel.playerWins.get(i)));
                    serverModel.leaders = dbh.getAllRecords();
                }
            }
        }
    }

    public boolean hitCalculation(double arrowX, double arrowY, double aimX, double aimY, double aimRadius) {
        return Math.pow((arrowX + arrowErrX - aimX), 2) + Math.pow((arrowY - aimY), 2) <= Math.pow(aimRadius, 2);
    }

    public void updateServerModel() {
        boolean allPlayersReady = clientModels.size() != 0;

        for (int i = 0; i < clientModels.size(); i++) {
            ClientModel clientModel = clientModels.get(i);
            if (clientModel == null) {
                continue;
            }

            if (serverModel.playerNames.size() == i) {
                if (serverModel.playerNames.contains(clientModel.playerName)) {
                    serverModel.playerNames.add(clientModel.playerName + i);
                }
                else {
                    serverModel.playerNames.add(clientModel.playerName);
                }

                serverModel.playersReady.add(clientModel.isReady);
                serverModel.playerScores.add(0);
                serverModel.playerShots.add(0);

                int playerWins = 0;
                for (Leader leader : serverModel.leaders) {
                    if (clientModel.playerName.equals(leader.playerName)) {
                        playerWins = leader.playerWins;
                    }
                }
                serverModel.playerWins.add(playerWins); // TO DO

                serverModel.arrowsX.add(arrowStartX);
                serverModel.arrowsFired.add(false);
            }
            else {
                serverModel.playersReady.set(i, clientModel.isReady);
                if (clientModel.isArrowFlying) {
                    if (!serverModel.arrowsFired.get(i)) {
                        serverModel.playerShots.set(i, serverModel.playerShots.get(i) + 1);
                    }
                    serverModel.arrowsFired.set(i, true);
                }
            }

            if (!clientModel.isReady) {
                allPlayersReady = false;
            }
        }

        if (!allPlayersReady && serverModel.winnerName != null) {
            restartGame();
        }

        serverModel.gameIsRunning = allPlayersReady;
    }

    public static void main(String[] args) throws IOException {
        new Server().startServer();
    }
}
