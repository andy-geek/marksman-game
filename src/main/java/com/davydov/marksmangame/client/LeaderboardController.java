package com.davydov.marksmangame.client;

import com.davydov.marksmangame.server.Leader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class LeaderboardController {
    public static ArrayList<Leader> records;

    public static void setRecords(ArrayList<Leader> rds) {
        records = rds;
    }

    @FXML
    public TableView<Leader> tableView;

    @FXML
    public void initialize() {
        tableView.setPlaceholder(new Label("No content in table"));

        TableColumn<Leader, String> columnOne = new TableColumn<>("Player");
        columnOne.setCellValueFactory(new PropertyValueFactory<>("playerName"));

        TableColumn<Leader, String> columnTwo = new TableColumn<>("Wins");
        columnTwo.setCellValueFactory(new PropertyValueFactory<>("playerWins"));

        tableView.getColumns().add(columnOne);
        tableView.getColumns().add(columnTwo);

        tableView.getItems().addAll(records);
    }
}
