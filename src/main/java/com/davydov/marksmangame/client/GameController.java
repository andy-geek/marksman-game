package com.davydov.marksmangame.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class GameController {
    public Client client;

    @FXML
    public AnchorPane mainPanel;
    @FXML
    public Circle aimOne;
    @FXML
    public Circle aimTwo;
    @FXML
    public HBox arrowOne;
    @FXML
    public HBox arrowTwo;
    @FXML
    public HBox arrowThree;
    @FXML
    public HBox arrowFour;

    @FXML
    public AnchorPane connectPanel;
    @FXML
    public TextField nameField;
    @FXML
    public Button btnConnect;

    @FXML
    public AnchorPane buttonPanel;
    @FXML
    public Button btnReady;
    @FXML
    public Button btnShot;
    @FXML
    public Button btnStop;
    @FXML
    public Button btnLeaderboard;

    @FXML
    public AnchorPane winnerPanel;
    @FXML
    public Label winnerName;
    @FXML
    public Button btnPlayAgain;

    @FXML
    public AnchorPane playerPanelOne;
    @FXML
    public Label playerNameOne;
    @FXML
    public Label playerScoreOne;
    @FXML
    public Label playerShotsOne;
    @FXML
    public Label playerWinsOne;

    @FXML
    public AnchorPane playerPanelTwo;
    @FXML
    public Label playerNameTwo;
    @FXML
    public Label playerScoreTwo;
    @FXML
    public Label playerShotsTwo;
    @FXML
    public Label playerWinsTwo;

    @FXML
    public AnchorPane playerPanelThree;
    @FXML
    public Label playerNameThree;
    @FXML
    public Label playerScoreThree;
    @FXML
    public Label playerShotsThree;
    @FXML
    public Label playerWinsThree;

    @FXML
    public AnchorPane playerPanelFour;
    @FXML
    public Label playerNameFour;
    @FXML
    public Label playerScoreFour;
    @FXML
    public Label playerShotsFour;
    @FXML
    public Label playerWinsFour;

    @FXML
    protected void onConnect() throws IOException {
        String playerName = nameField.getText();
        client.connect(playerName);
        connectPanel.setVisible(false);
        buttonPanel.setVisible(true);
    }

    @FXML
    protected void onReady() {
        client.clientModel.isReady = true;
    }

    @FXML
    protected void onShot() {
        client.clientModel.isArrowFlying = true;
    }

    @FXML
    protected void onStop() {
        client.clientModel.isReady = false;
    }

    @FXML
    protected void onLeaderboard() throws IOException {
        Parent root = new FXMLLoader(Client.class.getResource("LeaderboardWindow.fxml")).load();
        Stage stage = new Stage();
        stage.setTitle("Leaderboard");
        stage.setScene(new Scene(root, 400, 400));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    protected void onPlayAgain() {
        winnerPanel.setVisible(false);
        buttonPanel.setVisible(true);
    }
}
