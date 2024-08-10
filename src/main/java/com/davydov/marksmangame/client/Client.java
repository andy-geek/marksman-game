package com.davydov.marksmangame.client;

import com.davydov.marksmangame.server.ServerModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Application {
    int port = 3124;
    public GameController gameController;
    public ClientContact clientContact;
    public ClientModel clientModel = new ClientModel();

    public void connect(String playerName) throws IOException {
        try {
            InetAddress address = InetAddress.getLocalHost();
            Socket socket = new Socket(address, port);

            clientContact = new ClientContact(clientModel, socket);
            clientContact.start();

            clientModel.playerName = playerName;

            new Thread(() -> {
                while (true) {
                    if (clientContact != null) {
                        if (clientContact.serverModel != null) {
                            Platform.runLater(() -> {
                                updateNames(clientContact.serverModel);

                                if (clientContact.serverModel.gameIsRunning) {
                                    updateAims(clientContact.serverModel);
                                    updateArrows(clientContact.serverModel);

                                    updateScores(clientContact.serverModel);
                                    updateShots(clientContact.serverModel);
                                    updateWins(clientContact.serverModel);

                                    updateWinnerPanel(clientContact.serverModel);
                                }
                            });
                        }
                    }
                    try {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e) {
                    }
                }
            }).start();
        }
        catch (IOException e) {
        }
    }

    public void updateAims(ServerModel serverMsg) {
        gameController.aimOne.setLayoutY(serverMsg.aimOneY);
        gameController.aimTwo.setLayoutY(serverMsg.aimTwoY);
    }

    public void updateArrows(ServerModel serverMsg) {
        for (int i = 0; i < serverMsg.arrowsFired.size(); i++) {
            if (i == 0) {
                gameController.arrowOne.setVisible(serverMsg.arrowsFired.get(i));
            }
            if (i == 1) {
                gameController.arrowTwo.setVisible(serverMsg.arrowsFired.get(i));
            }
            if (i == 2) {
                gameController.arrowThree.setVisible(serverMsg.arrowsFired.get(i));
            }
            if (i == 3) {
                gameController.arrowFour.setVisible(serverMsg.arrowsFired.get(i));
            }
        }

        for (int i = 0; i < serverMsg.arrowsX.size(); i++) {
            if (i == 0) {
                gameController.arrowOne.setLayoutX(serverMsg.arrowsX.get(i));
            }
            if (i == 1) {
                gameController.arrowTwo.setLayoutX(serverMsg.arrowsX.get(i));
            }
            if (i == 2) {
                gameController.arrowThree.setLayoutX(serverMsg.arrowsX.get(i));
            }
            if (i == 3) {
                gameController.arrowFour.setLayoutX(serverMsg.arrowsX.get(i));
            }
        }
    }

    public void updateNames(ServerModel serverMsg) {
        for (int i = 0; i < serverMsg.playerNames.size(); i++) {
            if (i == 0) {
                gameController.playerNameOne.setText(serverMsg.playerNames.get(i));
                gameController.playerPanelOne.setVisible(true);
            }
            if (i == 1) {
                gameController.playerNameTwo.setText(serverMsg.playerNames.get(i));
                gameController.playerPanelTwo.setVisible(true);
            }
            if (i == 2) {
                gameController.playerNameThree.setText(serverMsg.playerNames.get(i));
                gameController.playerPanelThree.setVisible(true);
            }
            if (i == 3) {
                gameController.playerNameFour.setText(serverMsg.playerNames.get(i));
                gameController.playerPanelFour.setVisible(true);
            }
        }
    }

    public void updateScores(ServerModel serverMsg) {
        for (int i = 0; i < serverMsg.playerScores.size(); i++) {
            if (i == 0) {
                gameController.playerScoreOne.setText("Score: " + serverMsg.playerScores.get(i));
            }
            if (i == 1) {
                gameController.playerScoreTwo.setText("Score: " + serverMsg.playerScores.get(i));
            }
            if (i == 2) {
                gameController.playerScoreThree.setText("Score: " + serverMsg.playerScores.get(i));
            }
            if (i == 3) {
                gameController.playerScoreFour.setText("Score: " + serverMsg.playerScores.get(i));
            }
        }
    }

    public void updateShots(ServerModel serverMsg) {
        for (int i = 0; i < serverMsg.playerShots.size(); i++) {
            if (i == 0) {
                gameController.playerShotsOne.setText("Shots: " + serverMsg.playerShots.get(i));
            }
            if (i == 1) {
                gameController.playerShotsTwo.setText("Shots: " + serverMsg.playerShots.get(i));
            }
            if (i == 2) {
                gameController.playerShotsThree.setText("Shots: " + serverMsg.playerShots.get(i));
            }
            if (i == 3) {
                gameController.playerShotsFour.setText("Shots: " + serverMsg.playerShots.get(i));
            }
        }
    }

    public void updateWins(ServerModel serverMsg) {
        for (int i = 0; i < serverMsg.playerWins.size(); i++) {
            if (i == 0) {
                gameController.playerWinsOne.setText("Wins: " + serverMsg.playerWins.get(i));
            }
            if (i == 1) {
                gameController.playerWinsTwo.setText("Wins: " + serverMsg.playerWins.get(i));
            }
            if (i == 2) {
                gameController.playerWinsThree.setText("Wins: " + serverMsg.playerWins.get(i));
            }
            if (i == 3) {
                gameController.playerWinsFour.setText("Wins: " + serverMsg.playerWins.get(i));
            }
        }
    }

    public void updateWinnerPanel(ServerModel serverMsg) {
        if (serverMsg.winnerName != null) {
            gameController.buttonPanel.setVisible(false);
            gameController.winnerPanel.setVisible(true);
            gameController.winnerName.setText("Winner: " + serverMsg.winnerName);
            clientModel.isReady = false;
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("GameWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 550);
        stage.setTitle("The Marksman Game");
        stage.setScene(scene);
        stage.show();

        gameController = fxmlLoader.getController();
        gameController.client = this;
    }

    public static void main() {
        launch();
    }
}
